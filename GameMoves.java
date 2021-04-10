import java.util.*;

public class GameMoves {

    /*
     * make a class for pieces
     * make a class for action
     * each piece holds its location and its color (diagonal horizontal etc)
     * possible actions = piece and type of flank
     * when a user makes a move with a piece, that piece is added to the list
     * rest of pieces in the possible actions are discarded
     * during each move, player's edge piece list is updated
     * when a new piece is placed, add it to the edge piece list
     * when opponent's pieces are outflanked,
     * check the piece, if in the edge list add it to the opponent's edge piece list
     * if not don't
     * every time a new piece is placed, check 8 squares around it, skip empty ones
     * if any is not a edge piece anymore, remove from the list
     *
     * for possible moves, get the current player
     * iterate through opponent's edge piece list
     * check legal moves around each piece
     * each move is a piece
     * store those pieces/moves in a set
     * when a move is made,
     *
     *
     */



    public State makeMove(Action action, State state) {
        Board board = state.getBoard();
        Player currentPlayer = state.getCurrentPlayer();
        Player opponent = state.getOpponent();
        List<String> outFlankDirections = action.getFlankDirections();
        Piece newPiece = action.getPiece();
        int[] loc = newPiece.getLocation();
        //place the new piece
        board.getGameBoard()[loc[0]][loc[1]] = newPiece;
        //increase player's chips-on-board count
        currentPlayer.setNoChipsOnBoard(currentPlayer.getNoChipsOnBoard()+1);
        //add it to the current player's edge list
        //currentPlayer.getEdgePieceList().add(newPiece);

        //execute the action and update the board
        for(String s: outFlankDirections) {
            outFlank(s, newPiece, currentPlayer, opponent, board);
        }


        state.swapPlayers();

        return state;

    }


    public List<Action> generateValidActions(State state){
        List<Action> possibleActions = new LinkedList<>();
        Player curr = state.getCurrentPlayer();
        Player opp = state.getOpponent();
        Board board = state.getBoard();
        Piece[][] gameBoard = board.getGameBoard();
        int boardSize = board.getSize();
        for(int i = 0; i<boardSize;i++){
            for(int j = 0; j<boardSize;j++){
                Piece p = gameBoard[i][j];
                if(p==null) continue;
                if(p.getColor().equalsIgnoreCase(curr.color)) continue;
                if(!isCenter(p,gameBoard)){
                    addLegalMoves(possibleActions,p,state);
                }
            }
        }
        return possibleActions;
    }

    private void addLegalMoves(List<Action> possibleActions, Piece edgePiece, State state) {
        ArrayList<int[]> surroundingSquareCoords = getSurroundingSquareCoords(edgePiece);
        Board board = state.getBoard();
        Piece[][] gameBoard = board.getGameBoard();
        Player currentPlayer = state.getCurrentPlayer();
        Player opponent = state.getOpponent();

        for (int[] coord : surroundingSquareCoords) {

            if(outOfBoard(coord)) continue;

            int x = coord[0];
            int y = coord[1];
            Piece piece = gameBoard[x][y];
            //if the square on the board is empty
            if (piece == null && !listContains(possibleActions,coord)) {

                Piece newPiece = new Piece(currentPlayer.getColor(), coord);
                Stack<String> flankDirection = new Stack<String>();

                //check outflank in every possible direction
                if (canOutFlank("NW", newPiece, currentPlayer, opponent, board)) {
                    flankDirection.push("NW");
                }
                if (canOutFlank("NE", newPiece, currentPlayer, opponent, board)) {
                    flankDirection.push("NE");
                }
                if (canOutFlank("SW", newPiece, currentPlayer, opponent, board)) {
                    flankDirection.push("SW");
                }
                if (canOutFlank("SE", newPiece, currentPlayer, opponent, board)) {
                    flankDirection.push("SE");
                }
                if (canOutFlank("N", newPiece, currentPlayer, opponent, board)) {
                    flankDirection.push("N");
                }
                if (canOutFlank("S", newPiece, currentPlayer, opponent, board)) {
                    flankDirection.push("S");
                }
                if (canOutFlank("W", newPiece, currentPlayer, opponent, board)) {
                    flankDirection.push("W");
                }
                if (canOutFlank("E", newPiece, currentPlayer, opponent, board)) {
                    flankDirection.push("E");
                }
                //if action is valid, add it to the possible action set
                if (!flankDirection.isEmpty()) {
                    possibleActions.add(new Action(newPiece, flankDirection));
                }
            }
        }
    }

    private boolean listContains(List<Action> possibleActions, int[] loc) {
        for(Action a : possibleActions){
            int[] aLoc = a.getPiece().getLocation();
            if(aLoc[0]==loc[0] && aLoc[1]==loc[1]) return true;
        }
        return false;
    }

    private void outFlank(String direction, Piece newPiece, Player currentPlayer, Player opponent, Board board) {
        String currentPlayerColor = currentPlayer.getColor();
        String opponentColor = opponent.getColor();
        Piece currentPiece = newPiece;
        Piece nextPiece;
        //traverse the board in the given direction until the next piece is current player's piece
        while((nextPiece = getNextPiece(board, direction, currentPiece)).getColor().equals(opponentColor)){
            //flip the color
            nextPiece.setColor(currentPlayerColor);
            //adjust each players' number of chips on board
            currentPlayer.setNoChipsOnBoard(currentPlayer.getNoChipsOnBoard()+1);
            opponent.setNoChipsOnBoard(opponent.getNoChipsOnBoard()-1);

            currentPiece = nextPiece;
        }
    }

