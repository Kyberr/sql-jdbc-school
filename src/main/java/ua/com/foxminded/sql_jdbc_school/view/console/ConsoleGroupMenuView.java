package ua.com.foxminded.sql_jdbc_school.view.console;

import java.io.PrintWriter;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import ua.com.foxminded.sql_jdbc_school.model.GroupModel;
import ua.com.foxminded.sql_jdbc_school.view.GroupMenuView;

public class ConsoleGroupMenuView implements GroupMenuView<List<GroupModel>> {
    
    private static final String INCORRECT_INPUT = "The input must be the number of the corresponding value.\n";
    private static final String EXECUTION_HAS_BEEN_STOPPED = "The program execution has been stopped.";
    private static final String RETURN_MAIN_MENU_OR_EXIT = "Press the \"Enter\" key to return to the main "
            + "menu or write \"exit\" and press the \"Enter\" key.";
    private static final String ENTER_NUMBER_OF_STUDENTS = "Enter a number of students:\n";
    private static final String GROUPS_FORMAT = "| %-8s| %-12s| %-18s|\n";
    private static final String GROUPS_LINE_FORMAT = "%41s\n";
    private static final String NO_GROUPS = "There are no groups with this number of students.\n";
    private static final String GROUP_NAME = "Group's name";
    private static final String NUMBER_OF_STUDENTS_IN_GROUP = "Number of students";
    private static final String GROUP_ID = "Group ID";
    private static final int GROUP_LINE_LENGTH = 45;
    private static final int FIST_LINE = 0;
    private static final char NULL = '\0';
    private static final char HATCH = '-';
    
    @Override
    public void showIncorrectInputWarning() {
        PrintWriter printWriter = new PrintWriter(System.out, true);
        printWriter.println(INCORRECT_INPUT);
    }
    
    @Override
    public void executionHasBeenStopped() {
        PrintWriter printWriter = new PrintWriter(System.out, true);
        printWriter.println(EXECUTION_HAS_BEEN_STOPPED);
    }
    
    @Override
    public void returnMainMenuOrExit() {
        PrintWriter printWriter = new PrintWriter(System.out, true);
        printWriter.println(RETURN_MAIN_MENU_OR_EXIT);
    }
    
    @Override
    public void enterNumberOfStudents() {
        PrintWriter printWriter = new PrintWriter(System.out, true);
        printWriter.println(ENTER_NUMBER_OF_STUDENTS);
    }
    
    @Override
    public void showNumberOfStudentsInGroups(List<GroupModel> groupsList) {
        PrintWriter printWriter = new PrintWriter(System.out, true);

        if (groupsList.isEmpty()) {
            printWriter.println(NO_GROUPS);
        } else {
            AtomicInteger atomicInteger = new AtomicInteger();
            groupsList.stream().forEachOrdered((line) -> {
                if (atomicInteger.getAndIncrement() == FIST_LINE) {
                    printWriter.format(GROUPS_LINE_FORMAT, new String(new char[GROUP_LINE_LENGTH])
                                                           .replace(NULL, HATCH));
                    printWriter.format(GROUPS_FORMAT, GROUP_ID, GROUP_NAME, NUMBER_OF_STUDENTS_IN_GROUP);
                    printWriter.format(GROUPS_LINE_FORMAT, new String(new char[GROUP_LINE_LENGTH])
                                                           .replace(NULL, HATCH));
                    printWriter.format(GROUPS_FORMAT, line.getGroupId(), 
                                                      line.getGroupName(),
                                                      line.getStudentQuantity());
                    printWriter.format(GROUPS_LINE_FORMAT, new String(new char[GROUP_LINE_LENGTH])
                                                           .replace(NULL, HATCH));
                } else {
                    printWriter.format(GROUPS_FORMAT, line.getGroupId(), 
                                                      line.getGroupName(),
                                                      line.getStudentQuantity());
                    printWriter.format(GROUPS_LINE_FORMAT, new String(new char[GROUP_LINE_LENGTH])
                                                           .replace(NULL, HATCH));
                }
            });
        }
    }
}
