package ua.com.foxminded.sql_jdbc_school.services;

import java.util.List;
import java.util.Scanner;
import ua.com.foxminded.sql_jdbc_school.services.dto.CourseDTO;
import ua.com.foxminded.sql_jdbc_school.services.dto.GroupDTO;
import ua.com.foxminded.sql_jdbc_school.services.dto.StudentCourseDTO;
import ua.com.foxminded.sql_jdbc_school.services.dto.StudentDTO;

public class UniversityMenu {
    private static final String ERROR_BOOTSTRAP = "The bootstraption has not performed.";
    private static final String ERROR_LOAD = "The menu loading is failed.";
    
    private TableService<Integer> tableService;
    private StudentService<List<StudentDTO>, List<GroupDTO>> studentService;
    private CourseService<List<CourseDTO>> courseService;
    private GroupService<List<GroupDTO>> groupService;
    private StudentCourseService<List<StudentDTO>, 
                                 List<CourseDTO>, 
                                 List<StudentCourseDTO>> studentCourseView;

    public UniversityMenu(TableService<Integer> tableService,
            StudentService<List<StudentDTO>, List<GroupDTO>> studentService,
            CourseService<List<CourseDTO>> courseService, GroupService<List<GroupDTO>> groupService,
            StudentCourseService<List<StudentDTO>, List<CourseDTO>, List<StudentCourseDTO>> studentCourseView) {
        this.tableService = tableService;
        this.studentService = studentService;
        this.courseService = courseService;
        this.groupService = groupService;
        this.studentCourseView = studentCourseView;
    }

    public void load() throws ServicesException.LoadUniversityMenuFail {
        try {
            bootstrap();
            System.out.println("Done in " + this.getClass().getName());
            
            
        } catch (ServicesException.BootstrapFail e) {
            throw new ServicesException.LoadUniversityMenuFail(ERROR_LOAD, e);
        }
        
        
        /*
        int intInput = toInt(scan());
        
        switch (intInput) {
        case 1:
            System.out.println("Enter the number of students.");
        }
        */
    }
    
    private void bootstrap() throws ServicesException.BootstrapFail {
        try {
            tableService.creatTables();
            List<CourseDTO> courses = courseService.createCourses();
            studentService.createStudents();
            List<GroupDTO> groups = groupService.createGroups();
            List<StudentDTO> students = studentService.assignGroup(groups);
            
            studentCourseView.createStudentCourseRelation(students, courses);
            
            
            
            
        } catch (ServicesException.TableCreationFail 
                | ServicesException.CoursesCreationServiceFail 
                | ServicesException.GroupCreationFail 
                | ServicesException.StudentCreationFail
                | ServicesException.AssignGgoupToStudentsFail 
                | ServicesException.StudentsCoursesRelationFailure e) {
            throw new ServicesException.BootstrapFail(ERROR_BOOTSTRAP, e);
        }
    }
    
    private int toInt (String input) {
        int intInput = 0;
        
        try {
            
            intInput = Integer.valueOf(input);
        } catch (NumberFormatException e) {
            System.out.println("The input must be a number.");
        }
        return intInput;
    }
    
    private String scan() {
        try (Scanner scanner = new Scanner(System.in)) {
            return scanner.nextLine();
        }
    }
}
