package student_player;
import boardgame.Move;
import pentago_twist.PentagoBoardState;
import pentago_twist.PentagoMove;
import pentago_twist.PentagoBoardState.Piece;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MyTools {
    public static double getSomething() {
        return Math.random();
    }

//    public static PentagoBoardState test(PentagoBoardState boardState) {
//        PentagoBoardState newBoardState;
//        newBoardState = (PentagoBoardState) boardState.clone();
//
//        ArrayList<PentagoMove> legalMoves = newBoardState.getAllLegalMoves();
//        for (PentagoMove move : legalMoves) {
//            System.out.println(move.toPrettyString());
//        }
//
//        return newBoardState;
//    }

    // Returns the best move if we are able to win or stop a loss in the next turn
    public static PentagoMove bestMove(PentagoBoardState boardState) {
        ArrayList<PentagoMove> legalMoves = boardState.getAllLegalMoves();
        int player = boardState.getTurnPlayer();
        PentagoMove currentMove;
        int numMoves = legalMoves.size();

        // If we can win in the next move
        legalMoves.sort(Comparator.comparingInt(a -> evalMove(boardState, a, player)));
        currentMove = legalMoves.get(numMoves-1);
        //System.out.println("Best move eval: " + String.valueOf(evalMove(boardState, currentMove, player)));
        if(evalMove(boardState, currentMove, player) != 0) {
            System.out.println("ENSURING WIN IN ONE");
            return(currentMove);
        }

        // If we can prevent a loss in the next move
        legalMoves.sort(Comparator.comparingInt(a -> evalOtherMove(boardState, a, player)));
        currentMove = legalMoves.get(numMoves-1);
        //System.out.println("Worst other move eval: " + String.valueOf(evalOtherMove(boardState, legalMoves.get(0), player)));
        if(evalOtherMove(boardState, legalMoves.get(0), player) == -1) {
            System.out.println("PREVENTING LOSS IN ONE");
            return(currentMove);
        }

        // If we cannot prevent an immediate loss or win, run Monte Carlo Tree Search
        MonteCarlo mcts = new MonteCarlo(boardState, player);
        currentMove = mcts.bestMove();
        System.out.println("USING MCTS");
        mcts.tree.root.printNode();
        return(currentMove);

    }

    // Evaluates opponents next move after trying our move
    public static int evalOtherMove(PentagoBoardState boardState, PentagoMove move, int player) {
        PentagoBoardState newBoardState = (PentagoBoardState) boardState.clone();
        newBoardState.processMove(move);
        ArrayList<PentagoMove> legalMoves = newBoardState.getAllLegalMoves();
        int minVal = Integer.MAX_VALUE - 1;
        for (PentagoMove legalMove : legalMoves) {
            minVal = Math.min(minVal, evalMove(newBoardState, legalMove, player));
        }
        return(minVal);
    }

    // Evaluates our move on the current board
    public static int evalMove(PentagoBoardState boardState, PentagoMove move, int player) {
        PentagoBoardState newBoardState = (PentagoBoardState) boardState.clone();
        newBoardState.processMove(move);
        return(evaluation(newBoardState, player));
    }

    // Returns 0 unless we win or lose
    public static int evaluation(PentagoBoardState boardState, int player) {
        int winner = boardState.getWinner();
        if (winner == player) return 1;
        else if (winner == Integer.MAX_VALUE - 1) {
            return 0;
        }
        return -1;

    }

//    public ArrayList<PentagoMove> getGoodMoves(PentagoBoardState state) {
//        ArrayList<PentagoMove> moves = new ArrayList<PentagoMove>();
//        for(int i = 0; i < PentagoBoardState.BOARD_SIZE; i++) {
//            for(int j = 0; j < PentagoBoardState.BOARD_SIZE; j++) {
//                if(state.getPieceAt(i,j) == Piece.EMPTY) {
//                    for(int[] pair : getAdjacent(i, j)) {
//                        if(state.getPieceAt(pair[0], pair[1]) != Piece.EMPTY) {
//
//                        }
//                    }
//                }
//            }
//        }
//    }

    public ArrayList<int[]> getAdjacent(int x, int y) {
        ArrayList<int[]> pairs = new ArrayList<int[]>();
        for(int i=-1; i<=1; i++) {
            for(int j=-1; j<=1; j++) {
                if(0 <= x + i && x+i < PentagoBoardState.BOARD_SIZE && 0 <= y + j && y+j <= PentagoBoardState.BOARD_SIZE) {
                    int[] temp = new int[]{x+i, y+j};
                    pairs.add(temp);
                }
            }
        }
        return(pairs);
    }

}