package ua.com.foxminded.university.dao.university;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import ua.com.foxminded.university.dao.DAOException;
import ua.com.foxminded.university.dao.DAOException.DatabaseConnectionFail;
import ua.com.foxminded.university.dao.DAOException.TableCreationFail;
import ua.com.foxminded.university.dao.TablesDAO;

public class UniversityTablesDAO implements TablesDAO {
    private static final String MES_TABLES_CREATION = "The tabels has been created.";
    private static final String ERROR_TABALES_CREATION = "The creation of the tables in the database is failed!";
    private String role;
    private String password;

    public UniversityTablesDAO(String role, String password) {
        this.role = role;
        this.password = password;
    }
/*
    public void createTables(String sql) throws SQLException {
        
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = UniversityDAOFactory.creatConnection(role, "123");
            preparedStatement = connection.prepareStatement(sql);
            
            int status = preparedStatement.executeUpdate();
            
            if (status == 0) {
                System.out.println(MES_TABLES_CREATION);
            } 
        } finally {
            if (connection != null && preparedStatement != null) {
                preparedStatement.close();
                connection.close();
            }
        }
    }
    */
    
    public int createTables(String sql) throws TableCreationFail {
        try (Connection connection = UniversityDAOFactory.creatConnection(role, "123");
             PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
             int status = preparedStatement.executeUpdate();
             return status;
            
        } catch (DatabaseConnectionFail | SQLException e) {
            throw new DAOException.TableCreationFail(ERROR_TABALES_CREATION, e);
        }
    }
      
}
