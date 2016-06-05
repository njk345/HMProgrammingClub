import java.util.*;
public class SimpleSort
{
    public static final int pSize = 5000;
    public static final int minRoomSize = 4;
    public static ArrayList<ArrayList<String>> solve(ArrayList<String> problem) {
        ArrayList<String> copy = new ArrayList<String>(problem);
        Collections.sort(copy);
        
        ArrayList<ArrayList<String>> solution = new ArrayList<ArrayList<String>>();
        for (int i = 0; i < problem.size(); i += minRoomSize) {
            ArrayList<String> room = new ArrayList<String>();
            room.add(copy.get(i));
            room.add(copy.get(i+1));
            room.add(copy.get(i+2));
            room.add(copy.get(i+3));
            solution.add(room);
        }
        return solution;
    }
}
