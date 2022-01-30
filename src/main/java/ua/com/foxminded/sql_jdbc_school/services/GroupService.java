package ua.com.foxminded.sql_jdbc_school.services;

public interface GroupService<T> {
    
    public T createGroups() throws ServicesException.GroupCreationFail;

}
