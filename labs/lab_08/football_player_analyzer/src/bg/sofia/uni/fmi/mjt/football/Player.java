package bg.sofia.uni.fmi.mjt.football;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public record Player(String name, String fullName, LocalDate birthDate, int age, double heightCm,
                     double weightKg, List<Position> positions, String nationality, int overallRating,
                     int potential, long valueEuro, long wageEuro, Foot preferredFoot) {

    private static final String PLAYER_DELIMITER = ";";
    private static final int INDEX_ZERO = 0;
    private static final int INDEX_ONE = 1;
    private static final int INDEX_TWO = 2;
    private static final int INDEX_THREE = 3;
    private static final int INDEX_FOUR = 4;
    private static final int INDEX_FIVE = 5;
    private static final int INDEX_SIX = 6;
    private static final int INDEX_SEVEN = 7;
    private static final int INDEX_EIGHT = 8;
    private static final int INDEX_NINE = 9;
    private static final int INDEX_TEN = 10;
    private static final int INDEX_ELEVEN = 11;
    private static final int INDEX_TWELVE = 12;

    public static Player of(String line) {
        final String[] tokens = line.split(PLAYER_DELIMITER);
        final String name = tokens[INDEX_ZERO];
        final String fullName = tokens[INDEX_ONE];
        final String[] birthDataString = tokens[INDEX_TWO].split("/");
        int month = Integer.parseInt(birthDataString[INDEX_ZERO]);
        int day = Integer.parseInt(birthDataString[INDEX_ONE]);
        int year = Integer.parseInt(birthDataString[INDEX_TWO]);
        final LocalDate birthDate = LocalDate.of(year, month, day);
        int age = Integer.parseInt(tokens[INDEX_THREE]);
        double heightCm = Double.parseDouble(tokens[INDEX_FOUR]);
        double weightKg = Double.parseDouble(tokens[INDEX_FIVE]);
        List<Position> positions = Arrays.stream(tokens[INDEX_SIX].split(",")).map(Position::valueOf).toList();
        String nationality = tokens[INDEX_SEVEN];
        int overallRating = Integer.parseInt(tokens[INDEX_EIGHT]);
        int potential = Integer.parseInt(tokens[INDEX_NINE]);
        long valueEuro = Long.parseLong(tokens[INDEX_TEN]);
        long wageEuro = Long.parseLong(tokens[INDEX_ELEVEN]);
        Foot preferredFoot = Foot.valueOf(tokens[INDEX_TWELVE].toUpperCase());
        return new Player(name, fullName, birthDate , age, heightCm, weightKg, positions , nationality,
            overallRating, potential, valueEuro, wageEuro, preferredFoot);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Player player = (Player) obj;
        return age == player.age &&
            Double.compare(player.heightCm, heightCm) == 0 &&
            Double.compare(player.weightKg, weightKg) == 0 &&
            overallRating == player.overallRating &&
            potential == player.potential &&
            valueEuro == player.valueEuro &&
            wageEuro == player.wageEuro &&
            Objects.equals(name, player.name) &&
            Objects.equals(fullName, player.fullName) &&
            Objects.equals(birthDate, player.birthDate) &&
            Objects.equals(positions, player.positions) &&
            Objects.equals(nationality, player.nationality) &&
            preferredFoot == player.preferredFoot;
    }

    // Explicitly overriding hashCode method
    @Override
    public int hashCode() {
        return Objects.hash(name, fullName, birthDate, age, heightCm, weightKg, positions,
            nationality, overallRating, potential, valueEuro, wageEuro, preferredFoot);
    }
}
