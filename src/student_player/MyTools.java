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
        PentagoMove current_move;
        int numMoves = legalMoves.size();

        legalMoves.sort(Comparator.comparingInt(a -> evalOtherMove(boardState, a, player)));
        current_move = legalMoves.get(numMoves-1);

        System.out.println("Best move eval: " + String.valueOf(evalMove(boardState, current_move, player)));
        return(current_move);
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
        if (winner == player) return -1;
        else if (winner == Integer.MAX_VALUE - 1) {
            return 0;
        }
        return 1;

    }

}