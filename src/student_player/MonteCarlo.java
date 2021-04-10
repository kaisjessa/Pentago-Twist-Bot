package student_player;
// Kais Jessa
// 260910524

import pentago_twist.PentagoBoardState;
import pentago_twist.PentagoMove;
import java.util.ArrayList;

// Class for implementing MCTS methods
public class MonteCarlo {

    Tree tree;
    int player;

    // Constructor initializes tree using input board state
    public MonteCarlo(PentagoBoardState state, int player) {
        this.player = player;
        tree = new Tree(state);
        for(PentagoMove move : state.getAllLegalMoves()) {
            tree.addNode(state, move, tree.root);
        }
    }

    // Calculates UCT of given node, used as tree policy
    public float UCT(Node node) {
        if(node.getNumGames() == 0) {
            return Integer.MAX_VALUE-1;
        }
        float q = (float)node.getNumWins() / (float)node.getNumGames();
        q += Math.sqrt(2 * node.parent.getNumGames());
        return(q);
    }

    // Finds the next leaf node to explore
    public Node selection() {
        Node current = this.tree.root;
        while(current.getNumChildren() > 0) {
            float currentMax = 0;
            Node nextNode = current.children.get(0);
            for(Node node : current.getChildren()) {
                if(UCT(node) > currentMax) {
                    currentMax = UCT(node);
                    nextNode = node;
                }
            }
            current = nextNode;
        }
        return current;
    }

    public void expansion(Node node) {
        PentagoBoardState state = node.getState();
        for(PentagoMove move: state.getAllLegalMoves()) {
            tree.addNode(state, move, node);
        }

    }

    // Play random moves
    public int simulation(PentagoBoardState state, int player) {
        PentagoBoardState newState = (PentagoBoardState)state.clone();
        PentagoMove nextMove;
        while(!newState.gameOver()) {
            nextMove = (PentagoMove)newState.getRandomMove();
            newState.processMove(nextMove);
        }
        int winner = newState.getWinner();
        if(winner == player) return(1);
        // Return 0 if we tie or lose (for now)
        return(0);
    }

    // Update the parent nodes after a simulation
    public void backpropagate(Node node, int winner) {
        Node currentNode = node;
        while(currentNode != null) {
            currentNode.numGames += 1;
            currentNode.numWins += winner;
            currentNode = currentNode.getParent();
        }
    }

    // One complete iteration of MCTS
    public void iteration() {
        Node currentNode = selection();
        expansion(currentNode);
        for (int i = 0; i < 10; i++) {
            int winner = simulation(currentNode.getState(), this.player);
            backpropagate(currentNode, winner);
        }
    }

    public PentagoMove bestMove() {
        long x = System.currentTimeMillis();
        while(System.currentTimeMillis() - x < 1500) {
            iteration();
        }
        PentagoMove bestMove = this.tree.root.getChildren().get(0).getMove();
        float currentMax = 0;
        float temp;
        for(Node node: this.tree.root.getChildren()) {
            temp = (float)node.getNumWins()/(float)node.getNumGames();
            if(temp > currentMax) {
                currentMax = temp;
                bestMove = node.getMove();
            }
        }
        return bestMove;
    }
}