package ua.com.foxminded.sql_jdbc_school;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.InvalidPathException;
import java.util.List;

import ua.com.foxminded.sql_jdbc_school.services.Reader;

public class SqlJdbcSchoolSession {
    private Reader reader;
    
    public SqlJdbcSchoolSession(Reader reader) {
        this.reader = reader;
    }
    
    public void boot(String tablesCreationSQLScript) throws InvalidPathException, IOException {
        URL tablesCreationSQLScriptFile = SqlJdbcSchoolSession.class.getClassLoader()
                                                                    .getResource(tablesCreationSQLScript);
        List<String> SQLScript = reader.toList(new File(tablesCreationSQLScriptFile.getFile()).getPath());
        
        
        
    }
    

}
