import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
  
  
public class BigInteger
{
    public static final String QUIT_COMMAND = "quit";
    public static final String MSG_INVALID_INPUT = "Wrong Input";
  
    // implement this
    public static final Pattern EXPRESSION_PATTERN = Pattern.compile(""); // 뭣에 쓰는 물건인고?

    String sign = "0";
    int[] numArray;
    int length;
  
  
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
        this.length = length;
    }

    public BigInteger(String inputSign, String inputVal)
    {
        this.sign = inputSign;
        this.numArray = new int[inputVal.length()];
        this.length = inputVal.length();

        for (int i=0; i<inputVal.length(); i++){
            this.numArray[i] = Integer.parseInt(inputVal.substring(i, i+1));
        }

        System.out.printf("BingInteger.__init__ %s %s %d\n", this.sign, Arrays.toString(this.numArray), this.length);
        
    }


    public BigInteger delZero(){
        BigInteger result = new BigInteger("0", "0");
        // 앞에 있는 0을 제거해주는 메서드
        // 첫 번째로 유효숫자가 나올 때부터 result의 array 초기화하고 복제 시작한다
        // boolean validDigit = false;
        int firstValidDigit = -1; // this에서 처음으로 유효숫자 나오는 index(0~)

        // 0123; i=1,  newNumArray[3] 
        for (int i=0; i<this.length; i++){
            if (firstValidDigit < 0){
                if (this.numArray[i] > 0) {
                    result = new BigInteger(this.sign, this.length-i);
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
        else if (this.sign.equals(arg2.sign)){
            int max_length = Math.max(this.length, arg2.length);
            result = new BigInteger(this.sign, max_length+1);

            int carry = 0;
            int arg1Digit, arg2Digit;
            for (int curPlace = 0; curPlace < result.length; curPlace++){ // curPlace : 끝에서 i(0~)번째 자리수
                try {
                    arg1Digit = this.numArray[this.length-1-curPlace];
                } catch (ArrayIndexOutOfBoundsException e) {
                    arg1Digit = 0;
                }

                try {
                    arg2Digit = arg2.numArray[arg2.length-1-curPlace];
                } catch (ArrayIndexOutOfBoundsException e) {
                    arg2Digit = 0;
                }


                int curDigit = carry + arg1Digit + arg2Digit;
                if (curDigit >= 10){
                    carry = curDigit / 10;
                    curDigit = curDigit % 10;
                }
                result.numArray[result.length-1-curPlace] = curDigit;
            }
        }

        // 양음 or 음양인 경우
            // 대소비교
                // 같은 경우 -> 부호 0, 값도 0 처리
                // 다른 경우 -> 절대값 더 큰 게 부호가 된다.
                    // 절대값 차이 계산
            //

        // 둘 중 하나가 0, 둘 모두 0
        // 실제로 컴퓨터가 어떻게 연산하는가?
            // 그냥  char로 모든 수 배열 받고, 초보적인 사칙연산하듯이 하면 되겠다.




            
            // 비트 연산의 방법 사용?
            // 7에서 오버플로우 된다고 생각해보자
            // 음수의 덧셈도 고려
        // overflow 감지 어떻게 하는지??

        // 필요 없는 array는 제거해줘야 함.

        result = result.delZero();

        return result;
    }

/*
    public BigInteger subtract(BigInteger big)
    {
        // 음수의 뺄셈 -> add로 처리
    }
  
    public BigInteger multiply(BigInteger big)
    {
        // 필요없는 array는 제거해 줘야 함
    }
*/

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
            // 출력하되 앞에 있는 앞에 나오는 0은 제외해야 함
            // 단 숫자 자체가 0인 경우 제외 <- this 모든 array 다 돌아도 validDigit이 false인 경우
            }
            
            for (int i=0; i<this.length; i++){
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
            System.out.println("Parse Matcher not found.");
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
            ;
        }
        else if (parsedStrs[2].equals("*")){
            ;
        }


        
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
        // return result;
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
