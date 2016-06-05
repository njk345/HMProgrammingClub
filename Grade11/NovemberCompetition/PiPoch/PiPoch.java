//Nick Keirstead
//November Competition
//Problem 3
//Pi-Poch

import java.util.Scanner;

public class PiPoch {
	public static final int[] epoch = {1970,0,0,0,0};
	
	public static final int SECSPERMIN = 60;
	public static final int SECSPERHOUR = SECSPERMIN * 60;
	public static final int SECSPERDAY = SECSPERHOUR * 24;
	public static final int SECSPERYEAR = SECSPERDAY * 365;
	public static final int[] constants = {SECSPERYEAR, SECSPERDAY, SECSPERHOUR, SECSPERMIN, 1};
	
	public static final double PISECSPERMIN = Math.PI;
	public static final double PISECSPERHOUR = PISECSPERMIN * Math.PI;
	public static final double PISECSPERDAY = PISECSPERHOUR * Math.PI;
	public static final double[] piConstants = {PISECSPERDAY, PISECSPERHOUR, PISECSPERMIN, 1};
	
	public static final String[] words = {"Pi-Day","Pi-Hour","Pi-Minute","Pi-Second"};

	public static void main (String[] args) {
		Scanner scan = new Scanner(System.in);
		
		/*
		- Gregorian calendar date is inputted as year, days, hours, minutes, and seconds.
		- Difference in regular seconds is found between the inputted time and the epoch.
		- Regular seconds difference is split into days, hours, minutes, and seconds in the pi clock
		- Sign is accounted for at the end.
		
		- Dates in Gregorian calendar are stored as 5 element arrays of ints, where
			elements are years, days, hours, minutes, and seconds.
		- Dates in Pi Clock are returned as 5 element arrays of doubles, where 
			first 4 elements are days, hours, minutes, seconds, and last element is the sign (-1 or 1).
			
		Note: I didn't account for leap years, leap centuries, leap seconds, etc.
		*/
		
		System.out.print("Enter Year: ");
		int y = scan.nextInt();
		System.out.print("Enter Day: ");
		int d = scan.nextInt();
		System.out.print("Enter Hour: ");
		int h = scan.nextInt();
		System.out.print("Enter Minute: ");
		int m = scan.nextInt();
		System.out.print("Enter Seconds: ");
		int s = scan.nextInt();
		int[] inputTime = {y, d, h, m, s};
		
		double[] result = piTimeSinceEpoch(inputTime);
		printResult(result);
	}
	
	public static double secsBetweenTimes(int[] t1, int[] t2) {
		double rv = 0;
		for (int i = 0; i < t1.length; i++) {
			rv += (t1[i] - t2[i]) * constants[i];
		}
		return rv;
	}
	
	public static double secsSinceEpoch(int[] time) {
		return secsBetweenTimes(time, epoch);
	}

	public static double[] piTimeSinceEpoch(int[] time) {
    	double[] piTime = new double[5];
		double secsDiff = secsSinceEpoch(time);
		boolean isNeg = secsDiff < 0;
		if (isNeg) secsDiff *= -1;
		
		for (int i = 0; i < piTime.length - 1; i++) {
			double val = secsDiff / piConstants[i];
			if (i != piTime.length - 2) {
				//unless it's seconds, floor the value
				val = Math.floor(val);
			}
			secsDiff -= val * piConstants[i];
			piTime[i] = val;
		}
		piTime[piTime.length - 1] = isNeg? -1 : 1;
		return piTime;
	}
	
	public static void printResult(double[] time) {
		if (time[time.length - 1] == -1) {
			System.out.print("Negative ");
		}
		for (int i = 0; i < time.length - 2; i++) {
			String word = (time[i] == 1.0) ? words[i] : words[i] + "s";
			String timeFormatted = Integer.toString((int)time[i]);
			System.out.print(timeFormatted + " " + word + ", ");
		}
		String word = (time[time.length - 2] == 1.0) ? words[time.length - 2] : words[time.length - 2] + "s";
		String timeFormatted = String.format("%.4f", time[time.length - 2]);
		System.out.print(timeFormatted + " " + word);
	}
}
