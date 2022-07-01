package ua.com.foxminded.sql_jdbc_school.service;

import java.util.List;

import ua.com.foxminded.sql_jdbc_school.model.GroupModel;

public interface GroupService extends GenericService<List<GroupModel>> {

    public List<GroupModel> findGroupsWithLessOrEqualStudents(int studentQuantity) throws ServiceException;
}
