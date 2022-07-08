package ua.com.foxminded.sql_jdbc_school.view;

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

public class ViewFacade {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String CLOSE_CONNECTION_POOL_ERROR = "Closing connections of the pool failed.";
    private static final String ERROR_BOOTSTRAP = "The bootstraption has not performed.";
    private static final String ERROR_EXECUTE = "The ServiceController execution is failed.";
    private static final String WORD_EXIT = "exit";
    private static final int NORMAL_DELETE_STATUS = 0;
    private static final int NUMBER_OF_ITEMS = 6;

    private CourseView courseView;
    private GroupView groupView;
    private StudentView studentView;
    private DAOConnectionPool daoConnectionPool;
    private ViewProcessor viewProcessor;

    public ViewFacade(CourseView courseMenu, 
                      GroupView groupMenu, 
                      StudentView studentMenu, 
                      DAOConnectionPool daoConnectionPool, ViewProcessor viewProcessor) {
        this.courseView = courseMenu;
        this.groupView = groupMenu;
        this.studentView = studentMenu;
        this.daoConnectionPool = daoConnectionPool;
        this.viewProcessor = viewProcessor;
    }

    public void execute() throws ServiceException {
        Scanner scanner = new Scanner(System.in);

        try {
            for (;;) {
                viewProcessor.showMenuItems();
                int caseNumber = preventWrongInputOrExit(scanner);

                switch (caseNumber) {
                case 1:
                    groupView.findGroupsWithLessOrEqualStudents(scanner);
                    break;
                case 2:
                    courseView.findStudentsRelatedToCourse(scanner);
                    break;
                case 3:
                    studentView.addStudentToDatabase(scanner);
                    break;
                case 4:
                    studentView.deleteStudentFromDatabase(scanner);
                    break;
                case 5:
                    courseView.addStudentToCourse(scanner);
                    break;
                case 6:
                    courseView.removeStudentFromCourse(scanner);
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

    public void bootstrap(String courseNameListFilename, 
                          String firstNameListFilename, 
                          String lastNameListFilename) throws ServiceException {
        try {
            studentView.deleteAllStudents();
            groupView.deleteAllGroups();
            courseView.deleteAllCourses();
            List<CourseModel> courses = courseView.createCourses(courseNameListFilename);
            studentView.createStudents(firstNameListFilename, lastNameListFilename);
            List<GroupModel> groups = groupView.createGroups();
            List<StudentModel> studentsHavingGroupId = studentView.assignGroupToStudent(groups);
            studentView.assignCourseToStudent(studentsHavingGroupId, courses);
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
                    viewProcessor.showIncorrectInputWarning();
                }
            } else {
                output = scanner.nextInt();
                scanner.nextLine();

                if (output == 0 || output > NUMBER_OF_ITEMS) {
                    viewProcessor.showIncorrectInputWarning();
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
