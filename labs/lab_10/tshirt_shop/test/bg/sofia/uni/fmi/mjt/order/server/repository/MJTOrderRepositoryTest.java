package bg.sofia.uni.fmi.mjt.order.server.repository;

import bg.sofia.uni.fmi.mjt.order.server.Response;
import bg.sofia.uni.fmi.mjt.order.server.destination.Destination;
import bg.sofia.uni.fmi.mjt.order.server.order.Order;
import bg.sofia.uni.fmi.mjt.order.server.tshirt.Color;
import bg.sofia.uni.fmi.mjt.order.server.tshirt.Size;
import bg.sofia.uni.fmi.mjt.order.server.tshirt.TShirt;
import bg.sofia.uni.fmi.mjt.order.server.tshirt.TShirtTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MJTOrderRepositoryTest {
    private MJTOrderRepository repository;

    @BeforeEach
    void setup() {
        repository = new MJTOrderRepository();
        repository.request(String.valueOf(Size.S), String.valueOf(Color.WHITE), String.valueOf(Destination.EUROPE));
        repository.request(String.valueOf(Size.M), String.valueOf(Color.BLACK), String.valueOf(Destination.AUSTRALIA));
        repository.request(String.valueOf(Size.XL), String.valueOf(Color.RED),
            String.valueOf(Destination.NORTH_AMERICA));
        repository.request(String.valueOf(Size.L), String.valueOf(Color.WHITE),
            String.valueOf(Destination.NORTH_AMERICA));
        repository.request(String.valueOf(Size.M), String.valueOf(Color.BLACK), String.valueOf(Destination.EUROPE));
        repository.request(String.valueOf(Size.S), String.valueOf(Color.BLACK), String.valueOf(Destination.EUROPE));
    }

    @Test
    void testRequest() {
        Response expected = Response.create(7);
        Response actual = repository.request("L", "BLACK", "EUROPE");

        assertEquals(expected, actual, "Result is different than expected");
    }

    @Test
    void testRequestThrowsWhenSizeIsNull() {
        assertThrows(IllegalArgumentException.class, () -> repository.request(null, "", ""),
            "Should throw an exception when size is null");
    }

    @Test
    void testRequestThrowsWhenColorIsNull() {
        assertThrows(IllegalArgumentException.class, () -> repository.request("", null, ""),
            "Should throw an exception when color is null");
    }

    @Test
    void testRequestThrowsWhenDestinationIsNull() {
        assertThrows(IllegalArgumentException.class, () -> repository.request("", "", null),
            "Should throw an exception when destination is null");
    }

    @Test
    void testRequestWhenAllAreRandom() {
        Response expected = Response.decline("invalid=size,color,destination");
        Response actual = repository.request("", "", "");

        assertEquals(expected, actual, "Result is different than expected");
    }

    @Test
    void testRequestWhenSizeAndColorAreRandom() {
        Response expected = Response.decline("invalid=size,color");
        Response actual = repository.request("", "", String.valueOf(Destination.EUROPE));

        assertEquals(expected, actual, "Result is different than expected");
    }

    @Test
    void testRequestWhenSizeAndDestinationAreRandom() {
        Response expected = Response.decline("invalid=size,destination");
        Response actual = repository.request("", String.valueOf(Color.RED), "");

        assertEquals(expected, actual, "Result is different than expected");
    }

    @Test
    void testRequestWhenSizeIsRandom() {
        Response expected = Response.decline("invalid=size");
        Response actual = repository.request("", String.valueOf(Color.RED), String.valueOf(Destination.AUSTRALIA));

        assertEquals(expected, actual, "Result is different than expected");
    }

    @Test
    void testRequestWhenColorAndDestinationAreRandom() {
        Response expected = Response.decline("invalid=color,destination");
        Response actual = repository.request(String.valueOf(Size.M), "", "");

        assertEquals(expected, actual, "Result is different than expected");
    }

    @Test
    void testRequestWhenColorIsRandom() {
        Response expected = Response.decline("invalid=color");
        Response actual = repository.request(String.valueOf(Size.M), "", String.valueOf(Destination.NORTH_AMERICA));

        assertEquals(expected, actual, "Result is different than expected");
    }

    @Test
    void testRequestWhenDestinationIsRandom() {
        Response expected = Response.decline("invalid=destination");
        Response actual = repository.request(String.valueOf(Size.M), String.valueOf(Color.BLACK), "");

        assertEquals(expected, actual, "Result is different than expected");
    }

    @Test
    void testRequestSuccessful() {
        Response expected = Response.create(7);
        Response actual =
            repository.request(String.valueOf(Size.S), String.valueOf(Color.WHITE), String.valueOf(Destination.EUROPE));

        assertEquals(expected, actual, "Result is different than expected");
    }

    @Test
    void testGetOrderByIdWhenFound() {
        TShirt tShirtTest = new TShirt(Size.XL, Color.RED);
        Order orderTest = new Order(3, tShirtTest, Destination.NORTH_AMERICA);
        Response expected = Response.ok(List.of(orderTest));
        Response actual = repository.getOrderById(3);

        assertEquals(expected, actual, "Result is different than expected");
    }

    @Test
    void testGetOrderByIdWhenNotFound() {
        Response expected = Response.notFound(1000);
        Response actual = repository.getOrderById(1000);

        assertEquals(expected, actual, "Result is different than expected");
    }

    @Test
    void testGetAllOrders() {
        TShirt ts1 = new TShirt(Size.S, Color.WHITE), ts2 = new TShirt(Size.M, Color.BLACK), ts3 =
            new TShirt(Size.XL, Color.RED), ts4 = new TShirt(Size.L, Color.WHITE), ts5 =
            new TShirt(Size.M, Color.BLACK), ts6 = new TShirt(Size.S, Color.BLACK);
        Collection<Order> orders =
            List.of(new Order(1, ts1, Destination.EUROPE), new Order(2, ts2, Destination.AUSTRALIA),
                new Order(3, ts3, Destination.NORTH_AMERICA), new Order(4, ts4, Destination.NORTH_AMERICA),
                new Order(5, ts5, Destination.EUROPE), new Order(6, ts6, Destination.EUROPE));
        Response expected = Response.ok(orders);
        Response actual = repository.getAllOrders();

        assertEquals(expected, actual, "Result is i");
    }

    @Test
    void testGetAllSuccessfulOrders() {
        TShirt ts1 = new TShirt(Size.S, Color.WHITE), ts2 = new TShirt(Size.M, Color.BLACK), ts3 = new TShirt(Size.XL, Color.RED),
            ts4 = new TShirt(Size.L, Color.WHITE), ts5 = new TShirt(Size.M,Color.BLACK), ts6 = new TShirt(Size.S,Color.BLACK);
        Collection<Order> orders = List.of(
            new Order(1, ts1, Destination.EUROPE),new Order(2, ts2, Destination.AUSTRALIA), new Order(3,ts3,Destination.NORTH_AMERICA),
            new Order(4, ts4, Destination.NORTH_AMERICA), new Order(5, ts5, Destination.EUROPE), new Order(6, ts6, Destination.EUROPE)
        );
        Response expected = Response.ok(orders);
        Response actual = repository.getAllSuccessfulOrders();

        assertEquals(expected, actual, "Result is different than expected");
    }
}
