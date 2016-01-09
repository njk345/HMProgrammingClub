import java.util.*;
public class Chmod {
  public static char[] keys = {'s', 's', 't'};
  public static void main (String[] args) {
    int[][] inputs = new int[5][4];
    getInput(inputs);
    for (int[] inp : inputs) {
      int spec = inp[0];
      int[] oct = new int[]{inp[1],inp[2],inp[3]};
      System.out.println("Solution: " + output(oct, spec));
    }
  }
  public static void getInput(int[][] input) {
    Scanner s = new Scanner(System.in);
    for (int i = 0; i < 5; i++) {
      int[] inpCase = new int[4];
      System.out.print("Enter Input Case " + (i+1) + " (Comma Separated, Spaces Dont Matter): ");
      String line = s.nextLine();
      String[] lSplit = line.replaceAll(" ", "").split(",");
      if (lSplit.length != inpCase.length) {
        throw new RuntimeException("Error Reading Input: Please Run Again and Check Input Format.");
      }
      for (int j = 0; j < lSplit.length; j++) {
        inpCase[j] = Integer.parseInt(lSplit[j]);
      }
      input[i] = inpCase;
    }
  }
  public static String output(int[] oct, int spec) {
    String rv = "";
    int[][] bins = {octToBin(oct[0]), octToBin(oct[1]), octToBin(oct[2])};
    for (int i = 0; i < bins.length; i++) {
      for (int j = 0; j < bins[i].length; j++) {
        rv += bins[i][j];
      }
    rv += " ";
    }
    rv += "and ";
    for (int i = 0; i < 3; i++) {
      int[] bin = bins[i];
      boolean useSpec = (spec-1) == i || spec == 4 && i == 2;
      rv += bin[0] == 1? 'r' : '-';
      rv += bin[1] == 1? 'w' : '-';
      rv += bin[2] == 0? '-' : useSpec? keys[i] : 'x';
      rv += " ";
    }
    return rv.trim();
  }
  public static int[] octToBin(int oct) {
    int[] bin = new int[3];
    bin[0] = oct / 4;
    oct -= bin[0] * 4;
    bin[1] = oct / 2;
    oct -= bin[1] * 2;
    bin[2] = oct;
    return bin;
  }
}
