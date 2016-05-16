/**
 * Created by njk on 4/25/16.
 */
import java.util.*;
public class MinimaxBot {
    public static void main(String[] args) {
        Tron.init();
        Board map = new Board(Tron.getMap());
        int[] myPos = map.getMyPos();
        int[] oppPos = map.getOppPos();
        
    }
    private static class Board {
        private ArrayList<ArrayList<Tron.Tile>> board;
        private int[] myPos;
        private int[] oppPos;
        private boolean meLost;
        private boolean oppLost;
        public Board(ArrayList<ArrayList<Tron.Tile>> board) {
            // makes a COPY of the inputted 2d list
            this.board = new ArrayList<ArrayList<Tron.Tile>>(board);
            this.myPos = TronUtils.findMe(board);
            this.oppPos = TronUtils.findMe(board);
            meLost = oppLost = false;
        }
        public ArrayList<ArrayList<Tron.Tile>> getBoard()
        {
            return board;
        }
        public int[] getMyPos() {
            return myPos;
        }
        public int[] getOppPos() {
            return oppPos;
        }
        private void markUsed(int[] pos, boolean me) {
            board.get(pos[1]).set(pos[0], (me? Tron.Tile.TAKEN_BY_ME : Tron.Tile.TAKEN_BY_OPPONENT));
        }
        private void markOccupied(int[] pos, boolean me) {
            board.get(pos[1]).set(pos[0], (me? Tron.Tile.ME : Tron.Tile.OPPONENT));
        }
        public int checkForWinner() {
            //return 0 if user wins, 1 if opponent wins, 2 if game not over, randomly 0 or 1 if both lose
            return meLost && oppLost? (int)(Math.random()) : meLost? 1 : oppLost? 0 : 2;
        }
        public void simMove(int dir, boolean me) {
            int[] nextPos = TronUtils.movedPos((me? myPos : oppPos), dir);
            markOccupied(nextPos, me);
            markUsed((me? myPos : oppPos), me);
            if (me) {
                myPos[0] = nextPos[0];
                myPos[1] = nextPos[1];
                //check for end of game state
                if (TronUtils.offBoard(myPos) || !TronUtils.isFree(board,myPos)) {
                    meLost = true;
                }
            } else {
                oppPos[0] = nextPos[0];
                oppPos[1] = nextPos[1];
                //check for end of game state
                if (TronUtils.offBoard(oppPos) || !TronUtils.isFree(board, oppPos)) {
                    oppLost = true;
                }
            }
        }
    }
}
