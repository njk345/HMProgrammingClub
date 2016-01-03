import java.util.*;
public class Score
{
    //size is the number of names to sort into rooms
    public static final int size = 5000;
    
    //returns the score for an entire solution
    public static int scoreProblem(ArrayList<ArrayList<String>> solution) {
        int totPeople = 0;
        for (ArrayList<String> room : solution) {
            totPeople += room.size();
        }
        if (totPeople != size) {
            String message = totPeople < size? "Not enough people assigned" : "Too many people assigned";
            throw new RuntimeException(message);
        }
        if (containsDuplicates(solution)) {
            throw new RuntimeException("Someone is in more than one room");
        }
        //good to go --> grade it
        int score = 0;
        for (ArrayList<String> room : solution) {
            score += scoreRoom(room);
        }
        return score;
    }

    //returns the score for a single room
    public static int scoreRoom(ArrayList<String> room) {
        if (room.size() < 2) {
            throw new RuntimeException("Room Has Only " + room.size() + " People");
        }
        int commonLetters = commonChars(room);
        return commonLetters * room.size() * room.size();
    }

    //returns how many times any character appears in each string of a given list of strings
    //a character is only counted more than once if it appears that many times in each string
    public static int commonChars(ArrayList<String> names) {
		ArrayList<HashMap<Character, Integer>> chars = new ArrayList<HashMap<Character, Integer>>();
		for (String s : names) {
			HashMap<Character, Integer> freqs = new HashMap<Character, Integer>();
			for (int i = 0; i < s.length(); i++) {
				if (!freqs.containsKey(s.charAt(i))) {
					freqs.put(s.charAt(i), 1);
				}
				else freqs.put(s.charAt(i), freqs.get(s.charAt(i)) + 1);
			}
			chars.add(freqs);
		}
		int tot = 0;
		for (Character c : chars.get(0).keySet()) {
			int lowestFreq = chars.get(0).get(c);
			for (int i = 1; i < chars.size(); i++) {
				if (!chars.get(i).containsKey(c)) {
					lowestFreq = 0;
					break;
				}
				int currFreq = chars.get(i).get(c);
				if (currFreq < lowestFreq) {
					lowestFreq = currFreq;
				}
			}
			tot += lowestFreq;
		}
		return tot;
	}

    public static boolean containsDuplicates(ArrayList<ArrayList<String>> solution) {
        HashMap<String, Integer> names = new HashMap<String, Integer>();
        int totNames = 0;
        for (int i = 0; i < solution.size(); i++) {
            for (int j = 0; j < solution.get(i).size(); j++) {
                totNames++;
                String n = solution.get(i).get(j);
                if (!names.containsKey(n)) {
                    names.put(n, 1);
                }
                else {
                    int freq = names.get(n);
                    names.put(n, freq + 1);
                }
            }
        }
        return names.size() < totNames;
    }
}
