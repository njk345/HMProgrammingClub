import java.util.*;
public class AntColony
{
    //constants to play around with
    public static final double alpha = 1, beta = 5, rho = 0.05, q = 1, q0 = 0.1, p0 = 0.00005;
    /*public static final boolean nn = true;*/
    /*public static final int nnSize = 10;*/
    
    public static Random r = new Random(1234);

    public static int[][] solveAllProblems(ArrayList<ArrayList<Point>> problems, int t, int a, ArrayList<Integer> pToSolve, double[][][] dists) {
        double totLen = 0;
        int[][] solutions = new int[problems.size() + 1][problems.get(0).size()];
        //solutions always has 20 arrays representing paths, no matter size of pToSolve

        for (int i = 0; i < pToSolve.size(); i++) {
            int problemNum = pToSolve.get(i) - 1;
            solutions[problemNum] = solveProblem(problems.get(problemNum), t, a, i, dists[problemNum]);
            System.out.println(" Solved Problem " + (problemNum + 1));
            totLen += evalIntPathLen(solutions[problemNum], dists[problemNum]);
        }
        solutions[solutions.length - 1][0] = (int)totLen;
        //stuff the length of the alg as first el of last bucket in matrix
        return solutions;
    }

    public static int[] solveProblem(ArrayList<Point> points, int t, int a, int pNum, double[][] dists) {
        double[][] globalPheromones = new double[200][200];
        fillMatrix(globalPheromones, p0);
        Ant[] ants = new Ant[a];
        Ant allTimeBest = null;
        double globalBestLen = Double.MAX_VALUE;

        for (int i = 0; i < a; i++) {
            ants[i] = new Ant(dists);
        }

        for (int i = 0; i < t; i++) {
            //RESET EACH ANT
            for (Ant ant : ants) {
                ant.reset();
            }
            //EACH ANT MAKES TRIP AND STORES LOCAL PHEROMONES
            for (Ant ant : ants) {
                ant.makeTrip(globalPheromones);
            }
            evapPheromones(globalPheromones);
            for (Ant ant : ants) {
                ant.dropPheromones(globalPheromones, false);
            }
            Ant potentialBest = updateAllTimeBest(ants, dists, globalBestLen);
            if (potentialBest != null) {
                allTimeBest = potentialBest;
                globalBestLen = evalIntPathLen(allTimeBest.getPath(), dists);
            }
            allTimeBest.dropPheromones(globalPheromones, true);

            if (t >= 1000 || a >= 1000) {
                System.out.print("Iteration " + (i + 1) + " Done || ");
                System.out.println("High Score = " + (int)globalBestLen);
            }
        }
        System.out.print("Length = " + (int) globalBestLen);
        return allTimeBest.getPath();
    }

    static class Ant {
        int currPoint;
        int[] path;
        int numVisited;
        boolean[] visited;
        double[][] dists;

        public Ant(double[][] dists) {
            path = new int[dists.length];
            visited = new boolean[dists.length];
            this.dists = dists;
        }

        public void reset() {
            for (int i = 0; i < visited.length; i++) {
                visited[i] = false;
            }
            for (int i = 0; i < path.length; i++) {
                //reset to meaningless path
                path[i] = -1;
            }
            currPoint = r.nextInt(dists.length - 1);
            visited[currPoint] = true;
            path[0] = currPoint;
            numVisited = 1;
        }

        public int[] getPath() {
            //only to be invoked when ant has completed its path
            return path;
        }

        public void makeTrip(double[][] globalPheromones) {
            //int moves = 0;
            while (numVisited < dists.length) {
                advancePoint(globalPheromones);
                //System.out.println(moves);
                //moves++;
            }
        }

        public void dropPheromones(double[][] pheromones, boolean isBest) {
            double pAmount = isBest ? rho * (q / evalIntPathLen(path, dists)) : (rho * p0);
            for (int i = 0; i < path.length - 1; i++) {
                pheromones[path[i]][path[i+1]] += pAmount;
                pheromones[path[i+1]][path[i]] += pAmount;
            }
        }

        public void advancePoint(double[][] globalPheromones) {
            //sends ant to next point out of points, updates position, updates memory
            int nextP = findNext(globalPheromones);
            currPoint = nextP;
            numVisited++;
            path[numVisited-1] = currPoint;
            visited[currPoint] = true;
        }

        private int findNext(double[][] globalPheromones) {
            //load the array of probabilities to go to non-visited points
            double[] probs = new double[dists.length];
            int highestProbIndex = -1;
            double highestProb = -1;
            double probTot = 0;
            for (int i = 0; i < dists.length; i++) {
                if (!visited[i]) {
                    probs[i] = pathProb(i, globalPheromones);
                    probTot += probs[i];
                    if (probs[i] > highestProb) {
                        highestProb = probs[i];
                        highestProbIndex = i;
                    }
                }
            }
            
            //if rand <= q0, return the max probability point
            double rand = r.nextDouble();
            if (rand <= q0) {
                return highestProbIndex;
            }
            
            //otherwise pick according to pseudo-random probability distribution
            for (int i = 0; i < probs.length; i++) {
                //normalize the probabilities to be between 0 and 1
                probs[i] /= probTot;
            }

            probTot = 0; //reset to build up again
            rand = r.nextDouble(); //between 0 and 1
            for (int i = 0; i < probs.length; i++) {
                probTot += probs[i];
                if (probTot > rand) return i;
            }
            
            //It only gets here if rounding error with the doubles
            //causes the rand to be greater than every probability
            //THIS IS SUPER SUPER SUPER RARE, so just return any valid point
            for (int i = 0; i < dists.length; i++) {
                if (!visited[i]) return i;
            }
            throw new RuntimeException("Better Not Freaking Get Here");
        }

        private double pathProb(int nextPoint, double[][] globalPheromones) {
            double prob = pow(globalPheromones[currPoint][nextPoint], alpha) * pow(1 / dists[currPoint][nextPoint], beta);
            return prob;
        }
    }
    
    public static Ant updateAllTimeBest (Ant[] ants, double[][] dists, double bestLen) {
        Ant bestAnt = null;
        for (int i = 0; i < ants.length; i++) {
            double len = evalIntPathLen(ants[i].getPath(), dists);
            if (len < bestLen) {
                bestAnt = ants[i];
                bestLen = len;
            }
        }
        return bestAnt;
    }

    public static void evapPheromones(double[][] globalPheromones) {
        for (int i = 0; i < globalPheromones.length; i++) {
            for (int j = 0; j < globalPheromones[i].length; j++) {
                globalPheromones[i][j] *= (1 - rho);
            }
        }
    }
    
    ////////////// PATH OPTIMIZATION SECTION
    
    
    
    
    //////////////

    public static double evalIntPathLen (int[] indices, double[][] dists) {
        double len = 0;
        for (int i = 0; i < indices.length - 1; i++) {
            len += dists[indices[i]][indices[i+1]];
        }
        len += dists[indices[indices.length-1]][indices[0]];
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
}
