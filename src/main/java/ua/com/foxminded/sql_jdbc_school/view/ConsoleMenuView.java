package ua.com.foxminded.sql_jdbc_school.view;

import java.io.PrintWriter;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import ua.com.foxminded.sql_jdbc_school.services.dto.CourseDTO;
import ua.com.foxminded.sql_jdbc_school.services.dto.GroupDTO;
import ua.com.foxminded.sql_jdbc_school.services.dto.StudentCourseDTO;

public class ConsoleMenuView implements MenuView<List<GroupDTO>, List<CourseDTO>, List<StudentCourseDTO>> {
    private static final String FORMAT_START = "%70s";
    private static final String FORMAT_MENU = "%-4s%4s";
    private static final String FORMAT_GROUPS = "| %-8s| %-12s| %-18s|\n";
    private static final String FORMAT_GROUPS_LINE = "%41s\n";
    private static final String FORMAT_COURSES = "| %-9s| %-25s| %-20s|\n";
    private static final String FORMAT_COURSES_LINE = "%50s\n";
    private static final String FORMAT_STUDENT_COURSES = "| %-10s| %-9s| %-10s| %-10s| %-9s| %-18s| %-18s|\n";
    private static final String FORMAT_STUDENT_COURSES_LINE = "%99s\n";
    private static final String FORMAT_WARNING = "%48s";
    
    private static final String START = "Enter the corresponding number and press the \"Enter\" key.\n\n";
    private static final String FIND_LESS = "Find all groups with a less or equal student count.\n";
    private static final String FIND_LESS_NUMBER = "1.";
    private static final String FIND_ALL = "Find all students related to a course with a given name.\n";
    private static final String FIND_ALL_NUMBER = "2.";
    private static final String ADD_STUDENT = "Add a new student to the database.\n";
    private static final String ADD_STUDENT_NUMBER = "3.";
    private static final String DEL_STUDENT = "Delete a student from the database.\n";
    private static final String DEL_STUDENT_NUMBER = "4.";
    private static final String COURSE = "Add a student to a course.\n";
    private static final String COURSE_NUMBER = "5.";
    private static final String REMOVE = "Remove a student from a course.\n";
    private static final String REMOVE_NUMBER = "6.";
    private static final String STUDENTS_NUMBER_INPUT_MESSAGE = "Enter the number of students:\n";
    private static final String NO_GROUPS_MESSAGE = "There are no groups with this number of students.\n";
    private static final String COURSE_ID_INPUT_MESSAGE = "Enter the course ID from the list above:\n";
    private static final String NO_STUDENTS_MESSAGE = "No students are studying the specified course.\n";
    private static final String ERROR_INPUT = "The input must be the number of the corresponding value.\n";
    private static final String GROUP_ID = "Group ID";
    private static final String GROUP_NAME = "Group's name";
    private static final String GROUP_STUDENTS_NUMBER = "Number of students";
    private static final String COURSE_ID = "Course ID";
    private static final String COURSE_NAME = "Course name";
    private static final String COURSE_DESCRIPTION = "Course description";
    private static final String STUDENT_ID = "Student ID";
    private static final String FIRST_NAME = "First name";
    private static final String LAST_NAME = "Last name";
    private static final String FINAL_ITEM_MESSAGE = "Press the \"Enter\" key to return to the main menu or "
                                                   + "write \"exit\" and press the \"Enter\" key.";
    private static final String FINAL_PROGRAM_MESSAGE = "The program execution has been stopped.";
    private static final String LAST_NAME_INPUT_MESSAGE = "Enter the last name of the student adding:";
    private static final String FIRST_NAME_INPUT_MESSAGE = "Enter the first name of the student adding:";
    private static final String CONFIRMING_MESSAGE = "To confirm the student adding, write \"yes\" or \"no\" "
                                                   + "and press the \"Enter\" key:";
    private static final String ADD_STUDENT_OR_RETURN_MESSAGE = "Press the \"Enter\" key to add another student or "
            + "write \"exit\" and press the \"Enter\" key to return the main menu.";
    private static final String ADD_STUDENT_CONFIRM_MESSAGE = "The new student has been added."; 
    private static final int GROUP_LINE = 45;
    private static final int COURSE_LINE = 61;
    private static final int STUDENT_COURSE_LINE = 99;
    private static final char NULL = '\0';
    private static final char HATCH = '-';
    
