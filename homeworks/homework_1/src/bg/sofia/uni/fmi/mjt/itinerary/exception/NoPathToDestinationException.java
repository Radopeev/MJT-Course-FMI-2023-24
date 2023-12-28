package bg.sofia.uni.fmi.mjt.itinerary.exception;

public class NoPathToDestinationException extends RuntimeException {
    public NoPathToDestinationException(String message) {
        super(message);
    }

    public NoPathToDestinationException(String message, Throwable cause) {
        super(message, cause);
    }
}
