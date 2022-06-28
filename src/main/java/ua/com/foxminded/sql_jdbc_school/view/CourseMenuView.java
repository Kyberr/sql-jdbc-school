package ua.com.foxminded.sql_jdbc_school.view;

public interface CourseMenuView<E, S> {
    
    public void executionHasBeenStopped();
    
    public void returnMainMenuOrExit();
    
    public void showIncorrectInputWarning();
    
    public void showStudentCourse(S studentCourseList);
    
    public void enterCourseId();
    
    public void showCourses(E coursesList);
}
