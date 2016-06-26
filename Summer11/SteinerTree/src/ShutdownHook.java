import java.util.*;

/**
 * Created by njk on 6/25/16.
 */
public class ShutdownHook extends Thread {
    private ArrayList<Point> points;
    public ShutdownHook(ArrayList<Point> points) {
        this.points = points;
    }
    public void run() {
        ArrayList<Line> tree = new Prim().makeTree(points);
        System.out.println("Finished — Score = " + Utils.scoreTree(tree));
        Utils.writeOutput(tree);
    }
}
