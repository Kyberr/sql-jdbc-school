package ua.com.foxminded.university.dao;

import ua.com.foxminded.university.dao.postgres.DatabaseDAO;
import ua.com.foxminded.university.dao.postgres.PostgresDAOFactory;
import ua.com.foxminded.university.dao.postgres.PostgresDatabaseDAO;
import ua.com.foxminded.university.dao.postgres.PostgresRoleDAO;
import ua.com.foxminded.university.dao.university.UniversityDAOFactory;

public abstract class DAOFactory {
    public static final int POSTGRES = 1;
    public static final int UNIVERSITY = 2;
    
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
    
    public RoleDAO getRoleDAO(String superuserName, String superuserPass) {
        return new PostgresRoleDAO(superuserName, superuserPass);
    }
    
    public DatabaseDAO getDatabaseDAO(String superuserName, String superuserPass) {
        return new PostgresDatabaseDAO(superuserName, superuserPass);
    }

    public abstract TablesDAO getTablesDAO(String role, String password);
    
    
    
    
}
