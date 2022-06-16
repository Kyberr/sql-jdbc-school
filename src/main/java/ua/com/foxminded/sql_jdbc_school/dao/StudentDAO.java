package ua.com.foxminded.sql_jdbc_school.dao;

import java.util.List;
import ua.com.foxminded.sql_jdbc_school.dao.entities.StudentEntity;

public interface StudentDAO extends GenericDAO<StudentEntity, Integer>{
    
    public List<StudentEntity> readStudentsHavingGroupId() throws DAOException;
    public StudentEntity read(int studentId) throws DAOException;
    public int delete(int studentId) throws DAOException;
    public int update(List<StudentEntity> students) throws DAOException;
}
