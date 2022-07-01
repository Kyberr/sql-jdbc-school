package ua.com.foxminded.sql_jdbc_school.dao;

import java.util.List;

import ua.com.foxminded.sql_jdbc_school.entity.CourseEntity;
import ua.com.foxminded.sql_jdbc_school.entity.StudentEntity;

public interface StudentDAO extends GenericDAO<StudentEntity> {

    public List<StudentEntity> getStudensOfCourseById(int courseId) throws DAOException;

    public StudentEntity getStudentOfCourseById(int studentId, int courseId) throws DAOException;

    public List<StudentEntity> getAllStudentsHavingCouse() throws DAOException;

    public int addStudentToCourse(StudentEntity student, CourseEntity course) throws DAOException;

    public List<StudentEntity> getStudentsHavingGroupId() throws DAOException;

    public StudentEntity getStudentById(int studentId) throws DAOException;

    public int deleteStudentById(int studentId) throws DAOException;

    public int update(List<StudentEntity> students) throws DAOException;
}
