public class Point
{
    int x, y, z, index;
    public Point(int x, int y, int z, int index) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.index = index;
    }

    public double getDistance (Point p) {
        return Math.sqrt(Math.pow(x-p.x,2) + Math.pow(y-p.y,2) + Math.pow(z-p.z,2));
    }
    public boolean equals(Point p) {
        return p.x == x && p.y == y && p.z == z;
    }
}
