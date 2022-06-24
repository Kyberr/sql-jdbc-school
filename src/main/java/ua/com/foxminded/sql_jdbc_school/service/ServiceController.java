package ua.com.foxminded.sql_jdbc_school.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.com.foxminded.sql_jdbc_school.model.CourseModel;
import ua.com.foxminded.sql_jdbc_school.model.GroupModel;
import ua.com.foxminded.sql_jdbc_school.model.StudentModel;
import ua.com.foxminded.sql_jdbc_school.view.ServiceControllerView;

public class ServiceController {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String ERROR_BOOTSTRAP = "The bootstraption has not performed.";
    private static final String ERROR_EXECUTE = "The ServiceController execution is failed.";
    private static final String WORD_EXIT = "exit";
    private static final String EMPTY_STRING = "";
    private static final String WORD_NO = "no";
    private static final String WORD_YES = "yes";
    private static final int NORMAL_STATUS = 1;
    private static final int NORMAL_DEL_STATUS = 0;
    private static final int NUMBER_OF_ITEMS = 6;
    private static final int NORMAL_STATUS_OF_ADDING = 1;

    private StudentService<List<StudentModel>, List<GroupModel>, String, Integer, List<CourseModel>> studentService;
    private CourseService<List<CourseModel>, Integer> courseService;
    private GroupService<List<GroupModel>, Integer> groupService;
    private ServiceControllerView<List<GroupModel>, 
                                  List<CourseModel>, 
                                  List<StudentModel>, 
                                  List<StudentModel>, 
                                  Integer> serviceControllerView;

    public ServiceController(StudentService<List<StudentModel>, 
                                            List<GroupModel>, 
                                            String, Integer, 
                                            List<CourseModel>> studentService,
                             CourseService<List<CourseModel>, Integer> courseService, 
                             GroupService<List<GroupModel>, Integer> groupService,
                             ServiceControllerView<List<GroupModel>, 
                                                   List<CourseModel>, 
                                                   List<StudentModel>, 
                                                   List<StudentModel>, 
                                                   Integer> serviceControllerView) {
        this.studentService = studentService;
        this.courseService = courseService;
        this.groupService = groupService;
        this.serviceControllerView = serviceControllerView;
    }

