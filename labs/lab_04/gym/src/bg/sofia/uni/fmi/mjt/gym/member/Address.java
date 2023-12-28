package bg.sofia.uni.fmi.mjt.gym.member;

public record Address(double longitude, double latitude) {
    public double getDistanceTo(Address other) {
        if (other == null) {
            throw new IllegalArgumentException();
        }
        double deltaX = this.longitude - other.longitude;
        double deltaY = this.latitude - other.latitude;
        double diffBetweenLongitudeSquared = Math.pow(deltaX, 2);
        double diffBetweenLatitudeSquared = Math.pow(deltaY, 2);
        return Math.sqrt(diffBetweenLongitudeSquared + diffBetweenLatitudeSquared);
    }
}
