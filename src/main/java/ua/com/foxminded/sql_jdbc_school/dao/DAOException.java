package ua.com.foxminded.sql_jdbc_school.dao;

public class DAOException {
    
    public static class GetAllGroupsFail extends Exception {
        private static final long serialVersionUID = 9;

        public GetAllGroupsFail(String message, Throwable cause) {
            super(message, cause);
        }
    }
    
    public static class GetAllSutudentsFail extends Exception {
        public static final long serialVersionUID = 8L;

        public GetAllSutudentsFail(String message, Throwable cause) {
            super(message, cause);
        }
    }
    
    public static class DatabaseConnectionFail extends Exception {
        private static final long serialVersionUID = 1L;
        
        public DatabaseConnectionFail(String message, Throwable cause) {
            super(message, cause);
        }
    }
    
    public static class TableCreationFail extends Exception {
        private static final long serialVersionUID = 2L;

        public TableCreationFail(String message, Throwable cause) {
            super(message, cause);
        }
    }
    
    public static class PropertyFileLoadingFail extends Exception {
        private static final long serialVersionUID = 3L;
        
        public PropertyFileLoadingFail (String message, Throwable cause) {
            super(message, cause);
        }
        
        public PropertyFileLoadingFail (Throwable cause) {
            super(cause);
        }
    }
    
    public static class StudentInsertionFail extends Exception {
        private static final long serialVersionUID = 4L;
        
        
        public StudentInsertionFail(Throwable cause) {
            super(cause);
        }

        public StudentInsertionFail(String message, Throwable cause) {
            super(message, cause);
        }
    }
    
    public static class GroupInsertionFail extends Exception {
        private static final long serialVersionUID = 5L;
        
        public GroupInsertionFail(Throwable cause) {
            super(cause);
        }

        public GroupInsertionFail(String message, Throwable cause) {
            super(message, cause);
        }
    }
    
    public static class CourseInsertionFail extends Exception {
        private static final long serialVersionUID = 6L;

        public CourseInsertionFail(Throwable cause) {
            super(cause);
        }

        public CourseInsertionFail(String message, Throwable cause) {
            super(message, cause);
        }
    }
    
    public static class StudentUptatingFail extends Exception {
        private static final long serialVersionUID = 7L;

        public StudentUptatingFail(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
