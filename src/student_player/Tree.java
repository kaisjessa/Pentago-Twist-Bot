package student_player;

// Kais Jessa
// 260910524

import pentago_twist.PentagoBoardState;
import pentago_twist.PentagoMove;
import java.util.ArrayList;

public class Tree {
    Node root;
    ArrayList<Node> allNodes;

    public Tree(PentagoBoardState inState) {
        this.root = new Node(inState);
        this.allNodes = new ArrayList<Node>();
        this.allNodes.add(this.root);
    }

//    public void addNode(PentagoBoardState state, Node parent) {
//        Node newNode = new Node(state);
//        parent.addChild(newNode);
//        newNode.setParent(parent);
//        this.allNodes.add(newNode);
//
//    }

    public void addNode(PentagoBoardState oldState, PentagoMove move, Node parent) {
        PentagoBoardState newState = (PentagoBoardState)oldState.clone();
        newState.processMove(move);
        Node newNode = new Node(newState);
        newNode.setMove(move);
        parent.addChild(newNode);
        newNode.setParent(parent);
        this.allNodes.add(newNode);
    }

    public Node getRoot() {
        return this.root;
    }

    public ArrayList<Node> getAllNodes() {
        return this.allNodes;
    }

    public void printTree() {
        System.out.println("Printing tree...");
        for(Node node: allNodes) {
            System.out.println("Number of games: " + String.valueOf(node.getNumGames()));
            System.out.println("Number of wins: " + String.valueOf(node.getNumWins()));
        }
    }

    public void printChildren(Node inNode) {
        System.out.println("Printing children...");
        for(Node node: inNode.getChildren()) {
            if(node.move != null) System.out.println(node.move.toPrettyString());
            System.out.println("Number of games: " + String.valueOf(node.getNumGames()));
            System.out.println("Number of wins: " + String.valueOf(node.getNumWins()));
        }
    }
}
