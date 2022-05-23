package ua.com.foxminded.sql_jdbc_school.service;

public interface Table<T> {
    
    public T createTables() throws ServiceException;
    public T createStudentCourseTable() throws ServiceException;
}
