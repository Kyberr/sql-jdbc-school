package ua.com.foxminded.sql_jdbc_school;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.com.foxminded.sql_jdbc_school.service.CourseService;
import ua.com.foxminded.sql_jdbc_school.service.Generator;
import ua.com.foxminded.sql_jdbc_school.service.GroupService;
import ua.com.foxminded.sql_jdbc_school.service.Menu;
import ua.com.foxminded.sql_jdbc_school.service.Parser;
import ua.com.foxminded.sql_jdbc_school.service.Reader;
import ua.com.foxminded.sql_jdbc_school.service.StudentCourseService;
import ua.com.foxminded.sql_jdbc_school.service.StudentService;
import ua.com.foxminded.sql_jdbc_school.service.TableService;
import ua.com.foxminded.sql_jdbc_school.service.dto.CourseDTO;
import ua.com.foxminded.sql_jdbc_school.service.dto.GroupDTO;
import ua.com.foxminded.sql_jdbc_school.service.dto.StudentCourseDTO;
import ua.com.foxminded.sql_jdbc_school.service.dto.StudentDTO;
import ua.com.foxminded.sql_jdbc_school.service.university.UniversityCourseService;
import ua.com.foxminded.sql_jdbc_school.service.university.UniversityGroupService;
import ua.com.foxminded.sql_jdbc_school.service.university.UniversityStudentCourseService;
import ua.com.foxminded.sql_jdbc_school.service.university.UniversityStudentService;
import ua.com.foxminded.sql_jdbc_school.service.university.UniversityTableService;
import ua.com.foxminded.sql_jdbc_school.view.ConsoleMenuView;
import ua.com.foxminded.sql_jdbc_school.view.MenuView;

public class Main {
    private static final Logger LOGGER = LogManager.getLogger(Main.class);

    public static void main(String[] args) {

        Reader reader = new Reader();
        Parser parser = new Parser();
        Generator generator = new Generator();
        TableService<Integer> tableService = new UniversityTableService(reader, parser);
        StudentService<List<StudentDTO>, List<GroupDTO>, String, 
                       Integer> studentService = new UniversityStudentService(reader, generator);
        CourseService<List<CourseDTO>> courseService = new UniversityCourseService(reader);
        GroupService<List<GroupDTO>, Integer> groupService = new UniversityGroupService(generator);
        StudentCourseService<List<StudentDTO>, List<CourseDTO>, List<StudentCourseDTO>, 
                             Integer> studentCourseService = new UniversityStudentCourseService(generator);
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
