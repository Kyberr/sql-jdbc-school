package ua.com.foxminded.sql_jdbc_school.service;

public interface GroupService<T, E> {

    public E deleteAllGroups() throws ServiceException;

    public T createGroups() throws ServiceException;

    public T findGroupsWithLessOrEqualStudents(E studentQuantity) throws ServiceException;
}
