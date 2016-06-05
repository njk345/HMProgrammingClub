public class Token {
    public enum Value {
        NUM, LPAREN, RPAREN, BINOP, UNOP
    }

    private String str;
    private Value val;
    private int precedence;

    public Token(String s) {
        str = s;
        try {
            double foo = Double.parseDouble(str);
            //gets to this point if actually a double
            val = Value.NUM;
        }
        catch (NumberFormatException e) {
            if (str.equals("(")) {
                val = Value.LPAREN;
            }
            else if (str.equals(")")) {
                val = Value.RPAREN;
            }
            else {
                val = str.equals("SQRT") ? Value.UNOP : Value.BINOP;
                String type = str;
                if (type.equals("SQRT")) {
                    precedence = 1;
                }
                else if (type.equals("POW")) {
                    precedence = 2;
                }
                else if (type.equals("MUL") || type.equals("DIV")) {
                    precedence = 3;
                }
                else precedence = 4;
            }
        }
    }

    public double getDoubleVal() {
        if (val != Value.NUM) return -1;
        return Double.parseDouble(str);
    }
    public Value getVal () {
        return val;
    }
    public String toString () {
        return val + "." + str;
    }
    public String getString() {
        return str;
    }
    public int getPrecedence() {
        return precedence;
    }
}