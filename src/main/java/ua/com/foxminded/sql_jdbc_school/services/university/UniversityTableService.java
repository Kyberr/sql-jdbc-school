package ua.com.foxminded.sql_jdbc_school.services.university;

import java.util.List;
import ua.com.foxminded.sql_jdbc_school.dao.DAOException;
import ua.com.foxminded.sql_jdbc_school.dao.DAOFactory;
import ua.com.foxminded.sql_jdbc_school.dao.TableDAO;
import ua.com.foxminded.sql_jdbc_school.services.Parser;
import ua.com.foxminded.sql_jdbc_school.services.Reader;
import ua.com.foxminded.sql_jdbc_school.services.ReaderServicesPropertiesCache;
import ua.com.foxminded.sql_jdbc_school.services.ServicesException;
import ua.com.foxminded.sql_jdbc_school.services.TableService;

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
    public Integer creatTables() throws ServicesException.TableCreationFail {
        try {
            String fileName = ReaderServicesPropertiesCache.getInstance().getProperty(SQL_FILE_NAME_KEY);
            List<String> sqlScriptList = reader.toList(fileName);
            String sqlScript = parser.toStringList(sqlScriptList); 
            DAOFactory universityDAOFactory = DAOFactory.getDAOFactory(DAOFactory.UNIVERSITY);
            TableDAO universityTableDAO = universityDAOFactory.getTableDAO();
            return universityTableDAO.createTables(sqlScript);
        } catch (ServicesException.PropertyFileLoadingFail |
                 ServicesException.ReadFail |
                 DAOException.TableCreationFail e) {
            throw new ServicesException.TableCreationFail(ERROR_TABLE_CREATION, e);
        }
    }
}
