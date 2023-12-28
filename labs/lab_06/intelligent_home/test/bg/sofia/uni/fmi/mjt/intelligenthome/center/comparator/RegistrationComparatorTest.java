package bg.sofia.uni.fmi.mjt.intelligenthome.center.comparator;

import bg.sofia.uni.fmi.mjt.intelligenthome.center.comparator.KWhComparator;
import bg.sofia.uni.fmi.mjt.intelligenthome.device.AmazonAlexa;
import bg.sofia.uni.fmi.mjt.intelligenthome.device.IoTDevice;
import bg.sofia.uni.fmi.mjt.intelligenthome.device.RgbBulb;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RegistrationComparatorTest {

    @Test
    void compareTest() {
        IoTDevice mockOne =new RgbBulb("testOne",1.1, LocalDateTime.now());
        IoTDevice mockTwo = new AmazonAlexa("testTwo",2.2,LocalDateTime.now());
        RegistrationComparator comparatorMock = new RegistrationComparator();

        mockOne.setRegistration(LocalDateTime.now());
        mockTwo.setRegistration(LocalDateTime.now().plusHours(5));

        assertEquals(-5, comparatorMock.compare(mockOne,mockTwo),
            "Comparator should work correctly");
    }

}
