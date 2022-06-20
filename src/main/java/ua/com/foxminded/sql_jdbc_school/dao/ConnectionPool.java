package ua.com.foxminded.sql_jdbc_school.dao;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionPool {
	
	public Connection getConnection() throws DAOException,  
	  									     SQLException, 
	  									     InterruptedException;
	public void releaseConnection(Connection con);
	public void closeConnectionsOfPool() throws SQLException;
}
