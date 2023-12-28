package bg.sofia.uni.fmi.mjt.space.rocket;

import java.util.Optional;

import static bg.sofia.uni.fmi.mjt.space.stringutils.StringUtils.parseOptionalDouble;

public record Rocket(String id, String name, Optional<String> wiki, Optional<Double> height) {
    private static final int INDEX_ZERO = 0;
    private static final int INDEX_ONE = 1;
    private static final int INDEX_TWO = 2;
    private static final int LENGTH_TWO = 2;
    private static final int INDEX_THREE = 3;
    private static final int LENGTH_THREE = 3;
    private static final int LENGTH_FOUR = 4;
    private static final int INDEX_FOUR = 4;

    public static Rocket of(String rocket) {
        String[] separated = rocket.split(",");
        if (separated.length > LENGTH_FOUR) {
            String[] separatedCopy = new String[LENGTH_FOUR];
            separatedCopy[INDEX_ZERO] = separated[INDEX_ZERO];
            separatedCopy[INDEX_ONE] = separated[INDEX_ONE] + "," + separated[INDEX_TWO];
            separatedCopy[INDEX_TWO] = separated[INDEX_THREE];
            separatedCopy[INDEX_THREE] = separated[INDEX_FOUR];
            separated = separatedCopy;
        }
        String id = separated[INDEX_ZERO];
        String name = separated[INDEX_ONE];
        Optional<String> wiki = separated.length > LENGTH_TWO && !separated[INDEX_TWO].isEmpty()
            ? Optional.of(separated[INDEX_TWO])
            : Optional.empty();
        Optional<Double> height = separated.length > LENGTH_THREE && !separated[INDEX_THREE].isEmpty()
            ? parseOptionalDouble(separated[INDEX_THREE])
            : Optional.empty();

        return new Rocket(id, name, wiki, height);
    }
}
