package ua.com.foxminded.sql_jdbc_school.service;

public interface GenericService<T> {
    
    public T assignIdAndAddToDatabase(T students) throws ServiceException;
    
    public int deleteAll() throws ServiceException;
}