    public void execute() throws ServiceException {
        Scanner scanner = new Scanner(System.in);

        try {
            for (;;) {
                serviceControllerView.showMenuItems();

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
            studentService.deleteAllStudents();
            groupService.deleteAllGroups();
            courseService.deleteAllCourses();
            List<CourseModel> courses = courseService.createCourses();
            studentService.createStudents();
            List<GroupModel> groups = groupService.createGroups();
            List<StudentModel> studentsHavingGroupId = studentService.assignGroupToStudent(groups);
            studentService.assignCourseToStudent(studentsHavingGroupId, courses);
        } catch (ServiceException e) {
            LOGGER.error(ERROR_BOOTSTRAP, e);
            throw new ServiceException(ERROR_BOOTSTRAP, e);
        }
    }

    private void removeStudentFromCourse(Scanner scanner) throws ServiceException {
        for (;;) {
            List<StudentModel> studnetCourse = studentService.getAllStudentsHavingCourse();
            serviceControllerView.showStudentCourse(studnetCourse);
            serviceControllerView.deleteStudentIdFromCourse();
            int studentId = scanOnlyIntInput(scanner);
            serviceControllerView.enterCourseId();
            int courseId = scanOnlyIntInput(scanner);

            serviceControllerView.confirmStudentDeleting();
            String yesOrNo = scanOnlyYesOrNo(scanner);

            if (yesOrNo.equals(WORD_YES)) {
                int status = courseService.deleteStudentFromCourse(studentId, courseId);

                if (status == NORMAL_STATUS) {
                    serviceControllerView.successStudentFromCourseDeleting();
                    serviceControllerView.deleteAnotherStudentFromCourse();
                    String keyWord = scanOnlyEmptyStringOrExitWord(scanner);

                    if (keyWord.equals(WORD_EXIT)) {
                        break;
                    }
                } else {
                    serviceControllerView.failureStudentFromCourseDeleting();
                }
            }
            serviceControllerView.deleteAnotherStudentFromCourse();
            String keyWord = scanOnlyEmptyStringOrExitWord(scanner);

            if (keyWord.equals(WORD_EXIT)) {
                break;
            }
        }
    }

    private void addStudentToCourse(Scanner scanner) throws ServiceException {
        first: for (;;) {
            List<StudentModel> studentsHaveGroupId = studentService.getStudentsHavingGroupId();
            serviceControllerView.showStudents(studentsHaveGroupId);
            List<CourseModel> allCourses = courseService.getAllCourses();
            serviceControllerView.showCourses(allCourses);
            serviceControllerView.enterStudentId();
            int studentId = scanOnlyIntInput(scanner);
            serviceControllerView.enterCourseId();
            int courseId = scanOnlyIntInput(scanner);
            serviceControllerView.addStudentYesOrNo();
            String confirm = scanOnlyYesOrNo(scanner);
            int status = 0;

            if (confirm.equals(WORD_YES)) {
                status = studentService.addStudentToCourseById(studentId, courseId);
                if (status == NORMAL_STATUS_OF_ADDING) {
                    serviceControllerView.studentHasBeenAddedToCourse();
                } else {
                    serviceControllerView.studentHasNotBeenAddedToCourse();
                }
            } else if (confirm.equals(WORD_NO)) {
                break first;
            }

            for (;;) {
                serviceControllerView.addStudentToCourseOrReturnMenu();
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
            List<StudentModel> students = studentService.getAllStudents();
            serviceControllerView.showStudents(students);
            serviceControllerView.enterStudentId();
            int studentId = scanOnlyIntInput(scanner);
            serviceControllerView.confirmStudentDeleting();
            String keyWord = scanOnlyYesOrNo(scanner);

            if (keyWord.equals(WORD_YES)) {
                int status = studentService.deleteStudent(studentId);

                if (status == NORMAL_STATUS) {
                    serviceControllerView.studentHasBeenDeleted(studentId);
                } else {
                    serviceControllerView.studentHasNotBeenDeleted(studentId);
                }
            }
            for (;;) {
                serviceControllerView.deleteStudentOrReturnMainMenu();
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
        for (;;) {
            serviceControllerView.enterLastName();
            String lastName = scanner.nextLine();
            serviceControllerView.enterFirstName();
            String firstName = scanner.nextLine();
            enterStudentNameAndAddToDatabase(lastName, firstName, scanner);
            serviceControllerView.addStudentToDatabaseOrReturnMenu();
            String keyWord = scanOnlyEmptyStringOrExitWord(scanner);

            if (keyWord.equals(WORD_EXIT)) {
                break;
            }
        }
    }

    private String scanOnlyEmptyStringOrExitWord(Scanner scanner) {
        String keyWord = "";
        for (;;) {
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

    private void enterStudentNameAndAddToDatabase(String lastName, String firstName, Scanner scanner)
            throws ServiceException {
        for (;;) {
            serviceControllerView.addStudentYesOrNo();
            String input = scanner.nextLine();

            if (input.equals(WORD_YES)) {
                if (studentService.addStudent(lastName, firstName) == NORMAL_STATUS_OF_ADDING) {
                    serviceControllerView.studentHasBeenAddedToDatabase();
                    break;
                }
            } else if (input.equals(WORD_NO)) {
                break;
            }
        }
    }

    private void findStudentsRelatedToCourse(Scanner couseIdScanner) throws ServiceException {
        List<CourseModel> courses = courseService.getAllCourses();
        serviceControllerView.showCourses(courses);
        serviceControllerView.enterCourseId();
        Integer courseID = scanOnlyIntInput(couseIdScanner);
        List<StudentModel> studentCourse = studentService.getStudentsOfCourseById(courseID);
        serviceControllerView.showStudentCourse(studentCourse);
        exitOrReturnMainMenu(couseIdScanner);
    }

    private void findGroupsWithLessOrEqualStudents(Scanner studentsNumberScanner) throws ServiceException {
        serviceControllerView.enterNumberOfStudents();
        List<GroupModel> groups = groupService
                .findGroupsWithLessOrEqualStudents(scanOnlyIntInput(studentsNumberScanner));
        serviceControllerView.showNumberOfStudentsInGroups(groups);
        exitOrReturnMainMenu(studentsNumberScanner);
    }

    private void exitOrReturnMainMenu(Scanner scanner) {
        serviceControllerView.returnMainMenuOrExit();

        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();

            if (input.equals(WORD_EXIT)) {
                serviceControllerView.executionHasBeenStopped();
                System.exit(NORMAL_DEL_STATUS);
            } else if (input.equals(EMPTY_STRING)) {
                break;
            } else {
                serviceControllerView.returnMainMenuOrExit();
            }
        }
    }

    private int scanOnlyIntInput(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            scanner.nextLine();
            serviceControllerView.showIncorrectInputWarning();
        }
        int input = scanner.nextInt();
        scanner.nextLine(); // it is used to clean the buffer from the empty string
        return input;
    }

    private int preventWrongInputOrExit(Scanner scanner) {
        int output = 0;

        for (;;) {
            if (!scanner.hasNextInt()) {
                if (scanner.nextLine().equals(WORD_EXIT)) {
                    System.exit(NORMAL_DEL_STATUS);
                } else {
                    serviceControllerView.showIncorrectInputWarning();
                }
            } else {
                output = scanner.nextInt();
                scanner.nextLine();

                if (output == 0 || output > NUMBER_OF_ITEMS) {
                    serviceControllerView.showIncorrectInputWarning();
                } else {
                    break;
                }
            }
        }
        return output;
    }
}
