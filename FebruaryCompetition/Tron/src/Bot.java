import java.util.*;
public class Bot {
    //an object to be used to perform certain actions in a game
    //such as keeping track of the current board state, sending moves,
    //and processing optimal next moves

    protected int[] pos;
    protected ArrayList<ArrayList<Tron.Tile>> board;
    public Bot(ArrayList<ArrayList<Tron.Tile>> board, int[] startPos) {
        this.board = board;
        this.pos = new int[2];
        this.pos[0] = startPos[0];
        this.pos[1] = startPos[1];
    }

    public void update() {
        board = Tron.getMap();
    }

    public ArrayList<ArrayList<Tron.Tile>> getBoard() {
        return board;
    }

    public int[] getPos() {
        return pos;
    }
    //sends a move and updates bot's own account of its position
    public void move(int dir) {
        if (dir == 1) { //up
            Tron.sendMove(Tron.Direction.SOUTH);
            pos[1] -= 1;
        }
        else if (dir == 2) {
            Tron.sendMove(Tron.Direction.NORTH);
            pos[1] += 1;
        }
        else if (dir == 3) {
            Tron.sendMove(Tron.Direction.WEST);
            pos[0] -= 1;
        }
        else if (dir == 4){
            Tron.sendMove(Tron.Direction.EAST);
            pos[0] += 1;
        }
        else if (dir == 0){ //move randomly
            int r = (int) (Math.random() * 4) + 1;
            move(r);
        }
        else {
            Tron.logln("Invalid Move Direction " + dir);
        }
    }
    //moves in first free direction from an array of directions.
    //user is thus able to prioritize certain directions over others.
    public void moveFirstFree(int[] dirPriorities) {
        boolean moveFound = false;
        for (int dir : dirPriorities) {
            if (direcFree(dir)) {
                move(dir);
                moveFound = true;
                break;
            }
        }
        if (!moveFound) move(0); //go randomly ... accept fate
    }

    public void moveRandomFree() {
        ArrayList<Integer> freeDirs = freeDirecs();
        if (freeDirs.isEmpty()) move(0);
        else {
            int rand = (int)(Math.random() * freeDirs.size());
            move(freeDirs.get(rand));
        }
    }

    public int movesFromOpponent() {
        int[] op = TronUtils.findOpp(board);
        return Math.abs(op[0] - pos[0]) + Math.abs(op[1] - pos[1]);
        //Taxicab metric!
    }
    public int movesFromOpponent(int[] pos, int[] oppPos) {
        return Math.abs(oppPos[0] - pos[0]) + Math.abs(oppPos[1] - pos[1]);
        //Taxicab metric!
    }
    /*
    public boolean oppMightCollide(int dir) {
        if (movesFromOpponent() > 2) return false;
        int[] op = TronUtils.findOpp(board);
        int[] np = TronUtils.movedPos(pos,dir);
        return movesFromOpponent(np, op) == 1;
    }*/
    
    public boolean moveWouldTrap(int dir) {
        return TronUtils.adjacentFree(board, TronUtils.movedPos(pos, dir)).isEmpty();
    }

    public ArrayList<Integer> freeDirecs() {
        return TronUtils.adjacentFree(board, pos);
    }

    public boolean direcFree(int dir) {
        return freeDirecs().contains(dir);
    }

    public void logPos() {
        Tron.logln("Pos = " + pos[0] + ", " + pos[1]);
    }
}