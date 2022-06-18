package ua.com.foxminded.sql_jdbc_school.view;

import java.io.PrintWriter;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import ua.com.foxminded.sql_jdbc_school.service.dto.CourseDto;
import ua.com.foxminded.sql_jdbc_school.service.dto.GroupDto;
import ua.com.foxminded.sql_jdbc_school.service.dto.StudentDto;

public class ConsoleServiceControllerView implements ServiceControllerView<List<GroupDto>, 
																		   List<CourseDto>, 
																		   List<StudentDto>, 
																		   List<StudentDto>, 
																		   Integer> {
    
    private static final String START_MESSAGE_FORMAT = "%85s";
    private static final String MENU_FORMAT = "%-4s%4s";
    private static final String GROUPS_FORMAT = "| %-8s| %-12s| %-18s|\n";
    private static final String GROUPS_LINE_FORMAT = "%41s\n";
    private static final String COURSES_FORMAT = "| %-9s| %-25s| %-20s|\n";
    private static final String COURSES_LINE_FORMAT = "%50s\n";
    private static final String STUDENT_COURSES_FORMAT = "| %-10s| %-9s| %-10s| %-10s| %-9s| %-23s| %-18s|\n";
    private static final String STUDENTS_FORMAT = "| %-10s| %-11s| %-11s| %-8s|\n";
    private static final String STUDENT_HAS_BEEN_DELETED_FORMAT = "The student with ID %s has been deleted.\n"; 
    private static final String STUDENT_HAS_NOT_BEEN_DELETED_FORMAT = "There is not a student with "
                                                                    + "ID %s in the database.\n"; 
    
    private static final String ADD_STUDENT_TO_COURSE_OR_RETURN = "Press the \"Enter\" key to add another student or "
            + "write \"exit\" and press the \"Enter\" key to return to the main menu.";
    private static final String STUDENT_HAS_NOT_BEEN_ADDED_TO_COURSE = "The student has not been "
    		+ "added to the course.";
    private static final String STUDENT_HAS_BEEN_ADDED_TO_COURSE = "The student has been added to the course.";
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
    private static final String ENTER_NUMBER_OF_STUDENTS = "Enter a number of students:\n";
    private static final String NO_GROUPS = "There are no groups with this number of students.\n";
    private static final String ENTER_COURSE_ID = "Enter a course ID from the list above:";
    private static final String NO_STUDENTS_OF_COURSE = "No students are studying the specified course.\n";
    private static final String INCORRECT_INPUT = "The input must be the number of the corresponding value.\n";
    private static final String GROUP_ID = "Group ID";
    private static final String GROUP_NAME = "Group's name";
    private static final String NUMBER_OF_STUDENTS_IN_GROUP = "Number of students";
    private static final String COURSE_ID = "Course ID";
    private static final String COURSE_NAME = "Course name";
    private static final String COURSE_DESCRIPTION = "Course description";
    private static final String STUDENT_ID = "Student ID";
    private static final String STUDENT_FIRST_NAME = "First name";
    private static final String STUDENT_LAST_NAME = "Last name";
    private static final String RETURN_MAIN_MENU_OR_EXIT = "Press the \"Enter\" key to return to the main menu or "
                                                    + "write \"exit\" and press the \"Enter\" key.";
    private static final String ADD_ANOTHER_STUDENT_FROM_COURSE = "Press the \"Enter\" key to delete another "
            + "student from a course or write \"exit\" and press the \"Enter\" key.";
    private static final String FAILURE_STUDENT_FROM_COURSE_DELETING = "There is no such student in the course.";
    private static final String SUCCESS_STUDENT_FROM_COURSE_DELETING = "The student has been deleted from the course.";
    private static final String EXECUTION_HAS_BEEN_STOPPED = "The program execution has been stopped.";
    private static final String ENTER_LAST_NAME = "Enter a last name of the student adding:";
    private static final String ENTER_FIRST_NAME = "Enter a first name of the student adding:";
    private static final String ADD_STUDENT_YES_OR_NO = "To confirm the student adding, write \"yes\" or \"no\" "
                                                      + "and press the \"Enter\" key:";
    private static final String ADD_STUDENT_OR_RETURN_MENU = "Press the \"Enter\" key to add another student or "
            + "write \"exit\" and press the \"Enter\" key to return the main menu.";
    private static final String STUDENT_HAS_BEEN_ADDED = "The new student has been added to the databse."; 
    private static final String DATABASE_HAS_NO_STUDENTS = "The database has no students."; 
    private static final String STUDENT_ID_INPUT = "Enter a student ID from the list above "
                                                 + "and press the \"Enter\" key:"; 
    private static final String CONFIRM_STUDENT_DELETING = "Write \"yes\" to delete the student "
            + " or write \"no\" and press the \"Enter\" key.";
    private static final String DELETE_STUDENT_ID_FROM_COURSE = "To delete a student from a course first enter the "
            + "student's id from the list above and press the \"Enter\" key:";
    private static final String DELETE_STUDENT_OR_RETURN_MAIN_MENU = "Press the \"Enter\" key to delete another "
            + "student or write \"exit\" and press the \"Enter\" key to return the main menu."; 
    private static final int GROUP_LINE_LENGTH = 45;
    private static final int COURSE_LINE_LENGTH = 61;
    private static final int STUDENT_COURSES_LINE_LENGTH = 104;
    private static final int STUDENTS_LINE_LENGTH = 49;
    private static final int FIST_LINE = 0;
    private static final char NULL = '\0';
    private static final char HATCH = '-';

    @Override
    public void deleteAnotherStudentFromCourse() {
        PrintWriter printWriter = new PrintWriter(System.out, true);
        printWriter.println(ADD_ANOTHER_STUDENT_FROM_COURSE);
    }
    
    @Override
    public void failureStudentFromCourseDeleting() {
        PrintWriter printWriter = new PrintWriter(System.out, true);
        printWriter.println(FAILURE_STUDENT_FROM_COURSE_DELETING);
    }
    
    @Override 
    public void successStudentFromCourseDeleting() {
        PrintWriter printWriter = new PrintWriter(System.out, true);
        printWriter.println(SUCCESS_STUDENT_FROM_COURSE_DELETING);
    }
    
    @Override
    public void confirmStudentDeleting() {
        PrintWriter printWriter = new PrintWriter(System.out, true);
        printWriter.println(CONFIRM_STUDENT_DELETING);
    }
    
    @Override
    public void deleteStudentIdFromCourse() {
        PrintWriter printWriter = new PrintWriter(System.out, true);
        printWriter.println(DELETE_STUDENT_ID_FROM_COURSE);
    }
    
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
    public void enterStudentId() {
        PrintWriter printWriter = new PrintWriter(System.out, true);
        printWriter.println(STUDENT_ID_INPUT);
    }
    
    @Override
    public void showStudents(List<StudentDto> students) {
        PrintWriter printWriter = new PrintWriter(System.out, true);
        
        if (students.isEmpty()) {
            printWriter.println(DATABASE_HAS_NO_STUDENTS);
        } else {
            AtomicInteger atomicInteger = new AtomicInteger();
            students.parallelStream()
            		.sorted(Comparator.comparing(StudentDto::getStudentId))
            		.forEachOrdered((student) -> {
                if (atomicInteger.getAndIncrement() == FIST_LINE) {
                    printWriter.println(new String(new char[STUDENTS_LINE_LENGTH]).replace(NULL, HATCH));
                    printWriter.format(STUDENTS_FORMAT, STUDENT_ID, STUDENT_FIRST_NAME, 
                    				   STUDENT_LAST_NAME, GROUP_ID);
                    printWriter.println(new String(new char[STUDENTS_LINE_LENGTH]).replace(NULL, HATCH));
                    printWriter.format(STUDENTS_FORMAT, student.getStudentId(), student.getFirstName(), 
                                       student.getLastName(), student.getGroupId());
                    printWriter.println(new String(new char[STUDENTS_LINE_LENGTH]).replace(NULL, HATCH));
                } else {
                    printWriter.format(STUDENTS_FORMAT, student.getStudentId(), student.getFirstName(), 
                            student.getLastName(), student.getGroupId());
                    printWriter.println(new String(new char[STUDENTS_LINE_LENGTH]).replace(NULL, HATCH));
                }
            });
        }
    }
    
    @Override
    public void studentHasBeenAddedToDatabase() {
        PrintWriter printWriter = new PrintWriter(System.out, true);
        printWriter.println(STUDENT_HAS_BEEN_ADDED);
    }
    
    @Override
    public void addStudentToDatabaseOrReturnMenu() {
        PrintWriter printWriter = new PrintWriter(System.out, true);
        printWriter.println(ADD_STUDENT_OR_RETURN_MENU);
    }
    
    @Override
    public void addStudentYesOrNo() {
        PrintWriter printWriter = new PrintWriter(System.out, true);
        printWriter.println(ADD_STUDENT_YES_OR_NO);
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
    
    @Override 
    public void showStudentCourse(List<StudentDto> studentCourseList) {
        PrintWriter printWriter = new PrintWriter(System.out, true);
        
        if (studentCourseList.isEmpty()) {
            printWriter.println(NO_STUDENTS_OF_COURSE);
        } else {
            AtomicInteger atomicInteger = new AtomicInteger();
            studentCourseList.stream().forEachOrdered((line) -> {
                if (atomicInteger.getAndIncrement() == FIST_LINE) {
                    printWriter.println(new String(new char[STUDENT_COURSES_LINE_LENGTH]).replace(NULL, HATCH));
                    printWriter.format(STUDENT_COURSES_FORMAT, STUDENT_ID, GROUP_ID, STUDENT_FIRST_NAME, 
                                       STUDENT_LAST_NAME, COURSE_ID, COURSE_NAME, COURSE_DESCRIPTION);
                    printWriter.println(new String(new char[STUDENT_COURSES_LINE_LENGTH]).replace(NULL, HATCH));
                    printWriter.format(STUDENT_COURSES_FORMAT, line.getStudentId(), line.getGroupId(),  
                                       line.getFirstName(), line.getLastName(), line.getCourseId(), 
                                       line.getCourseName(), line.getCourseDescription());
                    printWriter.println(new String(new char[STUDENT_COURSES_LINE_LENGTH]).replace(NULL, HATCH));
                } else {
                    printWriter.format(STUDENT_COURSES_FORMAT, line.getStudentId(), line.getGroupId(),  
                            line.getFirstName(), line.getLastName(), line.getCourseId(), 
                            line.getCourseName(), line.getCourseDescription());
                    printWriter.println(new String(new char[STUDENT_COURSES_LINE_LENGTH]).replace(NULL, HATCH));
                }
            });
        }
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
    public void showCourses(List<CourseDto> coursesList) {
        PrintWriter printWriter = new PrintWriter(System.out, true);
        AtomicInteger atomicInteger = new AtomicInteger();
        coursesList.parallelStream()
        		   .sorted(Comparator.comparing(CourseDto::getCourseId))
        		   .forEachOrdered((line) -> {
            if (atomicInteger.getAndIncrement() == FIST_LINE) {
                printWriter.format(COURSES_LINE_FORMAT, new String(new char[COURSE_LINE_LENGTH])
                										    .replace(NULL, HATCH));
                printWriter.format(COURSES_FORMAT, COURSE_ID, COURSE_NAME, COURSE_DESCRIPTION);
                printWriter.format(COURSES_LINE_FORMAT, new String(new char[COURSE_LINE_LENGTH])
                										    .replace(NULL, HATCH));
                printWriter.format(COURSES_FORMAT, line.getCourseId(), line.getCourseName(), 
                                   line.getCourseDescription());
                printWriter.format(COURSES_LINE_FORMAT, new String(new char[COURSE_LINE_LENGTH])
                										    .replace(NULL, HATCH));
            } else {
                printWriter.format(COURSES_FORMAT, line.getCourseId(), line.getCourseName(),
                                   line.getCourseDescription());
                printWriter.format(COURSES_LINE_FORMAT, new String(new char[COURSE_LINE_LENGTH])
                										    .replace(NULL, HATCH));
            }
        });
    }
    
    @Override 
    public void enterCourseId() {
        PrintWriter printWriter = new PrintWriter(System.out, true);
        printWriter.println(ENTER_COURSE_ID);
    }
    
    @Override
    public void showNumberOfStudentsInGroups(List<GroupDto> groupsList) {
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
                    printWriter.format(GROUPS_FORMAT, line.getGroupId(), line.getGroupName(), 
                                       line.getStudentQuantity());
                    printWriter.format(GROUPS_LINE_FORMAT, new String(new char[GROUP_LINE_LENGTH])
                                                               .replace(NULL, HATCH));
                } else {
                    printWriter.format(GROUPS_FORMAT, line.getGroupId(), line.getGroupName(),
                    				   line.getStudentQuantity());
                    printWriter.format(GROUPS_LINE_FORMAT, new String(new char[GROUP_LINE_LENGTH])
                                                               .replace(NULL, HATCH));
                }
            });
        }
    }
    
    @Override
    public void enterNumberOfStudents() {
        PrintWriter printWriter = new PrintWriter(System.out, true);
        printWriter.println(ENTER_NUMBER_OF_STUDENTS);
    }
    
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
