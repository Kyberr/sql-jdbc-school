package ua.com.foxminded.sql_jdbc_school.dao;

import java.sql.Connection;

public interface DAOConnectionFactory {

    public Connection createConnection() throws DAOException;
}
