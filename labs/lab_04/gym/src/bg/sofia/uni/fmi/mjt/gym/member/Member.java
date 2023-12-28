package bg.sofia.uni.fmi.mjt.gym.member;

import bg.sofia.uni.fmi.mjt.gym.workout.Exercise;
import bg.sofia.uni.fmi.mjt.gym.workout.Workout;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Member implements GymMember {
    private Map<DayOfWeek, Workout> trainingProgram;
    private Address address;

    private String name;

    private int age;

    private String personalIdNumber;

    private Gender gender;

    public Member(Address address, String name, int age, String personalIdNumber, Gender gender) {
        this.address = address;
        this.name = name;
        this.age = age;
        this.personalIdNumber = personalIdNumber;
        this.gender = gender;
        this.trainingProgram = new HashMap<>();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getAge() {
        return age;
    }

    @Override
    public String getPersonalIdNumber() {
        return personalIdNumber;
    }

    @Override
    public Gender getGender() {
        return gender;
    }

    @Override
    public Address getAddress() {
        return address;
    }

    @Override
    public Map<DayOfWeek, Workout> getTrainingProgram() {
        return Map.copyOf(trainingProgram);
    }

    @Override
    public void setWorkout(DayOfWeek day, Workout workout) {
        if (day == null || workout == null) {
            throw new IllegalArgumentException("Day and workout cannot be null");
        }
        trainingProgram.put(day, workout);
    }

    @Override
    public Collection<DayOfWeek> getDaysFinishingWith(String exerciseName) {
        if (exerciseName == null) {
            throw new IllegalArgumentException("Exercise name cannot be null");
        }
        if (exerciseName.isBlank()) {
            throw new IllegalArgumentException("Exercise name cannot be blank");
        }
        ArrayList<DayOfWeek> result = new ArrayList<>();
        for (Map.Entry<DayOfWeek, Workout> entry : trainingProgram.entrySet()) {
            DayOfWeek key = entry.getKey();
            Workout value = entry.getValue();
            if (key == null || value == null) {
                continue;
            }
            if (value.exercises().getLast().name().equals(exerciseName)) {
                result.add(key);
            }
        }
        return result;
    }

    @Override
    public void addExercise(DayOfWeek day, Exercise exercise) {
        if (day == null || exercise == null) {
            throw new IllegalArgumentException();
        }

        Workout curr = trainingProgram.get(day);

        if (curr == null) {
            throw new DayOffException("This day is day off");
        }

        curr.exercises().add(exercise);
    }

    @Override
    public void addExercises(DayOfWeek day, List<Exercise> exercises) {
        if (day == null || exercises == null) {
            throw new IllegalArgumentException("The values cannot be null");
        }
        if (exercises.isEmpty()) {
            throw new IllegalArgumentException("It must have at least one exercise");
        }

        Workout curr = trainingProgram.get(day);

        if (curr == null) {
            throw new DayOffException("This day is day off");
        }

        curr.exercises().addAll(exercises);
    }

    @Override
    public int compareTo(GymMember other) {
        return personalIdNumber.compareTo(other.getPersonalIdNumber());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof GymMember gymMember)) {
            return false;
        }

        return getPersonalIdNumber().equals(gymMember.getPersonalIdNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPersonalIdNumber());
    }
}
