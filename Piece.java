public class Piece {

    private String color;
    private int[] location;

    public Piece(String color, int[] location) {
        this.color = color;
        this.location = location;
    }

    public String getColor() {
        return color;

    }

    public String toString(){
        return color + " piece at " + (char)(location[0]+65) + Integer.toString(location[1]+1);
    }

    public Piece copyPiece(){
        int[] newLocation = {location[0], location[1]};
        return new Piece(color, newLocation);
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int[] getLocation() {
        return location;
    }

    public void setLocation(int[] location) {
        this.location = location;
    }


}
