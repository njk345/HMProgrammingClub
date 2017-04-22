import java.util.*;

public class LightsOut {
    static final String[] HEX = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};
    static final String[] BIN = {"0000", "0001", "0010", "0011", "0100", "0101", "0110", "0111", "1000", "1001", "1010", "1011", "1100", "1101", "1110", "1111"};
    
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        String[] boards = new String[6];
        for (int i = 1; i <= 6; i++) {
            System.out.print("Enter Input Line " + i + ": ");
            boards[i-1] = s.nextLine();
        }
        int[] solutions = solveAll(boards);
        for (int i = 1; i <= 5; i++) {
            System.out.println(i + ": " + solutions[i-1]);
        }
    }
    static int[] solveAll(String[] boards) {
        int[] solutions = new int[5];
        for (int i = 0; i < boards.length - 1; i++) {
            solutions[i] = solve(boards[i], boards[i+1]);
        }
        return solutions;
    }
    static int solve(String s1, String s2) {
        int[][] b1 = hexToBoard(s1);
        int[][] b2 = hexToBoard(s2);
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                String tNum = "" + i + j;
                int[][] changed = tapTile(b1, tNum);
                if (boardsSame(changed, b2)) {
                    String official = "" + (i+1) + (j+1);
                    return Integer.parseInt(official);
                }
            }
        }
        return -1; //shouldn't get here
    }
    static boolean boardsSame(int[][] board1, int[][] board2) {
        for (int i = 0; i < board1.length; i++) {
            for (int j = 0; j < board1.length; j++) {
                if (board1[i][j] != board2[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
    static int[][] tapTile(int[][] board, String t) {
        int[][] copy = new int[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j< 8; j++) {
                copy[i][j] = board[i][j];
            }
        }
        int[] tile = {Integer.parseInt(t.substring(0, 1)), Integer.parseInt(t.substring(1, 2))};
        
        int[][] nearbyTiles = near(tile);
        ArrayList<int[]> nearbyValidTiles = nearValid(nearbyTiles);
        
        //first toggle the picked tile itself
        copy[tile[0]][tile[1]] = (copy[tile[0]][tile[1]] == 1) ? 0 : 1;
        
        //now toggle the nearby valid tiles
        for (int[] a : nearbyValidTiles) {
            int row = a[0], col = a[1];
            copy[row][col] = (copy[row][col] == 1) ? 0 : 1;
        }
        
        return copy;
    }
    static ArrayList<int[]> nearValid(int[][] n) {
        ArrayList<int[]> nv = new ArrayList<>();
        for (int[] a : n) {
            if (valid(a)) {
                nv.add(a);
            }
        }
        return nv;
    }
    static int[][] near(int[] t) {
        int[][] n = {
            new int[]{t[0],t[1]+1}, new int[]{t[0],t[1]-1}, new int[]{t[0]+1,t[1]}, new int[]{t[0]-1,t[1]}, new int[]{t[0],t[1]+2}, new int[]{t[0],t[1]-2}, new int[]{t[0]+2,t[1]}, new int[]{t[0]-2,t[1]}, new int[]{t[0]-1,t[1]+1}, new int[]{t[0]-1,t[1]-1}, new int[]{t[0]+1,t[1]+1}, new int[]{t[0]+1,t[1]-1}
        };
        return n;
    }
    static boolean valid(int[] t) {
        return t[0] >= 0 && t[0] < 8 && t[1] >= 0 && t[1] < 8;
    }
    static int[][] hexToBoard(String hex) {
        hex = hex.replaceAll("\\s", "");
        int[][] board = new int[8][8];
        int row = 0, col = 0;
        for (int i = 0; i < hex.length(); i++) {
            if (i % 2 == 0 && i != 0) {
                col = 0;
                row += 1;
            }
            String bin = BIN[indOf(HEX, hex.substring(i, i+1))];
            for (int j = 0; j < 4; j++) {
                board[row][col + j] = Integer.parseInt(bin.substring(j, j+1));
            }
            col += 4;
        }
        return board;
    }
    static int indOf(String[] a, String s) {
        for (int i = 0; i < a.length; i++) {
            if (a[i].equals(s)) return i;
        }
        return -1;
    }
}