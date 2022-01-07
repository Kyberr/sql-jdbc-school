package ua.com.foxminded.sql_jdbc_school;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.InvalidPathException;
import java.sql.SQLException;
import java.util.List;
import ua.com.foxminded.sql_jdbc_school.dao.DatabaseConmmander;
import ua.com.foxminded.sql_jdbc_school.dao.DatabaseGenerator;
import ua.com.foxminded.sql_jdbc_school.services.Parser;
import ua.com.foxminded.sql_jdbc_school.services.Reader;

public class SqlJdbcSchoolSession {
    private Reader reader;
    private DatabaseConmmander databaseCommander;
    private DatabaseGenerator databaseGenerator;
    private Parser parser;
    
    public SqlJdbcSchoolSession(Reader reader, 
                                DatabaseConmmander databaseCommander, 
                                DatabaseGenerator databaseGenerator,
                                Parser parser) {
        this.reader = reader;
        this.databaseCommander = databaseCommander;
        this.databaseGenerator = databaseGenerator;
        this.parser = parser;
    }
    
    public void boot(String tablesCreationSQLScript) throws InvalidPathException, 
                                                            IOException, 
                                                            SQLException {
        URL tablesCreationSQLScriptFile = SqlJdbcSchoolSession.class.getClassLoader()
                                                                    .getResource(tablesCreationSQLScript);
        List<String> SQLScript = reader.toList(new File(tablesCreationSQLScriptFile.getFile()).getPath());
        databaseGenerator.generate(parser.toStringList(SQLScript));

        System.out.println(parser.toStringList(SQLScript));
        
        
        
        
        
        
    }
    

}
