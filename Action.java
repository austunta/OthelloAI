import java.util.Iterator;
import java.util.List;
import java.util.Stack;

public class Action {

    private Piece piece;
    private List<String> flankDirections;


    public Action(Piece piece, List<String> flankDirection) {
        this.piece = piece;
        this.flankDirections = flankDirection;
    }


    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public String toString(){
        String directions = "";

        for(String s: flankDirections) directions+= "(" + s + ") ";

        return piece.toString() + "\noutflanking in following directions: " + directions;
    }

    public void printActionLoc(){
        int[] loc = piece.getLocation();
        System.out.printf("(%c%d) ",(char)(loc[0]+65), loc[1]+1);
    }

    public List<String> getFlankDirections() {
        return flankDirections;
    }

    public void setFlankDirections(Stack<String> flankDirections) {
        this.flankDirections = flankDirections;
    }



}
