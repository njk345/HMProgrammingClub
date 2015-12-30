import java.util.*;
public class OptimizedGreedy
{
    public static ArrayList<ArrayList<Point>> solveAllProblems (ArrayList<ArrayList<Point>> problems, double[][][] dists) {
        ArrayList<ArrayList<Point>> solutions = new ArrayList<ArrayList<Point>>();
        for (int i = 0; i < problems.size(); i++) {
            solutions.add(solveProblem(problems.get(i), i, dists[i]));
        }
        return solutions;
    }

    public static ArrayList<Point> solveProblem (ArrayList<Point> points, int pNum, double[][] dists) {
        double bestDist = Double.MAX_VALUE;
        int bestStart = -1;
        ArrayList<Point> bestSolution = null;
        for (int i = 0; i < points.size(); i++) {
            ArrayList<Point> solution = Greedy.solveProblem(points, pNum, i, dists);
            double len = PathMeasure.evalPathLen(solution, null, solution.get(0), pNum, dists);
            if (len < bestDist) {
                bestDist = len;
                bestStart = i;
                bestSolution = solution;
            }
        }
        return bestSolution;
    }
}
