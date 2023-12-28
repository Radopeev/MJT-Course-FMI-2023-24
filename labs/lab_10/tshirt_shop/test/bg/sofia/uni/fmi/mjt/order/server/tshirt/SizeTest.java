package bg.sofia.uni.fmi.mjt.order.server.tshirt;

import bg.sofia.uni.fmi.mjt.order.server.destination.Destination;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SizeTest {
    @Test
    void testSizeTest() {
        Size size = Size.S;
        String expected = "S";
        String actual = size.getName();

        assertEquals(expected, actual , "Result is different than expected");
    }
}
