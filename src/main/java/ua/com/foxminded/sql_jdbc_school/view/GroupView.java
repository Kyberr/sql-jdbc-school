package ua.com.foxminded.sql_jdbc_school.view;

public interface GroupView<T> {
    
    public void showIncorrectInputWarning();
    
    public void programHasBeenStopped();
    
    public void returnMainMenuOrExit();
    
    public void showStudentQuantityOfGroups(T groupsList);
    
    public void enterStudentQuantity();
}
