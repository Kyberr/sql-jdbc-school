package ua.com.foxminded.sql_jdbc_school.dao;

import java.util.List;
import ua.com.foxminded.sql_jdbc_school.services.dto.StudentDTO;

public interface StudentDAO {
    
    public int insertStudents(List<StudentDTO> studend) throws DAOException.StudentInsertionFail;
}
