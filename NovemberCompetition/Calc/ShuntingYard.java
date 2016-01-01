import java.util.*;

public class ShuntingYard {
    
    public ShuntingYard() {
        //blank constructor
    }

    public ArrayList<Token> toPostfix(ArrayList<Token> infix) {
        //this function might be a tad unwieldy
        if (infix == null) return null;
        Stack<Token> ops = new Stack<Token>();
        ArrayList<Token> output = new ArrayList<Token>();
        for (int i = 0; i < infix.size(); i++) {
            Token t = infix.get(i);
            switch (t.getVal()) {
                case NUM:
                if (i != 0) {
                    if (infix.get(i-1).getVal() == Token.Value.NUM) {
                        return null;
                        //if back to back numbers, input error, get outta here
                        //only in cases of back-to-back pi or e, since space removal
                        //blends numbers into one
                    }
                }
                output.add(t);
                break;

                case LPAREN:
                //if left paren, add to ops stack
                ops.push(t);
                break;

                case RPAREN:
                //if right paren:
                    //get outta here returning null if no preceding ops (stack empty)
                    //pop ops off ops stack and add into output until left paren encountered
                    //if no left paren found before reaching bottom of stack, mismatched parentheses
                boolean parenMismatch = false;
                if (ops.empty()) return null;
                
                Token a = ops.pop();
                while (a.getVal() != Token.Value.LPAREN) {
                    output.add(a);
                    if (ops.empty()) {
                        parenMismatch = true;
                        break;
                    }
                    a = ops.pop();
                }
                if (parenMismatch) {
                    return null;
                    //have searched to bottom of stack and not found a LPAREN
                    //thus parenthesis mismatch on the user's part
                }
                //if it gets here, have popped everything enclosed in parentheses
                //off the ops stack and added into the output list
                break;

                default: //case UNOP OR BINOP
                if (i != 0) {
                    if (t.getVal() == Token.Value.BINOP && infix.get(i-1).getVal() == Token.Value.BINOP) {
                        //two binary operators in a row means bad user input
                        //number before a binary operator means bad user input
                        return null;
                    }
                    if (t.getVal() == Token.Value.UNOP && infix.get(i-1).getVal() == Token.Value.NUM) {
                        return null;
                    }
                }
                if (ops.empty()) {
                    ops.push(t);
                }
                else {
                    Token top = ops.peek();
                    if (top.getVal() == Token.Value.LPAREN) {
                        ops.push(t);
                    }
                    else {//another op at top of the ops stack -- figure out which one goes first
                        int prec = comparePrecedence(t,top);
                        while ((prec == 1 && isRightAssoc(t)) || (!isRightAssoc(t) && prec > 0)) {
                            output.add(top);
                            ops.pop();
                            if (ops.empty()) break;
                            top = ops.peek();
                            prec = comparePrecedence(t,top);
                        }
                        ops.push(t); //finally t's turn to be pushed to top of ops stack
                    }
                }
                break;
            }
        }
        while (!ops.empty()) {
            Token leftover = ops.pop();
            if (leftover.getVal() == Token.Value.UNOP || leftover.getVal() == Token.Value.BINOP) {
                output.add(leftover);
            }
            else return null;
            //get out now --> bad user input
        }
        
        if (output.isEmpty()) return null;
        else if (output.size() == 1 && (output.get(0).getVal() == Token.Value.UNOP || output.get(0).getVal() == Token.Value.BINOP)) {
            return null;
        }
        return output;
    }

    private boolean isRightAssoc(Token t) {
        return /*t.getString().equals("POW") ||*/ t.getString().equals("SQRT");
        //if not right associative, it's left associative (this function only used on known OP tokens)
    }

    private int comparePrecedence(Token t1, Token t2) {
        //only used for operator tokens
        //returns 0 if t1 higher precedence, 1 if t2 higher precedence, 2 if same precedence
        return t1.getPrecedence() < t2.getPrecedence() ? 0 : t1.getPrecedence() > t2.getPrecedence() ? 1 : 2;
    }

}
