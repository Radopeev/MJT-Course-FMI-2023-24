package bg.sofia.uni.fmi.mjt.order.server.repository;

import bg.sofia.uni.fmi.mjt.order.server.Response;
import bg.sofia.uni.fmi.mjt.order.server.destination.Destination;
import bg.sofia.uni.fmi.mjt.order.server.order.Order;
import bg.sofia.uni.fmi.mjt.order.server.tshirt.Color;
import bg.sofia.uni.fmi.mjt.order.server.tshirt.Size;
import bg.sofia.uni.fmi.mjt.order.server.tshirt.TShirt;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class MJTOrderRepository implements OrderRepository {

    private final Collection<Order> orders;
    private int id = 1;

    public MJTOrderRepository() {
        orders = new LinkedList<>();
    }

    private void assertSizeColorDestination(String size, String color, String destination) {
        if (size == null || color == null || destination == null) {
            throw new IllegalArgumentException("Size, color and destination cannot be null");
        }
    }

    private Size assignSize(String size, AtomicBoolean sizeFlag) {
        Size sizeEnum = Size.UNKNOWN;
        try {
            sizeEnum = Size.valueOf(size);
        } catch (IllegalArgumentException e) {
            sizeFlag.set(false);
        }
        return sizeEnum;
    }

    private Color assignColor(String color, AtomicBoolean colorFlag) {
        Color colorEnum = Color.UNKNOWN;
        try {
            colorEnum = Color.valueOf(color);
        } catch (IllegalArgumentException e) {
            colorFlag.set(false);
        }
        return colorEnum;
    }

    private Destination assignDestination(String destination, AtomicBoolean destFlag) {
        Destination destEnum = Destination.UNKNOWN;
        try {
            destEnum = Destination.valueOf(destination);
        } catch (IllegalArgumentException e) {
            destFlag.set(false);
        }
        return destEnum;
    }

    private Response checkAllDeclinedCases(AtomicBoolean sizeFlag, AtomicBoolean colorFlag, AtomicBoolean destFlag) {
        if (!sizeFlag.get() && !colorFlag.get() && !destFlag.get()) {
            return Response.decline("invalid=size,color,destination");
        }
        if (!sizeFlag.get() && !colorFlag.get()) {
            return Response.decline("invalid=size,color");
        }
        if (!sizeFlag.get() && !destFlag.get()) {
            return Response.decline("invalid=size,destination");
        }
        if (!sizeFlag.get()) {
            return Response.decline("invalid=size");
        }
        if (!colorFlag.get() && !destFlag.get()) {
            return Response.decline("invalid=color,destination");
        }
        if (!colorFlag.get()) {
            return Response.decline("invalid=color");
        }
        if (!destFlag.get()) {
            return Response.decline("invalid=destination");
        }
        return null;
    }

    @Override
    public Response request(String size, String color, String destination) {
        assertSizeColorDestination(size, color, destination);

        AtomicBoolean sizeFlag = new AtomicBoolean(true);
        AtomicBoolean colorFlag = new AtomicBoolean(true);
        AtomicBoolean destFlag = new AtomicBoolean(true);

        Size sizeEnum = assignSize(size, sizeFlag);
        Color colorEnum = assignColor(color, colorFlag);
        Destination destEnum = assignDestination(destination, destFlag);

        TShirt tShirt = new TShirt(sizeEnum, colorEnum);
        Order declinedOrder = new Order(-1, tShirt, destEnum);
        Order createdOrder = new Order(id, tShirt, destEnum);
        Response tempResponse = checkAllDeclinedCases(sizeFlag, colorFlag, destFlag);
        if (!sizeFlag.get() || !colorFlag.get() || !destFlag.get()) {
            orders.add(declinedOrder);
            return tempResponse;
        }
        orders.add(createdOrder);
        return Response.create(id++);
    }

    @Override
    public Response getOrderById(int id) {
        for (Order o : orders) {
            if (o.id() == id) {
                return Response.ok(List.of(o));
            }
        }
        return Response.notFound(id);
    }

    @Override
    public Response getAllOrders() {
        return Response.ok(orders);
    }

    @Override
    public Response getAllSuccessfulOrders() {
        List<Order> successfulOrder = orders.stream().filter(o -> o.id() != -1).toList();
        return Response.ok(successfulOrder);
    }
}
