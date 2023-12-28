package bg.sofia.uni.fmi.mjt.itinerary.comparator;

import bg.sofia.uni.fmi.mjt.itinerary.Journey;

import java.util.Comparator;

public class JourneyComparator implements Comparator<Journey> {
    @Override
    public int compare(Journey o1, Journey o2) {
        return o1.from().name().compareTo(o2.from().name());
    }
}
