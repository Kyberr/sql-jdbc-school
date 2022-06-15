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
import ua.com.foxminded.sql_jdbc_school.dao.university.UniverstiyGroupDAO;
import ua.com.foxminded.sql_jdbc_school.dao.university.UniverstiyStudentDAO;
import ua.com.foxminded.sql_jdbc_school.service.Course;
import ua.com.foxminded.sql_jdbc_school.service.CourseService;
import ua.com.foxminded.sql_jdbc_school.service.Generator;
import ua.com.foxminded.sql_jdbc_school.service.Group;
import ua.com.foxminded.sql_jdbc_school.service.GroupService;
import ua.com.foxminded.sql_jdbc_school.service.Menu;
import ua.com.foxminded.sql_jdbc_school.service.Parser;
import ua.com.foxminded.sql_jdbc_school.service.Reader;
import ua.com.foxminded.sql_jdbc_school.service.Student;
import ua.com.foxminded.sql_jdbc_school.service.StudentCourse;
import ua.com.foxminded.sql_jdbc_school.service.StudentCourseService;
import ua.com.foxminded.sql_jdbc_school.service.StudentService;
import ua.com.foxminded.sql_jdbc_school.service.Table;
import ua.com.foxminded.sql_jdbc_school.service.TableService;
import ua.com.foxminded.sql_jdbc_school.service.dto.CourseDTO;
import ua.com.foxminded.sql_jdbc_school.service.dto.GroupDTO;
import ua.com.foxminded.sql_jdbc_school.service.dto.StudentCourseDTO;
import ua.com.foxminded.sql_jdbc_school.service.dto.StudentDTO;
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
        StudentDAO studentDAO = new UniverstiyStudentDAO(universityConnectionDAOFactory);
        GroupDAO groupDAO = new UniverstiyGroupDAO(universityConnectionDAOFactory);
        StudentCourseDAO studentCourseDAO = new UniversityStudentCourseDAO(universityConnectionDAOFactory);
        DAO universityDAO = new UniversityDAO(universityConnectionDAOFactory);
        Table<Integer> tableService = new TableService(reader, parser, universityDAO);
        Student<List<StudentDTO>, List<GroupDTO>, String, Integer> studentService = 
        		new StudentService(reader, generator, studentDAO);
        Course<List<CourseDTO>> courseService = new CourseService(reader, courseDAO);
        Group<List<GroupDTO>,Integer> groupService = new GroupService(generator, groupDAO, studentDAO);
        StudentCourse<List<StudentDTO>, List<CourseDTO>, List<StudentCourseDTO>, 
        			  Integer> studentCourseService = new StudentCourseService(generator, studentCourseDAO, 
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
