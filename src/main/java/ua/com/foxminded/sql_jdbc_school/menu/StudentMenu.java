package ua.com.foxminded.sql_jdbc_school.menu;

import java.util.List;
import java.util.Scanner;

import ua.com.foxminded.sql_jdbc_school.model.CourseModel;
import ua.com.foxminded.sql_jdbc_school.model.GroupModel;
import ua.com.foxminded.sql_jdbc_school.model.StudentModel;
import ua.com.foxminded.sql_jdbc_school.service.CourseService;
import ua.com.foxminded.sql_jdbc_school.service.ServiceException;
import ua.com.foxminded.sql_jdbc_school.service.StudentService;
import ua.com.foxminded.sql_jdbc_school.view.CourseView;
import ua.com.foxminded.sql_jdbc_school.view.StudentView;

public class StudentMenu {
    
    private static final String EMPTY_STRING = "";
    private static final String WORD_NO = "no";
    private static final String WORD_YES = "yes";
    private static final int NORMAL_STATUS = 1;
    private static final String WORD_EXIT = "exit";
    
    CourseView<List<CourseModel>, List<StudentModel>> courseMenuView;
    CourseService<List<CourseModel>, Integer> courseService;
    StudentService<List<StudentModel>, List<GroupModel>, String, Integer, List<CourseModel>> studentService;
    StudentView<List<StudentModel>, Integer> studentMenuView;

    public StudentMenu(CourseView<List<CourseModel>, List<StudentModel>> courseMenuView,
                       CourseService<List<CourseModel>, Integer> courseService,
                       StudentService<List<StudentModel>, 
                                      List<GroupModel>, 
                                      String, 
                                      Integer, 
                                      List<CourseModel>> studentService,
                       StudentView<List<StudentModel>, Integer> studentMenuView) {
        this.courseMenuView = courseMenuView;
        this.courseService = courseService;
        this.studentService = studentService;
        this.studentMenuView = studentMenuView;
    }

    public void assignCourseToStudent(List<StudentModel> students, 
                                      List<CourseModel> groups) throws ServiceException {
        studentService.assignCourseToStudent(students, groups);
    }
    
    public List<StudentModel> assignGroupToStudent(List<GroupModel> groups) throws ServiceException {
        return studentService.assignGroupToStudent(groups);
    }
    
    public void createStudents() throws ServiceException {
        studentService.createStudents();
    }
    
    public void deleteAllStudents() throws ServiceException {
        studentService.deleteAllStudents();
    }
    
    public void deleteStudentFromDatabase(Scanner scanner) throws ServiceException {
        first: for (;;) {
            List<StudentModel> students = studentService.getAllStudents();
            studentMenuView.showStudents(students);
            studentMenuView.enterStudentId();
            int studentId = scanOnlyIntInput(scanner);
            studentMenuView.confirmStudentDeleting();
            String keyWord = scanOnlyYesOrNo(scanner);

            if (keyWord.equals(WORD_YES)) {
                int status = studentService.deleteStudent(studentId);

                if (status == NORMAL_STATUS) {
                    studentMenuView.studentHasBeenDeleted(studentId);
                } else {
                    studentMenuView.studentHasNotBeenDeleted(studentId);
                }
            }
            for (;;) {
                studentMenuView.deleteStudentOrReturnMainMenu();
                String input = scanner.nextLine();

                if (input.equals(WORD_EXIT)) {
                    break first;
                } else if (input.equals(EMPTY_STRING)) {
                    continue first;
                }
            }
        }
    }
    
    public void addStudentToDatabase(Scanner scanner) throws ServiceException {
        for (;;) {
            studentMenuView.enterLastName();
            String lastName = scanner.nextLine();
            studentMenuView.enterFirstName();
            String firstName = scanner.nextLine();
            enterStudentNameAndAddToDatabase(lastName, firstName, scanner);
            studentMenuView.addStudentToDatabaseOrReturnMenu();
            String keyWord = scanOnlyEmptyStringOrExitWord(scanner);

            if (keyWord.equals(WORD_EXIT)) {
                break;
            }
        }
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
            studentMenuView.showIncorrectInputWarning();
        }
        int input = scanner.nextInt();
        scanner.nextLine(); // it is used to clean the buffer from the empty string
        return input;
    }
    
    private void enterStudentNameAndAddToDatabase(String lastName, String firstName, Scanner scanner)
            throws ServiceException {
        for (;;) {
            studentMenuView.addStudentYesOrNo();
            String input = scanner.nextLine();

            if (input.equals(WORD_YES)) {
                if (studentService.addStudent(lastName, firstName) == NORMAL_STATUS) {
                    studentMenuView.studentHasBeenAddedToDatabase();
                    break;
                }
            } else if (input.equals(WORD_NO)) {
                break;
            }
        }
    }
    
    private String scanOnlyEmptyStringOrExitWord(Scanner scanner) {
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
}
