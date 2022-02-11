package ua.com.foxminded.sql_jdbc_school.view;

public interface View<T> {
    
    public void showStudentsNumberOfGroups(T groupsList);
    public void showFirstItemMessage();
    public void showMenuItems();
    public void showWrongInputWarning();
}
