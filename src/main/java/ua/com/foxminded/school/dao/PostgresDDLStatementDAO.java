package ua.com.foxminded.school.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PostgresDDLStatementDAO implements DDLStatementDAO {
    private static final Logger LOGGER = LogManager.getLogger(PostgresDDLStatementDAO.class);
    private static final String ERROR_CONNECT = "The connection is failure.";
    private static final String ERROR_CLOSING = "The connection closing is failure.";
    private static final String LOG_ERROR_CONNECT = "The connection is failure. The SQL state: {}\n{}.";
    private static final String LOG_ERROR_CLOSING = "The connection closing is failure.";
    private String user;
    private String password;

    public PostgresDDLStatementDAO(String user, String password) {
        this.user = user;
        this.password = password;
    }

    public int sendDDLStatementDAO(String sql) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = PostgresDAOFactory.createConnection(user, password);
            preparedStatement = connection.prepareStatement(sql);
            int execute = preparedStatement.executeUpdate();
            return execute;
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
}
