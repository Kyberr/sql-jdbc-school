package ua.com.foxminded.sql_jdbc_school.dao;

public interface DAOPropertiesCache<T> {
    
    public T getProperty(T arg) throws DAOException;
}
