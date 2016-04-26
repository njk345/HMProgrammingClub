/**
 * Created by njk on 4/25/16.
 */
import java.util.*;
public class MinimaxBot {
    public static void main(String[] args) {
        Tron.init();
    }
    private static class Board {
        private ArrayList<ArrayList<Tron.Tile>> board;
        private int[] myPos;
        private int[] oppPos;
        public Board(ArrayList<ArrayList<Tron.Tile>> board) {
            this.board = new ArrayList<ArrayList<Tron.Tile>>(board);
            this.myPos = TronUtils.findMe(board);
            this.oppPos = TronUtils.findMe(board);
        }
        public ArrayList<ArrayList<Tron.Tile>> getBoard() {
            return board;
        }
        private void markUsed(int[] pos, boolean me) {
            board.get(pos[1]).set(pos[0], (me? Tron.Tile.TAKEN_BY_ME : Tron.Tile.TAKEN_BY_OPPONENT));
        }
        private void markOccupied(int[] pos, boolean me) {
            board.get(pos[1]).set(pos[0], (me? Tron.Tile.ME : Tron.Tile.OPPONENT));
        }
        public void simMove(int dir, boolean me) {
            if (me) {
                int[] nextPos = TronUtils.movedPos(myPos, dir);

            }
        }
    }
}
