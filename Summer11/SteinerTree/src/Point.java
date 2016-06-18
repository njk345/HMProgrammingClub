/**
 * Created by njk on 6/7/16.
 */
public class Point {
    private final double x;
    private final double y;
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }
    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public String toString() {
        return "" + x + " " + y;
    }
}
