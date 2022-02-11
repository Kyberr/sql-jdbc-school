package ua.com.foxminded.sql_jdbc_school.view;

import java.io.PrintWriter;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import ua.com.foxminded.sql_jdbc_school.services.dto.CourseDTO;
import ua.com.foxminded.sql_jdbc_school.services.dto.GroupDTO;

public class UniversityView implements View<List<GroupDTO>, List<CourseDTO>> {
    private static final String FORMAT_START = "%85s";
    private static final String FORMAT_MENU = "%-4s%4s";
    private static final String FORMAT_ITEM_MESSAGE = "%s";
    private static final String FORMAT_GROUPS = "|%-8s |%-12s |%-18s |\n";
    private static final String FORMAT_GROUPS_LINE = "%41s\n";
    private static final String FORMAT_COURSES = "|%-9s |%-25s |%-20s |\n";
    private static final String FORMAT_COURSES_LINE = "%50s\n";
    private static final String FORMAT_WARNING = "%48s";
    
    private static final String START = "Enter the corresponding number and press the \"Enter\" key.\n\n";
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
    private static final String ITEM_1_MESSAGE = "Enter the number of students:\n";
    private static final String ITEM_1_MESSAGE_NO_GROUPS = "There are no groups with this number of students.\n";
    private static final String ITEM_2_MESSAGE = "Enter the course ID from the above list:\n";
    private static final String ERROR_INPUT = "The input must be the number of the corresponding value.\n";
    private static final String GROUP_ID = "Group ID";
    private static final String GROUP_NAME = "Group's name";
    private static final String GROUP_STUDENTS_NUMBER = "Number of students";
    private static final String COURSE_ID = "Course ID";
    private static final String COURSE_NAME = "Course name";
    private static final String COURSE_DESCRIPTION = "Course description";
    private static final int GROUP_LINE = 45;
    private static final int COURSE_LINE = 61;
    private static final char NULL = '\0';
    private static final char HATCH = '-';
    
    @Override
    public void showCourses(List<CourseDTO> coursesList) {
        PrintWriter printWriter = new PrintWriter(System.out, true);
        AtomicInteger atomicInteger = new AtomicInteger();
        coursesList.stream().parallel().forEachOrdered((line) -> {
            if (atomicInteger.getAndIncrement() == 0) {
                printWriter.format(FORMAT_COURSES_LINE, new String(new char[COURSE_LINE]).replace(NULL, HATCH));
                printWriter.format(FORMAT_COURSES, COURSE_ID, COURSE_NAME, COURSE_DESCRIPTION);
                printWriter.format(FORMAT_COURSES_LINE, new String(new char[COURSE_LINE]).replace(NULL, HATCH));
                printWriter.format(FORMAT_COURSES, line.getCourseId(), line.getCourseName(),
                                   line.getCourseDescription());
                printWriter.format(FORMAT_COURSES_LINE, new String(new char[COURSE_LINE]).replace(NULL, HATCH));
            } else {
                printWriter.format(FORMAT_COURSES, line.getCourseId(), line.getCourseName(),
                                   line.getCourseDescription());
                printWriter.format(FORMAT_COURSES_LINE, new String(new char[COURSE_LINE]).replace(NULL, HATCH));
            }
        });
    }
    
    @Override 
    public void showMessageOfItemTwo() {
        PrintWriter printWriter = new PrintWriter(System.out, true);
        printWriter.format(FORMAT_ITEM_MESSAGE, ITEM_2_MESSAGE);
    }
    
    @Override
    public void showStudentsNumberOfGroups(List<GroupDTO> groupsList) {
        PrintWriter printWriter = new PrintWriter(System.out, true);
        
        if (groupsList.isEmpty()) {
            printWriter.format(FORMAT_ITEM_MESSAGE, ITEM_1_MESSAGE_NO_GROUPS);
        } else {
            AtomicInteger atomicInteger = new AtomicInteger();
            groupsList.stream().parallel().forEachOrdered((line) -> {
                if (atomicInteger.getAndIncrement() == 0) {
                    printWriter.format(FORMAT_GROUPS_LINE, new String(new char[GROUP_LINE]).replace(NULL, HATCH));
                    printWriter.format(FORMAT_GROUPS, GROUP_ID, GROUP_NAME, GROUP_STUDENTS_NUMBER);
                    printWriter.format(FORMAT_GROUPS_LINE, new String(new char[GROUP_LINE]).replace(NULL, HATCH));
                    printWriter.format(FORMAT_GROUPS, line.getGroupId(), 
                                       line.getGroupName(), line.getStudentsNumber());
                    printWriter.format(FORMAT_GROUPS_LINE, new String(new char[GROUP_LINE]).replace(NULL, HATCH));
                } else {
                    printWriter.format(FORMAT_GROUPS, line.getGroupId(), 
                                       line.getGroupName(), line.getStudentsNumber());
                    printWriter.format(FORMAT_GROUPS_LINE, new String(new char[GROUP_LINE]).replace(NULL, HATCH));
                }
            });
        }
    }
    
    @Override
    public void showMessageOfItemOne() {
        PrintWriter printWriter = new PrintWriter(System.out, true);
        printWriter.format(FORMAT_ITEM_MESSAGE, ITEM_1_MESSAGE);
    }
    
    @Override
    public void showWrongInputWarning() {
        PrintWriter printWriter = new PrintWriter(System.out, true);
        printWriter.format(FORMAT_WARNING, ERROR_INPUT);
    }
    
    @Override
    public void showMenuItems() {
        PrintWriter printWriter = new PrintWriter(System.out, true);
        printWriter.format(FORMAT_START, START);
        printWriter.format(FORMAT_MENU, FIND_LESS_NUMBER, FIND_LESS);
        printWriter.format(FORMAT_MENU, FIND_ALL_NUMBER, FIND_ALL);
        printWriter.format(FORMAT_MENU, ADD_STUDENT_NUMBER, ADD_STUDENT);
        printWriter.format(FORMAT_MENU, DEL_STUDENT_NUMBER, DEL_STUDENT);
        printWriter.format(FORMAT_MENU, COURSE_NUMBER, COURSE);
        printWriter.format(FORMAT_MENU, REMOVE_NUMBER, REMOVE);
    }
}
