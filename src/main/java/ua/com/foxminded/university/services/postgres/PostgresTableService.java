package ua.com.foxminded.university.services.postgres;

import java.util.List;
import ua.com.foxminded.university.dao.DAOException;
import ua.com.foxminded.university.dao.DAOFactory;
import ua.com.foxminded.university.dao.TableDAO;
import ua.com.foxminded.university.dao.postgres.PostgresDAOPropertyCache;
import ua.com.foxminded.university.services.Parser;
import ua.com.foxminded.university.services.Reader;
import ua.com.foxminded.university.services.ServicesException;
import ua.com.foxminded.university.services.TableService;

public class PostgresTableService implements TableService<Integer> {
    private static final String ERROR_TABLE_CREATION = "The table creation service dosn't work.";
    private static final String SQL_FILE_NAME_KEY = "SQLFileName";
    private Reader reader;
    private Parser parser;
    
    public PostgresTableService(Reader reader, Parser parser) {
        this.reader = reader;
        this.parser = parser;
    }
    
    public Integer createTables() throws ServicesException.TableCreationFail {
        try {
            String fileName = PostgresDAOPropertyCache.getInstance().getProperty(SQL_FILE_NAME_KEY);
            List<String> sqlScriptList = reader.toList(fileName);
            String sqlScript = parser.toStringList(sqlScriptList); 
            
            DAOFactory postgresDAOFactory = DAOFactory.getDAOFactory(DAOFactory.UNIVERSITY);
            TableDAO postgresTableDAO = postgresDAOFactory.getTableDAO();
            return postgresTableDAO.createTables(sqlScript);
        } catch (DAOException.DatabaseConnectionFail | 
                 DAOException.TableCreationFail | 
                 ServicesException.ReadFail |
                 DAOException.PropertyFileLoadFail e ) {
            throw new ServicesException.TableCreationFail(ERROR_TABLE_CREATION, e);
        }
    }
}
