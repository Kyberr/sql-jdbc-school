package ua.com.foxminded.sql_jdbc_school.view;

public interface GroupMenuView<T> {
    
    public void showIncorrectInputWarning();
    
    public void executionHasBeenStopped();
    
    public void returnMainMenuOrExit();
    
    public void showNumberOfStudentsInGroups(T groupsList);
    
    public void enterNumberOfStudents();
}
