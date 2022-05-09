package ua.com.foxminded.sql_jdbc_school.service;

public interface GroupService<T, E> {
    
    public T createGroups() throws ServiceException;
    
    public T findGroupsWithLessOrEqualStudents(E students) 
            throws ServiceException;
}
