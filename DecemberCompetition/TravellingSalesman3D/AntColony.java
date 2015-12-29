import java.util.*;
public class AntColony
{
    //constants to play around with
    public static final double alpha = 1, beta = 1, rho = 0.05, q = 1, randThresh = 0.02;

    public static int[][] solveAllProblems(ArrayList<ArrayList<Point>> problems, int t, int a, ArrayList<ArrayList<Point>> bestStarts) {
        int[][] solutions = new int[problems.size()][problems.get(0).size()];
        int times = 0;
        for (int i = 0; i < problems.size(); i++) {
            solutions[i] = solveProblem(problems.get(i), t, a, bestStarts.get(i).get(0).index);
            System.out.println(" Solved Problem " + (i + 1));
        }
        return solutions;
    }

    public static int[] solveProblem(ArrayList<Point> points, int t, int a, int start) {
        HashMap<List<Integer>, Double> usedPaths = new HashMap<List<Integer>, Double>();
        Ant[] ants = new Ant[a];
        int[] globalBest = new int[points.size()];
        double globalBestLen = Double.MAX_VALUE;
        

        for (int i = 0; i < a; i++) {
            ants[i] = new Ant(points, start);
        }

        for (int i = 0; i < t; i++) {
            for (Ant ant : ants) {
                ant.makeTrip(usedPaths);
            }
            for (Ant ant : ants) {
                ant.dropPheromones(usedPaths);
            }
            evapPheromones(usedPaths);
            int[] rv = findNewGlobalBest(globalBestLen, ants, points);
            int[] nb = new int[rv.length];
            for (int x = 0; x < nb.length; x++) {
                nb[x] = rv[x];
            }
            if (nb[0] != -1) {
                globalBest = nb;
                globalBestLen = evalIntPathLen(globalBest, points);
            }
            for (Ant ant : ants) {
                ant.reset();
            }
        }
        System.out.print("Length = " + (int) globalBestLen);
        return globalBest;
    }

    static class Ant {
        int currPoint;
        ArrayList<Point> points;
        int[] path;
        int numVisited;
        boolean[] visited;
        int start;
        
        public Ant(ArrayList<Point> points, int start) {
            this.points = points;
            path = new int[points.size()];
            visited = new boolean[points.size()];
            
            this.start = start;
            currPoint = start;
            visited[start] = true;
            path[0] = currPoint;
            numVisited = 1;
        }

        public void reset() {
            for (int i = 0; i < visited.length; i++) {
                visited[i] = false;
            }
            for (int i = 0; i < path.length; i++) {
                //reset to meaningless path
                path[i] = -1;
            }
            currPoint = start;
            visited[start] = true;
            path[0] = start;
            numVisited = 1;
        }

        public int[] getPath() {
            //only to be invoked when ant has completed its path
            return path;
        }

        public void makeTrip(HashMap<List<Integer>, Double> usedPaths) {
            int moves = 0;
            while (numVisited < points.size()) {
                advancePoint(usedPaths);
                //System.out.println(moves);
                moves++;
            }
        }

        public void dropPheromones(HashMap<List<Integer>, Double> usedPaths) {
            //updates pheromones on travelled edges
            //only to be invoked after path has been completed
            double pAmount = q / evalIntPathLen(path, points);
            for (int i = 0; i < path.length - 1; i++) {
                ArrayList<Integer> e = new ArrayList<Integer>();
                e.add(path[i]);
                e.add(path[i+1]);
                if (usedPaths.containsKey(e)) {
                    //if path explored before
                    double currP = usedPaths.get(e);
                    usedPaths.put(e, currP + pAmount);
                }
                else {
                    //if path new
                    usedPaths.put(e, pAmount);
                }
            }
        }

        public void advancePoint(HashMap<List<Integer>, Double> usedPaths) {
            //sends ant to next point out of points, updates position, updates memory
            int nextP = findNext(usedPaths);
            currPoint = nextP;
            numVisited++;
            path[numVisited-1] = currPoint;
            visited[currPoint] = true;
        }

        private int findNext(HashMap<List<Integer>, Double> usedPaths) {
//             Random r = new Random();
//             double chance = r.nextDouble();
//             int rp = r.nextInt(points.size());
//             if (chance < randThresh && !visited[rp]) {
//                 //returns random next point
//                 return rp;
//             }

            //returns next point based on probability
            double highestProb = -1;
            int nextPoint = -1;
            for (int i = 0; i < points.size(); i++) {
                Point cp = points.get(i);
                if (!visited[cp.index]) {
                    double prob = pathProb(cp, points, usedPaths);
                    if (prob > highestProb) {
                        highestProb = prob;
                        nextPoint = cp.index;
                    }
                }
            }
            return nextPoint;
        }

        private double pathProb(Point next, ArrayList<Point> points, HashMap<List<Integer>, Double> usedPaths) {
            ArrayList<Integer> e = new ArrayList<Integer>();
            e.add(currPoint);
            e.add(next.index);
            double num = pow(1 / points.get(currPoint).getDistance(next), beta);
            if (usedPaths.containsKey(e)) {
                num += pow(usedPaths.get(e), alpha);
            }
            
            double denom = 0;
            for (int i = 0; i < points.size(); i++) {
                if (!visited[i]) {
                    ArrayList<Integer> eOther = new ArrayList<Integer>();
                    eOther.add(currPoint);
                    eOther.add(points.get(i).index);
                    denom += pow(1 / points.get(currPoint).getDistance(points.get(i)), beta);
                    
                    if (usedPaths.containsKey(eOther)) {
                        denom += pow(usedPaths.get(eOther), alpha);
                    }
                }
            }
            return num / denom;
        }
    }

    public static int[] findNewGlobalBest (double currBest, Ant[] ants, ArrayList<Point> points) {
        double bestLen = currBest;
        int[] newGlobalBest = new int[points.size()];
        newGlobalBest[0] = -1;
        //set first el to -1 b/c, if this is returned, I know there isn't new global best path
        for (Ant a : ants) {
            int[] localPath = a.getPath();
            double localLen = evalIntPathLen(localPath, points);
            if (localLen < bestLen) {
                bestLen = localLen;
                newGlobalBest = localPath;
            }
        }
        return newGlobalBest;
        //if null is returned, no new global best path was found
    }

    public static void evapPheromones(HashMap<List<Integer>, Double> usedPaths) {
        for (List<Integer> key : usedPaths.keySet()) {
            double currVal = usedPaths.get(key);
            usedPaths.put(key, (1 - rho) * currVal);
        }
    }

    public static ArrayList<Point> indicesToPoints (int[] indices, ArrayList<Point> points) {
        ArrayList<Point> rvs = new ArrayList<Point>();
        for (int i = 0; i < indices.length; i++) {
            rvs.add(points.get(indices[i]));
        }
        return rvs;
    }
    
    public static double evalIntPathLen (int[] indices, ArrayList<Point> points) {
        double len = 0;
        for (int i = 0; i < indices.length - 1; i++) {
            len += points.get(indices[i]).getDistance(points.get(indices[i+1]));
        }
        len += points.get(indices[indices.length - 1]).getDistance(points.get(0));
        return len;
    }

    public static double pow(final double a, final double b) {
        final int x = (int) (Double.doubleToLongBits(a) >> 32);
        final int y = (int) (b * (x - 1072632447) + 1072632447);
        return Double.longBitsToDouble(((long) y) << 32);
    }
    
    public static void print(int[] a) {
        for (int i = 0; i < 5; i++) {
            System.out.print(a[i] + " ");
        }
        System.out.println();
    }
}
