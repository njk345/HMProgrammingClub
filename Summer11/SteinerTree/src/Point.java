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
    public boolean equals(Object o) {
        if (o instanceof Point) {
            String x1str = "" + x;
            String x2str = "" + ((Point)o).getX();
            String y1str = "" + y;
            String y2str = "" + ((Point)o).getY();
            return x1str.equals(x2str) && y1str.equals(y2str);
        }
        return false;
    }
    public String toString() {
        return "" + x + " " + y;
    }
}
