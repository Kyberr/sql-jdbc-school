package ua.com.foxminded.sql_jdbc_school.dao;

public interface DAOFactory {
    
    public StudentDAO getStudentDAO();
    public GroupDAO getGroupDAO();
    public CourseDAO getCourseDAO();
    public StudentCourseDAO getStudentCourseDAO();
    public DAO getDAO();
}
