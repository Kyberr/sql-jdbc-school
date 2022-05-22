package ua.com.foxminded.sql_jdbc_school.dao.postgres;

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
import ua.com.foxminded.sql_jdbc_school.dao.entities.GroupEntity;

public class PostgresGroupDAO extends PostgresGenericDAO<GroupEntity> implements GroupDAO {
    private static final String SQL_INSERT = "insert into department.groups"
                                           + "(group_name) values (?)";
    private static final String SQL_SELECT_ALL = "select * from department.groups";
    private static final String SQL_SELECT_LESS_EQUAL_STUDENTS = "select department.groups.group_id, "
            + "department.groups.group_name, count(department.students.group_id) students_number "
            + "from department.groups join department.students on groups.group_id = students.group_id "
            + "group by groups.group_id "
            + "having count(department.students.group_id) <= %d "
            + "order by group_id";
    private static final String GROUP_ID = "group_id";
    private static final String GROUP_NAME = "group_name";
    private static final String GROUP_STUDENTS_NUMBER = "students_number";
    private static final String ERROR_INSERT_GROUP = "The group has not been iserted to the database.";
    private static final String ERROR_GET_ALL_GROUP = "The getting data from the "
                                                    + "\"groups\" table is failed.";
    private static final String ERROR_GET_LESS_OR_EQUAL_STUD = "Getting the groups with a less or "
                                                             + "equal number of students is failed.";
    
    @Override
    public List<GroupEntity> getGroupsWithLessOrEqualStudents (int students) throws DAOException {
        try (Connection con = PostgresDAOFactory.creatConnection();
             PreparedStatement statement = con.prepareStatement(String
                     .format(SQL_SELECT_LESS_EQUAL_STUDENTS, students));
             ResultSet resultSet = statement.executeQuery();) {
            
            List<GroupEntity> result = new ArrayList<>();
            
            while (resultSet.next()) {
                result.add(new GroupEntity((Integer) resultSet.getObject(GROUP_ID),
                                        resultSet.getString(GROUP_NAME),
                                        Integer.valueOf(resultSet.getString(GROUP_STUDENTS_NUMBER))));
            }
            return result;
        } catch (DAOException | ClassCastException | NumberFormatException | SQLException e) {
            throw new DAOException (ERROR_GET_LESS_OR_EQUAL_STUD, e);
        }
    }
    
    @Override
    public List<GroupEntity> readAll() throws DAOException {
        try (Connection con = PostgresDAOFactory.creatConnection();
             Statement statement = con.createStatement();
             ResultSet resultSet = statement.executeQuery(SQL_SELECT_ALL)) {
            
            List<GroupEntity> result = new ArrayList<>();
            
            while (resultSet.next()) {
                result.add(new GroupEntity(resultSet.getInt(GROUP_ID), 
                                        resultSet.getString(GROUP_NAME)));
            }
            return result;
        } catch (DAOException | SQLException e) {
            throw new DAOException(ERROR_GET_ALL_GROUP, e);
        }
    }
    
    @Override
    public Integer create(List<GroupEntity> groups) throws DAOException {

        try (Connection connection = PostgresDAOFactory.creatConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT)) {
            
            connection.setAutoCommit(false);
            Savepoint save1 = connection.setSavepoint();
            int status = 0;
           
            try {
                for (GroupEntity group : groups) {
                    preparedStatement.setString(1, group.getGroupName());
                    status = preparedStatement.executeUpdate();
                }
                
                connection.commit();
                return status;
            } catch (SQLException e) {
                connection.rollback(save1);
                throw new SQLException(e);
            }
        } catch (DAOException | SQLException e) {
            throw new DAOException(ERROR_INSERT_GROUP, e);
        }
    }
}
