package ua.com.foxminded.sql_jdbc_school;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.com.foxminded.sql_jdbc_school.dao.DAOConnectionFactory;
import ua.com.foxminded.sql_jdbc_school.dao.CourseDAO;
import ua.com.foxminded.sql_jdbc_school.dao.DAOConnectionPool;
import ua.com.foxminded.sql_jdbc_school.dao.GroupDAO;
import ua.com.foxminded.sql_jdbc_school.dao.StudentDAO;
import ua.com.foxminded.sql_jdbc_school.dao.jdbc.JdbcDAOConnectionFactory;
import ua.com.foxminded.sql_jdbc_school.dao.jdbc.JdbcCourseDAO;
import ua.com.foxminded.sql_jdbc_school.dao.jdbc.JdbcDAOConnectionPool;
import ua.com.foxminded.sql_jdbc_school.dao.jdbc.JdbcGroupDAO;
import ua.com.foxminded.sql_jdbc_school.dao.jdbc.JdbcStudentDAO;
import ua.com.foxminded.sql_jdbc_school.model.CourseModel;
import ua.com.foxminded.sql_jdbc_school.model.GroupModel;
import ua.com.foxminded.sql_jdbc_school.model.StudentModel;
import ua.com.foxminded.sql_jdbc_school.service.CourseService;
import ua.com.foxminded.sql_jdbc_school.service.GroupService;
import ua.com.foxminded.sql_jdbc_school.service.ServiceController;
import ua.com.foxminded.sql_jdbc_school.service.Reader;
import ua.com.foxminded.sql_jdbc_school.service.StudentService;
import ua.com.foxminded.sql_jdbc_school.service.impl.CourseServiceImpl;
import ua.com.foxminded.sql_jdbc_school.service.impl.GroupServiceImpl;
import ua.com.foxminded.sql_jdbc_school.service.impl.StudentServiceImpl;
import ua.com.foxminded.sql_jdbc_school.view.ServiceControllerView;
import ua.com.foxminded.sql_jdbc_school.view.console.ConsoleServiceControllerView;

public class Main {
    private static final Logger LOGGER = LogManager.getLogger();

    public static void main(String[] args) {
        Reader reader = new Reader();
        DAOConnectionFactory jdbcDaoConnectionFactory = new JdbcDAOConnectionFactory();
        DAOConnectionPool jdbcDaoConnectionPool = new JdbcDAOConnectionPool(jdbcDaoConnectionFactory);
        CourseDAO courseDAO = new JdbcCourseDAO(jdbcDaoConnectionPool);
        StudentDAO studentDAO = new JdbcStudentDAO(jdbcDaoConnectionPool);
        GroupDAO groupDAO = new JdbcGroupDAO(jdbcDaoConnectionPool);
        StudentService<List<StudentModel>, 
                       List<GroupModel>, 
                       String, Integer, 
                       List<CourseModel>> studentService = new StudentServiceImpl(reader, 
                                                                                  studentDAO, 
                                                                                  courseDAO);
        CourseService<List<CourseModel>, Integer> courseService = new CourseServiceImpl(reader, 
                                                                                        courseDAO);
        GroupService<List<GroupModel>, Integer> groupService = new GroupServiceImpl(groupDAO, 
                                                                                    studentDAO);
        ServiceControllerView<List<GroupModel>, 
                              List<CourseModel>, 
                              List<StudentModel>, 
                              List<StudentModel>, 
                              Integer> serviceControllerView = new ConsoleServiceControllerView();
        ServiceController serviceController = new ServiceController(studentService, 
                                                                    courseService, 
                                                                    groupService,
                                                                    serviceControllerView,
                                                                    jdbcDaoConnectionFactory,
                                                                    jdbcDaoConnectionPool);

        try {
            serviceController.bootstrap();
            serviceController.execute();
        } catch (Exception e) {
            LOGGER.error("Error", e);
        }
    }
}
