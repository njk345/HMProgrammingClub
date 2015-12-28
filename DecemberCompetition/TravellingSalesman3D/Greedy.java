import java.util.*;

public class Greedy {
    public static ArrayList<ArrayList<Point>> solveAllProblems (ArrayList<ArrayList<Point>> problems) {
        ArrayList<ArrayList<Point>> solutions = new ArrayList<ArrayList<Point>>();
        int times = 0;
        for (ArrayList<Point> points : problems) {
            solutions.add(solveProblem(points));
            times++;
            if (times == 1) break;
        }
        return solutions;
    }

    public static ArrayList<Point> solveProblem (ArrayList<Point> points) {
        ArrayList<Integer> visited = new ArrayList<Integer>();
        ArrayList<Point> solution = new ArrayList<Point>();
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

    public static int findNearestPoint (ArrayList<Point> points, ArrayList<Integer> visited, int start) {
        Point startP = points.get(start);
        double minDist = Double.MAX_VALUE; //ain't no distance greater than this
        int minIndex = -1; //ain't no index smaller than or equal to this

        for (int i = 0; i < points.size(); i++) {
            if (visited.contains(i) || start == i) {
                continue;
            }
            double currDist = points.get(i).getDistance(startP);
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