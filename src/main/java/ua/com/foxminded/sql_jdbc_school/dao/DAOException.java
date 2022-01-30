package ua.com.foxminded.sql_jdbc_school.dao;

import ua.com.foxminded.sql_jdbc_school.BaseException;

public class DAOException {
    
    public static class DatabaseConnectionFail extends BaseException {
        private static final long serialVersionUID = 1L;
        
        public DatabaseConnectionFail(String message, Throwable cause) {
            super(message, cause);
        }
    }
    
    public static class TableCreationFail extends BaseException {
        private static final long serialVersionUID = 2L;

        public TableCreationFail(String message, Throwable cause) {
            super(message, cause);
        }
    }
    
    public static class PropertyFileLoadFail extends BaseException {
        private static final long serialVersionUID = 3L;
        
        public PropertyFileLoadFail (String message, Throwable cause) {
            super(message, cause);
        }
        
        public PropertyFileLoadFail (Throwable cause) {
            super(cause);
        }
    }
    
    public static class StudentInsertionFail extends BaseException {
        private static final long serialVersionUID = 4L;
        
        
        public StudentInsertionFail(Throwable cause) {
            super(cause);
        }
    }
    
    public static class GroupInsertionFail extends BaseException {
        private static final long serialVersionUID = 5L;
        
        public GroupInsertionFail(Throwable cause) {
            super(cause);
        }
    }
}
