
public class IPValidator {
    public static boolean validateIPv4Address(String str){
        String[] splitted=str.split("\\.");
        if(splitted.length!=4){
            return false;
        }
        for (String s : splitted) {
            int length = s.length();
            int firstNonZeroIndex = 0;
            while (firstNonZeroIndex < length && s.charAt(firstNonZeroIndex) == '0') {
                firstNonZeroIndex++;
            }

            if(firstNonZeroIndex > 0)
                return false;
            char[] charArray=s.toCharArray();
            for(int i=0;i<charArray.length;i++){
                if(!isDigit(charArray[i])){
                    return false;
                }
            }
        }
        return true;
    }

}
