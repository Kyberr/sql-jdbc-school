package ua.com.foxminded.university.dao.university;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import ua.com.foxminded.university.PropertyCache;
import ua.com.foxminded.university.dao.DAOException;
import ua.com.foxminded.university.dao.DAOException.PropertyFileLoadFail;
import ua.com.foxminded.university.dao.DAOFactory;
import ua.com.foxminded.university.dao.StudentDAO;
import ua.com.foxminded.university.dao.TableDAO;

public class UniversityDAOFactory extends DAOFactory {
    private static final String DB_URL_KEY = "UniversityDatabaseURL";
    private static final String USER_NAME_KEY = "UniversityUser";
    private static final String USER_PASS_KEY = "UniversityPassword";
    private static final String CONNECT_ERROR = "The database connection is failure."; 
    
    
    
    public static Connection creatConnection() throws DAOException.DatabaseConnectionFail {
        try {
            return DriverManager.getConnection(PropertyCache.getInstance()
                                                                       .getProperty(DB_URL_KEY), 
                                               PropertyCache.getInstance()
                                                                       .getProperty(USER_NAME_KEY),
                                               PropertyCache.getInstance()
                                                                       .getProperty(USER_PASS_KEY));
        } catch (PropertyFileLoadFail | SQLException e) {
            throw new DAOException.DatabaseConnectionFail(CONNECT_ERROR, e);
        }
    }
    
    @Override
    public TableDAO getTableDAO() {
        return new UniversityTableDAO();
    }
    
    @Override
    public StudentDAO getStudentDAO() {
        return new UniversityStudentDAO();
    }
}
