package ua.com.foxminded.sql_jdbc_school.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import ua.com.foxminded.sql_jdbc_school.dao.DAOException;
import ua.com.foxminded.sql_jdbc_school.dao.DAOFactory;
import ua.com.foxminded.sql_jdbc_school.dao.DAO;

public class TableService implements Table<Integer> {
	private static final String ERROR_STUDENT_COURSE_TABLE_CREATION = "The StudentCourse table "
																    + "has not been created.";
    private static final String ERROR_TABLES_CREATION = "The table creation service dosn't work.";
    private static final String TABLES_SQL_FILE_NAME = "tablesCreationSqlScript.txt";
    private static final String STUDENT_COURSE_SQL_FILE_NAME = "studentCourseQueries.properties";
    private static final String STUDENT_COURSE_SQL_QUERY = "createStudentCourseDAO";
    private Reader reader;
    private Parser parser;
    
    public TableService(Reader reader, Parser parser) {
        this.reader = reader;
        this.parser = parser;
    }
    
    @Override
    public Integer createTables() throws ServiceException {
        try {
            List<String> sqlScriptList = reader.read(TABLES_SQL_FILE_NAME);
            String tablesSqlScript = parser.toString(sqlScriptList); 
            DAOFactory postgresDAOFactory = DAOFactory.getDAOFactory(DAOFactory.POSTGRES);
            DAO postgresDAO = postgresDAOFactory.getDAO();
            return postgresDAO.create(tablesSqlScript);
        } catch (ServiceException | DAOException e) {
            throw new ServiceException(ERROR_TABLES_CREATION, e);
        }
    }
    
    @Override 
    public Integer createStudentCourseTable() throws ServiceException {
    	try (InputStream input = this.getClass()
									 .getClassLoader()
									 .getResourceAsStream(STUDENT_COURSE_SQL_FILE_NAME);) {
    		
    		Properties properties = new Properties();
    		properties.load(input);
    		String studentCourseSqlScript = properties.getProperty(STUDENT_COURSE_SQL_QUERY);
    		DAOFactory postgresDAOFactory = DAOFactory.getDAOFactory(DAOFactory.POSTGRES);
    		DAO postgresDAO = postgresDAOFactory.getDAO();
    		return postgresDAO.create(studentCourseSqlScript);
    	} catch (DAOException | IOException e) {
    		throw new ServiceException(ERROR_STUDENT_COURSE_TABLE_CREATION, e);
    	}
    }
}
