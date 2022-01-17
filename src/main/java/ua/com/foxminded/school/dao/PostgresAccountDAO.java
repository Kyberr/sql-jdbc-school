package ua.com.foxminded.school.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PostgresAccountDAO implements AccountDAO {
    private static final Logger LOGGER = LogManager.getLogger(PostgresAccountDAO.class);
    private static final String CREAT_ROLE_SQL = "create role %s with login password '%s'";
    private static final String SELECT_ROLE_SQL = "select rolname from pg_roles where rolname = ?";
    private static final String COLUMN_NAME_SQL = "rolname";
    private static final String ERROR = "The connection is failure";
    private static final String LOG_ERROR = "The connection is failure. The SQL state: {}\n{}";
    private static final String ACCOUNT_IS_READY_MES = "The account with the name \"%s\" has been created.";
    private static final String ACCOUNT_EXISTS_MES = "The account with the name \"%s\" has already existed.";
    private static final String DROP_ROLE_SQL = "drop role %s";
    private static final String REASSIGN_ROLE_SQL = "reassign owned by %s to postgres";
    private static final String NO_ACCOUNT_MES = "The account with the name \"%s\" doesn't exist.";
    private static final String REASSIGN_MES = "\nAll the objects owned by the account \"%s\""
                                             + " have been reassigned to the account \"postgres\".";
    private static final String DELETE_ROLE_MES = "\nThe account \"%s\" has been deleted.";
    private String user;
    private String password;
    
    public PostgresAccountDAO(String user, String password) {
        this.user = user;
        this.password = password;
    }
    
    public void createAccountDAO(String accountName, String accountPassword) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Connection connection = null;
        
        try {
            connection = PostgresDAOFactory.createConnection(user, password);
            preparedStatement = connection.prepareStatement(SELECT_ROLE_SQL);
            preparedStatement.setString(1, accountName);
            resultSet = preparedStatement.executeQuery();
            String role = null;
            
            while (resultSet.next()) {
                role = resultSet.getString(COLUMN_NAME_SQL);
            }
            
            if (role == null) {
                preparedStatement = connection.prepareStatement(String.format(CREAT_ROLE_SQL, 
                                                                              accountName, 
                                                                              accountPassword)); 
                // ? Resource leak: 'preparedStatement' is not closed at this location
                int accountCreation = preparedStatement.executeUpdate();
                
                if (accountCreation == 0) {
                    System.out.printf(ACCOUNT_IS_READY_MES, accountName);
                }
            } else {
                System.out.printf(ACCOUNT_EXISTS_MES, accountName);
            }
        } catch (SQLException e) {
            LOGGER.error(LOG_ERROR,  e.getSQLState(), e.getMessage());
            throw new SQLException(ERROR, e);
        } finally {
            try {
                
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                
                if (connection != null) {
                    connection.close();
                }
                
            } catch (SQLException e) {
                LOGGER.error(LOG_ERROR,  e.getSQLState(), e.getMessage());
                throw new SQLException(ERROR, e); 
                //? Exceptions should not be thrown in finally blocks
            }
            
        }
    }
    
    public void deleteAccountDAO(String accountName) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        
        try {
            connection = PostgresDAOFactory.createConnection(user, password);
            preparedStatement = connection.prepareStatement(SELECT_ROLE_SQL);
            preparedStatement.setString(1, accountName);
            resultSet = preparedStatement.executeQuery();
            String role = null;
            
            while (resultSet.next()) {
                role = resultSet.getString(COLUMN_NAME_SQL);
            }
            
            if (role == null) {
                System.out.printf(NO_ACCOUNT_MES, accountName);
            } else {
                preparedStatement = connection.prepareStatement(String.format(REASSIGN_ROLE_SQL, 
                                                                              accountName));
                boolean reassignRole = preparedStatement.execute();
                
                if (!reassignRole) {
                    System.out.printf(REASSIGN_MES, accountName);
                    preparedStatement = connection.prepareStatement(String.format(DROP_ROLE_SQL, accountName));
                    preparedStatement.executeUpdate();
                    System.out.printf(DELETE_ROLE_MES, accountName);
                }
            }
        } catch (SQLException e) {
            LOGGER.error(LOG_ERROR,  e.getSQLState(), e.getMessage());
            throw new SQLException(ERROR, e);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                LOGGER.error(LOG_ERROR,  e.getSQLState(), e.getMessage());
                throw new SQLException (ERROR, e);
            }
        }
    }
}
