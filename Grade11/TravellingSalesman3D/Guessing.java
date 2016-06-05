import java.util.*;
public class Guessing
{
    public static ArrayList<ArrayList<Point>> solveAllProblems (ArrayList<ArrayList<Point>> problems) {
        ArrayList<ArrayList<Point>> solutions = new ArrayList<ArrayList<Point>>();
        for (ArrayList<Point> points : problems) {
            solutions.add(solveProblem(points));
        }
        return solutions;
    }

    public static ArrayList<Point> solveProblem (ArrayList<Point> points) {
        ArrayList<Point> solution = new ArrayList<Point>();
        ArrayList<Point> copy = new ArrayList<Point>();
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
