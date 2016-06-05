//Nick Keirstead
//ACSL Competition 2 - February
import java.util.*;
class ACSLString {
	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		for (int i = 0; i < 5; i++) {
			System.out.println("Enter Line " + (i+1) + " Of Input (Comma Separated): ");
			String in = s.nextLine();
			String[] inParts = in.split(",");
			for (int j = 0; j < inParts.length; j++) {
				inParts[j] = inParts[j].trim();
			}
			System.out.print("Output " + (i+1) + ": ");
			System.out.println(decForm(inParts[0], Integer.parseInt(inParts[1]), Integer.parseInt(inParts[2])));
		}
	}
	public static String decForm(String num, int len, int dec) {
		if (len == 0) return "";
		String sign = !Character.isDigit(num.charAt(0))? "" + num.charAt(0) : "";
		if (!sign.equals("")) num = num.substring(1);
		
		String front = splitOnDec(num)[0], back = splitOnDec(num)[1];
		int totLen = sign.length() + front.length() + dec;
		
		//round to given #decimal places if dec > #givendecimals
		if (back.length() < dec) dec = back.length();
		
		if (sign.length() + front.length() + dec > len) {
			//Error: Cannot fit all the stuff in that length
			String errFront = "";
			String errBack = dec != 0? "." : "";
			for (int i = 0; i < dec; i++) {
				if (i+1 >= len) break; //instructions didn't dictate what to do here, so ill just break
				errBack += "#";
			}
			for (int i = 0; i < len - (dec+1+sign.length()); i++) {
				errFront += "#";
			}
			return errFront + errBack;
		}
		
		if (dec == 0) front = front.substring(0, front.length() - 1);
		
		//Normal Case --> No errors
		String roundedBack = roundDec(back, dec);
		String frontHashes = "";
		for (int i = 0; i < len - (front.length() + dec + sign.length()); i++) {
			frontHashes += "#";
		}
		return sign + frontHashes + front + roundedBack;
	}
	public static String[] splitOnDec(String num) {
		String[] rv = num.split("\\.");
		rv[0] += ".";
		return rv;
	}
	public static String roundDec(String dec, int len) {
		//guarenteed a len >= 0 and <= dec.length()
		String rv = "";
		if (len == 0) return rv;
		for (int i = 0; i < len - 1; i++) {
			rv += dec.charAt(i);
		}
		if (len != dec.length() && Integer.parseInt("" + dec.charAt(len)) >= 5) {
			rv += Integer.toString(Integer.parseInt("" + dec.charAt(len - 1)) + 1);
		} else {
			rv += dec.charAt(len - 1);
		}
		return rv;
	}
}