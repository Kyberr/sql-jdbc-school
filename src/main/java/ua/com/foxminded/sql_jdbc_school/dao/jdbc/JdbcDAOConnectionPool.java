package ua.com.foxminded.sql_jdbc_school.dao.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.com.foxminded.sql_jdbc_school.dao.DAOConnectionFactory;
import ua.com.foxminded.sql_jdbc_school.dao.DAOConnectionPool;
import ua.com.foxminded.sql_jdbc_school.dao.DAOException;
import ua.com.foxminded.sql_jdbc_school.dao.jdbc.JdbcDAOConnectionPool;

public class JdbcDAOConnectionPool implements DAOConnectionPool {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String CONNECTION_POOL_ERROR = "The connection pool is full, "
            + "there are no available connections.";
    private static final String GET_CONNECTION_ERROR = "The getting connection failed.";
    private static final String CLOSE_CONNECTION_POOL_ERROR = "The close connection operation failed.";
    private static final int CON_TIMEOUT = 1;
    private static final int ONE = 1;
    private static final int MAX_POOL_SIZE = 20;

    private List<Connection> availablePool = new ArrayList<>();
    private List<Connection> inUsePool = new ArrayList<>();
    private DAOConnectionFactory connectionFactory;

    public JdbcDAOConnectionPool(DAOConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override
    public synchronized Connection getConnection() throws DAOException {
        Connection connection = null;

        try {
            if (availablePool.isEmpty() && (inUsePool.size() < MAX_POOL_SIZE)) {
                connection = connectionFactory.createConnection();
                inUsePool.add(connection);
                return connection;
            } else if (!availablePool.isEmpty()) {
                connection = getConnectionFromPool();
            } else {
                LOGGER.error(CONNECTION_POOL_ERROR);
                throw new SQLException(CONNECTION_POOL_ERROR);
            }
        } catch (SQLException e) {
            LOGGER.error(GET_CONNECTION_ERROR, e);
            Thread.currentThread().interrupt();
            throw new DAOException(GET_CONNECTION_ERROR, e);
        }
        return connection;
    }

    @Override
    public synchronized void releaseConnection(Connection connection) {
        inUsePool.remove(inUsePool.size() - ONE);
        availablePool.add(connection);
    }

    @Override
    public void closeConnections() throws DAOException {
        for (Connection connection : availablePool) {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                LOGGER.error(CLOSE_CONNECTION_POOL_ERROR, e);
                throw new DAOException(CLOSE_CONNECTION_POOL_ERROR, e);
            }
        }
    }

    private Connection getConnectionFromPool() throws DAOException, SQLException {
        Connection connection = availablePool.remove(availablePool.size() - ONE);
        inUsePool.add(connection);

        if (connection == null) {
            connection = connectionFactory.createConnection();
            inUsePool.add(connection);
        } else if (!connection.isValid(CON_TIMEOUT)) {
            connection.close();
            connection = connectionFactory.createConnection();
            inUsePool.add(connection);
        }
        return connection;
    }
}
