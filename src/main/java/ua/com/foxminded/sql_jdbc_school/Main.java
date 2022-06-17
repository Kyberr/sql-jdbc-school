package ua.com.foxminded.sql_jdbc_school;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.com.foxminded.sql_jdbc_school.dao.ConnectionDAOFactory;
import ua.com.foxminded.sql_jdbc_school.dao.CourseDAO;
import ua.com.foxminded.sql_jdbc_school.dao.DAO;
import ua.com.foxminded.sql_jdbc_school.dao.GroupDAO;
import ua.com.foxminded.sql_jdbc_school.dao.StudentCourseDAO;
import ua.com.foxminded.sql_jdbc_school.dao.StudentDAO;
import ua.com.foxminded.sql_jdbc_school.dao.university.UniversityConnectionDAOFactory;
import ua.com.foxminded.sql_jdbc_school.dao.university.UniversityCourseDAO;
import ua.com.foxminded.sql_jdbc_school.dao.university.UniversityDAO;
import ua.com.foxminded.sql_jdbc_school.dao.university.UniversityStudentCourseDAO;
import ua.com.foxminded.sql_jdbc_school.dao.university.UniversityGroupDAO;
import ua.com.foxminded.sql_jdbc_school.dao.university.UniversityStudentDAO;
import ua.com.foxminded.sql_jdbc_school.service.CourseUniversity;
import ua.com.foxminded.sql_jdbc_school.service.Generator;
import ua.com.foxminded.sql_jdbc_school.service.GroupUniversity;
import ua.com.foxminded.sql_jdbc_school.service.Menu;
import ua.com.foxminded.sql_jdbc_school.service.Parser;
import ua.com.foxminded.sql_jdbc_school.service.Reader;
import ua.com.foxminded.sql_jdbc_school.service.StudentService;
import ua.com.foxminded.sql_jdbc_school.service.StudentCourseService;
import ua.com.foxminded.sql_jdbc_school.service.TableService;
import ua.com.foxminded.sql_jdbc_school.service.dto.CourseDTO;
import ua.com.foxminded.sql_jdbc_school.service.dto.GroupDTO;
import ua.com.foxminded.sql_jdbc_school.service.dto.StudentCourseDTO;
import ua.com.foxminded.sql_jdbc_school.service.dto.StudentDTO;
import ua.com.foxminded.sql_jdbc_school.service.impl.CourseServiceImpl;
import ua.com.foxminded.sql_jdbc_school.service.impl.GroupServiceImpl;
import ua.com.foxminded.sql_jdbc_school.service.impl.StudentCourseServiceImpl;
import ua.com.foxminded.sql_jdbc_school.service.impl.StudentServiceImpl;
import ua.com.foxminded.sql_jdbc_school.service.impl.TableServiceImpl;
import ua.com.foxminded.sql_jdbc_school.view.ConsoleMenuView;
import ua.com.foxminded.sql_jdbc_school.view.MenuView;


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
        StudentCourseDAO studentCourseDAO = new UniversityStudentCourseDAO(universityConnectionDAOFactory);
        DAO universityDAO = new UniversityDAO(universityConnectionDAOFactory);
        TableService<Integer> tableService = new TableServiceImpl(reader, parser, universityDAO);
        StudentService<List<StudentDTO>, List<GroupDTO>, String, Integer> studentService = 
        		new StudentServiceImpl(reader, generator, studentDAO);
        CourseUniversity<List<CourseDTO>> courseService = new CourseServiceImpl(reader, courseDAO);
        GroupUniversity<List<GroupDTO>,Integer> groupService = new GroupServiceImpl(generator, groupDAO, studentDAO);
        StudentCourseService<List<StudentDTO>, List<CourseDTO>, List<StudentCourseDTO>, 
        			  Integer> studentCourseService = new StudentCourseServiceImpl(generator, studentCourseDAO, 
        					  											       studentDAO, courseDAO);
        MenuView<List<GroupDTO>, List<CourseDTO>, List<StudentCourseDTO>, List<StudentDTO>, 
                 Integer> menuView = new ConsoleMenuView();
        Menu menu = new Menu(tableService, studentService, courseService, groupService, 
                             studentCourseService, menuView);
        
        try {
            menu.bootstrap();
            menu.execute();
        } catch (Exception e) {
            LOGGER.error("Error", e);
        }
    }
}
