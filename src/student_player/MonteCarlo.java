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
    int maxDepth = 0;

    // Constructor initializes tree using input board state
    public MonteCarlo(PentagoBoardState state, int player) {
        this.player = player;
        tree = new Tree(state);
    }

    // Calculates UCT of given node, used as tree policy
    public float UCT(Node node) {
        if(node.getNumGames() == 0) {
            return Integer.MAX_VALUE-1;
        }
        float q = (float)node.getNumWins() / (float)node.getNumGames();
        if(node.parent != null) q += Math.sqrt(2.0 * Math.log(node.parent.getNumGames()) / (float)node.getNumGames());
        return(q);
    }

    // Finds the next leaf node to explore
    public Node selection() {
        Node current = this.tree.root;
        float currentMax;
        Node nextNode;
        int depth = 0;
        while(current.getNumChildren() > 0) {
            depth += 1;
            if(depth > this.maxDepth) {
                this.maxDepth = depth;
            }
            nextNode = current.children.get(0);
            currentMax = UCT(nextNode);
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

    // Create child nodes of selected node
    public void expansion(Node node) {
        PentagoBoardState state = node.getState();
        if (!node.state.gameOver() && node.getNumGames() > 0) {
            for (PentagoMove move : state.getAllLegalMoves()) {
                tree.addNode(state, move, node);
            }
        }
    }

    // Play random moves
    public int simulation(PentagoBoardState state) {
        PentagoBoardState newState = (PentagoBoardState)state.clone();
        PentagoMove nextMove;
        while(!newState.gameOver()) {
            nextMove = (PentagoMove)newState.getRandomMove();
            newState.processMove(nextMove);
        }
        return(newState.getWinner());
    }

    // Update the parent nodes after a simulation
    public void backpropagate(Node node, int winner) {
        Node currentNode = node;
        while(currentNode != null) {
            currentNode.numGames += 2;
            if(this.player == winner) {
                currentNode.numWins += 2;
            }
            else if(winner > 1) {
                currentNode.numWins += 1;
            }
            currentNode = currentNode.getParent();
        }
    }

    // One complete iteration of MCTS
    public void iteration() {
        Node currentNode = selection();
        //System.out.print("Chosen node: ");
        //currentNode.printNode();
        //currentNode.getState().printBoard();
        int winner;
        expansion(currentNode);
        if (currentNode.state.gameOver()) {
            winner = currentNode.state.getWinner();
            backpropagate(currentNode, winner);
        }
        else if(currentNode.getNumGames() == 0) {
            winner = simulation(currentNode.getState());
            backpropagate(currentNode, winner);
        }
        else {
//            for(Node node : currentNode.getChildren()) {
//                winner = simulation(node.getState());
//                backpropagate(node, winner);
//            }
            Node node = currentNode.getChildren().get(0);
            winner = simulation(node.getState());
            backpropagate(node, winner);

        }

    }

    public PentagoMove bestMove() {
//        for(int i=0; i<10; i++) {
//            iteration();
//        }
        long x = System.currentTimeMillis();
        while(System.currentTimeMillis() - x < 1500) {
            iteration();
        }
        Node bestNode = this.tree.root.getChildren().get(0);
        PentagoMove bestMove = bestNode.getMove();
        float currentMax = 0;
        float temp;
        for(Node node: this.tree.root.getChildren()) {
            temp = (float)node.getNumGames();
            if(temp > currentMax) {
                currentMax = temp;
                bestNode = node;
                bestMove = bestNode.getMove();
            }
        }
        System.out.print("Best node: ");
        bestNode.printNode();
        System.out.println("Tree has: " + String.valueOf(this.tree.getAllNodes().size()) + " total nodes");
        System.out.println("Tree depth: " + String.valueOf(this.maxDepth));
//        for(int i=0; i < tree.root.getChildren().size(); i++) {
//                System.out.print("Node " + String.valueOf(i) + " ");
//                tree.root.getChildren().get(i).printNode();
//                //tree.root.getChildren().get(i).getState().printBoard();
//        }
        return bestMove;
    }
}
