import java.util.*;
import java.io.*;

public class MinimaxBot
{
    public static void main(String[] args) {
        Tron.init();
        Board board = new Board(Tron.getMap());
        int[] startPos = TronUtils.findMe(board.getValue());
        Bot bot = new Bot(board.getValue(), startPos);
        while (true) {
            TreeNode<Board> boardTree = new TreeNode<Board>(board, null);
            
        }
    }
    private static class Board {
        //just so I don't have to type ArrayList<ArrayList<.... every time
        private ArrayList<ArrayList<Tron.Tile>> value;
        public Board(ArrayList<ArrayList<Tron.Tile>> value) {
            this.value = value;
        }
        public ArrayList<ArrayList<Tron.Tile>> getValue() {
            return this.value;
        }
        public void setValue(ArrayList<ArrayList<Tron.Tile>> value) {
            this.value = value;
        }
    }
}
