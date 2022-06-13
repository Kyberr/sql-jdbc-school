package ua.com.foxminded.sql_jdbc_school.dao.university;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.com.foxminded.sql_jdbc_school.dao.DAOException;
import ua.com.foxminded.sql_jdbc_school.dao.DAO;

public class UniversityDAO implements DAO {
	private static final Logger LOGGER = LogManager.getLogger();
	private static final String ERROR_DAO_CREATION = "The creation of the tables "
			   									   + "in the database is failed!";	
	
	@Override
	    public Integer create(String sql) throws DAOException {
	        try (Connection connection = UniversityDAOFactory.creatConnection();
	             PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
	             int status = preparedStatement.executeUpdate();
	             return status;
	        } catch (DAOException | SQLException e) {
	        	LOGGER.error(ERROR_DAO_CREATION, e);
	            throw new DAOException(ERROR_DAO_CREATION, e);
	        }
	    }
}
