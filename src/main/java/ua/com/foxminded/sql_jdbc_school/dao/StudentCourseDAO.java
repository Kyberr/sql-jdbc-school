package ua.com.foxminded.sql_jdbc_school.dao;

import java.util.List;

import ua.com.foxminded.sql_jdbc_school.service.dto.StudentCourseDTO;

public interface StudentCourseDAO {
    
    public int deleteStudentFromCourse(int studentId, int courseId) throws DAOException;
    public List<StudentCourseDTO> getAllStudentCourse() throws DAOException;
    public List<StudentCourseDTO> getStudentCourse(int studentId, int courseId) throws DAOException;
    public List<StudentCourseDTO> getStudentsOfCourse(int courseID) throws DAOException;
    public int createStudentCourseTable() throws DAOException; 
    public int insertStudentCourse(List<StudentCourseDTO> list) throws DAOException;
}
