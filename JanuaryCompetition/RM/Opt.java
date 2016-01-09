import java.util.*;
public class Opt
{
    //a class to hold little optimization functions to perform on
    //solutions that are already pretty good
    public static ArrayList<Integer> badRooms(ArrayList<ArrayList<String>> solution, int min) {
        //searches through solution set and finds all rooms with < min common chars
        ArrayList<Integer> rvs = new ArrayList<Integer>();
        for (int i = 0; i < solution.size(); i++) {
            if (Score.commonChars(solution.get(i)) < min) {
                rvs.add(i);
            }
        }
        //System.out.println(rvs.size());
        return rvs;
    }

    public static ArrayList<ArrayList<String>> shuffleBad(ArrayList<ArrayList<String>> sol, int min) {
        ArrayList<ArrayList<String>> solution = new ArrayList<ArrayList<String>>(sol);
        ArrayList<Integer> bad = badRooms(solution, min);
        int bestScore = Score.scoreProblem(solution);
        for (int i = 0; i < bad.size(); i++) {
            int b = bad.get(i);
            ArrayList<String> r1 = solution.get(b);
            for (int j = 0; j < bad.size(); j++) {
                if (j == i) continue;
                int b2 = bad.get(j);
                ArrayList<String> r2 = solution.get(b2);
                int[] bestSwap = getBestSwap(r1, r2);
                if (bestSwap[0] != -1) {
                    swap(r1, r2, bestSwap[0], bestSwap[1]);
                    int newScore = Score.scoreProblem(solution);
                    if (newScore >= bestScore) {
                        bestScore = newScore;
                    }
                    else {
                        //change it back
                        swap(r1, r2, bestSwap[0], bestSwap[1]);
                    }
                }
            }
        }
        return solution;
    }

    public static int[] getBestSwap(ArrayList<String> r1, ArrayList<String> r2) {
        //returns the indices in r1 and r2 which, when switched, will produce
        //the best possible combined common characters score
        int[] best = {-1,-1};
        int bestScore = Score.commonChars(r1) + Score.commonChars(r2);
        for (int i = 0; i < r1.size(); i++) {
            for (int j = 0; j < r2.size(); j++) {
                swap(r1, r2, i, j);
                int totCom = Score.commonChars(r1) + Score.commonChars(r2);
                if (totCom > bestScore) {
                    //keep the swap and store best score
                    bestScore = totCom;
                    best[0] = i;
                    best[1] = j;
                    swap(r1, r2, i, j);
                } else {
                    //undo the swap and keep searching
                    swap(r1, r2, i, j);
                }
            }
        }
        return best;
    }
    public static void swap(ArrayList<String> r1, ArrayList<String> r2, int a, int b) {
        String temp = r1.get(a);
        r1.set(a, r2.get(b));
        r2.set(b, temp);
    }
}
