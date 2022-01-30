package ua.com.foxminded.sql_jdbc_school;

public abstract class BaseException extends Exception {
    private static final long serialVersionUID = 1L;

    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseException(Throwable cause) {
        super(cause);
    }
}
