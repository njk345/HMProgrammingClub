import java.util.*;

public class Evaluator
{
   public Evaluator() {
       
   }
   //using Double wrapper class b/c could return null
   public Double eval(ArrayList<Token> postfix) {
       if (postfix == null) return null;
       
       Stack<Double> nums = new Stack<Double>();
       ArrayList<Token> ops = new ArrayList<Token>();
       
       for (int i = 0; i < postfix.size(); i++) {
           if (postfix.get(i).getVal() == Token.Value.NUM) {
               nums.push(postfix.get(i).getDoubleVal());
           }
           else { //its an operator
               if (postfix.get(i).getVal() == Token.Value.UNOP) {
                   double x = nums.pop();
                   nums.push(performUnaryOp(postfix.get(i),x));
               }
               else { //it's a binary operator
                   double y = nums.pop();
                   if (nums.empty()) return null;
                   double x = nums.pop();
                   nums.push(performBinaryOp(postfix.get(i),x,y));
               }
           }
       }
       return nums.pop();
   }
   
   public Double performUnaryOp(Token op, double x) {
       Double rv = null; //default return is null, and is returned if op not supported
       String s = op.getString();
       
       if (s.equals("SQRT")) {
           rv = Math.sqrt(x);
       }
       return rv;
   }
   public Double performBinaryOp(Token op, double x, double y) {
       String s = op.getString();
       Double rv = null;
       
       if (s.equals("ADD")) {
           rv = x + y;
       }
       if (s.equals("SUB")) {
           rv = x - y;
       }
       if (s.equals("MUL")) {
           rv = x * y;
       }
       if (s.equals("DIV")) {
           rv = x / y;
       }
       if (s.equals("POW")) {
           rv = Math.pow(x,y);
       }
       return rv;
   }
}
