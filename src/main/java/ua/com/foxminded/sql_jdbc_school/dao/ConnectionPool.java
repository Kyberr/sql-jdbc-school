package ua.com.foxminded.sql_jdbc_school.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ua.com.foxminded.sql_jdbc_school.dao.ConnectionPool;

public class ConnectionPool {
	private static final int CON_TIMEOUT = 3;
	private static final int ONE = 1;
	
	private int maxPoolSize;
	
	private List<Connection> availablePool = new ArrayList<>();
	private List<Connection> inUsePool = new ArrayList<>();
	private ConnectionDAOFactory connectionFactory;
	
	public ConnectionPool(ConnectionDAOFactory connectionFactory, int maxPoolSize) {
		this.connectionFactory = connectionFactory;
		this.maxPoolSize = maxPoolSize;
	}
	
	public synchronized Connection getConnection() throws DAOException, 
														  SQLException, 
														  InterruptedException {
		Connection con;
		
		if (availablePool.isEmpty()) {
			if(inUsePool.size() < maxPoolSize) {
				con = createConnection(connectionFactory);
				inUsePool.add(con);
				return con;
			} else {
				while(inUsePool.size() >= maxPoolSize) {
					wait();
				}
				
				con = availablePool.remove(availablePool.size() - ONE);
				
				if (con == null) {
					con = createConnection(connectionFactory);
					inUsePool.add(con);
				} else if (!con.isValid(CON_TIMEOUT)) {
					con.close();
					con = createConnection(connectionFactory);
					inUsePool.add(con);
				}
				
				return con;
			}
		} else {
			return availablePool.remove(availablePool.size() - ONE);
		}
	}
	
	public synchronized void releaseConnection(Connection con) {
		inUsePool.remove(availablePool.size() - ONE);
		availablePool.add(con);
		notify();
	}
	
	public void closeConnectionsOfPool() throws SQLException {
		for(Connection con : availablePool) {
			con.close();
		}
	}
	
	private Connection createConnection(ConnectionDAOFactory connectionFactory) throws DAOException {
		return connectionFactory.createConnection();
	}
}
