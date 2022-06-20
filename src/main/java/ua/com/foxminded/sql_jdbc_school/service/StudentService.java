package ua.com.foxminded.sql_jdbc_school.service;

public interface StudentService<T, S, E, F, K> {
	
	public F deleteAllStudents() throws ServiceException;
	public T getStudentsOfCourseById(F courseId) throws ServiceException;
	public F addStudentToCourseById(F studentId, F courseId) throws ServiceException;
	public T getAllStudentsHavingCourse() throws ServiceException;
	public T assignStudentToCourse (T students, K courses) throws ServiceException;
    public T getStudentsHavingGroupId() throws ServiceException;
    public F deleteStudent(F studentId) throws ServiceException;
    public T getAllStudents() throws ServiceException;
    public F addStudent(E lastName, E firstName) throws ServiceException;
    public T createStudents() throws ServiceException;
    public T assignGroup(S groups) throws ServiceException;
}
