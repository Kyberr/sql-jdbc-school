package ua.com.foxminded.sql_jdbc_school.dao.university;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import ua.com.foxminded.sql_jdbc_school.dao.DAOException;
import ua.com.foxminded.sql_jdbc_school.dao.TableDAO;

public class UniversityTableDAO implements TableDAO {
    private static final String ERROR_TABALES_CREATION = "The creation of the tables in the database is failed!";
    
    public int createTables(String sql) throws DAOException.TableCreationFail {
        try (Connection connection = UniversityDAOFactory.creatConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
             int status = preparedStatement.executeUpdate();
             return status;
        } catch (DAOException.DatabaseConnectionFail | SQLException e) {
            throw new DAOException.TableCreationFail(ERROR_TABALES_CREATION, e);
        }
    }
}
