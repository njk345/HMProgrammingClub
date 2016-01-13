import java.util.*;
public class Opt
{
    //a class to hold little optimization functions to perform on
    //solutions that are already pretty good
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

    public static void swap(ArrayList<String> r1, ArrayList<String> r2, int a, int b) {
        String temp = r1.get(a);
        r1.set(a, r2.get(b));
        r2.set(b, temp);
    }
}
