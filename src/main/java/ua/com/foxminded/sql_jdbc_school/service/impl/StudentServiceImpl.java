package ua.com.foxminded.sql_jdbc_school.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.com.foxminded.sql_jdbc_school.dao.CourseDAO;
import ua.com.foxminded.sql_jdbc_school.dao.DAOException;
import ua.com.foxminded.sql_jdbc_school.dao.StudentDAO;
import ua.com.foxminded.sql_jdbc_school.entity.CourseEntity;
import ua.com.foxminded.sql_jdbc_school.entity.StudentEntity;
import ua.com.foxminded.sql_jdbc_school.model.CourseModel;
import ua.com.foxminded.sql_jdbc_school.model.GroupModel;
import ua.com.foxminded.sql_jdbc_school.model.StudentModel;
import ua.com.foxminded.sql_jdbc_school.service.Reader;
import ua.com.foxminded.sql_jdbc_school.service.ServiceException;
import ua.com.foxminded.sql_jdbc_school.service.StudentService;

public class StudentServiceImpl implements StudentService {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final int CROURSE_AMPLITUDE = 3;
    private static final int MIN_NUMBER_OF_CROURSES = 1;
    private static final int ONE_STEP = 1;
    private static final int MIN_STUDENTS = 10;
    private static final int MAX_STUDENTS = 30;
    private static final int AMPLITUDE = 20;
    private static final int INT_AMPL_PROBABILITY = 10;
    private static final int STUDENTS_WITHOUT_GROUP = 21;
    private static final int INT_PROBABILITY_VALUE = 3;
    private static final int ONE_STUDENT = 1;
    private static final int ZERO_STUDENTS = 0;
    private static final int BAD_STATUS = 0;
    private static final int STUDENT_INDEX = 0;
    private static final int COURSE_INDEX = 1;
    private static final double ONE_HALF = 0.5;
    private static final double DOUBLE_AMPL_PROBABILITY = 10.0;
    private static final double DOUBLE_PROBABILITY_VALUE = 4.0;
    private static final String ERROR_DELETE_STUDENTS = "The service of students deletion failed.";
    private static final String ERROR_GET_STUDENTS_OF_COURSE = "Getting students of the course failed.";
    private static final String ERROR_ADD_STUDENT_TO_COURSE_BY_ID = "Adding the student to the course failed.";
    private static final String ERROR_GET_COURSES_OF_STUDENT = "Getting courses of the student "
            + "from the database failed.";
    private static final String ERROR_GET_ALL = "Getting all of the students from the database failed.";
    private static final String ERROR_ADD_STUDENT_TO_COURSE = "The studen has not been added to the course.";
    private static final String ERROR_CREATE_STUDENT_COURSE_RELATION = "The relation creation failed.";
    private static final String FIST_NAME_FILENAME = "student-first-names.txt";
    private static final String LAST_NAME_FILENAME = "student-last-names.txt";
    private static final String ERROR_CREATE_STUDENTS = "The student addition service to the database failed.";
    private static final String ERROR_ASSIGN_GROUP = "The assining group to students failed.";
    private static final String ERROR_ADD_STUDENT = "The student adding to the database failed.";
    private static final String ERROR_GET_ALL_STUDENT = "Getting students from the database failed.";
    private static final String ERROR_DELETE_STUDENT = "The deletion of the student from the database failed.";
    private static final String ERROR_GET_STUDENTS_WITH_GROUP = "Getting students that have group ID failed.";
   
    private final Reader reader;
    private final StudentDAO studentDAO;
    private final CourseDAO courseDAO;
    
    public StudentServiceImpl(Reader reader, 
                              StudentDAO studentDAO, 
                              CourseDAO courseDAO) {
        this.reader = reader;
        this.studentDAO = studentDAO;
        this.courseDAO = courseDAO;
    }

    @Override
    public Integer deleteAll() throws ServiceException {
        int status = 0;
        
        try {
            status = studentDAO.deleteAll();
            return status;
        } catch (DAOException e) {
            LOGGER.error(ERROR_DELETE_STUDENTS, e);
            throw new ServiceException(ERROR_DELETE_STUDENTS, e);
        } 
    }

    @Override
    public List<StudentModel> getStudentsOfCourseById(Integer courseId) throws ServiceException {
        try {
           
            CourseEntity course = courseDAO.getCourseById(courseId);
            return  studentDAO.getStudensOfCourseById(courseId)
                              .parallelStream()
                              .map((entity) -> new StudentModel(entity.getStudentId(), 
                                                                entity.getGroupId(), 
                                                                entity.getFirstName(),
                                                                entity.getLastName(),
                                                                course.getCourseId(),
                                                                course.getCourseName(),
                                                                course.getCourseDescription()))
                              .collect(Collectors.toList());
        } catch (DAOException e) {
            LOGGER.error(ERROR_GET_STUDENTS_OF_COURSE, e);
            throw new ServiceException(ERROR_GET_STUDENTS_OF_COURSE, e);
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
        } 
    }

