package ua.com.foxminded.sql_jdbc_school.service;

import java.util.List;
import java.util.stream.Collectors;

import ua.com.foxminded.sql_jdbc_school.dao.CourseDAO;
import ua.com.foxminded.sql_jdbc_school.dao.DAOException;
import ua.com.foxminded.sql_jdbc_school.dao.DAOFactory;
import ua.com.foxminded.sql_jdbc_school.dao.entities.CourseEntity;
import ua.com.foxminded.sql_jdbc_school.service.dto.CourseDTO;

public class CourseService implements Course<List<CourseDTO>> {
    private static final String COURSE_NAME_LIST_FILENAME = "courseNameList.txt";
    private static final String ERROR_CREATE_COURSES = "The courses creation service doesn't work.";
    private static final String ERROR_GET_ALL_COURSES = "The getting all courses service doesn't work.";
    private Reader reader;
    
    public CourseService(Reader reader) {
        this.reader = reader;
    }
    
    @Override
    public List<CourseDTO> createCourses() throws ServiceException {
        try {
            List<String> coursesList = reader.read(COURSE_NAME_LIST_FILENAME);
            List<CourseEntity> courseEntities = coursesList.parallelStream()
            											   .map((name) -> new CourseEntity(name))
            											   .collect(Collectors.toList());
            DAOFactory postgresDAOFactory = DAOFactory.getDAOFactory(DAOFactory.POSTGRES);
            CourseDAO postgresCourseDAO = postgresDAOFactory.getCourseDAO();
            postgresCourseDAO.create(courseEntities);
            
            
            return postgresCourseDAO.readAll()
            						.parallelStream()
            						.map((entity) -> new CourseDTO(entity.getCourseId(), 
            													   entity.getCourseName(), 
            													   entity.getCourseDescription()))
            						.collect(Collectors.toList());
        } catch (ServiceException | DAOException e) {
            throw new ServiceException(ERROR_CREATE_COURSES, e);
        }
    }
    
    @Override
    public List<CourseDTO> getAllCourses() throws ServiceException {
    	try {
            DAOFactory universityDAOFactory = DAOFactory.getDAOFactory(DAOFactory.POSTGRES);
            CourseDAO universityCourseDAO = universityDAOFactory.getCourseDAO();
            return universityCourseDAO.readAll()
            						  .stream()
            						  .map((entity) -> new CourseDTO(entity.getCourseId(), 
            								  						 entity.getCourseName(), 
            								  						 entity.getCourseDescription()))
            						  .collect(Collectors.toList());
        } catch (DAOException e) {
            throw new ServiceException (ERROR_GET_ALL_COURSES, e);
        }
    }
}
