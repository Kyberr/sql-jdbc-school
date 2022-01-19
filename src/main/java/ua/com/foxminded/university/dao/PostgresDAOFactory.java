package ua.com.foxminded.university.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PostgresDAOFactory extends DAOFactory {
    private static final Logger LOGGER = LogManager.getLogger(PostgresDAOFactory.class);
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String ERROR_CONNECT = "The connection is failure";
    private static final String LOG_ERROR_CONNECT = "The connection is failure. The SQL state: {}\n{}.";

    public static Connection createConnection(String user, String password) throws SQLException {
        try {
            return DriverManager.getConnection(DB_URL, user, password);
        } catch (SQLException e) {
            LOGGER.error(LOG_ERROR_CONNECT, e.getSQLState(), e.getMessage());
            throw new SQLException (ERROR_CONNECT, e); 
        }
    }
    
    @Override
    public AccountDAO getAccountDAO(String superuserName, String superuserPass) {
        return new PostgresAccountDAO(superuserName, superuserPass);
    }
    
    @Override
    public DatabaseDAO getDatabaseDAO(String superuserName, String superuserPass) {
        return new PostgresDatabaseDAO(superuserName, superuserPass);
    }
    /*
    public StudentDAO getStudentDAO() {
        return new PostgresStudentDAO();
    }
    
    public GroupDAO getGroupDAO() {
        return new PostgresGroupDAO();
    }
    
    public CourseDAO getCourseDAO() {
        return new PostgresCourseDAO();
    }
    */
}
