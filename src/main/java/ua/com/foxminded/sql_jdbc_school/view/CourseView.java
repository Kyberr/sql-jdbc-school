package ua.com.foxminded.sql_jdbc_school.view;

import java.util.List;

import ua.com.foxminded.sql_jdbc_school.model.CourseModel;
import ua.com.foxminded.sql_jdbc_school.model.StudentModel;

public interface CourseView extends GenericView {
    
    public void studentFromCourseDeletingFailed();
    
    public void deleteAnotherStudentFromCourse();
    
    public void successStudentFromCourseDeleting();
    
    public void deleteStudentFromCourseById();
    
    public void executionHasBeenStopped();
    
    public void returnMainMenuOrExit();
    
    public void showStudentsOfCourse(List<StudentModel> studentCourseList);
    
    public void enterCourseId();
    
    public void showCourses(List<CourseModel> coursesList);
}
