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

    public static PentagoBoardState test(PentagoBoardState boardState) {
        PentagoBoardState newBoardState;
        newBoardState = (PentagoBoardState) boardState.clone();

        ArrayList<PentagoMove> legalMoves = newBoardState.getAllLegalMoves();
        for (PentagoMove move : legalMoves) {
            System.out.println(move.toPrettyString());
        }

        return newBoardState;
    }

    public static PentagoMove bestMove(PentagoBoardState boardState) {
        ArrayList<PentagoMove> legalMoves = boardState.getAllLegalMoves();
        int player = boardState.getTurnPlayer();
        int current_eval = 0;
        PentagoMove current_move = legalMoves.get(0);
        int numMoves = legalMoves.size();

        legalMoves.sort(Comparator.comparingInt(a -> evalOtherMove(boardState, a, player)));
        current_move = legalMoves.get(numMoves-1);

        System.out.println("Best move eval: " + String.valueOf(evalMove(boardState, current_move, player)));
        return(current_move);
    }

    public static int evalOtherMove(PentagoBoardState boardState, PentagoMove move, int player) {
        PentagoBoardState newBoardState = (PentagoBoardState) boardState.clone();
        newBoardState.processMove(move);
        ArrayList<PentagoMove> legalMoves = newBoardState.getAllLegalMoves();
        int minVal = Integer.MAX_VALUE - 1;
        for(int i = 0; i < legalMoves.size(); i++) {
            minVal = Math.min(minVal, evalMove(newBoardState, legalMoves.get(i), player));
        }
        return(minVal);
    }


    public static int evalMove(PentagoBoardState boardState, PentagoMove move, int player) {
        PentagoBoardState newBoardState = (PentagoBoardState) boardState.clone();
        newBoardState.processMove(move);
        return(evaluation(newBoardState, player));
    }

    public static int evaluation(PentagoBoardState boardState, int player) {
//        int p1 = 0;
//        int p2 = 0;
//        Piece[][] pieces = boardState.getBoard();
//        for (Piece[] pieceRow : pieces) {
//            for (Piece piece : pieceRow) {
//                if (piece.toString().equals("WHITE")) {
//                    p1 += 1;
//                } else if (piece.toString().equals("BLACK")) {
//                    p2 += 1;
//                }
//            }
//        }
//        return (p1 - p2);
//    }

        int winner = boardState.getWinner();
        if (winner == player) return Integer.MAX_VALUE - 1;
        else if (winner == Integer.MAX_VALUE - 1) {
            return 3;
        }
        return Integer.MIN_VALUE + 1;

    }

}