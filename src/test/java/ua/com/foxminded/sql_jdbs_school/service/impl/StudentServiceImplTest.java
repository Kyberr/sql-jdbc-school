package ua.com.foxminded.sql_jdbs_school.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

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
import ua.com.foxminded.sql_jdbc_school.service.impl.StudentServiceImpl;

@ExtendWith(MockitoExtension.class)
class StudentServiceImplTest {
    private static final int MIN_NUMBER_OF_STUDENT_COURSE_RELATIONS = 1;
    private static final int ONE_COURSE = 1;
    private static final int TEST_COURSE_ID = 1;
    private static final int TEST_STUDENT_ID = 1;
    private static final int MIN_COURSE_QUANTITY_OF_STUDENT = 1;
    private static final int MAX_COURSE_QUANTITY_OF_STUDENT = 3;
    private static final int MAX_STUDENT_QUANTITY_IN_GROUP = 30;
    private static final int MIN_STUDENT_QUANTITY_IN_GROUP = 10;
    private static final int STUDENT_QUANTITY = 200;
    private static final int ZERO = 0;
    private static final int ONE_STUDENT = 1;
    private static final int INITIAL_GROUP_ID = 1;
    private static final int NEXT_TO_LAST_STUDENT_ID = 195;
    private static final int NEXT_TO_LAST_COURSE_ID = 10;
    private static final int NEXT_TO_LAST_GROUP_ID = 11;
    private static final int INITIAL_COURSE_ID =  1;
    private static final int INITIAL_STUDENT_ID =  1;
    private static final int NAMES_QUANTITY = 20;
    private static final String FIRST_NAME_LIST_FILENAME = "student-first-names.txt";
    private static final String LAST_NAME_LIST_FILENAME = "student-last-names.txt";
    private static final String TEST_NAME = "SomeName";
    
    
    @InjectMocks
    StudentServiceImpl studentService;
    
    @Mock
    StudentDAO studentDaoMock;
    
    @Mock
    CourseDAO courseDaoMock;
    
    @Mock
    Reader readerMock;
    
    @Test
    void deleteAll() throws ServiceException, DAOException {
        studentService.deleteAll();
        verify(studentDaoMock, times(1)).deleteAll();
    }
    
    @Test
    void getStudentsOfCourseById_GettingCoursesAndStudentsFromDatabase_CorrectNumberOfCalls() 
            throws DAOException, ServiceException {
        studentService.getStudentsOfCourseById(TEST_COURSE_ID);
        InOrder inOrder = Mockito.inOrder(courseDaoMock, studentDaoMock);
        inOrder.verify(courseDaoMock, times(1)).getCourseById(anyInt());
        inOrder.verify(studentDaoMock, times(1)).getStudensOfCourseById(anyInt());
    }
    
    @Test
    void addStudentToCourseById_AddingStudentCourseRelationToDatabase_CorrectNumberAndOrderOfCalls() 
            throws ServiceException, DAOException {
        StudentEntity student = new StudentEntity();
        student.setStudentId(TEST_STUDENT_ID);
        when(studentDaoMock.getStudentById(anyInt())).thenReturn(student);
        
        CourseEntity course = new CourseEntity();
        course.setCourseId(TEST_COURSE_ID);
        when(courseDaoMock.getCourseById(anyInt())).thenReturn(course);
        
        studentService.addStudentToCourseById(TEST_STUDENT_ID, TEST_COURSE_ID);
        InOrder inOrder = Mockito.inOrder(studentDaoMock, courseDaoMock);
        inOrder.verify(studentDaoMock, times(1)).getStudentOfCourseById(anyInt(), anyInt());
        inOrder.verify(studentDaoMock, times(1)).getStudentById(anyInt());
        inOrder.verify(courseDaoMock, times(1)).getCourseById(anyInt());
        inOrder.verify(studentDaoMock, times(1)).addStudentToCourse(ArgumentMatchers.<StudentEntity>any(), 
                                                                    ArgumentMatchers.<CourseEntity>any());
    }
    
