package ua.com.foxminded.university.dao;

import ua.com.foxminded.university.dao.postgres.PostgresDAOFactory;

public abstract class DAOFactory {
    public static final int POSTGRES = 1;
    public static final int UNIVERSITY = 2;
    
    public static DAOFactory getDAOFactory(int whichFactory) {
        switch (whichFactory) {
        case UNIVERSITY:
            return new PostgresDAOFactory();
        default:
            return null;
        }
    }
    
    public abstract TableDAO getTableDAO();
}
