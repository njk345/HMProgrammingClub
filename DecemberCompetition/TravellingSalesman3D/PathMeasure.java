import java.util.*;
public class PathMeasure
{
    public static final ArrayList<ArrayList<Point>> points = FileUtils.getProblems();
    public static int evalAlgLen(ArrayList<ArrayList<Point>> solutions) {
        double sum = 0;
        for (int i = 0; i < solutions.size(); i++) {
            sum += evalPathLen(solutions.get(i), null, solutions.get(i).get(0));
        }
        return (int)sum;
    }

    public static double evalPathLen(ArrayList<Point> points, Point before, Point after) {
        double sum = 0;
        for (int i = 1; i < points.size(); i++) {
            sum += points.get(i).getDistance(points.get(i-1));
        }
        if (before != null) {
            sum += points.get(0).getDistance(before);
        }
        if (after != null) {
            sum += points.get(points.size()-1).getDistance(after);
        }
        return sum;
    }
}
