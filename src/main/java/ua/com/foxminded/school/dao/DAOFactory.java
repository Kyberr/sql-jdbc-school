package ua.com.foxminded.school.dao;

public abstract class DAOFactory {
    public static final int POSTGRES = 1;

    public abstract AccountDAO getAccountDAO(String user, String password);
   /*
    public abstract StudentDAO getStudentDAO();
    public abstract GroupDAO getGroupDAO();
    public abstract CourseDAO getCourseDAO();
   */

    public static DAOFactory getDAOFactory(int whichFactory) {
        switch (whichFactory) {
        case POSTGRES:
            return new PostgresDAOFactory();
        default:
            return null;
        }
    }
}
