import java.util.*;
public class LastResort
{
    public static Random r = new Random();
    public static final int badSteps = 500;
    public static ArrayList<ArrayList<String>> solve (ArrayList<ArrayList<String>> solution, int target) {
        int currScore = Score.scoreProblem(solution);
        int tries = 1;
        while (currScore < target) {
            System.out.println("Try #" + tries);
            System.out.println("Current Score = " + currScore);
            wait(1);
            int[] nextSwap = Opt.bestSwapBruted(solution);
            if (nextSwap[0] == -1) {
                System.out.println("No Better Swap - Taking Steps Backward");
                wait(1);
                acceptWorse(solution,badSteps);
                currScore = Score.scoreProblem(solution);
            }
            else {
                System.out.println("Better Swap Found! " + Arrays.toString(nextSwap));
                swap(solution.get(nextSwap[0]), solution.get(nextSwap[1]), nextSwap[2], nextSwap[3]);
                currScore = Score.scoreProblem(solution);
            }
            tries++;
        }
        return solution;
    }

    public static void wait(int sec) {
        try {
            Thread.sleep(sec * 1000); //1000 milliseconds is one second.
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    public static void acceptWorse(ArrayList<ArrayList<String>> solution, int n) {
        for (int i = 0; i < n; i++) {
            int[] randSwap = swapRandom(solution);
            swap(solution.get(randSwap[0]), solution.get(randSwap[1]), randSwap[2], randSwap[3]);
        }
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
}
