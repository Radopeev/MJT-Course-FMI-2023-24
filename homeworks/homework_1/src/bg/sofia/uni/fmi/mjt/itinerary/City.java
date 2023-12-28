package bg.sofia.uni.fmi.mjt.itinerary;

import java.util.Objects;

public record City(String name, Location location) {

    public City {
        if (name.isBlank() || name.isEmpty() || name == null) {
            throw new IllegalArgumentException("Name of city cannot be null, blank or empty!");
        }
        if (location == null) {
            throw new IllegalArgumentException("Location cannot be null");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof City city)) {
            return false;
        }

        return Objects.equals(name, city.name) &&
            Objects.equals(location, city.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, location);
    }
}
