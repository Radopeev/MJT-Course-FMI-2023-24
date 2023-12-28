package bg.sofia.uni.fmi.mjt.space.mission;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MissionStatusTest {

    @Test
    void testToString(){
        MissionStatus test = MissionStatus.SUCCESS;
        String expected = "Success";
        assertEquals(expected, test.toString(),
            "Result is different than expected");
    }

    @Test
    void testFromStringWhenStringIsInvalid() {
        assertThrows(IllegalArgumentException.class, () -> MissionStatus.fromString("alabala"),
            "Should throw an exception when text is invalid");
    }
}
