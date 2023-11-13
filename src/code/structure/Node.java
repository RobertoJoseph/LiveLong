package code.structure;

import code.LLAPSearch;
import code.enums.Action;
import code.enums.Attribute;

import java.util.HashMap;

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
        return state.equals(other.state);
    }

    @Override
    public int hashCode() {
        return state.hashCode();
    }

    public static int getHeuristicOne(Node node) {
        return Math.max(0, 100 - node.getState().getProsperity());
    }

    // Add this method to your Node class
    public static int getHeuristicTwo(Node node) {
        State currentState = node.getState();

        // Assuming a linear relationship between prosperity and cost
        int estimatedCost = Math.max(0, 100 - currentState.getProsperity());

        // Penalize the heuristic based on the scarcity of resources
        estimatedCost += 2 * (currentState.getFood() + currentState.getMaterials() + currentState.getEnergy());

        return estimatedCost;
    }

    public static int totalCost(Node node, int heuristic) {
        if (heuristic == 1)
            return node.getPathCost() + getHeuristicOne(node);
        else
            return node.getPathCost() + getHeuristicTwo(node);
    }

    public  boolean isEnoughMoneyForBuild(HashMap<Attribute, Integer> variables, int numOfBuild, int moneySpent) {
        int givenFood = (numOfBuild == 1) ? variables.get(Attribute.FOOD_USE_BUILD1) : variables.get(Attribute.FOOD_USE_BUILD2);
        int givenMaterials = (numOfBuild == 1) ? variables.get(Attribute.MATERIALS_USE_BUILD1) : variables.get(Attribute.MATERIALS_USE_BUILD2);
        int givenEnergy = (numOfBuild == 1) ? variables.get(Attribute.ENERGY_USE_BUILD1) : variables.get(Attribute.ENERGY_USE_BUILD2);
        int givenPrice = (numOfBuild == 1) ? variables.get(Attribute.PRICE_BUILD1) : variables.get(Attribute.PRICE_BUILD2);
        int givenProsperity = (numOfBuild == 1) ? variables.get(Attribute.PROSPERITY_BUILD1) : variables.get(Attribute.PROSPERITY_BUILD2);

        return 100000 - (moneySpent + givenPrice + (LLAPSearch.UNIT_PRICE_FOOD * givenFood) + (LLAPSearch.UNIT_PRICE_ENERGY * givenEnergy) + (LLAPSearch.UNIT_PRICE_MATERIALS * givenMaterials)) >= 0;

    }

    public boolean isEnoughMoneyDefaultRequest(HashMap<Attribute, Integer> variables, int moneySpent) {
        return 100000 - (moneySpent +  LLAPSearch.UNIT_PRICE_FOOD  + LLAPSearch.UNIT_PRICE_ENERGY  + LLAPSearch.UNIT_PRICE_MATERIALS) >= 0;

    }


}

