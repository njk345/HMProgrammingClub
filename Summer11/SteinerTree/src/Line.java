/**
 * Created by njk on 6/7/16.
 */
public class Line {
    private Point p1, p2;
    private double length;
    public Line(Point p1, Point p2) {
        this.p1 = p1;
        this.p2 = p2;
        this.length = Utils.dist(p1, p2);
    }
    public Point getP1() {
        return p1;
    }
    public Point getP2() {
        return p2;
    }
    public void setP1(Point p) {
        p1 = p;
    }
    public void setP2(Point p) {
        p2 = p;
    }
    public double getLength() {
        return length;
    }
    public String toString() {
        return p1.toString() + " " + p2.toString();
    }
}
