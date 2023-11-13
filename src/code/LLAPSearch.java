package code;

import code.enums.Action;
import code.enums.Attribute;
import code.enums.RequestState;
import code.generic.Problem;
import code.structure.Node;
import code.structure.State;

import java.util.*;

import static code.structure.Node.totalCost;

public class LLAPSearch extends GenericSearch {


    public static Integer UNIT_PRICE_FOOD;
    public static Integer UNIT_PRICE_MATERIALS;
    public static Integer UNIT_PRICE_ENERGY;
    public static Problem problem;


    public static String solve(String initialState, String strategy, boolean visualize) {
        LLAPSearch.problem = new Problem(initialState);
        System.out.println("PACKAGE");

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
                value = dfs(root, 0, solution, new HashSet<>());
                break;
            case "ID":
                value = iterativeDeepeningSearch(root, solution);
                break;
            case "UC":
                value = bfsVersions(root, 0, 0);
                break;
            case "GR1":
                value = bfsVersions(root, 1, 1);
                break;
            case "GR2":
                value = bfsVersions(root, 1, 2);
                break;
            case "AS1":
                value = bfsVersions(root, 2, 1);
                break;
            case "AS2":
                value = bfsVersions(root, 2, 2);
                break;
        }
        return value == null ? "nosolution" : value;
    }


    public static String dfs(Node node, int numOfNodes, StringBuilder pathString, HashSet<Node> visitedNodes) {

        if (isGoalState(node)) {
            return getPathToGoal(node, numOfNodes, pathString);
        }
        if (isEndState(node) || visitedNodes.contains(node)) {
            return null;
        }

        visitedNodes.add(node);
        List<Node> successors = generateSuccessorNodes(node);
        for (Node successor : successors) {
            pathString.append(successor.getAction()).append(",");
            numOfNodes++;
            String result = dfs(successor, numOfNodes, pathString, visitedNodes);
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
            if (node.getAction() != Action.DEFAULT) {
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


    public static String breadthFirstSearch(Node node) {
        Queue<Node> queue = new LinkedList<>();
        Set<Node> visitedNodes = new HashSet<>();
        queue.add(node);
        visitedNodes.add(node);
        while (!queue.isEmpty()) {
            Node currentNode = queue.poll();
            assert currentNode != null;
            if (isGoalState(currentNode)) {
                return getPathToGoalBfs(currentNode, 0);
            }

            if (isEndState(currentNode)) {
                continue;
            }

            if (visitedNodes.contains(currentNode) && currentNode.getAction() != Action.DEFAULT) {
                continue;
            }

            visitedNodes.add(currentNode);

            List<Node> successors = generateSuccessorNodes(currentNode);
            for (Node successor : successors) {
                if (!visitedNodes.contains(successor)) {
                    queue.add(successor);
                }
            }
        }
        return null;
    }

    public static String iterativeDeepeningSearch(Node root, StringBuilder solution) {
        for (int depth = 0; depth < Integer.MAX_VALUE; depth++) {
            String result = depthLimitedSearch(root, depth, solution, new HashSet<>());
            if (result != null) {
                return result;
            }
        }
        return null; // If no solution is found within the depth limit
    }


    public static String depthLimitedSearch(Node node, int depthLimit, StringBuilder pathString, HashSet<Node> visitedNodes) {
        return recursiveDLS(node, depthLimit, 0, pathString, visitedNodes);
    }

    private static String recursiveDLS(Node node, int depthLimit, int currentDepth, StringBuilder pathString, HashSet<Node> visitedNodes) {
        if (isGoalState(node)) {
            return getPathToGoal(node, 0, pathString);
        }

        if (currentDepth == depthLimit || isEndState(node) || visitedNodes.contains(node)) {
            return null;
        }

        visitedNodes.add(node);
        List<Node> successors = generateSuccessorNodes(node);

        for (Node successor : successors) {
            pathString.append(successor.getAction()).append(",");
            String result = recursiveDLS(successor, depthLimit, currentDepth + 1, pathString, visitedNodes);

            if (result != null) {
                return result;
            }

            pathString.setLength(pathString.length() - successor.getAction().toString().length() - 1);
        }

        return null;
    }


    public static String bfsVersions(Node root, int strategy, int heuristic) {
        PriorityQueue<Node> queue = createPriorityQueue(strategy, heuristic);
        Set<Node> visitedNodes = new HashSet<>();
        queue.add(root);
        visitedNodes.add(root);
        while (!queue.isEmpty()) {
            Node currentNode = queue.poll();

            if (isGoalState(currentNode)) {
                return getPathToGoalBfs(currentNode, 0);
            }

            if (isEndState(currentNode)) {
                continue;
            }
            if (visitedNodes.contains(currentNode) && currentNode.getAction() != Action.DEFAULT) {
                System.out.println("CONTAINS");
                continue;
            }
            visitedNodes.add(currentNode);

            List<Node> successors = generateSuccessorNodes(currentNode);

            for (Node successor : successors) {
                if (!visitedNodes.contains(successor)) {
                    queue.add(successor);
                }
            }
        }

        return null;

    }

    private static PriorityQueue<Node> createPriorityQueue(int strategy, int heuristic) {
        if (strategy == 0) {
            return new PriorityQueue<>(Comparator.comparingInt(Node::getPathCost));
        } else if (strategy == 1) {
            return new PriorityQueue<>(Comparator.comparingInt(heuristic == 1 ? Node::getHeuristicOne : Node::getHeuristicTwo));
        } else {
            return new PriorityQueue<>(Comparator.comparingInt(node -> totalCost(node, heuristic)));
        }
    }

    public static List<Node> generateSuccessorNodes(Node node) {
        List<Node> successors = new ArrayList<>();
        if (node.getState().getRequestState() == RequestState.DEFAULT) {
            generateBuildSuccessors(node, successors);
            generateDefaultStateSuccessors(node, successors);
        } else {
            if (node.getState().getDelayTime() == 1) {
                State newState = node.getState().resourceDelivered(node.getState().getRequestState(), problem.variables);
                generateBuildWithNewState(node, successors, newState);
                generateWaitSuccessor(node, newState, successors);
            } else {
                // Add condition to handle delay time greater than 1
                if (node.getState().getDelayTime() > 1) {
                    State currentState = node.getState();
                    State newState = new State(
                        currentState.getProsperity(),
                        currentState.getFood(),
                        currentState.getMaterials(),
                        currentState.getEnergy(),
                        currentState.getMoneySpent(),
                        currentState.getDelayTime() - 1,
                        currentState.getRequestState()
                    );
                    generateBuildWithNewState(node, successors, newState);
                    generateWaitSuccessor(node, newState, successors);
                }
            }
        }
        return successors;
    }

    private static void generateWaitSuccessor(Node node, State newState, List<Node> successors) {
        if (node.isEnoughMoneyDefaultRequest(problem.variables, node.getState().getMoneySpent())) {
            State doWaitState = newState.doWait();
            successors.add(new Node(doWaitState, node, doWaitState.getMoneySpent(), Action.WAIT, node.getDepth() + 1));
        }

    }

    private static void generateDefaultSuccessorsWithNewState(Node node, List<Node> successors, State newState) {

        if (node.isEnoughMoneyDefaultRequest(problem.variables, node.getState().getMoneySpent())) {

            if (node.getState().getFood() - 1 + problem.variables.get(Attribute.AMOUNT_REQUEST_FOOD) <= 50) {
                State newStateRequestFood = newState.requestFood(problem.variables.get(Attribute.DELAY_REQUEST_FOOD));
                successors.add(new Node(newStateRequestFood, node, newStateRequestFood.getMoneySpent(), Action.RequestFood, node.getDepth() + 1));
            }


            if (node.getState().getMaterials() - 1 + problem.variables.get(Attribute.AMOUNT_REQUEST_MATERIALS) <= 50) {
                State newStateRequestMaterials = newState.requestMaterials(problem.variables.get(Attribute.DELAY_REQUEST_MATERIALS));
                successors.add(new Node(newStateRequestMaterials, node, newStateRequestMaterials.getMoneySpent(), Action.RequestMaterials, node.getDepth() + 1));
            }


            if (node.getState().getEnergy() - 1 + problem.variables.get(Attribute.AMOUNT_REQUEST_ENERGY) <= 50) {
                State newStateRequestEnergy = newState.requestEnergy(problem.variables.get(Attribute.DELAY_REQUEST_ENERGY));
                successors.add(new Node(newStateRequestEnergy, node, newStateRequestEnergy.getMoneySpent(), Action.RequestEnergy, node.getDepth() + 1));
            }
        }


    }


    private static void generateDefaultStateSuccessors(Node node, List<Node> successors) {

        if (node.isEnoughMoneyDefaultRequest(problem.variables, node.getState().getMoneySpent())) {

            if (node.getState().getFood() - 1 + problem.variables.get(Attribute.AMOUNT_REQUEST_FOOD) <= 50) {
                State newRequestFoodState = node.getState().requestFood(problem.variables.get(Attribute.DELAY_REQUEST_FOOD));
                successors.add(new Node(newRequestFoodState, node, newRequestFoodState.getMoneySpent(), Action.RequestFood, node.getDepth() + 1));
            }


            // For RequestMaterials
            if (node.getState().getMaterials() - 1 + problem.variables.get(Attribute.AMOUNT_REQUEST_MATERIALS) <= 50) {
                State newStateRequestMaterials = node.getState().requestMaterials(problem.variables.get(Attribute.DELAY_REQUEST_MATERIALS));
                successors.add(new Node(newStateRequestMaterials, node, newStateRequestMaterials.getMoneySpent(), Action.RequestMaterials, node.getDepth() + 1));
            }


            // For RequestEnergy
            if (node.getState().getEnergy() - 1 + problem.variables.get(Attribute.AMOUNT_REQUEST_ENERGY) <= 50) {
                State newStateRequestEnergy = node.getState().requestEnergy(problem.variables.get(Attribute.DELAY_REQUEST_ENERGY));
                successors.add(new Node(newStateRequestEnergy, node, newStateRequestEnergy.getMoneySpent(), Action.RequestEnergy, node.getDepth() + 1));
            }
        }


    }

    private static void generateBuildSuccessors(Node node, List<Node> successors) {
        if (checkIfCanBuild(node, 1)) {
            State buildStateOne = node.getState().doBuild(problem.variables, 1);
            successors.add(new Node(buildStateOne, node, buildStateOne.getMoneySpent(), Action.BUILD1, node.getDepth() + 1));
        }
        if (checkIfCanBuild(node, 2)) {
            State buildStateTwo = node.getState().doBuild(problem.variables, 2);
            successors.add(new Node(buildStateTwo, node, buildStateTwo.getMoneySpent(), Action.BUILD2, node.getDepth() + 1));
        }
    }

    private static boolean checkIfCanBuild(Node node, int buildType) {
        return node.getState().getFood() >= problem.variables.get(buildType == 1 ? Attribute.FOOD_USE_BUILD1 : Attribute.FOOD_USE_BUILD2)
            && node.getState().getEnergy() >= problem.variables.get(buildType == 1 ? Attribute.ENERGY_USE_BUILD1 : Attribute.ENERGY_USE_BUILD2)
            && node.getState().getMaterials() >= problem.variables.get(buildType == 1 ? Attribute.MATERIALS_USE_BUILD1 : Attribute.MATERIALS_USE_BUILD2)
            && node.isEnoughMoneyForBuild(problem.variables, buildType, node.getState().getMoneySpent());
    }


    private static void generateBuildWithNewState(Node node, List<Node> successors, State newState) {
        if (checkIfCanBuild(node, 1)) {
            State buildStateOne = newState.doBuild(problem.variables, 1);
            successors.add(new Node(buildStateOne, node, buildStateOne.getMoneySpent(), Action.BUILD1, node.getDepth() + 1));
        }
        if (checkIfCanBuild(node, 2)) {
            State buildStateTwo = newState.doBuild(problem.variables, 2);
            successors.add(new Node(buildStateTwo, node, buildStateTwo.getMoneySpent(), Action.BUILD2, node.getDepth() + 1));
        }
    }


    public static boolean isEndState(Node node) {
        return node.getState().getMoneySpent() >= 100000 || node.getState().getFood() <= 0 || node.getState().getMaterials() <= 0 || node.getState().getEnergy() <= 0;
    }

    public static boolean isGoalState(Node node) {
        return node.getState().getProsperity() >= 100;
    }


}
