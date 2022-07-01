package ua.com.foxminded.sql_jdbc_school;

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
import ua.com.foxminded.sql_jdbc_school.service.CourseService;
import ua.com.foxminded.sql_jdbc_school.service.GroupService;
import ua.com.foxminded.sql_jdbc_school.service.Reader;
import ua.com.foxminded.sql_jdbc_school.service.StudentService;
import ua.com.foxminded.sql_jdbc_school.service.impl.CourseServiceImpl;
import ua.com.foxminded.sql_jdbc_school.service.impl.GroupServiceImpl;
import ua.com.foxminded.sql_jdbc_school.service.impl.StudentServiceImpl;
import ua.com.foxminded.sql_jdbc_school.view.CourseView;
import ua.com.foxminded.sql_jdbc_school.view.GroupView;
import ua.com.foxminded.sql_jdbc_school.view.StudentView;
import ua.com.foxminded.sql_jdbc_school.view.ViewFacade;
import ua.com.foxminded.sql_jdbc_school.view.ViewProcessor;
import ua.com.foxminded.sql_jdbc_school.view.console.ConsoleViewProcessor;

public class Main {
    private static final Logger LOGGER = LogManager.getLogger();

    public static void main(String[] args) {
        Reader reader = new Reader();
        DAOConnectionFactory jdbcDaoConnectionFactory = new JdbcDAOConnectionFactory();
        DAOConnectionPool jdbcDaoConnectionPool = new JdbcDAOConnectionPool(jdbcDaoConnectionFactory);
        CourseDAO courseDAO = new JdbcCourseDAO(jdbcDaoConnectionPool);
        StudentDAO studentDAO = new JdbcStudentDAO(jdbcDaoConnectionPool);
        GroupDAO groupDAO = new JdbcGroupDAO(jdbcDaoConnectionPool);
        StudentService studentService = new StudentServiceImpl(reader, studentDAO, courseDAO);
        CourseService courseService = new CourseServiceImpl(reader, courseDAO);
        GroupService groupService = new GroupServiceImpl(groupDAO, studentDAO);
        ViewProcessor view = new ConsoleViewProcessor();
        
        CourseView courseMenu = new CourseView(view, courseService, studentService, 
                                               jdbcDaoConnectionPool);
        GroupView groupMenu = new GroupView(view, groupService, jdbcDaoConnectionPool);
        StudentView studentMenu = new StudentView(view, courseService, studentService);
        ViewFacade menu = new ViewFacade(courseMenu, groupMenu, studentMenu, jdbcDaoConnectionPool, view);

        try {
            menu.bootstrap();
            menu.execute();
        } catch (Exception e) {
            LOGGER.error("Error", e);
        }
    }
}
