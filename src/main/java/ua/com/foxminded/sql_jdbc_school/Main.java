package ua.com.foxminded.sql_jdbc_school;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.com.foxminded.sql_jdbc_school.dao.ConnectionDAOFactory;
import ua.com.foxminded.sql_jdbc_school.dao.CourseDAO;
import ua.com.foxminded.sql_jdbc_school.dao.DAOConnectionPool;
import ua.com.foxminded.sql_jdbc_school.dao.GroupDAO;
import ua.com.foxminded.sql_jdbc_school.dao.StudentDAO;
import ua.com.foxminded.sql_jdbc_school.dao.jdbc.JdbcConnectionDAOFactory;
import ua.com.foxminded.sql_jdbc_school.dao.jdbc.JdbcCourseDAO;
import ua.com.foxminded.sql_jdbc_school.dao.jdbc.JdbcDAOConnectionPool;
import ua.com.foxminded.sql_jdbc_school.dao.jdbc.JdbcGroupDAO;
import ua.com.foxminded.sql_jdbc_school.dao.jdbc.JdbcStudentDAO;
import ua.com.foxminded.sql_jdbc_school.dto.CourseDto;
import ua.com.foxminded.sql_jdbc_school.dto.GroupDto;
import ua.com.foxminded.sql_jdbc_school.dto.StudentDto;
import ua.com.foxminded.sql_jdbc_school.service.CourseService;
import ua.com.foxminded.sql_jdbc_school.service.Generator;
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
        Generator generator = new Generator();
        ConnectionDAOFactory jdbcConnectionDaoFactory = new JdbcConnectionDAOFactory();
        DAOConnectionPool jdbcDaoConnectionPool = new JdbcDAOConnectionPool(jdbcConnectionDaoFactory);
        CourseDAO courseDAO = new JdbcCourseDAO(jdbcDaoConnectionPool);
        StudentDAO studentDAO = new JdbcStudentDAO(jdbcDaoConnectionPool);
        GroupDAO groupDAO = new JdbcGroupDAO(jdbcDaoConnectionPool);
        StudentService<List<StudentDto>, 
                       List<GroupDto>, 
                       String, Integer, 
                       List<CourseDto>> studentService = new StudentServiceImpl(reader, 
                                                                                generator, 
                                                                                studentDAO, 
                                                                                courseDAO, 
                                                                                jdbcDaoConnectionPool);
        CourseService<List<CourseDto>, Integer> courseService = new CourseServiceImpl(reader, 
                                                                                      courseDAO, 
                                                                                      jdbcDaoConnectionPool);
        GroupService<List<GroupDto>, Integer> groupService = new GroupServiceImpl(generator, 
                                                                                  groupDAO, 
                                                                                  studentDAO, 
                                                                                  jdbcDaoConnectionPool);
        ServiceControllerView<List<GroupDto>, 
                              List<CourseDto>, 
                              List<StudentDto>, 
                              List<StudentDto>, 
                              Integer> serviceControllerView = new ConsoleServiceControllerView();
        ServiceController serviceController = new ServiceController(studentService, 
                                                                    courseService, 
                                                                    groupService,
                                                                    serviceControllerView);

        try {
            serviceController.bootstrap();
            serviceController.execute();
        } catch (Exception e) {
            LOGGER.error("Error", e);
        }
    }
}
