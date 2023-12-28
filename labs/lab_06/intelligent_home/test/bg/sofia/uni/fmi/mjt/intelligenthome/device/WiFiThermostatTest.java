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
public class WiFiThermostatTest {

    private  static WiFiThermostat wiFiThermostatTest;
    @BeforeAll
    static void setup() {
        wiFiThermostatTest = new WiFiThermostat("test", 1.1, LocalDateTime.now());
    }
    @Test
    void getTypeTest(){
        assertEquals(DeviceType.THERMOSTAT,wiFiThermostatTest.getType(),
            "Get type should return the correct type");
    }
    @Test
    void getIdTest(){
        assertEquals(DeviceType.THERMOSTAT.getShortName() +
                '-' + wiFiThermostatTest.getName() +
                '-' + IoTDeviceBase.uniqueNumberDevice,
            wiFiThermostatTest.getId(),
            "The id should be correct");
    }
}
