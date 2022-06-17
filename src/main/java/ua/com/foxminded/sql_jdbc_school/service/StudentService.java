package ua.com.foxminded.sql_jdbc_school.service;

public interface StudentService<T, S, E, F> {
    
	public T assignStudentToCourse (T students, E courses) throws ServiceException;
    public T getStudentsHavingGroupId() throws ServiceException;
    public F deleteStudent(F studentId) throws ServiceException;
    public T getAllStudents() throws ServiceException;
    public F addStudent(E lastName, E firstName) throws ServiceException;
    public T createStudents() throws ServiceException;
    public T assignGroup(S groups) throws ServiceException;
}
