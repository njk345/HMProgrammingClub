import java.util.*;
public class HashSort
{
    public static ArrayList<HashMap<Character, Integer>> nameLetterFreqs(ArrayList<String> names) {
        ArrayList<HashMap<Character, Integer>> freqs = new ArrayList<HashMap<Character, Integer>>();
        for (String s : names) {
            HashMap<Character, Integer> letts = new HashMap<Character, Integer>();
            for (int i = 0; i < s.length(); i++) {
                char c = s.charAt(i);
                if (letts.containsKey(c)) {
                    letts.put(c, letts.get(c) + 1);
                } else {
                    letts.put(c, 1);
                }
            }
            freqs.add(letts);
        }
        System.out.println(freqs.toString());
        return freqs;
    }
    
}
