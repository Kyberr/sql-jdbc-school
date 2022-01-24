package ua.com.foxminded.university.dao.university;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import ua.com.foxminded.university.dao.DAOException;
import ua.com.foxminded.university.dao.DAOException.DatabaseConnectionFail;
import ua.com.foxminded.university.dao.DAOFactory;
import ua.com.foxminded.university.dao.TablesDAO;

public class UniversityDAOFactory extends DAOFactory {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/university";
    private static final String CONNECT_ERROR = "The database connection is failure."; 
    
    public static Connection creatConnection(String role, String password) throws DatabaseConnectionFail {
        try {
            return DriverManager.getConnection(DB_URL, role, password);
        } catch (SQLException e) {
            throw new DAOException.DatabaseConnectionFail(CONNECT_ERROR, e);
        }
    }
    
    @Override
    public TablesDAO getTablesDAO(String role, String password) {
        return new UniversityTablesDAO(role, password);
    }
}
