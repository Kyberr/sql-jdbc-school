package ua.com.foxminded.sql_jdbc_school.services;

public interface CourseService<T> {
    
    public T createCourses() throws ServicesException.CoursesCreationServiceFail;
    public T getAllCourses() throws ServicesException.GetAllCoursesFailure;
}
