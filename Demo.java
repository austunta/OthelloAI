import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Demo {

    public static void main(String args[]) {
        String mode = args[0];
        if(mode.equalsIgnoreCase("play")){
            playMode();
        }else if(mode.equalsIgnoreCase("test")){
            int roundNo = Integer.parseInt(args[3]);
            boolean print = Boolean.parseBoolean(args[4]);
            testMode(args[1],args[2],roundNo,print);
        }else{
            System.out.printf("Invalid mode entry");
        }
    }


    private static void playMode() {
        System.out.println("Welcome to Othello/Reversi!\nBefore we start, we need to set up the players.");
        /*
         * initialize game
         */
        Board board = new Board();
        Scanner scan = new Scanner(System.in);

        //set players
        Player player1 = getUserInputForPlayerType("black");
        Player player2 = getUserInputForPlayerType("white");



        //initialize the state
        State state = new State(board, player1, player2);
        board.printBoard();

        //start the game
        while (true) {
            Player curr = state.getCurrentPlayer();
            if (curr.moves.isGameOver(state)) {
                break;
            }

            System.out.println("Curr player: " + state.getCurrentPlayer().getColor() + "\nno of chips:" + state.getCurrentPlayer().getNoChipsOnBoard());
            System.out.println("opponent player: " + state.getOpponent().getColor() + "\nno of chips:" + state.getOpponent().getNoChipsOnBoard());
            System.out.println("Player " + curr.getColor() + " is making a move");
            State newState = curr.makeMove(state, true);
            if (newState != null) state = newState;
            else {
                System.out.println("NO VALID MOVES, PASS");
            }
            state.getBoard().printBoard();
        }

        System.out.println("Game Over");

        if (player1.noChipsOnBoard > player2.noChipsOnBoard) {
            System.out.println("Player1 won, color: " + player1.getColor());
        } else if (player1.noChipsOnBoard < player2.noChipsOnBoard) {
            System.out.println("Player2 won, color: " + player2.getColor());
        } else {
            System.out.println("DRAW");
        }


    }

    private static void testMode(String player1type, String player2type, int rounds, boolean print) {
        int depth1 = 0;
        int depth2 = 0;
        String color1 = "black";
        String color2 = "white";

        if (player1type.equalsIgnoreCase("ai")) depth1 = getUserInputForDepth();
        if (player2type.equalsIgnoreCase("ai")) depth2 = getUserInputForDepth();
        int player1score = 0;
        int player2score = 0;
        Player player1 = null;
        Player player2 = null;
        int i = 0;
        for (i = 0; i < rounds; i++) {
            //initialize game
            Board board = new Board();
//

            //swap colors each rounds
            if (i % 2 == 0) {
                color1 = "black";
                color2 = "white";
            } else {
                color1 = "white";
                color2 = "black";
            }

            if (player1type.equalsIgnoreCase("ai")) player1 = new AIPlayer(color1, depth1);
            else if (player1type.equalsIgnoreCase("dummy")) player1 = new DummyPlayer(color1);

            if (player2type.equalsIgnoreCase("ai")) player2 = new AIPlayer(color2, depth2);
            else if (player2type.equalsIgnoreCase("dummy")) player2 = new DummyPlayer(color2);


            if (print) {
                System.out.println("Player 1 color: " + player1.getColor());
                System.out.println("Player 2 color: " + player2.getColor());
            }


            //initialize the state

            State state = new State(board, player1, player2);


            //start the game
            while (true) {
                Player curr = state.getCurrentPlayer();
                if (curr.moves.isGameOver(state)) {
                    break;
                }

                State newState = curr.makeMove(state, false);
                if (newState != null) state = newState;
            }

            if (print) System.out.println("Game Over");

            if (player1.noChipsOnBoard > player2.noChipsOnBoard) {
                if (print) System.out.println("Player1 won");
                player1score++;
            } else if (player1.noChipsOnBoard < player2.noChipsOnBoard) {
                if (print) System.out.println("Player2 won");
                player2score++;
            } else {
                if (print) System.out.println("DRAW");
                player1score++;
                player2score++;
            }
        }
        System.out.println("success rate of player1 (" + player1.getColor() + "): " + (((double) player1score) / rounds) * 100 + "%");
        System.out.println("success rate of player2 (" + player2.getColor() + "): " + (((double) player2score) / rounds) * 100 + "%");


    }


    private static int getUserInputForDepth() {
        Scanner scan = new Scanner(System.in);
        int depth = 0;

        System.out.println("Enter a depth limit for the AI player");
        do {
            try {
                if (scan.hasNext()) depth = scan.nextInt();
                else {
                    scan.next();
                    continue;
                }
                if (depth <= 12) return depth;
                else System.out.println("ENTER A REASONABLE DEPTH (LESS THAN 13)");
            } catch (Exception e) {
                System.out.println("WRONG FORMAT! Enter an INTEGER");
                scan.next();
            }
        } while (true);
    }

    private static Player getUserInputForPlayerType(String color) {
        Scanner scan = new Scanner(System.in);
        String playerType = null;
        System.out.printf("Setting the %s Player?%nEnter Human, AI, or Dummy%n", color);
        do {
            try {
                if (scan.hasNext()) playerType = scan.next();
                else {
                    scan.next();
                    continue;
                }
                if (playerType.equalsIgnoreCase("human")) {
                    return new HumanPlayer(color);
                } else if (playerType.equalsIgnoreCase("AI")) {
                    return new AIPlayer(color, getUserInputForDepth());
                } else if (playerType.equalsIgnoreCase("Dummy")) {
                    return new DummyPlayer(color);
                } else System.out.println("INVALID PLAYER TYPE! Accepted Input:\nHuman\nAI\nDummy");
            } catch (Exception e) {
                System.out.println("WRONG FORMAT! Accepted Input:\nHuman\nAI\nDummy");
                scan.next();
            }
        } while (true);

    }

}
