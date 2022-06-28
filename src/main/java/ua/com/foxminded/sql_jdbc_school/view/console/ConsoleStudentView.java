package ua.com.foxminded.sql_jdbc_school.view.console;

import java.io.PrintWriter;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import ua.com.foxminded.sql_jdbc_school.model.StudentModel;
import ua.com.foxminded.sql_jdbc_school.view.StudentView;

public class ConsoleStudentView implements StudentView<List<StudentModel>, Integer> {
    
    private static final String ADD_STUDENT_TO_COURSE_OR_RETURN = "Press the \"Enter\" key to add another "
            + "student or write \"exit\" and press the \"Enter\" key to return to the main menu.";
    private static final String STUDENT_HAS_NOT_BEEN_ADDED_TO_COURSE = "The student has not been "
            + "added to the course.";
    private static final String STUDENT_HAS_BEEN_ADDED_TO_COURSE = "The student has been added to the course.";
    private static final String DELETE_STUDENT_OR_RETURN_MAIN_MENU = "Press the \"Enter\" key to delete "
            + "another student or write \"exit\" and press the \"Enter\" key to return the main menu.";
    private static final String STUDENT_HAS_NOT_BEEN_DELETED_FORMAT = "There is no student with "
            + "ID %s in the database.\n";
    private static final String STUDENT_HAS_BEEN_DELETED_FORMAT = "The student with ID %s has been deleted.\n";
    private static final String CONFIRM_STUDENT_DELETING = "Write \"yes\" to delete the student "
            + " or write \"no\" and press the \"Enter\" key.";
    private static final String INCORRECT_INPUT = "The input must be the number "
            + "of the corresponding value.\n";
    private static final String STUDENT_ID_INPUT = "Enter a student ID from the list above "
            + "and press the \"Enter\" key:";
    private static final String STUDENT_LAST_NAME = "Last name";
    private static final String STUDENT_FIRST_NAME = "First name";
    private static final String STUDENTS_FORMAT = "| %-10s| %-11s| %-11s| %-8s|\n";
    private static final String STUDENT_ID = "Student ID";
    private static final String GROUP_ID = "Group ID";
    private static final String DATABASE_HAS_NO_STUDENTS = "The database has no students.";
    private static final int STUDENTS_LINE_LENGTH = 49;
    private static final int FIST_LINE = 0;
    private static final char NULL = '\0';
    private static final char HATCH = '-';
    private static final String STUDENT_HAS_BEEN_ADDED_TO_DATABASE = "The new student has been added "
            + "to the databse.";
    private static final String ADD_STUDENT_YES_OR_NO = "To confirm the student adding, "
            + "write \"yes\" or \"no\" and press the \"Enter\" key:";
    private static final String ADD_STUDENT_OR_RETURN_MENU = "Press the \"Enter\" key to add "
            + "another student or write \"exit\" and press the \"Enter\" key to return the main menu.";
    private static final String ENTER_FIRST_NAME = "Enter a first name of the student adding:";
    private static final String ENTER_LAST_NAME = "Enter the last name of the student adding:";
    
    @Override
    public void addStudentToCourseOrReturnMenu() {
        PrintWriter printWriter = new PrintWriter(System.out, true);
        printWriter.println(ADD_STUDENT_TO_COURSE_OR_RETURN);
    }
    
    @Override
    public void studentHasNotBeenAddedToCourse() {
        PrintWriter printWriter = new PrintWriter(System.out, true);
        printWriter.println(STUDENT_HAS_NOT_BEEN_ADDED_TO_COURSE);
    }
    
    @Override
    public void studentHasBeenAddedToCourse() {
        PrintWriter printWriter = new PrintWriter(System.out, true);
        printWriter.println(STUDENT_HAS_BEEN_ADDED_TO_COURSE);
    }
    
    @Override
    public void deleteStudentOrReturnMainMenu() {
        PrintWriter printWriter = new PrintWriter(System.out, true);
        printWriter.println(DELETE_STUDENT_OR_RETURN_MAIN_MENU);
    }
    
