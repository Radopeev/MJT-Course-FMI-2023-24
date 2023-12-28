package bg.sofia.uni.fmi.mjt.itinerary;

import bg.sofia.uni.fmi.mjt.itinerary.vehicle.VehicleType;

import java.math.BigDecimal;
import java.util.Objects;

public record Journey(VehicleType vehicleType, City from, City to, BigDecimal price) {

    public Journey {
        if (vehicleType == null) {
            throw new IllegalArgumentException("Vehicle type cannot be null");
        }
        if (from == null) {
            throw new IllegalArgumentException("From cannot be null");
        }
        if (to == null) {
            throw new IllegalArgumentException("To cannot be null");
        }
        if (price == null) {
            throw new IllegalArgumentException("Price cannot be null");
        }
    }

    public static final BigDecimal TAX_PER_KILOMETER = BigDecimal.valueOf(20.0);

    public BigDecimal getPriceWithTaxes() {
        BigDecimal tax = vehicleType.getGreenTax().multiply(price);
        return price.add(tax);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Journey journey)) {
            return false;
        }

        return from.equals(journey.from)
            && to.equals(journey.to)
            && vehicleType.equals(journey.vehicleType)
            && price.equals(journey.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vehicleType, from, to, price);
    }
}
