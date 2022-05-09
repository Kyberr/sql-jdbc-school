package ua.com.foxminded.sql_jdbc_school.dao;

import java.util.List;

import ua.com.foxminded.sql_jdbc_school.service.dto.GroupDTO;

public interface GroupDAO {
    
    public int insertGroup(List<String> groupNameList) throws DAOException;
    public List<GroupDTO> getAllGroups() throws DAOException;
    public List<GroupDTO> getGroupsWithLessOrEqualStudents(int students) throws DAOException;
}
