package ua.com.foxminded.sql_jdbc_school.service;

public interface GenericService<T> {
    
    public T create() throws ServiceException;
    public int deleteAll() throws ServiceException;

}
