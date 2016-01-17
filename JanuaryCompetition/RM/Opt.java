import java.util.*;
public class Opt
{
    //a class to hold little optimization functions to perform on
    //solutions that are already pretty good
    public static final int roomSize = 4;
    public static ArrayList<Integer> badRooms(ArrayList<ArrayList<String>> solution, int min) {
        //searches through solution set and finds all rooms with < min common chars
        ArrayList<Integer> rvs = new ArrayList<Integer>();
        int i = 0;
        for (ArrayList<String> room : solution) {
            if (Score.commonChars(room) < min) {
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
        int bestOverallScore = Score.scoreProblem(solution);
        for (int room1 = 0; room1 < solution.size(); room1++) {
            System.out.println("Checking Room " + (room1 + 1) + " Of " + solution.size());
            for (int room2 = 0; room2 < solution.size(); room2++) {
                if (room1 == room2) continue;
                for (int name1 = 0; name1 < roomSize; name1++) {
                    for (int name2 = 0; name2 < roomSize; name2++) {
                        int preSwap = Score.scoreRoom(solution.get(room1)) + Score.scoreRoom(solution.get(room2));
                        swap(solution.get(room1), solution.get(room2), name1, name2);
                        int postSwap = Score.scoreRoom(solution.get(room1)) + Score.scoreRoom(solution.get(room2));
                        int newOverallScore = bestOverallScore - preSwap + postSwap;
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

    public static void swap(ArrayList<String> r1, ArrayList<String> r2, int a, int b) {
        String temp = r1.get(a);
        r1.set(a, r2.get(b));
        r2.set(b, temp);
    }
}
