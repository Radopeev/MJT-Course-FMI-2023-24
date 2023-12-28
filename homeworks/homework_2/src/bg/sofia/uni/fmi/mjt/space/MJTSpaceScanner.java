package bg.sofia.uni.fmi.mjt.space;

import bg.sofia.uni.fmi.mjt.space.algorithm.Rijndael;
import bg.sofia.uni.fmi.mjt.space.exception.CipherException;
import bg.sofia.uni.fmi.mjt.space.exception.TimeFrameMismatchException;
import bg.sofia.uni.fmi.mjt.space.mission.Mission;
import bg.sofia.uni.fmi.mjt.space.mission.MissionStatus;
import bg.sofia.uni.fmi.mjt.space.rocket.Rocket;
import bg.sofia.uni.fmi.mjt.space.rocket.RocketStatus;

import javax.crypto.SecretKey;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class MJTSpaceScanner implements SpaceScannerAPI {

    private List<Mission> missions;
    private List<Rocket> rockets;
    private Rijndael rijndael;

    public MJTSpaceScanner(Reader missionsReader, Reader rocketsReader, SecretKey secretKey) {
        try (var bf = new BufferedReader(missionsReader)) {
            String firstLine = bf.readLine();
            missions = bf.lines().map(Mission::of).toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try (var bf = new BufferedReader(rocketsReader)) {
            String firstLine = bf.readLine();
            rockets = bf.lines().map(Rocket::of).toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        rijndael = new Rijndael(secretKey);
    }

    @Override
    public Collection<Mission> getAllMissions() {
        return missions;
    }

    @Override
    public Collection<Mission> getAllMissions(MissionStatus missionStatus) {
        if (missionStatus == null) {
            throw new IllegalArgumentException("Mission status cannot be null");
        }
        return missions.stream().filter(m -> m.missionStatus().equals(missionStatus)).toList();
    }

    @Override
    public String getCompanyWithMostSuccessfulMissions(LocalDate from, LocalDate to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Time frames cannot be null");
        }
        if (from.isAfter(to) || to.isBefore(from)) {
            throw new TimeFrameMismatchException("Time frames do not match");
        }

        Map<String, Long> companyMissionCounts = missions.stream()
            .filter(m -> m.missionStatus().equals(MissionStatus.SUCCESS))
            .filter(m -> m.date().isAfter(from) && m.date().isBefore(to))
            .collect(Collectors.groupingBy(Mission::company, Collectors.counting()));

        return companyMissionCounts.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse("");
    }

    @Override
    public Map<String, Collection<Mission>> getMissionsPerCountry() {
        return missions.stream()
            .collect(Collectors.groupingBy(
                m -> {
                    String[] separated = m.location().split(",");
                    return separated[separated.length - 1].replaceAll("\\s", "");
                },
                Collectors.collectingAndThen(
                    Collectors.toList(),
                    col -> (Collection<Mission>) col)
            ));
    }

    @Override
    public List<Mission> getTopNLeastExpensiveMissions(int n, MissionStatus missionStatus, RocketStatus rocketStatus) {
        if (n <= 0) {
            throw new IllegalArgumentException("N cannot be less or equal to zero");
        }
        if (missionStatus == null || rocketStatus == null) {
            throw new IllegalArgumentException("Mission status or rocket status cannot be null");
        }
        return missions.stream()
            .filter(m -> m.missionStatus().equals(missionStatus))
            .filter(m -> m.rocketStatus().equals(rocketStatus))
            .sorted(Comparator.comparingDouble(m -> m.cost().orElse(Double.MAX_VALUE)))
            .limit(n)
            .toList();
    }

    @Override
    public Map<String, String> getMostDesiredLocationForMissionsPerCompany() {
        Map<String, Map<String, Long>> countedLocations = missions.stream()
            .collect(Collectors.groupingBy(Mission::company,
                Collectors.groupingBy(Mission::location, Collectors.counting())
            ));

        return countedLocations.entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> entry.getValue().entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .orElse("")
            ));
    }

    @Override
    public Map<String, String> getLocationWithMostSuccessfulMissionsPerCompany(LocalDate from, LocalDate to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Time frames cannot be null");
        }
        if (from.isAfter(to) || to.isBefore(from)) {
            throw new TimeFrameMismatchException("Time frames do not match");
        }
        Map<String, Map<String, Long>> filteredMission = missions.stream()
            .filter(m -> m.date().isAfter(from) && m.date().isBefore(to))
            .filter(m -> m.missionStatus().equals(MissionStatus.SUCCESS))
            .collect(Collectors.groupingBy(Mission::company,
                Collectors.groupingBy(Mission::location, Collectors.counting())
            ));
        return filteredMission.entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> entry.getValue().entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .orElse("")
            ));
    }

    @Override
    public Collection<Rocket> getAllRockets() {
        return rockets;
    }

    @Override
    public List<Rocket> getTopNTallestRockets(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("N cannot be less or equal to zero");
        }
        return rockets.stream()
            .filter( r -> r.height().isPresent())
            .sorted((rocket1, rocket2) -> Double.compare(
                rocket2.height().orElse(Double.MIN_VALUE),
                rocket1.height().orElse(Double.MIN_VALUE)))
            .limit(n)
            .toList();
    }

    @Override
    public Map<String, Optional<String>> getWikiPageForRocket() {
        return rockets.stream()
            .collect(Collectors.toMap(Rocket::name, Rocket::wiki));
    }

    @Override
    public List<String> getWikiPagesForRocketsUsedInMostExpensiveMissions(int n, MissionStatus missionStatus,
                                                                          RocketStatus rocketStatus) {
        if (n <= 0) {
            throw new IllegalArgumentException("N cannot be less than or equal to 0");
        }
        if (missionStatus == null || rocketStatus == null) {
            throw new IllegalArgumentException("Mission status and rocket status cannot be null");
        }

        List<Mission> filteredMissions = missions.stream()
            .filter(mission -> mission.missionStatus() == missionStatus)
            .filter(mission -> mission.rocketStatus() == rocketStatus)
            .sorted((m1, m2) -> Double.compare(
                m2.cost().orElse(Double.MIN_VALUE),
                m1.cost().orElse(Double.MIN_VALUE)))
            .limit(n)
            .toList();

        Set<String> rocketNames = filteredMissions.stream()
            .map(mission -> mission.detail().rocketName())
            .collect(Collectors.toSet());

        return rockets.stream()
            .filter(r -> rocketNames.stream()
                .anyMatch(name -> name.trim().equalsIgnoreCase(r.name().trim())))
            .map(Rocket::wiki)
            .filter(Optional::isPresent)
            .map(Optional::orElseThrow)
            .toList();
    }

    @Override
    public void saveMostReliableRocket(OutputStream outputStream, LocalDate from, LocalDate to) throws CipherException {
        if (from == null || to == null) {
            throw new IllegalArgumentException("From and to cannot be null");
        }
        if (from.isAfter(to) || to.isBefore(from)) {
            throw new TimeFrameMismatchException("Time frames cannot mismatch");
        }
        String mostReliableRocket = calculateReliability(from, to);
        InputStream inputStream = new ByteArrayInputStream(mostReliableRocket.getBytes(StandardCharsets.UTF_8));
        rijndael.encrypt(inputStream, outputStream);
    }

    private String calculateReliability(LocalDate from, LocalDate to) {
        Map<String, Double> reliabilityMap = missions.stream()
            .filter(m -> m.date().isAfter(from) && m.date().isBefore(to))
            .collect(Collectors.groupingBy(
                m -> m.detail().rocketName().trim(),
                Collectors.partitioningBy(
                    m -> m.missionStatus().equals(MissionStatus.SUCCESS),
                    Collectors.counting()
                )
            ))
            .entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> {
                    long successfulMissions = entry.getValue().getOrDefault(true, 0L);
                    long failedMissions = entry.getValue().getOrDefault(false, 0L);
                    long totalMissions = failedMissions + successfulMissions;
                    return (double) (2 * successfulMissions + failedMissions) / (2 * totalMissions);
                }
            ));

        Optional<Map.Entry<String, Double>> maxReliabilityEntry = reliabilityMap.entrySet().stream()
            .max(Map.Entry.comparingByValue());

        return maxReliabilityEntry.map(Map.Entry::getKey).orElse("");
    }
}
