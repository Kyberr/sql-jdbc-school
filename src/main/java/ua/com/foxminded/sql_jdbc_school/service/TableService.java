package ua.com.foxminded.sql_jdbc_school.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import ua.com.foxminded.sql_jdbc_school.dao.DAOException;
import ua.com.foxminded.sql_jdbc_school.dao.DAOFactory;
import ua.com.foxminded.sql_jdbc_school.dao.DAO;

public class TableService implements Table<Integer> {
	private static final String ERROR_CREATE_STUDENT_COURSE_TABLE = "The StudentCourse table "
																  + "has not been created.";
    private static final String ERROR_TABLE_CREATION = "The table creation service dosn't work.";
    private static final String SQL_FILE_NAME = "SQLFileName";
    private static final String STUDENT_COURSE_QUERIES_FILE_NAME = "studentCourseQueries.properties";
    private static final String STUDENT_COURSE_SQL_QUERY = "createEntity";
    private Reader reader;
    private Parser parser;
    
    public TableService(Reader reader, Parser parser) {
        this.reader = reader;
        this.parser = parser;
    }
    
    @Override
    public Integer createTables() throws ServiceException {
        try {
            String fileName = ReaderServicesPropertiesCache.getInstance()
            											   .getProperty(SQL_FILE_NAME);
            List<String> sqlScriptList = reader.toList(fileName);
            String sqlScript = parser.toStringList(sqlScriptList); 
            DAOFactory postgresDAOFactory = DAOFactory.getDAOFactory(DAOFactory.POSTGRES);
            DAO postgresEntities = postgresDAOFactory.getDAO();
            return postgresEntities.create(sqlScript);
        } catch (ServiceException | DAOException e) {
            throw new ServiceException(ERROR_TABLE_CREATION, e);
        }
    }
    
    @Override 
    public Integer createStudentCourseTable() throws ServiceException {
    	try (InputStream input = this.getClass()
									 .getClassLoader()
									 .getResourceAsStream(STUDENT_COURSE_QUERIES_FILE_NAME);) {
    		
    		Properties properties = new Properties();
    		properties.load(input);
    		String studentCourse = properties.getProperty(STUDENT_COURSE_SQL_QUERY);
    		DAOFactory postgresDAOFactory = DAOFactory.getDAOFactory(DAOFactory.POSTGRES);
    		DAO postgresDAO = postgresDAOFactory.getDAO();
    		return postgresDAO.create(studentCourse);
    	} catch (DAOException | IOException e) {
    		throw new ServiceException(ERROR_CREATE_STUDENT_COURSE_TABLE, e);
    	}
    }
}
