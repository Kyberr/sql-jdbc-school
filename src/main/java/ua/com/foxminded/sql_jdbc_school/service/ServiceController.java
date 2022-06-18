package ua.com.foxminded.sql_jdbc_school.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.com.foxminded.sql_jdbc_school.service.dto.CourseDto;
import ua.com.foxminded.sql_jdbc_school.service.dto.GroupDto;
import ua.com.foxminded.sql_jdbc_school.service.dto.StudentDto;
import ua.com.foxminded.sql_jdbc_school.service.dto.StudentDto;
import ua.com.foxminded.sql_jdbc_school.view.ServiceControllerView;

public class ServiceController {
	private static final Logger LOGGER = LogManager.getLogger();
    private static final String ERROR_BOOTSTRAP = "The bootstraption has not performed.";
    private static final String ERROR_EXECUTE = "The menu execution is failed.";
    private static final String WORD_EXIT = "exit";
    private static final String EMPTY_STRING = "";
    private static final String WORD_NO = "no";
    private static final String WORD_YES = "yes";
    private static final int NORMAL_STATUS = 1;
    private static final int NORMAL_DEL_STATUS = 0;
    private static final int NUMBER_OF_ITEMS = 6;
    private static final int NORMAL_STATUS_OF_ADDING = 1;
   
    private TableService<Integer> tableService;
    private StudentService<List<StudentDto>, List<GroupDto>, String, Integer> studentService;
    private CourseService<List<CourseDto>, Integer> courseService;
    private GroupService<List<GroupDto>, Integer> groupService;
    private StudentCourseService<List<StudentDto>, List<CourseDto>, List<StudentDto>,
                                 Integer> studentCourseService;
    private ServiceControllerView<List<GroupDto>, List<CourseDto>, List<StudentDto>, List<StudentDto>, 
                     Integer> menuView;
    public ServiceController(TableService<Integer> tableService, StudentService<List<StudentDto>, List<GroupDto>, 
                String, Integer> studentService,
                CourseService<List<CourseDto>, Integer> courseService, GroupService<List<GroupDto>, 
                       Integer> groupService,
                StudentCourseService<List<StudentDto>, List<CourseDto>, List<StudentDto>, 
                              Integer> studentCourseService,
                ServiceControllerView<List<GroupDto>, List<CourseDto>, List<StudentDto>, List<StudentDto>, 
                         Integer> menuView) {
        this.tableService = tableService;
        this.studentService = studentService;
        this.courseService = courseService;
        this.groupService = groupService;
        this.studentCourseService = studentCourseService;
        this.menuView = menuView;
    }

    public void execute() throws ServiceException {
        Scanner scanner = new Scanner(System.in);
        
        try {
            for ( ; ; ) {
                menuView.showMenuItems();
                
                switch (preventWrongInputOrExit(scanner)) {
                case 1:
                    findGroupsWithLessOrEqualStudents(scanner);
                    break;
                case 2:
                    findStudentsRelatedToCourse(scanner);
                    break;
                case 3:
                    addStudentToDatabase(scanner);
                    break;
                case 4:
                    deleteStudentFromDatabase(scanner);
                    break;
                case 5:
                    addStudentToCourse(scanner);
                    break;
                case 6:
                    removeStudentFromCourse(scanner); 
                    break;
                }
            }
        } catch (ServiceException | NoSuchElementException | IllegalStateException e) {
        	LOGGER.error(ERROR_EXECUTE, e);
            throw new ServiceException(ERROR_EXECUTE, e);
        } finally {
            scanner.close();
        }
    }
    
    public void bootstrap() throws ServiceException {
        try {
            tableService.createTables();
            List<CourseDto> courses = courseService.createCourses();
            studentService.createStudents();
            List<GroupDto> groups = groupService.createGroups();
            List<StudentDto> studentsHavingGroupId = studentService.assignGroup(groups);
            tableService.createStudentCourseTable();
            studentCourseService.assignStudentToCourse(studentsHavingGroupId, courses);
        } catch (ServiceException e) {
        	LOGGER.error(ERROR_BOOTSTRAP, e);
            throw new ServiceException(ERROR_BOOTSTRAP, e);
        }
    }
    
