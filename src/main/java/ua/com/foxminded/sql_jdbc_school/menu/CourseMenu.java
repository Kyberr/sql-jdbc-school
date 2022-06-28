package ua.com.foxminded.sql_jdbc_school.menu;

import java.util.List;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.com.foxminded.sql_jdbc_school.dao.DAOConnectionPool;
import ua.com.foxminded.sql_jdbc_school.dao.DAOException;
import ua.com.foxminded.sql_jdbc_school.model.CourseModel;
import ua.com.foxminded.sql_jdbc_school.model.GroupModel;
import ua.com.foxminded.sql_jdbc_school.model.StudentModel;
import ua.com.foxminded.sql_jdbc_school.service.CourseService;
import ua.com.foxminded.sql_jdbc_school.service.ServiceException;
import ua.com.foxminded.sql_jdbc_school.service.StudentService;
import ua.com.foxminded.sql_jdbc_school.view.CourseMenuView;

public class CourseMenu {
    
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String WORD_EXIT = "exit";
    private static final int NORMAL_DELETE_STATUS = 0;
    private static final String EMPTY_STRING = "";
    private static final String CLOSE_CONNECTION_POOL_ERROR = "Closing connections of the pool failed.";
    
    CourseService<List<CourseModel>, Integer> courseService;
    CourseMenuView<List<CourseModel>, List<StudentModel>> courseMenuView;
    StudentService <List<StudentModel>, List<GroupModel>, String, Integer, List<CourseModel>> studentService;
    DAOConnectionPool daoConnectionPool;
    
    
    
    public void findStudentsRelatedToCourse(Scanner couseIdScanner) throws ServiceException {
        List<CourseModel> courses = courseService.getAllCourses();
        courseMenuView.showCourses(courses);
        courseMenuView.enterCourseId();
        Integer courseID = scanOnlyIntInput(couseIdScanner);
        List<StudentModel> studentCourse = studentService.getStudentsOfCourseById(courseID);
        courseMenuView.showStudentCourse(studentCourse);
        exitOrReturnMainMenu(couseIdScanner);
    }
    
    private int scanOnlyIntInput(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            scanner.nextLine();
            courseMenuView.showIncorrectInputWarning();
        }
        int input = scanner.nextInt();
        scanner.nextLine(); // it is used to clean the buffer from the empty string
        return input;
    }
    
    private void exitOrReturnMainMenu(Scanner scanner) {
        courseMenuView.returnMainMenuOrExit();

        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();

            if (input.equals(WORD_EXIT)) {
                closeConnectionPool();
                courseMenuView.executionHasBeenStopped();
                System.exit(NORMAL_DELETE_STATUS);
            } else if (input.equals(EMPTY_STRING)) {
                break;
            } else {
                courseMenuView.returnMainMenuOrExit();
            }
        }
    }
    
    private void closeConnectionPool() {
        try {
            daoConnectionPool.closeConnections();
        } catch (DAOException e) {
            LOGGER.error(CLOSE_CONNECTION_POOL_ERROR, e);
        } 
    }
}
