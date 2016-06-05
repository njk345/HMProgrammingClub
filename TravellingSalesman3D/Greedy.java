import java.util.*;

public class Greedy {
    public static ArrayList<ArrayList<Point>> solveAllProblems (ArrayList<ArrayList<Point>> problems, double[][][] dists) {
        ArrayList<ArrayList<Point>> solutions = new ArrayList<ArrayList<Point>>();
        for (int i = 0; i < problems.size(); i++) {
            solutions.add(solveProblem(problems.get(i), i, 0, dists[i]));
        }
        return solutions;
    }

    public static ArrayList<Point> solveProblem (ArrayList<Point> points, int pNum, int start, double[][] dists) {
        PathMeasure.fillDistsMatrix(dists, pNum);
        boolean[] visited = new boolean[points.size()];
        int[] path = new int[points.size()];
        int numVisited;
        for (int i = 0; i < visited.length; i++) {
            visited[i] = false;
        }
        ArrayList<Point> solution = new ArrayList<Point>();
        
        //might as well start at the first point
        int currPoint = start;
        visited[currPoint] = true;
        path[0] = currPoint;
        numVisited = 1;

        while (numVisited < 200) {
            int nextPoint = findNearestPoint(currPoint, visited, dists);
            path[numVisited] = nextPoint;
            visited[nextPoint] = true;
            currPoint = nextPoint;
            numVisited++;
        }
        return PathMeasure.indicesToPoints(path, pNum);
    }

    public static int findNearestPoint (int currPoint, boolean[] visited, double[][] dists) {
        double minDist = Double.MAX_VALUE; //ain't no distance greater than this
        int closestPoint = -1; //ain't no index smaller than or equal to this

        for (int i = 0; i < dists.length; i++) {
            if (!visited[i]) {
                double dist = dists[currPoint][i];
                if (dist < minDist) {
                    minDist = dist;
                    closestPoint = i;
                }
            }
        }
        return closestPoint;
    }
}