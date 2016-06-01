/**
 * Created by njk on 4/25/16.
 */
import java.util.*;

//THIS IS BUGGY STILL AND DOES NOT WORK

public class MinimaxBot {
    private static class UserWin extends Exception {
        public UserWin() {
            super("User Won");
        }
    }
    private static class OppWin extends Exception {
        public OppWin() {
            super("Opponent Won");
        }
    }
    private static class Tie extends Exception {
        public Tie() {
            super("Tie");
        }
    }
    //public static final int DEPTH = 1; //how many moves to look ahead in minimax
    public static void main(String[] args) {
        Tron.init();
        ArrayList<ArrayList<Tron.Tile>> map = Tron.getMap();
        int[] myPos = TronUtils.findMe(map);
        int[] oppPos = TronUtils.findOpp(map);
        int turns = 0;

        while (true) {
            turns++;
            int[][] results = new int[4][4];
            for (int a = 0; a < 4; a++) {
                for (int b = 0; b < 4; b++) {
                    try {
                        results[a][b] = voronoi(simMoves(map, a, b));
                    } catch(Exception e) {
                        results[a][b] = (e instanceof Tie)? 0 : (e instanceof UserWin)? 257 : -257;
                    }
                }
            }
            int[] miniValues = new int[4];
            for (int a = 0; a < miniValues.length; a++) {
                int min = Integer.MAX_VALUE;
                for (int x = 0; x < results[a].length; x++) {
                    if (results[a][x] < min) {
                        min = results[a][x];
                    }
                }
                miniValues[a] = min;
            }
            int max = Integer.MIN_VALUE;
            int maxIndex = -1;
            for (int i = 0; i < miniValues.length; i++) {
                if (miniValues[i] < max) {
                    max = miniValues[i];
                    maxIndex = i;
                }
            }
            Tron.sendMove(maxIndex == 0? Tron.Direction.NORTH : maxIndex == 1? Tron.Direction.EAST :
            maxIndex == 2? Tron.Direction.SOUTH : Tron.Direction.WEST);
        }
    }
    public static ArrayList<ArrayList<Tron.Tile>> simMoves(ArrayList<ArrayList<Tron.Tile>> map, int myMove, int oppMove) throws Tie, OppWin, UserWin {
        int[] myPos = TronUtils.findMe(map);
        int[] oppPos = TronUtils.findOpp(map);
        ArrayList<ArrayList<Tron.Tile>> altered = new ArrayList<ArrayList<Tron.Tile>>(map); //copy it over first
        altered.get(myPos[1]).set(myPos[0], Tron.Tile.TAKEN_BY_ME);
        altered.get(oppPos[1]).set(oppPos[0], Tron.Tile.TAKEN_BY_OPPONENT);

        ArrayList<Integer> myAf = TronUtils.adjacentFree(map, myPos);
        ArrayList<Integer> oppAf = TronUtils.adjacentFree(map, oppPos);

        if (!myAf.contains(myMove) && !oppAf.contains(oppMove)) {
            throw new Tie();
        }
        if (!myAf.contains(myMove)) {
            throw new OppWin();
        }
        if (!oppAf.contains(oppMove)) {
            throw new UserWin();
        }

        int[] myNextPos = TronUtils.movedPos(myPos, myMove), oppNextPos = TronUtils.movedPos(oppPos, oppMove);
        altered.get(myNextPos[1]).set(myNextPos[0], Tron.Tile.ME);
        altered.get(oppNextPos[1]).set(oppNextPos[0], Tron.Tile.OPPONENT);
        return altered;
    }
    public static int voronoi(ArrayList<ArrayList<Tron.Tile>> map) {
        int[] myPos = TronUtils.findMe(map);
        int[] oppPos = TronUtils.findOpp(map);
        int[][] myDists = new int[16][16];
        ArrayList<int[]> front = new ArrayList<>();
        front.add(myPos);

        for (int dist = 1; !front.isEmpty(); dist++) {
            ArrayList<int[]> newFront = new ArrayList<>();
            for (int[] a : front) {
                ArrayList<Integer> nearbyFree = TronUtils.adjacentFree(map, a);
                for (int dir : nearbyFree) {
                    int[] nextPos = TronUtils.movedPos(a, dir);
                    if (myDists[nextPos[1]][nextPos[0]] == 0) {
                        newFront.add(nextPos);
                        myDists[nextPos[1]][nextPos[0]] = dist;
                    }
                }
            }
            front.clear();
            for (int[] a : newFront) {
                front.add(a);
            }
        }

        int[][] oppDists = new int[16][16];
        ArrayList<int[]> oppFront = new ArrayList<>();
        oppFront.add(oppPos);

        for (int dist = 1; !oppFront.isEmpty(); dist++) {
            ArrayList<int[]> newFront = new ArrayList<>();
            for (int[] a : oppFront) {
                ArrayList<Integer> nearbyFree = TronUtils.adjacentFree(map, a);
                for (int dir : nearbyFree) {
                    int[] nextPos = TronUtils.movedPos(a, dir);
                    if (oppDists[nextPos[1]][nextPos[0]] == 0) {
                        newFront.add(nextPos);
                        oppDists[nextPos[1]][nextPos[0]] = dist;
                    }
                }
            }
            oppFront.clear();
            for (int[] a : newFront) {
                oppFront.add(a);
            }
        }

        int total = 0;
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                if (myDists[i][j] == oppDists[i][j]) continue;
                if (oppDists[i][j] == 0 || (myDists[i][j] < oppDists[i][j] && myDists[i][j] != 0)) {
                    total++;
                } else total--;
            }
        }
        return total;
    }
    /*public static class Board {
        private ArrayList<ArrayList<Tron.Tile>> board;
        private int[] myPos;
        private int[] oppPos;
        private boolean meLost;
        private boolean oppLost;
        public Board(ArrayList<ArrayList<Tron.Tile>> board) {
            // makes a COPY of the inputted 2d list
            this.board = new ArrayList<ArrayList<Tron.Tile>>(board);
            this.myPos = TronUtils.findMe(board);
            this.oppPos = TronUtils.findOpp(board);
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
            if (!TronUtils.offBoard(pos)) {
                board.get(pos[1]).set(pos[0], (me ? Tron.Tile.TAKEN_BY_ME : Tron.Tile.TAKEN_BY_OPPONENT));
            }
        }
        private void markOccupied(int[] pos, boolean me) {
            if (!TronUtils.offBoard(pos)) {
                board.get(pos[1]).set(pos[0], (me ? Tron.Tile.ME : Tron.Tile.OPPONENT));
            }
        }
        public int checkForWinner() {
            //return 0 if user wins, 1 if opponent wins, 2 if both lose, 3 if game not over
            return meLost && oppLost? 2: meLost? 1 : oppLost? 0 : 3;
        }
        public void simMove(int dirMe, int dirOpp) {
            int[] myNextPos = TronUtils.movedPos(myPos, dirMe);
            int[] oppNextPos = TronUtils.movedPos(oppPos, dirOpp);
            markOccupied(myNextPos, true);
            markOccupied(oppNextPos, false);
            markUsed(myPos, true);
            markUsed(oppPos, false);
            myPos[0] = myNextPos[0];
            myPos[1] = myNextPos[1];
            if (TronUtils.offBoard(myPos) || !TronUtils.isFree(board, myPos)) {
                meLost = true;
            }
            oppPos[0] = oppNextPos[0];
            oppPos[1] = oppNextPos[1];
            if (TronUtils.offBoard(oppPos) || !TronUtils.isFree(board, oppPos)) {
                oppLost = true;
            }
        }
        public String toString() {
            //if (meLost && oppLost) return "Both Players Dead";
            if (meLost) return "User Dead";
            if (oppLost) return "Opponent Dead";
            return "My Pos: " + Arrays.toString(myPos) + ", Opp. Pos: " + Arrays.toString(oppPos);
        }
    }*/
    /*//fills tree to DEPTH levels with children
    private static void fillMoveTree(Tree<Board, Integer> moveTree) {
        if(moveTree.getDepth() >= DEPTH) {
            return; //stop here
        }
        else {
            boolean userMove = moveTree.getDepth() % 2 == 0;
            produceChildren(moveTree, userMove);
            for (Tree<Board, Integer> kid : moveTree.getChildren()) {
                fillMoveTree(kid);
            }
        }
    }
    //dummy method for now
    private static double evalBoard(Board map) {
        switch(map.checkForWinner()) {
            case 0:
                return 100;
            case 1:
                return -100;
            case 2:
                //return difference in number of my free adjacent tiles and those of opponent, times 25
                int freeDiff = TronUtils.adjacentFree(map.getBoard(), map.getMyPos()).size()
                        - TronUtils.adjacentFree(map.getBoard(), map.getOppPos()).size();
                return freeDiff * 25;
            case 3:
                return -50;
        }
        return Integer.MIN_VALUE; //NOT POSSIBLE
    }
    private static void produceChildren(Tree<Board, Integer> moveTree, boolean myMove) {
        Board currMap = moveTree.getData();
        //ArrayList<Integer> possibleMoves = TronUtils.freeDirecs(currMap.getBoard(), currMap.getMyPos());
        //for (int d : possibleMoves) {
        for (int d = 1; d <= 4; d++) {
            Board childMap = new Board(new ArrayList<ArrayList<Tron.Tile>>(currMap.getBoard()));
            childMap.simMove(d, myMove);
            moveTree.addChild(childMap, d);
        }
        //}
    }*/
}