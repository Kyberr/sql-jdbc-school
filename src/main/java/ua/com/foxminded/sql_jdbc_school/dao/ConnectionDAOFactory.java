package ua.com.foxminded.sql_jdbc_school.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionDAOFactory {
	
	public Connection createConnection() throws IOException, SQLException;
}
