import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class HumanPlayer extends Player {

    class InvalidMoveException extends Exception{
        InvalidMoveException(String message){
            super(message);
        }
    }


    public HumanPlayer(String color) {
        super(color);
    }

    public HumanPlayer(String color, int noChips, int noChipsOnBoard) {
        super(color,noChips,noChipsOnBoard);
    }

    public int[] parseUserMove(String move){
        int x1 = ((int)(move.charAt(0)))-65;
        int y1 = Character.getNumericValue(move.charAt(1))-1;
        return new int[]{x1,y1};
    }

    public int[] getUserInput(List<Action> possibleActions){
        int[] loc = new int[2];
        Scanner scan =  new Scanner(System.in);
        boolean error = true;
        String move = null;
        boolean help = false;


        error = true;
        do {
            try {
                System.out.println("Please enter a location to place your piece e.g B4\n(or enter help)");
                if(scan.hasNext()) move = scan.next();
                else {
                    scan.next();
                    continue;
                }
                if(move.matches("[A-H][1-8]")){
                    error=false;
                }else if(move.equals("help")){
                    moves.printActionLoc(possibleActions);
                    continue;
                }else {
                    if(error)System.out.println("WRONG INPUT! Please enter a location to place your piece eg. B4 or enter help");}



            }catch(Exception e) {
                if(error)System.out.println("WRONG FORMAT! Please enter a location to place your piece eg. B4");
                scan.next();
            }
        }while(error);

        return parseUserMove(move);

    }

    @Override
    public State makeMove(State state, boolean print) {
        Boolean error = true;
        List<Action> possibleActions = moves.generateValidActions(state);
        if(possibleActions.isEmpty()) return null;

        Action action = null;

        do{
            int[] loc = getUserInput(possibleActions);

            for(Action a : possibleActions){
                int[] aLoc = a.getPiece().getLocation();
                if(aLoc[0]==loc[0] && aLoc[1]==loc[1]){
                    action = a;
                }
            }
            //if the user inputted move is not valid throw an exception
            if(action == null) {
                try {
                    throw new InvalidMoveException("INVALID MOVE!");
                } catch (InvalidMoveException e) {
                    System.out.println(e.getMessage());
                }
            }else{
                return moves.makeMove(action,state);
            }
        }while(error);

        return null;
    }

    @Override
    public String toString(){
        return "Human Player, color:" + color + ", no of pieces on board:" + noChipsOnBoard;
    }


    @Override
    public Player copyPlayer() {
        return new HumanPlayer(color, noChips, noChipsOnBoard);
    }



}
