package ua.com.foxminded.sql_jdbc_school.services;

public interface GroupService<T, E> {
    
    public T createGroups() throws ServicesException.GroupCreationFail;
    
    public T findGroupsWithLessOrEqualStudents(E students) 
            throws ServicesException.FindGroupsWithLessOrEqualStudentsFailure;

}
