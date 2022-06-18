package ua.com.foxminded.sql_jdbc_school.service;

public interface StudentCourseService<T, E, S, F> {
    
    public S getStudentsOfCourse(F courseId) throws ServiceException;
}
