package bg.sofia.uni.fmi.mjt.intelligenthome.center;

import bg.sofia.uni.fmi.mjt.intelligenthome.center.exceptions.DeviceAlreadyRegisteredException;
import bg.sofia.uni.fmi.mjt.intelligenthome.center.exceptions.DeviceNotFoundException;
import bg.sofia.uni.fmi.mjt.intelligenthome.device.AmazonAlexa;
import bg.sofia.uni.fmi.mjt.intelligenthome.device.DeviceType;
import bg.sofia.uni.fmi.mjt.intelligenthome.device.IoTDevice;
import bg.sofia.uni.fmi.mjt.intelligenthome.device.RgbBulb;
import bg.sofia.uni.fmi.mjt.intelligenthome.storage.DeviceStorage;
import bg.sofia.uni.fmi.mjt.intelligenthome.storage.MapDeviceStorage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.isNull;

@ExtendWith(MockitoExtension.class)
public class IntelligentHomeCenterTest {

    private static IntelligentHomeCenter intelligentHomeCenterTest;
    private static DeviceStorage storageTest;
    private static IoTDevice bulbTest;
    @BeforeAll
    static void setUp(){
        storageTest=new MapDeviceStorage();
        intelligentHomeCenterTest=new IntelligentHomeCenter(storageTest);
        bulbTest=new RgbBulb("test",1.1,LocalDateTime.now().minusHours(5));
    }

    @Test
    void registerTest() throws DeviceAlreadyRegisteredException {
        intelligentHomeCenterTest.register(bulbTest);

        assertTrue(storageTest.exists(bulbTest.getId()));
    }
    @Test
    void registerThrowsNullTest() {
        assertThrows(IllegalArgumentException.class,()->intelligentHomeCenterTest.register(null));
    }
    @Test
    void registerThrowDeviceAlreadyRegisterTest() throws DeviceAlreadyRegisteredException {
        intelligentHomeCenterTest.register(bulbTest);

        assertThrows(DeviceAlreadyRegisteredException.class,
            ()->intelligentHomeCenterTest.register(bulbTest));
    }
    @Test
    void unregisterTest() throws DeviceNotFoundException {
        intelligentHomeCenterTest.unregister(bulbTest);

        assertFalse(storageTest.exists(bulbTest.getId()));
    }
    @Test
    void unregisterThrowsNullTest() {
        assertThrows(IllegalArgumentException.class,()->intelligentHomeCenterTest.unregister(null));
    }
    @Test
    void unregisterThrowDeviceAlreadyRegisterTest() throws DeviceNotFoundException {
        assertThrows(DeviceNotFoundException.class,
            ()->intelligentHomeCenterTest.unregister(bulbTest));
    }
    @Test
    void getDeviceByIdThrowsNullTest(){
        assertThrows(IllegalArgumentException.class,()->intelligentHomeCenterTest.getDeviceById(null));
    }
    @Test
    void  getDeviceByIdThrowsBlankTest(){
        assertThrows(IllegalArgumentException.class,()->intelligentHomeCenterTest.getDeviceById(""));
    }
    @Test
    void getDeviceByIdThrowsDeviceNotFoundTest(){
        assertThrows(DeviceNotFoundException.class,
            ()->intelligentHomeCenterTest.getDeviceById("alabal"));
    }
    @Test
    void getDeviceByTest() throws DeviceNotFoundException {
        storageTest.store(bulbTest.getId(),bulbTest);
        assertEquals(bulbTest,intelligentHomeCenterTest.getDeviceById(bulbTest.getId()),
            "Get device by ID should return the right device");
    }
    @Test
    void getDeviceQuantityPerTypeTest(){
        storageTest.store(bulbTest.getId(),bulbTest);
        assertEquals(1,intelligentHomeCenterTest.getDeviceQuantityPerType(DeviceType.BULB),
            "Get device quantity per type should work correctly");
    }
    @Test
    void getDeviceQuantityPerTypeThrowsNullTest(){
        assertThrows(IllegalArgumentException.class,()->intelligentHomeCenterTest.getDeviceQuantityPerType(null),
            "Get device quantity per type should throw when type is null");
    }
    @Test
    public void testGetFirstNDevicesByRegistration() throws DeviceAlreadyRegisteredException {
        DeviceStorage storage = new MapDeviceStorage();
        LocalDateTime now = LocalDateTime.now();
        IoTDevice device1 = new AmazonAlexa("Device1", 10.5, now.minusHours(3));
        IoTDevice device2 = new AmazonAlexa("Device2", 8.2, now.minusHours(5));
        IoTDevice device3 = new AmazonAlexa("Device3", 15.0, now.minusHours(1));

        IntelligentHomeCenter homeCenter = new IntelligentHomeCenter(storage);

        homeCenter.register(device1);
        homeCenter.register(device2);
        homeCenter.register(device3);

        Collection<IoTDevice> firstTwoDevices = homeCenter.getFirstNDevicesByRegistration(2);
        assertEquals(2, firstTwoDevices.size());
        assertTrue(firstTwoDevices.contains(device3));
        assertTrue(firstTwoDevices.contains(device1));

        Collection<IoTDevice> allDevices = homeCenter.getFirstNDevicesByRegistration(5);
        assertEquals(3, allDevices.size());
        assertTrue(allDevices.contains(device3));
        assertTrue(allDevices.contains(device1));
        assertTrue(allDevices.contains(device2));

        Collection<IoTDevice> emptyList = homeCenter.getFirstNDevicesByRegistration(0);
        assertEquals(0, emptyList.size());

        assertThrows(IllegalArgumentException.class, () -> homeCenter.getFirstNDevicesByRegistration(-1));
    }
    @Test
    public void testGetTopNDevicesByPowerConsumption() throws DeviceAlreadyRegisteredException {
        DeviceStorage storage = new MapDeviceStorage();
        LocalDateTime now = LocalDateTime.now();
        IoTDevice device1 = new AmazonAlexa("Device1", 10.5, now.minusHours(3));
        IoTDevice device2 = new AmazonAlexa("Device2", 8.2, now.minusHours(5));
        IoTDevice device3 = new AmazonAlexa("Device3", 15.0, now.minusHours(1));

        IntelligentHomeCenter homeCenter = new IntelligentHomeCenter(storage);

        homeCenter.register(device1);
        homeCenter.register(device2);
        homeCenter.register(device3);

        Collection<String> topTwoDevices = homeCenter.getTopNDevicesByPowerConsumption(2);
        assertEquals(2, topTwoDevices.size());
        assertTrue(topTwoDevices.contains(device3.getId()));
        assertTrue(topTwoDevices.contains(device1.getId()));

        Collection<String> allDevices = homeCenter.getTopNDevicesByPowerConsumption(5);
        assertEquals(3, allDevices.size());
        assertTrue(allDevices.contains(device3.getId()));
        assertTrue(allDevices.contains(device1.getId()));
        assertTrue(allDevices.contains(device2.getId()));

        Collection<String> emptyList = homeCenter.getTopNDevicesByPowerConsumption(0);
        assertEquals(0, emptyList.size());

        assertThrows(IllegalArgumentException.class, () -> homeCenter.getTopNDevicesByPowerConsumption(-1));
    }
}