    @Test
    void getAllStudentsHavingCourse_GettingStudentsFromDatabase_CorrectNumberAndOrderOfCalls() 
            throws DAOException, ServiceException {
        StudentEntity studentEntity = new StudentEntity();
        studentEntity.setStudentId(TEST_STUDENT_ID);
        List<StudentEntity> studentEntityList = new ArrayList<>();
        studentEntityList.add(studentEntity);
        when(studentDaoMock.getAllStudentsHavingCouse()).thenReturn(studentEntityList);
        studentService.getAllStudentsHavingCourse();
        InOrder inOrder = Mockito.inOrder(studentDaoMock, courseDaoMock);
        inOrder.verify(studentDaoMock, times(1)).getAllStudentsHavingCouse();
        inOrder.verify(courseDaoMock, times(1)).getCoursesOfStudentById(anyInt());
    }
    
    @Test
    void assignCourseToStudent_AddingToDatabase_CorrectNumberOfCalls() throws ServiceException, DAOException {
        List<StudentModel> studentModelList = new ArrayList<>();
        IntStream.range(INITIAL_STUDENT_ID, NEXT_TO_LAST_STUDENT_ID)
                 .forEach((id) -> studentModelList.add(new StudentModel(id)));
        List<CourseModel> courseModelList = new ArrayList<>();
        IntStream.range(INITIAL_COURSE_ID, NEXT_TO_LAST_COURSE_ID)
                 .forEach((id) -> courseModelList.add(new CourseModel(id)));

        studentService.assignCourseToStudent(studentModelList, courseModelList);
        verify(studentDaoMock, atLeast(MIN_NUMBER_OF_STUDENT_COURSE_RELATIONS)).addStudentToCourse(
                ArgumentMatchers.<StudentEntity>any(),
                ArgumentMatchers.<CourseEntity>any());
    }
    
    @Test
    void assignCourseToStudent_AssigningCourse_CorrectNumberOfCoursesPerStudent() throws ServiceException {
        List<StudentModel> studentModelList = new ArrayList<>();
        IntStream.range(INITIAL_STUDENT_ID, NEXT_TO_LAST_STUDENT_ID)
                 .forEach((id) -> new StudentModel(id));
        List<CourseModel> courseModelList = new ArrayList<>();
        IntStream.range(INITIAL_COURSE_ID, NEXT_TO_LAST_COURSE_ID)
                 .forEach((id) -> new CourseModel(id));
        List<StudentModel> studentsHavingCourse = studentService.assignCourseToStudent(studentModelList, 
                                                                                       courseModelList);
        Map<Integer, Integer> courseQuantityOfStudent = new HashMap<>();
        studentsHavingCourse.stream().forEach((student) -> {
            if(!courseQuantityOfStudent.containsKey(student.getStudentId())) {
                int studentId = student.getStudentId();
                courseQuantityOfStudent.put(studentId, ONE_COURSE);
            } else {
                int studentId = student.getStudentId();
                courseQuantityOfStudent.replace(studentId, 
                                                courseQuantityOfStudent.get(studentId) + ONE_COURSE);
            }
        });
        
        courseQuantityOfStudent.entrySet().forEach((relation) -> {
            int courseQuantity = relation.getValue();
            assertTrue((courseQuantity >= MIN_COURSE_QUANTITY_OF_STUDENT) && 
                       (courseQuantity <= MAX_COURSE_QUANTITY_OF_STUDENT));
        });
    }
    @Test
    void getStudentsHavingGroupId_GettingStudentsFromDatabase_CorrectNumberOfCalls() throws ServiceException, 
                                                                                            DAOException {
        studentService.getStudentsHavingGroupId();
        verify(studentDaoMock, times(1)).getAllStudentsHavingGroupId();
    }
    
