package ua.com.foxminded.sql_jdbc_school;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.com.foxminded.sql_jdbc_school.service.Course;
import ua.com.foxminded.sql_jdbc_school.service.CourseService;
import ua.com.foxminded.sql_jdbc_school.service.Generator;
import ua.com.foxminded.sql_jdbc_school.service.Group;
import ua.com.foxminded.sql_jdbc_school.service.GroupService;
import ua.com.foxminded.sql_jdbc_school.service.Menu;
import ua.com.foxminded.sql_jdbc_school.service.Parser;
import ua.com.foxminded.sql_jdbc_school.service.Reader;
import ua.com.foxminded.sql_jdbc_school.service.StudentCourse;
import ua.com.foxminded.sql_jdbc_school.service.StudentCourseService;
import ua.com.foxminded.sql_jdbc_school.service.StudentService;
import ua.com.foxminded.sql_jdbc_school.service.Student;
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
        Table<Integer> tableService = new TableService(reader, parser);
        Student<List<StudentDTO>, List<GroupDTO>, String, Integer> studentService = 
        		new StudentService(reader, generator);
        Course<List<CourseDTO>> courseService = new CourseService(reader);
        Group<List<GroupDTO>, Integer> groupService = new GroupService(generator);
        StudentCourse<List<StudentDTO>, List<CourseDTO>, List<StudentCourseDTO>, 
        			  Integer> studentCourseService = new StudentCourseService(generator);
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
