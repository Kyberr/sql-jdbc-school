package ua.com.foxminded.sql_jdbc_school.service;

public interface CourseService<T, E> {

    public E deleteAllCourses() throws ServiceException;

    public E deleteStudentFromCourseById(E studentId, E courseId) throws ServiceException;

    public T createCourses() throws ServiceException;

    public T getAllCourses() throws ServiceException;
}
