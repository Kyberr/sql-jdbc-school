package ua.com.foxminded.sql_jdbc_school.service;

public interface Group<T, E> {
    
    public T createGroups() throws ServiceException;
    public T findGroupsWithLessOrEqualStudents(E studentQuantity) 
            throws ServiceException;
}
