package ua.com.foxminded.sql_jdbc_school.service.university;

import java.util.List;
import ua.com.foxminded.sql_jdbc_school.dao.DAOException;
import ua.com.foxminded.sql_jdbc_school.dao.DAOFactory;
import ua.com.foxminded.sql_jdbc_school.dao.TableDAO;
import ua.com.foxminded.sql_jdbc_school.service.Parser;
import ua.com.foxminded.sql_jdbc_school.service.Reader;
import ua.com.foxminded.sql_jdbc_school.service.ReaderServicesPropertiesCache;
import ua.com.foxminded.sql_jdbc_school.service.ServiceException;
import ua.com.foxminded.sql_jdbc_school.service.TableService;

public class UniversityTableService implements TableService<Integer> {
    private static final String ERROR_TABLE_CREATION = "The table creation service dosn't work.";
    private static final String SQL_FILE_NAME_KEY = "SQLFileName";
    private Reader reader;
    private Parser parser;
    
    public UniversityTableService(Reader reader, Parser parser) {
        this.reader = reader;
        this.parser = parser;
    }
    
    @Override
    public Integer creatTables() throws ServiceException.TableCreationFail {
        try {
            String fileName = ReaderServicesPropertiesCache.getInstance().getProperty(SQL_FILE_NAME_KEY);
            List<String> sqlScriptList = reader.toList(fileName);
            String sqlScript = parser.toStringList(sqlScriptList); 
            DAOFactory universityDAOFactory = DAOFactory.getDAOFactory(DAOFactory.UNIVERSITY);
            TableDAO universityTableDAO = universityDAOFactory.getTableDAO();
            return universityTableDAO.createTables(sqlScript);
        } catch (ServiceException.PropertyFileLoadingFail |
                 ServiceException.ReadFail |
                 DAOException.TableCreationFail e) {
            throw new ServiceException.TableCreationFail(ERROR_TABLE_CREATION, e);
        }
    }
}
