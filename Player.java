import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Player {

    protected String color;
    protected int noChips;
    protected int noChipsOnBoard;
    protected GameMoves moves;

    public Player(String color) {
        this.color = color;
        noChips = 32;
        noChipsOnBoard = 2;
        moves = new GameMoves();
    }


    public Player(String color, int noChips, int noChipsOnBoard) {
        this.color = color;
        this.noChips = noChips;
        this.noChipsOnBoard = noChipsOnBoard;
    }

    public Player copyPlayer() {
        return new Player(color, noChips, noChipsOnBoard);
    }

    public State makeMove(State state, boolean print){
        return state;
    }

    public String getColor() {
        return color;
    }

    public int getNoChips() {
        return noChips;
    }

    public void setNoChips(int noChips) {
        this.noChips = noChips;
    }

    public int getNoChipsOnBoard() {
        return noChipsOnBoard;
    }

    public void setNoChipsOnBoard(int noChipsOnBoard) {
        this.noChipsOnBoard = noChipsOnBoard;
    }


    public String toString(){
        return "Player, color:" + color + ", no of pieces on board:" + noChipsOnBoard;
    }

}
