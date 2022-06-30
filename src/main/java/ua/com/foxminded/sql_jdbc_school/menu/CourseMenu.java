package ua.com.foxminded.sql_jdbc_school.menu;

import java.util.List;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.com.foxminded.sql_jdbc_school.dao.DAOConnectionPool;
import ua.com.foxminded.sql_jdbc_school.dao.DAOException;
import ua.com.foxminded.sql_jdbc_school.model.CourseModel;
import ua.com.foxminded.sql_jdbc_school.model.StudentModel;
import ua.com.foxminded.sql_jdbc_school.service.CourseService;
import ua.com.foxminded.sql_jdbc_school.service.ServiceException;
import ua.com.foxminded.sql_jdbc_school.service.StudentService;
import ua.com.foxminded.sql_jdbc_school.view.CourseView;
import ua.com.foxminded.sql_jdbc_school.view.StudentView;

public class CourseMenu {
    
    private static final Logger LOGGER = LogManager.getLogger();
    private static final int NORMAL_STATUS = 1;
    private static final int NORMAL_EXIT_STATUS = 0;
    private static final String WORD_EXIT = "exit";
    private static final String WORD_NO = "no";
    private static final String WORD_YES = "yes";
    private static final String EMPTY_STRING = "";
    private static final String CLOSE_CONNECTION_POOL_ERROR = "Closing connections of the pool failed.";
    
    StudentView studenView;
    CourseService courseService;
    CourseView courseView;
    StudentService studentService;
    DAOConnectionPool daoConnectionPool;
    
    public CourseMenu(StudentView studentView, CourseService courseService, CourseView courseView, 
                      StudentService studentService, DAOConnectionPool daoConnectionPool) {
        this.studenView = studentView;
        this.courseService = courseService;
        this.courseView = courseView;
        this.studentService = studentService;
        this.daoConnectionPool = daoConnectionPool;
    }

    public List<CourseModel> createCourses() throws ServiceException {
        return courseService.create();
    }
    
    public void deleteAllCourses() throws ServiceException {
        courseService.deleteAll();
    }
    
    public void addStudentToCourse(Scanner scanner) throws ServiceException {
        first: for (;;) {
            List<StudentModel> studentsHaveGroupId = studentService.getStudentsHavingGroupId();
            studenView.showStudents(studentsHaveGroupId);
            List<CourseModel> allCourses = courseService.getAllCourses();
            courseView.showCourses(allCourses);
            studenView.enterStudentId();
            int studentId = scanOnlyIntInput(scanner);
            courseView.enterCourseId();
            int courseId = scanOnlyIntInput(scanner);
            studenView.addStudentYesOrNo();
            String confirm = scanOnlyYesOrNo(scanner);
            int status = 0;

            if (confirm.equals(WORD_YES)) {
                status = studentService.addStudentToCourseById(studentId, courseId);
                
                if (status == NORMAL_STATUS) {
                    studenView.studentHasBeenAddedToCourse();
                } else {
                    studenView.studentHasNotBeenAddedToCourse();
                }
            } else if (confirm.equals(WORD_NO)) {
                break first;
            }

            for (;;) {
                studenView.addStudentToCourseOrReturnMenu();
                String input = scanner.nextLine();

                if (input.equals(EMPTY_STRING)) {
                    continue first;
                } else if (input.equals(WORD_EXIT)) {
                    break first;
                }
            }
        }
    }
    
    public void removeStudentFromCourse(Scanner scanner) throws ServiceException {
        for (;;) {
            List<StudentModel> studnetsHavingCourse = studentService.getAllStudentsHavingCourse();
            courseView.showStudentsOfCourse(studnetsHavingCourse);
            courseView.deleteStudentFromCourseById();
            int studentId = scanOnlyIntInput(scanner);
            courseView.enterCourseId();
            int courseId = scanOnlyIntInput(scanner);
            studenView.confirmStudentDeleting();
            String yesOrNo = scanOnlyYesOrNo(scanner);

            if (yesOrNo.equals(WORD_YES)) {
                int status = courseService.deleteStudentFromCourseById(studentId, courseId);

                if (status == NORMAL_STATUS) {
                    courseView.successStudentFromCourseDeleting();
                    courseView.deleteAnotherStudentFromCourse();
                    String keyWord = scanOnlyEmptyStringOrWordExit(scanner);

                    if (keyWord.equals(WORD_EXIT)) {
                        break;
                    }
                } else {
                    courseView.studentFromCourseDeletingFailed();
                }
            }
            courseView.deleteAnotherStudentFromCourse();
            String keyWord = scanOnlyEmptyStringOrWordExit(scanner);

            if (keyWord.equals(WORD_EXIT)) {
                break;
            }
        }
    }
    
    public void findStudentsRelatedToCourse(Scanner couseIdScanner) throws ServiceException {
        List<CourseModel> allCourses = courseService.getAllCourses();
        courseView.showCourses(allCourses);
        courseView.enterCourseId();
        Integer courseID = scanOnlyIntInput(couseIdScanner);
        List<StudentModel> studentsOfCourse = studentService.getStudentsOfCourseById(courseID);
        courseView.showStudentsOfCourse(studentsOfCourse);
        exitOrReturnMainMenu(couseIdScanner);
    }
    
    private String scanOnlyEmptyStringOrWordExit(Scanner scanner) {
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
    
    private int scanOnlyIntInput(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            scanner.nextLine();
            courseView.showIncorrectInputWarning();
        }
        int input = scanner.nextInt();
        scanner.nextLine(); // it is used to clean the buffer from the empty string
        return input;
    }
    
    private void exitOrReturnMainMenu(Scanner scanner) {
        courseView.returnMainMenuOrExit();

        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();

            if (input.equals(WORD_EXIT)) {
                closeConnectionPool();
                courseView.executionHasBeenStopped();
                System.exit(NORMAL_EXIT_STATUS);
            } else if (input.equals(EMPTY_STRING)) {
                break;
            } else {
                courseView.returnMainMenuOrExit();
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
