import java.util.*;
public class GreedyBrute
{
    public static ArrayList<ArrayList<Point>> solveAllProblems (ArrayList<ArrayList<Point>> problems, int bruteLim) {
        ArrayList<ArrayList<Point>> solutions = new ArrayList<ArrayList<Point>>();
        for (ArrayList<Point> points : problems) {
            solutions.add(solveProblem(points, bruteLim));
        }
        return solutions;
    }

    public static ArrayList<Point> solveProblem (ArrayList<Point> points, int bruteLim) {
        ArrayList<Point> greedyOutput = Greedy.solveProblem(points);
        ArrayList<Point> bruteOutput = Brute.bruteSection(greedyOutput, points.size()-bruteLim, bruteLim);
        return bruteOutput;
    }
}