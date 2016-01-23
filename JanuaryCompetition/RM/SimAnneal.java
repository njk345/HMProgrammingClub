import java.util.*;
public class SimAnneal
{
    static Random r = new Random();
    static final double startTemp = 50;
    static final double tempFactor = 0.999;
    static final int saveRate = 5000;
    static final int maxIterNoImprov = 50000;
    static final double minTemp = .0001;
    static final double tolerance = .0000001;
    static final double randTarget = 0.05;
    static final int iterBeforeTwoSwap = 50000;
    static boolean isStartOfProg = true;
    
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
            /*if (iterNoImprov >= maxIterNoImprov) {
                t = startTemp;
                iterNoImprov = 0;
            }*/
            if (iterNoImprov >= iterBeforeTwoSwap || isStartOfProg) {
                //time to try all two swaps until we get better
                System.out.println("Halting Main Program: Entering Two-Swap Search Mode in 3 sec.\n");
                wait(3);
                int tries = 1;
                while (true) {
                    System.out.print("Two-Swap Tries: " + tries + " || ");
                    int[] twoSwap = swapTwoRandom(solution);
                    int preScore = Score.scoreRoom(solution.get(twoSwap[0])) + Score.scoreRoom(solution.get(twoSwap[1]));
                    swapTwo(solution.get(twoSwap[0]), solution.get(twoSwap[1]), twoSwap[2], twoSwap[3]);
                    int postScore = Score.scoreRoom(solution.get(twoSwap[0])) + Score.scoreRoom(solution.get(twoSwap[1]));
                    System.out.println("Prescore = " + preScore + "; Postscore = " + postScore);
                    if (postScore > preScore) {
                        //finally an improvement, so leave the loop
                        System.out.println("IMPROVEMENT MADE!! Returning to Main Loop in 3 seconds");
                        wait(3);
                        break;
                    }
                    else {
                        //swap back
                        swapTwo(solution.get(twoSwap[0]), solution.get(twoSwap[1]), twoSwap[2], twoSwap[3]);
                    }
                    tries++;
                }
                iterNoImprov = 0;
            }
            
            
            if (t > minTemp) t *= tempFactor;
            isStartOfProg = false;
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
        int r1 = -1, r2 = -1, size = input.size();
        while (r1 == r2) {
            r1 = r.nextInt(size);
            r2 = r.nextInt(size);
        }
        int p1 = r1 % 4;
        int p2 = r2 % 4;
        return new int[]{r1, r2, p1, p2};
    }
    
    public static int[] swapTwoRandom(ArrayList<ArrayList<String>> input) {
        int r1 = -1, r2 = -1, size = input.size();
        while (r1 == r2) {
            r1 = r.nextInt(size);
            r2 = r.nextInt(size);
        }
        int p1 = r.nextInt(4);
        int p2 = (p1 + 1) % 4;
        return new int[]{r1, r2, p1, p2};
    }

    public static void swap(ArrayList<String> r1, ArrayList<String> r2, int p1, int p2) {
        String n1 = r1.get(p1);
        String n2 = r2.get(p2);
        r1.set(p1, n2);
        r2.set(p2, n1);
    }
    
    public static void swapTwo(ArrayList<String> r1, ArrayList<String> r2, int p1, int p2) {
        //swaps two people in two rooms
        String[] r1names = {r1.get(p1), r1.get(p2)};
        String[] r2names = {r2.get(p1), r2.get(p2)};
        r1.set(p1, r2names[0]);
        r1.set(p2, r2names[1]);
        r2.set(p1, r1names[0]);
        r2.set(p2, r1names[1]);
    }

    public static void wait(int sec) {
        try {
            Thread.sleep(sec * 1000);                 //1000 milliseconds is one second.
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
}
