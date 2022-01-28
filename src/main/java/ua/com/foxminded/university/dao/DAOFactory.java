package ua.com.foxminded.university.dao;

import ua.com.foxminded.university.dao.university.UniversityDAOFactory;

public abstract class DAOFactory {
    public static final int UNIVERSITY = 1;
    
    public static DAOFactory getDAOFactory(int whichFactory) {
        switch (whichFactory) {
        case UNIVERSITY:
            return new UniversityDAOFactory();
        default:
            return null;
        }
    }
    
    public abstract TableDAO getTableDAO();
    public abstract StudentDAO getStudentDAO();
}
