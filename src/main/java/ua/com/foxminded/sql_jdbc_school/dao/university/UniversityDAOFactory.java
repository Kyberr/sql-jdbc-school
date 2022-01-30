package ua.com.foxminded.sql_jdbc_school.dao.university;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import ua.com.foxminded.sql_jdbc_school.dao.DAOException;
import ua.com.foxminded.sql_jdbc_school.dao.DAOFactory;
import ua.com.foxminded.sql_jdbc_school.dao.GroupDAO;
import ua.com.foxminded.sql_jdbc_school.dao.StudentDAO;
import ua.com.foxminded.sql_jdbc_school.dao.TableDAO;
import ua.com.foxminded.sql_jdbc_school.dao.DAOException.PropertyFileLoadFail;
import ua.com.foxminded.sql_jdbc_school.services.PropertyCache;

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
    
    @Override
    public GroupDAO getGroupDAO() {
        return new UniversityGroupDAO();
    }
}
