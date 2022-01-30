package ua.com.foxminded.sql_jdbc_school.dao;

import java.util.List;

public interface GroupDAO {
    
    public int insertGroup(List<String> groupNameList) throws DAOException.GroupInsertionFail;
}
