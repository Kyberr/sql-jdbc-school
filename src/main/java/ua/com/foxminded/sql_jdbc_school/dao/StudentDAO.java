package ua.com.foxminded.sql_jdbc_school.dao;

import java.util.List;
import ua.com.foxminded.sql_jdbc_school.dao.entities.StudentEntity;

public interface StudentDAO extends GenericDAO<StudentEntity, Integer, String>{
    
    public List<StudentEntity> getStudentsWithGroupId() throws DAOException;
    public StudentEntity getStudent(int studentId) throws DAOException;
    public int deleteStudent(int studentId) throws DAOException;
    public int updateStudent(List<StudentEntity> students) throws DAOException;
}
