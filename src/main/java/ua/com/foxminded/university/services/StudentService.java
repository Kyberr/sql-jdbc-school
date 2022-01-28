package ua.com.foxminded.university.services;

public interface StudentService<T> {
    
    public T insertStudents() throws ServicesException.StudentInsertionFail;
}
