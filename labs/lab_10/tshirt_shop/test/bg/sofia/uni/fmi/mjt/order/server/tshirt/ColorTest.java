package bg.sofia.uni.fmi.mjt.order.server.tshirt;

import bg.sofia.uni.fmi.mjt.order.server.destination.Destination;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ColorTest {
    @Test
    void testColorTest() {
        Color color = Color.WHITE;
        String expected = "WHITE";
        String actual = color.getName();

        assertEquals(expected, actual , "Result is different than expected");
    }
}
