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
import ua.com.foxminded.sql_jdbc_school.view.CourseView;
import ua.com.foxminded.sql_jdbc_school.view.StudentView;

public class CourseMenu {
    
    private static final Logger LOGGER = LogManager.getLogger();
    private static final int NORMAL_STATUS = 1;
    private static final String WORD_NO = "no";
    private static final String WORD_YES = "yes";
    private static final String WORD_EXIT = "exit";
    private static final int NORMAL_DELETE_STATUS = 0;
    private static final String EMPTY_STRING = "";
    private static final String CLOSE_CONNECTION_POOL_ERROR = "Closing connections of the pool failed.";
    
    StudentView<List<StudentModel>, Integer> studentMenuView;
    CourseService<List<CourseModel>, Integer> courseService;
    CourseView<List<CourseModel>, List<StudentModel>> courseMenuView;
    StudentService <List<StudentModel>, List<GroupModel>, String, Integer, List<CourseModel>> studentService;
    DAOConnectionPool daoConnectionPool;
    
    public CourseMenu(StudentView<List<StudentModel>, Integer> studentMenuView,
                      CourseService<List<CourseModel>, Integer> courseService,
                      CourseView<List<CourseModel>, List<StudentModel>> courseMenuView,
                      StudentService<List<StudentModel>, 
                                     List<GroupModel>, 
                                     String, 
                                     Integer, 
                                     List<CourseModel>> studentService,
                      DAOConnectionPool daoConnectionPool) {
        this.studentMenuView = studentMenuView;
        this.courseService = courseService;
        this.courseMenuView = courseMenuView;
        this.studentService = studentService;
        this.daoConnectionPool = daoConnectionPool;
    }

    public List<CourseModel> createCourses() throws ServiceException {
        return courseService.createCourses();
    }
    
    public void deleteAllCourses() throws ServiceException {
        courseService.deleteAllCourses();
    }
    
    public void addStudentToCourse(Scanner scanner) throws ServiceException {
        first: for (;;) {
            List<StudentModel> studentsHaveGroupId = studentService.getStudentsHavingGroupId();
            studentMenuView.showStudents(studentsHaveGroupId);
            List<CourseModel> allCourses = courseService.getAllCourses();
            courseMenuView.showCourses(allCourses);
            studentMenuView.enterStudentId();
            int studentId = scanOnlyIntInput(scanner);
            courseMenuView.enterCourseId();
            int courseId = scanOnlyIntInput(scanner);
            studentMenuView.addStudentYesOrNo();
            String confirm = scanOnlyYesOrNo(scanner);
            int status = 0;

            if (confirm.equals(WORD_YES)) {
                status = studentService.addStudentToCourseById(studentId, courseId);
                if (status == NORMAL_STATUS) {
                    studentMenuView.studentHasBeenAddedToCourse();
                } else {
                    studentMenuView.studentHasNotBeenAddedToCourse();
                }
            } else if (confirm.equals(WORD_NO)) {
                break first;
            }

            for (;;) {
                studentMenuView.addStudentToCourseOrReturnMenu();
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
            List<StudentModel> studnetCourse = studentService.getAllStudentsHavingCourse();
            courseMenuView.showStudentCourse(studnetCourse);
            courseMenuView.deleteStudentIdFromCourse();
            int studentId = scanOnlyIntInput(scanner);
            courseMenuView.enterCourseId();
            int courseId = scanOnlyIntInput(scanner);

            studentMenuView.confirmStudentDeleting();
            String yesOrNo = scanOnlyYesOrNo(scanner);

            if (yesOrNo.equals(WORD_YES)) {
                int status = courseService.deleteStudentFromCourse(studentId, courseId);

                if (status == NORMAL_STATUS) {
                    courseMenuView.successStudentFromCourseDeleting();
                    courseMenuView.deleteAnotherStudentFromCourse();
                    String keyWord = scanOnlyEmptyStringOrExitWord(scanner);

                    if (keyWord.equals(WORD_EXIT)) {
                        break;
                    }
                } else {
                    courseMenuView.failureStudentFromCourseDeleting();
                }
            }
            courseMenuView.deleteAnotherStudentFromCourse();
            String keyWord = scanOnlyEmptyStringOrExitWord(scanner);

            if (keyWord.equals(WORD_EXIT)) {
                break;
            }
        }
    }
    
    public void findStudentsRelatedToCourse(Scanner couseIdScanner) throws ServiceException {
        List<CourseModel> courses = courseService.getAllCourses();
        courseMenuView.showCourses(courses);
        courseMenuView.enterCourseId();
        Integer courseID = scanOnlyIntInput(couseIdScanner);
        List<StudentModel> studentCourse = studentService.getStudentsOfCourseById(courseID);
        courseMenuView.showStudentCourse(studentCourse);
        exitOrReturnMainMenu(couseIdScanner);
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
