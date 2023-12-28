package bg.sofia.uni.fmi.mjt.itinerary;

import java.math.BigDecimal;
import java.util.Objects;

public record Location(int x, int y) {

    private static final double ONE_THOUSAND = 1000.0;

    public static BigDecimal getManhattanDistanceBetweenTwoCities(Location l1, Location l2) {
        double deltaX = Math.abs(l1.x - l2.x);
        double deltaY = Math.abs(l1.y - l2.y);
        return BigDecimal.valueOf((deltaX + deltaY) / ONE_THOUSAND);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Location location)) {
            return false;
        }

        return x == location.x && y == location.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
