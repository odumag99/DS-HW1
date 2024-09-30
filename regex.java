import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class regex {
    public static void main(String[] args){
        String arg1Sign, arg1Val, operand, arg2Sign, arg2Val;
        String input = "   -10000000    *  -0 ";

        // 공백 제거
        input = input.replaceAll(" ", "");
        
        Pattern pattern = Pattern.compile("([+\\-]?)(\\d+)([+\\-*])([+\\-]?)(\\d+)");
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()){
            arg1Sign = matcher.group(1).isEmpty() ? "+" : matcher.group(1);
            arg1Val = matcher.group(2);
            operand = matcher.group(3);
            arg2Sign = matcher.group(4).isEmpty() ? "+" : matcher.group(4);
            arg2Val = matcher.group(5);

            System.out.printf("arg1Sign:%s, arg1Val:%s, operand:%s, arg2Sign:%s, arg2Val:%s\n", arg1Sign, arg1Val, operand, arg2Sign, arg2Val);

        } else {
            System.out.println("Parse Matcher not found.");
        }
    }
    
}
