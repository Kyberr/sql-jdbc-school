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
import ua.com.foxminded.sql_jdbc_school.services.UniversityMenu;
import ua.com.foxminded.sql_jdbc_school.services.dto.CourseDTO;
import ua.com.foxminded.sql_jdbc_school.services.dto.GroupDTO;
import ua.com.foxminded.sql_jdbc_school.services.dto.StudentCourseDTO;
import ua.com.foxminded.sql_jdbc_school.services.dto.StudentDTO;
import ua.com.foxminded.sql_jdbc_school.services.university.UniversityCourseService;
import ua.com.foxminded.sql_jdbc_school.services.university.UniversityGroupService;
import ua.com.foxminded.sql_jdbc_school.services.university.UniversityStudentCourseService;
import ua.com.foxminded.sql_jdbc_school.services.university.UniversityStudentService;
import ua.com.foxminded.sql_jdbc_school.services.university.UniversityTableService;
import ua.com.foxminded.sql_jdbc_school.view.UniversityView;
import ua.com.foxminded.sql_jdbc_school.view.View;

public class Main {
    private static final Logger LOGGER = LogManager.getLogger(Main.class);

    public static void main(String[] args) {

        Reader reader = new Reader();
        Parser parser = new Parser();
        Generator generator = new Generator();
        
        TableService<Integer> tableService = new UniversityTableService(reader, parser);
        StudentService<List<StudentDTO>, 
                List<GroupDTO>> studentService = new UniversityStudentService(reader, generator);
        CourseService<List<CourseDTO>> courseService = new UniversityCourseService(reader);
        GroupService<List<GroupDTO>, Integer> groupService = new UniversityGroupService(generator);
        StudentCourseService<List<StudentDTO>, 
                List<CourseDTO>, List<StudentCourseDTO>> studentCourseService = 
                new UniversityStudentCourseService(generator);
        View<List<GroupDTO>> universityView = new UniversityView();
        UniversityMenu menu = new UniversityMenu(tableService, studentService, courseService, 
                                                 groupService, studentCourseService, universityView);
        
        try {
            menu.load();
            
            
            
            
            
        } catch (Exception e) {
            LOGGER.error("Error", e);
        }
        
    }
}
