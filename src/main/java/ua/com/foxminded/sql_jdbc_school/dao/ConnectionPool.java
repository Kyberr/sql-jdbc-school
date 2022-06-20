package ua.com.foxminded.sql_jdbc_school.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.com.foxminded.sql_jdbc_school.dao.ConnectionPool;

public class ConnectionPool {
	private static final Logger LOGGER = LogManager.getLogger();
	private static final String GET_CONNECTION_ERROR = "The getting connection operation failed.";
	private static final String CLOSE_CONNECTION_ERROR = "The close connection operation failed.";
	private static final int CON_TIMEOUT = 3;
	private static final int ONE = 1;
	private static final int MAX_POOL_SIZE = 10;
	
	private List<Connection> availablePool = new ArrayList<>();
	private List<Connection> inUsePool = new ArrayList<>();
	private ConnectionDAOFactory connectionFactory;
	
	public ConnectionPool(ConnectionDAOFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}
	
	public synchronized Connection getConnection() throws SQLException, 
														  IOException {
		Connection con = null;
		
		if (availablePool.isEmpty()) {
			if(inUsePool.size() < MAX_POOL_SIZE) {
				con = createConnection(connectionFactory);
				inUsePool.add(con);
				return con;
			} else {
				try {
					while(availablePool.isEmpty()) {
						wait();
					}
				} catch (InterruptedException e) {
					LOGGER.error(GET_CONNECTION_ERROR, e);
					Thread.currentThread().interrupt();
				}
			}
		} else {
			con = availablePool.remove(availablePool.size() - ONE);
			inUsePool.add(con);
			
			if (con == null) {
				con = createConnection(connectionFactory);
				inUsePool.add(con);
			} else if (!con.isValid(CON_TIMEOUT)) {
				con.close();
				con = createConnection(connectionFactory);
				inUsePool.add(con);
			}
		}
		return con;
	}
	
	public synchronized void releaseConnection(Connection con) {
		inUsePool.remove(inUsePool.size() - ONE);
		availablePool.add(con);
		notifyAll();
	}
	
	public void closeConnectionsOfPool() throws DAOException {
		for(Connection con : availablePool) {
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
	
	private Connection createConnection(ConnectionDAOFactory connectionFactory) throws IOException, 
																					   SQLException {
		return connectionFactory.createConnection();
	}
}
