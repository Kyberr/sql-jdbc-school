package ua.com.foxminded.sql_jdbc_school.dao;

import java.util.List;
import ua.com.foxminded.sql_jdbc_school.services.dto.StudentDTO;

public interface StudentDAO {
    
    public List<StudentDTO> getStudentsWithGroupId() throws DAOException.GetStudentsWithGroupIdFailure;
    public StudentDTO getStudent(int studentId) throws DAOException.GetStudentFailure;
    public int deleteStudent(int studentId) throws DAOException.DeleteStudentFailure;
    public int insertStudent(List<StudentDTO> students) throws DAOException.StudentInsertionFail;
    public List<StudentDTO> getAllStudents() throws DAOException.GetAllSutudentsFail;  
    public int updateStudent(List<StudentDTO> students) throws DAOException.StudentUptatingFail;
}
