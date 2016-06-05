import java.util.*;
public class SimAnneal
{
    static Random r = new Random();
    static final double startTemp = 50;
    static final double tempFactor = 0.999;
    static final int saveRate = 5000;
    static final int maxIterNoImprov = 200000;
    static final double minTemp = .05;
    static final double tolerance = 0;
    static final double randTarget = 0.05;
    static final int iterBeforeTwoSwap = 50000;

    public static ArrayList<ArrayList<String>> solveMethod1(ArrayList<ArrayList<String>> input, double maxMin, int itPerIt) {
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
            //double probUncondTake = r.nextDouble();
            if (bestSwapScore > currBest /*|| Math.abs(probUncondTake - randTarget) <= tolerance*/) {
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
                    if (bestSwapScore != currBest) {
                        System.out.println("Probabilistic Swap! Iteration " + (iter+1) + ": Score = " + bestSwapScore);
                        iterNoImprov = 0;
                    } else iterNoImprov++;

                    currBest = bestSwapScore;
                    swap(solution.get(bestSwap[0]), solution.get(bestSwap[1]), bestSwap[2], bestSwap[3]);
                } else {
                    iterNoImprov++;
                }
            }
            //System.out.println("Iter No Improv. = " + iterNoImprov);

            if (t > minTemp) t *= tempFactor;
            /*if (iterNoImprov >= maxIterNoImprov) {
            System.out.println("About to accept some bad solutions");
            wait(3);
            for (int i = 0; i < 4; i++) {
            int[] randSwap = swapRandom(solution);
            swap(solution.get(randSwap[0]), solution.get(randSwap[1]), randSwap[2], randSwap[3]);
            }
            currBest = Score.scoreProblem(solution);
            iterNoImprov = 0;
            }*/
            if (iterNoImprov >= maxIterNoImprov) {
                System.out.println("About to Brute Force a two-swap");
                wait(3);
                int[] bruteSwap = Opt.bestSwapBruted(solution);
                if (bruteSwap[0] == -1) {
                    System.out.println("No better two-swap - will take steps backward");
                    wait(3);
                    for (int i = 0; i < 4; i++) {
                        int[] randSwap = swapRandom(solution);
                        swap(solution.get(randSwap[0]), solution.get(randSwap[1]), randSwap[2], randSwap[3]);
                    }
                }
                else {
                    System.out.println("Better Swap Found! " + Arrays.toString(bruteSwap));
                    swap(solution.get(bruteSwap[0]), solution.get(bruteSwap[1]), bruteSwap[2], bruteSwap[3]);
                }
                currBest = Score.scoreProblem(solution);
                wait(3);
            }
        }
        /*if (currBest > saveThreshold) {
        FileUtils.outputTemperature(t);
        }*/
        return solution;
    }

    public static ArrayList<ArrayList<String>> solveMethod2(ArrayList<ArrayList<String>> input, double maxMin, int itPerIt) {
        ArrayList<ArrayList<String>> solution = new ArrayList<ArrayList<String>>(input);
        int currScore = Score.scoreProblem(solution);
        int saveThreshold = Score.scoreProblem(FileUtils.loadSolution("Simulated Annealing_Out.txt"));
        double t = startTemp;
        int iter = 0;
        long startTime = System.currentTimeMillis();
        int iterNoImprov = 0;
        while (true) {
            iter++;
            long currTime = System.currentTimeMillis();
            if ((double)(currTime - startTime) / 60000 >= maxMin) {
                break;
            }

            for (int i = 0; i < itPerIt; i++) {
                int[] toSwap = swapRandom(solution);
                int currSubScore = Score.scoreRoom(solution.get(toSwap[0])) + Score.scoreRoom(solution.get(toSwap[1]));

                swap(solution.get(toSwap[0]), solution.get(toSwap[1]), toSwap[2], toSwap[3]);

                int swapScore = Score.scoreRoom(solution.get(toSwap[0])) + Score.scoreRoom(solution.get(toSwap[1]));
                int newOverall = currScore - currSubScore + swapScore;

                double probUncondTake = r.nextDouble();
                if (newOverall >= currScore || Math.abs(probUncondTake - randTarget) <= tolerance) {
                    //definitely accept
                    currScore = newOverall;
                    System.out.println("Iteration " + (iter+1) + ": Score = " + currScore);
                    iterNoImprov = 0;
                }
                else {
                    double p = prob(currScore, newOverall, t);
                    double rand = r.nextDouble();
                    if (p >= rand) {
                        //accept with probability p
                        currScore = newOverall;
                        System.out.println("Iteration " + (iter+1) + ": Score = " + currScore);
                        iterNoImprov = 0;
                    } else {
                        swap(solution.get(toSwap[0]), solution.get(toSwap[1]), toSwap[2], toSwap[3]);
                        iterNoImprov++;
                    }
                }
            }

            if (t > minTemp) t *= tempFactor;
        }
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
