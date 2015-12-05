import java.util.*;

public class TravellingSalesman {
	public static void main (String[] args) {
		ArrayList<ArrayList<Point>> problems = DecemberCompetition.getProblems();
		ArrayList<ArrayList<Point>> solutions = solveAllProblems(problems);
		DecemberCompetition.outputSolutionsToFile("Nick Keirstead", solutions);
	}
	public static ArrayList<ArrayList<Point>> solveAllProblems (ArrayList<ArrayList<Point>> problems) {
		ArrayList<ArrayList<Point>> solutions = new ArrayList<ArrayList<Integer>>();
		for (int i = 0; i < problems.size(); i++) {
			solutions.add(solveOneProblem(problems.get(i)));
		}
		return solutions;
	}
	public static ArrayList<Point> solveOneProblem (ArrayList<Point> points) {
		ArrayList<Integer> visited = new ArrayList<Integer>();
		ArrayList<Point> solution = new ArrayList<Integer>();
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

	public static long getDistance (Point p1, Point p2) {
		return Math.pow(p1.x-p2.x,2) + Math.pow(p1.y-p2.y,2) + Math.pow(p1.z-p2.z,2);
	}

	public static int findNearestPoint (ArrayList<Point> points, ArrayList<Integer> visited, int start) {
		Point startP = points.get(start);
		long minDist = -1;
		int minIndex = -1;
		
		for (int i = 0; i < points.size(); i++) {
			if (visited.contains(i) || start == i) {
				continue;
			}
			long currDist = getDistance(points.get(i), startP);
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