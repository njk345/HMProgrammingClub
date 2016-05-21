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
        return !offBoard(pos) && board.get(pos[1]).get(pos[0]) == Tron.Tile.EMPTY;
    }

    public static boolean offBoard(int[] pos) {
        return pos[0] >= width || pos[0] < 0 || pos[1] >= height || pos[1] < 0;
    }

    public static boolean moveWouldTrap(ArrayList<ArrayList<Tron.Tile>> board, int[] pos, int dir) {
        return TronUtils.adjacentFree(board, TronUtils.movedPos(pos, dir)).isEmpty();
    }

    public static ArrayList<Integer> freeDirecs(ArrayList<ArrayList<Tron.Tile>> board, int[] pos) {
        ArrayList<Integer> freeAdj = TronUtils.adjacentFree(board, pos);
        ArrayList<Integer> freeDirs = new ArrayList<Integer>();
        for (int d : freeAdj) {
            if (direcFree(board, pos, d)) freeDirs.add(d);
        }
        return freeDirs;
    }

    public static boolean direcFree(ArrayList<ArrayList<Tron.Tile>> board, int[] pos, int dir) {
        return TronUtils.adjacentFree(board, pos).contains(dir) && !TronUtils.moveWouldTrap(board, pos, dir);
    }

    public static int[] movedPos(int[] pos, int dir) {
        int[] nextPos = new int[2];
        if (dir == 1) {
            nextPos[0] = pos[0];
            nextPos[1] = pos[1] - 1;
        } else if (dir == 2) {
            nextPos[0] = pos[0];
            nextPos[1] = pos[1] + 1;
        } else if (dir == 3) {
            nextPos[0] = pos[0] - 1;
            nextPos[1] = pos[1];
        } else if (dir == 4) {
            nextPos[0] = pos[0] + 1;
            nextPos[1] = pos[1];
        }
        return nextPos;
    }
    public static ArrayList<Integer> adjacentFree(ArrayList<ArrayList<Tron.Tile>> board, int[] pos) {
        ArrayList<Integer> af = new ArrayList<Integer>();
        for (int i = 1; i <= 4; i++) {
            int[] nextP = movedPos(pos, i);
            if (isFree(board, nextP)) af.add(i);
        }
        return af;
    }
}