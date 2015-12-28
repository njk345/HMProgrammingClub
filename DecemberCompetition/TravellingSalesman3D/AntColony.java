import java.util.*;
public class AntColony
{
    //constants to play around with
    public static final double alpha = 1, beta = 5, rho = 0.05, q = 1, randThresh = 0.01;
    
    public static ArrayList<ArrayList<Point>> solveAllProblems(ArrayList<ArrayList<Point>> problems, int t, int a) {
        ArrayList<ArrayList<Point>> solutions = new ArrayList<ArrayList<Point>>();
        int times = 0;
        for (ArrayList<Point> points : problems) {
            solutions.add(solveProblem(points, t, a));
            if (++times == 1) break;
        }
        return solutions;
    }
    public static ArrayList<Point> solveProblem(ArrayList<Point> points, int t, int a) {
        ArrayList<Edge> usedPaths = new ArrayList<Edge>();
        ArrayList<Ant> ants = new ArrayList<Ant>();
        ArrayList<Point> globalBest = null;
        double globalBestLen = Double.MAX_VALUE;
        
        for (int i = 0; i < a; i++) {
            ants.add(new Ant(points));
        }
        
        for (int i = 0; i < t; i++) {
            for (Ant ant : ants) {
                ant.makeTrip(usedPaths);
            }
            for (Ant ant : ants) {
                ant.dropPheromones(usedPaths);
            }
            evapPheromones(usedPaths);
            ArrayList<Point> nb = new ArrayList<Point>(findNewGlobalBest(globalBestLen, ants));
            if (nb != null) {
                globalBest = nb;
                globalBestLen = PathMeasure.evalPathLen(globalBest, null, globalBest.get(0));
            }
            for (Ant ant : ants) {
                ant.reset();
            }
        }
        return globalBest;
    }
    
    static class Ant {
        Point currPoint;
        ArrayList<Point> points;
        ArrayList<Edge> edgeMemory;
        ArrayList<Point> pointMemory;
        public Ant(ArrayList<Point> points) {
            this.points = points;
            currPoint = points.get(0);
            edgeMemory = new ArrayList<Edge>();
            pointMemory = new ArrayList<Point>();
            pointMemory.add(currPoint);
        }
        public void reset() {
            currPoint = points.get(0);
            edgeMemory.clear();
            pointMemory.clear();
        }
        public ArrayList<Point> getMemory() {
            //only to be invoked when ant has completed its path
            return pointMemory;
        }
        public void makeTrip(ArrayList<Edge> usedPaths) {
            int moves = 0;
            while (moves < points.size() - 1) {
                advancePoint(usedPaths);
                moves++;
                System.out.println(moves);
            }
        }
        public void dropPheromones(ArrayList<Edge> usedPaths) {
            //updates pheromones on travelled edges
            //only to be invoked after path has been completed
            double pAmount = q / PathMeasure.evalPathLen(pointMemory,null,pointMemory.get(0));
            for (int i = 0; i < edgeMemory.size(); i++) {
                int index = pathExplored(edgeMemory.get(i), usedPaths);
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
        public void advancePoint(ArrayList<Edge> usedPaths) {
            //sends ant to next point out of points, updates position, updates memory
            Point nextP = findNext(usedPaths);
            edgeMemory.add(new Edge(currPoint,nextP));
            currPoint = nextP;
            pointMemory.add(currPoint);
        }
        private Point findNext(ArrayList<Edge> usedPaths) {
            Random r = new Random();
            double chance = r.nextDouble();
            int rp = r.nextInt(points.size());
            if (chance < randThresh && !pointMemory.contains(points.get(rp))) {
                //returns random next point 
                return points.get(rp);
            }
            
            //returns next point based on probability
            double highestProb = -1;
            Point nextPoint = null;
            for (int i = 0; i < points.size(); i++) {
                Point cp = points.get(i);
                if (!pointMemory.contains(cp)) {
                    double prob = pathProb(cp, points, usedPaths);
                    if (prob > highestProb) {
                        highestProb = prob;
                        nextPoint = cp;
                    }
                }
            }
            return nextPoint;
        }
        private double pathProb(Point next, ArrayList<Point> points, ArrayList<Edge> usedPaths) {
            //returns probability that ant will go to given next Point
            Edge potential = new Edge(currPoint, next);
            int index;
            if ((index = pathExplored(potential, usedPaths)) != -1) {
                potential = usedPaths.get(index);
            }
            //sum up the length + pheromone level of all other possible next points
            double sum = 0;
            for (int i = 0; i < points.size(); i++) {
                if (!pointMemory.contains(points.get(i))) {
                    Edge otherPotential = new Edge(currPoint, points.get(i));
                    int index2;
                    if ((index2 = pathExplored(otherPotential, usedPaths)) != -1) {
                        otherPotential = usedPaths.get(index2);
                    }
                    sum += pow(otherPotential.getPheromone(), alpha);
                    sum += pow(1/otherPotential.getLen(), beta);
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
    public static ArrayList<Point> findNewGlobalBest (double currBest, ArrayList<Ant> ants) {
        double bestLen = currBest;
        ArrayList<Point> newGlobalBest = null;
        for (Ant a : ants) {
            ArrayList<Point> localBest = a.getMemory();
            double localLen = PathMeasure.evalPathLen(localBest, null, localBest.get(0));
            if (localLen < bestLen) {
                bestLen = localLen;
                newGlobalBest = localBest;
            }
        }
        return newGlobalBest;
        //if null is returned, no new global best path was found
    }
    public static void evapPheromones(ArrayList<Edge> usedPaths) {
        for (int i = 0; i < usedPaths.size(); i++) {
            Edge e = usedPaths.get(i);
            e.setPheromone(e.getPheromone() * (1 - rho));
        }
    }
    public static int pathExplored(Edge e, ArrayList<Edge> usedPaths) {
        for (int i = 0; i < usedPaths.size(); i++) {
            if (usedPaths.get(i).equals(e)) {
                return i;
            }
        }
        return -1; //e not in usedPaths
    }
    public static double pow(final double a, final double b) {
        final int x = (int) (Double.doubleToLongBits(a) >> 32);
        final int y = (int) (b * (x - 1072632447) + 1072632447);
        return Double.longBitsToDouble(((long) y) << 32);
    }
}
