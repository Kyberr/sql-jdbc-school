package ua.com.foxminded.sql_jdbc_school.dao.university;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import ua.com.foxminded.sql_jdbc_school.dao.DAOException;
import ua.com.foxminded.sql_jdbc_school.dao.GroupDAO;
import ua.com.foxminded.sql_jdbc_school.services.dto.GroupDTO;

public class UniversityGroupDAO implements GroupDAO {
    private static final String SQL_INSERT = "insert into department.groups"
                                           + "(group_name) values (?)";
    private static final String SQL_SELECT_ALL = "select * from department.groups";
    private static final String SQL_GROUP_ID = "group_id";
    private static final String SQL_GROUP_NAME = "group_name";
    private static final String ERROR_GET_ALL_GROUP = "The getting data from the "
                                                    + "\"groups\" table is failed.";
    
    
    

    public List<GroupDTO> getAllGroups() throws DAOException.GetAllGroupsFail {
        try (Connection con = UniversityDAOFactory.creatConnection();
             Statement statement = con.createStatement();
             ResultSet resultSet = statement.executeQuery(SQL_SELECT_ALL)) {
            
            List<GroupDTO> result = new ArrayList<>();
            
            while (resultSet.next()) {
                result.add(new GroupDTO(resultSet.getString(SQL_GROUP_ID), 
                                        resultSet.getString(SQL_GROUP_NAME)));
            }
            return result;
        } catch (DAOException.DatabaseConnectionFail | SQLException e) {
            throw new DAOException.GetAllGroupsFail(ERROR_GET_ALL_GROUP, e);
        }
    }

    public int insertGroup(List<String> groupNameList) throws DAOException.GroupInsertionFail {

        try (Connection connection = UniversityDAOFactory.creatConnection();
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
        } catch (DAOException.DatabaseConnectionFail | SQLException e) {
            throw new DAOException.GroupInsertionFail(e);
        }
    }
}
