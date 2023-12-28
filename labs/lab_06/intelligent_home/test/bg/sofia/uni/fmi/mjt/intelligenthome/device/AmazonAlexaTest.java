package bg.sofia.uni.fmi.mjt.intelligenthome.device;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.LocalDateTime;

import static java.time.Duration.between;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.Duration;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AmazonAlexaTest {

    private static AmazonAlexa amazonAlexaTest;
    private LocalDateTime mockedRegistrationTime;
    private LocalDateTime mockedCurrentTime;
    @BeforeAll
    static void setup() {
        amazonAlexaTest = new AmazonAlexa("test", 1.1, LocalDateTime.now());
    }
    @Test
    void getTypeTest(){
        assertEquals(DeviceType.SMART_SPEAKER,amazonAlexaTest.getType(),
            "Get type should return the correct type");
    }
    @Test
    void getIdTest(){
        assertEquals(DeviceType.SMART_SPEAKER.getShortName() +
                '-' + amazonAlexaTest.getName() +
                '-' + IoTDeviceBase.uniqueNumberDevice,
            amazonAlexaTest.getId(),
            "The id should be correct");
    }
    @Test
    void getNameTest(){
        assertEquals(amazonAlexaTest.name,amazonAlexaTest.getName(),
            "Get name should return the correct name");
    }
    @Test
    void getPowerConsumption(){
        assertEquals(amazonAlexaTest.powerConsumption,amazonAlexaTest.getPowerConsumption(),
            "Get power consumption should return the correct consumption");
    }
    @Test
    void getInstallationDateTimeTest(){
        assertEquals(amazonAlexaTest.installationDateTime,amazonAlexaTest.getInstallationDateTime(),
            "Get installation date time should return the correct ti");
    }
    @Test
    void setRegistrationTest(){
        LocalDateTime dateTimeTemp=LocalDateTime.MAX;
        amazonAlexaTest.setRegistration(dateTimeTemp);

        assertEquals(LocalDateTime.MAX, amazonAlexaTest.registration,"" +
            "Set registraion should work correctly");
    }
    @Test
    void getRegistrationTest(){
        amazonAlexaTest.setRegistration(LocalDateTime.MIN);
        long expectedHours = Duration.between(amazonAlexaTest.registration, LocalDateTime.now()).toHours();

        assertEquals(expectedHours,
            amazonAlexaTest.getRegistration(),
            "Get registration should return the correct time");
    }
    /*
    @Test
    void getPowerConsumptionKwhTest(){
        amazonAlexaTest.setRegistration(LocalDateTime.now().minusHours(5));
        long duration = Duration.between(amazonAlexaTest.registration, LocalDateTime.now()).toHours();
        long expectedResult =(long)(amazonAlexaTest.powerConsumption*duration);

        assertEquals(expectedResult,
            amazonAlexaTest.getPowerConsumptionKWh(),
            "Get power consumption kwh should work correctly");
    }*/
}
