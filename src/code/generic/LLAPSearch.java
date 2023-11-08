package code.generic;

import code.enums.Action;
import code.enums.Attribute;
import code.enums.RequestState;
import code.structure.Node;
import code.structure.State;

import java.util.*;

public class LLAPSearch extends GenericSearch {


    public static Integer UNIT_PRICE_FOOD;
    public static Integer UNIT_PRICE_MATERIALS;
    public static Integer UNIT_PRICE_ENERGY;
    Problem problem;

    public static RequestState requestState = RequestState.DEFAULT;

    public String solve(String initialState, String strategy, boolean visualize) {
        Problem problem = new Problem(initialState);
        this.problem = problem;
        UNIT_PRICE_FOOD = problem.variables.get(Attribute.UNIT_PRICE_FOOD);
        UNIT_PRICE_MATERIALS = problem.variables.get(Attribute.UNIT_PRICE_MATERIALS);
        UNIT_PRICE_ENERGY = problem.variables.get(Attribute.UNIT_PRICE_ENERGY);
        State state = problem.getInitialState();
        Node root = new Node(state, null, 0, null, 0);
        StringBuilder solution = new StringBuilder();
        String value = "";
        switch (strategy) {
            case "BF":
                value= breadthFirstSearch(root);
                break;
            case "DF":
                value= dfs(root,0,solution);
                break;
            case "ID":
                break;
            case "UC":
                break;
            case "GR1":
                break;
            case "GR2":
                break;
            case "AS1":
                break;
            case "AS2":
                break;
        }

        return value;
    }


    public String dfs(Node node, int numOfNodes,StringBuilder pathString) {

        if (isGoalState(node)) {
            pathString.append("Number of nodes: ").append(numOfNodes).append("\n");
            pathString.append("Money Spend : ").append(node.getState().getMoneySpent()).append("\n");
            return pathString.toString();
        }
        if (isEndState(node)) {
            return null;
        }
        List<Node> successors = generateSuccessorNodes(node);
        for (Node successor : successors) {
//            if (!isEndState(successor)) {
                pathString.append(successor.getAction()).append("-> ");
                numOfNodes++;
                String result = dfs(successor, numOfNodes,pathString);
                if (result != null) {
                    return result;
                }
//            }
        }

        return null;
    }



    public String breadthFirstSearch(Node initialNode) {
        Queue<Node> queue = new LinkedList<>();
        Map<Node, List<Action>> pathMap = new HashMap<>();
        queue.add(initialNode);
        pathMap.put(initialNode, new ArrayList<>());
        while (!queue.isEmpty()) {
            Node currentNode = queue.poll();

            if (isGoalState(currentNode)) {
                List<Action> path = pathMap.get(currentNode);
                return formatPath(path, currentNode);
            }
            if (isEndState(currentNode)) {
                List<Action> path = new ArrayList<>(pathMap.get(currentNode));
                path.add(currentNode.getAction());
                pathMap.put(currentNode, path);
                continue;
            }
            // Generate successor nodes for the current node
            List<Node> successors = generateSuccessorNodes(currentNode);

            for (Node successor : successors) {
                List<Action> path = new ArrayList<>(pathMap.get(currentNode));
                path.add(successor.getAction());
                pathMap.put(successor, path);
                queue.add(successor);
            }
        }
        return "No path found";
    }

    private String formatPath(List<Action> path, Node currentNode) {
        StringBuilder pathString = new StringBuilder("Path: ");
        for (Action action : path) {
            pathString.append(action).append(" -> ");
        }
        pathString.append("Goal | ");
        pathString.append("Number of Nodes: ").append(path.size());
        pathString.append("Depth | Depth Of Node: ").append(currentNode.getDepth());
        return pathString.toString();
    }




    public List<Node> generateSuccessorNodes(Node node) {

        List<Node> successors = new ArrayList<>();

        // Generate successors based on each possible action
        if (requestState == RequestState.DEFAULT) {
            successors.add(new Node(node.getState().requestFood(problem.variables.get(Attribute.DELAY_REQUEST_FOOD)), node, node.getState().getMoneySpent(), Action.REQUEST_FOOD, node.getDepth() + 1));
            successors.add(new Node(node.getState().requestEnergy(problem.variables.get(Attribute.DELAY_REQUEST_ENERGY)), node, node.getState().getMoneySpent(), Action.REQUEST_ENERGY, node.getDepth() + 1));
            successors.add(new Node(node.getState().requestMaterials(problem.variables.get(Attribute.DELAY_REQUEST_MATERIALS)), node, node.getState().getMoneySpent(), Action.REQUEST_MATERIALS, node.getDepth() + 1));
            successors.add(new Node(node.getState().doBuild(problem.variables, 1), node, node.getState().getMoneySpent(), Action.BUILD1, node.getDepth() + 1));
            successors.add(new Node(node.getState().doBuild(problem.variables, 2), node, node.getState().getMoneySpent(), Action.BUILD2, node.getDepth() + 1));

        } else {
            if (node.getState().getDelayTime() == 0) {
                State currentState = node.getState();
                State newState = currentState.resourceDelivered(requestState, problem.variables);
                requestState = RequestState.DEFAULT;
                successors.add(new Node(newState.doWait(), node, node.getState().getMoneySpent(), Action.WAIT, node.getDepth() + 1));
                successors.add(new Node(newState.doBuild(problem.variables, 1), node, node.getState().getMoneySpent(), Action.BUILD1, node.getDepth() + 1));
                successors.add(new Node(newState.doBuild(problem.variables, 2), node, node.getState().getMoneySpent(), Action.BUILD2, node.getDepth() + 1));
            } else {
                State newState  = node.getState();
                newState.setDelayTime(newState.getDelayTime() - 1);
                successors.add(new Node(newState.doWait(), node, node.getState().getMoneySpent(), Action.WAIT, node.getDepth() + 1));
                successors.add(new Node(newState.doBuild(problem.variables, 1), node, node.getState().getMoneySpent(), Action.BUILD1, node.getDepth() + 1));
                successors.add(new Node(newState.doBuild(problem.variables, 2), node, node.getState().getMoneySpent(), Action.BUILD2, node.getDepth() + 1));
            }
        }

        return successors;
    }


    public boolean isEndState(Node node) {
        return node.getState().getMoneySpent() >= 100000 || node.getState().getFood() <= 0 || node.getState().getMaterials() <= 0 || node.getState().getEnergy() <= 0;
    }

    public boolean isGoalState(Node node) {
        return node.getState().getProsperity() >= 100;
    }


}
