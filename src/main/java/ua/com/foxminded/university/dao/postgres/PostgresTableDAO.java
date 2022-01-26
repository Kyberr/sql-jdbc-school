package ua.com.foxminded.university.dao.postgres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import ua.com.foxminded.university.dao.DAOException;
import ua.com.foxminded.university.dao.DAOException.DatabaseConnectionFail;
import ua.com.foxminded.university.dao.DAOException.TableCreationFail;
import ua.com.foxminded.university.dao.TableDAO;

public class PostgresTableDAO implements TableDAO {
    private static final String ERROR_TABALES_CREATION = "The creation of the tables in the database is failed!";
    
    public int createTables(String sql) throws TableCreationFail {
        try (Connection connection = PostgresDAOFactory.creatConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
             int status = preparedStatement.executeUpdate();
             return status;
        } catch (DatabaseConnectionFail | SQLException e) {
            throw new DAOException.TableCreationFail(ERROR_TABALES_CREATION, e);
        }
    }
}
