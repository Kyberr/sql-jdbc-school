package ua.com.foxminded.sql_jdbs_school.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

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

import ua.com.foxminded.sql_jdbc_school.dao.DAOException;
import ua.com.foxminded.sql_jdbc_school.dao.StudentDAO;
import ua.com.foxminded.sql_jdbc_school.entity.StudentEntity;
import ua.com.foxminded.sql_jdbc_school.model.GroupModel;
import ua.com.foxminded.sql_jdbc_school.model.StudentModel;
import ua.com.foxminded.sql_jdbc_school.service.Reader;
import ua.com.foxminded.sql_jdbc_school.service.ServiceException;
import ua.com.foxminded.sql_jdbc_school.service.impl.StudentServiceImpl;

@ExtendWith(MockitoExtension.class)
class StudentServiceImplTest {
    private static final int MAX_STUDENT_QUANTITY_IN_GROUP = 30;
    private static final int MIN_STUDENT_QUANTITY_IN_GROUP = 10;
    private static final int STUDENT_QUANTITY = 200;
    private static final int ZERO = 0;
    private static final int INCREMENT = 1;
    private static final int INITIAL_GROUP_ID = 1;
    private static final int NEXT_TO_LAST_GROUP_ID = 11;
    private static final int INITIAL_STUDENT_QUANTITY =  1;
    private static final int NAMES_QUANTITY = 20;
    private static final String TEST_NAME = "SomeName";
    
    
    @InjectMocks
    StudentServiceImpl studentService;
    
    @Mock
    StudentDAO studentDaoMock;
    
    @Mock
    Reader readerMock;
    
    void addStudent() {
        
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
                quantityInGroups.put(student.getGroupId(), INITIAL_STUDENT_QUANTITY);
            } else {
                quantityInGroups.replace(student.getGroupId(), 
                                         quantityInGroups.get(student.getGroupId()) + INCREMENT);
            }
        });
        quantityInGroups.entrySet()
                        .stream()
                        .forEach((e) -> assertTrue((e.getValue() >= MIN_STUDENT_QUANTITY_IN_GROUP) && 
                                                   (e.getKey() <= MAX_STUDENT_QUANTITY_IN_GROUP)));
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
        when(readerMock.read(anyString())).thenReturn(nameList);
        when(readerMock.read(anyString())).thenReturn(nameList);
        List<StudentModel> students = studentService.createWithoutId();
        assertEquals(STUDENT_QUANTITY, students.size());
    }
}
