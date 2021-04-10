import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/*
 * Dummy Player generates all the valid actions
 * chooses one randomly, and plays
 */
public class DummyPlayer extends Player {


    public DummyPlayer(String color) {
        super(color);
    }

    public DummyPlayer(String color, int noChips, int noChipsOnBoard) {
        super(color,noChips,noChipsOnBoard);
    }

    @Override
    public State makeMove(State state, boolean print){
        //generate valid moves
        List<Action> possibleActions = moves.generateValidActions(state);
        if(possibleActions.isEmpty()) return null;

        Random rand = new Random();
        //choose a random action
        Action action = possibleActions.get(rand.nextInt(possibleActions.size()));
        if(print)System.out.println("Move chosen by Dummy Player: " + action.toString());

        return moves.makeMove(action,state);

    }

    @Override
    public String toString(){
        return "Dummy Player, color:" + color + ", no of pieces on board:" + noChipsOnBoard;
    }


    @Override
    public Player copyPlayer() {
        return new DummyPlayer(color, noChips, noChipsOnBoard);
    }


}