    @Override
    public void studentHasBeenAddedMessage() {
        PrintWriter printWriter = new PrintWriter(System.out, true);
        printWriter.println(ADD_STUDENT_CONFIRM_MESSAGE);
    }
    
    @Override
    public void addStudentOrReturnMainMenuMessage() {
        PrintWriter printWriter = new PrintWriter(System.out, true);
        printWriter.println(ADD_STUDENT_OR_RETURN_MESSAGE);
    }
    
    @Override
    public void confirmingMessage() {
        PrintWriter printWriter = new PrintWriter(System.out, true);
        printWriter.println(CONFIRMING_MESSAGE);
    }
    
    @Override 
    public void showFirstNameInputMessage() {
        PrintWriter printWriter = new PrintWriter(System.out, true);
        printWriter.println(FIRST_NAME_INPUT_MESSAGE);
    }
    
    @Override
    public void showLastNameInputMessage() {
        PrintWriter printWriter = new PrintWriter(System.out, true);
        printWriter.println(LAST_NAME_INPUT_MESSAGE);
    }
    
    @Override 
    public void showStudentsOfCourse(List<StudentCourseDTO> studentCourseList) {
        PrintWriter printWriter = new PrintWriter(System.out, true);
        
        if (studentCourseList.isEmpty()) {
            printWriter.println(NO_STUDENTS_MESSAGE);
        } else {
            AtomicInteger atomicInteger = new AtomicInteger();
            studentCourseList.stream().parallel().forEachOrdered((line) -> {
                if (atomicInteger.getAndIncrement() == 0) {
                    printWriter.format(FORMAT_STUDENT_COURSES_LINE, new String(new char[STUDENT_COURSE_LINE])
                                                                    .replace(NULL, HATCH));
                    printWriter.format(FORMAT_STUDENT_COURSES, STUDENT_ID, GROUP_ID, FIRST_NAME, LAST_NAME, 
                                       COURSE_ID, COURSE_NAME, COURSE_DESCRIPTION);
                    printWriter.format(FORMAT_STUDENT_COURSES_LINE, new String(new char[STUDENT_COURSE_LINE])
                            .replace(NULL, HATCH));
                    printWriter.format(FORMAT_STUDENT_COURSES, line.getStudentId(), line.getGroupId(),  
                                       line.getFirstName(), line.getLastName(), line.getCourseId(), 
                                       line.getCourseName(), line.getCourseDescription());
                    printWriter.format(FORMAT_STUDENT_COURSES_LINE, new String(new char[STUDENT_COURSE_LINE])
                            .replace(NULL, HATCH));
                } else {
                    printWriter.format(FORMAT_STUDENT_COURSES, line.getStudentId(), line.getGroupId(),  
                            line.getFirstName(), line.getLastName(), line.getCourseId(), 
                            line.getCourseName(), line.getCourseDescription());
                    printWriter.format(FORMAT_STUDENT_COURSES_LINE, new String(new char[STUDENT_COURSE_LINE])
                            .replace(NULL, HATCH));
                }
            });
        }
    }
    
    @Override
    public void showFinalProgramMessage() {
        PrintWriter printWriter = new PrintWriter(System.out, true);
        printWriter.println(FINAL_PROGRAM_MESSAGE);
    }
    
    @Override
    public void showFinalItemMessage() {
        PrintWriter printWriter = new PrintWriter(System.out, true);
        printWriter.println(FINAL_ITEM_MESSAGE);
    }
    
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
    public void showCourseIdInputMessage() {
        PrintWriter printWriter = new PrintWriter(System.out, true);
        printWriter.println(COURSE_ID_INPUT_MESSAGE);
    }
    
    @Override
    public void showStudentsNumberOfGroups(List<GroupDTO> groupsList) {
        PrintWriter printWriter = new PrintWriter(System.out, true);
        
        if (groupsList.isEmpty()) {
            printWriter.println(NO_GROUPS_MESSAGE);
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
    public void showStudentsNumberInputMessage() {
        PrintWriter printWriter = new PrintWriter(System.out, true);
        printWriter.println(STUDENTS_NUMBER_INPUT_MESSAGE);
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
