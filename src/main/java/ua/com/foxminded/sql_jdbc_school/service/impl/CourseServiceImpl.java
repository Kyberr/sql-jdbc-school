package ua.com.foxminded.sql_jdbc_school.service.impl;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.com.foxminded.sql_jdbc_school.dao.CourseDAO;
import ua.com.foxminded.sql_jdbc_school.dao.DAOException;
import ua.com.foxminded.sql_jdbc_school.entity.CourseEntity;
import ua.com.foxminded.sql_jdbc_school.model.CourseModel;
import ua.com.foxminded.sql_jdbc_school.service.CourseService;
import ua.com.foxminded.sql_jdbc_school.service.Reader;
import ua.com.foxminded.sql_jdbc_school.service.ServiceException;

public class CourseServiceImpl implements CourseService {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String CREATE_WITHOUT_ID = "Creating students failed.";
    private static final String ERROR_DELETE_ALL_COURSES = "The service of course deletion doesn't work.";
    private static final String ERROR_DELETE_STUDENT_FROM_COURSE = "The service of the deletion of a student "
                                                                 + "from the course doesn't work.";
    private static final String ASSIGN_ID_AND_ADD_TO_DATABASE = "Assigning and adding courses to the database failed.";
    private static final String ERROR_GET_ALL_COURSES = "The getting all courses service doesn't work.";
    
    private final Reader reader;
    private final CourseDAO courseDao;

    public CourseServiceImpl(Reader reader, CourseDAO courseDao) {
        this.reader = reader;
        this.courseDao = courseDao;
    }

    @Override
    public int deleteAll() throws ServiceException {
        try {
            return courseDao.deleteAll();
        } catch (DAOException e) {
            LOGGER.error(ERROR_DELETE_ALL_COURSES, e);
            throw new ServiceException(ERROR_DELETE_ALL_COURSES, e);
        } 
    }

    @Override
    public int deleteStudentFromCourseById(int studentId, int courseId) throws ServiceException {
        try {
            return courseDao.deleteStudentFromCourseById(studentId, courseId);
        } catch (DAOException e) {
            LOGGER.error(ERROR_DELETE_STUDENT_FROM_COURSE, e);
            throw new ServiceException(ERROR_DELETE_STUDENT_FROM_COURSE, e);
        } 
    }
    
    @Override
    public List<CourseModel> assignIdAndAddToDatabase(List<CourseModel> courses) throws ServiceException {
        try {
            List<CourseEntity> courseEntities = courses.parallelStream()
                    .map((model) -> new CourseEntity(model.getCourseName(), 
                                                     model.getCourseDescription()))
                    .collect(Collectors.toList());
            
            courseDao.insert(courseEntities);
            return courseDao.getAll()
                            .parallelStream()
                            .map((entity) -> new CourseModel(entity.getCourseId(),
                                                           entity.getCourseName(), 
                                                           entity.getCourseDescription()))
                            .collect(Collectors.toList());
        } catch (DAOException e) {
            LOGGER.error(ASSIGN_ID_AND_ADD_TO_DATABASE, e);
            throw new ServiceException(ASSIGN_ID_AND_ADD_TO_DATABASE, e);
        }
    }

    @Override
    public List<CourseModel> createWithoutId(String courseNameListFilename) throws ServiceException {
        try {
            URL courseNameFileUrl = CourseServiceImpl.class.getClassLoader()
                                                           .getResource(courseNameListFilename);
            Path courseNameFilePath = Paths.get(courseNameFileUrl.toURI());
            List<String> coursesList = reader.read(courseNameFilePath);
            return coursesList.parallelStream().map((courseName) -> new CourseModel(courseName))
                                               .collect(Collectors.toList());
        } catch (URISyntaxException e) {
            LOGGER.error(CREATE_WITHOUT_ID, e);
            throw new ServiceException(null, e);
        }
        
            
    }

    @Override
    public List<CourseModel> getAllCourses() throws ServiceException {
        try {
            return courseDao.getAll()
                            .stream()
                            .map((entity) -> new CourseModel(entity.getCourseId(),
                                                             entity.getCourseName(), 
                                                             entity.getCourseDescription()))
                            .collect(Collectors.toList());
        } catch (DAOException e) {
            LOGGER.error(ERROR_GET_ALL_COURSES, e);
            throw new ServiceException(ERROR_GET_ALL_COURSES, e);
        } 
    }
}