    @Test
    void deleteStudent_DeletionStudentFromDatabase_CorrectNumberOfCalls() throws ServiceException, 
                                                                                 DAOException {
        studentService.deleteStudentById(TEST_STUDENT_ID);
        verify(studentDaoMock, times(1)).deleteStudentById(ArgumentMatchers.anyInt());
    }
    
    @Test
    void getAllStudents_GettingStudentFromDatabase_CorrectNumberOfCalls() throws ServiceException, 
                                                                                 DAOException {
        studentService.getAllStudents();
        verify(studentDaoMock, times(1)).getAll();
    }
    
    @Test
    void createStudent_CreationStudentAndInsertionToDatabase_CorrectNumberOfCalls() throws DAOException, 
                                                                                           ServiceException {
        studentService.createStudent(TEST_NAME, TEST_NAME);
        verify(studentDaoMock, times(1)).insert(ArgumentMatchers.<StudentEntity>anyList());
    }

    @Test
    void assignGroupIdToStudent_AssigningGroupId_CorrectStudentQuantityInGroup() 
            throws ServiceException, DAOException {
        List<StudentEntity> studentEntities = new ArrayList<>();
        List<GroupModel> groupModels = new ArrayList<>();
        IntStream.range(ZERO, STUDENT_QUANTITY)
                 .forEachOrdered((e) -> studentEntities.add(new StudentEntity()));
        IntStream.range(INITIAL_GROUP_ID, NEXT_TO_LAST_GROUP_ID)
                 .forEach((groupId) -> groupModels.add(new GroupModel(groupId)));
        when(studentDaoMock.getAll()).thenReturn(studentEntities);
        List<StudentModel> studentModels = studentService.assignGroupIdToStudent(groupModels);
        Map<Integer, Integer> quantityInGroups = new HashMap<>();
        studentModels.stream().forEach((student) -> {
            if (!quantityInGroups.containsKey(student.getGroupId())) {
                int groupId = student.getGroupId();
                quantityInGroups.put(groupId, ONE_STUDENT);
            } else {
                int groupId = student.getGroupId();
                quantityInGroups.replace(groupId, 
                                         quantityInGroups.get(groupId) + ONE_STUDENT);
            }
        });
        quantityInGroups.entrySet()
                        .stream()
                        .forEach((relation) -> {
                            int studentQuantity = relation.getValue();
                                    assertTrue(( studentQuantity >= MIN_STUDENT_QUANTITY_IN_GROUP) && 
                                               (studentQuantity <= MAX_STUDENT_QUANTITY_IN_GROUP));
                        });
    }
    
    @Test
    void assignIdAndAddToDatabase_AssigningIdToStudentsAndAddingToDatabase_CorrectNumberAndOrderOfCalls() 
            throws DAOException, ServiceException {
        List<StudentModel> students = new ArrayList<>(); 
        studentService.assignIdAndAddToDatabase(students);
        InOrder inOrder = Mockito.inOrder(studentDaoMock);
        inOrder.verify(studentDaoMock, times(1)).insert(ArgumentMatchers.<StudentEntity>anyList());
        inOrder.verify(studentDaoMock, times(1)).getAll();
    }
    
    @Test
    void createWithoutId_CreationOfStudents_CorrectStudentQuantity() 
            throws ServiceException {
        List<String> nameList = Stream.generate(() -> TEST_NAME)
                                      .limit(NAMES_QUANTITY)
                                      .collect(Collectors.toList());
        when(readerMock.read(ArgumentMatchers.<Path>any())).thenReturn(nameList);
        when(readerMock.read(ArgumentMatchers.<Path>any())).thenReturn(nameList);
        List<StudentModel> students = studentService.createWithoutId(FIRST_NAME_LIST_FILENAME, 
                                                                     LAST_NAME_LIST_FILENAME);
        assertEquals(STUDENT_QUANTITY, students.size());
    }
}
