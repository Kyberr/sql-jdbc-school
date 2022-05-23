package ua.com.foxminded.sql_jdbc_school.dao;

public interface DAOEntity {
	
	public Integer create(String sqlQuery) throws DAOException;
}
