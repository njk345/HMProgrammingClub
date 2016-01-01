//Nick Keirstead

import java.util.Scanner;

public class Gullible {
	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		double n;
		do {
			System.out.print("Enter any number other than 5: ");
			n = s.nextDouble();
		} while (n != 5);
		System.out.println("Hey! you weren't supposed to enter 5!");
	}
}