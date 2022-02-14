package ua.com.foxminded.sql_jdbc_school.view;

public interface MenuView<T, E, S> {
    
    public void showStudentsOfCourse(S studentCourseList);
    public void showFinalProgramMessage();
    public void showFinalItemMessage();
    public void showCourses(E coursesList);
    public void showStudentsNumberOfGroups(T groupsList);
    public void showMessageOfItemOne();
    public void showMessageOfItemTwo();
    public void showMenuItems();
    public void showWrongInputWarning();
}
