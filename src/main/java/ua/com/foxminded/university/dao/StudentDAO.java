package ua.com.foxminded.university.dao;

import java.util.List;
import ua.com.foxminded.university.dao.DAOException.StudentInsertionFail;
import ua.com.foxminded.university.dto.StudentData;

public interface StudentDAO {
    
    public int insertStudents(List<StudentData> studend) throws StudentInsertionFail;
}
