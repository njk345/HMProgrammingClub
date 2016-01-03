import java.util.*;
public class Rand
{
    public static Random r = new Random();
    public static final String myName = "Nick Keirstead";
    public static ArrayList<ArrayList<String>> solve (ArrayList<String> problem, int roomSize) {
        if (problem.size() % roomSize != 0) {
            throw new RuntimeException("Cannot Do Random With Room Size " + roomSize);
        }
        ArrayList<String> probCopy = new ArrayList<String>(problem);
        ArrayList<ArrayList<String>> solution = new ArrayList<ArrayList<String>>();
        while (probCopy.size() > 0) {
            ArrayList<String> room = new ArrayList<String>();
            for (int i = 0; i < roomSize; i++) {
                int rand = r.nextInt(probCopy.size());
                room.add(probCopy.get(rand));
                probCopy.remove(rand);
            }
            solution.add(room);
        }
        return solution;
    }
    public static ArrayList<ArrayList<String>> loopSolve (ArrayList<String> problem, int roomSize, int target) {
        int bestScore = -1;
        ArrayList<ArrayList<String>> bestSolution = null;
        while (bestScore < target) {
            ArrayList<ArrayList<String>> solution = solve(problem, roomSize);
            int currScore = Score.scoreProblem(solution);
            if (currScore > bestScore) {
                bestScore = currScore;
                bestSolution = solution;
                System.out.println("New High Score = " + bestScore);
                FileUtils.outputIfBest(myName, bestSolution, 2);
            }
        }
        return bestSolution;
    }
}
