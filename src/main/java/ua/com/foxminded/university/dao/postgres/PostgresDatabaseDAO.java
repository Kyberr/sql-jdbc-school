package ua.com.foxminded.university.dao.postgres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PostgresDatabaseDAO implements DatabaseDAO {
    private static final Logger LOGGER = LogManager.getLogger(PostgresDatabaseDAO.class);
    private static final String ERROR_CONNECT = "The connection is failure.";
    private static final String ERROR_CLOSING = "The connection closing is failure.";
    private static final String LOG_ERROR_CONNECT = "The connection is failure. The SQL state: {}\n{}.";
    private static final String LOG_ERROR_CLOSING = "The connection closing is failure.";
    private static final String DATABASE_COLUMN_NAME = "datname";
    private static final String MES_DATABASE_EXISTENCE = "The database with the name \"%s\" has already existed.\n";
    private static final String MES_NO_DATABASE = "The database with the name \"%s\" is absent.\n";
    private static final String MES_DATABASE_DELETION = "The database with the name \"%s\" has been deleted.\n";
    private static final String MES_DATABASE_CREATION = "The database with the name \"%s\" has been created."
            + "\nThe owner of the database is an account with the name \"%s\".\n";
    private static final String MES_ROLE_EXISTENCE = "The account with the name \"%s\" is not exist. Create an account.\n";
    private static final String SQL_SELECT_DATABASE = "select datname from pg_database where datname = ?";
    private static final String SQL_CREATE_DATABASE = "create database %s owner %s";
    private static final String SQL_SELECT_ROLE = "select rolname from pg_roles where rolname = ?";
    private static final String SQL_DROP_DATABASE = "drop database %s";
    private static final String ROLE_COLUMN_NAME = "rolname";
    
    private String superuserName;
    private String superuserPass;

    public PostgresDatabaseDAO(String superuserName, String superuserPass) {
        this.superuserName = superuserName;
        this.superuserPass = superuserPass;
    }
    
    public void createDatabase(String databaseName, String ownerDatabase) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = PostgresDAOFactory.createConnection(superuserName, superuserPass);
            preparedStatement = connection.prepareStatement(SQL_SELECT_DATABASE);
            preparedStatement.setString(1, databaseName);
            resultSet = preparedStatement.executeQuery();
            String databaseExistence = null;
            
            while(resultSet.next()) {
                databaseExistence = resultSet.getString(DATABASE_COLUMN_NAME);
            }
            
            preparedStatement = connection.prepareStatement(SQL_SELECT_ROLE);
            preparedStatement.setString(1, ownerDatabase);
            resultSet = preparedStatement.executeQuery();
            String roleExistence = null;
            
            while(resultSet.next()) {
                roleExistence = resultSet.getString(ROLE_COLUMN_NAME);
            }
            
            if (databaseExistence != null) {
                System.out.printf(MES_DATABASE_EXISTENCE, databaseName);
            } else if (roleExistence == null) {
                System.out.printf(MES_ROLE_EXISTENCE, ownerDatabase);
            } else {
                preparedStatement = connection.prepareStatement(String.format(SQL_CREATE_DATABASE, 
                                                                              databaseName, 
                                                                              ownerDatabase));
                preparedStatement.execute();
                System.out.printf(MES_DATABASE_CREATION, databaseName, ownerDatabase);
            }
        } catch (SQLException e) {
            LOGGER.error(LOG_ERROR_CONNECT, e.getSQLState(), e.getMessage());
            throw new SQLException(ERROR_CONNECT, e);
        } finally {
            try {
                if (resultSet != null && preparedStatement != null && connection != null) {
                    resultSet.close();
                    preparedStatement.close();
                    connection.close();
                }
            } catch (SQLException e) {
                LOGGER.error(LOG_ERROR_CLOSING, e.getSQLState(), e.getMessage());
                throw new SQLException (ERROR_CLOSING, e);
            }
        }
    }
    
    public void deleteDatabase(String databaseName) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        
        try {
            connection = PostgresDAOFactory.createConnection(superuserName, superuserPass);
            preparedStatement = connection.prepareStatement(String.format(SQL_SELECT_DATABASE, databaseName));
            preparedStatement.setString(1, databaseName);
            resultSet = preparedStatement.executeQuery();
            String databaseExistence = null;
            
            while (resultSet.next()) {
                databaseExistence = resultSet.getString(DATABASE_COLUMN_NAME);
            }
            
            if (databaseExistence == null) {
                System.out.printf(MES_NO_DATABASE, databaseName);
            } else {
                preparedStatement = connection.prepareStatement(String.format(SQL_DROP_DATABASE, databaseName));
                preparedStatement.execute();
                System.out.printf(MES_DATABASE_DELETION, databaseName);
            }
        } catch (SQLException e) {
            LOGGER.error(LOG_ERROR_CONNECT, e.getSQLState(), e.getMessage());
            throw new SQLException(ERROR_CONNECT, e);
        } finally {
            try {
                if (resultSet != null && preparedStatement != null && connection != null) {
                    resultSet.close();
                    preparedStatement.close();
                    connection.close();
                }
            } catch (SQLException e) {
                LOGGER.error(LOG_ERROR_CLOSING, e.getSQLState(), e.getMessage());
                throw new SQLException (ERROR_CLOSING, e);
            }
        }
    }
}
