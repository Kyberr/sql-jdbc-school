package ua.com.foxminded.sql_jdbc_school.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.com.foxminded.sql_jdbc_school.dao.ConnectionDAOFactory;
import ua.com.foxminded.sql_jdbc_school.dao.DAOException;
import ua.com.foxminded.sql_jdbc_school.dao.DAOPropertiesCache;
import ua.com.foxminded.sql_jdbc_school.dao.GenericDAO;

public abstract class JdbcGenericDAO<T> implements GenericDAO<T, Integer, String> {
    public static final String STUDENTS = "students";
    public static final String COURSES = "courses";
    public static final String GROUPS = "groups";
    private static final String ERROR_DELETE_ALL = "Deleting of objects from the database is failed.";
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String SQL_QUERIES_FILENAME = "generic-queries.properties";

    ConnectionDAOFactory universityConnectionDAOFactory;

    protected JdbcGenericDAO(ConnectionDAOFactory universityConnectionDAOFactory) {
        this.universityConnectionDAOFactory = universityConnectionDAOFactory;
    }

    @Override
    public Integer deleteAll(String typeObjects) throws DAOException {
        int status = 0;

        try (Connection con = universityConnectionDAOFactory.createConnection();
             PreparedStatement prStatement = con.prepareStatement(DAOPropertiesCache
                     .getInstance(SQL_QUERIES_FILENAME)
                     .getProperty(typeObjects));) {

            status = prStatement.executeUpdate();
        } catch (SQLException | DAOException e) {
            LOGGER.error(ERROR_DELETE_ALL, e);
            throw new DAOException(ERROR_DELETE_ALL, e);
        }
        return status;
    }
}
