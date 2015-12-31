import java.util.*;
public class PathMeasure
{
    public static final ArrayList<ArrayList<Point>> problems = FileUtils.getProblems();
    
    public static int evalAlgLen(ArrayList<ArrayList<Point>> solutions) {
        double sum = 0;
        for (int i = 0; i < solutions.size(); i++) {
            double[][] dists = new double[200][200];
            fillDistsMatrix(dists, i);
            sum += evalPathLen(solutions.get(i), null, solutions.get(i).get(0), dists);
        }
        return (int)sum;
    }

    public static double evalPathLen(ArrayList<Point> points, Point before, Point after, double[][] distances) {
        double sum = 0;
        for (int i = 0; i < points.size() - 1; i++) {
            sum += distances[points.get(i).index][points.get(i+1).index];
        }
        if (before != null) {
            sum += distances[points.get(0).index][before.index];
        }
        if (after != null) {
            sum += distances[points.get(points.size()-1).index][after.index];
        }
        return sum;
    }
    
    public static ArrayList<ArrayList<Point>> allIndicesToPoints (int[][] indices, boolean isAntColony) {
        ArrayList<ArrayList<Point>> rvs = new ArrayList<ArrayList<Point>>();
        int lim = isAntColony? indices.length - 1 : indices.length;
        for (int i = 0; i < lim; i++) {
            ArrayList<Point> toFill = new ArrayList<Point>();
            ArrayList<Point> points = problems.get(i);
            for (int j = 0; j < indices[i].length; j++) {
                toFill.add(points.get(indices[i][j]));
            }
            rvs.add(points);
        }
        return rvs;
    }
    
    public static ArrayList<Point> indicesToPoints (int[] indices, int pNum) {
        ArrayList<Point> points = problems.get(pNum);
        ArrayList<Point> rvs = new ArrayList<Point>();
        for (int i = 0; i < indices.length; i++) {
            rvs.add(points.get(indices[i]));
        }
        return rvs;
    }
    
    public static void fillProbMatrices(double[][][] allDists, int numProbs) {
        for (int i = 0; i < numProbs; i++) {
            fillDistsMatrix(allDists[i], i);
        }
    }
    
    public static void fillDistsMatrix(double[][] dists, int pNum) {
        ArrayList<Point> points = problems.get(pNum);
        int size = points.size();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                dists[i][j] = points.get(i).getDistance(points.get(j));
            }
        }
    }
}
