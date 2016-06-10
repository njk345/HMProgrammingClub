/**
 * Created by njk on 6/8/16.
 */
import java.util.*;
public class Greedy implements Algorithm {
    public ArrayList<Line> makeTree(ArrayList<Point> points) {
        double[][] dists = Utils.distsMatrix(points);

        ArrayList<Line> bestTree = null;
        double bestScore = Double.MAX_VALUE;

        for (int start = 0; start < points.size(); start++) {
            System.out.println("Testing Tree Starting At Point " + start + "...");
            boolean[] visited = new boolean[points.size()];
            ArrayList<Line> tree = new ArrayList<>();
            visited[start] = true;
            int currPos = start;
            for (int i = 0; i < points.size() - 1; i++) {
                int nn = nearestNeighbor(currPos, dists, visited);
                tree.add(new Line(points.get(currPos), points.get(nn)));
                currPos = nn;
                visited[currPos] = true;
            }
            double score = Utils.scoreTree(tree);

            if (score < bestScore) {
                bestTree = tree;
                bestScore = score;
            }
        }
        return bestTree;
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