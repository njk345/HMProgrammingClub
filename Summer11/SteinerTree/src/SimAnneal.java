/**
 * Created by njk on 6/8/16.
 */
import java.util.*;
public class SimAnneal implements Algorithm {
    private static final String startingSolutionFileName = "trees/" + Utils.bestSolutionFileName();
    private static final double STARTING_TEMP = 20;
    private static final double MIN_TEMP = 0.01;
    private static final double TEMP_FACTOR = 0.999;
    private double maxMinutes;
    private long maxMillis;
    public SimAnneal(double maxMin) {
        maxMinutes = maxMin;
        maxMillis = (long)(60000 * maxMinutes);
    }

    public ArrayList<Line> makeTree(ArrayList<Point> points) {
        ArrayList<Line> currTree = Utils.loadSolution(startingSolutionFileName);
        double currScore = Utils.scoreTree(currTree);
        double temp = STARTING_TEMP;
        long startTime = System.currentTimeMillis();
        System.out.println("Starting Score = " + currScore);

        while ((System.currentTimeMillis() - startTime) < maxMillis) {
            ArrayList<Line> nt = neighborTree(currTree);
            double ntScore = Utils.scoreTree(nt);
            double probability = prob(currScore, ntScore, temp);
            if (Math.random() < probability)  {
                currTree = nt;
                currScore = ntScore;
                System.out.println("Solution Updated - Score = " + currScore);
            }
            if (temp > MIN_TEMP) {
                temp *= TEMP_FACTOR;
            }
        }
        return currTree;
    }
    private static ArrayList<Line> neighborTree(ArrayList<Line> tree) {
        ArrayList<Point> points = new ArrayList<>();
        for (int i = 0; i < tree.size(); i++) {
            //make COPIES of the points
            points.add(tree.get(i).getP1());
        }
        points.add(tree.get(tree.size() - 1).getP2());
        //now swap two random points
        int rand1 = (int)(Math.random() * points.size());
        int rand2 = (int)(Math.random() * points.size());
        Point temp = points.get(rand1);
        points.set(rand1, points.get(rand2));
        points.set(rand2, temp);
        //reconstruct the tree
        ArrayList<Line> nt = new ArrayList<>();
        for (int i = 0; i < points.size() - 1; i++) {
            nt.add(new Line(points.get(i), points.get(i+1)));
        }
        //System.out.println(nt.size());
        return nt;
    }
    private static double prob(double oldScore, double newScore, double temp) {
        return newScore < oldScore? 1 : Math.exp(-(newScore - oldScore) / temp);
    }
}
