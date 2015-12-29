import java.util.*;
public class AntColony
{
    //constants to play around with
    public static final double alpha = 1, beta = 5, rho = 0.05, q = 1, randThresh = 0.01;
    
    public static double[][] distances = new double[200][200];
    public static Random r = new Random(1234);

    public static int[][] solveAllProblems(ArrayList<ArrayList<Point>> problems, int t, int a, ArrayList<ArrayList<Point>> bestStarts) {
        double totLen = 0;
        int[][] solutions = new int[problems.size() + 1][problems.get(0).size()];
        int times = 0;
        for (int i = 0; i < problems.size(); i++) {
            solutions[i] = solveProblem(problems.get(i), t, a, bestStarts.get(i).get(0).index);
            System.out.println(" Solved Problem " + (i + 1));
            totLen += evalIntPathLen(solutions[i]);
            if(times == 0) break;
        }
        solutions[solutions.length - 1][0] = (int)totLen;
        //stuff the length of the alg as first el of last bucket in matrix
        return solutions;
    }

    public static int[] solveProblem(ArrayList<Point> points, int t, int a, int start) {
        double[][] pheromones = new double[200][200];
        fillMatrix(pheromones, 1);
        Ant[] ants = new Ant[a];
        int[] globalBest = new int[points.size()];
        double globalBestLen = Double.MAX_VALUE;

        //how the distances between points are stored
        fillDistsMatrix(distances, points);

        for (int i = 0; i < a; i++) {
            ants[i] = new Ant(points, start);
        }

        for (int i = 0; i < t; i++) {
            for (Ant ant : ants) {
                ant.makeTrip(pheromones);
            }
            for (Ant ant : ants) {
                ant.dropPheromones(pheromones);
            }
            evapPheromones(pheromones);
            int[] rv = findNewGlobalBest(globalBestLen, ants);
            int[] nb = new int[rv.length];
            for (int x = 0; x < nb.length; x++) {
                nb[x] = rv[x];
            }
            if (nb[0] != -1) {
                globalBest = nb;
                globalBestLen = evalIntPathLen(globalBest);
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
        int[] path;
        int numVisited;
        boolean[] visited;
        int start;

        public Ant(ArrayList<Point> points, int start) {
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

        public void makeTrip(double[][] pheromones) {
            int moves = 0;
            while (numVisited < distances.length) {
                advancePoint(pheromones);
                //System.out.println(moves);
                moves++;
            }
        }

        public void dropPheromones(double[][] pheromones) {
            //updates pheromones on travelled edges
            //only to be invoked after path has been completed
            double pAmount = q / evalIntPathLen(path);
            for (int i = 0; i < path.length - 1; i++) {
                pheromones[path[i]][path[i+1]] += pAmount;
            }
        }

        public void advancePoint(double[][] pheromones) {
            //sends ant to next point out of points, updates position, updates memory
            int nextP = findNext(pheromones);
            currPoint = nextP;
            numVisited++;
            path[numVisited-1] = currPoint;
            visited[currPoint] = true;
        }

        private int findNext(double[][] pheromones) {
            double chance = r.nextDouble();
            if (chance < randThresh) {
                //returns random next point
                return findNextRandom();
            }

            //returns next point based on probability
            double highestProb = -1;
            int nextPoint = -1;
            double totProb = 0;
            double[] probs = new double[distances.length];
            for (int i = 0; i < distances.length; i++) {
                if (!visited[i]) {
                    probs[i] = pathProb(i, pheromones);
                    totProb += probs[i];
                }
            }
            for (int i = 0; i < distances.length; i++) {
                double prob = probs[i] / totProb;
                if (prob > highestProb) {
                    highestProb = prob;
                    nextPoint = i;
                }
            }
            return nextPoint;
        }
        
        private int findNextRandom() {
            int rp = r.nextInt(distances.length - numVisited);
            int rv = -1;
            int count = 0;
            for (int i = 0; i < visited.length; i++) {
                if (!visited[i]) {
                    if (count == rp) {
                        rv = i;
                        break;
                    }
                    count++;
                }
            }
            return rv;
        }

        private double pathProb(int nextPoint, double[][] pheromones) {
            double prob = pow(pheromones[currPoint][nextPoint], alpha) * pow(1 / distances[currPoint][nextPoint], beta);
            return prob;
        }
    }

    public static int[] findNewGlobalBest (double currBest, Ant[] ants) {
        double bestLen = currBest;
        int[] newGlobalBest = new int[distances.length];
        newGlobalBest[0] = -1;
        //set first el to -1 b/c, if this is returned, I know there isn't new global best path
        for (Ant a : ants) {
            int[] localPath = a.getPath();
            double localLen = evalIntPathLen(localPath);
            if (localLen < bestLen) {
                bestLen = localLen;
                newGlobalBest = localPath;
            }
        }
        return newGlobalBest;
        //if null is returned, no new global best path was found
    }

    public static void evapPheromones(double[][] pheromones) {
        for (int i = 0; i < pheromones.length; i++) {
            for (int j = 0; j < pheromones[i].length; j++) {
                pheromones[i][j] *= (1 - rho);
            }
        }
    }

    public static ArrayList<Point> indicesToPoints (int[] indices, ArrayList<Point> points) {
        ArrayList<Point> rvs = new ArrayList<Point>();
        for (int i = 0; i < indices.length; i++) {
            rvs.add(points.get(indices[i]));
        }
        return rvs;
    }

    public static double evalIntPathLen (int[] indices) {
        double len = 0;
        for (int i = 0; i < indices.length - 1; i++) {
            len += distances[indices[i]][indices[i+1]];
        }
        len += distances[indices[indices.length-1]][indices[0]];
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
    
    public static void fillMatrix (double[][] pher, double val) {
        for (int i = 0; i < pher.length; i++) {
            for (int j = 0; j < pher[i].length; j++) {
                pher[i][j] = val;
            }
        }
    }

    public static void fillDistsMatrix(double[][] dists, ArrayList<Point> points) {
        for (int i = 0; i < points.size(); i++) {
            for (int j = 0; j < points.size(); j++) {
                dists[i][j] = points.get(i).getDistance(points.get(j));
            }
        }
    }
}
