package ua.com.foxminded.sql_jdbc_school.service;

public interface ServicePropertyCache<T> {
    
    public T getProperty(T key) throws ServiceException;
}
