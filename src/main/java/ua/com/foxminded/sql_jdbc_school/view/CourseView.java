package ua.com.foxminded.sql_jdbc_school.view;

public interface CourseView<E, S> {
    
    public void failureStudentFromCourseDeleting();
    
    public void deleteAnotherStudentFromCourse();
    
    public void successStudentFromCourseDeleting();
    
    public void deleteStudentIdFromCourse();
    
    public void executionHasBeenStopped();
    
    public void returnMainMenuOrExit();
    
    public void showIncorrectInputWarning();
    
    public void showStudentCourse(S studentCourseList);
    
    public void enterCourseId();
    
    public void showCourses(E coursesList);
}
