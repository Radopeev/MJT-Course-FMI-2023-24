package bg.sofia.uni.fmi.mjt.order.server.destination;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DestinationTest {
    @Test
    void testDestinationTest() {
        Destination dest = Destination.EUROPE;
        String expected = "EUROPE";
        String actual = dest.getName();

        assertEquals(expected, actual , "Result is different than expected");
    }
}
