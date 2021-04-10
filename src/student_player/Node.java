package student_player;

// Kais Jessa
// 260910524

import pentago_twist.PentagoBoardState;
import pentago_twist.PentagoMove;
import java.util.ArrayList;


// Class for Nodes in the Monte Carlo Tree Search
public class Node {
        // Contains board state, state of parent, and ArrayList of children
        // Also holds number of games, and number of wins for MCTS
        PentagoBoardState state;
        Node parent;
        PentagoMove move;
        ArrayList<Node> children;
        int numGames;
        int numWins;
        int numChildren;

        // Constructor
    public Node(PentagoBoardState inState) {
        this.state = inState;
        this.parent = null;
        this.move = null;
        this.children = new ArrayList<Node>();
        this.numGames = 0;
        this.numWins = 0;
        this.numChildren = 0;
    }

    // Add child to ArrayList of children
    public void addChild(Node newChild) {
        this.children.add(newChild);
        this.numChildren += 1;
    }


    public void setParent(Node inParent) {
        this.parent = inParent;
    }
    public void setMove(PentagoMove inMove) {
        this.move = inMove;
    }

    // Getters
    public ArrayList<Node> getChildren() {
        return this.children;
    }
    public int getNumGames() {
        return this.numGames;
    }
    public int getNumWins() {
        return this.numWins;
    }
    public Node getParent() {
        return this.parent;
    }
    public int getNumChildren() {
        return this.numChildren;
    }
    public PentagoBoardState getState() {
        return this.state;
    }
    public PentagoMove getMove() {
        return this.move;
    }

    public void printNode() {
        System.out.println("Number of wins / Number of games = " + String.valueOf(this.getNumWins()) + " / " + String.valueOf(this.getNumGames()));
    }
}
