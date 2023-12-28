import java.util.Arrays;

public class JumpGame {
    public static boolean canWin(int[] array){
        int len= array.length;
        if(len==1)
            return true;
        if(len<1) {
            return false;
        }
        int num=array[0];
        for(int i=num;i>0;i--){
            int[] copy= Arrays.copyOfRange(array,i,len);
            if(!canWin(copy)){
                continue;
            }
            return true;
        }
        return false;
    }
}
