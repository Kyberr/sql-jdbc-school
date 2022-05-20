package ua.com.foxminded.sql_jdbc_school.dao;

import java.util.List;

import ua.com.foxminded.sql_jdbc_school.dao.entities.GroupEntity;
import ua.com.foxminded.sql_jdbc_school.service.dto.GroupEntity;

public interface GroupDAO extends GenericDAO<GroupEntity, Integer> {
    
    public List<GroupEntity> getAllGroups() throws DAOException;
    public List<GroupEntity> getGroupsWithLessOrEqualStudents(int students) throws DAOException;
}
