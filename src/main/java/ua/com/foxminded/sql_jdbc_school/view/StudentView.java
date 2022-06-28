package ua.com.foxminded.sql_jdbc_school.view;

public interface StudentView<F, M> {
    
    public void addStudentToCourseOrReturnMenu();
    
    public void studentHasNotBeenAddedToCourse();
    
    public void studentHasBeenAddedToCourse();
    
    public void deleteStudentOrReturnMainMenu();
    
    public void studentHasNotBeenDeleted(M studentID);
    
    public void studentHasBeenDeleted(M studentId);
    
    public void confirmStudentDeleting();
    
    public void showIncorrectInputWarning();
    
    public void enterStudentId();
    
    public void showStudents(F students);
    
    public void studentHasBeenAddedToDatabase();
    
    public void addStudentYesOrNo();
    
    public void addStudentToDatabaseOrReturnMenu();
    
    public void enterFirstName();
    
    public void enterLastName();
}
