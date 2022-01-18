package ua.com.foxminded.school.dao;

import java.sql.SQLException;

public interface DDLStatementDAO {

    public int sendDDLStatementDAO(String sql) throws SQLException;
}
