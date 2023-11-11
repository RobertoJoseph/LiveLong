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
        LLAPSearch.problem = problem;
        UNIT_PRICE_FOOD = problem.variables.get(Attribute.UNIT_PRICE_FOOD);
        UNIT_PRICE_MATERIALS = problem.variables.get(Attribute.UNIT_PRICE_MATERIALS);
        UNIT_PRICE_ENERGY = problem.variables.get(Attribute.UNIT_PRICE_ENERGY);
        State state = problem.getInitialState();
        Node root = new Node(state, null, 0, Action.DEFAULT, 0);
        StringBuilder solution = new StringBuilder();
        String value = null;
        switch (strategy) {
            case "BF":
                value = breadthFirstSearch(root);
                break;
            case "DF":
                value = dfs(root, 0,solution, new HashSet<>());
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



    public static String dfs(Node node, int numOfNodes, StringBuilder pathString,HashSet<Node>visitedNodes) {
        if (isGoalState(node)) {
            return getPathToGoal(node, numOfNodes, pathString);
        }
        if (isEndState(node)||visitedNodes.contains(node)) {
            return null;
        }

        visitedNodes.add(node);
        List<Node> successors = generateSuccessorNodes(node);
        for (Node successor : successors) {
            pathString.append(successor.getAction()).append(",");
            numOfNodes++;
            String result = dfs(successor, numOfNodes, pathString,visitedNodes);
            if (result != null) {
                return result;
            }
            pathString.setLength(pathString.length() - successor.getAction().toString().length() - 1);
        }

        return null;
    }

    private static String getPathToGoal(Node node, int numOfNodes, StringBuilder pathString) {
        pathString.setLength(pathString.length() - 1);
        pathString.append(";");
        pathString.append(node.getState().getMoneySpent()).append(";");
        pathString.append(numOfNodes).append(";");
        return pathString.toString();
    }



    public static String getPathToGoalBfs(Node node, int numOfNodes) {
        StringBuilder pathString = new StringBuilder();
        int moneySpent = node.getState().getMoneySpent();
        while (node != null) {
            System.out.println("GET PATH");
            if(node.getAction()!=Action.DEFAULT){
                pathString.insert(0, node.getAction()).insert(0, ",");
            }
            node = node.getParent();
            numOfNodes++;
        }
        pathString.replace(0, 1, "");
        pathString.append(";");
        pathString.append(moneySpent).append(";");
        pathString.append(numOfNodes).append(";");
        return pathString.toString();

    }


    public static String breadthFirstSearch(Node node){
        Queue<Node>queue = new LinkedList<>();
        Set<Node> visitedNodes = new HashSet<>();
        queue.add(node);
        visitedNodes.add(node);

        while(!queue.isEmpty()){
            System.out.println("BFS: " + queue.size() + " nodes in the queue");
            Node currentNode = queue.poll();
            assert currentNode != null;
            if(isGoalState(currentNode)){
                System.out.println("GOALLLLL");
                return getPathToGoalBfs(currentNode,0);
            }
            if (isEndState(currentNode) || (visitedNodes.contains(currentNode) && currentNode.getAction() != Action.DEFAULT)) {
                continue;
            }

            List<Node> successors = generateSuccessorNodes(currentNode);
            for(Node successor: successors){
                if(!visitedNodes.contains(successor)){
//                    visitedNodes.add(successor);
                    queue.add(successor);
                }
            }
        }
        return null;
    }


//    public static String breadthFirstSearch(Node initialNode) {
//        Queue<Node> queue = new LinkedList<>();
//        Map<Node, List<Action>> pathMap = new HashMap<>();
//        queue.add(initialNode);
//        pathMap.put(initialNode, new ArrayList<>());
//        while (!queue.isEmpty()) {
//            Node currentNode = queue.poll();
//
//            if (isGoalState(currentNode)) {
//                List<Action> path = pathMap.get(currentNode);
//                return formatPath(path, currentNode);
//            }
//            if (isEndState(currentNode)) {
//                List<Action> path = new ArrayList<>(pathMap.get(currentNode));
//                path.add(currentNode.getAction());
//                pathMap.put(currentNode, path);
//                continue;
//            }
//            // Generate successor nodes for the current node
//            List<Node> successors = generateSuccessorNodes(currentNode);
//
//            for (Node successor : successors) {
//                List<Action> path = new ArrayList<>(pathMap.get(currentNode));
//                path.add(successor.getAction());
//                pathMap.put(successor, path);
//                queue.add(successor);
//            }
//        }
//        return "No path found";
//    }

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

                generateBuildWithNewState(node, successors, newState);
                if(node.getAction()==Action.WAIT)
                    generateDefaultSuccessorsWithNewState(node,successors,newState);
                successors.add(new Node(newState.doWait(), node, node.getState().getMoneySpent(), Action.WAIT, node.getDepth() + 1));


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

//        State newStateRequestEnergy = new State(newState.getProsperity(),newState.getFood(),newState.getMaterials(),newState.getEnergy(),newState.getMoneySpent(),newState.getDelayTime(),RequestState.ENERGY);
        State newRequestFoodState = newState.requestEnergy(problem.variables.get(Attribute.DELAY_REQUEST_ENERGY));

        successors.add(new Node(newRequestFoodState, node, newRequestFoodState.getMoneySpent(), Action.RequestEnergy, node.getDepth() + 1));

//        State newStateRequestMaterials = new State(newState.getProsperity(),newState.getFood(),newState.getMaterials(),newState.getEnergy(),newState.getMoneySpent(),newState.getDelayTime(),RequestState.MATERIALS);
        State newStateRequestMaterials = newState.requestMaterials(problem.variables.get(Attribute.DELAY_REQUEST_MATERIALS));
        successors.add(new Node(newStateRequestMaterials, node, newStateRequestMaterials.getMoneySpent(), Action.RequestMaterials, node.getDepth() + 1));

//        State newStateRequestFood = new State(newState.getProsperity(),newState.getFood(),newState.getMaterials(),newState.getEnergy(),newState.getMoneySpent(),newState.getDelayTime(),RequestState.FOOD);
        State newStateRequestFood = newState.requestFood(problem.variables.get(Attribute.DELAY_REQUEST_FOOD));
        successors.add(new Node(newStateRequestFood, node,newStateRequestFood.getMoneySpent() , Action.RequestFood, node.getDepth() + 1));

    }


    private static void generateDefaultStateSuccessors(Node node, List<Node> successors) {

        // For RequestFood
        //TODO: node.getMoneySpent should be of the newState maybe?
//        State newStateRequestFood = new State(node.getState().getProsperity(),node.getState().getFood(),node.getState().getMaterials(),node.getState().getEnergy(),node.getState().getMoneySpent(),node.getState().getDelayTime(),RequestState.FOOD);
        State newRequestFoodState = node.getState().requestFood(problem.variables.get(Attribute.DELAY_REQUEST_FOOD));
        successors.add(new Node(newRequestFoodState, node, newRequestFoodState.getMoneySpent(), Action.RequestFood, node.getDepth() + 1));

        // For RequestMaterials
//        State newStateRequestMaterials = new State(node.getState().getProsperity(), node.getState().getFood(), node.getState().getMaterials(), node.getState().getEnergy(), node.getState().getMoneySpent(), node.getState().getDelayTime(), RequestState.MATERIALS);
        State newStateRequestMaterials = node.getState().requestMaterials(problem.variables.get(Attribute.DELAY_REQUEST_MATERIALS));
        successors.add(new Node(newStateRequestMaterials, node, newStateRequestMaterials.getMoneySpent(), Action.RequestMaterials, node.getDepth() + 1));
        // For RequestEnergy
//        State newStateRequestEnergy =new State(node.getState().getProsperity(), node.getState().getFood(), node.getState().getMaterials(), node.getState().getEnergy(), node.getState().getMoneySpent(), node.getState().getDelayTime(), RequestState.ENERGY);
        State newStateRequestEnergy = node.getState().requestEnergy(problem.variables.get(Attribute.DELAY_REQUEST_ENERGY));
        successors.add(new Node(newStateRequestEnergy, node, newStateRequestEnergy.getMoneySpent(), Action.RequestEnergy, node.getDepth() + 1));


    }

    private static void generateBuildSuccessors(Node node, List<Node> successors) {
        if (checkIfCanBuild(node, 1)) {
            State newState = node.getState().doBuild(problem.variables, 1);
            successors.add(new Node(newState, node, newState.getMoneySpent(), Action.BUILD1, node.getDepth() + 1));
        }
        if (checkIfCanBuild(node, 2)) {
            State newState = node.getState().doBuild(problem.variables, 2);
            successors.add(new Node(newState, node, newState.getMoneySpent(), Action.BUILD2, node.getDepth() + 1));
        }
    }

    private static boolean checkIfCanBuild(Node node, int buildType) {
        return node.getState().getFood() >= problem.variables.get(buildType == 1 ? Attribute.FOOD_USE_BUILD1 : Attribute.FOOD_USE_BUILD2)
            && node.getState().getEnergy() >= problem.variables.get(buildType == 1 ? Attribute.ENERGY_USE_BUILD1 : Attribute.ENERGY_USE_BUILD2)
            && node.getState().getMaterials() >= problem.variables.get(buildType == 1 ? Attribute.MATERIALS_USE_BUILD1 : Attribute.MATERIALS_USE_BUILD2);
    }


    private static void generateBuildWithNewState(Node node, List<Node> successors, State newState) {
        if (checkIfCanBuild(node, 1)) {
            State newState1 = newState.doBuild(problem.variables, 1);
            successors.add(new Node(newState1, node, newState1.getMoneySpent(), Action.BUILD1, node.getDepth() + 1));
//            successors.add(new Node(newState.doBuild(problem.variables, 1), node, node.getState().getMoneySpent(), Action.BUILD1, node.getDepth() + 1));
        }
        if (checkIfCanBuild(node, 2)) {
            State newState2 = newState.doBuild(problem.variables, 2);
            successors.add(new Node(newState2, node, newState2.getMoneySpent(), Action.BUILD2, node.getDepth() + 1));
//            successors.add(new Node(newState.doBuild(problem.variables, 2), node, node.getState().getMoneySpent(), Action.BUILD2, node.getDepth() + 1));
        }
    }


    public static boolean isEndState(Node node) {
        return node.getState().getMoneySpent() >= 100000 || node.getState().getFood() <= 0 || node.getState().getMaterials() <= 0 || node.getState().getEnergy() <= 0;
    }

    public static boolean isGoalState(Node node) {
        return node.getState().getProsperity() >= 100;
    }


}
