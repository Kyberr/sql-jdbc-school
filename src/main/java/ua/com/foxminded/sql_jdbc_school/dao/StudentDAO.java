package ua.com.foxminded.sql_jdbc_school.dao;

import java.util.List;

import ua.com.foxminded.sql_jdbc_school.dao.entities.CourseEntity;
import ua.com.foxminded.sql_jdbc_school.dao.entities.StudentEntity;

public interface StudentDAO extends GenericDAO<StudentEntity, Integer>{
    
	public List<StudentEntity> getStudentsHavingCouse() throws DAOException; 
	public int addStudentToCourse(StudentEntity student, CourseEntity course) throws DAOException;
    public List<StudentEntity> getStudentsHavingGroupId() throws DAOException;
    public StudentEntity getById(int studentId) throws DAOException;
    public int deleteById(int studentId) throws DAOException;
    public int update(List<StudentEntity> students) throws DAOException;
}
