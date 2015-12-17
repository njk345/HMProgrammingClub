import java.util.*;
public class OptimizedGreedyBrute
{
    public static ArrayList<ArrayList<DecemberCompetition.Point>> solveAllProblems (ArrayList<ArrayList<DecemberCompetition.Point>> problems, int bruteLim) {
        ArrayList<ArrayList<DecemberCompetition.Point>> solutions = new ArrayList<ArrayList<DecemberCompetition.Point>>();
        for (ArrayList<DecemberCompetition.Point> points : problems) {
            solutions.add(solveProblem(points, bruteLim));
        }
        return solutions;
    }
    public static ArrayList<DecemberCompetition.Point> solveProblem (ArrayList<DecemberCompetition.Point> points, int bruteLim) {
        ArrayList<DecemberCompetition.Point> greedyOutput = OptimizedGreedy.solveProblem(points);
        ArrayList<DecemberCompetition.Point> solution = new ArrayList<DecemberCompetition.Point>();
        ArrayList<DecemberCompetition.Point> subset = new ArrayList<DecemberCompetition.Point>();
        int sz = greedyOutput.size();
        DecemberCompetition.Point fp = greedyOutput.get(0);
        DecemberCompetition.Point bp = greedyOutput.get(sz - (bruteLim + 1));
        for (int i = sz - bruteLim; i < sz; i++) {
            subset.add(greedyOutput.get(i));
        }
        ArrayList<DecemberCompetition.Point> bestSubsetPath = GreedyBruteAlgorithm.bruteForce(subset, fp, bp, bruteLim);
        for (int i = 0; i < sz - bruteLim; i++) {
            solution.add(greedyOutput.get(i));
        }
        for (int i = 0; i < bestSubsetPath.size(); i++) {
            solution.add(bestSubsetPath.get(i));
        }
        return solution;
    }
}
