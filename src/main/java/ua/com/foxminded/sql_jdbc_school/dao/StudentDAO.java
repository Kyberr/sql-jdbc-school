package ua.com.foxminded.sql_jdbc_school.dao;

import java.util.List;
import ua.com.foxminded.sql_jdbc_school.services.dto.StudentDTO;

public interface StudentDAO {
    
    public int insertStudent(List<StudentDTO> students) throws DAOException.StudentInsertionFail;
    
    public List<StudentDTO> getAllStudents() throws DAOException.GetAllSutudentsFail;  
    
    public int updateStudent(List<StudentDTO> students) throws DAOException.StudentUptatingFail;
}
