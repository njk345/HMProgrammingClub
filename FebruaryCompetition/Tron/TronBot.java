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
            } else {
                //figure out directional priorities
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
                bot.moveFirstFree(priorities);
            }
            bot.update();
        }
    }
}