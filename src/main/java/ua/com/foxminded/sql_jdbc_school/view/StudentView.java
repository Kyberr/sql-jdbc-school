package ua.com.foxminded.sql_jdbc_school.view;

import java.util.List;

import ua.com.foxminded.sql_jdbc_school.model.StudentModel;

public interface StudentView extends GenericView {
    
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
}
