import java.util.*;
public class SimAnneal
{
    public static Random r = new Random();
    public static final double startTemp = 50;
    public static final double tempFactor = 0.95;
    public static ArrayList<ArrayList<String>> solve(ArrayList<ArrayList<String>> input, int maxMin, int itPerIt) {
        ArrayList<ArrayList<String>> solution = new ArrayList<ArrayList<String>>(input);
        int bestScore = Score.scoreProblem(solution);
        double t = startTemp;
        long startTime = System.currentTimeMillis();
        int iter = 0;
        while (true) {
            iter++;
            long currTime = System.currentTimeMillis();
            if ((double)(currTime - startTime) / 60000 >= maxMin) {
                break;
            }
         
            int[] bestSwap = new int[4];
            int bestSwapScore = -1;
            for (int i = 0; i < itPerIt; i++) {
                int[] toSwap = swapRandom(solution);
                
                ArrayList<ArrayList<String>> copy = new ArrayList<ArrayList<String>>(solution);
                int currScore = Score.commonChars(solution.get(toSwap[0])) + Score.commonChars(solution.get(toSwap[1]));
                
                swap(copy.get(toSwap[0]), copy.get(toSwap[1]), toSwap[2], toSwap[3]);
                
                int swapScore = Score.commonChars(copy.get(toSwap[0])) + Score.commonChars(copy.get(toSwap[1]));
                int newOverall = bestScore - currScore + swapScore;
                if (newOverall > bestSwapScore) {
                    bestSwapScore = newOverall;
                    bestSwap[0] = toSwap[0];
                    bestSwap[1] = toSwap[1];
                    bestSwap[2] = toSwap[2];
                    bestSwap[3] = toSwap[3];
                }
            }
            
            if (bestSwapScore > bestScore) {
                //definitely accept
                bestScore = bestSwapScore;
                swap(solution.get(bestSwap[0]), solution.get(bestSwap[1]), bestSwap[2], bestSwap[3]);
                System.out.println("Iteration " + (iter+1) + ": Score = " + bestScore);
            }
            else {
                double p = prob(bestScore, bestSwapScore, t);
                double rand = r.nextDouble();
                if (p >= rand) {
                    //accept with probability p
                    bestScore = bestSwapScore;
                    swap(solution.get(bestSwap[0]), solution.get(bestSwap[1]), bestSwap[2], bestSwap[3]);
                    System.out.println("Iteration " + (iter+1) + ": Score = " + bestScore);
                }
            }
            t *= tempFactor;
        }
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
        int p1 = r.nextInt(input.get(r1).size());
        int p2 = r.nextInt(input.get(r2).size());
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
