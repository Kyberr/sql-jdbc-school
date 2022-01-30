package ua.com.foxminded.sql_jdbc_school.services;

import ua.com.foxminded.sql_jdbc_school.BaseException;

public class ServicesException {
    
    public static class ReadFail extends BaseException {
        private static final long serialVersionUID = 1L; 
        
        public ReadFail(String message, Throwable cause) {
            super(message, cause);
        }
    }
    
    public static class TableCreationFail extends BaseException {
        private static final long serialVersionUID = 2L;
        
        public TableCreationFail(String message, Throwable cause) {
            super(message, cause);
        }
    }
    
    public static class StudentCreationFail extends BaseException {
        private static final long serialVersionUID = 3L;
        
        public StudentCreationFail(String message, Throwable cause) {
            super(message, cause);
        }
    }
    
    public static class GroupCreationFail extends BaseException {
        private static final long serialVersionUID = 4L;

        public GroupCreationFail(String message, Throwable cause) {
            super(message, cause);
        }

        public GroupCreationFail(Throwable cause) {
            super(cause);
        }
    }
}
