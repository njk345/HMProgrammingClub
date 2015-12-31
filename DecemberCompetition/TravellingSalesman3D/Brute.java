import java.util.*;
public class Brute
{
    public static ArrayList<Point> bruteMultSections(ArrayList<Point> points, ArrayList<ArrayList<Integer>> sections, int pNum, double[][] dists) {
        ArrayList<Point> currSolution = new ArrayList<Point>(points);
        for (int i = 0; i < sections.size(); i++) {
            ArrayList<Point> newSolution = new ArrayList<Point>();
            newSolution = bruteSection(currSolution, sections.get(i).get(0), sections.get(i).get(1), pNum, dists);
            currSolution = newSolution;
        }
        return currSolution;
    }

    public static ArrayList<Point> bruteSection(ArrayList<Point> points, int start, int len, int pNum, double[][] dists) {
        ArrayList<Point> rv = new ArrayList<Point>();
        //add points up to the section to be brute-forced
        for (int i = 0; i < start; i++) {
            rv.add(points.get(i));
        }
        //add points in section to be bruteforced to separate list
        ArrayList<Point> subset = new ArrayList<Point>();
        for (int i = start; i < start + len; i++) {
            subset.add(points.get(i));
        }
        Point pBefore;
        Point pAfter;
        if (start + len == points.size()) {
            pAfter = points.get(0);
        } else {
            pAfter = points.get(start + len);
        }
        if (start == 0) {
            pBefore = null;
        } else {
            pBefore = points.get(start - 1);
        }
        //brute force it
        ArrayList<Point> bestSubsetPath = bruteForce(subset, pBefore, pAfter, len, pNum, dists);
        //add the brute forced points to the solution set
        for (int i = 0; i < bestSubsetPath.size(); i++) {
            rv.add(bestSubsetPath.get(i));
        }
        //add the rest of the points to the solution set
        for (int i = start + len; i < points.size(); i++) {
            rv.add(points.get(i));
        }
        return rv;
    }

    public static ArrayList<Point> bruteForce (ArrayList<Point> points, Point pStart, Point pBefore, int bruteLim, int pNum, double[][] dists) {
        //size of points input list must be 6 for now
        ArrayList<ArrayList<Integer>> combs = listPermutations(indexList(bruteLim));
        ArrayList<ArrayList<Point>> pCombs = new ArrayList<ArrayList<Point>>();

        for (int i = 0; i < combs.size(); i++) 
        {
            ArrayList<Point> path = new ArrayList<Point>();
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
        ArrayList<Point> bestPath = null;
        for (int i = 0; i < pCombs.size(); i++)
        {
            double len = PathMeasure.evalPathLen(pCombs.get(i), pStart, pBefore, dists);
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
