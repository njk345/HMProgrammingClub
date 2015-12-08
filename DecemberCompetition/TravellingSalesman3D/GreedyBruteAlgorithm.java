import java.util.*;
public class GreedyBruteAlgorithm
{
    public static ArrayList<DecemberCompetition.Point> solveProblem (ArrayList<DecemberCompetition.Point> points) {
        ArrayList<Integer> visited = new ArrayList<Integer>();
        ArrayList<DecemberCompetition.Point> solution = new ArrayList<DecemberCompetition.Point>();
        //might as well start at the first point
        int currPoint = 0;
        int maxPoint = points.size();
        visited.add(currPoint);
        solution.add(points.get(currPoint));        

        while (visited.size() < maxPoint - 8) 
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
        ArrayList<DecemberCompetition.Point> bestSubsetPath = bruteForce(subset, points.get(0));

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

    public static ArrayList<DecemberCompetition.Point> bruteForce (ArrayList<DecemberCompetition.Point> points, DecemberCompetition.Point pStart) {
        //size of points input list must be 6 for now
        ArrayList<ArrayList<Integer>> combs = indexCombinations();
        /*int tot = 0;  
        for (int i = 0; i < combs.size(); i++) 
        {
        System.out.println(combs.get(i).toString());
        tot++;
        }*/

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
            double len = PathMeasure.evalPathLen(pCombs.get(i), true, pStart);
            if (len < bestLen) 
            {
                bestLen = len;
                bestPath = pCombs.get(i); 
            }
        }
        return bestPath;
    }

    public static ArrayList<ArrayList<Integer>> indexCombinations () {
        ArrayList<ArrayList<Integer>> combs = new ArrayList<ArrayList<Integer>>();
        //five ways to pick first digit
        int count = 0;
        for (int a = 0; a < 8; a++) 
        {
            for (int b = 0; b < 8; b++) 
            {
                if (b == a) continue;
                for (int c = 0; c < 8; c++) 
                {
                    if (c == a || c == b) continue;
                    for (int d = 0; d < 8; d++)
                    {
                        if (d == a || d == b || d == c) continue;
                        for (int e = 0; e < 8; e++)
                        {
                            if (e == a || e == b || e == c || e == d) continue;
                            for (int f = 0; f < 8; f++)
                            {
                                if (f == a || f == b || f == c || f == d || f == e) continue;
                                for (int g = 0; g < 8; g++) {
                                    if (g == a || g == b || g == c || g == d || g == e || g == f) continue;
                                    for (int h = 0; h < 8; h++) {
                                        if (h == a || h == b || h == c || h == d || h == e || h == f || h == g) continue;
                                        ArrayList<Integer> comb = new ArrayList<Integer>();   
                                        comb.add(a);
                                        comb.add(b);
                                        comb.add(c);
                                        comb.add(d);
                                        comb.add(e);
                                        comb.add(f);
                                        comb.add(g);
                                        comb.add(h);
                                        combs.add(comb);
                                        count++;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        //Recursive integer permutation generator
        //takes in arbitrary number of ints to permute
        /*public static ArrayList<ArrayList<Integer>> indexPermutations (int n) {
        if (n == 0) return new ArrayList<ArrayList<Integer>>(0);

        }*/
        //System.out.println(combs);
        return combs;
    }
}
