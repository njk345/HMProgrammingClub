/**
 * Created by njk on 4/18/16.
 */
import java.util.*;
public class GreedyBot {
    public static void main(String[] args) {
        Tron.init();
        ArrayList<ArrayList<Tron.Tile>> board = Tron.getMap();
        int[] myPos = TronUtils.findMe(board);
        Bot bot = new Bot(board, myPos);

        while (true) {
            //bot.moveRandomFree();
            bot.moveFirstFree(new int[]{1,2,3,4});
            bot.update();
            bot.logPos();
        }
    }
}
