package ua.com.foxminded.sql_jdbc_school.dao;

import java.util.List;

public interface GenericDAO<T, E, F> {
	
	public E insert(List<T> entity) throws DAOException;
	public List<T> getAll() throws DAOException;
	public E deleteAll(F objectType) throws DAOException;
}
