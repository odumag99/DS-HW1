import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Arrays; // 디버그용 import -> 완성되면 지워도 됨.
  
  
public class BigInteger
{
    public static final String QUIT_COMMAND = "quit";
    public static final String MSG_INVALID_INPUT = "Wrong Input";
  
    // implement this
    public static final Pattern EXPRESSION_PATTERN = Pattern.compile(""); // 뭣에 쓰는 물건인고?

    String sign = "0";
    int[] numArray;
  
  
/*
    public BigInteger(int i)
    {
    }
  
    public BigInteger(int[] num1)
    {
    }
*/
    public BigInteger(String inputSign, int length)
    {
        this.sign = inputSign;
        this.numArray = new int[length];
    }

    public BigInteger(String inputSign, String inputVal)
    {
        this.sign = inputSign;
        this.numArray = new int[inputVal.length()];

        for (int i=0; i<inputVal.length(); i++){
            this.numArray[i] = Integer.parseInt(inputVal.substring(i, i+1));
        }

        // System.out.printf("BingInteger.__init__ %s %s %d\n", this.sign, Arrays.toString(this.numArray), this.length);
        
    }

    public int getLength(){
        return this.numArray.length;
    }

    public BigInteger delZero(){
        BigInteger result = new BigInteger("0", "0");
        // 앞에 있는 0을 제거해주는 메서드
        // 첫 번째로 유효숫자가 나올 때부터 result의 array 초기화하고 복제 시작한다
        // boolean validDigit = false;
        int firstValidDigit = -1; // this에서 처음으로 유효숫자 나오는 index(0~)

        // 0123; i=1,  newNumArray[3] 
        for (int i=0; i<this.getLength(); i++){
            if (firstValidDigit < 0){
                if (this.numArray[i] > 0) {
                    result = new BigInteger(this.sign, this.getLength()-i);
                    firstValidDigit = i;
                }
            }
            
            if (firstValidDigit >= 0){
                result.numArray[i-firstValidDigit] = this.numArray[i];
            }

        }

        // 모두 0인 경우 -> 초기에 초기화된 result("0", "0") 그대로 반환

        return result;
    }

    public int compareTo(BigInteger arg2) {
        // this의 절대값이 크면 1, 같으면 0, 작으면 -1 반환하는 메서드
        // this와 arg2 모두 delZero 된 상태로 가정

        int result = 0;

        if (this.getLength() > arg2.getLength()) {
            result = 1;
        } else if (this.getLength() < arg2.getLength()) {
            result = -1;
        } else { // 자리수 같은 경우 가장 큰 자리수부터 비교 
            for (int i=0; i < this.getLength(); i++){
                if (this.numArray[i] > arg2.numArray[i]){ // this가 더 큰 자리 발견하면
                    result = 1;
                    break;
                } else if (this.numArray[i] < arg2.numArray[i]){
                    result = -1;
                    break;
                } else { // 계속 같으면 -> this와 arg2 같다.
                    continue;
                }
            }
        }

        return result;
    }

    public int[] absSub(BigInteger arg2){
        // this의 절대값이 더 크다는 게 보장되어야 함
        int[] result = new int[this.getLength()];

        int curDigit = 0;
        int borrow = 0;
        int arg1Digit, arg2Digit;
        for (int i=0; i<this.getLength(); i++){ // i는 끝에서부터의 자리 index(0~)
            try {
                arg1Digit = this.numArray[this.getLength()-1-i];
            } catch (ArrayIndexOutOfBoundsException e) {
                // System.out.printf("absSub: this.numArray[%d] is out of bound\n", this.getLength()-1-i);
                arg1Digit = 0;
            }

            try {
                arg2Digit = arg2.numArray[arg2.getLength()-1-i];
            } catch (ArrayIndexOutOfBoundsException e) {
                arg2Digit = 0;
            }

            curDigit = arg1Digit - arg2Digit - borrow;

            if (curDigit < 0) {
                borrow = 1;
                curDigit += 10;
            } else {
                borrow = 0;
            }

            result[this.getLength()-1-i] = curDigit;
        }

        //System.out.printf("absSub: %s\n", Arrays.toString(result));
        
        return result;
    }
    
