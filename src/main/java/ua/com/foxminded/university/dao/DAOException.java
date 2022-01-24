package ua.com.foxminded.university.dao;

public class DAOException {
    
    public static class DatabaseConnectionFail extends BaseException {
        private static final long serialVersionUID = 1L;
        
        public DatabaseConnectionFail(String message, Throwable error) {
            super(message, error);
        }
    }
    
    public static class TableCreationFail extends BaseException {
        private static final long serialVersionUID = 2L;

        public TableCreationFail(String message, Throwable error) {
            super(message, error);
        }
    }
}
