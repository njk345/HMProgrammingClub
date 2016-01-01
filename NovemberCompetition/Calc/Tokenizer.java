import java.util.*;

public class Tokenizer {
    private final String[] keys = {"ADD", "SUB", "MUL", "DIV", "POW", "SQRT", "PI", "E"};

    public Tokenizer() {
        //blank constructor
    }

    public ArrayList<Token> tokenize(String input) { 
        ArrayList<Token> rv = new ArrayList<Token>();
        String[] split = splitUp(input);
        boolean needRightParen = false;
        
        if (split == null) return null;
        
        for (int i = 0; i < split.length; i++) {
            if (split[i].equals("")) break;
            if (split[i].equals("-")) {
                //negative sign means add left paren, add -1, add MUL, add next number, add right paren
                rv.add(new Token("("));
                rv.add(new Token("-1"));
                rv.add(new Token("MUL"));
                needRightParen = true;
                i++;
            }
            Token t = new Token(split[i]);
            rv.add(t);
            if (needRightParen) {
                rv.add(new Token(")"));
                needRightParen = false;
            }
        }
        return rv;
    }

    public String[] splitUp(String input) {
        String s = removeSpaces(input).toUpperCase();

        String[] splitup = new String[s.length()];
        Arrays.fill(splitup,"");

        int j = 0; //index inside splitup
        int i = 0;
        while (i < s.length()) {
            char c = s.charAt(i);
            if (Character.isDigit(c)) {
                splitup[j] += c;
                int k = i+1;
                while (k < s.length()) {
                    if (Character.isDigit(s.charAt(k))) {
                        splitup[j] += s.charAt(k);
                        k++;
                    }
                    else break;
                }

                if (k < s.length()) {
                    if (charIsInvalid(s.charAt(k))) return null;
                }
                j++;
                i = k;
            }
            else if (!anyStartingWith(c, keys).isEmpty()) {
                ArrayList<Integer> indices = anyStartingWith(c,keys);

                String sRemaining = s.substring(i);

                for (int k = 0; k < indices.size(); k++) {
                    String keyTerm = keys[indices.get(k)];
                    if (startEqual(sRemaining, keyTerm)) {
                        splitup[j] += keyTerm;
                        j++;
                        i += keyTerm.length();
                        break;
                    }
                }
                if (splitup[j-1].length() == 0) {
                    //if nothing added to splitup yet, no matches
                    //were found in keys array --> bad user input
                    return null;
                }
            }
            else if (c == '(' || c == ')' || c == '-') {
                splitup[j] += c;
                j++;
                i++;
            }
            else { //bad input
                return null;
            }
        }
        splitup = replaceConstants(splitup);

        return splitup;
    }

    private String[] replaceConstants(String[] tokens) {
        String[] rv = new String[tokens.length];
        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i].equals("PI")) {
                rv[i] = Double.toString(Math.PI);
            }
            else if (tokens[i].equals("E")) {
                rv[i] = Double.toString(Math.E);
            }
            else {
                rv[i] = tokens[i];
            }
        }
        return rv;
    }

    private boolean startEqual(String s1, String s2) {
        //takes two strings, returns true if s1 contains s2, starting at index 0 in s1
        if (s1.length() < s2.length()) return false;
        return s1.substring(0,s2.length()).equals(s2);
    }

    private boolean charIsInvalid(char c) {
        return (anyStartingWith(c,keys).isEmpty() && !Character.isDigit(c) && c != '(' && c != ')');
    }

    private String removeSpaces(String s) {
        String rv = "";
        for (int i = 0; i < s.length(); i++) {
            if (!s.substring(i,i+1).equals(" ")) {
                rv += s.substring(i, i+1);
            }
        }
        return rv;
    }

    private ArrayList<Integer> anyStartingWith(char c, String[] words) {
        ArrayList<Integer> indices = new ArrayList<Integer>();      
        for (int i = 0; i < words.length; i++) {
            if (words[i].charAt(0) == c) {
                indices.add(i);
            }
        }
        return indices;
    }

    //this method non-essential to the program; for testing purposes
    private void printArray(String[] a) {
        if (a == null) System.out.println("NULL");
        else {
            for (int i = 0; i < a.length; i++) {
                System.out.print(a[i] + " ");
            }
            System.out.print("\n");
        }
    }
}