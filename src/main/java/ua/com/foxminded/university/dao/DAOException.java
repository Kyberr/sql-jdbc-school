package ua.com.foxminded.university.dao;

public class DAOException {
    
    public static class DatabaseConnectionFail extends Exception {
        private static final long serialVersionUID = 1L;
        
        public DatabaseConnectionFail(String message, Throwable error) {
            super(message, error);
        }
    }
    
    public static class TableCreationFail extends Exception {
        private static final long serialVersionUID = 2L;

        public TableCreationFail(String message, Throwable error) {
            super(message, error);
        }
    }
    
    public static class PropertyFileLoadFail extends Exception {
        private static final long serialVersionUID = 3L;
        
        public PropertyFileLoadFail (String message, Throwable error) {
            super(message, error);
        }
        
        public PropertyFileLoadFail (Throwable error) {
            super(error);
        }
    }
    
    public static class StudentInsertionFail extends Exception {
        private static final long serialVersionUID = 4L;
        
        
        public StudentInsertionFail(Throwable error) {
            super(error);
        }
    }
}
