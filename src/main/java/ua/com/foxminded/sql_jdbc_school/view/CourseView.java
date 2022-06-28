package ua.com.foxminded.sql_jdbc_school.view;

public interface CourseView<E, S> {
    
    public void studentFromCourseDeletingFailed();
    
    public void deleteAnotherStudentFromCourse();
    
    public void successStudentFromCourseDeleting();
    
    public void deleteStudentFromCourseById();
    
    public void executionHasBeenStopped();
    
    public void returnMainMenuOrExit();
    
    public void showIncorrectInputWarning();
    
    public void showStudentsOfCourse(S studentCourseList);
    
    public void enterCourseId();
    
    public void showCourses(E coursesList);
}
