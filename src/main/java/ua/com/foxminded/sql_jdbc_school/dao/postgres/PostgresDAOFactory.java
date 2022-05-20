package ua.com.foxminded.sql_jdbc_school.dao.postgres;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import ua.com.foxminded.sql_jdbc_school.dao.CourseDAO;
import ua.com.foxminded.sql_jdbc_school.dao.DAOException;
import ua.com.foxminded.sql_jdbc_school.dao.DAOFactory;
import ua.com.foxminded.sql_jdbc_school.dao.GroupDAO;
import ua.com.foxminded.sql_jdbc_school.dao.StudentCourseDAO;
import ua.com.foxminded.sql_jdbc_school.dao.StudentDAO;
import ua.com.foxminded.sql_jdbc_school.dao.TableDAO;

public class PostgresDAOFactory extends DAOFactory {
    private static final String DB_URL_KEY = "PostgresDatabaseURL";
    private static final String USER_NAME_KEY = "PostgresUser";
    private static final String USER_PASS_KEY = "PostgresPassword";
    private static final String CONNECT_ERROR = "The database connection is failure."; 
    
    public static Connection creatConnection() throws DAOException {
        try {
            return DriverManager.getConnection(PostgresDatabasePropertiesCache.getInstance()
                                                                       	   .getProperty(DB_URL_KEY), 
                                               PostgresDatabasePropertiesCache.getInstance()
                                                                       	   .getProperty(USER_NAME_KEY),
                                               PostgresDatabasePropertiesCache.getInstance()
                                                                           .getProperty(USER_PASS_KEY));
        } catch (DAOException | SQLException e) {
            throw new DAOException(CONNECT_ERROR, e);
        }
    }
    
    @Override
    public TableDAO getTableDAO() {
        return new PostgresTableDAO();
    }
    
    @Override
    public StudentDAO getStudentDAO() {
        return new PostgresStudentDAO();
    }
    
    @Override
    public GroupDAO getGroupDAO() {
        return new PostgresGroupDAO();
    }

    @Override
    public CourseDAO getCourseDAO() {
        return new PostgresCourseDAO();
    }
    
    @Override
    public StudentCourseDAO getStudentCourseDAO() {
        return new PostgresStudentCourseDAO();
    }
}
