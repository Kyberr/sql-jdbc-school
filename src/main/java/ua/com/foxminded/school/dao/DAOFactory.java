package ua.com.foxminded.school.dao;

public abstract class DAOFactory {
    public static final int POSTGRES = 1;

    public abstract CustomerDAO getCustomerDAO();

    public static DAOFactory getDAOFactory(int whichFactory) {
        switch (whichFactory) {
        case POSTGRES:
            return new PostgresDAOFactory();
        default:
            return null;
        }
    }
}
