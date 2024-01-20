package bg.sofia.uni.fmi.mjt.cookingcompass.exceptions;

public class LimitExceededException extends Exception {
    public LimitExceededException(String message) {
        super(message);
    }

    public LimitExceededException(String message, Throwable cause) {
        super(message, cause);
    }
}

