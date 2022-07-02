package ua.com.foxminded.sql_jdbs_school.service;

import static org.mockito.Mockito.times;

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
import ua.com.foxminded.sql_jdbc_school.service.Reader;
import ua.com.foxminded.sql_jdbc_school.service.ServiceException;
import ua.com.foxminded.sql_jdbc_school.service.impl.StudentServiceImpl;

@ExtendWith(MockitoExtension.class)
public class StudentServiceImplTest {
    
    @InjectMocks
    StudentServiceImpl studentService;
    
    @Mock
    StudentDAO studentDao;
    
    @Mock
    Reader reader;
    
    @Test
    void create_CreationOfStudents_CorrectNumberOfStudents() {
        
        
        
    }
    
    @Test
    void create_CreationOfStudents_CorrectNumberAndOrderOfCalls() throws ServiceException, DAOException {
        studentService.createWithoutId();
        InOrder inOrder = Mockito.inOrder(studentService);
        inOrder.verify(reader, times(2)).read(ArgumentMatchers.anyString());
        inOrder.verify(studentDao).insert(ArgumentMatchers.<StudentEntity>anyList());
    }
}
