import java.util.*;
public class GreedyBrute
{
    public static ArrayList<ArrayList<Point>> solveAllProblems (ArrayList<ArrayList<Point>> problems, int bruteLim, double[][][] dists) {
        ArrayList<ArrayList<Point>> solutions = new ArrayList<ArrayList<Point>>();
        for (int i = 0; i < problems.size(); i++) {
            solutions.add(solveProblem(problems.get(i), bruteLim, i, dists[i]));
        }
        return solutions;
    }

    public static ArrayList<Point> solveProblem (ArrayList<Point> points, int bruteLim, int pNum, double[][] dists) {
        ArrayList<Point> greedyOutput = Greedy.solveProblem(points, pNum, 0, dists);
        ArrayList<Point> bruteOutput = Brute.bruteSection(greedyOutput, points.size()-bruteLim, bruteLim, pNum, dists);
        return bruteOutput;
    }
}