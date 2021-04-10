import java.util.HashSet;
import java.util.Set;

public class Board {

    private Piece[][] gameBoard;
    private int size;


    public Board() {
        size = 8;
        gameBoard = new Piece[size][size];
        initBoard();
    }

    public void initBoard() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                //null = empty square
                gameBoard[i][j] = null;
            }
        }

        //assign white pieces in the middle
        gameBoard[3][3] = new Piece("white", new int[]{3, 3});
        gameBoard[4][4] = new Piece("white", new int[]{4,4});
        //assign black pieces in the middle
        gameBoard[3][4] = new Piece("black", new int[]{3,4});
        gameBoard[4][3] = new Piece("black", new int[]{4,3});

    }

    public void printBoard() {
        String color = "";
        String print = "";
        System.out.print("     ");
        for (int no = 1; no <= size; no++) {
            System.out.print("  " + no + "  ");
        }
        System.out.println();
        System.out.print("      ");
        for (int k = 0; k < size; k++) {
            System.out.print("-----");
        }
        System.out.println();
        for (int i = 0; i < size; i++) {
            char letter = (char) (i + 65);
            System.out.print("  " + letter + "  | ");
            for (int j = 0; j < size; j++) {
                Piece piece = gameBoard[i][j];
                if(piece != null) {
                    color = piece.getColor();
                    if (color.equals("black")) print = "b ";
                    if (color.equals("white")) print = "w ";
                }else print = "  ";
                System.out.print(print + " | ");
            }
            System.out.println();
            System.out.print("      ");
            for (int k = 0; k < size; k++) {
                System.out.print("-----");
            }
            System.out.println();
        }
        System.out.println();
    }

    //returns a copy of this board
    public Board copyBoard() {
        Board newBoard = new Board();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if(gameBoard[i][j]!=null)
                newBoard.gameBoard[i][j] = gameBoard[i][j].copyPiece();
            }
        }
        return newBoard;
    }


    public Piece[][] getGameBoard() {
        return gameBoard;
    }

    public void setGameBoard(Piece[][] gameBoard) {
        this.gameBoard = gameBoard;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }


}
