import java.util.*;
public class AntColony
{
    //constants to play around with
    public static final double alpha = 1, beta = 5, rho = 0.05, q = 1;
    
    //holds all paths from one point to another that have been explored so far
    public static ArrayList<Edge> usedPaths = new ArrayList<Edge>();
    //public static ArrayList<Ant> ants = new ArrayList<Ant>();
    
    public static ArrayList<ArrayList<Point>> solveAllProblems(ArrayList<ArrayList<Point>> problems, int t) {
        ArrayList<ArrayList<Point>> solutions = new ArrayList<ArrayList<Point>>();
        for (ArrayList<Point> points : problems) {
            solutions.add(solveProblem(points, t));
        }
        return solutions;
    }
    public static ArrayList<Point> solveProblem(ArrayList<Point> points, int t) {
        usedPaths.clear();
        Ant a = new Ant(points.get(0));
        for (int i = 0; i < t; i++) {
            a.reset(points.get(0));
            a.makeTrip(points);
            a.dropPheromones();
            evapPheromones();
        }
        System.out.println("Done");
        ArrayList<Point> solution = a.getMemory();
        return solution;
    }
    
    static class Ant {
        Point currPoint;
        ArrayList<Edge> edgeMemory;
        ArrayList<Point> pointMemory;
        public Ant(Point start) {
            currPoint = start;
            edgeMemory = new ArrayList<Edge>();
            pointMemory = new ArrayList<Point>();
            pointMemory.add(currPoint);
        }
        public void reset(Point start) {
            currPoint = start;
            edgeMemory.clear();
            pointMemory.clear();
        }
        public ArrayList<Point> getMemory() {
            //only to be invoked when ant has completed its path
            return pointMemory;
        }
        public void makeTrip(ArrayList<Point> points) {
            int moves = 0;
            while (moves < points.size()) {
                advancePoint(points);
                moves++;
                System.out.println(moves);
            }
        }
        public void dropPheromones() {
            //updates pheromones on travelled edges
            //only to be invoked after path has been completed
            double pAmount = q / PathMeasure.evalPathLen(pointMemory,null,pointMemory.get(0));
            for (int i = 0; i < edgeMemory.size(); i++) {
                int index = pathExplored(edgeMemory.get(i));
                if (index == -1) {
                    //if one of the paths the ant went on isn't yet explored
                    usedPaths.add(edgeMemory.get(i));
                    edgeMemory.get(i).addPheromone(pAmount);
                }
                else {
                    //path has been explored before
                    usedPaths.get(index).addPheromone(pAmount);
                }
            }
        }
        public void advancePoint(ArrayList<Point> points) {
            //sends ant to next point out of points, updates position, updates memory
            Point nextP = findNext(points);
            edgeMemory.add(new Edge(currPoint,nextP));
            currPoint = nextP;
            pointMemory.add(currPoint);
        }
        private Point findNext(ArrayList<Point> points) {
            //returns next point that ant will go to out of all points
            double highestProb = -1;
            Point nextPoint = null;
            for (int i = 0; i < points.size(); i++) {
                Point cp = points.get(i);
                if (!pointMemory.contains(cp)) {
                    double prob = pathProb(cp, points);
                    if (prob > highestProb) {
                        highestProb = prob;
                        nextPoint = cp;
                    }
                }
            }
            return nextPoint;
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
                    sum += Math.pow(1/otherPotential.getLen(), beta);
                }
            }
            return (Math.pow(potential.getPheromone(),alpha) + Math.pow(1/potential.getLen(), beta)) / sum;
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
        public void setPheromone(double p) {
            this.pheromone = p;
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
            //note that direction matters: A -> B != B -> A
            return e.getStart().equals(p1) && e.getEnd().equals(p2);
        }
    }
    public static void evapPheromones() {
        for (int i = 0; i < usedPaths.size(); i++) {
            Edge e = usedPaths.get(i);
            e.setPheromone(e.getPheromone() * (1 - rho));
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
