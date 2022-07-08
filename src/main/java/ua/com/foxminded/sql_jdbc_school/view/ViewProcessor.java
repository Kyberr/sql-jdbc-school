package ua.com.foxminded.sql_jdbc_school.view;

import java.util.List;

import ua.com.foxminded.sql_jdbc_school.model.CourseModel;
import ua.com.foxminded.sql_jdbc_school.model.GroupModel;
import ua.com.foxminded.sql_jdbc_school.model.StudentModel;

public interface ViewProcessor {
    
public void addStudentToCourseOrReturnMenu();
    
    public void studentHasNotBeenAddedToCourse();
    
    public void studentHasBeenAddedToCourse();
    
    public void deleteStudentOrReturnMainMenu();
    
    public void studentHasNotBeenDeleted(Integer studentID);
    
    public void studentHasBeenDeleted(Integer studentId);
    
    public void confirmStudentDeleting();
    
    public void enterStudentId();
    
    public void showStudents(List<StudentModel> students);
    
    public void studentHasBeenAddedToDatabase();
    
    public void addStudentYesOrNo();
    
    public void addStudentToDatabaseOrReturnToMenu();
    
    public void enterFirstName();
    
    public void enterLastName();
    
    public void programHasBeenStopped();

    public void showStudentQuantityOfGroups(List<GroupModel> groupsList);

    public void enterStudentQuantity();

    public void showIncorrectInputWarning();

    public void studentFromCourseDeletingFailed();

    public void deleteAnotherStudentFromCourse();

    public void successStudentFromCourseDeleting();

    public void deleteStudentFromCourseById();

    public void executionHasBeenStopped();

    public void returnMainMenuOrExit();

    public void showStudentsOfCourse(List<StudentModel> studentCourseList);

    public void enterCourseId();

    public void showCourses(List<CourseModel> coursesList);

    public void showMenuItems();

}
