package ua.com.foxminded.university.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PostgresAccountDAO implements AccountDAO {
    private static final Logger LOGGER = LogManager.getLogger(PostgresAccountDAO.class);
    private static final String SQL_CREAT_ROLE = "create role %s with login password '%s'";
    private static final String SQL_SELECT_ROLE = "select rolname from pg_roles where rolname = ?";
    private static final String SQL_COLUMN_NAME = "rolname";
    private static final String SQL_DROP_ROLE = "drop role %s";
    private static final String SQL_REASSIGN_ROLE = "reassign owned by %s to postgres";
    private static final String ERROR_CONNECT = "The connection is failure.";
    private static final String ERROR_CLOSING = "The connection closing is failure.";
    private static final String LOG_ERROR_CONNECT = "The connection is failure. The SQL state: {}\n{}.";
    private static final String LOG_ERROR_CLOSING = "The connection closing is failure.";
    private static final String MES_ACCOUNT_IS_READY = "The account with the name \"%s\" has been created.";
    private static final String MES_ACCOUNT_EXISTS = "The account with the name \"%s\" has already existed.";
    private static final String MES_NO_ACCOUNT = "The account with the name \"%s\" doesn't exist.";
    private static final String MES_REASSIGN = "All the objects owned by the account \"%s\""
                                             + " have been reassigned to the account \"postgres\".";
    private static final String DELETE_ROLE_MES = "\nThe account \"%s\" has been deleted.";
    private String superuserName;
    private String superuserPass;
    
    public PostgresAccountDAO(String superuserName, String superuserPass) {
        this.superuserName = superuserName;
        this.superuserPass = superuserPass;
    }
    
    public void createAccount(String newAccountName, 
                              String newAccountPass) throws SQLException {
        
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Connection connection = null;
        
        try {
            connection = PostgresDAOFactory.createConnection(superuserName, superuserPass);
            preparedStatement = connection.prepareStatement(SQL_SELECT_ROLE);
            preparedStatement.setString(1, newAccountName);
            resultSet = preparedStatement.executeQuery();
            String role = null;
            
            while (resultSet.next()) {
                role = resultSet.getString(SQL_COLUMN_NAME);
            }
            
            if (role == null) {
                preparedStatement = connection.prepareStatement(String.format(SQL_CREAT_ROLE, 
                                                                              newAccountName, 
                                                                              newAccountPass)); 
                // ? Resource leak: 'preparedStatement' is not closed at this location
                int accountCreation = preparedStatement.executeUpdate();
                
                if (accountCreation == 0) {
                    System.out.printf(MES_ACCOUNT_IS_READY, newAccountName);
                }
            } else {
                System.out.printf(MES_ACCOUNT_EXISTS, newAccountName);
            }
        } catch (SQLException e) {
            LOGGER.error(LOG_ERROR_CONNECT,  e.getSQLState(), e.getMessage());
            throw new SQLException(ERROR_CONNECT, e);
        } finally {
            try {
                
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                
                if (connection != null) {
                    connection.close();
                }
                
            } catch (SQLException e) {
                LOGGER.error(LOG_ERROR_CLOSING,  e.getSQLState(), e.getMessage());
                throw new SQLException(ERROR_CLOSING, e); 
                //? Exceptions should not be thrown in finally blocks
            }
            
        }
    }
    
    public void deleteAccount(String superuserName, 
                              String superuserPass, 
                              String deleteAccount) throws SQLException {
        
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        
        try {
            connection = PostgresDAOFactory.createConnection(superuserName, superuserPass);
            preparedStatement = connection.prepareStatement(SQL_SELECT_ROLE);
            preparedStatement.setString(1, deleteAccount);
            resultSet = preparedStatement.executeQuery();
            String role = null;
            
            while (resultSet.next()) {
                role = resultSet.getString(SQL_COLUMN_NAME);
            }
            
            if (role == null) {
                System.out.printf(MES_NO_ACCOUNT, deleteAccount);
            } else {
                preparedStatement = connection.prepareStatement(String.format(SQL_REASSIGN_ROLE, 
                                                                              deleteAccount));
                boolean reassignRole = preparedStatement.execute();
                
                if (!reassignRole) {
                    System.out.printf(MES_REASSIGN, deleteAccount);
                    preparedStatement = connection.prepareStatement(String.format(SQL_DROP_ROLE, deleteAccount));
                    preparedStatement.executeUpdate();
                    System.out.printf(DELETE_ROLE_MES, deleteAccount);
                }
            }
        } catch (SQLException e) {
            LOGGER.error(LOG_ERROR_CONNECT,  e.getSQLState(), e.getMessage());
            throw new SQLException(ERROR_CONNECT, e);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                LOGGER.error(LOG_ERROR_CLOSING,  e.getSQLState(), e.getMessage());
                throw new SQLException (LOG_ERROR_CLOSING, e);
            }
        }
    }
}
