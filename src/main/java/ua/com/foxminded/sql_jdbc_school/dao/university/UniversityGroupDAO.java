package ua.com.foxminded.sql_jdbc_school.dao.university;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.List;

import ua.com.foxminded.sql_jdbc_school.dao.DAOException;
import ua.com.foxminded.sql_jdbc_school.dao.GroupDAO;

public class UniversityGroupDAO implements GroupDAO {
    private static final String SQL_INSERT = "insert into department.groups(group_name) values (?)"; 

    public int insertGroup(List<String> groupNameList) throws DAOException.GroupInsertionFail {
        
        try(Connection connection = UniversityDAOFactory.creatConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT)) {
            connection.setAutoCommit(false);
            Savepoint save1 = connection.setSavepoint();
            int status = 0;
            try {
                for (String groupName : groupNameList) {
                    preparedStatement.setString(1, groupName);
                    status = preparedStatement.executeUpdate();
                }

                connection.commit();
                return status;
            } catch (SQLException e) {
                connection.rollback(save1);
                throw new SQLException(e);
            }
        } catch (DAOException.DatabaseConnectionFail | 
                 SQLException e) {
            throw new DAOException.GroupInsertionFail(e);
        }
    }
}
