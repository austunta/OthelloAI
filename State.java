public class State {

    private Board board;
    private Player currentPlayer;
    private Player opponent;

    public State(Board board, Player player1, Player player2) {
        this.board = board;
        if(player1.getColor().equalsIgnoreCase("black")) {
            this.currentPlayer = player1;
            this.opponent = player2;
        }else{
            this.currentPlayer = player2;
            this.opponent = player1;
        }
    }


    public State copyState() {
        Board copyBoard = board.copyBoard();
        Player copyCurrentPlayer = currentPlayer.copyPlayer();
        Player copyOpponent = opponent.copyPlayer();
        return new State(copyBoard, copyCurrentPlayer, copyOpponent);

    }

    public void swapPlayers(){
        Player curr = currentPlayer;
        currentPlayer = opponent;
        opponent = curr;
    }


    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public Player getOpponent() {
        return opponent;
    }

    public void setOpponent(Player opponent) {
        this.opponent = opponent;
    }

}
