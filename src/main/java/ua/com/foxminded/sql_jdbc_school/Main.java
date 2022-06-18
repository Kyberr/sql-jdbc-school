package ua.com.foxminded.sql_jdbc_school;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.com.foxminded.sql_jdbc_school.dao.ConnectionDAOFactory;
import ua.com.foxminded.sql_jdbc_school.dao.CourseDAO;
import ua.com.foxminded.sql_jdbc_school.dao.GroupDAO;
import ua.com.foxminded.sql_jdbc_school.dao.StudentDAO;
import ua.com.foxminded.sql_jdbc_school.dao.university.UniversityConnectionDAOFactory;
import ua.com.foxminded.sql_jdbc_school.dao.university.UniversityCourseDAO;
import ua.com.foxminded.sql_jdbc_school.dao.university.UniversityGroupDAO;
import ua.com.foxminded.sql_jdbc_school.dao.university.UniversityStudentDAO;
import ua.com.foxminded.sql_jdbc_school.service.CourseService;
import ua.com.foxminded.sql_jdbc_school.service.Generator;
import ua.com.foxminded.sql_jdbc_school.service.GroupService;
import ua.com.foxminded.sql_jdbc_school.service.ServiceController;
import ua.com.foxminded.sql_jdbc_school.service.Parser;
import ua.com.foxminded.sql_jdbc_school.service.Reader;
import ua.com.foxminded.sql_jdbc_school.service.StudentService;
import ua.com.foxminded.sql_jdbc_school.service.dto.CourseDto;
import ua.com.foxminded.sql_jdbc_school.service.dto.GroupDto;
import ua.com.foxminded.sql_jdbc_school.service.dto.StudentDto;
import ua.com.foxminded.sql_jdbc_school.service.impl.CourseServiceImpl;
import ua.com.foxminded.sql_jdbc_school.service.impl.GroupServiceImpl;
import ua.com.foxminded.sql_jdbc_school.service.impl.StudentServiceImpl;
import ua.com.foxminded.sql_jdbc_school.view.ConsoleServiceControllerView;
import ua.com.foxminded.sql_jdbc_school.view.ServiceControllerView;


public class Main {
    private static final Logger LOGGER = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        Reader reader = new Reader();
        Parser parser = new Parser();
        Generator generator = new Generator();
        ConnectionDAOFactory universityConnectionDAOFactory = new UniversityConnectionDAOFactory();
        CourseDAO courseDAO = new UniversityCourseDAO(universityConnectionDAOFactory);
        StudentDAO studentDAO = new UniversityStudentDAO(universityConnectionDAOFactory);
        GroupDAO groupDAO = new UniversityGroupDAO(universityConnectionDAOFactory);
        StudentService<List<StudentDto>, List<GroupDto>, String, Integer, List<CourseDto>> studentService = 
        		new StudentServiceImpl(reader, generator, studentDAO, courseDAO);
        CourseService<List<CourseDto>, Integer> courseService = new CourseServiceImpl(reader, courseDAO);
        GroupService<List<GroupDto>,Integer> groupService = new GroupServiceImpl(generator, groupDAO, studentDAO);
        ServiceControllerView<List<GroupDto>, List<CourseDto>, List<StudentDto>, List<StudentDto>, 
                 			  Integer> serviceControllerView = new ConsoleServiceControllerView();
        ServiceController menu = new ServiceController(studentService, courseService, groupService, 
        											   serviceControllerView);
        
        try {
            menu.bootstrap();
            menu.execute();
        } catch (Exception e) {
            LOGGER.error("Error", e);
        }
    }
}