    public BigInteger add(BigInteger arg2)
    {
        BigInteger result = new BigInteger("0", "0");

        // this가 0인 경우
        if (this.sign.equals("0")){
            // arg2도 0인 경우
            if (arg2.sign.equals("0")){
                result = new BigInteger("0", "0");
            }
            // arg2는 0이 아닌 경우
            else { result = arg2; }
        } 
        // this는 0이 아니고, arg2만 0인 경우
        else if (arg2.sign.equals("0")){
            result = this;
        }
        
        // 양양 or 음음
        // 나중에 기회되면 absAdd 메서드로 빼버리기
        else if (this.sign.equals(arg2.sign)){
            int max_length = Math.max(this.getLength(), arg2.getLength());
            result = new BigInteger(this.sign, max_length+1);

            int carry = 0;
            int arg1Digit, arg2Digit;
            for (int curPlace = 0; curPlace < result.getLength(); curPlace++){ // curPlace : 끝에서 i(0~)번째 자리
                try {
                    arg1Digit = this.numArray[this.getLength()-1-curPlace]; // arg1Digit: 끝에서 i(0~)번째 자리수
                } catch (ArrayIndexOutOfBoundsException e) {
                    arg1Digit = 0;
                }

                try {
                    arg2Digit = arg2.numArray[arg2.getLength()-1-curPlace]; // arg2Digit: 끝에서 i(0~)번째 자리수
                } catch (ArrayIndexOutOfBoundsException e) {
                    arg2Digit = 0;
                }


                int curDigit = carry + arg1Digit + arg2Digit;
                carry = curDigit / 10;
                curDigit = curDigit % 10;
                result.numArray[result.getLength()-1-curPlace] = curDigit;
            }
        }

        // 양음 or 음양인 경우
        else {
            // 절대값 대소비교
            int thisCompareTo = this.compareTo(arg2);
            //System.out.printf("this.compareTo(arg2): %d\n", thisCompareTo);

            // this가 더 큰 경우 -> 부호는 this 따라. numArray는 절대값 차이
            if (thisCompareTo == 1) { 
                result.sign = this.sign;
                result.numArray = this.absSub(arg2);
            }
            // this가 더 작은 경우
            else if (thisCompareTo == -1) { 
                result.sign = arg2.sign;
                result.numArray = arg2.absSub(this);
            }
            // this와 arg2 같은 경우 -> 초기화된 result가 그대로
        }

        //System.out.printf("add: %s %s\n", result.sign, Arrays.toString(result.numArray));
        return result;
    }


    public BigInteger subtract(BigInteger arg2)
    {
        BigInteger result;

        // arg2가 0인 경우(이미 arg2는 sign이 "0" 처리 되었다고 가정)
        if (arg2.sign.equals("0")){
            result =  this;
        }
        // arg2가 0 아닌 경우 -> arg2 부호 반대로 하여 add 처리
        else {
            arg2.sign = arg2.sign.equals("+") ? "-" : "+";
            result = this.add(arg2);
        }

        //System.out.printf("subtract: %s %s\n", result.sign, Arrays.toString(result.numArray));
        return result;

    }
  
    public static int[] mulDigit(int[] arg1, int dig){
        int[] result = new int[arg1.length + 1];

        int carry = 0;
        for (int i=0; i<arg1.length; i++){
            int curDigit = arg1[arg1.length-1-i] * dig + carry;
            carry = curDigit / 10;
            curDigit = curDigit % 10;
            result[result.length-1-i] = curDigit;
        }
        result[0] = carry;

        // int가 0이나 1일 경우 좀 더 효율적으로 처리할 여지 있음

        return result;
    }

    public static int[] absMul(int[] arg1, int[] arg2){
        int[] result = new int[arg1.length + arg2.length];
        
        int[] curRow;
        for (int i=0; i<arg2.length; i++){ // i: arg2의 끝에서 i(0~)번째 자리
            // arg1에 arg2의 끝 i번째 자리를 곱함
            int dig = arg2[arg2.length-1-i];
            curRow = BigInteger.mulDigit(arg1, dig);
            // 그 결과를 result의 끝에서부터 i(0~)번째 자리에 더함
            for (int j = 0; j < curRow.length; j++) {
                result[result.length - 1 - i - j] += curRow[curRow.length - 1 - j];
                if (result[result.length - 1 - i - j] >= 10) {
                    result[result.length - 1 - i - j - 1] += result[result.length - 1 - i - j] / 10;
                    result[result.length - 1 - i - j] %= 10;
                }
            }
            
        }
        return result;

    }

    public BigInteger multiply(BigInteger arg2)
    {
        BigInteger result = new BigInteger("0", "0");

        // 둘 중 하나가 0인 경우
        if (this.sign.equals("0") || arg2.sign.equals("0")){
            return new BigInteger("0", "0");
        } 
        // 0이 아닌 경우 -> result 자리수 초기화. 자리수 <- this 자리수 + arg2 자리수
        else {
            result.sign = this.sign.equals(arg2.sign) ? "+" : "-";
            result.numArray = BigInteger.absMul(this.numArray, arg2.numArray);
        }
        // 둘 모두 부호 같은 경우

        // 둘의 부호 다른 경우

        // arg1 * arg2의 첫째자리 + arg1 * arg2의 둘째자리 * 10 + arg1 * arg2의 셋째자리 * 100 + ...
            // arg1 * arg2의 둘째자리 * 10 -> 미리 끝자리는 0으로 비워두고 시작. 새로운 BigInt 자리수는 arg1 자리수 + 1 + 1
            // arg1 * arg2의 셋째자리 * 100 -> 미리 끝자리는 00으로 비워두고 시작. 새로운 BigInt 자리수는 arg1 자리수 + 1 + 2
            // arg2 매자리수마다 모든 자리수 도는 건 비효율적 -> shiftAdd 메서드가 필요

        //System.out.printf("multiply: %s %s\n", result.sign, Arrays.toString(result.numArray));
        return result;
    }


