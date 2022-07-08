package ua.com.foxminded.sql_jdbc_school.service.impl;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    private static final int FIRST_ELEMENT = 0;
    private static final int SECOND_ELEMENT = 1;
    private static final double ONE_HALF = 0.5;
    private static final double DOUBLE_AMPL_PROBABILITY = 10.0;
    private static final double DOUBLE_PROBABILITY_VALUE = 4.0;
    private static final String CREATE_WHIOUT_ID_ERROR = "Creating courses failed.";
    private static final String ERROR_DELETE_STUDENTS = "The service of students deletion failed.";
    private static final String ERROR_GET_STUDENTS_OF_COURSE = "Getting students of the course failed.";
    private static final String ERROR_ADD_STUDENT_TO_COURSE_BY_ID = "Adding the student to the course failed.";
    private static final String ERROR_GET_COURSES_OF_STUDENT = "Getting courses of the student "
            + "from the database failed.";
    private static final String ERROR_GET_ALL = "Getting all of the students from the database failed.";
    private static final String ERROR_ADD_STUDENT_TO_COURSE = "The studen has not been added to the course.";
    private static final String ERROR_CREATE_STUDENT_COURSE_RELATION = "The relation creation failed.";
    private static final String ASSIGN_ID_AND_ADD_TO_DATABASE_ERROR = "The assigning id and adding students "
            + "to database operations failed.";
    private static final String ERROR_ASSIGN_GROUP = "The assining group to students failed.";
    private static final String ERROR_ADD_STUDENT = "The student adding to the database failed.";
    private static final String ERROR_GET_ALL_STUDENT = "Getting students from the database failed.";
    private static final String ERROR_DELETE_STUDENT = "The deletion of the student from the database failed.";
    private static final String ERROR_GET_STUDENTS_WITH_GROUP = "Getting students that have group ID failed.";
   
    private final Reader reader;
    private final StudentDAO studentDao;
    private final CourseDAO courseDao;
    
    public StudentServiceImpl(Reader reader, 
                              StudentDAO studentDao, 
                              CourseDAO courseDao) {
        this.reader = reader;
        this.studentDao = studentDao;
        this.courseDao = courseDao;
    }

    @Override
    public int deleteAll() throws ServiceException {
        int status = 0;
        
        try {
            status = studentDao.deleteAll();
            return status;
        } catch (DAOException e) {
            LOGGER.error(ERROR_DELETE_STUDENTS, e);
            throw new ServiceException(ERROR_DELETE_STUDENTS, e);
        } 
    }

    @Override
    public List<StudentModel> getStudentsOfCourseById(int courseId) throws ServiceException {
        try {
            CourseEntity course = courseDao.getCourseById(courseId);
            return  studentDao.getStudensOfCourseById(courseId)
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
    public int addStudentToCourseById(int studentId, int courseId) throws ServiceException {
        int status;

        try {
            StudentEntity studentHavingCourse = studentDao.getStudentOfCourseById(studentId, courseId);

            if (studentHavingCourse != null) {
                status = BAD_STATUS;
            } else {
                StudentEntity student = studentDao.getStudentById(studentId);
                CourseEntity course = courseDao.getCourseById(courseId);

                if ((student == null) || (course == null)) {
                    status = BAD_STATUS;
                } else {
                    status = studentDao.addStudentToCourse(student, course);
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
            List<StudentEntity> studentsHavingCourse = studentDao.getAllStudentsHavingCouse();

            studentsHavingCourse.stream().forEach((student) -> {

                try {
                    courseDao.getCoursesOfStudentById(student.getStudentId())
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
        } catch (RuntimeException | DAOException e) {
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
                    studentDao.addStudentToCourse(
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
            return studentDao.getAllStudentsHavingGroupId().stream()
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
    public int deleteStudentById(int studentId) throws ServiceException {
        try {
            return studentDao.deleteStudentById(studentId);
        } catch (DAOException e) {
            LOGGER.error(ERROR_DELETE_STUDENT, e);
            throw new ServiceException(ERROR_DELETE_STUDENT, e);
        } 
    }

    @Override
    public List<StudentModel> getAllStudents() throws ServiceException {
        try {
            return studentDao.getAll()
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
    public int createStudent(String lastName, String firstName) throws ServiceException {
        try {
            List<StudentModel> studentModelList = new ArrayList<>();
            studentModelList.add(new StudentModel(firstName, lastName));
            List<StudentEntity> studentEntityList = studentModelList.stream()
                    .map((model) -> new StudentEntity(model.getFirstName(), 
                                                      model.getLastName()))
                    .collect(Collectors.toList());
            return studentDao.insert(studentEntityList);
        } catch (DAOException e) {
            LOGGER.error(ERROR_ADD_STUDENT, e);
            throw new ServiceException(ERROR_ADD_STUDENT, e);
        } 
    }

    @Override
    public List<StudentModel> assignGroupIdToStudent(List<GroupModel> groups) throws ServiceException {
        try {
            List<StudentEntity> studentEntities = studentDao.getAll();
            List<Integer> groupSizeList = generateStudentQuantiyOfGroup(studentEntities.size(), groups.size());
            List<StudentEntity> studentsHavingGroupId = new ArrayList<>();
            AtomicInteger atomicInteger = new AtomicInteger();
            IntStream.range(0, groupSizeList.size())
                     .forEach((groupIndex) -> IntStream.range(0, groupSizeList.get(groupIndex))
                             .forEach((index) -> {
                                 int studentIndex = atomicInteger.getAndIncrement();
                                 studentsHavingGroupId.add(
                                         new StudentEntity(studentEntities.get(studentIndex).getStudentId(),
                                                           groups.get(groupIndex).getGroupId(), 
                                                           studentEntities.get(studentIndex).getFirstName(),
                                                           studentEntities.get(studentIndex).getLastName()));
                             }));
            studentDao.update(studentsHavingGroupId);
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
    public List<StudentModel> assignIdAndAddToDatabase(List<StudentModel> students) throws ServiceException {
        try {
            List<StudentEntity> studentEntities = students.parallelStream()
                    .map((studentDTO) -> new StudentEntity(studentDTO.getFirstName(), 
                                                           studentDTO.getLastName()))
                    .collect(Collectors.toList());
            studentDao.insert(studentEntities);
            return studentDao.getAll().parallelStream()
                                      .map((entity) -> new StudentModel(entity.getStudentId(),
                                                                        entity.getGroupId(),
                                                                        entity.getFirstName(),
                                                                        entity.getLastName()))
                                      .collect(Collectors.toList());
        } catch (DAOException e) {
            LOGGER.error(ASSIGN_ID_AND_ADD_TO_DATABASE_ERROR, e);
            throw new ServiceException(ASSIGN_ID_AND_ADD_TO_DATABASE_ERROR, e);
        }
    }

    @Override
    public List<StudentModel> createWithoutId(String firstNameListFilename, 
                                              String lastNameListFilename) throws ServiceException {
        try {
            URL firstNameFileUrl = StudentServiceImpl.class.getClassLoader()
                                                           .getResource(firstNameListFilename);
            Path firstNameFilePath = Paths.get(firstNameFileUrl.toURI());
            URL lastNameFileUrl = StudentServiceImpl.class.getClassLoader()
                                                          .getResource(lastNameListFilename);
            Path lastNameFilePath = Paths.get(lastNameFileUrl.toURI());
            List<String> firstNames = reader.read(firstNameFilePath);
            List<String> lastNames = reader.read(lastNameFilePath);
            return generateStudents(firstNames, lastNames);
        } catch (URISyntaxException e) {
            LOGGER.error(CREATE_WHIOUT_ID_ERROR, e);
            throw new ServiceException(CREATE_WHIOUT_ID_ERROR, e);
        }
    }
    
    private List<StudentModel> generateStudents(List<String> firstNames, List<String> lastNames) {
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
                studentsHavingGroupId.size(), 
                courses.size());

        try (Stream<List<Integer>> indexRelationStream = studentCourseIndexRelation.stream()) {
            return indexRelationStream
                    .map((indexRelation) -> {
                        int studentIndex = indexRelation.get(FIRST_ELEMENT);
                        int courseIndex = indexRelation.get(SECOND_ELEMENT);
                        return new StudentModel(studentsHavingGroupId.get(studentIndex).getStudentId(),
                                                studentsHavingGroupId.get(studentIndex).getGroupId(),
                                                studentsHavingGroupId.get(studentIndex).getFirstName(),
                                                studentsHavingGroupId.get(studentIndex).getLastName(),
                                                courses.get(courseIndex).getCourseId(),
                                                courses.get(courseIndex).getCourseName(),
                                                courses.get(courseIndex).getCourseDescription());
                    })
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
