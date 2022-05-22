package ua.com.foxminded.sql_jdbc_school.dao;

import java.util.List;
import ua.com.foxminded.sql_jdbc_school.dao.entities.GroupEntity;

public interface GroupDAO extends GenericDAO<GroupEntity, Integer, String> {
    
    public List<GroupEntity> getGroupsWithLessOrEqualStudents(int students) throws DAOException;
}
