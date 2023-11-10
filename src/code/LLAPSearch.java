package code;

import code.enums.Action;
import code.enums.Attribute;
import code.enums.RequestState;
import code.generic.Problem;
import code.structure.Node;
import code.structure.State;

import java.util.*;

public class LLAPSearch extends GenericSearch {


    public static Integer UNIT_PRICE_FOOD;
    public static Integer UNIT_PRICE_MATERIALS;
    public static Integer UNIT_PRICE_ENERGY;
    public static Problem problem;


    public static String solve(String initialState, String strategy, boolean visualize) {
        Problem problem = new Problem(initialState);
        System.out.println("Problem variables" + problem.variables);
        LLAPSearch.problem = problem;
        UNIT_PRICE_FOOD = problem.variables.get(Attribute.UNIT_PRICE_FOOD);
        UNIT_PRICE_MATERIALS = problem.variables.get(Attribute.UNIT_PRICE_MATERIALS);
        UNIT_PRICE_ENERGY = problem.variables.get(Attribute.UNIT_PRICE_ENERGY);
        State state = problem.getInitialState();
        Node root = new Node(state, null, 0, null, 0);
        StringBuilder solution = new StringBuilder();
        String value = null;
        switch (strategy) {
            case "BF":
                value = breadthFirstSearch(root);
                break;
            case "DF":
                value = dfs(root, 0, solution);
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
        return value == null ? "nosolution" : value;
    }


    public static String dfs(Node node, int numOfNodes, StringBuilder pathString) {

        if (isGoalState(node)) {
            pathString.setLength(pathString.length() - 1);
            pathString.append(";");
            pathString.append(node.getState().getMoneySpent()).append(";");
            pathString.append(numOfNodes).append(";");
            return pathString.toString();
        }
        if (isEndState(node)) {
            return null;
        }
        List<Node> successors = generateSuccessorNodes(node);
        for (Node successor : successors) {

//            if (!isEndState(successor)) {
            pathString.append(successor.getAction()).append(",");
            numOfNodes++;
            String result = dfs(successor, numOfNodes, pathString);
            if (result != null) {
                return result;
            }
//            }
        }

        return null;
    }


    public static String breadthFirstSearch(Node initialNode) {
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

    private static String formatPath(List<Action> path, Node currentNode) {
        StringBuilder pathString = new StringBuilder("Path: ");
        for (Action action : path) {
            pathString.append(action).append(" -> ");
        }
        pathString.append("Goal | ");
        pathString.append("Number of Nodes: ").append(path.size());
        pathString.append("Depth | Depth Of Node: ").append(currentNode.getDepth());
        return pathString.toString();
    }



    public static List<Node> generateSuccessorNodes(Node node) {
        List<Node> successors = new ArrayList<>();
        if (node.getState().getRequestState() == RequestState.DEFAULT) {
            generateBuildSuccessors(node, successors);
            generateDefaultStateSuccessors(node, successors);
        } else {
            if (node.getState().getDelayTime() == 1) {
                State currentState =  new State(node.getState().getProsperity(),node.getState().getFood(),node.getState().getMaterials(),node.getState().getEnergy(),node.getState().getMoneySpent(),node.getState().getDelayTime(),RequestState.DEFAULT);
                State newState = currentState.resourceDelivered(node.getState().getRequestState(), problem.variables);
                //TODO: Should we do wait first then resource delivered?
//                successors.add(new Node(newState.doWait(), node, node.getState().getMoneySpent(), Action.WAIT, node.getDepth() + 1));
                generateBuildWithNewState(node, successors, newState);
                generateDefaultSuccessorsWithNewState(node,successors,newState);

            } else {
                State newState =new State(node.getState().getProsperity(),node.getState().getFood(),node.getState().getMaterials(),node.getState().getEnergy(),node.getState().getMoneySpent(),node.getState().getDelayTime(),node.getState().getRequestState());
                newState.setDelayTime(node.getState().getDelayTime() - 1);
                generateBuildWithNewState(node, successors, newState);
                successors.add(new Node(newState.doWait(), node, node.getState().getMoneySpent(), Action.WAIT, node.getDepth() + 1));

            }

        }
        return successors;
    }

    private static void generateDefaultSuccessorsWithNewState(Node node, List<Node> successors, State newState) {

        State newStateRequestEnergy = new State(newState.getProsperity(),newState.getFood(),newState.getMaterials(),newState.getEnergy(),newState.getMoneySpent(),newState.getDelayTime(),RequestState.ENERGY);
        successors.add(new Node(newStateRequestEnergy.requestEnergy(problem.variables.get(Attribute.DELAY_REQUEST_ENERGY)), node, node.getState().getMoneySpent(), Action.RequestEnergy, node.getDepth() + 1));

        State newStateRequestMaterials = new State(newState.getProsperity(),newState.getFood(),newState.getMaterials(),newState.getEnergy(),newState.getMoneySpent(),newState.getDelayTime(),RequestState.MATERIALS);
        successors.add(new Node(newStateRequestMaterials.requestMaterials(problem.variables.get(Attribute.DELAY_REQUEST_MATERIALS)), node, node.getState().getMoneySpent(), Action.RequestMaterials, node.getDepth() + 1));

        State newStateRequestFood = new State(newState.getProsperity(),newState.getFood(),newState.getMaterials(),newState.getEnergy(),newState.getMoneySpent(),newState.getDelayTime(),RequestState.FOOD);
        successors.add(new Node(newStateRequestFood.requestFood(problem.variables.get(Attribute.DELAY_REQUEST_FOOD)), node, node.getState().getMoneySpent(), Action.RequestFood, node.getDepth() + 1));

    }


    private static void generateDefaultStateSuccessors(Node node, List<Node> successors) {

        // For RequestFood
        //TODO: node.getMoneySpent should be of the newState maybe?
        State newStateRequestFood = new State(node.getState().getProsperity(),node.getState().getFood(),node.getState().getMaterials(),node.getState().getEnergy(),node.getState().getMoneySpent(),node.getState().getDelayTime(),RequestState.FOOD);
        successors.add(new Node(newStateRequestFood.requestFood(problem.variables.get(Attribute.DELAY_REQUEST_FOOD)), node, node.getState().getMoneySpent(), Action.RequestFood, node.getDepth() + 1));

        // For RequestMaterials
        State newStateRequestMaterials = new State(node.getState().getProsperity(), node.getState().getFood(), node.getState().getMaterials(), node.getState().getEnergy(), node.getState().getMoneySpent(), node.getState().getDelayTime(), RequestState.MATERIALS);
        successors.add(new Node(newStateRequestMaterials.requestMaterials(problem.variables.get(Attribute.DELAY_REQUEST_MATERIALS)), node, node.getState().getMoneySpent(), Action.RequestMaterials, node.getDepth() + 1));
        // For RequestEnergy
        State newStateRequestEnergy =new State(node.getState().getProsperity(), node.getState().getFood(), node.getState().getMaterials(), node.getState().getEnergy(), node.getState().getMoneySpent(), node.getState().getDelayTime(), RequestState.ENERGY);
        successors.add(new Node(newStateRequestEnergy.requestEnergy(problem.variables.get(Attribute.DELAY_REQUEST_ENERGY)), node, node.getState().getMoneySpent(), Action.RequestEnergy, node.getDepth() + 1));


    }

    private static void generateBuildSuccessors(Node node, List<Node> successors) {
        if (checkIfCanBuild(node, 1)) {
            successors.add(new Node(node.getState().doBuild(problem.variables, 1), node, node.getState().getMoneySpent(), Action.BUILD1, node.getDepth() + 1));
        }
        if (checkIfCanBuild(node, 2)) {
            successors.add(new Node(node.getState().doBuild(problem.variables, 2), node, node.getState().getMoneySpent(), Action.BUILD2, node.getDepth() + 1));
        }
    }

    private static boolean checkIfCanBuild(Node node, int buildType) {
        return node.getState().getFood() >= problem.variables.get(buildType == 1 ? Attribute.FOOD_USE_BUILD1 : Attribute.FOOD_USE_BUILD2)
            && node.getState().getEnergy() >= problem.variables.get(buildType == 1 ? Attribute.ENERGY_USE_BUILD1 : Attribute.ENERGY_USE_BUILD2)
            && node.getState().getMaterials() >= problem.variables.get(buildType == 1 ? Attribute.MATERIALS_USE_BUILD1 : Attribute.MATERIALS_USE_BUILD2);
    }


    private static void generateBuildWithNewState(Node node, List<Node> successors, State newState) {
        if (checkIfCanBuild(node, 1)) {
            successors.add(new Node(newState.doBuild(problem.variables, 1), node, node.getState().getMoneySpent(), Action.BUILD1, node.getDepth() + 1));
        }
        if (checkIfCanBuild(node, 2)) {
            successors.add(new Node(newState.doBuild(problem.variables, 2), node, node.getState().getMoneySpent(), Action.BUILD2, node.getDepth() + 1));
        }
    }


    public static boolean isEndState(Node node) {
//        if (node.getAction() == Action.BUILD1) {
//            return node.getState().getFood() < problem.variables.get(Attribute.FOOD_USE_BUILD1)
//                || node.getState().getEnergy() < problem.variables.get(Attribute.ENERGY_USE_BUILD1)
//                || node.getState().getMaterials() < problem.variables.get(Attribute.MATERIALS_USE_BUILD1);
//        }
//
//        if (node.getAction() == Action.BUILD2) {
//            return node.getState().getFood() < problem.variables.get(Attribute.FOOD_USE_BUILD2)
//                || node.getState().getEnergy() < problem.variables.get(Attribute.ENERGY_USE_BUILD2)
//                || node.getState().getMaterials() < problem.variables.get(Attribute.MATERIALS_USE_BUILD2);
//        }

        return node.getState().getMoneySpent() >= 100000 || node.getState().getFood() <= 0 || node.getState().getMaterials() <= 0 || node.getState().getEnergy() <= 0;
    }

    public static boolean isGoalState(Node node) {
        return node.getState().getProsperity() >= 100;
    }


}
