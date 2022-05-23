package ua.com.foxminded.sql_jdbc_school.service;

import java.util.List;
import ua.com.foxminded.sql_jdbc_school.dao.DAOException;
import ua.com.foxminded.sql_jdbc_school.dao.DAOFactory;
import ua.com.foxminded.sql_jdbc_school.dao.DAOEntity;

public class TableService implements Table<Integer> {
    private static final String ERROR_TABLE_CREATION = "The table creation service dosn't work.";
    private static final String SQL_FILE_NAME_KEY = "SQLFileName";
    private Reader reader;
    private Parser parser;
    
    public TableService(Reader reader, Parser parser) {
        this.reader = reader;
        this.parser = parser;
    }
    
    @Override
    public Integer creatTables() throws ServiceException {
        try {
            String fileName = ReaderServicesPropertiesCache.getInstance()
            											   .getProperty(SQL_FILE_NAME_KEY);
            List<String> sqlScriptList = reader.toList(fileName);
            String sqlScript = parser.toStringList(sqlScriptList); 
            DAOFactory postgresDAOFactory = DAOFactory.getDAOFactory(DAOFactory.POSTGRES);
            DAOEntity postgresEntity = postgresDAOFactory.getEntity();
            return postgresEntity.create(sqlScript);
        } catch (ServiceException | DAOException e) {
            throw new ServiceException(ERROR_TABLE_CREATION, e);
        }
    }
}
