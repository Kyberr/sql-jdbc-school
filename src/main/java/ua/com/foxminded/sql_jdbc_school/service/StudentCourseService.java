package ua.com.foxminded.sql_jdbc_school.service;

public interface StudentCourseService<T, E, S, F> {
    
    public F addStudentToCourse(F studentId, F courseId) throws ServiceException;
    public S getStudentsOfCourse(F courseId) throws ServiceException;
}
