package bg.sofia.uni.fmi.mjt.cookingcompass.exceptions;

public class ForbiddenErrorException extends Exception {
    public ForbiddenErrorException(String message) {
        super(message);
    }

    public ForbiddenErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}
