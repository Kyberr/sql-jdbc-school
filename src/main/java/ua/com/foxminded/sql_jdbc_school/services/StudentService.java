package ua.com.foxminded.sql_jdbc_school.services;

public interface StudentService<T> {
    
    public T createStudents() throws ServicesException.StudentCreationFail;
    
    public T assignGroup() throws ServicesException.AssignGgoupToStudentsFail;
}
