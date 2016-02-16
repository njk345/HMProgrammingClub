import java.util.*;
public class TronUtils {
    public static final int width = 16, height = 16;
    private static int[] find(ArrayList<ArrayList<Tron.Tile>> board, Tron.Tile person) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (board.get(y).get(x) == person) {
                    return new int[]{x, y};
                }
            }
        }
        //person is off the board, but shouldn't get here
        return new int[]{-1, -1};
    }
    public static int[] findMe(ArrayList<ArrayList<Tron.Tile>> board) {
        return find(board, Tron.Tile.ME);
    }
    public static int[] findOpp(ArrayList<ArrayList<Tron.Tile>> board) {
        return find(board, Tron.Tile.OPPONENT);
    }
    public static boolean isFree(ArrayList<ArrayList<Tron.Tile>> board, int[] pos) {
        return board.get(pos[1]).get(pos[0]) == Tron.Tile.EMPTY;
    }
    public static boolean offBoard(int[] pos) {
        return pos[0] >= width || pos[0] < 0 || pos[1] >= height || pos[1] < 0;
    }
}