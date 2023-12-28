package bg.sofia.uni.fmi.mjt.udemy.exception;

public class CourseNotPurchasedException extends Throwable {
    public CourseNotPurchasedException(String message) {
        super(message);
    }

    public CourseNotPurchasedException(String message, Throwable cause) {
        super(message, cause);
    }
}
