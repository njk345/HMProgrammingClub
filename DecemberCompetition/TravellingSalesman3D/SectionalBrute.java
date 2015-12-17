import java.util.*;
public class SectionalBrute
{
    public static ArrayList<DecemberCompetition.Point> bruteMultSections(ArrayList<DecemberCompetition.Point> points, ArrayList<ArrayList<Integer>> sections) {
        ArrayList<DecemberCompetition.Point> currSolution = new ArrayList<DecemberCompetition.Point>(points);
        for (int i = 0; i < sections.size(); i++) {
            ArrayList<DecemberCompetition.Point> newSolution = new ArrayList<DecemberCompetition.Point>();
            newSolution = bruteSection(currSolution, sections.get(i).get(0), sections.get(i).get(1));
            currSolution = newSolution;
        }
        return currSolution;
    }
    public static ArrayList<DecemberCompetition.Point> bruteSection(ArrayList<DecemberCompetition.Point> points, int start, int len) {
        ArrayList<DecemberCompetition.Point> rv = new ArrayList<DecemberCompetition.Point>();
        //add points up to the section to be brute-forced
        for (int i = 0; i < start; i++) {
            rv.add(points.get(i));
        }
        //add points in section to be bruteforced to separate list
        ArrayList<DecemberCompetition.Point> subset = new ArrayList<DecemberCompetition.Point>();
        for (int i = start; i < start + len; i++) {
            subset.add(points.get(i));
        }
        DecemberCompetition.Point pBefore;
        DecemberCompetition.Point pAfter;
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
        ArrayList<DecemberCompetition.Point> bestSubsetPath = GreedyBruteAlgorithm.bruteForce(subset, pBefore, pAfter, len);
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
}