    @Override
    public void studentHasNotBeenDeleted(Integer studentID) {
        PrintWriter printWriter = new PrintWriter(System.out, true);
        printWriter.format(STUDENT_HAS_NOT_BEEN_DELETED_FORMAT, studentID);
    }
    
    @Override
    public void studentHasBeenDeleted(Integer studentId) {
        PrintWriter printWriter = new PrintWriter(System.out, true);
        printWriter.format(STUDENT_HAS_BEEN_DELETED_FORMAT, studentId);
    }
    
    @Override
    public void confirmStudentDeleting() {
        PrintWriter printWriter = new PrintWriter(System.out, true);
        printWriter.println(CONFIRM_STUDENT_DELETING);
    }
    
    @Override
    public void showIncorrectInputWarning() {
        PrintWriter printWriter = new PrintWriter(System.out, true);
        printWriter.println(INCORRECT_INPUT);
    }
    
    @Override
    public void enterStudentId() {
        PrintWriter printWriter = new PrintWriter(System.out, true);
        printWriter.println(STUDENT_ID_INPUT);
    }
    
    @Override
    public void showStudents(List<StudentModel> students) {
        PrintWriter printWriter = new PrintWriter(System.out, true);

        if (students.isEmpty()) {
            printWriter.println(DATABASE_HAS_NO_STUDENTS);
        } else {
            AtomicInteger atomicInteger = new AtomicInteger();
            students.parallelStream()
                    .sorted(Comparator.comparing(StudentModel::getStudentId))
                    .forEachOrdered((student) -> {
                        if (atomicInteger.getAndIncrement() == FIST_LINE) {
                            printWriter.println(new String(new char[STUDENTS_LINE_LENGTH])
                                                .replace(NULL, HATCH));
                            printWriter.format(STUDENTS_FORMAT, STUDENT_ID, 
                                                                STUDENT_FIRST_NAME, 
                                                                STUDENT_LAST_NAME,
                                                                GROUP_ID);
                            printWriter.println(new String(new char[STUDENTS_LINE_LENGTH])
                                                .replace(NULL, HATCH));
                            printWriter.format(STUDENTS_FORMAT, student.getStudentId(), 
                                                                student.getFirstName(),
                                                                student.getLastName(), 
                                                                student.getGroupId());
                            printWriter.println(new String(new char[STUDENTS_LINE_LENGTH])
                                                .replace(NULL, HATCH));
                        } else {
                            printWriter.format(STUDENTS_FORMAT, student.getStudentId(), 
                                                                student.getFirstName(),
                                                                student.getLastName(), 
                                                                student.getGroupId());
                            printWriter.println(new String(new char[STUDENTS_LINE_LENGTH])
                                                .replace(NULL, HATCH));
                        }
                    });
        }
    }
    
    @Override
    public void studentHasBeenAddedToDatabase() {
        PrintWriter printWriter = new PrintWriter(System.out, true);
        printWriter.println(STUDENT_HAS_BEEN_ADDED_TO_DATABASE);
    }
    
    @Override
    public void addStudentYesOrNo() {
        PrintWriter printWriter = new PrintWriter(System.out, true);
        printWriter.println(ADD_STUDENT_YES_OR_NO);
    }
    
    @Override
    public void addStudentToDatabaseOrReturnMenu() {
        PrintWriter printWriter = new PrintWriter(System.out, true);
        printWriter.println(ADD_STUDENT_OR_RETURN_MENU);
    }
    
    @Override
    public void enterFirstName() {
        PrintWriter printWriter = new PrintWriter(System.out, true);
        printWriter.println(ENTER_FIRST_NAME);
    }
    
    @Override
    public void enterLastName() {
        PrintWriter printWriter = new PrintWriter(System.out, true);
        printWriter.println(ENTER_LAST_NAME);
    }
}
