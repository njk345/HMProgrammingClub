import java.util.*;
public class AntColony
{
    //constants to play around with
    public static final double alpha = 1, beta = 5, rho = 0.05, q = 2, q0 = 0.01, p0 = 0.00005;
    
    public static Random daddyR = new Random(1234);
    public static Random r = new Random(daddyR.nextInt(1234));

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
        int[] allTimeBestPath = new int[dists.length];
        double globalBestLen = Double.MAX_VALUE;

        for (int i = 0; i < a; i++) {
            ants[i] = new Ant(dists, r.nextInt(dists.length));
        }

        for (int i = 0; i < t; i++) {
            //RESET EACH ANT
            for (Ant ant : ants) {
                ant.reset();
            }
            //EACH ANT MAKES TRIP
            for (Ant ant : ants) {
                ant.makeTrip(globalPheromones);
                bruteAll(ant.getPath(), dists);
            }
            evapPheromones(globalPheromones);
            for (Ant ant : ants) {
                ant.dropPheromones(globalPheromones);
            }
            int[] potentialBest = updateAllTimeBest(ants, dists, globalBestLen);
            if (potentialBest != null) {
                globalBestLen = evalIntPathLen(potentialBest, dists);
                for (int j = 0; j < potentialBest.length; j++) {
                    allTimeBestPath[j] = potentialBest[j];
                }
            }
            bestDropPheromones(globalPheromones, allTimeBestPath, globalBestLen);

            System.out.print("Iteration " + (i + 1) + " Done || ");
            System.out.println("High Score = " + (int)globalBestLen);
        }
        System.out.print("Length = " + (int) globalBestLen);
        return allTimeBestPath;
    }

    static class Ant {
        int currPoint;
        int[] path;
        int numVisited;
        boolean[] visited;
        double[][] dists;
        int start;

        public Ant(double[][] dists, int start) {
            this.start = start;
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
            currPoint = start;
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

        public void dropPheromones(double[][] pheromones) {
            double pAmount = rho * p0;
            for (int i = 0; i < path.length - 1; i++) {
                pheromones[path[i]][path[i+1]] += pAmount;
                pheromones[path[i+1]][path[i]] += pAmount;
            }
            pheromones[path.length - 1][path[0]] += pAmount;
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
                System.out.println("Never Happens");
                if (!visited[i]) return i;
            }
            throw new RuntimeException("Better Not Freaking Get Here");
        }

        private double pathProb(int nextPoint, double[][] globalPheromones) {
            double prob = pow(globalPheromones[currPoint][nextPoint], alpha) * pow(1 / dists[currPoint][nextPoint], beta);
            return prob;
        }
    }
    
    public static void bestDropPheromones(double[][] pher, int[] bestPath, double bestLen) {
        double pAmount = rho * (q / bestLen);
        for (int i = 0; i < bestPath.length - 1; i++) {
            pher[bestPath[i]][bestPath[i+1]] += pAmount;
            pher[bestPath[i+1]][bestPath[i]] += pAmount;
        }
        pher[bestPath[bestPath.length-1]][bestPath[0]] += pAmount;
    }
    
    public static int[] updateAllTimeBest (Ant[] ants, double[][] dists, double bestLen) {
        int[] bestPath = null;
        for (int i = 0; i < ants.length; i++) {
            int[] localPath = ants[i].getPath();
            double len = evalIntPathLen(ants[i].getPath(), dists);
            if (len < bestLen) {
                bestPath = localPath;
                bestLen = len;
            }
        }
        return bestPath;
    }

    public static void evapPheromones(double[][] globalPheromones) {
        for (int i = 0; i < globalPheromones.length; i++) {
            for (int j = 0; j < globalPheromones[i].length; j++) {
                globalPheromones[i][j] *= (1 - rho);
            }
        }
    }
    
    ////////////// PATH OPTIMIZATION SECTION
    public static int bruteLim = 5;
    
    public static void bruteAll(int[] path, double[][] dists) {
        int i = 0;
        for ( ; i < dists.length - bruteLim; i+=bruteLim) {
            brute(path, dists, i, bruteLim);
        }
        int leftOver = dists.length - i;
        brute(path, dists, i, leftOver);
    }
    
    public static void brute(int[] path, double[][] dists, int start, int bSize) {
        ArrayList<Integer> section = new ArrayList<Integer>(bSize);
        for (int i = start; i < start + bSize; i++) {
            section.add(path[i]);
        }
        
        ArrayList<ArrayList<Integer>> perms = Brute.listPermutations(new ArrayList<Integer>(section));
        ArrayList<Integer> bestSection = null;
        int before = start == 0? -1 : start - 1;
        int after = (start + bSize == 200)? path[0] : start + bSize;
        
        double bestLen = evalIntPathLen(section, dists, before, after);
        for (int i = 0; i < perms.size(); i++) {
            ArrayList<Integer> sect = perms.get(i);
            double len = evalIntPathLen(sect, dists, before, after);
            if (len < bestLen) {
                bestLen = len;
                bestSection = sect;
            }
        }
        if (bestSection != null) {
            //reconstruct path with newly-arranged section
            int[] newPath = new int[path.length];
            int i = 0;
            for ( ; i < start; i++) {
                newPath[i] = path[i];
            }
            for ( ; i < start + bSize; i++) {
                newPath[i] = bestSection.get(i - start);
            }
            for ( ; i < path.length; i++) {
                newPath[i] = path[i];
            }
            path = newPath;
        }
    }
    
    
    //////////////

    public static double evalIntPathLen (ArrayList<Integer> indices, double[][] dists, int before, int after) {
        double len = 0;
        for (int i = 0; i < indices.size() - 1; i++) {
            len += dists[indices.get(i)][indices.get(i+1)];
        }
        if (before != -1) {
            len += dists[indices.get(0)][before];
        }
        if (after != -1) {
            len += dists[indices.get(indices.size() - 1)][after];
        }
        return len;
    }
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
