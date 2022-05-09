package ua.com.foxminded.sql_jdbc_school.dao;

public class DAOException extends Exception {
    private static final long serialVersionUID = 2L; 
    		
    		public DAOException (String message, Throwable cause) {
    	super(message, cause);
    }
}
