package ua.com.foxminded.sql_jdbc_school.dao;

import java.util.List;
import ua.com.foxminded.sql_jdbc_school.services.dto.StudentCourseDTO;

public interface StudentCourseDAO {
    
    public int deleteStudentFromCourse(int studentId, int courseId) 
            throws DAOException.DeleteStudentFromCourseFailure;
    public List<StudentCourseDTO> getAllStudentCourse() throws DAOException.GetAllStudentCourseFailure;
    public List<StudentCourseDTO> getStudentCourse(int studentId, int courseId) 
            throws DAOException.GetCourseFailure;
    public List<StudentCourseDTO> getStudentsOfCourse(int courseID) 
            throws DAOException.GetStudentRelatedToCourseFailure;
    public int createStudentCourseTable() throws DAOException.CreatingStudentCourseTableFailure; 
    public int insertStudentCourse(List<StudentCourseDTO> list) throws DAOException.StudentCourseInsertionFailure;
}