    @Override
    public List<StudentModel> getAllStudentsHavingCourse() throws ServiceException {
        List<StudentModel> studentCourseRelation = new ArrayList<>();

        try {
            List<StudentEntity> studentsHavingCourse = studentDAO.getAllStudentsHavingCouse();

            studentsHavingCourse.stream().forEach((student) -> {

                try {
                    courseDAO.getCoursesOfStudentById(student.getStudentId())
                             .stream()
                             .forEach((course) -> studentCourseRelation.add(new StudentModel(
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
        } 
    }

    @Override
    public List<StudentModel> assignCourseToStudent(List<StudentModel> studentsHavingGroupId, 
                                                    List<CourseModel> courses) throws ServiceException {

        List<StudentModel> studentsHavingCourseId = generateStudentCourseRelation(studentsHavingGroupId, 
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
        } 
    }

    @Override
    public List<StudentModel> getStudentsHavingGroupId() throws ServiceException {

        try {
            return studentDAO.getStudentsHavingGroupId().stream()
                    .map((studentEntity) -> new StudentModel(studentEntity.getStudentId(), 
                                                           studentEntity.getGroupId(),
                                                           studentEntity.getFirstName(), 
                                                           studentEntity.getLastName()))
                    .collect(Collectors.toList());
        } catch (DAOException e) {
            LOGGER.error(ERROR_GET_STUDENTS_WITH_GROUP, e);
            throw new ServiceException(ERROR_GET_STUDENTS_WITH_GROUP, e);
        } 
    }

    @Override
    public Integer deleteStudent(Integer studentId) throws ServiceException {
        try {
            return studentDAO.deleteById(studentId);
        } catch (DAOException e) {
            LOGGER.error(ERROR_DELETE_STUDENT, e);
            throw new ServiceException(ERROR_DELETE_STUDENT, e);
        } 
    }

    @Override
    public List<StudentModel> getAllStudents() throws ServiceException {
        try {
            return studentDAO.getAll()
                             .stream()
                             .map((studentEntity) -> new StudentModel(studentEntity.getStudentId(),
                                                                    studentEntity.getGroupId(), 
                                                                    studentEntity.getFirstName(), 
                                                                    studentEntity.getLastName()))
                             .collect(Collectors.toList());
        } catch (DAOException e) {
            LOGGER.error(ERROR_GET_ALL_STUDENT, e);
            throw new ServiceException(ERROR_GET_ALL_STUDENT, e);
        } 
    }

    @Override
    public Integer addStudent(String lastName, String firstName) throws ServiceException {
        try {
            List<StudentModel> studentDTOs = new ArrayList<>();
            studentDTOs.add(new StudentModel(firstName, lastName));
            List<StudentEntity> studentEntities = studentDTOs.stream()
                    .map((studentDTO) -> new StudentEntity(studentDTO.getFirstName(), 
                                                           studentDTO.getLastName()))
                    .collect(Collectors.toList());
            return studentDAO.insert(studentEntities);
        } catch (DAOException e) {
            LOGGER.error(ERROR_ADD_STUDENT, e);
            throw new ServiceException(ERROR_ADD_STUDENT, e);
        } 
    }

    @Override
    public List<StudentModel> assignGroupToStudent(List<GroupModel> groups) throws ServiceException {
        try {
            List<StudentEntity> studentEntities = studentDAO.getAll();
            List<Integer> groupSize = generateStudentQuantiyOfGroup(studentEntities.size(), groups.size());
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
                                        .map((studentEntity) -> new StudentModel(studentEntity.getStudentId(),
                                                                               studentEntity.getGroupId(), 
                                                                               studentEntity.getFirstName(), 
                                                                               studentEntity.getLastName()))
                                        .collect(Collectors.toList());
        } catch (DAOException e) {
            LOGGER.error(ERROR_ASSIGN_GROUP, e);
            throw new ServiceException(ERROR_ASSIGN_GROUP, e);
        } 
    }

    @Override
    public List<StudentModel> create() throws ServiceException {
        try {
            List<String> firstNames = reader.read(FIST_NAME_FILENAME);
            List<String> lastNames = reader.read(LAST_NAME_FILENAME);
            List<StudentModel> studentDTOs = generateStudents(firstNames, lastNames);
            List<StudentEntity> studentEntities = studentDTOs.stream()
                    .map((studentDTO) -> new StudentEntity(studentDTO.getFirstName(), 
                                                           studentDTO.getLastName()))
                    .collect(Collectors.toList());
            studentDAO.insert(studentEntities);
            return studentDTOs;
        } catch (ServiceException | DAOException e) {
            LOGGER.error(ERROR_CREATE_STUDENTS, e);
            throw new ServiceException(ERROR_CREATE_STUDENTS, e);
        } 
    }
    
    public List<StudentModel> generateStudents(List<String> firstNames, List<String> lastNames) {
        return Stream.generate(() -> new StudentModel(firstNames.get(new Random().nextInt(firstNames.size())),
                                                      lastNames.get(new Random().nextInt(lastNames.size()))))
                     .limit(200)
                     .collect(Collectors.toList());
    }
    
    private List<Integer> generateStudentQuantiyOfGroup(int studentsNumber, int groupsNumber) {
        List<Integer> result = new ArrayList<>();
        int noGroupStudents = new Random().nextInt(STUDENTS_WITHOUT_GROUP);
        int remainder = studentsNumber - noGroupStudents;
        
        for (int i = 0; i < groupsNumber; i++) {
            double probability = new Random().nextInt(INT_PROBABILITY_VALUE) / DOUBLE_PROBABILITY_VALUE;
            int zeroProbability = (int) ((probability + ONE_HALF) - (probability - ONE_HALF));
            double amplitudeProbability = new Random().nextInt(INT_AMPL_PROBABILITY) / DOUBLE_AMPL_PROBABILITY;
            int studentsInGroup = (int) (zeroProbability * (MIN_STUDENTS + AMPLITUDE * amplitudeProbability));
            
            if (studentsInGroup <= remainder && remainder != 0 || studentsInGroup == 0) {
                result.add(studentsInGroup);
                remainder -= studentsInGroup;
            } else if (studentsInGroup > remainder && remainder >= MIN_STUDENTS) {
                result.add(remainder);
                remainder = 0;
            } else if (remainder != 0) {
                for (int j = 0; j < result.size(); j++) {
                    if (result.get(j) < MAX_STUDENTS && result.get(j) >= MIN_STUDENTS && remainder != 0) {
                        result.set(j, result.get(j) + ONE_STUDENT);
                        remainder -= ONE_STUDENT;

                        if (j == (result.size() - ONE_STEP) && remainder > 0) {
                            j = 0;
                        } else if (remainder == 0) {
                            result.add(ZERO_STUDENTS);
                            break;
                        }
                    }
                }
            } else {
                result.add(ZERO_STUDENTS);
            }
        }
        return result;
    }

    private List<StudentModel> generateStudentCourseRelation(List<StudentModel> studentsHavingGroupId,
                                                             List<CourseModel> courses) {
        List<List<Integer>> studentCourseIndexRelation = generatetStudentCourseIndexRelation(
                studentsHavingGroupId.size(), courses.size());

        try (Stream<List<Integer>> indexRelationStream = studentCourseIndexRelation.stream()) {
            return indexRelationStream
                    .map((indexRelation) -> new StudentModel(
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
    
    private List<List<Integer>> generatetStudentCourseIndexRelation(int numberOfStudents, 
                                                                    int numberOfCourses) {
        Map<Integer, Integer> numberOfCoursesOfEachStudent = new HashMap<>();

        for (int i = 0; i < numberOfStudents; i++) {
            int numberOfCoursesPerStudent = MIN_NUMBER_OF_CROURSES + new Random().nextInt(CROURSE_AMPLITUDE);
            numberOfCoursesOfEachStudent.put(i, numberOfCoursesPerStudent);
        }

        List<List<Integer>> studentCourseIndexRelation = new ArrayList<>();

        for (int i = 0; i < numberOfStudents; i++) {
            List<Integer> cache = new ArrayList<>();

            for (int j = 0; j < numberOfCoursesOfEachStudent.get(i); j++) {
                int courseIndex = new Random().nextInt(numberOfCourses);
                List<Integer> studentAndCourseIndex = new ArrayList<>();

                if (!cache.contains(courseIndex)) {
                    cache.add(courseIndex);
                    studentAndCourseIndex.add(i);
                    studentAndCourseIndex.add(courseIndex);
                    studentCourseIndexRelation.add(studentAndCourseIndex);
                } else {
                    j -= ONE_STEP;
                }
            }
        }
        return studentCourseIndexRelation;
    }
}
