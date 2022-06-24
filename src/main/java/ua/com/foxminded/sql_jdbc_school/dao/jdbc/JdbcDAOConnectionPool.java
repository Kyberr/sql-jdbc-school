package ua.com.foxminded.sql_jdbc_school.dao.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.com.foxminded.sql_jdbc_school.dao.ConnectionDAOFactory;
import ua.com.foxminded.sql_jdbc_school.dao.DAOConnectionPool;
import ua.com.foxminded.sql_jdbc_school.dao.DAOException;
import ua.com.foxminded.sql_jdbc_school.dao.jdbc.JdbcDAOConnectionPool;

public class JdbcDAOConnectionPool implements DAOConnectionPool {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String GET_CONNECTION_ERROR = "The getting connection failed.";
    private static final String WAIT_FREE_CONNECTION_ERROR = "The thread waiting for the "
                                                           + "connection has been interrupted.";
    private static final String CLOSE_CONNECTION_ERROR = "The close connection operation failed.";
    private static final int CON_TIMEOUT = 1;
    private static final int ONE = 1;
    private static final int MAX_POOL_SIZE = 10;
    private boolean wakeFlag;
    private boolean suspendFlag;

    private List<Connection> availablePool = new ArrayList<>();
    private List<Connection> inUsePool = new ArrayList<>();
    private ConnectionDAOFactory connectionFactory;

    public JdbcDAOConnectionPool(ConnectionDAOFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override
    public synchronized Connection getConnection() throws DAOException {
        suspendFlag = false;
        wakeFlag = false;
        Connection con = null;

        try {
            if (availablePool.isEmpty() && (inUsePool.size() < MAX_POOL_SIZE)) {
                con = connectionFactory.createConnection();
                inUsePool.add(con);
                return con;
            } else if (!availablePool.isEmpty()) {
                con = getConnectionFromPool();
            } else {
                waitFreeConnection();
                con = getConnection();
            }
        } catch (SQLException | InterruptedException e) {
            LOGGER.error(GET_CONNECTION_ERROR, e);
            Thread.currentThread().interrupt();
            throw new DAOException(GET_CONNECTION_ERROR, e);
        }

        return con;
    }

    @Override
    public synchronized void releaseConnection(Connection con) {
        inUsePool.remove(inUsePool.size() - ONE);
        availablePool.add(con);

        if (!availablePool.isEmpty() && suspendFlag) {
            while (!wakeFlag) {
                notifyAll();
            }
        }
    }

    @Override
    public void closeConnections() throws DAOException {
        for (Connection con : availablePool) {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                LOGGER.error(CLOSE_CONNECTION_ERROR, e);
                throw new DAOException(CLOSE_CONNECTION_ERROR, e);
            }
        }
    }

    private void waitFreeConnection() throws InterruptedException {
        try {
            while (availablePool.isEmpty()) {
                suspendFlag = true;
                wait();
                wakeFlag = true;
            }
        } catch (InterruptedException e) {
            LOGGER.error(WAIT_FREE_CONNECTION_ERROR, e);
            Thread.currentThread().interrupt();
            throw new InterruptedException();
        }
    }

    private Connection getConnectionFromPool() throws DAOException, SQLException {
        Connection con = availablePool.remove(availablePool.size() - ONE);
        inUsePool.add(con);

        if (con == null) {
            con = connectionFactory.createConnection();
            inUsePool.add(con);
        } else if (!con.isValid(CON_TIMEOUT)) {
            con.close();
            con = connectionFactory.createConnection();
            inUsePool.add(con);
        }

        return con;
    }
}
