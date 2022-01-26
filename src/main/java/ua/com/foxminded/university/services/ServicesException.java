package ua.com.foxminded.university.services;

public class ServicesException {
    
    public static class ReadFail extends Exception {
        private static final long serialVersionUID = 1L; 
        
        public ReadFail(String message, Throwable error) {
            super(message, error);
        }
    }
    
    public static class TableCreationFail extends Exception {
        private static final long serialVersionUID = 2L;
        
        public TableCreationFail(String message, Throwable error) {
            super(message, error);
        }
    }
}
