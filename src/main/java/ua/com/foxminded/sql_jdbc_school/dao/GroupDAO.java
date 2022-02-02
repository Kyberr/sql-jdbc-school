package ua.com.foxminded.sql_jdbc_school.dao;

import java.util.List;

import ua.com.foxminded.sql_jdbc_school.services.dto.GroupDTO;

public interface GroupDAO {
    
    public int insertGroup(List<String> groupNameList) throws DAOException.GroupInsertionFail;
    
    public List<GroupDTO> getAllGroups() throws DAOException.GetAllGroupsFail;
}
