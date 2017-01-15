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
            try {
                indicesVisited[minConnection] = true;
            } catch(ArrayIndexOutOfBoundsException e) {
                //something's fishy about the last point that was added,
                //so get rid of it and throw away the algorithm iteration
                System.out.println("ERROR");
                points.remove(points.size() - 1);
                return null;
            }
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
