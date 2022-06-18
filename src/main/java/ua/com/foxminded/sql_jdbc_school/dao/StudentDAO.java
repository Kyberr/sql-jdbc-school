package ua.com.foxminded.sql_jdbc_school.dao;

import java.util.List;

import ua.com.foxminded.sql_jdbc_school.dao.entities.CourseEntity;
import ua.com.foxminded.sql_jdbc_school.dao.entities.StudentEntity;

public interface StudentDAO extends GenericDAO<StudentEntity, Integer> {
    
	public List<StudentEntity> getStudensOfCourseById(Integer courseId) throws DAOException;
	public StudentEntity getStudentOfCourseById(Integer studentId, Integer courseId) throws DAOException;
	public List<StudentEntity> getAllStudentsHavingCouse() throws DAOException; 
	public int addStudentToCourse(StudentEntity student, CourseEntity course) throws DAOException;
    public List<StudentEntity> getStudentsHavingGroupId() throws DAOException;
    public StudentEntity getStudentById(int studentId) throws DAOException;
    public int deleteById(int studentId) throws DAOException;
    public int update(List<StudentEntity> students) throws DAOException;
}
