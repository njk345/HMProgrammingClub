import java.util.*;
public class GreedyBruteAlgorithm
{
    public static ArrayList<ArrayList<DecemberCompetition.Point>> solveAllProblems (ArrayList<ArrayList<DecemberCompetition.Point>> problems, int bruteLim) {
        ArrayList<ArrayList<DecemberCompetition.Point>> solutions = new ArrayList<ArrayList<DecemberCompetition.Point>>();
        for (ArrayList<DecemberCompetition.Point> points : problems) {
            solutions.add(solveProblem(points, bruteLim));
        }
        return solutions;
    }
    public static ArrayList<DecemberCompetition.Point> solveProblem (ArrayList<DecemberCompetition.Point> points, int bruteLim) {
        ArrayList<Integer> visited = new ArrayList<Integer>();
        ArrayList<DecemberCompetition.Point> solution = new ArrayList<DecemberCompetition.Point>();
        ArrayList<DecemberCompetition.Point> regGreedy = GreedyAlgorithm.solveProblem(points);

        //might as well start at the first point
        int currPoint = 0;
        visited.add(currPoint);
        solution.add(points.get(currPoint));

        while (visited.size() < points.size() - bruteLim) 
        {
            int nextPoint = findNearestPoint(points, visited, currPoint);
            solution.add(points.get(nextPoint));    
            visited.add(nextPoint);
            currPoint = nextPoint;
        }

        ArrayList<DecemberCompetition.Point> subset = new ArrayList<DecemberCompetition.Point>(points);
        for (Iterator<DecemberCompetition.Point> it = subset.iterator(); it.hasNext();) 
        {
            if (visited.contains(it.next().index)) 
            {
                it.remove();    
            }
        }
        ArrayList<DecemberCompetition.Point> bestSubsetPath = bruteForce(subset, points.get(0), solution.get(solution.size() - 1), bruteLim);

        for (int i = 0; i < bestSubsetPath.size(); i++) 
        {
            solution.add(bestSubsetPath.get(i));
        }
        return solution;
    }

    public static int findNearestPoint (ArrayList<DecemberCompetition.Point> points, ArrayList<Integer> visited, int start) {
        DecemberCompetition.Point startP = points.get(start);
        double minDist = Double.MAX_VALUE; //ain't no distance greater than this
        int minIndex = -1; //ain't no index smaller than or equal to this

        for (int i = 0; i < points.size(); i++) 
        {   
            if (visited.contains(i) || start == i) 
            {
                continue;
            }
            double currDist = PathMeasure.getDistance(points.get(i), startP);
            if (i == 0 || currDist < minDist) 
            {
                minDist = currDist;   
                minIndex = i;
            }
        }
        return minIndex;            
        //only time this will return -1 is if salesman has "visited" every point already
        //but I wouldn't call the method anyway in that situation
    }

    public static ArrayList<DecemberCompetition.Point> bruteForce (ArrayList<DecemberCompetition.Point> points, DecemberCompetition.Point pStart, DecemberCompetition.Point pBefore, int bruteLim) {
        //size of points input list must be 6 for now
        ArrayList<ArrayList<Integer>> combs = listPermutations(indexList(bruteLim));
        ArrayList<ArrayList<DecemberCompetition.Point>> pCombs = new ArrayList<ArrayList<DecemberCompetition.Point>>();

        for (int i = 0; i < combs.size(); i++) 
        {
            ArrayList<DecemberCompetition.Point> path = new ArrayList<DecemberCompetition.Point>();
            for (int x = 0; x < combs.get(i).size(); x++) 
            {
                int index = combs.get(i).get(x);
                path.add(points.get(index));
            }
            pCombs.add(path);
        }
        //pCombs now filled with every possible combination of points
        //now find the point combination with smallest distance and return that one

        double bestLen = Double.MAX_VALUE;
        ArrayList<DecemberCompetition.Point> bestPath = null;
        for (int i = 0; i < pCombs.size(); i++)
        {
            double len = PathMeasure.evalPathLen(pCombs.get(i), true, pStart, pBefore);
            if (len < bestLen)
            {
                bestLen = len;
                bestPath = pCombs.get(i);
            }
        }
        return bestPath;
    }

    public static ArrayList<ArrayList<Integer>> listPermutations(ArrayList<Integer> list) {
        if (list.size() == 0) {
            ArrayList<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>();
            result.add(new ArrayList<Integer>());
            return result;
        }

        ArrayList<ArrayList<Integer>> returnMe = new ArrayList<ArrayList<Integer>>();

        Integer firstElement = list.remove(0);

        ArrayList<ArrayList<Integer>> recursiveReturn = listPermutations(list);
        for (ArrayList<Integer> li : recursiveReturn) {
            for (int index = 0; index <= li.size(); index++) {
                ArrayList<Integer> temp = new ArrayList<Integer>(li);
                temp.add(index, firstElement);
                returnMe.add(temp);
            }
        }
        return returnMe;
    }

    public static ArrayList<Integer> indexList(int n) {
        ArrayList<Integer> rvs = new ArrayList<Integer>();
        for (int i = 0; i < n; i++) {
            rvs.add(i);
        }
        return rvs;
    }
}
