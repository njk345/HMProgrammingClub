//Nick Keirstead
import java.util.*;
public class RegExp
{
    public static void main(String[] args) {
        System.out.print("\f");
        ArrayList<String> problems = getProblems();
        System.out.println(problems);
        ArrayList<ArrayList<String>> solutions = solveProblems(problems);
        outputSolutions(solutions);
    }
    public static ArrayList<String> getProblems() {
        ArrayList<String> problems = new ArrayList<String>();
        Scanner s = new Scanner(System.in);
        System.out.println("Enter Each of Character Strings (Comma-Separated, spaces don't matter):");
        String[] strs = s.nextLine().split(",");
        for (int i = 0; i < strs.length; i++) {
            problems.add(strs[i].trim());
        }
        for (int i = 1; i <= 5; i++) {
            System.out.println("Enter Reg. Exp. " + i + ": ");
            problems.add(s.nextLine().trim());
        }
        return problems;
    }
    public static ArrayList<ArrayList<String>> solveProblems(ArrayList<String> problems) {
        ArrayList<ArrayList<String>> solutions = new ArrayList<ArrayList<String>>();
        ArrayList<String> strs = new ArrayList<String>();
        for (int i = 0; i < 10; i++) {
            strs.add(problems.get(i));
        }
        for (int i = 10; i < problems.size(); i++) {
            ArrayList<String> allMatches = findMatches(strs, problems.get(i));
            solutions.add(allMatches);
        }
        return solutions;
    }
    public static ArrayList<String> findMatches(ArrayList<String> strs, String re) {
        ArrayList<String> matches = new ArrayList<String>();
        for (String s: strs) {
            if (matches(s, re)) {
                matches.add(s);
            }
        }
        return matches;
    }
    public static boolean matches(String s, String re) {
        return true;
    }
    public static void outputSolutions(ArrayList<ArrayList<String>> solutions) {
        for (int i = 0; i < solutions.size(); i++) {
            System.out.println("" + (i+1) + ". " + solutions.toString());
        }
    }
}
