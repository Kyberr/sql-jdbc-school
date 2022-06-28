package ua.com.foxminded.sql_jdbc_school.menu;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.com.foxminded.sql_jdbc_school.dao.DAOConnectionPool;
import ua.com.foxminded.sql_jdbc_school.dao.DAOException;
import ua.com.foxminded.sql_jdbc_school.model.CourseModel;
import ua.com.foxminded.sql_jdbc_school.model.GroupModel;
import ua.com.foxminded.sql_jdbc_school.model.StudentModel;
import ua.com.foxminded.sql_jdbc_school.service.ServiceException;
import ua.com.foxminded.sql_jdbc_school.view.MenuView;

public class Menu {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String CLOSE_CONNECTION_POOL_ERROR = "Closing connections of the pool failed.";
    private static final String ERROR_BOOTSTRAP = "The bootstraption has not performed.";
    private static final String ERROR_EXECUTE = "The ServiceController execution is failed.";
    private static final String WORD_EXIT = "exit";
    private static final int NORMAL_DELETE_STATUS = 0;
    private static final int NUMBER_OF_ITEMS = 6;

    private CourseMenu courseMenu;
    private GroupMenu groupMenu;
    private StudentMenu studentMenu;
    private DAOConnectionPool daoConnectionPool;
    private MenuView menuView;

    public Menu(CourseMenu courseMenu, GroupMenu groupMenu, StudentMenu studentMenu,
            DAOConnectionPool daoConnectionPool, MenuView menuView) {
        this.courseMenu = courseMenu;
        this.groupMenu = groupMenu;
        this.studentMenu = studentMenu;
        this.daoConnectionPool = daoConnectionPool;
        this.menuView = menuView;
    }

    public void execute() throws ServiceException {
        Scanner scanner = new Scanner(System.in);

        try {
            for (;;) {
                menuView.showMenuItems();
                int caseNumber = preventWrongInputOrExit(scanner);

                switch (caseNumber) {
                case 1:
                    groupMenu.findGroupsWithLessOrEqualStudents(scanner);
                    break;
                case 2:
                    courseMenu.findStudentsRelatedToCourse(scanner);
                    break;
                case 3:
                    studentMenu.addStudentToDatabase(scanner);
                    break;
                case 4:
                    studentMenu.deleteStudentFromDatabase(scanner);
                    break;
                case 5:
                    courseMenu.addStudentToCourse(scanner);
                    break;
                case 6:
                    courseMenu.removeStudentFromCourse(scanner);
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
            studentMenu.deleteAllStudents();
            groupMenu.deleteAllGroups();
            courseMenu.deleteAllCourses();
            List<CourseModel> courses = courseMenu.createCourses();
            studentMenu.createStudents();
            List<GroupModel> groups = groupMenu.createGroups();
            List<StudentModel> studentsHavingGroupId = studentMenu.assignGroupToStudent(groups);
            studentMenu.assignCourseToStudent(studentsHavingGroupId, courses);
        } catch (ServiceException e) {
            LOGGER.error(ERROR_BOOTSTRAP, e);
            throw new ServiceException(ERROR_BOOTSTRAP, e);
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
    
    private void closeConnectionPool() {
        try {
            daoConnectionPool.closeConnections();
        } catch (DAOException e) {
            LOGGER.error(CLOSE_CONNECTION_POOL_ERROR, e);
        } 
    }
}
