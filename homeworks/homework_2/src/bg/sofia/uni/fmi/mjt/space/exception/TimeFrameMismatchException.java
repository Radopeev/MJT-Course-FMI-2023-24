package bg.sofia.uni.fmi.mjt.space.exception;

public class TimeFrameMismatchException extends RuntimeException {
    public TimeFrameMismatchException(String message) {
        super(message);
    }

    public TimeFrameMismatchException(String message, Throwable cause) {
        super(message, cause);
    }

    public TimeFrameMismatchException(Throwable cause) {
        super(cause);
    }
}