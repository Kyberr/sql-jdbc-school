package ua.com.foxminded.sql_jdbc_school.services;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import ua.com.foxminded.sql_jdbc_school.services.dto.CourseDTO;
import ua.com.foxminded.sql_jdbc_school.services.dto.GroupDTO;
import ua.com.foxminded.sql_jdbc_school.services.dto.StudentCourseDTO;
import ua.com.foxminded.sql_jdbc_school.services.dto.StudentDTO;
import ua.com.foxminded.sql_jdbc_school.view.MenuView;

public class Menu {
    private static final String ERROR_BOOTSTRAP = "The bootstraption has not performed.";
    private static final String ERROR_LOAD = "The menu loading is failed.";
    private static final String EXIT = "exit";
    private static final String EMPTY_STRING = "";
    private static final int NORMAL_STATUS = 0;
    private static final int NUMBER_OF_ITEMS = 6;
    
   
    private TableService<Integer> tableService;
    private StudentService<List<StudentDTO>, List<GroupDTO>> studentService;
    private CourseService<List<CourseDTO>> courseService;
    private GroupService<List<GroupDTO>, Integer> groupService;
    private StudentCourseService<List<StudentDTO>, 
                                 List<CourseDTO>, 
                                 List<StudentCourseDTO>,
                                 Integer> studentCourseService;
    private MenuView<List<GroupDTO>, List<CourseDTO>, List<StudentCourseDTO>> menuView;

    public Menu(TableService<Integer> tableService, StudentService<List<StudentDTO>, List<GroupDTO>> studentService,
            CourseService<List<CourseDTO>> courseService, GroupService<List<GroupDTO>, Integer> groupService,
            StudentCourseService<List<StudentDTO>, List<CourseDTO>, List<StudentCourseDTO>, Integer> studentCourseService,
            MenuView<List<GroupDTO>, List<CourseDTO>, List<StudentCourseDTO>> menuView) {
        this.tableService = tableService;
        this.studentService = studentService;
        this.courseService = courseService;
        this.groupService = groupService;
        this.studentCourseService = studentCourseService;
        this.menuView = menuView;
    }

    public void load() throws ServicesException.LoadUniversityMenuFail {
        Scanner scanner = new Scanner(System.in);
        
        try {
            for ( ; ; ) {
                menuView.showMenuItems();
                
                switch (preventWrongInput(scanner)) {
                case 1:
                    menuView.showMessageOfItemOne();
                    List<GroupDTO> groups = groupService
                            .findGroupsWithLessOrEqualStudents(ensureIntInput(scanner));
                    menuView.showStudentsNumberOfGroups(groups);
                    exitOrReturnToMenu(scanner);
                    break;
                case 2:
                    List<CourseDTO> courses = courseService.getAllCourses();
                    menuView.showCourses(courses);
                    menuView.showMessageOfItemTwo();
                    List<StudentCourseDTO> studentCourse = studentCourseService
                            .getStudentsOfCourse(ensureIntInput(scanner));
                    menuView.showStudentsOfCourse(studentCourse);
                    exitOrReturnToMenu(scanner);
                    break;
                }
            }
        } catch (ServicesException.FindGroupsWithLessOrEqualStudentsFailure
                | NoSuchElementException 
                | IllegalStateException
                | ServicesException.GetAllCoursesFailure 
                | ServicesException.GetStudentsRelatedToCourseFaluer e) {
            throw new ServicesException.LoadUniversityMenuFail(ERROR_LOAD, e);
        } finally {
            scanner.close();
        }
    }
    
    public void bootstrap() throws ServicesException.BootstrapFail {
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
    
    private void exitOrReturnToMenu(Scanner scanner) {
        menuView.showFinalItemMessage();
        
        while(scanner.hasNextLine()) {
            scanner.nextLine();
            String input = scanner.nextLine();
            if (input.equals(EXIT)) {
                menuView.showFinalProgramMessage();
                System.exit(NORMAL_STATUS);
            } else if (input.equals(EMPTY_STRING)) {
                break;
            }
        }
    }
    
    private int ensureIntInput(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            scanner.nextLine();
            menuView.showWrongInputWarning();
        }
        return scanner.nextInt();
    }
    
    private int preventWrongInput(Scanner scanner) {
        int output = 0;
        
        for ( ; ; ) {
            if (!scanner.hasNextInt()) {
                scanner.nextLine();
                menuView.showWrongInputWarning();
            } else {
                output = scanner.nextInt();
                
                if (output == 0 || output > NUMBER_OF_ITEMS) {
                    menuView.showWrongInputWarning();
                } else {
                    break;
                }
            }
        }
        return output;
    }
}
