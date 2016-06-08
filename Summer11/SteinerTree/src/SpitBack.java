/**
 * Created by njk on 6/8/16.
 */
import java.util.ArrayList;
public class SpitBack implements Algorithm {
    public ArrayList<Line> makeTree(ArrayList<Point> points) {
        ArrayList<Line> tree = new ArrayList<>();
        for (int i = 0; i < points.size() - 1; i++) {
            tree.add(new Line(points.get(i), points.get(i+1)));
        }
        tree.add(new Line(points.get(points.size() - 1), points.get(0)));
        return tree;
    }
}
