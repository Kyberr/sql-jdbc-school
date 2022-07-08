package ua.com.foxminded.sql_jdbc_school.view;

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

public class CourseView {
    
    private static final Logger LOGGER = LogManager.getLogger();
    private static final int NORMAL_STATUS = 1;
    private static final int NORMAL_EXIT_STATUS = 0;
    private static final String WORD_EXIT = "exit";
    private static final String WORD_NO = "no";
    private static final String WORD_YES = "yes";
    private static final String EMPTY_STRING = "";
    private static final String CLOSE_CONNECTION_POOL_ERROR = "Closing connections of the pool failed.";
    
    ViewProcessor viewProcessor;
    CourseService courseService;
    StudentService studentService;
    DAOConnectionPool daoConnectionPool;
    
    public CourseView(ViewProcessor viewProcessor, CourseService courseService, 
                      StudentService studentService, DAOConnectionPool daoConnectionPool) {
        this.viewProcessor = viewProcessor;
        this.courseService = courseService;
        this.studentService = studentService;
        this.daoConnectionPool = daoConnectionPool;
    }

    public List<CourseModel> createCourses(String courseListFilename) throws ServiceException {
        List<CourseModel> coursesWithoutId =  courseService.createWithoutId(courseListFilename);
        return courseService.assignIdAndAddToDatabase(coursesWithoutId);
    }
    
    public void deleteAllCourses() throws ServiceException {
        courseService.deleteAll();
    }
    
    public void addStudentToCourse(Scanner scanner) throws ServiceException {
        first: for (;;) {
            List<StudentModel> studentsHaveGroupId = studentService.getStudentsHavingGroupId();
            viewProcessor.showStudents(studentsHaveGroupId);
            List<CourseModel> allCourses = courseService.getAllCourses();
            viewProcessor.showCourses(allCourses);
            viewProcessor.enterStudentId();
            int studentId = scanOnlyIntInput(scanner);
            viewProcessor.enterCourseId();
            int courseId = scanOnlyIntInput(scanner);
            viewProcessor.addStudentYesOrNo();
            String confirm = scanOnlyYesOrNo(scanner);
            int status = 0;

            if (confirm.equals(WORD_YES)) {
                status = studentService.addStudentToCourseById(studentId, courseId);
                
                if (status == NORMAL_STATUS) {
                    viewProcessor.studentHasBeenAddedToCourse();
                } else {
                    viewProcessor.studentHasNotBeenAddedToCourse();
                }
            } else if (confirm.equals(WORD_NO)) {
                break first;
            }

            for (;;) {
                viewProcessor.addStudentToCourseOrReturnMenu();
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
            viewProcessor.showStudentsOfCourse(studnetsHavingCourse);
            viewProcessor.deleteStudentFromCourseById();
            int studentId = scanOnlyIntInput(scanner);
            viewProcessor.enterCourseId();
            int courseId = scanOnlyIntInput(scanner);
            viewProcessor.confirmStudentDeleting();
            String yesOrNo = scanOnlyYesOrNo(scanner);

            if (yesOrNo.equals(WORD_YES)) {
                int status = courseService.deleteStudentFromCourseById(studentId, courseId);

                if (status == NORMAL_STATUS) {
                    viewProcessor.successStudentFromCourseDeleting();
                    viewProcessor.deleteAnotherStudentFromCourse();
                    String keyWord = scanOnlyEmptyStringOrWordExit(scanner);

                    if (keyWord.equals(WORD_EXIT)) {
                        break;
                    }
                } else {
                    viewProcessor.studentFromCourseDeletingFailed();
                }
            }
            viewProcessor.deleteAnotherStudentFromCourse();
            String keyWord = scanOnlyEmptyStringOrWordExit(scanner);

            if (keyWord.equals(WORD_EXIT)) {
                break;
            }
        }
    }
    
    public void findStudentsRelatedToCourse(Scanner couseIdScanner) throws ServiceException {
        List<CourseModel> allCourses = courseService.getAllCourses();
        viewProcessor.showCourses(allCourses);
        viewProcessor.enterCourseId();
        Integer courseID = scanOnlyIntInput(couseIdScanner);
        List<StudentModel> studentsOfCourse = studentService.getStudentsOfCourseById(courseID);
        viewProcessor.showStudentsOfCourse(studentsOfCourse);
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
            viewProcessor.showIncorrectInputWarning();
        }
        int input = scanner.nextInt();
        scanner.nextLine(); // it is used to clean the buffer from the empty string
        return input;
    }
    
    private void exitOrReturnMainMenu(Scanner scanner) {
        viewProcessor.returnMainMenuOrExit();

        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();

            if (input.equals(WORD_EXIT)) {
                closeConnectionPool();
                viewProcessor.executionHasBeenStopped();
                System.exit(NORMAL_EXIT_STATUS);
            } else if (input.equals(EMPTY_STRING)) {
                break;
            } else {
                viewProcessor.returnMainMenuOrExit();
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
