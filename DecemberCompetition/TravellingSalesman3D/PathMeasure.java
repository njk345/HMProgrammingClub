import java.util.*;
public class PathMeasure
{
    public static final ArrayList<ArrayList<DecemberCompetition.Point>> points = DecemberCompetition.getProblems();
    public static int evalAlgLen(ArrayList<ArrayList<DecemberCompetition.Point>> solutions) {
        double sum = 0;
        for (int i = 0; i < solutions.size(); i++) {
            sum += evalPathLen(solutions.get(i));
        }
        return (int)sum;
    }
    public static double evalPathLen(ArrayList<DecemberCompetition.Point> points) {
        double sum = 0;
        for (int i = 1; i < points.size(); i++) {
            sum += getDistance(points.get(i-1), points.get(i));
        }
        sum += getDistance(points.get(0), points.get(points.size() - 1));
        return sum;
    }
    public static double getDistance (DecemberCompetition.Point p1, DecemberCompetition.Point p2) {
        return Math.sqrt(Math.pow(p1.x-p2.x,2) + Math.pow(p1.y-p2.y,2) + Math.pow(p1.z-p2.z,2));
    }
}
