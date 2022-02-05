package ua.com.foxminded.sql_jdbc_school.dao;

public interface StudentCourseDAO {
    
    public int createStudentCourseView() throws DAOException.CreatingStudentCourseViewFailure; 
}
