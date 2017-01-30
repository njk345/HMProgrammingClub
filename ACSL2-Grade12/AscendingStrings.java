// Nick Keirstead
// ACSL #2 - Jan 2017

import java.util.Scanner;

public class AscendingStrings {
	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		for (int i = 1; i <= 5; i++) {
			System.out.print("Enter Input " + i + ": ");
			String prob = s.nextLine();
			System.out.println("Output " + i + ": " + solve(prob, null, true));
		}
	}
	public static String solve(String prob, String prev, boolean left) {
		if (prev == null) { // first time through
			String front = prob.substring(0,1);
			return front + " " + solve(prob.substring(1), front, false);
		}
		if (prob.length() == 1) {
			if (Integer.parseInt(prev) < Integer.parseInt(prob)) {
				return prob;
			} else {
				return "";
			}
		}
		if (left) {
			int i = 0;
			String next = "";
			while (i < prob.length()) {
				next += prob.substring(i,i+1);
				if (Integer.parseInt(next) > Integer.parseInt(prev)) {
					return remLeadingZeroes(next) + " " + solve(prob.substring(i+1), next, false);
				}
				i++;
			}
			return "";
		}
		else {
			int i = prob.length() - 1;
			String next = "";
			while (i >= 0) {
				next += prob.substring(i,i+1);
				if (Integer.parseInt(next) > Integer.parseInt(prev)) {
					return remLeadingZeroes(next) + " " + solve(prob.substring(0,i), next, true);
				}
				i--;
			}
			return "";
		}
	}
	private static String remLeadingZeroes(String num) {
		for (int i = 0; i < num.length(); i++) {
			if (!num.substring(i, i+1).equals("0")) {
				return num.substring(i);
			}
		}
		return "";
	}
}	