    private void removeStudentFromCourse(Scanner scanner) throws ServiceException {
        for (;;) {
            List<StudentDto> studnetCourse = studentCourseService.getAllStudentsHavingCourse();
            menuView.showStudentCourse(studnetCourse);
            menuView.deleteStudentIdFromCourse();
            int studentId = scanOnlyIntInput(scanner);
            menuView.enterCourseId();
            int courseId = scanOnlyIntInput(scanner);

            menuView.confirmStudentDeleting();
            String yesOrNo = scanOnlyYesOrNo(scanner);

            if (yesOrNo.equals(WORD_YES)) {
                int status = studentCourseService.deleteStudentFromCourse(studentId, courseId);

                if (status == NORMAL_STATUS) {
                    menuView.successStudentFromCourseDeleting();
                    menuView.deleteAnotherStudentFromCourse();
                    String keyWord = scanOnlyEmptyStringOrExitWord(scanner);

                    if (keyWord.equals(WORD_EXIT)) {
                        break;
                    }
                } else {
                    menuView.failureStudentFromCourseDeleting();
                }
            }
            menuView.deleteAnotherStudentFromCourse();
            String keyWord = scanOnlyEmptyStringOrExitWord(scanner);

            if (keyWord.equals(WORD_EXIT)) {
                break;
            }
        }
    }
    
    private void addStudentToCourse(Scanner scanner) throws ServiceException {
        first: for (;;) {
            List<StudentDto> studentsHaveGroupId = studentService.getStudentsHavingGroupId();
            menuView.showStudents(studentsHaveGroupId);
            List<CourseDto> allCourses = courseService.getAllCourses();
            menuView.showCourses(allCourses);
            menuView.enterStudentId();
            int studentId = scanOnlyIntInput(scanner);
            menuView.enterCourseId();
            int courseId = scanOnlyIntInput(scanner);
            menuView.addStudentYesOrNo();
            String confirm = scanOnlyYesOrNo(scanner);
            int status = 0;

            if (confirm.equals(WORD_YES)) {
                status = studentCourseService.addStudentToCourse(studentId, courseId);
                if (status == NORMAL_STATUS_OF_ADDING) {
                    menuView.studentHasBeenAddedToCourse();
                } else {
                    menuView.studentHasNotBeenAddedToCourse();
                }
            } else if (confirm.equals(WORD_NO)) {
                break first;
            }

            for ( ; ; ) {
                menuView.addStudentToCourseOrReturnMenu();
                String input = scanner.nextLine();
                
                if (input.equals(EMPTY_STRING)) {
                    continue first;
                } else if (input.equals(WORD_EXIT)) {
                    break first;
                }
            }
        }
    }
    
    private String scanOnlyYesOrNo(Scanner scanner) {
        String confirm = "";
        
        for (;;) {
            confirm = scanner.nextLine();

            if (confirm.equals(WORD_YES)) {
                return confirm;
            } else if (confirm.equals(WORD_NO)) {
                return confirm;
            }
        }
    }
    
    private void deleteStudentFromDatabase(Scanner scanner) throws ServiceException {
        first: for (;;) {
            List<StudentDto> students = studentService.getAllStudents();
            menuView.showStudents(students);
            menuView.enterStudentId();
            int studentId = scanOnlyIntInput(scanner);
            menuView.confirmStudentDeleting();
            String keyWord = scanOnlyYesOrNo(scanner);

            if (keyWord.equals(WORD_YES)) {
                int status = studentService.deleteStudent(studentId);

                if (status == NORMAL_STATUS) {
                    menuView.studentHasBeenDeleted(studentId);
                } else {
                    menuView.studentHasNotBeenDeleted(studentId);
                }
            }
            for (;;) {
                menuView.deleteStudentOrReturnMainMenu();
                String input = scanner.nextLine();

                if (input.equals(WORD_EXIT)) {
                    break first;
                } else if (input.equals(EMPTY_STRING)) {
                    continue first;
                }
            }
        }
    }
    
