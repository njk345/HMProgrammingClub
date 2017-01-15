//Nick Keirstead
//ACSL #1 - Dec 2016
import java.util.Scanner;
import java.util.Arrays;
import java.util.ArrayList;

public class Agram {
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		for (int i = 0; i < 5; i++) {
			System.out.print("Input Line " + (i+1) + ", Comma Separated: ");
			String[] lineParts = scan.nextLine().split(",");
			String[] input = new String[6];
			for (int j = 0; j < lineParts.length; j++) {
				input[j] = lineParts[j].trim().toUpperCase();
			}
			String output = getMove(input);
			System.out.println("Output " + (i+1) + ": " + output);
		}
	}
	public static String getMove(String[] input) {
		char oppRank = input[0].charAt(0);
		char oppSuit = input[0].charAt(1);
		ArrayList<String> sameSuit = new ArrayList<>();
		for (int i = 1; i < input.length; i++) {
			if (input[i].charAt(1) == oppSuit) {
				sameSuit.add(input[i]);
			}
		}
		if (!sameSuit.isEmpty()) {
			//look for lowest card higher in rank than opponent
			//if none, choose lowest card
			String lowestHigherRank = null;
			for (String s : sameSuit) {
				if (compareRank(s, input[0]) == 0) {
					if (lowestHigherRank == null) {
						lowestHigherRank = s;
					} else {
						if (compareRank(s, lowestHigherRank) == 1) {
							lowestHigherRank = s;
						}
					}
				}
			}
			if (lowestHigherRank != null) {
				return lowestHigherRank;
			} else {
				String lowestRank = sameSuit.get(0);
				for (int i = 1; i < sameSuit.size(); i++) {
					if (compareRank(sameSuit.get(i), lowestRank) == 1) {
						lowestRank = sameSuit.get(i);
					}
				}
				return lowestRank;
			}
		} else { //no cards of the same suit in dealer's hand
			String lowestCard = input[1];
			int lowestIndex = 1;
			for (int i = 2; i < input.length; i++) {
				if (compareRank(lowestCard, input[i]) == 0) {
					lowestCard = input[i];
					lowestIndex = i;
				}
			}
			//find cards tied for lowest rank
			ArrayList<String> ties = new ArrayList<>();
			for (int i = 1; i < input.length; i++) {
				if (i == lowestIndex) continue; //we already counted this one
				if (compareRank(lowestCard, input[i]) == 2) {
					ties.add(input[i]);
				}
			}
			String highestSuit = lowestCard;
			//hunt for ties with a higher suit
			for (String card : ties) {
				if (compareSuit(highestSuit, card) == 1) {
					highestSuit = card;
				}
			}
			return highestSuit;
		}
	}
	public static int compareRank(String c1, String c2) {
		char c1Rank = c1.charAt(0);
		char c2Rank = c2.charAt(0);
		int c1Val = c1Rank == 'T'? 10 : c1Rank == 'J'? 11 : c1Rank == 'Q'? 12 : c1Rank == 'K'? 13 : c1Rank == 'A' ? 1 : Integer.parseInt("" + c1Rank);
		int c2Val = c2Rank == 'T'? 10 : c2Rank == 'J'? 11 : c2Rank == 'Q'? 12 : c2Rank == 'K'? 13 : c2Rank == 'A' ? 1 : Integer.parseInt("" + c2Rank);
		
		return c1Val > c2Val? 0 : c1Val < c2Val? 1 : 2;
	}
	public static int compareSuit(String c1, String c2) {
		char c1Suit = c1.charAt(1);
		char c2Suit = c2.charAt(1);
		char[] suitOrder = {'S', 'H', 'D', 'C'};
		int c1SuitIndex = Arrays.asList(suitOrder).indexOf(c1Suit);
		int c2SuitIndex = Arrays.asList(suitOrder).indexOf(c2Suit);
		return c1SuitIndex < c2SuitIndex? 1 : c2SuitIndex > c2SuitIndex ? 0 : 2;
	}
}