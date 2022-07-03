package ua.com.foxminded.sql_jdbs_school.service.impl;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

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
import ua.com.foxminded.sql_jdbc_school.entity.CourseEntity;
import ua.com.foxminded.sql_jdbc_school.model.CourseModel;
import ua.com.foxminded.sql_jdbc_school.service.Reader;
import ua.com.foxminded.sql_jdbc_school.service.ServiceException;
import ua.com.foxminded.sql_jdbc_school.service.impl.CourseServiceImpl;

@ExtendWith(MockitoExtension.class)
class CourseServiceImlTest {
    
    private static final int STUDENT_ID = 1;
    private static final int COURSE_ID = 1;
	
	@InjectMocks
	CourseServiceImpl courseService;
	
	@Mock
	private Reader readerMock;
	
	@Mock
	private CourseDAO courseDAOMock;
	
	@Test
	void deleteAll_DeletingOfAllCourses_CorrectNumberOfCalls() throws ServiceException, DAOException {
	    courseService.deleteAll();
	    verify(courseDAOMock, times(1)).deleteAll();
	}
	
	@Test
	void deleteStudentFromCourseById_Call_CorrectNumberOfCalls() throws ServiceException, 
	                                                                    DAOException {
	    courseService.deleteStudentFromCourseById(STUDENT_ID, COURSE_ID);
	    verify(courseDAOMock, times(1)).deleteStudentFromCourseById(anyInt(), anyInt());
	}
	
	
	@Test
	void assignIdAndAddToDatabase() throws DAOException, ServiceException {
	    List<CourseModel> course = new ArrayList<>();
	    courseService.assignIdAndAddToDatabase(course);
	    InOrder inOrder = Mockito.inOrder(courseDAOMock);
	    inOrder.verify(courseDAOMock, times(1)).insert(ArgumentMatchers.<CourseEntity>anyList());
	}
	
	@Test
	void createWithoutId_CreatingCourses_CorrectNumberOfCalls() throws ServiceException, 
	                                                                     DAOException {
		courseService.createWithoutId();
		verify(readerMock, times(1)).read(anyString());
	}
	
	@Test
	void getAllCourses_GettingAllCourses_CorrectNumberOfCalls() throws DAOException, 
	                                                                   ServiceException {
		courseService.getAllCourses();
		verify(courseDAOMock, times(1)).getAll();
	}
}
