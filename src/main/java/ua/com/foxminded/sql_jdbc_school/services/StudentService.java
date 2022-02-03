package ua.com.foxminded.sql_jdbc_school.services;

public interface StudentService<T, S> {
    
    public T createStudents() throws ServicesException.StudentCreationFail;
    
    public T assignGroup(S groups) throws ServicesException.AssignGgoupToStudentsFail;
}
