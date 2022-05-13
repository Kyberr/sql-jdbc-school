package ua.com.foxminded.sql_jdbc_school.service;

import java.util.List;

import ua.com.foxminded.sql_jdbc_school.dao.CourseDAO;
import ua.com.foxminded.sql_jdbc_school.dao.DAOException;
import ua.com.foxminded.sql_jdbc_school.dao.DAOFactory;
import ua.com.foxminded.sql_jdbc_school.service.dto.CourseDTO;

public class CourseService implements Course<List<CourseDTO>> {
    private static final String COURSES_LIST_FILENAME_KEY = "CoursesListFilename";
    private static final String ERROR_CREATE_COURSES = "The courses creation service doesn't work.";
    private static final String ERROR_GET_ALL_COURSES = "The getting all courses service doesn't work.";
    private Reader reader;
    
    public CourseService(Reader reader) {
        this.reader = reader;
    }
    
    @Override
    public List<CourseDTO> createCourses() throws ServiceException {
        try {
            String coursesListFilename = ReaderServicesPropertiesCache.getInstance()
                                                                      .getProperty(COURSES_LIST_FILENAME_KEY);
            List<String> coursesList = reader.toList(coursesListFilename);
            DAOFactory universityDAOFactory = DAOFactory.getDAOFactory(DAOFactory.UNIVERSITY);
            CourseDAO universityCourseDAO = universityDAOFactory.getCourseDAO();
            universityCourseDAO.insertCourse(coursesList);
            return universityCourseDAO.getAllCourses();
        } catch (ServiceException | DAOException e) {
            throw new ServiceException(ERROR_CREATE_COURSES, e);
        }
    }
    
    @Override
    public List<CourseDTO> getAllCourses() throws ServiceException {
        try {
            DAOFactory universityDAOFactory = DAOFactory.getDAOFactory(DAOFactory.UNIVERSITY);
            CourseDAO universityCourseDAO = universityDAOFactory.getCourseDAO();
            return universityCourseDAO.getAllCourses();
        } catch (DAOException e) {
            throw new ServiceException (ERROR_GET_ALL_COURSES, e);
        }
    }
}
