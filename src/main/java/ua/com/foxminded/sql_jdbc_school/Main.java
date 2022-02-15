package ua.com.foxminded.sql_jdbc_school;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.com.foxminded.sql_jdbc_school.services.CourseService;
import ua.com.foxminded.sql_jdbc_school.services.Generator;
import ua.com.foxminded.sql_jdbc_school.services.GroupService;
import ua.com.foxminded.sql_jdbc_school.services.Parser;
import ua.com.foxminded.sql_jdbc_school.services.Reader;
import ua.com.foxminded.sql_jdbc_school.services.StudentCourseService;
import ua.com.foxminded.sql_jdbc_school.services.StudentService;
import ua.com.foxminded.sql_jdbc_school.services.TableService;
import ua.com.foxminded.sql_jdbc_school.services.Menu;
import ua.com.foxminded.sql_jdbc_school.services.dto.CourseDTO;
import ua.com.foxminded.sql_jdbc_school.services.dto.GroupDTO;
import ua.com.foxminded.sql_jdbc_school.services.dto.StudentCourseDTO;
import ua.com.foxminded.sql_jdbc_school.services.dto.StudentDTO;
import ua.com.foxminded.sql_jdbc_school.services.university.UniversityCourseService;
import ua.com.foxminded.sql_jdbc_school.services.university.UniversityGroupService;
import ua.com.foxminded.sql_jdbc_school.services.university.UniversityStudentCourseService;
import ua.com.foxminded.sql_jdbc_school.services.university.UniversityStudentService;
import ua.com.foxminded.sql_jdbc_school.services.university.UniversityTableService;
import ua.com.foxminded.sql_jdbc_school.view.ConsoleMenuView;
import ua.com.foxminded.sql_jdbc_school.view.MenuView;

public class Main {
    private static final Logger LOGGER = LogManager.getLogger(Main.class);

    public static void main(String[] args) {

        Reader reader = new Reader();
        Parser parser = new Parser();
        Generator generator = new Generator();
        TableService<Integer> tableService = new UniversityTableService(reader, parser);
        StudentService<List<StudentDTO>, 
                       List<GroupDTO>, 
                       String,
                       Integer> studentService = new UniversityStudentService(reader, generator);
        CourseService<List<CourseDTO>> courseService = new UniversityCourseService(reader);
        GroupService<List<GroupDTO>, Integer> groupService = new UniversityGroupService(generator);
        StudentCourseService<List<StudentDTO>, 
                             List<CourseDTO>, 
                             List<StudentCourseDTO>, Integer> studentCourseService = 
                                     new UniversityStudentCourseService(generator);
        MenuView<List<GroupDTO>, List<CourseDTO>, List<StudentCourseDTO>, 
                 List<StudentDTO>> menuView = new ConsoleMenuView();
        Menu menu = new Menu(tableService, studentService, courseService, 
                             groupService, studentCourseService, menuView);
        
        try {
            menu.bootstrap();
            menu.load();
        } catch (Exception e) {
            LOGGER.error("Error", e);
        }
    }
}
