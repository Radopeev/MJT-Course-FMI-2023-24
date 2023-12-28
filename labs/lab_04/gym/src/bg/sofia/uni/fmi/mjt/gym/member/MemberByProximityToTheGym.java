package bg.sofia.uni.fmi.mjt.gym.member;

import java.util.Comparator;

public class MemberByProximityToTheGym implements Comparator<GymMember> {
    private final Address gymAddress;

    public MemberByProximityToTheGym(Address address) {
        this.gymAddress = address;
    }

    @Override
    public int compare(GymMember m1, GymMember m2) {
        Double distanceFromMemberOne = m1.getAddress().getDistanceTo(gymAddress);
        Double distanceFromMemberTwo = m2.getAddress().getDistanceTo(gymAddress);
        return distanceFromMemberOne.compareTo(distanceFromMemberTwo);
    }
}
