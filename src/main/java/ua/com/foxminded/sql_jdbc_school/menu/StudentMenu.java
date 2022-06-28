package ua.com.foxminded.sql_jdbc_school.menu;

import java.util.List;
import java.util.Scanner;

import ua.com.foxminded.sql_jdbc_school.model.CourseModel;
import ua.com.foxminded.sql_jdbc_school.model.GroupModel;
import ua.com.foxminded.sql_jdbc_school.model.StudentModel;
import ua.com.foxminded.sql_jdbc_school.service.ServiceException;
import ua.com.foxminded.sql_jdbc_school.service.StudentService;
import ua.com.foxminded.sql_jdbc_school.view.StudentMenuView;

public class StudentMenu {
    
    private static final String EMPTY_STRING = "";
    private static final String WORD_NO = "no";
    private static final int NORMAL_STATUS_OF_ADDING = 1;
    private static final String WORD_YES = "yes";
    private static final String WORD_EXIT = "exit";
    
    StudentService<List<StudentModel>, List<GroupModel>, String, Integer, List<CourseModel>> studentService;
    StudentMenuView studentMenuView;
    
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
    
    private void enterStudentNameAndAddToDatabase(String lastName, String firstName, Scanner scanner)
            throws ServiceException {
        for (;;) {
            studentMenuView.addStudentYesOrNo();
            String input = scanner.nextLine();

            if (input.equals(WORD_YES)) {
                if (studentService.addStudent(lastName, firstName) == NORMAL_STATUS_OF_ADDING) {
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
