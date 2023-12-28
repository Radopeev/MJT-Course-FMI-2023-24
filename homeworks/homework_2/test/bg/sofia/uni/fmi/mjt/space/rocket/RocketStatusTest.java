package bg.sofia.uni.fmi.mjt.space.rocket;

import bg.sofia.uni.fmi.mjt.space.mission.MissionStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RocketStatusTest {
    @Test
    void testToString(){
        RocketStatus test = RocketStatus.STATUS_ACTIVE;
        String expected = "StatusActive";
        assertEquals(expected, test.toString(),
            "Result is different than expected");
    }

    @Test
    void testFromStringWhenStringIsInvalid() {
        assertThrows(IllegalArgumentException.class, () -> RocketStatus.fromString("alabala"),
            "Should throw an exception when text is invalid");
    }
}
