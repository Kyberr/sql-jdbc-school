package ua.com.foxminded.sql_jdbc_school.dao.university;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import ua.com.foxminded.sql_jdbc_school.dao.CourseDAO;
import ua.com.foxminded.sql_jdbc_school.dao.DAOException;
import ua.com.foxminded.sql_jdbc_school.dao.DAOFactory;
import ua.com.foxminded.sql_jdbc_school.dao.DAOPropertiesCache;
import ua.com.foxminded.sql_jdbc_school.dao.DAO;
import ua.com.foxminded.sql_jdbc_school.dao.GroupDAO;
import ua.com.foxminded.sql_jdbc_school.dao.StudentCourseDAO;
import ua.com.foxminded.sql_jdbc_school.dao.StudentDAO;

public class UniversityDAOFactory extends DAOFactory {
	private static final String PROPERTIES_FILE_NAME = "db.properties";
    private static final String DB_URL = "UniversityDatabaseURL";
    private static final String USER_NAME = "UniversityUser";
    private static final String USER_PASS = "UniversityPassword";
    private static final String CONNECT_ERROR = "The database connection is failure."; 
    
    public static Connection creatConnection() throws DAOException {
        try {
        	DAOPropertiesCache dbProperitesCache = DAOPropertiesCache.getInstance(PROPERTIES_FILE_NAME);
            return DriverManager.getConnection(dbProperitesCache.getProperty(DB_URL), 
            								   dbProperitesCache.getProperty(USER_NAME),
            								   dbProperitesCache.getProperty(USER_PASS));
        } catch (DAOException | SQLException e) {
            throw new DAOException(CONNECT_ERROR, e);
        }
    }
    
    @Override
    public StudentDAO getStudentDAO() {
        return new UniverstiyStudentDAO();
    }
    
    @Override
    public GroupDAO getGroupDAO() {
        return new UniverstiyGroupDAO();
    }

    @Override
    public CourseDAO getCourseDAO() {
        return new UniversityCourseDAO();
    }
    
    @Override
    public StudentCourseDAO getStudentCourseDAO() {
        return new UniversityStudentCourseDAO();
    }
    
    @Override
    public DAO getDAO() {
    	return new UniversityDAO();
    }
}
