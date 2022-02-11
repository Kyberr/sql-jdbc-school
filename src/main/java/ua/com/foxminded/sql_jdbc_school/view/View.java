package ua.com.foxminded.sql_jdbc_school.view;

public interface View<T, E> {
    
    public void showCourses(E coursesList);
    public void showStudentsNumberOfGroups(T groupsList);
    public void showMessageOfItemOne();
    public void showMessageOfItemTwo();
    public void showMenuItems();
    public void showWrongInputWarning();
}
