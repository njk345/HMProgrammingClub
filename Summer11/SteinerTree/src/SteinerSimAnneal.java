/**
 * Created by njk on 6/10/16.
 */
import java.util.*;
public class SteinerSimAnneal implements Algorithm {
    //private static final String startingSolutionFileName = "trees/" + Utils.bestSolutionFileName();
    private static final double STARTING_TEMP = 10;
    private static final double MIN_TEMP = 0.01;
    private static final double TEMP_FACTOR = 0.999;
    private static final long RAND_SEED = System.currentTimeMillis();
    private double maxMinutes;
    private long maxMillis;
    private static final Prim mstAlgo = new Prim();
    private static final Random randGen = new Random(RAND_SEED);
    public SteinerSimAnneal(double maxMin) {
        maxMinutes = maxMin;
        maxMillis = (long)(60000 * maxMinutes);
    }
    public ArrayList<Line> makeTree(ArrayList<Point> points) {
        //ArrayList<Line> currTree = Utils.loadSolution(startingSolutionFileName);

        double currScore = Utils.scoreTree(mstAlgo.makeTree(points));
        double temp = STARTING_TEMP;
        long startTime = System.currentTimeMillis();

        double allTimeBestScore = currScore;
        ArrayList<Point> allTimeBestGraph = new ArrayList<>(points);

        System.out.println("Starting Score = " + currScore);
        ArrayList<int[]> steinerTrios = new ArrayList<>();

        while ((System.currentTimeMillis() - startTime) < maxMillis) {
            ArrayList<int[]> stCopies = new ArrayList<>(steinerTrios);
            ArrayList<Point> ng = neighborGraph(points, stCopies);
            double ngScore = Utils.scoreTree(mstAlgo.makeTree(ng));
            if (Math.random() < prob(currScore, ngScore, temp)) {
                points = ng;
                currScore = ngScore;
                steinerTrios = stCopies;
                System.out.println("Change Accepted - Score = " + currScore);
            } else {
                System.out.println("Change Rejected - Proposed Score was " + ngScore);
            }
            //System.out.println(" || #points = " + points.size() + ", #trios = " + steinerTrios.size());
            if (temp > MIN_TEMP) {
                temp *= TEMP_FACTOR;
            }
            if (currScore < allTimeBestScore) {
                allTimeBestScore = currScore;
                allTimeBestGraph = new ArrayList<>(points);
            }
        }

        return mstAlgo.makeTree(allTimeBestGraph);
    }
    private static ArrayList<Point> neighborGraph(ArrayList<Point> points, ArrayList<int[]> steinerTrios) {
        ArrayList<Point> copyPoints = new ArrayList<>(points);
        if (copyPoints.size() == 1998) {
            //1/2 chance we delete, 1/2 chance we replace
            if (randGen.nextDouble() < 0.5) {
                deletePoint(copyPoints, steinerTrios);
            } else {
                replacePoint(copyPoints, steinerTrios);
            }
        } else if (copyPoints.size() == 1000) {
            //can only add
            addPoint(copyPoints, steinerTrios);
        } else {
            //1/3 chance we add, 1/3 chance we delete, 1/3 chance we replace
            double rand = randGen.nextDouble();
            if (rand < (double) 1 / 3) {
                addPoint(copyPoints, steinerTrios);
            } else if (rand >= (double) 2 / 3) {
                deletePoint(copyPoints, steinerTrios);
            } else {
                replacePoint(copyPoints, steinerTrios);
            }
        }
        return copyPoints;
    }
    //pick three random points and add the steiner point
    //then reform the MST
    private static void addPoint(ArrayList<Point> points, ArrayList<int[]> steinerTrios) {
        //System.out.print("Adding Point --> ");
        //pick 3 distinct random indices out of points list
        int rand1 = randGen.nextInt(points.size());
        int rand2 = rand1;
        while (rand2 == rand1) {
            rand2 = randGen.nextInt(points.size());
        }
        int rand3 = rand2;
        while (rand3 == rand2 || rand3 == rand1) {
            rand3 = randGen.nextInt(points.size());
        }
        points.add(getSteinerPoint(points.get(rand1), points.get(rand2), points.get(rand3)));
        //steinerTrios.add(new int[]{rand1, rand2, rand3});
    }
    //pick one random steiner point and remove it
    //then reform the MST
    private static void deletePoint(ArrayList<Point> points, ArrayList<int[]> steinerTrios) {
        //System.out.print("Deleting Point --> ");
        int randSteinerPoint = randGen.nextInt(points.size() - 1000) + 1000;
        points.remove(randSteinerPoint);
        //steinerTrios.remove(randSteinerPoint - 1000);
    }
    //perform add, then perform delete
    private static void replacePoint(ArrayList<Point> points, ArrayList<int[]> steinerTrios) {
        deletePoint(points, steinerTrios);
        addPoint(points, steinerTrios);
    }
    private static Point getSteinerPoint(Point p1, Point p2, Point p3) {
        //side lengths
        double a = Utils.dist(p2, p3);
        double b = Utils.dist(p1, p3);
        double c = Utils.dist(p1, p2);

        //trilinear coordinates
        double tx = b*c / (b*b - c*c);
        double ty = a*c / (c*c - a*a);
        double tz = a*b / (a*a - b*b);

        double denom = a*tx + b*ty + c*tz;

        double x = p1.getX() * (a*tx / denom) + p2.getX() * (b*ty / denom) + p3.getX() * (c*tz / denom);
        double y = p1.getY() * (a*tx / denom) + p2.getY() * (b*ty / denom) + p3.getY() * (c*tz / denom);
        return new Point(x, y);
    }
    private static double prob(double oldScore, double newScore, double temp) {
        return newScore < oldScore? 1 : Math.exp(-(newScore - oldScore) / temp);
    }
    private static boolean trioUsed(ArrayList<int[]> steinerTrios, int[] trio) {
        for (int[] t : steinerTrios) {
            if (trio[0] == t[0] && trio[1] == t[1] && trio[2] == t[2]) return true;
        }
        return false;
    }
}