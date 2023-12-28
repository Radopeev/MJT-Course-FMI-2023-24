package bg.sofia.uni.fmi.mjt.space.mission;

import bg.sofia.uni.fmi.mjt.space.rocket.RocketStatus;
import bg.sofia.uni.fmi.mjt.space.stringutils.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;

import static bg.sofia.uni.fmi.mjt.space.stringutils.StringUtils.parseOptionalDouble;
import static bg.sofia.uni.fmi.mjt.space.stringutils.StringUtils.removeQuotes;

public record Mission(String id, String company, String location, LocalDate date, Detail detail,
                      RocketStatus rocketStatus, Optional<Double> cost, MissionStatus missionStatus) {

    private static final int INDEX_ZERO = 0;
    private static final int INDEX_ONE = 1;
    private static final int INDEX_TWO = 2;
    private static final int INDEX_THREE = 3;
    private static final int INDEX_FOUR = 4;
    private static final int INDEX_FIVE = 5;
    private static final int INDEX_SIX = 6;
    private static final int INDEX_SEVEN = 7;

    public static Mission of(String mission) {
        String[] parts = StringUtils.splitLine(mission);
        String id = parts[INDEX_ZERO];
        String company = parts[INDEX_ONE];
        String location = removeQuotes(parts[INDEX_TWO]);
        String dateString = removeQuotes(parts[INDEX_THREE]);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd, yyyy", Locale.ENGLISH);
        LocalDate date = LocalDate.parse(dateString, formatter);
        Detail detail = Detail.of(parts[INDEX_FOUR]);
        RocketStatus rocketStatus = RocketStatus.fromString(parts[INDEX_FIVE]);
        Optional<Double> cost = parseOptionalDouble(removeQuotes(parts[INDEX_SIX]));

        MissionStatus missionStatus = MissionStatus.fromString(parts[INDEX_SEVEN]);
        return new Mission(id, company, location, date, detail, rocketStatus, cost, missionStatus);
    }
}
