import java.util.*;
public class SimAnneal
{
    public static Random r = new Random();
    public static final double startTemp = 50;
    public static final double tempFactor = 0.999;
    public static final int saveRate = 5000;
    public static final int maxIterNoImprov = 50000;
    public static final double minTemp = .0001;
    public static final double tolerance = .0000001;
    public static final double randTarget = 0.05;
    public static ArrayList<ArrayList<String>> solve(ArrayList<ArrayList<String>> input, double maxMin, int itPerIt) {
        ArrayList<ArrayList<String>> solution = new ArrayList<ArrayList<String>>(input);
        int currBest = Score.scoreProblem(solution);
        int saveThreshold = Score.scoreProblem(FileUtils.loadSolution("Simulated Annealing_Out.txt"));
        double t = startTemp; //could also load temp stored in file, but makes it slower for some reason
        long startTime = System.currentTimeMillis();
        int iter = 0;
        int iterNoImprov = 0;
        while (true) {
            iter++;
            long currTime = System.currentTimeMillis();
            if ((double)(currTime - startTime) / 60000 >= maxMin) {
                break;
            }
            //this if statement slows things down considerably
            /*if (currBest > saveThreshold && currTime % saveRate == 0) {
                FileUtils.output("Nick Keirstead", solution, 4);
            }*/
         
            //store the best overall swap from the inner loop
            int[] bestSwap = new int[4];
            int bestSwapScore = -1;
            for (int i = 0; i < itPerIt; i++) {
                int[] toSwap = swapRandom(solution);
                int currSubScore = Score.scoreRoom(solution.get(toSwap[0])) + Score.scoreRoom(solution.get(toSwap[1]));
                
                swap(solution.get(toSwap[0]), solution.get(toSwap[1]), toSwap[2], toSwap[3]);
                
                int swapScore = Score.scoreRoom(solution.get(toSwap[0])) + Score.scoreRoom(solution.get(toSwap[1]));
                int newOverall = currBest - currSubScore + swapScore;
                
                if (newOverall > bestSwapScore) {
                    //take note of new best swap
                    bestSwapScore = newOverall;
                    bestSwap[0] = toSwap[0];
                    bestSwap[1] = toSwap[1];
                    bestSwap[2] = toSwap[2];
                    bestSwap[3] = toSwap[3];
                }
                //always swap back
                swap(solution.get(toSwap[0]), solution.get(toSwap[1]), toSwap[2], toSwap[3]);
            }
            double probUncondTake = r.nextDouble();
            if (bestSwapScore >= currBest || Math.abs(probUncondTake - randTarget) <= tolerance) {
                //definitely accept
                currBest = bestSwapScore;
                swap(solution.get(bestSwap[0]), solution.get(bestSwap[1]), bestSwap[2], bestSwap[3]);
                System.out.println("Iteration " + (iter+1) + ": Score = " + currBest);
                iterNoImprov = 0;
            }
            else {
                double p = prob(currBest, bestSwapScore, t);
                double rand = r.nextDouble();
                if (p >= rand) {
                    //accept with probability p
                    currBest = bestSwapScore;
                    swap(solution.get(bestSwap[0]), solution.get(bestSwap[1]), bestSwap[2], bestSwap[3]);
                    System.out.println("Iteration " + (iter+1) + ": Score = " + currBest);
                    iterNoImprov = 0;
                } else {
                    iterNoImprov++;
                }
            }
            if (iterNoImprov >= maxIterNoImprov) {
                t = startTemp;
                iterNoImprov = 0;
            }
            
            
            if (t > minTemp) t *= tempFactor;
        }
        /*if (currBest > saveThreshold) {
            FileUtils.outputTemperature(t);
        }*/
        return solution;
    }

    public static double prob(int currScore, int potScore, double temp) {
        return Math.exp(-(currScore - potScore) / temp);
    }

    public static int[] swapRandom(ArrayList<ArrayList<String>> input) {
        int r1 = -1, r2 = -1;
        while (r1 == r2) {
            r1 = r.nextInt(input.size());
            r2 = r.nextInt(input.size());
        }
        int p1 = r1 % 4;
        int p2 = r2 % 4;
        return new int[]{r1, r2, p1, p2};
    }

    public static void swap(ArrayList<String> r1, ArrayList<String> r2, int p1, int p2) {
        String n1 = r1.get(p1);
        String n2 = r2.get(p2);
        r1.set(p1, n2);
        r2.set(p2, n1);
    }

    public static void wait(int sec) {
        try {
            Thread.sleep(sec * 1000);                 //1000 milliseconds is one second.
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
}
