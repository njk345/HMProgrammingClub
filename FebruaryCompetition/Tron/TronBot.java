//A "smart" diagonal bot strategy
//Goes diagonally for long as possible, then goes up and down,
//leaving only half of its diagonal half space unmarked

import java.util.*;
import java.io.*;

public class TronBot {
    public static void main(String[] args) {
        Tron.init();

        ArrayList<ArrayList<Tron.Tile>> board = Tron.getMap();
        int[] startPos = TronUtils.findMe(board);
        DiagBot bot = new DiagBot(board,startPos);
        
        while (true) {
            bot.logPos();
            bot.logState();
            bot.smartMove();
            bot.update();
        }
    }
    private static class DiagBot extends Bot {
        private static enum MoveState {DIAG, SPACE_FILL};
        private MoveState state;
        private int dir1, dir2, numMoves;
        private int[] dirPriorities;
        private int[] specialTurnPos;
        public DiagBot(ArrayList<ArrayList<Tron.Tile>> board, int[] sp) {
            super(board, sp);
            state = MoveState.DIAG;
            dir1 = sp[0] == 0? 4 : 3;
            dir2 = sp[0] == 0? 2 : 1;
            numMoves = 0;
            dirPriorities = dir1 == 4? new int[]{1,2,4,3} : new int[]{2,1,3,4};
            specialTurnPos = dir1 == 4? new int[]{14,0} : new int[]{1,15};
        }
        public void smartMove() {
            switch (state) {
                case DIAG:
                    int dir = numMoves % 2 == 0? dir1 : dir2;
                    if (direcFree(dir)) {
                        move(dir);
                    } else {
                        moveFirstFree(dirPriorities);
                        state = MoveState.SPACE_FILL;
                    }
                    break;
                case SPACE_FILL:
                    if (pos[0] == specialTurnPos[0] && pos[1] == specialTurnPos[1]) {
                        move(dir1 == 4? 3 : 4);
                        break;
                    }
                    boolean moveFound = false;
                    ArrayList<Integer> lastResorts = new ArrayList<Integer>();
                    for (int i = 0; i < dirPriorities.length; i++) {
                        int d = dirPriorities[i];
                        if (!direcFree(d)) continue;
                        if (moveWouldCutRegion(d)) {
                            lastResorts.add(d);
                            continue;
                        }
                        else if (oppMightCollide(d)) {
                            lastResorts.add(d);
                            continue;
                        }
                        moveFound = true;
                        move(d);
                        break;
                    }
                    if (!moveFound) {
                        if (!lastResorts.isEmpty()) {
                            move(lastResorts.get(0));
                        } else {
                            move(0);
                        }
                    }
            }
            numMoves++;
        }
        private boolean moveWouldCutRegion(int dir) {
            //move would put bot on edge of board
            int[] np = TronUtils.movedPos(pos, dir);
            return np[0] == 0 || np[0] == TronUtils.width-1
            || np[1] == 0 || np[1] == TronUtils.height-1;
        }
        public void logState() {
            Tron.logln("MoveState = " + state);
        }
    }
}