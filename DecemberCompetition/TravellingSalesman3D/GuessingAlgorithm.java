import java.util.*;
public class GuessingAlgorithm
{
    public static ArrayList<DecemberCompetition.Point> solveProblem (ArrayList<DecemberCompetition.Point> points) {
        ArrayList<DecemberCompetition.Point> solution = new ArrayList<DecemberCompetition.Point>();
        ArrayList<DecemberCompetition.Point> copy = new ArrayList<DecemberCompetition.Point>();
        Random rgen = new Random();
        
        for (int i = 0; i < points.size(); i++) {
            copy.add(points.get(i));
        }

        for (int i = 0; i < 200; i++) {
            long randomSeed = rgen.nextLong();
            rgen.setSeed(randomSeed);
            int rand = rgen.nextInt(copy.size());
            if (copy.isEmpty()) {
                break;
            }
            solution.add(copy.get(rand));
            copy.remove(rand);
        }
        return solution;
    }
}
