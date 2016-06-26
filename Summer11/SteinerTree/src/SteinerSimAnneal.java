/**
 * Created by njk on 6/10/16.
 */
import java.util.*;
public class SteinerSimAnneal implements Algorithm {
    private static final String startingSolutionFileName = "trees/" + Utils.bestSolutionFileName();
    private double STARTING_TEMP = 10;
    private static final double MIN_TEMP = 0.0001;
    private static final double TEMP_FACTOR = 0.999;
    private static final long RAND_SEED = System.currentTimeMillis();
    private final long maxMillis;
    private static final Prim mstAlgo = new Prim();
    private static final Random randGen = new Random(RAND_SEED);
    private final boolean startBest;
    private final boolean hillClimb;
    public SteinerSimAnneal(double maxMin, boolean startBest, boolean hillClimb) {
        maxMillis = (long)(60000 * maxMin);
        this.startBest = startBest;
        this.hillClimb = hillClimb;
        if (startBest) STARTING_TEMP = MIN_TEMP;
    }
    public ArrayList<Line> makeTree(ArrayList<Point> points) {
        if (startBest) {
            points = Utils.loadSolutionPoints(startingSolutionFileName);
        } //otherwise, we simply use the parameter points list

        double currScore = Utils.scoreTree(mstAlgo.makeTree(points));
        double temp = STARTING_TEMP;
        long startTime = System.currentTimeMillis();

        double allTimeBestScore = currScore;
        ArrayList<Point> allTimeBestGraph = new ArrayList<>(points);

        ShutdownHook currHook = new ShutdownHook(allTimeBestGraph);
        Runtime.getRuntime().addShutdownHook(currHook);

        System.out.println("Starting Score = " + currScore);

        while ((System.currentTimeMillis() - startTime) < maxMillis) {
            ArrayList<Point> ng = neighborGraph(points);
            ArrayList<Line> ngTree = mstAlgo.makeTree(ng);

            if (ngTree == null) continue;

            double ngScore = Utils.scoreTree(ngTree);
            if (Math.random() < prob(currScore, ngScore, temp)) {
                points = ng;
                currScore = ngScore;
                System.out.println("Change Accepted - Score = " + currScore);
            } /*else {
                System.out.println("Change Rejected - Proposed Score was " + ngScore);
            }*/
            if (temp > MIN_TEMP) {
                temp *= TEMP_FACTOR;
            }
            if (currScore < allTimeBestScore) {
                allTimeBestScore = currScore;
                allTimeBestGraph = new ArrayList<>(points);
                Runtime.getRuntime().removeShutdownHook(currHook);
                currHook = new ShutdownHook(allTimeBestGraph);
                Runtime.getRuntime().addShutdownHook(currHook);
            }
        }

        return mstAlgo.makeTree(allTimeBestGraph);
    }
    private static ArrayList<Point> neighborGraph(ArrayList<Point> points) {
        ArrayList<Point> copyPoints = new ArrayList<>(points);
        if (copyPoints.size() == 1998) {
            //1/2 chance we delete, 1/2 chance we replace
            if (randGen.nextDouble() < 0.5) {
                deletePoint(copyPoints);
            } else {
                replacePoint(copyPoints);
            }
        } else if (copyPoints.size() == 1000) {
            //can only add
            addPoint(copyPoints);
        } else {
            //1/3 chance we add, 1/3 chance we delete, 1/3 chance we replace
            double rand = randGen.nextDouble();
            if (rand < 1.0 / 3.0) {
                addPoint(copyPoints);
            } else if (rand >= 2.0 / 3.0) {
                deletePoint(copyPoints);
            } else {
                replacePoint(copyPoints);
            }
        }
        return copyPoints;
    }
    //pick three random points and add the steiner point
    //then reform the MST
    private static void addPoint(ArrayList<Point> points) {
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
        points.add(getFermatPoint(points.get(rand1), points.get(rand2), points.get(rand3)));
    }
    //pick one random steiner point and remove it
    //then reform the MST
    private static void deletePoint(ArrayList<Point> points) {
        //System.out.print("Deleting Point --> ");
        int randSteinerPoint = randGen.nextInt(points.size() - 1000) + 1000;
        points.remove(randSteinerPoint);
    }
    //perform add, then perform delete
    private static void replacePoint(ArrayList<Point> points) {
        deletePoint(points);
        addPoint(points);
    }
    private static Point getFermatPoint(Point p1, Point p2, Point p3) {
        double[] angles = getAngles(p1, p2, p3);
        double deg120 = 2.0 * Math.PI / 3.0;
        double deg30 = Math.PI / 6.0;
        if (angles[0] >= deg120) return new Point(p1.getX() + 0.001, p1.getY()+0.001);
        if (angles[1] >= deg120) return new Point(p2.getX() + 0.001, p2.getY()+0.001);
        if (angles[2] >= deg120) return new Point(p3.getX() + 0.001, p3.getY()+0.001);

        double x = 1.0 / Math.cos(angles[0] - deg30);
        double y = 1.0 / Math.cos(angles[1] - deg30);
        double z = 1.0 / Math.cos(angles[2] - deg30);

        double a = Utils.dist(p2, p3);
        double b = Utils.dist(p1, p3);
        double c = Utils.dist(p1, p2);

        double d = a*x + b*y + c*z;

        double fx = (a*x/d) * p1.getX() + (b*y/d) * p2.getX() + (c*z/d) * p3.getX();
        double fy = (a*x/d) * p1.getY() + (b*y/d) * p2.getY() + (c*z/d) * p3.getY();

        return new Point(fx, fy);
    }
    private static double[] getAngles(Point a, Point b, Point c) {
        //returns the three interior angles of triangle formed by three points
        //angle measures are in RADIANS
        double adot = (b.getX()-a.getX())*(c.getX()-a.getX()) + (b.getY()-a.getY())*(c.getY()-a.getY());
        double bdot = (c.getX()-b.getX())*(a.getX()-b.getX()) + (c.getY()-b.getY())*(a.getY()-b.getY());
        double cdot = (a.getX()-c.getX())*(b.getX()-c.getX()) + (a.getY()-c.getY())*(b.getY()-c.getY());
        double dist1 = Utils.dist(a,b);
        double dist2 = Utils.dist(a,c);
        double dist3 = Utils.dist(b,c);
        double angleA = Math.acos(adot / (dist1 * dist2));
        double angleB = Math.acos(bdot / (dist1 * dist3));
        double angleC = Math.acos(cdot / (dist2 * dist3));
        return new double[]{angleA, angleB, angleC};
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
    private double prob(double oldScore, double newScore, double temp) {
        if (hillClimb) {
            return newScore < oldScore? 1 : 0;
        } else {
            return newScore < oldScore ? 1 : Math.exp(-(newScore - oldScore) / temp);
        }
    }
}