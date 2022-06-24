package ua.com.foxminded.sql_jdbc_school.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.com.foxminded.sql_jdbc_school.dao.CourseDAO;
import ua.com.foxminded.sql_jdbc_school.dao.DAOConnectionPool;
import ua.com.foxminded.sql_jdbc_school.dao.DAOException;
import ua.com.foxminded.sql_jdbc_school.dao.StudentDAO;
import ua.com.foxminded.sql_jdbc_school.dto.CourseDto;
import ua.com.foxminded.sql_jdbc_school.dto.GroupDto;
import ua.com.foxminded.sql_jdbc_school.dto.StudentDto;
import ua.com.foxminded.sql_jdbc_school.entity.CourseEntity;
import ua.com.foxminded.sql_jdbc_school.entity.StudentEntity;
import ua.com.foxminded.sql_jdbc_school.service.Generator;
import ua.com.foxminded.sql_jdbc_school.service.Reader;
import ua.com.foxminded.sql_jdbc_school.service.ServiceException;
import ua.com.foxminded.sql_jdbc_school.service.StudentService;

public class StudentServiceImpl implements StudentService<List<StudentDto>, 
                                                          List<GroupDto>, 
                                                          String, 
                                                          Integer, 
                                                          List<CourseDto>> {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final String CLOSE_CONNECTION_POOL_ERROR = "The closing opreation of connections "
            + "in the pool failed.";
    private static final String ERROR_DELETE_STUDENTS = "The service of students deletion failed.";
    private static final String ERROR_GET_STUDENTS_OF_COURSE = "Getting students of the course failed.";
    private static final String ERROR_ADD_STUDENT_TO_COURSE_BY_ID = "Adding the student to the course failed.";
    private static final int BAD_STATUS = 0;
    private static final String ERROR_GET_COURSES_OF_STUDENT = "Getting courses of the student "
            + "from the database failed.";
    private static final String ERROR_GET_ALL = "Getting all of the students from the database failed.";
    private static final String ERROR_ADD_STUDENT_TO_COURSE = "The studen has not been added to the course.";
    private static final int STUDENT_INDEX = 0;
    private static final int COURSE_INDEX = 1;
    private static final String ERROR_CREATE_STUDENT_COURSE_RELATION = "The relation creation failed.";
    private static final String FIST_NAME_FILENAME = "firstNameList.txt";
    private static final String LAST_NAME_FILENAME = "lastNameList.txt";
    private static final String ERROR_CREATE_STUDENTS = "The student addition service to the database failed.";
    private static final String ERROR_ASSIGN_GROUP = "The assining group to students failed.";
    private static final String ERROR_ADD_STUDENT = "The student adding to the database failed.";
    private static final String ERROR_GET_ALL_STUDENT = "Getting students from the database failed.";
    private static final String ERROR_DELETE_STUDENT = "The deletion of the student from the database failed.";
    private static final String ERROR_GET_STUDENTS_WITH_GROUP = "Getting students that have group ID failed.";
   
    private final Reader reader;
    private final Generator generator;
    private final StudentDAO studentDAO;
    private final CourseDAO courseDAO;
    private final DAOConnectionPool connectionPool;


    
    public StudentServiceImpl(Reader reader, 
                              Generator generator, 
                              StudentDAO studentDAO, 
                              CourseDAO courseDAO,
                              DAOConnectionPool connectionPool) {
        this.reader = reader;
        this.generator = generator;
        this.studentDAO = studentDAO;
        this.courseDAO = courseDAO;
        this.connectionPool = connectionPool;
    }

    @Override
    public Integer deleteAllStudents() throws ServiceException {
        int status = 0;
        
        try {
            status = studentDAO.deleteAll();
            return status;
        } catch (DAOException e) {
            LOGGER.error(ERROR_DELETE_STUDENTS, e);
            throw new ServiceException(ERROR_DELETE_STUDENTS, e);
        } finally {
            closeConnectionPool();
        }
    }

    @Override
    public List<StudentDto> getStudentsOfCourseById(Integer courseId) throws ServiceException {
        try {
            return studentDAO.getStudensOfCourseById(courseId).parallelStream()
                    .map((entity) -> new StudentDto(entity.getStudentId(), 
                                                    entity.getGroupId(), 
                                                    entity.getFirstName(),
                                                    entity.getLastName()))
                    .collect(Collectors.toList());
        } catch (DAOException e) {
            LOGGER.error(ERROR_GET_STUDENTS_OF_COURSE, e);
            throw new ServiceException(ERROR_GET_STUDENTS_OF_COURSE, e);
        } finally {
            closeConnectionPool();
        }
    }

    @Override
    public Integer addStudentToCourseById(Integer studentId, Integer courseId) throws ServiceException {
        int status;

        try {
            StudentEntity studentHavingCourse = studentDAO.getStudentOfCourseById(studentId, courseId);

            if (studentHavingCourse != null) {
                status = BAD_STATUS;
            } else {
                StudentEntity student = studentDAO.getStudentById(studentId);
                CourseEntity course = courseDAO.getCourseById(courseId);

                if ((student == null) || (course == null)) {
                    status = BAD_STATUS;
                } else {
                    status = studentDAO.addStudentToCourse(student, course);
                }
            }
            return status;
        } catch (DAOException e) {
            LOGGER.error(ERROR_ADD_STUDENT_TO_COURSE_BY_ID, e);
            throw new ServiceException(ERROR_ADD_STUDENT_TO_COURSE_BY_ID, e);
        } finally {
            closeConnectionPool();
        }
    }

    @Override
    public List<StudentDto> getAllStudentsHavingCourse() throws ServiceException {
        List<StudentDto> studentCourseRelation = new ArrayList<>();

        try {
            List<StudentEntity> studentsHavingCourse = studentDAO.getAllStudentsHavingCouse();

            studentsHavingCourse.stream().forEach((student) -> {

                try {
                    courseDAO.getCoursesOfStudentById(student.getStudentId())
                             .stream()
                             .forEach((course) -> studentCourseRelation.add(new StudentDto(
                                     student.getStudentId(),
                                     student.getGroupId(), 
                                     student.getFirstName(), 
                                     student.getLastName(),
                                     course.getCourseId(), 
                                     course.getCourseName(), 
                                     course.getCourseDescription())));
                } catch (DAOException e) {
                    LOGGER.error(ERROR_GET_COURSES_OF_STUDENT, e);
                    throw new RuntimeException(ERROR_GET_COURSES_OF_STUDENT, e);
                }
            });
            return studentCourseRelation;
        } catch (DAOException e) {
            LOGGER.error(ERROR_GET_ALL, e);
            throw new ServiceException(ERROR_GET_ALL, e);
        } finally {
            closeConnectionPool();
        }
    }

    @Override
    public List<StudentDto> assignStudentToCourse(List<StudentDto> studentsHavingGroupId, 
                                                  List<CourseDto> courses) throws ServiceException {

        List<StudentDto> studentsHavingCourseId = generateStudentCourseRelation(studentsHavingGroupId, 
                                                                                courses);

        try {
            studentsHavingCourseId.stream().forEach((student) -> {
                try {
                    studentDAO.addStudentToCourse(
                            new StudentEntity(student.getStudentId(), 
                                              student.getGroupId(), 
                                              student.getFirstName(),
                                              student.getLastName()),
                            new CourseEntity(student.getCourseId(), 
                                             student.getCourseName(),
                                             student.getCourseDescription()));
                } catch (DAOException e) {
                    LOGGER.error(ERROR_ADD_STUDENT_TO_COURSE, e);
                    throw new RuntimeException(ERROR_ADD_STUDENT_TO_COURSE, e);
                }
            });
            return studentsHavingCourseId;
        } catch (RuntimeException e) {
            LOGGER.error(ERROR_CREATE_STUDENT_COURSE_RELATION, e);
            throw new ServiceException(ERROR_CREATE_STUDENT_COURSE_RELATION, e);
        } finally {
            closeConnectionPool();
        }
    }

    @Override
    public List<StudentDto> getStudentsHavingGroupId() throws ServiceException {

        try {
            return studentDAO.getStudentsHavingGroupId().stream()
                    .map((studentEntity) -> new StudentDto(studentEntity.getStudentId(), 
                                                           studentEntity.getGroupId(),
                                                           studentEntity.getFirstName(), 
                                                           studentEntity.getLastName()))
                    .collect(Collectors.toList());
        } catch (DAOException e) {
            LOGGER.error(ERROR_GET_STUDENTS_WITH_GROUP, e);
            throw new ServiceException(ERROR_GET_STUDENTS_WITH_GROUP, e);
        } finally {
            closeConnectionPool();
        }
    }

    @Override
    public Integer deleteStudent(Integer studentId) throws ServiceException {
        try {
            return studentDAO.deleteById(studentId);
        } catch (DAOException e) {
            LOGGER.error(ERROR_DELETE_STUDENT, e);
            throw new ServiceException(ERROR_DELETE_STUDENT, e);
        } finally {
            closeConnectionPool();
        }
    }

    @Override
    public List<StudentDto> getAllStudents() throws ServiceException {
        try {
            return studentDAO.getAll()
                             .stream()
                             .map((studentEntity) -> new StudentDto(studentEntity.getStudentId(),
                                                                    studentEntity.getGroupId(), 
                                                                    studentEntity.getFirstName(), 
                                                                    studentEntity.getLastName()))
                             .collect(Collectors.toList());
        } catch (DAOException e) {
            LOGGER.error(ERROR_GET_ALL_STUDENT, e);
            throw new ServiceException(ERROR_GET_ALL_STUDENT, e);
        } finally {
            closeConnectionPool();
        }
    }

    @Override
    public Integer addStudent(String lastName, String firstName) throws ServiceException {
        try {
            List<StudentDto> studentDTOs = new ArrayList<>();
            studentDTOs.add(new StudentDto(firstName, lastName));
            List<StudentEntity> studentEntities = studentDTOs.stream()
                    .map((studentDTO) -> new StudentEntity(studentDTO.getFirstName(), 
                                                           studentDTO.getLastName()))
                    .collect(Collectors.toList());
            return studentDAO.insert(studentEntities);
        } catch (DAOException e) {
            LOGGER.error(ERROR_ADD_STUDENT, e);
            throw new ServiceException(ERROR_ADD_STUDENT, e);
        } finally {
            closeConnectionPool();
        }
    }

    @Override
    public List<StudentDto> assignGroup(List<GroupDto> groups) throws ServiceException {
        try {
            List<StudentEntity> studentEntities = studentDAO.getAll();
            List<Integer> groupSize = generator.getNumberOfStudentsInGroup(studentEntities.size(), 
                                                                           groups.size());
            List<StudentEntity> studentsHavingGroupId = new ArrayList<>();
            AtomicInteger atomicInteger = new AtomicInteger();
            IntStream.range(0, groupSize.size())
                     .forEach((groupIndex) -> IntStream.range(0, groupSize.get(groupIndex))
                             .forEach((index) -> {
                                 int studentIndex = atomicInteger.getAndIncrement();
                                 studentsHavingGroupId.add(
                                         new StudentEntity(studentEntities.get(studentIndex).getStudentId(),
                                                           groups.get(groupIndex).getGroupId(), 
                                                           studentEntities.get(studentIndex).getFirstName(),
                                                           studentEntities.get(studentIndex).getLastName()));
                             }));
            studentDAO.update(studentsHavingGroupId);
            return studentsHavingGroupId.stream()
                                        .map((studentEntity) -> new StudentDto(studentEntity.getStudentId(),
                                                                               studentEntity.getGroupId(), 
                                                                               studentEntity.getFirstName(), 
                                                                               studentEntity.getLastName()))
                                        .collect(Collectors.toList());
        } catch (DAOException e) {
            LOGGER.error(ERROR_ASSIGN_GROUP, e);
            throw new ServiceException(ERROR_ASSIGN_GROUP, e);
        } finally {
            closeConnectionPool();
        }
    }

    @Override
    public List<StudentDto> createStudents() throws ServiceException {
        try {
            List<String> firstNames = reader.read(FIST_NAME_FILENAME);
            List<String> lastNames = reader.read(LAST_NAME_FILENAME);
            List<StudentDto> studentDTOs = generator.getStudentData(firstNames, lastNames);
            List<StudentEntity> studentEntities = studentDTOs.stream()
                    .map((studentDTO) -> new StudentEntity(studentDTO.getFirstName(), 
                                                           studentDTO.getLastName()))
                    .collect(Collectors.toList());
            studentDAO.insert(studentEntities);
            return studentDTOs;
        } catch (ServiceException | DAOException e) {
            LOGGER.error(ERROR_CREATE_STUDENTS, e);
            throw new ServiceException(ERROR_CREATE_STUDENTS, e);
        } finally {
            closeConnectionPool();
        }
    }

    private List<StudentDto> generateStudentCourseRelation(List<StudentDto> studentsHavingGroupId,
                                                           List<CourseDto> courses) {
        List<List<Integer>> studentCourseIndexRelation = generator
                .getStudentCourseIndexRelation(studentsHavingGroupId.size(), courses.size());

        try (Stream<List<Integer>> indexRelationStream = studentCourseIndexRelation.stream()) {
            return indexRelationStream
                    .map((indexRelation) -> new StudentDto(
                            studentsHavingGroupId.get(indexRelation.get(STUDENT_INDEX)).getStudentId(),
                            studentsHavingGroupId.get(indexRelation.get(STUDENT_INDEX)).getGroupId(),
                            studentsHavingGroupId.get(indexRelation.get(STUDENT_INDEX)).getFirstName(),
                            studentsHavingGroupId.get(indexRelation.get(STUDENT_INDEX)).getLastName(),
                            courses.get(indexRelation.get(COURSE_INDEX)).getCourseId(),
                            courses.get(indexRelation.get(COURSE_INDEX)).getCourseName(),
                            courses.get(indexRelation.get(COURSE_INDEX)).getCourseDescription()))
                    .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        }
    } 
    
    private void closeConnectionPool() {
        try {
            connectionPool.closeConnections();
        } catch (DAOException e) {
            LOGGER.error(CLOSE_CONNECTION_POOL_ERROR, e);
        } 
    }
}
