package ua.com.foxminded.sql_jdbc_school.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.com.foxminded.sql_jdbc_school.dao.CourseDAO;
import ua.com.foxminded.sql_jdbc_school.dao.DAOException;
import ua.com.foxminded.sql_jdbc_school.dao.entities.CourseEntity;
import ua.com.foxminded.sql_jdbc_school.service.CourseUniversity;
import ua.com.foxminded.sql_jdbc_school.service.Reader;
import ua.com.foxminded.sql_jdbc_school.service.ServiceException;
import ua.com.foxminded.sql_jdbc_school.service.dto.CourseDTO;

public class CourseServiceImpl implements CourseUniversity<List<CourseDTO>> {
	private static final Logger LOGGER = LogManager.getLogger();
    private static final String COURSE_NAME_LIST_FILENAME = "courseNameList.txt";
    private static final String ERROR_CREATE_COURSES = "The courses creation service doesn't work.";
    private static final String ERROR_GET_ALL_COURSES = "The getting all courses service doesn't work.";
    private final Reader reader;
    private final CourseDAO courseDAO;
    
    public CourseServiceImpl(Reader reader, CourseDAO courseDAO) {
        this.reader = reader;
        this.courseDAO = courseDAO;
    }
    
    @Override
    public List<CourseDTO> createCourses() throws ServiceException {
        try {
            List<String> coursesList = reader.read(COURSE_NAME_LIST_FILENAME);
            List<CourseEntity> courseEntities = coursesList.parallelStream()
            											   .map((courseName) -> new CourseEntity(courseName))
            											   .collect(Collectors.toList());
            courseDAO.insert(courseEntities);
            return courseDAO.getAll()
            				.parallelStream()
            				.map((entity) -> new CourseDTO(entity.getCourseId(), 
            						                       entity.getCourseName(), 
            						                       entity.getCourseDescription()))
            				.collect(Collectors.toList());
        } catch (ServiceException | DAOException e) {
        	LOGGER.error(ERROR_CREATE_COURSES, e);
            throw new ServiceException(ERROR_CREATE_COURSES, e);
        }
    }
    
    @Override
    public List<CourseDTO> getAllCourses() throws ServiceException {
    	try {
            return courseDAO.getAll()
            			    .stream()
            			    .map((entity) -> new CourseDTO(entity.getCourseId(), 
            				   	  						   entity.getCourseName(), 
            				   	  						   entity.getCourseDescription()))
            			    .collect(Collectors.toList());
        } catch (DAOException e) {
        	LOGGER.error(ERROR_GET_ALL_COURSES, e);
            throw new ServiceException (ERROR_GET_ALL_COURSES, e);
        }
    }
}
