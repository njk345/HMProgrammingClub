import java.util.*;
public class Opt
{
    //a class to hold little optimization functions to perform on
    //solutions that are already pretty good
    public static final int roomSize = 4;
    public static final int[][] tc = twoCombs();
    public static ArrayList<Integer> badRooms(ArrayList<ArrayList<String>> solution, int min) {
        //searches through solution set and finds all rooms with < min common chars
        ArrayList<Integer> rvs = new ArrayList<Integer>();
        int i = 0;
        for (ArrayList<String> room : solution) {
            if (Score.optCommonChars(room) < min) {
                rvs.add(i);
            }
            i++;
        }
        return rvs;
    }

    public static int[] bestSwapBruted(ArrayList<ArrayList<String>> solution) {
        //checks if there is in fact any swap of two elements that can be
        //performed to improve the overall score
        //if it returns the array of -1, it suggests your algorithm has hit 
        //its limit.
        int[] bestSwap = {-1, -1, -1, -1};
        int currOverall = Score.scoreProblem(solution);
        int bestOverallScore = currOverall;
        for (int room1 = 0; room1 < solution.size(); room1++) {
            System.out.println("Checking Room " + (room1 + 1) + " Of " + solution.size());
            for (int room2 = 0; room2 < solution.size(); room2++) {
                if (room1 == room2) continue;
                int preSwap = Score.scoreRoom(solution.get(room1)) + Score.scoreRoom(solution.get(room2));
                for (int name1 = 0; name1 < roomSize; name1++) {
                    for (int name2 = 0; name2 < roomSize; name2++) {
                        swap(solution.get(room1), solution.get(room2), name1, name2);
                        int postSwap = Score.scoreRoom(solution.get(room1)) + Score.scoreRoom(solution.get(room2));
                        int newOverallScore = currOverall - preSwap + postSwap;
                        if (newOverallScore > bestOverallScore) {
                            bestSwap[0] = room1;
                            bestSwap[1] = room2;
                            bestSwap[2] = name1;
                            bestSwap[3] = name2;
                            bestOverallScore = newOverallScore;
                        }
                        swap(solution.get(room1), solution.get(room2), name1, name2);
                    }
                }
            }
        }
        return bestSwap;
    }

    public static SwapData bestTwoSwapBruted(ArrayList<ArrayList<String>> solution) {
        SwapData bestSwap = null;
        int currOverall = Score.scoreProblem(solution);
        int bestOverallScore = currOverall;
        int size = solution.size();
        for (int r1 = 0; r1 < size; r1++) {
            System.out.println("Checking Room " + (r1 + 1) + " Of " + size);
            ArrayList<String> room1 = solution.get(r1);
            for (int r2 = 0; r2 < size; r2++) {
                if (r1 == r2) continue;
                ArrayList<String> room2 = solution.get(r2);
                int preScore = Score.scoreRoom(room1) + Score.scoreRoom(room2);
                for (int[] c1 : tc) {
                    for (int[] c2 : tc) {
                        swapTwo(room1, room2, c1, c2);
                        int postScore = Score.scoreRoom(room1) + Score.scoreRoom(room2);
                        int newOverall = currOverall - preScore + postScore;
                        if (newOverall > bestOverallScore) {
                            bestSwap = new SwapData(r1, r2, c1, c2);
                            bestOverallScore = newOverall;
                        }
                        swapTwo(room1, room2, c1, c2);
                    }
                }
            }
        }
        return bestSwap;
    }

    public static void swap(ArrayList<String> r1, ArrayList<String> r2, int a, int b) {
        String temp = r1.get(a);
        r1.set(a, r2.get(b));
        r2.set(b, temp);
    }

    public static void swapTwo(ArrayList<String> r1, ArrayList<String> r2, int[] r1p, int[] r2p) {
        //swaps two people in two rooms
        String[] r1names = {r1.get(r1p[0]), r1.get(r1p[1])};
        String[] r2names = {r2.get(r2p[0]), r2.get(r2p[1])};
        r1.set(r1p[0], r2names[0]);
        r1.set(r1p[1], r2names[1]);
        r2.set(r2p[0], r1names[0]);
        r2.set(r2p[1], r1names[1]);
    }

    public static int[][] twoCombs() {
        //returns array of all permutations of two elements' indices to pick from a room
        //4 choose 2 perms = 4*3/2 = 6
        int[][] combs = new int[6][2];
        combs[0] = new int[]{0,1};
        combs[1] = new int[]{0,2};
        combs[2] = new int[]{0,3};
        combs[3] = new int[]{1,2};
        combs[4] = new int[]{1,3};
        combs[5] = new int[]{2,3};
        return combs;
    }
    
    public static class SwapData {
        private int r1;
        private int r2;
        private int[] r1p;
        private int[] r2p;
        public SwapData(int r1, int r2, int[] r1p, int[] r2p) {
            this.r1 = r1;
            this.r2 = r2;
            this.r1p = r1p;
            this.r2p = r2p;
        }
        public int[] getRooms() {
            return new int[]{r1, r2};
        }
        public int[] getRoom1Names() {
            return r1p;
        }
        public int[] getRoom2Names() {
            return r2p;
        }
        public String toString() {
            if (this == null) return "Null";
            String rv = "Room1: " + r1 + ", Room2: " + r2 + ", ";
            rv += Arrays.toString(r1p) + ", ";
            rv += Arrays.toString(r2p);
            return rv;
        }
    }
}
