package ua.com.foxminded.sql_jdbc_school.service;

public interface CourseService<T> {
    
    public T createCourses() throws ServiceException;
    public T getAllCourses() throws ServiceException;
}
