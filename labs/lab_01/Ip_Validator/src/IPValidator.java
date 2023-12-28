import java.util.Objects;
import static java.lang.Character.isDigit;

public class IPValidator {
    public static boolean validateIPv4Address(String str) {
        String[] splitted = str.split("\\.");
        if (splitted.length != 4) {
            return false;
        }
        for (String s : splitted) {
            if(Objects.equals(s, "")){
                return false;
            }
            int length = s.length();
            int firstNonZeroIndex = 0;
            while (firstNonZeroIndex < length && s.charAt(firstNonZeroIndex) == '0') {
                firstNonZeroIndex++;
            }

            if (firstNonZeroIndex > 1)
                return false;
            char[] charArray = s.toCharArray();
            for (char c : charArray) {
                if (!Character.isDigit(c)) {
                    return false;
                }
            }
            int partValue = Integer.parseInt(s);
            if (partValue < 0 || partValue > 255) {
                return false;
            }
        }
        return true;
    }
}
