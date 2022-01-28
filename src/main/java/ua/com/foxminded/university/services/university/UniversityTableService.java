package ua.com.foxminded.university.services.university;

import java.util.List;
import ua.com.foxminded.university.PropertyCache;
import ua.com.foxminded.university.dao.DAOException;
import ua.com.foxminded.university.dao.DAOFactory;
import ua.com.foxminded.university.dao.TableDAO;
import ua.com.foxminded.university.services.Parser;
import ua.com.foxminded.university.services.Reader;
import ua.com.foxminded.university.services.ServicesException;
import ua.com.foxminded.university.services.TableService;

public class UniversityTableService implements TableService<Integer> {
    private static final String ERROR_TABLE_CREATION = "The table creation service dosn't work.";
    private static final String SQL_FILE_NAME_KEY = "SQLFileName";
    private Reader reader;
    private Parser parser;
    
    public UniversityTableService(Reader reader, Parser parser) {
        this.reader = reader;
        this.parser = parser;
    }
    
    public Integer createTables() throws ServicesException.TableCreationFail {
        try {
            String fileName = PropertyCache.getInstance().getProperty(SQL_FILE_NAME_KEY);
            List<String> sqlScriptList = reader.toList(fileName);
            String sqlScript = parser.toStringList(sqlScriptList); 
            
            DAOFactory universityDAOFactory = DAOFactory.getDAOFactory(DAOFactory.UNIVERSITY);
            TableDAO universityTableDAO = universityDAOFactory.getTableDAO();
            return universityTableDAO.createTables(sqlScript);
        } catch (DAOException.PropertyFileLoadFail |
                 ServicesException.ReadFail |
                 DAOException.TableCreationFail e) {
            throw new ServicesException.TableCreationFail(ERROR_TABLE_CREATION, e);
        }
    }
}
