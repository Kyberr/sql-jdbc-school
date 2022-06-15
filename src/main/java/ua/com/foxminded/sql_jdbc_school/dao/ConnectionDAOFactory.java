package ua.com.foxminded.sql_jdbc_school.dao;

import java.sql.Connection;

public interface ConnectionDAOFactory {
	
	public Connection createConnection() throws DAOException;
}
