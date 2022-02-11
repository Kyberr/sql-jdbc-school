package ua.com.foxminded.sql_jdbc_school.services.university;

import java.util.List;

import ua.com.foxminded.sql_jdbc_school.dao.CourseDAO;
import ua.com.foxminded.sql_jdbc_school.dao.DAOException;
import ua.com.foxminded.sql_jdbc_school.dao.DAOFactory;
import ua.com.foxminded.sql_jdbc_school.services.CourseService;
import ua.com.foxminded.sql_jdbc_school.services.Reader;
import ua.com.foxminded.sql_jdbc_school.services.ReaderServicesPropertiesCache;
import ua.com.foxminded.sql_jdbc_school.services.ServicesException;
import ua.com.foxminded.sql_jdbc_school.services.dto.CourseDTO;

public class UniversityCourseService implements CourseService<List<CourseDTO>> {
    private static final String COURSES_LIST_FILENAME_KEY = "CoursesListFilename";
    private static final String ERROR_CREATE_COURSES = "The courses creation service doesn't work.";
    private static final String ERROR_GET_ALL_COURSES = "The getting all courses service doesn't work.";
    private Reader reader;
    
    public UniversityCourseService(Reader reader) {
        this.reader = reader;
    }
    
    @Override
    public List<CourseDTO> createCourses() throws ServicesException.CoursesCreationServiceFail {
        try {
            String coursesListFilename = ReaderServicesPropertiesCache.getInstance()
                                                                      .getProperty(COURSES_LIST_FILENAME_KEY);
            List<String> coursesList = reader.toList(coursesListFilename);
            DAOFactory universityDAOFactory = DAOFactory.getDAOFactory(DAOFactory.UNIVERSITY);
            CourseDAO universityCourseDAO = universityDAOFactory.getCourseDAO();
            universityCourseDAO.insertCourse(coursesList);
            return universityCourseDAO.getAllCourses();
        } catch (ServicesException.PropertyFileLoadingFail | 
                 ServicesException.ReadFail | 
                 DAOException.CourseInsertionFail | 
                 DAOException.GetAllCoursesFail e) {
            throw new ServicesException.CoursesCreationServiceFail(ERROR_CREATE_COURSES, e);
        }
    }
    
    @Override
    public List<CourseDTO> getAllCourses() throws ServicesException.GetAllCoursesFailure {
        try {
            DAOFactory universityDAOFactory = DAOFactory.getDAOFactory(DAOFactory.UNIVERSITY);
            CourseDAO universityCourseDAO = universityDAOFactory.getCourseDAO();
            return universityCourseDAO.getAllCourses();
        } catch (DAOException.GetAllCoursesFail e) {
            throw new ServicesException.GetAllCoursesFailure (ERROR_GET_ALL_COURSES, e);
        }
    }
}
