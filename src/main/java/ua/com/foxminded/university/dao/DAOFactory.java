package ua.com.foxminded.university.dao;

public abstract class DAOFactory {
    public static final int POSTGRES = 1;
    public static final int UNIVERSITY = 2;

    public abstract AccountDAO getAccountDAO();
    public abstract DatabaseDAO getDatabaseDAO();
    
   /*
    public abstract StudentDAO getStudentDAO();
    public abstract GroupDAO getGroupDAO();
    public abstract CourseDAO getCourseDAO();
   */

    public static DAOFactory getDAOFactory(int whichFactory) {
        switch (whichFactory) {
        case POSTGRES:
            return new PostgresDAOFactory();
        case UNIVERSITY:
            return new UniversityDAOFactory();
        default:
            return null;
        }
    }
}
