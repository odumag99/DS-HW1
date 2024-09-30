import java.util.Arrays;

public class test {
    public static void main(String[] args){
        String inputVal = "1234";
        int[] numArray;

        System.out.printf("%s\n", inputVal.substring(0,4));

        numArray = new int[inputVal.length()];

        for (int i=0; i<inputVal.length(); i++){
            numArray[i] = Integer.parseInt(inputVal.substring(i, i+1));
        }

        System.out.printf("%s, %d\n", Arrays.toString(numArray), numArray.length);

    }
}
