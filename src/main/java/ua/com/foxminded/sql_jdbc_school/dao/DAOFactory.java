package ua.com.foxminded.sql_jdbc_school.dao;

import ua.com.foxminded.sql_jdbc_school.dao.university.UniversityDAOFactory;

public abstract class DAOFactory {
    public static final int UNIVERSITY = 1;
    
    public abstract StudentDAO getStudentDAO();
    public abstract GroupDAO getGroupDAO();
    public abstract CourseDAO getCourseDAO();
    public abstract StudentCourseDAO getStudentCourseDAO();
    public abstract DAO getDAO();
    
    public static DAOFactory getDAOFactory(int whichFactory) {
        switch (whichFactory) {
        case UNIVERSITY:
            return new UniversityDAOFactory();
        default:
            return null;
        }
    }
}
