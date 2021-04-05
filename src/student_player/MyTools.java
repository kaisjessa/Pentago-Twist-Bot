package student_player;
import boardgame.Move;
import pentago_twist.PentagoBoardState;
import pentago_twist.PentagoMove;
import pentago_twist.PentagoBoardState.Piece;
import java.util.ArrayList;

public class MyTools {
    public static double getSomething() {
        return Math.random();
    }

    public static PentagoBoardState test(PentagoBoardState boardState) {
        PentagoBoardState newBoardState;
        newBoardState = (PentagoBoardState)boardState.clone();

        ArrayList<PentagoMove> legalMoves = newBoardState.getAllLegalMoves();
        for(PentagoMove move: legalMoves) {
            System.out.println(move.toPrettyString());
        }

        return newBoardState;
    }

    public static int evaluation(PentagoBoardState boardState) {
        int p1 = 0;
        int p2 = 0;
        Piece[][] pieces = boardState.getBoard();
        for (Piece[] pieceRow : pieces) {
            for (Piece piece : pieceRow) {
                if (piece.toString().equals("WHITE")) {
                    p1 += 1;
                } else if (piece.toString().equals("BLACK")) {
                    p2 += 1;
                }
            }
        }
        return (p1 - p2);
    }

}