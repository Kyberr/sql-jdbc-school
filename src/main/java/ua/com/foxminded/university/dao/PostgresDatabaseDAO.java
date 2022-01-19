package ua.com.foxminded.university.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PostgresDatabaseDAO implements DatabaseDAO {
    private static final Logger LOGGER = LogManager.getLogger(PostgresDatabaseDAO.class);
    private static final String ERROR_CONNECT = "The connection is failure.";
    private static final String ERROR_CLOSING = "The connection closing is failure.";
    private static final String LOG_ERROR_CONNECT = "The connection is failure. The SQL state: {}\n{}.";
    private static final String LOG_ERROR_CLOSING = "The connection closing is failure.";
    private String user;
    private String password;

    public PostgresDatabaseDAO(String user, String password) {
        this.user = user;
        this.password = password;
    }
    
    /*
    public void createDatabase(String databaseName) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = PostgresDAOFactory.createConnection(user, password);
            preparedStatement = connection.prepareStatement(databaseName);
            int status = preparedStatement.executeUpdate();
            
            if (status == 0) {
                
            }
        } catch (SQLException e) {
            LOGGER.error(LOG_ERROR_CONNECT, e.getSQLState(), e.getMessage());
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
                LOGGER.error(LOG_ERROR_CLOSING, e.getSQLState(), e.getMessage());
                throw new SQLException (ERROR_CLOSING, e);
            }
        }
    }
    
    */
}
