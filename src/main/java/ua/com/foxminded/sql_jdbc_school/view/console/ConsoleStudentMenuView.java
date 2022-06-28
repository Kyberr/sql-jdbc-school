package ua.com.foxminded.sql_jdbc_school.view.console;

import java.io.PrintWriter;

import ua.com.foxminded.sql_jdbc_school.view.StudentMenuView;

public class ConsoleStudentMenuView implements StudentMenuView {
    
    private static final String STUDENT_HAS_BEEN_ADDED_TO_DATABASE = "The new student has been added "
            + "to the databse.";
    private static final String ADD_STUDENT_YES_OR_NO = "To confirm the student adding, "
            + "write \"yes\" or \"no\" and press the \"Enter\" key:";
    private static final String ADD_STUDENT_OR_RETURN_MENU = "Press the \"Enter\" key to add "
            + "another student or write \"exit\" and press the \"Enter\" key to return the main menu.";
    private static final String ENTER_FIRST_NAME = "Enter a first name of the student adding:";
    private static final String ENTER_LAST_NAME = "Enter the last name of the student adding:";
    
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
