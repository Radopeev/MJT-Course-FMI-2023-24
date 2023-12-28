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
public class KWhComparatorTest {

    @Test
    void compareTest() {
        IoTDevice mockOne =new RgbBulb("testOne",1.1, LocalDateTime.now().minusHours(5));
        IoTDevice mockTwo = new AmazonAlexa("testTwo",2.2,LocalDateTime.now());
        KWhComparator comparatorMock = new KWhComparator();

        assertEquals(-5, comparatorMock.compare(mockOne,mockTwo),
            "Comparator should work correctly");
    }

}
