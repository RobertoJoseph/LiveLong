package code.structure;

import code.enums.Action;

public class Node {
    private State state;
    private Node parent;
    private int pathCost;
    private final Action action;


    public Node(State state, Node parent, int pathCost,Action action) {
        this.state = state;
        this.parent = parent;
        this.pathCost = pathCost;
        this.action = action;
    }

    // Getters and setters for the attributes
    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }


    public int getPathCost() {
        return pathCost;
    }

    public void setPathCost(int pathCost) {
        this.pathCost = pathCost;
    }

    public Action getAction() {
        return action;
    }


    // Other methods as needed
}

