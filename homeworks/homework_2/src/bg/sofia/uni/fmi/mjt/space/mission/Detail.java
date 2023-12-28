package bg.sofia.uni.fmi.mjt.space.mission;

import static bg.sofia.uni.fmi.mjt.space.stringutils.StringUtils.removeQuotes;

public record Detail(String rocketName, String payload) {
    private static final int INDEX_ZERO = 0;
    private static final int INDEX_ONE = 1;

    public static Detail of(String detail) {
        String[] separated = detail.split("\\|");
        String rocketName = removeQuotes(separated[INDEX_ZERO]);
        String payload = removeQuotes(separated[INDEX_ONE]);
        return new Detail(rocketName, payload);
    }
}
