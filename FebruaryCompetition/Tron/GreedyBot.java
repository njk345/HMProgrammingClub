import java.util.*;
import java.io.*;
public class GreedyBot
{
    public static void main(String[] args) {
        Tron.init();
        ArrayList<ArrayList<Tron.Tile>> board = Tron.getMap();
        int[] myStart = TronUtils.findMe(board);
        Bot bot = new Bot(board, myStart);
        boolean down = true;
        while (true) {
            if (bot.getPos()[1] == 8) {
                down = false;
            }
            //bot.moveFirstFree(new int[]{1,2,3,4});
            //bot.moveRandomFree();
            if (down) bot.move(2);
            else bot.moveFirstFree(new int[]{4,1,3,2});
            bot.update();
        }
    }
}
