package ua.com.foxminded.sql_jdbc_school.dao;

import java.util.List;

import ua.com.foxminded.sql_jdbc_school.services.dto.StudentCourseDTO;

public interface StudentCourseDAO {
    
    public int createStudentCourseTable() throws DAOException.CreatingStudentCourseTableFailure; 
    
    public int insertStudentCourse(List<StudentCourseDTO> list) throws DAOException.StudentCourseInsertionFailure;
}
