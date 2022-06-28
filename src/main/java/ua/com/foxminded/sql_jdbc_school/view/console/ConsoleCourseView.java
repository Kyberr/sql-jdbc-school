package ua.com.foxminded.sql_jdbc_school.view.console;

import java.io.PrintWriter;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import ua.com.foxminded.sql_jdbc_school.model.CourseModel;
import ua.com.foxminded.sql_jdbc_school.model.StudentModel;
import ua.com.foxminded.sql_jdbc_school.view.CourseView;

public class ConsoleCourseView implements CourseView<List<CourseModel>, List<StudentModel>> {
    
    private static final String FAILURE_STUDENT_FROM_COURSE_DELETING = "There is no such student "
            + "in the course.";
    private static final String ADD_ANOTHER_STUDENT_FROM_COURSE = "Press the \"Enter\" key to delete another "
            + "student from a course or write \"exit\" and press the \"Enter\" key.";
    private static final String SUCCESS_STUDENT_FROM_COURSE_DELETING = "The student has been "
            + "deleted from the course.";
    private static final String DELETE_STUDENT_ID_FROM_COURSE = "To delete a student from a course first enter the "
            + "student's id from the list above and press the \"Enter\" key:";
    private static final String RETURN_MAIN_MENU_OR_EXIT = "Press the \"Enter\" key to return to the main "
            + "menu or write \"exit\" and press the \"Enter\" key.";
    private static final String EXECUTION_HAS_BEEN_STOPPED = "The program execution has been stopped.";
    private static final String INCORRECT_INPUT = "The input must be the number of the corresponding value.\n";
    private static final String ENTER_COURSE_ID = "Enter a course ID from the list above:";
    private static final String STUDENT_FIRST_NAME = "First name";
    private static final String STUDENT_LAST_NAME = "Last name";
    private static final String GROUP_ID = "Group ID";
    private static final String STUDENT_ID = "Student ID";
    private static final String NO_STUDENTS_OF_COURSE = "No students are studying the specified course.\n";
    private static final String COURSE_DESCRIPTION = "Course description";
    private static final String COURSE_NAME = "Course name";
    private static final String COURSE_ID = "Course ID";
    private static final String STUDENT_COURSES_FORMAT = "| %-10s| %-9s| %-10s| %-10s| %-9s| %-23s| %-18s|\n";
    private static final int STUDENT_COURSES_LINE_LENGTH = 104;
    private static final String COURSES_FORMAT = "| %-9s| %-25s| %-20s|\n";
    private static final String COURSES_LINE_FORMAT = "%50s\n";
    private static final int COURSE_LINE_LENGTH = 61;
    private static final int FIST_LINE = 0;
    private static final char NULL = '\0';
    private static final char HATCH = '-';
    
    @Override
    public void failureStudentFromCourseDeleting() {
        PrintWriter printWriter = new PrintWriter(System.out, true);
        printWriter.println(FAILURE_STUDENT_FROM_COURSE_DELETING);
    }
    
    @Override
    public void deleteAnotherStudentFromCourse() {
        PrintWriter printWriter = new PrintWriter(System.out, true);
        printWriter.println(ADD_ANOTHER_STUDENT_FROM_COURSE);
    }
    
    @Override
    public void successStudentFromCourseDeleting() {
        PrintWriter printWriter = new PrintWriter(System.out, true);
        printWriter.println(SUCCESS_STUDENT_FROM_COURSE_DELETING);
    }
    
    @Override
    public void deleteStudentIdFromCourse() {
        PrintWriter printWriter = new PrintWriter(System.out, true);
        printWriter.println(DELETE_STUDENT_ID_FROM_COURSE);
    }
    
    @Override
    public void returnMainMenuOrExit() {
        PrintWriter printWriter = new PrintWriter(System.out, true);
        printWriter.println(RETURN_MAIN_MENU_OR_EXIT);
    }
    
    @Override
    public void executionHasBeenStopped() {
        PrintWriter printWriter = new PrintWriter(System.out, true);
        printWriter.println(EXECUTION_HAS_BEEN_STOPPED);
    }

