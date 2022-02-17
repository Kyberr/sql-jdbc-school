package ua.com.foxminded.sql_jdbc_school.services;

public interface StudentService<T, S, E, F> {
    
    public T getStudentsWithGroupId() throws ServicesException.GetStudentsWithGroupIdFailure;
    public F deleteStudent(F studentId) throws ServicesException.DeleteStudentFailure;
    public T getAllStudents() throws ServicesException.GetAllStudentsFailure;
    public F addStudent(E lastName, E firstName) throws ServicesException.AddNewStudentFailure;
    public T createStudents() throws ServicesException.StudentCreationFail;
    public T assignGroup(S groups) throws ServicesException.AssignGgoupToStudentsFail;
}