    private void addStudentToDatabase(Scanner scanner) throws ServiceException {
        for ( ; ; ) {
            menuView.enterLastName();
            String lastName = scanner.nextLine();
            menuView.enterFirstName();
            String firstName = scanner.nextLine();
            enterStudentNameAndAddToDatabase(lastName, firstName, scanner);
            menuView.addStudentToDatabaseOrReturnMenu();
            String keyWord = scanOnlyEmptyStringOrExitWord(scanner);
            
            if (keyWord.equals(WORD_EXIT)) {
                break;
            }
        }
    }
    
    private String scanOnlyEmptyStringOrExitWord(Scanner scanner) {
        String keyWord = "";
        for ( ; ; ) {
            String input = scanner.nextLine();
            
            if (input.equals(EMPTY_STRING)) {
                break;
            } else if (input.equals(WORD_EXIT)) {
                keyWord = WORD_EXIT;
                break;
            }
        }
        return keyWord;
    }
    
    private void enterStudentNameAndAddToDatabase(String lastName, 
                                                  String firstName, 
                                                  Scanner scanner) throws ServiceException {
        for (;;) {
            menuView.addStudentYesOrNo();
            String input = scanner.nextLine();
            
            if (input.equals(WORD_YES)) {
                if (studentService.addStudent(lastName, firstName) == NORMAL_STATUS_OF_ADDING) {
                    menuView.studentHasBeenAddedToDatabase();
                    break;
                }
            } else if (input.equals(WORD_NO)) {
                break;
            }
        }
    }
    
    private void findStudentsRelatedToCourse(Scanner couseIdScanner) throws ServiceException {
        List<CourseDto> courses = courseService.getAllCourses();
        menuView.showCourses(courses);
        menuView.enterCourseId();
        Integer courseID = scanOnlyIntInput(couseIdScanner);
        List<StudentDto> studentCourse = studentCourseService.getStudentsOfCourse(courseID);
        menuView.showStudentCourse(studentCourse);
        exitOrReturnMainMenu(couseIdScanner);
    }
    
    private void findGroupsWithLessOrEqualStudents(Scanner studentsNumberScanner) 
            throws ServiceException {
        menuView.enterNumberOfStudents();
        List<GroupDto> groups = groupService
                .findGroupsWithLessOrEqualStudents(scanOnlyIntInput(studentsNumberScanner));
        menuView.showNumberOfStudentsInGroups(groups);
        exitOrReturnMainMenu(studentsNumberScanner);
    }
    
    private void exitOrReturnMainMenu(Scanner scanner) {
        menuView.returnMainMenuOrExit();
        
        while(scanner.hasNextLine()) {
            String input = scanner.nextLine();
            
            if (input.equals(WORD_EXIT)) {
                menuView.executionHasBeenStopped();
                System.exit(NORMAL_DEL_STATUS);
            } else if (input.equals(EMPTY_STRING)) {
                break;
            } else {
                menuView.returnMainMenuOrExit();
            }
        }
    }
    
    private int scanOnlyIntInput(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            scanner.nextLine();
            menuView.showIncorrectInputWarning();
        }
        int input = scanner.nextInt();
        scanner.nextLine(); // it is used to clean the buffer from the empty string       
        return input;
    }
    
    private int preventWrongInputOrExit(Scanner scanner) {
        int output = 0;
        
        for ( ; ; ) {
            if (!scanner.hasNextInt()) {
                if (scanner.nextLine().equals(WORD_EXIT)) {
                    System.exit(NORMAL_DEL_STATUS);
                } else {
                    menuView.showIncorrectInputWarning(); 
                }
            } else {
                output = scanner.nextInt();
                scanner.nextLine();
                
                if (output == 0 || output > NUMBER_OF_ITEMS) {
                    menuView.showIncorrectInputWarning();
                } else {
                    break;
                }
            }
        }
        return output;
    }
}
