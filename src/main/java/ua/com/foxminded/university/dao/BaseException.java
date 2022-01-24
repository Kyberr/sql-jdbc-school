package ua.com.foxminded.university.dao;

public abstract class BaseException extends Exception {
    private static final long serialVersionUID = 1L;

    public BaseException(String message, Throwable error) {
        super(message, error);
    }
}