    @Override
    public void showIncorrectInputWarning() {
        PrintWriter printWriter = new PrintWriter(System.out, true);
        printWriter.println(INCORRECT_INPUT);
    }
    
    @Override
    public void enterCourseId() {
        PrintWriter printWriter = new PrintWriter(System.out, true);
        printWriter.println(ENTER_COURSE_ID);
    }
    
    @Override
    public void showStudentCourse(List<StudentModel> studentsOfCourse) {
        PrintWriter printWriter = new PrintWriter(System.out, true);

        if (studentsOfCourse.isEmpty()) {
            printWriter.println(NO_STUDENTS_OF_COURSE);
        } else {
            AtomicInteger atomicInteger = new AtomicInteger();
            studentsOfCourse.stream().forEachOrdered((line) -> {
                if (atomicInteger.getAndIncrement() == FIST_LINE) {
                    printWriter.println(new String(new char[STUDENT_COURSES_LINE_LENGTH])
                                        .replace(NULL, HATCH));
                    printWriter.format(STUDENT_COURSES_FORMAT, STUDENT_ID, 
                                                               GROUP_ID, 
                                                               STUDENT_FIRST_NAME,
                                                               STUDENT_LAST_NAME, 
                                                               COURSE_ID, 
                                                               COURSE_NAME, 
                                                               COURSE_DESCRIPTION);
                    printWriter.println(new String(new char[STUDENT_COURSES_LINE_LENGTH])
                                                   .replace(NULL, HATCH));
                    printWriter.format(STUDENT_COURSES_FORMAT, line.getStudentId(), 
                                                               line.getGroupId(),
                                                               line.getFirstName(), 
                                                               line.getLastName(), 
                                                               line.getCourseId(), 
                                                               line.getCourseName(),
                                                               line.getCourseDescription());
                    printWriter.println(new String(new char[STUDENT_COURSES_LINE_LENGTH])
                                        .replace(NULL, HATCH));
                } else {
                    printWriter.format(STUDENT_COURSES_FORMAT, line.getStudentId(), 
                                                               line.getGroupId(),
                                                               line.getFirstName(), 
                                                               line.getLastName(), 
                                                               line.getCourseId(), 
                                                               line.getCourseName(),
                                                               line.getCourseDescription());
                    printWriter.println(new String(new char[STUDENT_COURSES_LINE_LENGTH])
                                        .replace(NULL, HATCH));
                }
            });
        }
    }
    
    @Override
    public void showCourses(List<CourseModel> coursesList) {
        PrintWriter printWriter = new PrintWriter(System.out, true);
        AtomicInteger atomicInteger = new AtomicInteger();
        coursesList.parallelStream()
                   .sorted(Comparator.comparing(CourseModel::getCourseId))
                   .forEachOrdered((line) -> {
                       if (atomicInteger.getAndIncrement() == FIST_LINE) {
                           printWriter.format(COURSES_LINE_FORMAT, new String(new char[COURSE_LINE_LENGTH])
                                                                   .replace(NULL, HATCH));
                           printWriter.format(COURSES_FORMAT, COURSE_ID, COURSE_NAME, COURSE_DESCRIPTION);
                           printWriter.format(COURSES_LINE_FORMAT, new String(new char[COURSE_LINE_LENGTH])
                                                                   .replace(NULL, HATCH));
                           printWriter.format(COURSES_FORMAT, line.getCourseId(), 
                                                              line.getCourseName(),
                                                              line.getCourseDescription());
                           printWriter.format(COURSES_LINE_FORMAT, new String(new char[COURSE_LINE_LENGTH])
                                                                   .replace(NULL, HATCH));
            } else {
                printWriter.format(COURSES_FORMAT, line.getCourseId(), 
                                                   line.getCourseName(),
                                                   line.getCourseDescription());
                printWriter.format(COURSES_LINE_FORMAT, new String(new char[COURSE_LINE_LENGTH])
                                                        .replace(NULL, HATCH));
            }
        });
    }
}
