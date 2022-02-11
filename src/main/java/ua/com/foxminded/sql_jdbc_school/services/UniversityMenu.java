package ua.com.foxminded.sql_jdbc_school.services;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import ua.com.foxminded.sql_jdbc_school.services.dto.CourseDTO;
import ua.com.foxminded.sql_jdbc_school.services.dto.GroupDTO;
import ua.com.foxminded.sql_jdbc_school.services.dto.StudentCourseDTO;
import ua.com.foxminded.sql_jdbc_school.services.dto.StudentDTO;
import ua.com.foxminded.sql_jdbc_school.view.View;

public class UniversityMenu {
    private static final String ERROR_BOOTSTRAP = "The bootstraption has not performed.";
    private static final String ERROR_LOAD = "The menu loading is failed.";
    
   
    private TableService<Integer> tableService;
    private StudentService<List<StudentDTO>, List<GroupDTO>> studentService;
    private CourseService<List<CourseDTO>> courseService;
    private GroupService<List<GroupDTO>, Integer> groupService;
    private StudentCourseService<List<StudentDTO>, 
                                 List<CourseDTO>, 
                                 List<StudentCourseDTO>> studentCourseService;
    private View<List<GroupDTO>> universityView;

    public UniversityMenu(TableService<Integer> tableService,
            StudentService<List<StudentDTO>, List<GroupDTO>> studentService,
            CourseService<List<CourseDTO>> courseService, GroupService<List<GroupDTO>, Integer> groupService,
            StudentCourseService<List<StudentDTO>, List<CourseDTO>, List<StudentCourseDTO>> studentCourseService,
            View<List<GroupDTO>> universityView) {
        this.tableService = tableService;
        this.studentService = studentService;
        this.courseService = courseService;
        this.groupService = groupService;
        this.studentCourseService = studentCourseService;
        this.universityView = universityView;
    }

    public void load() throws ServicesException.LoadUniversityMenuFail {
        Scanner scanner = new Scanner(System.in);
        
        try {
            bootstrap();
            universityView.showMenuItems();
            
            switch (preventWrongInput(scanner)) {
            case 1:
                universityView.showFirstItemMessage();
                List<GroupDTO> groups = groupService.findGroupsWithLessOrEqualStudents(ensureIntInput(scanner));
                universityView.showStudentsNumberOfGroups(groups);
                break;
            case 2:
                
                
            }
        } catch (
                ServicesException.BootstrapFail 
                | ServicesException.FindGroupsWithLessOrEqualStudentsFailure
                | NoSuchElementException 
                | IllegalStateException e) {
            throw new ServicesException.LoadUniversityMenuFail(ERROR_LOAD, e);
        } finally {
            scanner.close();
        }
    }
    
    private int ensureIntInput(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            scanner.nextLine();
            universityView.showWrongInputWarning();
        }
        return scanner.nextInt();
    }
    
    private int preventWrongInput(Scanner scanner) {
        int output = 0;
        
        for ( ; ; ) {
            if (!scanner.hasNextInt()) {
                scanner.nextLine();
                universityView.showWrongInputWarning();
            } else {
                output = scanner.nextInt();
                
                if (output == 0 || output > 6) {
                    universityView.showWrongInputWarning();
                } else {
                    break;
                }
            }
        }
        return output;
    }
    
    private void bootstrap() throws ServicesException.BootstrapFail {
        try {
            tableService.creatTables();
            List<CourseDTO> courses = courseService.createCourses();
            studentService.createStudents();
            List<GroupDTO> groups = groupService.createGroups();
            List<StudentDTO> students = studentService.assignGroup(groups);
            studentCourseService.createStudentCourseRelation(students, courses);
        } catch (ServicesException.TableCreationFail 
                | ServicesException.CoursesCreationServiceFail 
                | ServicesException.GroupCreationFail 
                | ServicesException.StudentCreationFail
                | ServicesException.AssignGgoupToStudentsFail 
                | ServicesException.StudentsCoursesRelationFailure e) {
            throw new ServicesException.BootstrapFail(ERROR_BOOTSTRAP, e);
        }
    }
}
