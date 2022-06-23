package ua.com.foxminded.sql_jdbc_school.dao.jdbc;

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

import ua.com.foxminded.sql_jdbc_school.dao.DAOConnectionPool;
import ua.com.foxminded.sql_jdbc_school.dao.DAOException;
import ua.com.foxminded.sql_jdbc_school.dao.DAOPropertiesCache;
import ua.com.foxminded.sql_jdbc_school.dao.GroupDAO;
import ua.com.foxminded.sql_jdbc_school.entity.GroupEntity;

public class JdbcGroupDAO implements GroupDAO {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final int BAD_STATUS = 0;
    private static final String DELETE_ALL_ERROR = "The deletion operation of all students failed.";
    private static final String DELETE_ALL = "deleteAll";
    private static final String SQL_QUERIES_FILENAME = "group-queries.properties";
    private static final String SELECT_INCLUSIVE_LESS_STUDENTS = "getGroupsHavingLessOrEqualStudents";
    private static final String SELECT_ALL = "getAll";
    private static final String INSERT = "insert";
    private static final String GROUP_ID = "group_id";
    private static final String GROUP_NAME = "group_name";
    private static final String INSERT_GROUP_ERROR = "The group has not been iserted to the database.";
    private static final String GET_ALL_GROUP_ERROR = "The getting data from the " + "\"groups\" table is failed.";
    private static final String GET_LESS_OR_EQUAL_STUD_ERROR = "Getting the groups with a less or "
            + "equal number of students is failed.";

    DAOConnectionPool jdbcDaoConnectionPool;

    public JdbcGroupDAO(DAOConnectionPool jdbcDaoConnectionPool) {
        this.jdbcDaoConnectionPool = jdbcDaoConnectionPool;
    }

    @Override
    public Integer deleteAll() throws DAOException {
        try {
            int status = 0;
            Connection con = jdbcDaoConnectionPool.getConnection();

            try (PreparedStatement prStatement = con
                    .prepareStatement(DAOPropertiesCache.getInstance(SQL_QUERIES_FILENAME).getProperty(DELETE_ALL));) {

                status = prStatement.executeUpdate();
            }
            jdbcDaoConnectionPool.releaseConnection(con);
            return status;
        } catch (SQLException e) {
            LOGGER.error(DELETE_ALL_ERROR, e);
            throw new DAOException(DELETE_ALL_ERROR, e);
        }
    }

    @Override
    public List<GroupEntity> getGroupsHavingLessOrEqualStudents(int students) throws DAOException {
        try {
            Connection con = jdbcDaoConnectionPool.getConnection();
            List<GroupEntity> result = new ArrayList<>();

            try (PreparedStatement statement = con.prepareStatement(String.format(
                    DAOPropertiesCache.getInstance(SQL_QUERIES_FILENAME).getProperty(SELECT_INCLUSIVE_LESS_STUDENTS),
                    students)); ResultSet resultSet = statement.executeQuery();) {

                while (resultSet.next()) {
                    result.add(
                            new GroupEntity((Integer) resultSet.getObject(GROUP_ID), resultSet.getString(GROUP_NAME)));
                }
            }
            jdbcDaoConnectionPool.releaseConnection(con);
            return result;
        } catch (ClassCastException | SQLException e) {
            LOGGER.error(GET_LESS_OR_EQUAL_STUD_ERROR, e);
            throw new DAOException(GET_LESS_OR_EQUAL_STUD_ERROR, e);
        }
    }

    @Override
    public List<GroupEntity> getAll() throws DAOException {
        try {
            Connection con = jdbcDaoConnectionPool.getConnection();
            List<GroupEntity> students = new ArrayList<>();
            
            try (Statement statement = con.createStatement();
                 ResultSet resultSet = statement.executeQuery(DAOPropertiesCache
                         .getInstance(SQL_QUERIES_FILENAME)
                         .getProperty(SELECT_ALL));) {

                while (resultSet.next()) {
                    students.add(new GroupEntity(resultSet.getInt(GROUP_ID), 
                                                 resultSet.getString(GROUP_NAME)));
                }
            }
            jdbcDaoConnectionPool.releaseConnection(con);
            return students;
        } catch (SQLException e) {
            LOGGER.error(GET_ALL_GROUP_ERROR, e);
            throw new DAOException(GET_ALL_GROUP_ERROR, e);
        }
    }

    @Override
    public Integer insert(List<GroupEntity> groups) throws DAOException {
        try {
            Connection con = jdbcDaoConnectionPool.getConnection();
            int status = BAD_STATUS;

            try (PreparedStatement prStatement = con.prepareStatement(DAOPropertiesCache
                    .getInstance(SQL_QUERIES_FILENAME)
                    .getProperty(INSERT));) {

                con.setAutoCommit(false);
                Savepoint save = con.setSavepoint();

                try {
                    for (GroupEntity group : groups) {
                        prStatement.setString(1, group.getGroupName());
                        status = prStatement.executeUpdate();
                    }
                    con.commit();
                } catch (SQLException e) {
                    con.rollback(save);
                    throw new SQLException(e);
                }
            }
            jdbcDaoConnectionPool.releaseConnection(con);
            return status;
        } catch (SQLException e) {
            LOGGER.error(INSERT_GROUP_ERROR, e);
            throw new DAOException(INSERT_GROUP_ERROR, e);
        }
    }
}
