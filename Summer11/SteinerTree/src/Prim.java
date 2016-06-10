/**
 * Created by njk on 6/9/16.
 */
import java.util.*;
public class Prim implements Algorithm {
    public ArrayList<Line> makeTree(ArrayList<Point> points) {
        //load in all the possible edges
        double[][] dists = Utils.distsMatrix(points);

        boolean[] visited = new boolean[points.size()];
        ArrayList<Integer> indiciesVisited = new ArrayList<>();

        ArrayList<Line> tree = new ArrayList<>();
        visited[0] = true;
        indiciesVisited.add(0);

        while (tree.size() < points.size() - 1) {
            //find next shortest, available edge
            double minEdgeLen = Double.MAX_VALUE;
            int[] minEdgePair = {-1, -1};
            for (int i : indiciesVisited) {
                for (int j = 0; j < dists[i].length; j++) {
                    if (i == j || visited[j]) continue;
                    if (dists[i][j] < minEdgeLen) {
                        minEdgeLen = dists[i][j];
                        minEdgePair[0] = i;
                        minEdgePair[1] = j;
                    }
                }
            }
            tree.add(new Line(points.get(minEdgePair[0]), points.get(minEdgePair[1])));
            visited[minEdgePair[1]] = true;
            indiciesVisited.add(minEdgePair[1]);
        }

        return tree;
    }
}
