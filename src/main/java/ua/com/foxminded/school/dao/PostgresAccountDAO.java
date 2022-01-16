package ua.com.foxminded.school.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PostgresAccountDAO implements AccountDAO {
    private static final Logger LOGGER = LogManager.getLogger(PostgresAccountDAO.class);
    private static final String COLUMN_ROLNAME = "rolname";
    private static final String ROLE_CREATION = "create role %s with login password '%s'";
    private static final String ROLE_SELECTION = "select rolname from pg_roles where rolname = ?";
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
            preparedStatement = connection.prepareStatement(ROLE_SELECTION);
            preparedStatement.setString(1, accountName);
            resultSet = preparedStatement.executeQuery();
            
            if (resultSet.getString(COLUMN_ROLNAME) == null) {
                preparedStatement = connection.prepareStatement(String.format(ROLE_CREATION, 
                                                                              accountName, 
                                                                              accountPassword)); 
                // ? Resource leak: 'preparedStatement' is not closed at this location
                int accountCreation = preparedStatement.executeUpdate();
                
                if (accountCreation == 1) {
                    LOGGER.info("The {} account has been created", accountName);
                }
            } else {
                LOGGER.info("The account with {} name has already existed", accountName);
            }
        } catch (SQLException e) {
            LOGGER.error("The connection is failure. The SQL state: {}\n{}",  e.getSQLState(), e.getMessage());
            throw new SQLException("The connection is failure", e);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                
                if (connection != null) {
                    connection.close();
                }
                
            } catch (SQLException e) {
                LOGGER.info("The connection is failure", e);
                throw new SQLException("The closing connection is faluer", e); //? Exceptions should not be thrown in finally blocks
            }
            
        }
    }
    
    public void deleteAccountDAO(String accountName) throws SQLException {
        String roleDeletion = "drop role ?";
        String roleReassign = "reassign owned by ? to postgres";
        String roleSelection = "select rolname from pg_roles where rolname='?'";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        
        try {
            connection = PostgresDAOFactory.createConnection(user, password);
            preparedStatement = connection.prepareStatement(roleSelection);
            preparedStatement.setString(1, accountName);
            resultSet = preparedStatement.executeQuery();
            
            if (resultSet.getString(COLUMN_ROLNAME) == null) {
                LOGGER.info("The specified account doesn't exist.");
            } else {
                preparedStatement = connection.prepareStatement(roleReassign);
                preparedStatement.setString(1, accountName);
                int reassignedRole = preparedStatement.executeUpdate();
                
                if (reassignedRole == 1) {
                    LOGGER.info("All the objects owned by {} have been reassigned to the postgres role.", accountName);
                    preparedStatement = connection.prepareStatement(roleDeletion);
                    preparedStatement.setString(1, accountName);
                    preparedStatement.executeUpdate();
                    LOGGER.info("The {} role has been deleted.", accountName);
                }
            }
        } catch (SQLException e) {
            LOGGER.info("The connection is failure", e);
            throw new SQLException("The connection is failure", e);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                LOGGER.info("The closing connection is faluer", e);
                throw new SQLException ("The closing connection is faluer", e);
            }
        }
    }
}
