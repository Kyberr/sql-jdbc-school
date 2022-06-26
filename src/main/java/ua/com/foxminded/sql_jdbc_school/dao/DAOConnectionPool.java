package ua.com.foxminded.sql_jdbc_school.dao;

import java.sql.Connection;

public interface DAOConnectionPool {

    public void closeConnectionPool() throws DAOException;

    public void releaseConnection(Connection con);

    public Connection getConnection() throws DAOException;
}
