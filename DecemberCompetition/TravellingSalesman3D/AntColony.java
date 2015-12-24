import java.util.*;
public class AntColony
{
    //constants to play around with
    public static final double alpha = 1, beta = 5, rho = 0.1, q = 1;
    
    //holds all paths from one point to another that have been explored so far
    public static ArrayList<Edge> usedPaths = new ArrayList<Edge>();
    static class Ant {
        Point currPoint;
        ArrayList<Edge> edgeMemory;
        ArrayList<Point> pointMemory;
        public Ant(Point start) {
            currPoint = start;
            pointMemory.add(currPoint);
        }
        public void dropPheromone(Edge e, double p) {
            e.addPheromone(p);
        }
        public Point findNext(ArrayList<Point> points) {
            double highestProb = -1;
            Point nextPoint = null;
            for (int i = 0; i < points.size(); i++) {
                if (!pointMemory.contains(points.get(i))) {
                    
                }
            }
        }
        private double pathProb(Point next, ArrayList<Point> points) {
            //returns probability that ant will go to given next Point
            Edge potential = new Edge(currPoint, next);
            int index;
            if ((index = pathExplored(potential)) != -1) {
                potential = usedPaths.get(index);
            }
            //sum up the length + pheromone level of all other possible next points
            double sum = 0;
            for (int i = 0; i < points.size(); i++) {
                if (!pointMemory.contains(points.get(i))) {
                    Edge otherPotential = new Edge(currPoint, points.get(i));
                    int index2;
                    if ((index2 = pathExplored(otherPotential)) != -1) {
                        otherPotential = usedPaths.get(index2);
                    }
                    sum += Math.pow(otherPotential.getPheromone(), alpha);
                    sum += Math.pow(otherPotential.getLen(), beta);
                }
            }
            return (Math.pow(potential.getPheromone(),alpha) + Math.pow(potential.getLen(), beta)) / sum;
        }
    }
    static class Edge {
        double pheromone;
        double len;
        Point p1, p2;
        public Edge(Point p1, Point p2) {
            this.p1 = p1;
            this.p2 = p2;
            this.len = p1.getDistance(p2);
            this.pheromone = 0;
        }
        public void addPheromone(double p) {
            this.pheromone += p;    
        }
        public double getPheromone() {
            return this.pheromone;
        }
        public double getLen() {
            return this.len;
        }
        public Point getStart() {
            return p1;
        }
        public Point getEnd() {
            return p2;
        }
        public boolean equals(Edge e) {
            return e.getStart().equals(p1) && e.getEnd().equals(p2);
        }
    }
    public static int pathExplored(Edge e) {
        for (int i = 0; i < usedPaths.size(); i++) {
            if (usedPaths.get(i).equals(e)) {
                return i;
            }
        }
        return -1; //e not in usedPaths
    }
}
