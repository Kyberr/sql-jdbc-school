package ua.com.foxminded.university.dao.postgres;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import ua.com.foxminded.university.dao.DAOException;
import ua.com.foxminded.university.dao.DAOException.DatabaseConnectionFail;
import ua.com.foxminded.university.dao.DAOException.PropertyFileLoadFail;
import ua.com.foxminded.university.dao.DAOFactory;
import ua.com.foxminded.university.dao.TableDAO;

public class PostgresDAOFactory extends DAOFactory {
    private static final String DB_URL_KEY = "DatabaseURL";
    private static final String USER_NAME_KEY = "User";
    private static final String USER_PASS_KEY = "Password";
    private static final String CONNECT_ERROR = "The database connection is failure."; 
    
    
    
    public static Connection creatConnection() throws DatabaseConnectionFail {
        try {
            return DriverManager.getConnection(PostgresDAOPropertyCache.getInstance()
                                                                       .getProperty(DB_URL_KEY), 
                                               PostgresDAOPropertyCache.getInstance()
                                                                       .getProperty(USER_NAME_KEY),
                                               PostgresDAOPropertyCache.getInstance()
                                                                       .getProperty(USER_PASS_KEY));
        } catch (PropertyFileLoadFail | SQLException e) {
            throw new DAOException.DatabaseConnectionFail(CONNECT_ERROR, e);
        }
    }
    
    @Override
    public TableDAO getTableDAO() {
        return new PostgresTableDAO();
    }
}