    private boolean canOutFlank(String direction, Piece newPiece, Player currentPlayer, Player opponent, Board board) {
        Piece nextPiece = getNextPiece(board,direction,newPiece);
        String currentPlayerColor = currentPlayer.getColor();
        String opponentColor = opponent.getColor();
        //if next spot is empty, cannot outflank
        if(nextPiece==null) return false;
        //if next piece is one of current player's pieces, cannot outflank
        if(nextPiece.getColor().equals(currentPlayerColor)) return false;
        /*
         * traverse the board in the given direction
         * while next piece is current player's piece
         * if an empty spot is encountered, return false
         * else a current player's piece is encountered, return true
         */
        else{

            while(nextPiece != null && nextPiece.getColor().equals(opponentColor)){
                nextPiece = getNextPiece(board,direction, nextPiece);
            }
            if(nextPiece == null) return false;
            else return true;
        }
    }

    public boolean isGameOver(State state){
        //check if the current player has any moves
        if(generateValidActions(state).isEmpty()) {
            state.swapPlayers();
            //check if the opponent has any moves
            if(generateValidActions(state).isEmpty()){
                state.swapPlayers();
                return true;
            }
        }
        return false;
    }

    private Piece getNextPiece(Board board, String direction, Piece currentPiece) {
        Piece[][] gameBoard = board.getGameBoard();
        int[] locCurrent = currentPiece.getLocation();
        int[] locNext;
        switch(direction){
            case "NW":
                locNext = getNWloc(locCurrent);
                break;
            case "NE":
                locNext = getNEloc(locCurrent);
                break;
            case "SW":
                locNext = getSWloc(locCurrent);
                break;
            case "SE":
                locNext = getSEloc(locCurrent);
                break;
            case "N":
                locNext = getNloc(locCurrent);
                break;
            case "S":
                locNext = getSloc(locCurrent);
                break;
            case "W":
                locNext = getWloc(locCurrent);
                break;
            case "E":
                locNext = getEloc(locCurrent);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + direction);
        }

        if(outOfBoard(locNext)) return null;
        else return gameBoard[locNext[0]][locNext[1]];
    }

    public boolean outOfBoard(int[] locNext) {
        int x = locNext[0];
        int y = locNext[1];
        //check horizontal and vertical boundaries boundaries
        if(x<0 || x>7 || y<0 || y>7) return true;
        else return false;
    }



    public ArrayList<int[]> getSurroundingSquareCoords(int[] pieceLoc) {
        ArrayList<int[]> coords = new ArrayList<int[]>();
        coords.add(getNWloc(pieceLoc));
        coords.add(getNloc(pieceLoc));
        coords.add(getNEloc(pieceLoc));
        coords.add(getEloc(pieceLoc));
        coords.add(getSEloc(pieceLoc));
        coords.add(getSloc(pieceLoc));
        coords.add(getSWloc(pieceLoc));
        coords.add(getWloc(pieceLoc));
        return coords;
    }

    public ArrayList<int[]> getSurroundingSquareCoords(Piece edgePiece) {
        return getSurroundingSquareCoords(edgePiece.getLocation());
    }


    private boolean isCenter(Piece piece, Piece[][] gameBoard) {
        ArrayList<int[]> coords = getSurroundingSquareCoords(piece);
        for (int[] loc : coords) {
            if(outOfBoard(loc)) continue;
            //if any square around piece is empty, return false
            if (gameBoard[loc[0]][loc[1]]==null) return false;
        }
        return true;
    }

    public void printActionLoc(List<Action> actionSet){
        System.out.println("Valid Locations:");
        for(Action a : actionSet){
            a.printActionLoc();
        }
        System.out.println();
    }




    /*
     * methods to get different locations
     * around a given square on the board
     * NW: North West, NE: North East
     * SW: South West, SE: South East
     * N: North, W: West, S: South, E: East
     */
    private int[] getNWloc(int[] loc) {
        int[] NWloc = new int[2];
        NWloc[0] = loc[0] - 1;
        NWloc[1] = loc[1] - 1;
        return NWloc;
    }

    private int[] getNEloc(int[] loc) {
        int[] NEloc = new int[2];
        NEloc[0] = loc[0] - 1;
        NEloc[1] = loc[1] + 1;
        return NEloc;
    }

    private int[] getSWloc(int[] loc) {
        int[] SWloc = new int[2];
        SWloc[0] = loc[0] + 1;
        SWloc[1] = loc[1] - 1;
        return SWloc;
    }

    private int[] getSEloc(int[] loc) {
        int[] SEloc = new int[2];
        SEloc[0] = loc[0] + 1;
        SEloc[1] = loc[1] + 1;
        return SEloc;
    }

    private int[] getNloc(int[] loc) {
        int[] Nloc = new int[2];
        Nloc[0] = loc[0] - 1;
        Nloc[1] = loc[1];
        return Nloc;
    }

    private int[] getSloc(int[] loc) {
        int[] Sloc = new int[2];
        Sloc[0] = loc[0] + 1;
        Sloc[1] = loc[1];
        return Sloc;
    }

    private int[] getEloc(int[] loc) {
        int[] Eloc = new int[2];
        Eloc[0] = loc[0];
        Eloc[1] = loc[1] + 1;
        return Eloc;
    }

    private int[] getWloc(int[] loc) {
        int[] Wloc = new int[2];
        Wloc[0] = loc[0];
        Wloc[1] = loc[1] - 1;
        return Wloc;
    }


}
