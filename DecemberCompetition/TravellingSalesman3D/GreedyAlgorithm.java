import java.util.*;

public class GreedyAlgorithm {
    public static ArrayList<ArrayList<DecemberCompetition.Point>> solveAllProblems (ArrayList<ArrayList<DecemberCompetition.Point>> problems) {
        ArrayList<ArrayList<DecemberCompetition.Point>> solutions = new ArrayList<ArrayList<DecemberCompetition.Point>>();
        for (ArrayList<DecemberCompetition.Point> points : problems) {
            solutions.add(solveProblem(points));
        }
        return solutions;
    }
    public static ArrayList<DecemberCompetition.Point> solveProblem (ArrayList<DecemberCompetition.Point> points) {
        ArrayList<Integer> visited = new ArrayList<Integer>();
        ArrayList<DecemberCompetition.Point> solution = new ArrayList<DecemberCompetition.Point>();
        //might as well start at the first point
        int currPoint = 0;
        visited.add(currPoint);
        solution.add(points.get(currPoint));

        while (visited.size() < points.size()) {
            int nextPoint = findNearestPoint(points, visited, currPoint);
            solution.add(points.get(nextPoint));
            visited.add(nextPoint);
            currPoint = nextPoint;
        }
        return solution;
    }

    public static int findNearestPoint (ArrayList<DecemberCompetition.Point> points, ArrayList<Integer> visited, int start) {
        DecemberCompetition.Point startP = points.get(start);
        double minDist = Double.MAX_VALUE; //ain't no distance greater than this
        int minIndex = -1; //ain't no index smaller than or equal to this

        for (int i = 0; i < points.size(); i++) {
            if (visited.contains(i) || start == i) {
                continue;
            }
            double currDist = PathMeasure.getDistance(points.get(i), startP);
            if (i == 0 || currDist < minDist) {
                minDist = currDist;
                minIndex = i;
            }
        }
        return minIndex;
        //only time this will return -1 is if salesman has "visited" every point already
        //but I wouldn't call the method anyway in that situation
    }
}