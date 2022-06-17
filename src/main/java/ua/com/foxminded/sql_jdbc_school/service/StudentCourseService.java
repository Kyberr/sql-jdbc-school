package ua.com.foxminded.sql_jdbc_school.service;

public interface StudentCourseService<T, E, S, F> {
    
    public F deleteStudentFromCourse(F studentId, F courseId) throws ServiceException; 
    public S getAllStudentCourse() throws ServiceException;
    public F addStudentToCourse(F studentId, F courseId) throws ServiceException;
    public S getStudentsOfCourse(F courseId) throws ServiceException;
    public S createStudentCourseRelation (T students, E courses) throws ServiceException; 
}
