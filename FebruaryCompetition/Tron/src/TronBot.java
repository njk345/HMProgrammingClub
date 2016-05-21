/**
 * Created by njk on 4/18/16.
 * An Intermediate Greedy Bot Strategy
 */
import java.util.*;

public class TronBot {
    private static final int TOLERANCE = 3;
    public static void main(String[] args) {
        Tron.init();
        ArrayList<ArrayList<Tron.Tile>> board = Tron.getMap();
        int[] myPos = TronUtils.findMe(board);
        Bot bot = new Bot(board, myPos);

        int[] dirs = {1,2,3,4};

        while (true) {
            //bot.moveRandomFree();
            //bot.moveFirstFree(new int[]{1,2,3,4});
            boolean moveFound = false;
            for (int d = 0; d < dirs.length; d++) {
                if (TronUtils.direcFree(board, bot.getPos(), dirs[d]) /*&& !moveWouldLinearTrap(board, bot.getPos(), dirs[d], TOLERANCE)*/) {
                    moveFound = true;
                    bot.move(dirs[d]);
                    break;
                }
            }
            if (!moveFound) bot.move(0);
            bot.update();
            bot.logPos();
        }
    }
    /*returns whether or not a move would trap the bot
      in a width/height-1 row/column of tiles from which it could not escape.
     */
    private static boolean moveWouldLinearTrap(ArrayList<ArrayList<Tron.Tile>> map, int[] pos, int dir, int tolerance) {
        boolean itsATrap = true;
        if (dir == 1 || dir == 2) {
            int yEnd = dir == 1? 16 : -1;
            int yIncr = dir == 1? 1 : -1;
            int checks = 0;
            for (int y = pos[1] + yIncr; y != yEnd; y += yIncr) {
                checks++;
                int[] left = {pos[0] - 1, y};
                int[] right = {pos[0] + 1, y};
                if (TronUtils.isFree(map, left) || TronUtils.isFree(map, right) || checks == tolerance) {
                    itsATrap = false;
                    break;
                }
            }
        } else { //dir == 3 or 4
            int xEnd = dir == 3? -1 : 16;
            int xIncr = dir == 3? -1 : 1;
            int checks = 0;
            for (int x = pos[0] + xIncr; x != xEnd; x += xIncr) {
                checks++;
                int[] up = {x, pos[1] - 1};
                int[] down = {x, pos[1] + 1};
                if (TronUtils.isFree(map, up) || TronUtils.isFree(map, down) || checks == tolerance) {
                    itsATrap = false;
                    break;
                }
            }
        }
        return itsATrap;
    }
}
