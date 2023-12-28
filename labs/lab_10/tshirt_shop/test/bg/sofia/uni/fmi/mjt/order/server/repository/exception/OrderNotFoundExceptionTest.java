package bg.sofia.uni.fmi.mjt.order.server.repository.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class OrderNotFoundExceptionTest {
    @Test
    public void testMessageConstructor() {
        String errorMessage = "Order not found!";
        OrderNotFoundException exception = new OrderNotFoundException(errorMessage);
        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    public void testMessageAndCauseConstructor() {
        String errorMessage = "Order not found!";
        Throwable cause = new RuntimeException("Root cause");
        OrderNotFoundException exception = new OrderNotFoundException(errorMessage, cause);

        assertEquals(errorMessage, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
}
