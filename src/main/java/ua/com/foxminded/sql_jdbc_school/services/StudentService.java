package ua.com.foxminded.sql_jdbc_school.services;

public interface StudentService<T, S, E, F> {
    
    public F addStudent(E lastName, E firstName) throws ServicesException.AddNewStudentFailure;
    public T createStudents() throws ServicesException.StudentCreationFail;
    public T assignGroup(S groups) throws ServicesException.AssignGgoupToStudentsFail;
}
