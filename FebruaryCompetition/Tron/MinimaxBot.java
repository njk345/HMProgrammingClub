import java.util.*;
import java.io.*;

public class MinimaxBot
{
    private static final int SEARCH_DEPTH = 4;
    public static void main(String[] args) {
        Tron.init();
        Board board = new Board(Tron.getMap());
        int[] startPos = TronUtils.findMe(board.getValue());
        int[] oppPos = new int[2];
        if (startPos[0] == 0) {
            oppPos[0] = 15;
            oppPos[1] = 15;
        }
        Bot bot = new Bot(board.getValue(), startPos);
        while (true) {
            Board currBoard = new Board(bot.getBoard());
            int[] currPos = bot.getPos();
            oppPos = TronUtils.findOpp(currBoard.getValue());
            
            Tree<Board, Integer> boardTree = new Tree<Board, Integer>(board, null, null);
            ArrayList<Integer> freeDirs = bot.freeDirecs();
            if (freeDirs.isEmpty()) {
                bot.move(0); //no moves left --> suicide
            }
            else {
                for (Integer d : freeDirs) {
                    Board postMove = Board.applyMove(currBoard, 0, currPos, d);
                    boardTree.addChild(postMove, d);
                }
            }
        }
    }
    private static Tree<Board, Integer> getMoveTree(Board board, int[] myPos, int[] oppPos) {return null;}
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

        public static Board applyMove(Board before, int player, int[] pos, int dir) {
            //takes in a game state and a player's desired move direction from
            //their current position, returns a Board object with the game state after this move
            ArrayList<ArrayList<Tron.Tile>> newState = new ArrayList<ArrayList<Tron.Tile>>(before.getValue());
            int[] nextPos = TronUtils.movedPos(pos, dir);

            Tron.Tile newTileState = player == 0? Tron.Tile.ME : Tron.Tile.OPPONENT;
            Tron.Tile prevTileState = player == 0? Tron.Tile.TAKEN_BY_ME : Tron.Tile.TAKEN_BY_OPPONENT;

            //set next position to currently occupied
            newState.get(nextPos[1]).set(nextPos[0], newTileState);
            //set previous position to taken
            newState.get(pos[1]).set(pos[0], prevTileState);

            return new Board(newState);
        }
    }
}
