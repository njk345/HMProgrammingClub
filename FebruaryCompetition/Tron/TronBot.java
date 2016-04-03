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
        Bot bot = new Bot(board,startPos);

        //first go diagonally until blocked
        int diagDir1 = startPos[0] == 0? 4 : 3;
        int diagDir2 = diagDir1 == 4? 2 : 1;

        int[] priorities = {1,2,3,4};
        if (diagDir1 == 4) {
            //if started top left, prioritize going right instead of left
            priorities[3] = 3;
            priorities[2] = 4;
        } else {
            //if started bottom right, prioritize going down instead of up
            priorities[0] = 2;
            priorities[1] = 1;
        }

        boolean movingDiag = true;
        int moves = 0;

        Tron.logln("Starting Game:");
        while (true) {
            bot.logPos();
            if (movingDiag) {
                int nextMove = moves % 2 == 0? diagDir1 : diagDir2;
                if (bot.direcFree(nextMove)) {
                    bot.move(nextMove);
                } else {//reached end of moving diagonally
                    bot.moveFirstFree(new int[]{1,2,3,4});
                    movingDiag = false;
                }
                moves++;
            } else {//can't move diagonally any more
                bot.moveFirstFree(priorities);
            }
            bot.update();
        }
    }
    private static class DiagBot extends Bot {
        private static enum MoveState {DIAG, SPACE_FILL};
        private MoveState state;
        private int dir1, dir2;
        private int[] dirPriorites;
        public DiagBot(ArrayList<ArrayList<Tron.Tile>> board, int[] sp) {
            super(board, sp);
            state = MoveState.DIAG;
            dir1 = sp[0] == 0? 4 : 3;
            dir2 = sp[0] == 0? 2 : 1;
            dirPriorities = new int[]{1,2,3,4};
            
        }
        
        private static boolean moveWouldCutRegion(int dir) {
            //move would put bot on edge of board
            int[] np = TronUtils.movedPos(board, pos, dir);
            return np[0] == 0 || np[0] == TronUtils.width-1
            || np[1] == 0 || np[1] == TronUtils.height-1;
        }
    }
}