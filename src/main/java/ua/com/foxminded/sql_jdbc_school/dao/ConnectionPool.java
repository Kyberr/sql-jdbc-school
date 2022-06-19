package ua.com.foxminded.sql_jdbc_school.dao;

import java.sql.Connection;

public interface ConnectionPool {
	
	Connection getConnection();
	boolean releaseConnection(Connection con);
	String getUrl();
	String getUser();
	String getPassword();
}
