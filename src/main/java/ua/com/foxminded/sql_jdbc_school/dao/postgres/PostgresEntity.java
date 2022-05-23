package ua.com.foxminded.sql_jdbc_school.dao.postgres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import ua.com.foxminded.sql_jdbc_school.dao.DAOException;
import ua.com.foxminded.sql_jdbc_school.dao.Entity;

public class PostgresEntity implements Entity{
	private static final String ERROR_ENTITY_CREATION = "The creation of the tables "
			   + "in the database is failed!";	
	
	@Override
	    public Integer create(String sql) throws DAOException {
	        try (Connection connection = PostgresDAOFactory.creatConnection();
	             PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
	             int status = preparedStatement.executeUpdate();
	             return status;
	        } catch (DAOException | SQLException e) {
	            throw new DAOException(ERROR_ENTITY_CREATION, e);
	        }
	    }
}
