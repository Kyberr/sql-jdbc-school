package ua.com.foxminded.sql_jdbc_school.dao;

import java.util.List;

public interface GenericDAO<T, E, K> {
	
	public E create(List<T> entity) throws DAOException;
	public List<T> readAll() throws DAOException;
	public E createEntity(K sqlQuery) throws DAOException;
}
