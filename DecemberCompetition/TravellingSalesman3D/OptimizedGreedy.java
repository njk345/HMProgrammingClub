import java.util.*;
public class OptimizedGreedy
{
    public static ArrayList<ArrayList<DecemberCompetition.Point>> solveAllProblems (ArrayList<ArrayList<DecemberCompetition.Point>> problems) {
        ArrayList<ArrayList<DecemberCompetition.Point>> solutions = new ArrayList<ArrayList<DecemberCompetition.Point>>();
        for (ArrayList<DecemberCompetition.Point> points : problems) {
            solutions.add(solveProblem(points));
        }
        return solutions;
    }
    public static ArrayList<DecemberCompetition.Point> solveProblem (ArrayList<DecemberCompetition.Point> points) {
        double bestDist = Double.MAX_VALUE;
        int bestStart = -1;
        ArrayList<DecemberCompetition.Point> bestSolution = null;
        for (int i = 0; i < points.size(); i++) {
            ArrayList<DecemberCompetition.Point> solution = greedyArbStart(points,i);
            double len = PathMeasure.evalPathLen(solution, false, null, null);
            if (len < bestDist) {
                bestDist = len;
                bestStart = i;
                bestSolution = solution;
            }
        }
        return bestSolution;
    }
    public static ArrayList<DecemberCompetition.Point> greedyArbStart(ArrayList<DecemberCompetition.Point> points, int start) {
        ArrayList<DecemberCompetition.Point> pCopies = new ArrayList<DecemberCompetition.Point>();
        DecemberCompetition.Point startP = points.get(start);
        pCopies.add(startP);
        for (int i = 0; i < points.size(); i++) {
            if (i != start) {
                pCopies.add(points.get(i));
            }
        }
        ArrayList<DecemberCompetition.Point> rvs = GreedyAlgorithm.solveProblem(pCopies);
        return rvs;
    }
}
