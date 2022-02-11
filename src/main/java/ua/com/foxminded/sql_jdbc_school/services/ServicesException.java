package ua.com.foxminded.sql_jdbc_school.services;


public class ServicesException {
    
    
    public static class FindGroupsWithLessOrEqualStudentsFailure extends Exception {
        private static final long serialVersionUID = 11L;

        public FindGroupsWithLessOrEqualStudentsFailure(String message, Throwable cause) {
            super(message, cause);
        }
    }
    
    public static class StudentsCoursesRelationFailure extends Exception {
        private static final long serialVersionUID = 10L;

        public StudentsCoursesRelationFailure(String message, Throwable cause) {
            super(message, cause);
        }
    }
    
    public static class LoadUniversityMenuFail extends Exception {
        private static final long serialVersionUID = 9L;

        public LoadUniversityMenuFail(String message, Throwable cause) {
            super(message, cause);
        }
    }
    
    public static class BootstrapFail extends Exception {
        private static final long serialVersionUID = 8L;

        public BootstrapFail(String message, Throwable cause) {
            super(message, cause);
        }
    }
    
    public static class AssignGgoupToStudentsFail extends Exception {
        private static final long serialVersionUID = 7L;

        public AssignGgoupToStudentsFail(String message, Throwable cause) {
            super(message, cause);
        }
    }
    
    public static class ReadFail extends Exception {
        private static final long serialVersionUID = 1L; 
        
        public ReadFail(String message, Throwable cause) {
            super(message, cause);
        }
    }
    
    public static class TableCreationFail extends Exception {
        private static final long serialVersionUID = 2L;
        
        public TableCreationFail(String message, Throwable cause) {
            super(message, cause);
        }
    }
    
    public static class StudentCreationFail extends Exception {
        private static final long serialVersionUID = 3L;
        
        public StudentCreationFail(String message, Throwable cause) {
            super(message, cause);
        }
    }
    
    public static class GroupCreationFail extends Exception {
        private static final long serialVersionUID = 4L;

        public GroupCreationFail(String message, Throwable cause) {
            super(message, cause);
        }

        public GroupCreationFail(Throwable cause) {
            super(cause);
        }
    }
    
    public static class PropertyFileLoadingFail extends Exception {
        private static final long serialVersionUID = 5L;

        public PropertyFileLoadingFail(String message, Throwable cause) {
            super(message, cause);
        }

        public PropertyFileLoadingFail(Throwable cause) {
            super(cause);
        }
    }
    
    public static class CoursesCreationServiceFail extends Exception {
        private static final long serialVersionUID = 6L;

        public CoursesCreationServiceFail(String message, Throwable cause) {
            super(message, cause);
        }

        public CoursesCreationServiceFail(Throwable cause) {
            super(cause);
        }
    }
}
