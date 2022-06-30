package ua.com.foxminded.sql_jdbc_school.service;

public interface GenericService<T, E> {
    
    public T create() throws ServiceException;
    public E deleteAll() throws ServiceException;

}
