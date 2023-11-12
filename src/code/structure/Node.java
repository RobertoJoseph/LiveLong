package code.structure;

import code.enums.Action;

public class Node {
    private State state;
    private Node parent;
    private int pathCost;
    private final Action action;
    private int depth;


    public Node(State state, Node parent, int pathCost, Action action, int depth) {
        this.state = state;
        this.parent = parent;
        this.pathCost = pathCost;
        this.action = action;
        this.depth = depth;
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

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
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

    public String toString() {
        return "State: " + state.toString() + "Action: " + this.getAction().toString() + " Path Cost: " + pathCost + " Depth: " + depth;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Node other = (Node) obj;
        return action == other.action && state.equals(other.state)&&pathCost>=other.pathCost;
    }

    @Override
    public int hashCode() {
        return state.hashCode();
    }

}

