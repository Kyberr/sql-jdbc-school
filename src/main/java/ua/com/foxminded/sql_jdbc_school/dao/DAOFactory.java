package ua.com.foxminded.sql_jdbc_school.dao;

import ua.com.foxminded.sql_jdbc_school.dao.postgres.PostgresDAOFactory;

public abstract class DAOFactory {
    public static final int POSTGRES = 1;
    
    public abstract StudentDAO getStudentDAO();
    public abstract GroupDAO getGroupDAO();
    public abstract CourseDAO getCourseDAO();
    public abstract StudentCourseDAO getStudentCourseDAO();
    public abstract Entity getEntity();
    
    public static DAOFactory getDAOFactory(int whichFactory) {
        switch (whichFactory) {
        case POSTGRES:
            return new PostgresDAOFactory();
        default:
            return null;
        }
    }
}
