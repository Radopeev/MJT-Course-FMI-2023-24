package bg.sofia.uni.fmi.mjt.itinerary.exception;

public class CityNotKnownException extends RuntimeException {
    public CityNotKnownException(String message) {
        super(message);
    }

    public CityNotKnownException(String message, Throwable cause) {
        super(message, cause);
    }
}
