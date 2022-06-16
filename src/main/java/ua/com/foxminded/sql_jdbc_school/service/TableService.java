package ua.com.foxminded.sql_jdbc_school.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.com.foxminded.sql_jdbc_school.dao.DAOException;
import ua.com.foxminded.sql_jdbc_school.dao.DAO;

public class TableService implements Table<Integer> {

	private static final Logger LOGGER = LogManager.getLogger();
	private static final String ERROR_CREATE_STUDENT_COURSE_TABLE = "The StudentCourse table "
																    + "has not been created.";
    private static final String ERROR_CREATE_TABLE = "The table creation service dosn't work.";
    private static final String TABLES_SQL_FILENAME = "tables.sql";
    private static final String STUDENT_COURSE_SQL_FILENAME = "studentCourse-queries.properties";
    private static final String STUDENT_COURSE_SQL_QUERY = "createStudentCourseDAO";
    private final Reader reader;
    private final Parser parser;
    private final DAO dao;
    
    public TableService(Reader reader, Parser parser, DAO dao) {
		this.reader = reader;
		this.parser = parser;
		this.dao = dao;
	}

	@Override
    public Integer createTables() throws ServiceException {
        try {
            List<String> sqlScriptList = reader.read(TABLES_SQL_FILENAME);
            String tablesSqlScript = parser.toString(sqlScriptList); 
            return dao.create(tablesSqlScript);
        } catch (ServiceException | DAOException e) {
        	LOGGER.error(ERROR_CREATE_TABLE, e);
            throw new ServiceException(ERROR_CREATE_TABLE, e);
        }
    }
    
    @Override 
    public Integer createStudentCourseTable() throws ServiceException {
    	try (InputStream input = this.getClass()
									 .getClassLoader()
									 .getResourceAsStream(STUDENT_COURSE_SQL_FILENAME);) {
    		
    		Properties properties = new Properties();
    		properties.load(input);
    		String studentCourseSqlScript = properties.getProperty(STUDENT_COURSE_SQL_QUERY);
    		return dao.create(studentCourseSqlScript);
    	} catch (DAOException | IOException e) {
    		LOGGER.error(ERROR_CREATE_STUDENT_COURSE_TABLE, e);
    		throw new ServiceException(ERROR_CREATE_STUDENT_COURSE_TABLE, e);
    	}
    }
}
