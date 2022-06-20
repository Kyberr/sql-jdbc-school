package ua.com.foxminded.sql_jdbc_school.dao.university;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.com.foxminded.sql_jdbc_school.dao.ConnectionDAOFactory;
import ua.com.foxminded.sql_jdbc_school.dao.DAOException;
import ua.com.foxminded.sql_jdbc_school.dao.DAOPropertiesCache;
import ua.com.foxminded.sql_jdbc_school.dao.GroupDAO;
import ua.com.foxminded.sql_jdbc_school.dao.entities.GroupEntity;

public class UniversityGroupDAO extends UniversityGenericDAO<GroupEntity> implements GroupDAO {
	
	private static final Logger LOGGER = LogManager.getLogger();
	private static final String QUERIES_FILE_NAME = "group-queries.properties";
	private static final String SELECT_INCLUSIVE_LESS_STUDENTS = "selectInclusiveLessStudents";
	private static final String SELECT_ALL = "selectAll";
    private static final String INSERT = "insert";
    private static final String GROUP_ID = "group_id";
    private static final String GROUP_NAME = "group_name";
    private static final String ERROR_INSERT_GROUP = "The group has not been iserted to the database.";
    private static final String ERROR_GET_ALL_GROUP = "The getting data from the "
                                                    + "\"groups\" table is failed.";
    private static final String ERROR_GET_LESS_OR_EQUAL_STUD = "Getting the groups with a less or "
                                                             + "equal number of students is failed.";
    
    public UniversityGroupDAO(ConnectionDAOFactory universityConnectionDAOFactory) {
    	super(universityConnectionDAOFactory);
	}

	@Override
    public List<GroupEntity> readGroupsWithLessOrEqualStudents (int students) throws DAOException {
        try (Connection con = universityConnectionDAOFactory.createConnection();
             PreparedStatement statement = con.prepareStatement(String.format(DAOPropertiesCache
            		 .getInstance(QUERIES_FILE_NAME).getProperty(SELECT_INCLUSIVE_LESS_STUDENTS), students));
             ResultSet resultSet = statement.executeQuery();) {
            
            List<GroupEntity> result = new ArrayList<>();
            
            while (resultSet.next()) {
                result.add(new GroupEntity((Integer) resultSet.getObject(GROUP_ID),
                                        resultSet.getString(GROUP_NAME)));
            }
            return result;
        } catch (ClassCastException | NumberFormatException | SQLException | IOException e) {
        	LOGGER.error(ERROR_GET_LESS_OR_EQUAL_STUD, e);
            throw new DAOException (ERROR_GET_LESS_OR_EQUAL_STUD, e);
        }
    }
    
    @Override
    public List<GroupEntity> getAll() throws DAOException {
        try (Connection con = universityConnectionDAOFactory.createConnection();
             Statement statement = con.createStatement();
             ResultSet resultSet = statement.executeQuery(DAOPropertiesCache
            		 .getInstance(QUERIES_FILE_NAME).getProperty(SELECT_ALL))) {
            
            List<GroupEntity> result = new ArrayList<>();
            
            while (resultSet.next()) {
                result.add(new GroupEntity(resultSet.getInt(GROUP_ID), 
                                        resultSet.getString(GROUP_NAME)));
            }
            return result;
        } catch (SQLException | IOException e) {
        	LOGGER.error(ERROR_GET_ALL_GROUP, e);
            throw new DAOException(ERROR_GET_ALL_GROUP, e);
        }
    }
    
    @Override
    public Integer insert(List<GroupEntity> groups) throws DAOException {
        try (Connection connection = universityConnectionDAOFactory.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DAOPropertiesCache
            		 .getInstance(QUERIES_FILE_NAME).getProperty(INSERT))) {
            
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
        } catch (SQLException | IOException e) {
        	LOGGER.error(ERROR_INSERT_GROUP, e);
            throw new DAOException(ERROR_INSERT_GROUP, e);
        }
    }
}
