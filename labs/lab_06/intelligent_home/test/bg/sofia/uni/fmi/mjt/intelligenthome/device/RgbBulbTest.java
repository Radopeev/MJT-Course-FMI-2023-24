package bg.sofia.uni.fmi.mjt.intelligenthome.device;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RgbBulbTest {

    private  static RgbBulb rgbBulbTest;
    @BeforeAll
    static void setup() {
        rgbBulbTest = new RgbBulb("test", 1.1, LocalDateTime.now());
    }
    @Test
    void getTypeTest(){
        assertEquals(DeviceType.BULB,rgbBulbTest.getType(),
            "Get type should return the correct type");
    }
    @Test
    void getIdTest(){
        assertEquals(DeviceType.BULB.getShortName() +
                '-' + rgbBulbTest.getName() +
                '-' + IoTDeviceBase.uniqueNumberDevice,
            rgbBulbTest.getId(),
            "The id should be correct");
    }
}
