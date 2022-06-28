package ua.com.foxminded.sql_jdbc_school.menu;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.com.foxminded.sql_jdbc_school.dao.DAOConnectionFactory;
import ua.com.foxminded.sql_jdbc_school.dao.DAOConnectionPool;
import ua.com.foxminded.sql_jdbc_school.dao.DAOException;
import ua.com.foxminded.sql_jdbc_school.dao.jdbc.JdbcDAOConnectionFactory;
import ua.com.foxminded.sql_jdbc_school.dao.jdbc.JdbcDAOConnectionPool;
import ua.com.foxminded.sql_jdbc_school.model.CourseModel;
import ua.com.foxminded.sql_jdbc_school.model.GroupModel;
import ua.com.foxminded.sql_jdbc_school.model.StudentModel;
import ua.com.foxminded.sql_jdbc_school.service.CourseService;
import ua.com.foxminded.sql_jdbc_school.service.GroupService;
import ua.com.foxminded.sql_jdbc_school.service.ServiceException;
import ua.com.foxminded.sql_jdbc_school.service.StudentService;
import ua.com.foxminded.sql_jdbc_school.view.MenuView;

public class Menu {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String CLOSE_CONNECTION_POOL_ERROR = "Closing connections of the pool failed.";
    private static final String ERROR_BOOTSTRAP = "The bootstraption has not performed.";
    private static final String ERROR_EXECUTE = "The ServiceController execution is failed.";
    private static final String WORD_EXIT = "exit";
    private static final String EMPTY_STRING = "";
    private static final String WORD_NO = "no";
    private static final String WORD_YES = "yes";
    private static final int NORMAL_STATUS = 1;
    private static final int NORMAL_DELETE_STATUS = 0;
    private static final int NUMBER_OF_ITEMS = 6;
    private static final int NORMAL_STATUS_OF_ADDING = 1;

    private StudentService<List<StudentModel>, 
                           List<GroupModel>, 
                           String, 
                           Integer, 
                           List<CourseModel>> studentService;
    private CourseService<List<CourseModel>, Integer> courseService;
    private GroupService<List<GroupModel>, Integer> groupService;
    private MenuView<List<GroupModel>, 
                                  List<CourseModel>, 
                                  List<StudentModel>, 
                                  List<StudentModel>, 
                                  Integer> serviceControllerView;
    private DAOConnectionFactory jdbcDaoConnectionFactory = new JdbcDAOConnectionFactory();
    private DAOConnectionPool jdbcDaoConnectionPool = new JdbcDAOConnectionPool(jdbcDaoConnectionFactory);

    public Menu(StudentService<List<StudentModel>, 
                                            List<GroupModel>, 
                                            String, Integer, 
                                            List<CourseModel>> studentService,
                             CourseService<List<CourseModel>, Integer> courseService, 
                             GroupService<List<GroupModel>, Integer> groupService,
                             MenuView<List<GroupModel>, 
                                                   List<CourseModel>, 
                                                   List<StudentModel>, 
                                                   List<StudentModel>, 
                                                   Integer> serviceControllerView, 
                             DAOConnectionFactory jdbcDaoConnectionFactory, 
                             DAOConnectionPool jdbcDaoConnectionPool) {
        this.studentService = studentService;
        this.courseService = courseService;
        this.groupService = groupService;
        this.serviceControllerView = serviceControllerView;
        this.jdbcDaoConnectionFactory = jdbcDaoConnectionFactory;
        this.jdbcDaoConnectionPool = jdbcDaoConnectionPool;
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

    private void closeConnectionPool() {
        try {
            jdbcDaoConnectionPool.closeConnections();
        } catch (DAOException e) {
            LOGGER.error(CLOSE_CONNECTION_POOL_ERROR, e);
        } 
    }

    private int preventWrongInputOrExit(Scanner scanner) {
        int output = 0;

        for (;;) {
            if (!scanner.hasNextInt()) {
                if (scanner.nextLine().equals(WORD_EXIT)) {
                    closeConnectionPool();
                    System.exit(NORMAL_DELETE_STATUS);
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
