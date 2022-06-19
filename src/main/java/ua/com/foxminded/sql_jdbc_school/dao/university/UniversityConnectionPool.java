package ua.com.foxminded.sql_jdbc_school.dao.university;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ua.com.foxminded.sql_jdbc_school.dao.ConnectionPool;

public class UniversityConnectionPool implements ConnectionPool {
	private static int INITIAL_POOL_SIZE = 10;
	private String url;
	private String user;
	private String password;
	private List<Connection> connectionPool;
	private List<Connection> usedConnections = new ArrayList<>();
	
	public UniversityConnectionPool(String url, String user, String password, List<Connection> connectionPool) {
		this.url = url;
		this.user = user;
		this.password = password;
		this.connectionPool = connectionPool;
	}

	public static UniversityConnectionPool create(String url, 
												  String user, 
												  String password) throws SQLException {
		List<Connection> pool = new ArrayList<>(INITIAL_POOL_SIZE);
		
		for (int i = 0; i < INITIAL_POOL_SIZE; i++) {
			pool.add(createConnection(url, user, password));
		}
		
		return new UniversityConnectionPool(url, user, password, pool);
	}
	
	@Override
	public Connection getConnection() {
		Connection connection = connectionPool.remove(connectionPool.size() - 1);
		usedConnections.add(connection);
		return connection;
	}
	
	@Override
	public boolean releaseConnection(Connection connection) {
		connectionPool.add(connection);
		return usedConnections.remove(connection);
	}
	
	private static Connection createConnection(String url, 
											   String user, 
											   String password) throws SQLException {
		return DriverManager.getConnection(url, user, password);
	}
	
	@Override
	public String getUrl() {
		return url;
	}
	
	@Override
	public String getUser() {
		return user;
	}

	@Override	
	public String getPassword() {
		return password;
	}
}
