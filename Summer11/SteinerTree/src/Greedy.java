/**
 * Created by njk on 6/8/16.
 */
import java.util.*;
public class Greedy implements Algorithm {
    public ArrayList<Line> makeTree(ArrayList<Point> points) {
        double[][] dists = Utils.distsMatrix(points);
        boolean[] visited = new boolean[points.size()];
        ArrayList<Line> tree = new ArrayList<>();
        int currPos = 0;
        visited[0] = true;
        for (int i = 0; i < points.size() - 1; i++) {
            int nn = nearestNeighbor(currPos, dists, visited);
            tree.add(new Line(points.get(currPos), points.get(nn)));
            currPos = nn;
            visited[currPos] = true;
        }
        Point last = tree.get(tree.size() - 1).getP2();
        tree.add(new Line(last, points.get(0)));
        return tree;
    }
    private static int nearestNeighbor(int pIndex, double[][] dists, boolean[] visited) {
        int closestIndex = -1;
        double closestDist = Double.MAX_VALUE;
        for (int i = 0; i < dists.length; i++) {
            if (i == pIndex || visited[i]) continue;
            if (dists[pIndex][i] < closestDist) {
                closestDist = dists[pIndex][i];
                closestIndex = i;
            }
        }
        return closestIndex;
    }
}