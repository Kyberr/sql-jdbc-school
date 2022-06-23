package ua.com.foxminded.sql_jdbc_school.dao;

import java.util.List;

import ua.com.foxminded.sql_jdbc_school.entity.GroupEntity;

public interface GroupDAO extends GenericDAO<GroupEntity, Integer, String> {

    public List<GroupEntity> getGroupsHavingLessOrEqualStudents(int students) throws DAOException;
}
