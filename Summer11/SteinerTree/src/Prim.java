/**
 * Created by njk on 6/9/16.
 */
import java.util.*;
public class Prim implements Algorithm {
    public ArrayList<Line> makeTree(ArrayList<Point> points) {
        ArrayList<Line> tree = new ArrayList<>();
        double[] cheapestDists = new double[points.size()];
        int[] cheapestConnections = new int[points.size()];
        boolean[] indiciesVisited = new boolean[points.size()];

        for (int i = 0; i < points.size(); i++) {
            cheapestDists[i] = Double.MAX_VALUE;
            cheapestConnections[i] = -1;
        }

        while (tree.size() < points.size() - 1) {
            double minDist = Double.MAX_VALUE;
            int minConnection = -1;
            for (int i = 0; i < points.size(); i++) {
                if (cheapestDists[i] < minDist) {
                    minDist = cheapestDists[i];
                    minConnection = i;
                }
            }

            indiciesVisited[minConnection] = true;
            if (cheapestConnections[minConnection] != -1) {
                tree.add(new Line(points.get(minConnection), points.get(cheapestConnections[minConnection])));
            }
            for (int i = 0; i < points.size(); i++) {
                if (!indiciesVisited[i]) {
                    double dist = Utils.dist(points.get(minConnection), points.get(i));
                    if (dist < cheapestDists[i]) {
                        cheapestDists[i] = dist;
                        cheapestConnections[i] = minConnection;
                    }
                }
            }
        }

        return tree;
    }
}
