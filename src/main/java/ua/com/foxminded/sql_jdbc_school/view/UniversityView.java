package ua.com.foxminded.sql_jdbc_school.view;

import java.io.PrintWriter;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import ua.com.foxminded.sql_jdbc_school.services.dto.GroupDTO;

public class UniversityView implements View<List<GroupDTO>> {
    private static final String FORMAT_START = "%85s";
    private static final String FORMAT_MENU = "%-4s%4s";
    private static final String FORMAT_LIST = "%s";
    private static final String FORMAT_GROUP_STUDENTS_NUMBER = "|%9s |%11s |%16s |\n";
    private static final String FORMAT_LINE = "%38s\n";
    private static final String FORMAT_WARNING = "%50s";
    
    
    
    private static final String MES_START = "Enter the corresponding number or write "
            + "\"exit\" and press the \"Enter\" key.\n\n";
    private static final String FIND_LESS = "Find all groups with a less or equal student count.\n";
    private static final String FIND_LESS_NUMBER = "1.";
    private static final String FIND_ALL = "Find all students related to a course with a given name.\n";
    private static final String FIND_ALL_NUMBER = "2.";
    private static final String ADD_STUDENT = "Add a new student.\n";
    private static final String ADD_STUDENT_NUMBER = "3.";
    private static final String DEL_STUDENT = "Delete a student.\n";
    private static final String DEL_STUDENT_NUMBER = "4.";
    private static final String COURSE = "Add a student to a course.\n";
    private static final String COURSE_NUMBER = "5.";
    private static final String REMOVE = "Remove a student from a course.\n";
    private static final String REMOVE_NUMBER = "6.";
    private static final String LIST_1 = "Enter the number of students:\n";
    private static final String ERROR_INPUT = "The input must be the number of the corresponding value.\n";
    private static final String GROUP_ID = "Group ID";
    private static final String GROUP_NAME = "Group Name";
    private static final String GROUP_STUDENTS_NUMBER = "Students number";
    private static final int LINE_LENGTH = 43;
    
    private static final char NULL = '\0';
    private static final char HATCH = '-';
    
    @Override
    public void showStudentsNumberOfGroups(List<GroupDTO> groupsList) {
        PrintWriter printWriter = new PrintWriter(System.out, true);
        AtomicInteger atomicInteger = new AtomicInteger();
        groupsList.stream().forEach((line) -> {
            if (atomicInteger.getAndIncrement() == 0) {
                printWriter.format(FORMAT_LINE, new String(new char[LINE_LENGTH]).replace(NULL, HATCH));
                printWriter.format(FORMAT_GROUP_STUDENTS_NUMBER, 
                                   GROUP_ID, GROUP_NAME, GROUP_STUDENTS_NUMBER);
                printWriter.format(FORMAT_LINE, new String(new char[LINE_LENGTH]).replace(NULL, HATCH));
            } else {
                printWriter.format(FORMAT_GROUP_STUDENTS_NUMBER, line.getGroupId(), 
                                   line.getGroupName(), line.getStudentsNumber());
                printWriter.format(FORMAT_LINE, new String(new char[LINE_LENGTH]).replace(NULL, HATCH));
            }
        });
    }
    
    @Override
    public void showFirstItemMessage() {
        PrintWriter printWriter = new PrintWriter(System.out, true);
        printWriter.format(FORMAT_LIST, LIST_1);
    }
    
    @Override
    public void showWrongInputWarning() {
        PrintWriter printWriter = new PrintWriter(System.out, true);
        printWriter.format(FORMAT_WARNING, ERROR_INPUT);
        
    }
    
    @Override
    public void showMenuItems() {
        PrintWriter printWriter = new PrintWriter(System.out, true);
        printWriter.format(FORMAT_START, MES_START);
        printWriter.format(FORMAT_MENU, FIND_LESS_NUMBER, FIND_LESS);
        printWriter.format(FORMAT_MENU, FIND_ALL_NUMBER, FIND_ALL);
        printWriter.format(FORMAT_MENU, ADD_STUDENT_NUMBER, ADD_STUDENT);
        printWriter.format(FORMAT_MENU, DEL_STUDENT_NUMBER, DEL_STUDENT);
        printWriter.format(FORMAT_MENU, COURSE_NUMBER, COURSE);
        printWriter.format(FORMAT_MENU, REMOVE_NUMBER, REMOVE);
    }

}
