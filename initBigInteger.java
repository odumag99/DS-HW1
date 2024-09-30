import java.util.Arrays;

public class test {
    public static void main(String[] args){
        String str = "1234";
        int[] result;

        result = new int[str.length()];

        for (int i=0; i<str.length(); i++){
            result[i] = Integer.parseInt(str.substring(i, i+1));
        }

        System.out.printf("%s", Arrays.toString(result));

    }
}
