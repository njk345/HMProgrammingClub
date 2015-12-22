import java.util.*;
public class OptimizedGreedy
{
    public static ArrayList<ArrayList<Point>> solveAllProblems (ArrayList<ArrayList<Point>> problems) {
        ArrayList<ArrayList<Point>> solutions = new ArrayList<ArrayList<Point>>();
        for (ArrayList<Point> points : problems) {
            solutions.add(solveProblem(points));
        }
        return solutions;
    }

    public static ArrayList<Point> solveProblem (ArrayList<Point> points) {
        double bestDist = Double.MAX_VALUE;
        int bestStart = -1;
        ArrayList<Point> bestSolution = null;
        for (int i = 0; i < points.size(); i++) {
            ArrayList<Point> solution = greedyArbStart(points,i);
            double len = PathMeasure.evalPathLen(solution, null, solution.get(0));
            if (len < bestDist) {
                bestDist = len;
                bestStart = i;
                bestSolution = solution;
            }
        }
        return bestSolution;
    }

    public static ArrayList<Point> greedyArbStart(ArrayList<Point> points, int start) {
        ArrayList<Point> pCopies = new ArrayList<Point>();
        Point startP = points.get(start);
        pCopies.add(startP);
        for (int i = 0; i < points.size(); i++) {
            if (i != start) {
                pCopies.add(points.get(i));
            }
        }
        ArrayList<Point> rvs = Greedy.solveProblem(pCopies);
        return rvs;
    }
}
