package bg.sofia.uni.fmi.mjt.space.stringutils;

import java.util.Optional;

public class StringUtils {
    public static String[] splitLine(String line) {
        int length = line.length();
        StringBuilder sb = new StringBuilder(length);
        boolean inQuotes = false;
        for (char c : line.toCharArray()) {
            if (c == '\"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                sb.append(";");
                continue;
            }
            sb.append(c);
        }
        return sb.toString().split(";");
    }

    public static String removeQuotes(String text) {
        return text.replaceAll("\"", "");
    }

    public static Optional<Double> parseOptionalDouble(String number) {
        double extractedNumber = extractNumber(number);
        return extractedNumber != 0.0 ? Optional.of(extractedNumber) : Optional.empty();
    }

    public static double extractNumber(String input) {
        boolean foundDecimal = false;
        boolean foundNumber = false;
        StringBuilder numberStr = new StringBuilder();

        for (char c : input.toCharArray()) {
            if ((c >= '0' && c <= '9') || (c == '.' && !foundDecimal)) {
                numberStr.append(c);
                if (c == '.') {
                    foundDecimal = true;
                }
                foundNumber = true;
            } else if (foundNumber) {
                break;
            }
        }

        if (foundNumber) {
            return Double.parseDouble(numberStr.toString());
        } else {
            return 0.0;
        }
    }
}
