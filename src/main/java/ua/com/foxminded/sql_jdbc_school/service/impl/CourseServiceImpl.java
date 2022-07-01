package ua.com.foxminded.sql_jdbc_school.service.impl;

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
    private static final String ERROR_DELETE_ALL_COURSES = "The service of course deletion doesn't work.";
    private static final String ERROR_DELETE_STUDENT_FROM_COURSE = "The service of the deletion of a student "
                                                                 + "from the course doesn't work.";
    private static final String COURSE_NAME_LIST_FILENAME = "course-names.txt";
    private static final String ERROR_CREATE_COURSES = "The courses creation service doesn't work.";
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
    public List<CourseModel> create() throws ServiceException {
        try {
            List<String> coursesList = reader.read(COURSE_NAME_LIST_FILENAME);
            List<CourseEntity> courseEntities = coursesList.parallelStream()
                    .map((courseName) -> new CourseEntity(courseName))
                    .collect(Collectors.toList());
            courseDao.insert(courseEntities);
            return courseDao.getAll()
                            .parallelStream()
                            .map((entity) -> new CourseModel(entity.getCourseId(),
                                                           entity.getCourseName(), 
                                                           entity.getCourseDescription()))
                            .collect(Collectors.toList());
        } catch (DAOException e) {
            LOGGER.error(ERROR_CREATE_COURSES, e);
            throw new ServiceException(ERROR_CREATE_COURSES, e);
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
