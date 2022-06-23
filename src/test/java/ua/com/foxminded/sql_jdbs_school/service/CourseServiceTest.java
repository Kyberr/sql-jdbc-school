package ua.com.foxminded.sql_jdbs_school.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.com.foxminded.sql_jdbc_school.dao.CourseDAO;
import ua.com.foxminded.sql_jdbc_school.dao.DAOException;
import ua.com.foxminded.sql_jdbc_school.service.Reader;
import ua.com.foxminded.sql_jdbc_school.service.ServiceException;
import ua.com.foxminded.sql_jdbc_school.service.impl.CourseServiceImpl;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {
	
	@InjectMocks
	CourseServiceImpl courseService;
	
	@Mock
	private Reader readerMock;
	
	@Mock
	private CourseDAO courseDAOMock;
	
	@Test
	void createCourses_Call_CorrectNumberAndOrderOfCalls() throws ServiceException, 
												      	          DAOException {
		courseService.createCourses();
		InOrder inOrder = Mockito.inOrder(readerMock, courseDAOMock);
		inOrder.verify(readerMock, times(1)).read(any(String.class));
		inOrder.verify(courseDAOMock, times(1)).getAll();
	}
	
	@Test
	void getAllCourses_Call_CorrectNumberAndOrCall() throws DAOException, 
														    ServiceException {
		courseService.getAllCourses();
		verify(courseDAOMock, times(1)).getAll();
	}
}
