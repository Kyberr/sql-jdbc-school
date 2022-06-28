package ua.com.foxminded.sql_jdbc_school.view.console;

import java.io.PrintWriter;

import ua.com.foxminded.sql_jdbc_school.view.MenuView;

public class ConsoleMenuView implements MenuView {
    
    private static final String INCORRECT_INPUT = "The input must be the number of the corresponding value.\n";
    private static final String START_MESSAGE_FORMAT = "%85s";
    private static final String MENU_FORMAT = "%-4s%4s";
    private static final String START_MESSAGE = "Enter the corresponding number or write "
            + "\"exit\" and press the \"Enter\" key.\n\n";
    private static final String FIND_GROUPS_WITH_NUMBER_OF_STUDENTS = "Find all groups with a less or equal "
            + "student count.\n";
    private static final String ONE = "1.";
    private static final String FIND_STUDENTS_OF_COURSE = "Find all students related to a course "
            + "with a given name.\n";
    private static final String TWO = "2.";
    private static final String ADD_STUDENT = "Add a new student to the database.\n";
    private static final String THREE = "3.";
    private static final String DELETE_STUDENT = "Delete a student from the database.\n";
    private static final String FOUR = "4.";
    private static final String ADD_STUDENT_TO_COURSE = "Add a student to a course.\n";
    private static final String FIVE = "5.";
    private static final String REMOVE_STUDENT_FROM_COURSE = "Remove a student from a course.\n";
    private static final String SIX = "6.";

    
    @Override
    public void showIncorrectInputWarning() {
        PrintWriter printWriter = new PrintWriter(System.out, true);
        printWriter.println(INCORRECT_INPUT);
    }
    
    @Override
    public void showMenuItems() {
        PrintWriter printWriter = new PrintWriter(System.out, true);
        printWriter.format(START_MESSAGE_FORMAT, START_MESSAGE);
        printWriter.format(MENU_FORMAT, ONE, FIND_GROUPS_WITH_NUMBER_OF_STUDENTS);
        printWriter.format(MENU_FORMAT, TWO, FIND_STUDENTS_OF_COURSE);
        printWriter.format(MENU_FORMAT, THREE, ADD_STUDENT);
        printWriter.format(MENU_FORMAT, FOUR, DELETE_STUDENT);
        printWriter.format(MENU_FORMAT, FIVE, ADD_STUDENT_TO_COURSE);
        printWriter.format(MENU_FORMAT, SIX, REMOVE_STUDENT_FROM_COURSE);
    }
}
