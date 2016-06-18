/**
 * Created by njk on 6/9/16.
 */
import java.util.*;
public class Prim implements Algorithm {
    public ArrayList<Line> makeTree(ArrayList<Point> points) {
        ArrayList<Line> tree = new ArrayList<>();
        double[] cheapestDists = new double[points.size()];
        int[] cheapestConnections = new int[points.size()];
        boolean[] indicesVisited = new boolean[points.size()];

        for (int i = 0; i < points.size(); i++) {
            cheapestDists[i] = 1000000.0;
            cheapestConnections[i] = -1;
        }

        while (tree.size() < points.size() - 1) {
            double minDist = Double.MAX_VALUE;
            int minConnection = -1;
            for (int i = 0; i < points.size(); i++) {
                if (!indicesVisited[i] && cheapestDists[i] < minDist) {
                    minDist = cheapestDists[i];
                    minConnection = i;
                }
            }

            indicesVisited[minConnection] = true;
            if (cheapestConnections[minConnection] != -1) {
                tree.add(new Line(points.get(minConnection), points.get(cheapestConnections[minConnection])));
            }
            for (int i = 0; i < points.size(); i++) {
                if (!indicesVisited[i]) {
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
