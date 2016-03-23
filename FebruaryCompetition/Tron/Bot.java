import java.util.*;
public class Bot {
    //an object to be used to perform certain actions in a game
    //such as keeping track of the current board state, sending moves,
    //and processing optimal next moves

    //TOP LEFT = {0,0}
    //BOTTOM LEFT = {0,15}
    //TOP RIGHT = {15,0}
    //BOTTOM RIGHT = {15,15}
    private int[] pos;
    private int botNum;
    private ArrayList<ArrayList<Tron.Tile>> board;
    public Bot(ArrayList<ArrayList<Tron.Tile>> board, int[] startPos) {
        this.board = board;
        this.pos = new int[2];
        this.pos[0] = startPos[0];
        this.pos[1] = startPos[1];
        botNum = this.pos[0] == 0? 1 : 2;
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
    //moves to first free position in an array of directions
    //user thus controls the order of the directions in the array,
    //prioritizing certains ones over others
    public void moveFirstFree(int[] dirPriorities) {
        boolean moveFound = false;
        for (int dir : dirPriorities) {
            if (direcFree(dir)) {
                move(dir);
                moveFound = true;
                break;
            }
        }
        if (!moveFound) move(0); //go randomly, accept fate
    }

    public void moveRandomFree() {
        ArrayList<Integer> freeDirs = freeDirecs();
        if (freeDirs.size() == 0) move(0);
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
    
    public boolean oppMightCollide(int dir) {
        int[] oppPos = TronUtils.findOpp(board);
        if (oppPos[0] == pos[0] && Math.abs(oppPos[1] - pos[1]) == 2) {
            //in same column and two tiles apart
            if (oppPos[1] - pos[1] < 0) {
                //opponent above user
                return dir == 1; //if dir is 1 (up), collision possible
            } return dir == 2; //if dir is 2 (down), collision possible
        } else if (oppPos[1] == pos[1] && Math.abs(oppPos[0] - pos[0]) == 2) {
            //in same row and two tiles apart
            if (oppPos[0] - pos[0] < 0) {
                //opponent left of user
                return dir == 3; //if dir is 3 (left), collision possible
            } return dir == 4; //if dir is 4 (right), collision possible
        } else {
            return false;
        }
    }

    public ArrayList<Integer> freeDirecs() {
        ArrayList<Integer> freeDirs = new ArrayList<Integer>();
        for (int i = 1; i <= 4; i++) {
            if (direcFree(i)) freeDirs.add(i);
        }
        return freeDirs;
    }

    public boolean direcFree(int dir) {
        if (dir < 1 || dir > 4) return false;
        int[] nextPos = TronUtils.movedPos(pos, dir);
        return !TronUtils.offBoard(nextPos) && TronUtils.isFree(board,nextPos) &&
            !TronUtils.moveWouldTrap(board, pos, dir) && !oppMightCollide(dir);
    }

    public void logPos() {
        Tron.logln("Bot " + botNum + ": Pos = " + pos[0] + ", " + pos[1]);
    }
}