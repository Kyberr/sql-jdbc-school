package ua.com.foxminded.sql_jdbc_school.dao;

import java.util.List;

public interface GenericDAO<T> {
    
    public int insert(List<T> entity) throws DAOException;

    public List<T> getAll() throws DAOException;

    public int deleteAll() throws DAOException;
}
