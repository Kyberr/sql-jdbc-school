package ua.com.foxminded.sql_jdbc_school;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.com.foxminded.sql_jdbc_school.dao.ConnectionDAOFactory;
import ua.com.foxminded.sql_jdbc_school.dao.CourseDAO;
import ua.com.foxminded.sql_jdbc_school.dao.DAOConnectionPoolImpl;
import ua.com.foxminded.sql_jdbc_school.dao.GroupDAO;
import ua.com.foxminded.sql_jdbc_school.dao.StudentDAO;
import ua.com.foxminded.sql_jdbc_school.dao.university.UniversityConnectionDAOFactory;
import ua.com.foxminded.sql_jdbc_school.dao.university.UniversityCourseDAO;
import ua.com.foxminded.sql_jdbc_school.dao.university.UniversityGroupDAO;
import ua.com.foxminded.sql_jdbc_school.dao.university.UniversityStudentDAO;
import ua.com.foxminded.sql_jdbc_school.dto.CourseDto;
import ua.com.foxminded.sql_jdbc_school.dto.GroupDto;
import ua.com.foxminded.sql_jdbc_school.dto.StudentDto;
import ua.com.foxminded.sql_jdbc_school.service.CourseService;
import ua.com.foxminded.sql_jdbc_school.service.CourseServiceImpl;
import ua.com.foxminded.sql_jdbc_school.service.Generator;
import ua.com.foxminded.sql_jdbc_school.service.GroupService;
import ua.com.foxminded.sql_jdbc_school.service.GroupServiceImpl;
import ua.com.foxminded.sql_jdbc_school.service.ServiceController;
import ua.com.foxminded.sql_jdbc_school.service.Reader;
import ua.com.foxminded.sql_jdbc_school.service.StudentService;
import ua.com.foxminded.sql_jdbc_school.service.StudentServiceImpl;
import ua.com.foxminded.sql_jdbc_school.view.ServiceControllerView;
import ua.com.foxminded.sql_jdbc_school.view.console.ConsoleServiceControllerView;

public class Main {
    private static final Logger LOGGER = LogManager.getLogger();

    public static void main(String[] args) {
        Reader reader = new Reader();
        Generator generator = new Generator();
        ConnectionDAOFactory universityConnectionDAOFactory = new UniversityConnectionDAOFactory();
        DAOConnectionPoolImpl connectionPool = new DAOConnectionPoolImpl(universityConnectionDAOFactory);
        CourseDAO courseDAO = new UniversityCourseDAO(universityConnectionDAOFactory, connectionPool);
        StudentDAO studentDAO = new UniversityStudentDAO(universityConnectionDAOFactory, connectionPool);
        GroupDAO groupDAO = new UniversityGroupDAO(universityConnectionDAOFactory);
        StudentService<List<StudentDto>, 
                       List<GroupDto>, 
                       String, Integer, 
                       List<CourseDto>> studentService = new StudentServiceImpl(reader, 
                                                                                generator, 
                                                                                studentDAO, 
                                                                                courseDAO, 
                                                                                connectionPool);
        CourseService<List<CourseDto>, Integer> courseService = new CourseServiceImpl(reader, courseDAO);
        GroupService<List<GroupDto>, Integer> groupService = new GroupServiceImpl(generator, 
                                                                                  groupDAO, 
                                                                                  studentDAO);
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