    @Override
    public String toString()
    {
        // 어디에 쓰이는교?
        // 각 연산 함수에서 이미 쓸 데 없는 앞 자리수는 모두 제거되었다고 가정!

        // 0 여부 판단 -> 부호 붙임 없이 0만 반환해야 함 -> 이건 각 연산 함수 메서드에서 처리하는 걸로...
        String result = "";

        if (this.sign.equals("0")){ // 0인 경우
            result = "0"; // -> 0만 출력
        } else { // 0이 아닌 경우
            if (this.sign.equals("-")){ // 음수인 경우 -> 앞에 - 붙여줘고 양수 표현에 합류
            result += "-";
            // 출력하되 앞에 있는 앞에 나오는 0은 제외해야 함 -> 각 연산자
            // 단 숫자 자체가 0인 경우 제외 <- this 모든 array 다 돌아도 validDigit이 false인 경우
            }
            
            for (int i=0; i<this.getLength(); i++){
                result += Integer.toString(this.numArray[i]);
            }
        }

        return result;
    }


    static String[] parse(String input)
    {
        String[] result = new String[5];

        // 공백 제거
        input = input.replaceAll(" ", ""); 

        // 부호 및 값 추출
        Pattern pattern = Pattern.compile("([+\\-]?)(\\d+)([+\\-*])([+\\-]?)(\\d+)");
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()){
            result[0] = matcher.group(1).isEmpty() ? "+" : matcher.group(1); // 0인 경우는?
            result[1] = matcher.group(2);
            if (result[1].equals("0")){
                result[0] = "0";
            }
            result[2] = matcher.group(3);
            result[3] = matcher.group(4).isEmpty() ? "+" : matcher.group(4);
            result[4] = matcher.group(5);
            if (result[4].equals("0")){
                result[3] = "0";
            }

        } else {
            //System.out.println("Parse Matcher not found.");
        }

        return result;
    }

    static BigInteger evaluate(String input) throws IllegalArgumentException
    {
        // implement here

        String[] parsedStrs = parse(input);
        

        // BigInteger 객체 생성
        BigInteger arg1 = new BigInteger(parsedStrs[0], parsedStrs[1]);
        BigInteger arg2 = new BigInteger(parsedStrs[3], parsedStrs[4]);

        BigInteger result = new BigInteger("0", "0");

        // 각 연산에서 쓸 데 없이 앞에 붙은 0은 모두 제거시키도록!

        if (parsedStrs[2].equals("+")){
            result = arg1.add(arg2);
        }
        else if (parsedStrs[2].equals("-")){
            result = arg1.subtract(arg2);
        }
        else if (parsedStrs[2].equals("*")){
            result = arg1.multiply(arg2);
        }


        result = result.delZero();

        
            // 연산자 추출
                // 그런데 맨 앞에 부호 붙은 것은 안 됨.
                // 숫자 나오기 전 +- 나오는 경우
                // 숫자 나오고 난 후 나오는 +-*는 무조건 연산 부호
                // 그 이후에 +- 나온다면 그것은 sign
            // num1 num2 양쪽 공백 제거
        // using regex is allowed
            // 어디에 써먹을꼬?
  
        // One possible implementation
        // BigInteger num1 = new BigInteger(arg1);
            // arg1은 String일 것...String을 받아서 BigInteger class로 변환시켜야...
        // BigInteger num2 = new BigInteger(arg2);
        // BigInteger result = num1.add(num2);

        return result;
    }
  
    public static void main(String[] args) throws Exception
    {
        try (InputStreamReader isr = new InputStreamReader(System.in))
        {
            try (BufferedReader reader = new BufferedReader(isr))
            {
                boolean done = false;
                while (!done)
                {
                    String input = reader.readLine();
  
                    try
                    {
                        done = processInput(input);
                    }
                    catch (IllegalArgumentException e)
                    {
                        System.err.println(MSG_INVALID_INPUT);
                    }
                }
            }
        }
    }
  
    static boolean processInput(String input) throws IllegalArgumentException
    {
        boolean quit = isQuitCmd(input);
  
        if (quit)
        {
            return true;
        }
        else
        {
            BigInteger result = evaluate(input);
            System.out.println(result.toString());
  
            return false;
        }
    }
  
    static boolean isQuitCmd(String input)
    {
        return input.equalsIgnoreCase(QUIT_COMMAND);
    }
}
