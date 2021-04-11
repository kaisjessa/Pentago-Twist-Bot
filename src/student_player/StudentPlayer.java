package student_player;

import boardgame.Move;

import pentago_twist.PentagoPlayer;
import pentago_twist.PentagoBoardState;
import pentago_twist.PentagoMove;

/** A player file submitted by a student. */
public class StudentPlayer extends PentagoPlayer {

    /**
     * You must modify this constructor to return your student number. This is
     * important, because this is what the code that runs the competition uses to
     * associate you with your agent. The constructor should do nothing else.
     */
    public StudentPlayer() {
        super("260910524");
    }

    /**
     * This is the primary method that you need to implement. The ``boardState``
     * object contains the current state of the game, which your agent must use to
     * make decisions.
     */
    public Move chooseMove(PentagoBoardState boardState) {
        // You probably will make separate functions in MyTools.
        // For example, maybe you'll need to load some pre-processed best opening
        // strategies...
        MyTools.getSomething();

        // Is random the best you can do?
        //Move myMove = boardState.getRandomMove();

        Move myMove;
        // If we have a critical move, i.e., win or lose in one move, choose that move
        // Otherwise, run Monte Carlo Tree Search
        //myMove = MyTools.bestMove(boardState);

        MonteCarlo mcts = new MonteCarlo(boardState, boardState.getTurnPlayer());
        myMove = mcts.bestMove();
        System.out.println("USING MCTS");
        System.out.print("Root node: ");
        mcts.tree.root.printNode();

        // Return your move to be processed by the server.
        return myMove;
    }
}
