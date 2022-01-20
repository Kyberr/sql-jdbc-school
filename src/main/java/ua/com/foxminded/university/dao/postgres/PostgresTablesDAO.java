package ua.com.foxminded.university.dao.postgres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.com.foxminded.university.dao.TablesDAO;

public class PostgresTablesDAO implements TablesDAO {
    private static final Logger LOGGER = LogManager.getLogger(PostgresTablesDAO.class);
    private static final String MES_TABLES_CREATION = "The tabels has been created.";
    private static final String ERROR_CONNECT = "The connection is failure.";
    private static final String ERROR_CLOSING = "The connection closing is failure.";
    private static final String LOG_ERROR_CONNECT = "The connection is failure. The SQL state: {}\n{}.";
    private static final String LOG_ERROR_CLOSING = "The connection closing is failure.";
    private String role;
    private String password;
    
    public PostgresTablesDAO(String role, String password) {
        this.role = role;
        this.password = password;
    }
    
    public void createTables(String sql) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        
        try {
            connection = PostgresDAOFactory.createConnection(role, password);
            preparedStatement = connection.prepareStatement(sql);
            int status = preparedStatement.executeUpdate();
            
            if (status == 0) {
                System.out.println(MES_TABLES_CREATION);
            }
        } catch (SQLException e) {
            LOGGER.error(LOG_ERROR_CONNECT, e.getSQLState(), e.getMessage());
            throw new SQLException(ERROR_CONNECT);
        } finally {
            try {
                if (preparedStatement != null && connection != null) {
                    preparedStatement.close();
                    connection.close();
                } 
            } catch (SQLException e) {
                LOGGER.error(LOG_ERROR_CLOSING, e.getSQLState(), e.getMessage());
                throw new SQLException(ERROR_CLOSING);
            }
        }
    }
}
