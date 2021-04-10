import javax.lang.model.type.ArrayType;
import java.util.*;

public class AIPlayer extends Player {

    final int INF_P = Integer.MAX_VALUE;
    final int INF_N = Integer.MIN_VALUE;
    private int maxDepth;
    public double timeSpentGeneratingActions;
    public double timeSpentCopy;
    public double timeSpentMove;

    public int statesVisited;

    class Result {
        public int value;
        public Action a;

        public Result(int value, Action a) {
            this.a = a;
            this.value = value;
        }
    }

    public AIPlayer(String color, int maxDepth) {
        super(color);
        this.maxDepth = maxDepth;

    }

    public AIPlayer(String color, int noChips, int noChipsOnBoard, int maxDepth) {
        super(color, noChips, noChipsOnBoard);
        this.maxDepth = maxDepth;
    }


    @Override
    public Player copyPlayer() {
        return new AIPlayer(color, noChips, noChipsOnBoard, maxDepth);
    }


    @Override
    public State makeMove(State state, boolean print) {
        //use h-minimax with alpha beta pruning to choose the best action
        double start = System.nanoTime();
        Action action = alphaBetaSearch(state);
        double end = System.nanoTime();
        double elapsed = end - start;


        if (action == null) return null;

        if(print){
            System.out.println("No of States visited: " + statesVisited);
            System.out.println("Time spent to choose the best move (in seconds): " + elapsed / 1000000000);
            System.out.println("Move chosen by AI Player: " + action.toString());
        }


        //execute the action
        return moves.makeMove(action, state);

    }


    private Action alphaBetaSearch(State state) {
        statesVisited = 0;
        List<Action> validActions = moves.generateValidActions(state);
        statesVisited += validActions.size();

        if (validActions.isEmpty()) return null;

        Result r = maxValue(state, INF_N, INF_P, 0);



        return r.a;
    }


    private Result maxValue(State state, int alpha, int beta, int depth) {
        if (depth == maxDepth) return new Result(heuristic(state), null);
        if (moves.isGameOver(state)) return new Result(trueEval(state), null);

        int value = INF_N;
        Action best = null;
        double start = System.nanoTime();
        List<Action> validActions = moves.generateValidActions(state);
        double end = System.nanoTime();
        double elapsed = end - start;
        timeSpentGeneratingActions += elapsed;


        for (Action a : validActions) {
            statesVisited++;
            start = System.nanoTime();
            State copyState = state.copyState();
            end = System.nanoTime();
            elapsed = end - start;
            timeSpentCopy += elapsed;
          
            start = System.nanoTime();
            State childState = moves.makeMove(a, copyState);
            end = System.nanoTime();
            elapsed = end - start;
            timeSpentMove += elapsed;
           
            value = max(value, minValue(childState, alpha, beta, depth + 1).value);
         
            if (value > alpha) {
                alpha = value;
                best = a;
            }
            if (beta <= alpha) break; //beta cutting branch for max
        }

        return new Result(value, best);
    }

    private Result minValue(State state, int alpha, int beta, int depth) {

       
        if (depth == maxDepth) return new Result(heuristic(state), null);
        if (moves.isGameOver(state)) return new Result(trueEval(state), null);

        int value = INF_P;
        Action best = null;
        double start = System.nanoTime();
        List<Action> validActions = moves.generateValidActions(state);
        double end = System.nanoTime();
        double elapsed = end - start;
        timeSpentGeneratingActions += elapsed;

        for (Action a : validActions) {
            statesVisited++;
            start = System.nanoTime();
            State copyState = state.copyState();
            end = System.nanoTime();
            elapsed = end - start;
            timeSpentCopy += elapsed;
            start = System.nanoTime();
            State childState = moves.makeMove(a, copyState);
            end = System.nanoTime();
            elapsed = end - start;
            timeSpentMove += elapsed;
            
            value = min(value, maxValue(childState, alpha, beta, depth + 1).value);
 
            if (value < beta) {
                beta = value;
                best = a;
            }
            if (beta <= alpha) break; //alpha cutting branch for min
        }

        return new Result(value, best);
    }

    private int trueEval(State state) {
        Player currPlayer = state.getCurrentPlayer();
        Player opponent = state.getOpponent();

        int chipNoCurrPlayer = currPlayer.noChipsOnBoard;
        int chipNoOpponent = opponent.noChipsOnBoard;

        if (chipNoCurrPlayer > chipNoOpponent) {
            if (currPlayer.getColor().equalsIgnoreCase(color)) return 1000;
            else return -1000;
        } else if (chipNoCurrPlayer < chipNoOpponent) {
            if (currPlayer.getColor().equalsIgnoreCase(color)) return -1000;
            else return 1000;
        } else {
            if (currPlayer.getColor().equalsIgnoreCase(color)) return 500;
            else return -500;
        }
    }


    private boolean terminalTest(State state, int depth) {
        if (depth == maxDepth) return true;
        if (moves.isGameOver(state)) return true;
        return false;
    }

    private int[][] valueBoard = {
            {120,-20, 20,  5,  5, 20,-20,120},
            {-20,-40, -5, -5, -5, -5,-40,-20},
            { 20, -5, 15,  3,  3, 15, -5, 20},
            {  5, -5,  3,  3,  3,  3, -5,  5},
            {  5, -5,  3,  3,  3,  3, -5,  5},
            { 20, -5, 15,  3,  3, 15, -5, 20},
            {-20,-40, -5, -5, -5, -5,-40,-20},
            {120,-20, 20,  5,  5, 20,-20,120}
    };

    //values the number of pieces on board
    //values the proximity to board's corners
    private int heuristic(State state) {

        /*
         * having more piece on board means
         * higher utility value
         */
        int utilityVal = 0;
        //get the value from valueBoard
        Piece[][] gameBoard = state.getBoard().getGameBoard();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece p = gameBoard[i][j];
                if (p != null && p.getColor().equalsIgnoreCase(state.getCurrentPlayer().color)) {
                    utilityVal += valueBoard[i][j];
                }
            }
        }

        List<Action> validActionsCurr = moves.generateValidActions(state);
        int mobility = (validActionsCurr.size()) * 50;

        Player currPlayer = state.getCurrentPlayer();
        Player opponent = state.getOpponent();

        if (color.equalsIgnoreCase(currPlayer.getColor())) {
            return utilityVal + mobility;
        } else return -(utilityVal + mobility);
    }


    private int min(int no1, int no2) {
        if (no1 <= no2) return no1;
        else return no2;
    }

    private int max(int no1, int no2) {
        if (no1 >= no2) return no1;
        else return no2;
    }

    @Override
    public String toString() {
        return "AI Player, color:" + color + ", no of pieces on board:" + noChipsOnBoard;
    }


    public int getMaxDepth() {
        return maxDepth;
    }

    public void setMaxDepth(int maxDepth) {
        this.maxDepth = maxDepth;
    }
}


