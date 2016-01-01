/**
 * Write a description of class Runner here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */

import java.util.*;

public class Runner
{
    public static void main (String[] args) {
        Scanner s = new Scanner(System.in);
        
        outerloop: //I have a feeling labels are terrible practice but i wanted this done fast
        while (true) {
            String input;
            Double result;
            System.out.println("Enter Expression To Evaluate (Or 'q' To Quit):");
            input = s.nextLine();
            
            if (input.equals("q") || input.equals("Q")) {
                System.out.println("Goodbye");
                break outerloop;
            }
            result = calculate(input);
        
            while (result == null) {
                System.out.println("Error ? Please Try Again With a Valid Expression:");
                input = s.nextLine();
                if (input.equals("q") || input.equals("Q")) {
                    System.out.println("Goodbye");
                    break outerloop;
                }
                result = calculate(input);
            }
            formattedPrint(result);
        }
    }
    public static Double calculate(String input) {
        Tokenizer t = new Tokenizer();
        ShuntingYard sy = new ShuntingYard();
        Evaluator ev = new Evaluator();
        ArrayList<Token> tokens = t.tokenize(input);
        ArrayList<Token> postfix = sy.toPostfix(tokens);
        Double result = ev.eval(postfix);
        return result;
    }
    public static void formattedPrint(double res) {
        boolean hasDecimal = Double.compare(res,Math.floor(res)) != 0;
        if (hasDecimal) {
            System.out.printf("%.4f\n",res);
        }
        else System.out.println((int)res);
    }
}
