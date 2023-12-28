package bg.sofia.uni.fmi.mjt.udemy.exception;

import java.rmi.server.ExportException;

public class CourseAlreadyPurchasedException extends Exception {
    public CourseAlreadyPurchasedException(String message) {
        super(message);
    }

    public CourseAlreadyPurchasedException(String message, Throwable cause) {
        super(message, cause);
    }
}
