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
        ArrayList<Integer> validDir = new ArrayList<Integer>();
        validDir.add(1);
        validDir.add(2);
        validDir.add(3);
        validDir.add(4);
        int randDir = (int)(Math.random() * 4);
        while (validDir.size() > 0) {
            Tron.logln("randDir = " + (randDir + 1));
            if (direcFree(randDir + 1)) {
                break;
            }
            validDir.remove(randDir);
            randDir = (int)(Math.random() * validDir.size());
        }
        if (validDir.size() == 0) {
            Tron.logln("Out of options");
            move(0);
        }
        else move(randDir + 1);
    }

    public int movesFromOpponent() {
        int[] op = TronUtils.findOpp(board);
        return Math.abs(op[0] - pos[0]) + Math.abs(op[1] - pos[1]);
    }

    public boolean direcFree(int dir) {
        int[] nextPos = new int[2];
        if (dir == 1) {
            nextPos[0] = pos[0];
            nextPos[1] = pos[1] - 1;
        } else if (dir == 2) {
            nextPos[0] = pos[0];
            nextPos[1] = pos[1] + 1;
        } else if (dir == 3) {
            nextPos[0] = pos[0] - 1;
            nextPos[1] = pos[1];
        } else if (dir == 4) {
            nextPos[0] = pos[0] + 1;
            nextPos[1] = pos[1];
        } else {
            return false;
        }
        return !TronUtils.offBoard(nextPos) && TronUtils.isFree(board,nextPos);
    }
    public void logPos() {
        Tron.logln("Bot " + botNum + ": Pos = " + pos[0] + ", " + pos[1]);
    }
}