package bg.sofia.uni.fmi.mjt.gym;

import bg.sofia.uni.fmi.mjt.gym.member.Address;
import bg.sofia.uni.fmi.mjt.gym.member.GymMember;
import bg.sofia.uni.fmi.mjt.gym.member.MemberByName;
import bg.sofia.uni.fmi.mjt.gym.member.MemberByProximityToTheGym;
import bg.sofia.uni.fmi.mjt.gym.workout.Workout;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

public class Gym implements GymAPI {
    private Address address;

    private int capacity;

    private SortedSet<GymMember> members;
    public Gym(int capacity, Address address) {
        this.capacity = capacity;
        this.address = address;
        members = new TreeSet<>();
    }

    @Override
    public SortedSet<GymMember> getMembers() {
        SortedSet<GymMember> sortedMembers = new TreeSet<>(members);
        return Collections.unmodifiableSortedSet(sortedMembers);
    }

    @Override
    public SortedSet<GymMember> getMembersSortedByName() {
        SortedSet<GymMember> sortedMembersByName = new TreeSet<>(new MemberByName());
        sortedMembersByName.addAll(members);
        return Collections.unmodifiableSortedSet(sortedMembersByName);
    }

    @Override
    public SortedSet<GymMember> getMembersSortedByProximityToGym() {
        SortedSet<GymMember> sortedMembersByName = new TreeSet<>(new MemberByProximityToTheGym(address));
        sortedMembersByName.addAll(members);
        return Collections.unmodifiableSortedSet(sortedMembersByName);
    }

    @Override
    public void addMember(GymMember member) throws GymCapacityExceededException {
        if (member == null) {
            throw new IllegalArgumentException("Member is null!");
        }
        if (members.size() == capacity) {
            throw new GymCapacityExceededException("The gym has reached its capacity");
        }
        members.add(member);
    }

    @Override
    public void addMembers(Collection<GymMember> members) throws GymCapacityExceededException {
        if (members == null) {
            throw new IllegalArgumentException("Members was null");
        }
        if (members.isEmpty()) {
            throw new IllegalArgumentException("Members was empty");
        }
        if (members.size() + members.size() >= capacity) {
            throw new GymCapacityExceededException("The gym has reached its capacity");
        }
        this.members.addAll(members);
    }

    @Override
    public boolean isMember(GymMember member) {
        if (member == null) {
            throw new IllegalArgumentException("Member is null!");
        }
        for (GymMember curr: members) {
            if (curr == member) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isExerciseTrainedOnDay(String exerciseName, DayOfWeek day) {
        if (exerciseName == null || day == null) {
            throw new IllegalArgumentException("Exercise name and day cannot be null");
        }
        if (exerciseName.isBlank()) {
            throw new IllegalArgumentException("Exercise name cannot be null");
        }
        for (GymMember curr: members) {
            for (Map.Entry<DayOfWeek, Workout> entry : curr.getTrainingProgram().entrySet()) {
                DayOfWeek key = entry.getKey();
                Workout value = entry.getValue();
                if ((key == day) && value.hasExercise(exerciseName)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Map<DayOfWeek, List<String>> getDailyListOfMembersForExercise(String exerciseName) {
        if (exerciseName == null) {
            throw new IllegalArgumentException("Exercise name cannot be null");
        }
        if ( exerciseName.isBlank()) {
            throw new IllegalArgumentException("Exercise name cannot be blank");
        }
        Map<DayOfWeek, List<String>> result = new HashMap<>();
        for (GymMember curr: members) {
            for (Map.Entry<DayOfWeek, Workout> entry : curr.getTrainingProgram().entrySet()) {
                DayOfWeek key = entry.getKey();
                Workout value = entry.getValue();
                if (value.hasExercise(exerciseName)) {
                    result.putIfAbsent(key, new ArrayList<>());
                    result.get(key).add(curr.getName());
                    if (result.get(key).isEmpty()) {
                        throw new IllegalArgumentException();
                    }
                }
            }
        }
        return Collections.unmodifiableMap(result);
    }
}
