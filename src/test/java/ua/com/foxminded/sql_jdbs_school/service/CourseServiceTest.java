package ua.com.foxminded.sql_jdbs_school.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ua.com.foxminded.sql_jdbc_school.dao.CourseDAO;
import ua.com.foxminded.sql_jdbc_school.dao.DAOException;
import ua.com.foxminded.sql_jdbc_school.dao.DAOFactory;
import ua.com.foxminded.sql_jdbc_school.dao.entities.CourseEntity;
import ua.com.foxminded.sql_jdbc_school.dao.university.UniversityDAOFactory;
import ua.com.foxminded.sql_jdbc_school.service.CourseService;
import ua.com.foxminded.sql_jdbc_school.service.Reader;
import ua.com.foxminded.sql_jdbc_school.service.ServiceException;



@ExtendWith(MockitoExtension.class)
class CourseServiceTest {
	private static final String COURSE_NAME_1 = "Programming";
	private static final String COURSE_NAME_2 = "Math";
	private static final String COURSE_NAME_3 = "Physics";
	private static final Integer GOOD_STATUS = 0;
	
	
	@InjectMocks
	CourseService courseService;
	
	@Mock
	private Reader reader;
	
//	@Mock
//	private DAOFactory postresDAOFactory;
	
	@Mock
	private CourseDAO postgresCourseDAO;
	
	@Test
	void createCourses_creationAndMappingToDatabase_success() throws ServiceException, 
																	 DAOException {
		
	//	courseService.createCourses();
	//	verify(reader, times(1)).read(any(String.class));
	/*
		List<String> courseNameList = new ArrayList<>();
		courseNameList.add(COURSE_NAME_1);
		courseNameList.add(COURSE_NAME_2);
		courseNameList.add(COURSE_NAME_3);
		
		when(reader.read(any(String.class))).thenReturn(courseNameList);
		
		List<CourseEntity> courseEntities = new ArrayList<>();
		courseEntities.add(new CourseEntity(COURSE_NAME_1));
		courseEntities.add(new CourseEntity(COURSE_NAME_2));
		courseEntities.add(new CourseEntity(COURSE_NAME_3));
		
	//	when(postgresCourseDAO.create(ArgumentMatchers.<CourseEntity>anyList())).thenReturn(GOOD_STATUS);
	//	when(postgresCourseDAO.readAll()).thenReturn(courseEntities);
	//	assertEquals(3, courseService.createCourses().size());
		
		
		
	//	verify(postresDAOFactory, times(1)).getCourseDAO();
	//	verify(postgresCourseDAO, times(1)).create(ArgumentMatchers.<CourseEntity>anyList());
	 * 
	 */
		courseService.createCourses();
		verify(postgresCourseDAO, times(1)).getAll();
	}
}
