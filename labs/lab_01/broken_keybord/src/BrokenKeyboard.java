import java.util.Objects;

public  class BrokenKeyboard {

    public static void removeSpaces(String[] strings){

    }
    public static int calculateFullyTypedWords(String message, String brokenKeys) {
        if(Objects.equals(message, "")){
            return 0;
        }
        String[] splitted = message.split("\\s");
        int count = splitted.length;
        char[] charArray = brokenKeys.toCharArray();
        for(int i = 0;i<splitted.length;i++) {
            if (splitted[i].isBlank())
            {
                count--;
                continue;
            }
                for (int j = 0; j < charArray.length; j++) {
                    if (splitted[i].contains(String.valueOf(brokenKeys.charAt(j)))) {
                        count--;
                        break;
                    }
                }
        }
        return count;
    }

}

