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
        LLAPSearch.problem = new Problem(initialState);

        UNIT_PRICE_FOOD = problem.variables.get(Attribute.UNIT_PRICE_FOOD);
        UNIT_PRICE_MATERIALS = problem.variables.get(Attribute.UNIT_PRICE_MATERIALS);
        UNIT_PRICE_ENERGY = problem.variables.get(Attribute.UNIT_PRICE_ENERGY);

        State state = problem.getInitialState();
        Node root = new Node(state, null, 0, Action.DEFAULT, 0);
        StringBuilder solution = new StringBuilder();

        String value = switch (strategy) {
            case "BF" -> breadthFirstSearch(root, 0);
            case "DF" -> dfs(root, 0, solution, new HashSet<>());
            case "ID" -> iterativeDeepeningSearch(root, solution);
            case "UC" -> searchStrategy(root, 0, 0,0);
            case "GR1" -> searchStrategy(root, 1, 1,0);
            case "GR2" -> searchStrategy(root, 1, 2,0);
            case "AS1" -> searchStrategy(root, 2, 1,0);
            case "AS2" -> searchStrategy(root, 2, 2,0);
            default -> null;
        };
        return value == null ? "NOSOLUTION" : value;
    }


    public static String dfs(Node node, int numOfNodes, StringBuilder pathString, HashSet<Node> visitedNodes) {


        if (isGoalState(node)) {
            return getPathToGoal(node, numOfNodes);
        }
        if (isEndState(node) || visitedNodes.contains(node)) {
            return null;
        }

        visitedNodes.add(node);
        numOfNodes++;
        List<Node> successors = generateSuccessorNodes(node);
        for (Node successor : successors) {
            pathString.append(successor.action()).append(",");
            String result = dfs(successor, numOfNodes, pathString, visitedNodes);
            if (result != null) {
                return result;
            }
            pathString.setLength(pathString.length() - successor.action().toString().length() - 1);
        }

        return null;
    }


    public static String breadthFirstSearch(Node node,int numOfNodes) {
        Queue<Node> queue = new LinkedList<>();
        Set<Node> visitedNodes = new HashSet<>();
        queue.add(node);
        visitedNodes.add(node);
        while (!queue.isEmpty()) {
            Node currentNode = queue.poll();
            numOfNodes++;
            assert currentNode != null;
            if (isGoalState(currentNode)) {
                return getPathToGoal(currentNode, numOfNodes);
            }

            if (isEndState(currentNode)) {
                continue;
            }

            if (visitedNodes.contains(currentNode) && currentNode.action() != Action.DEFAULT) {
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
        return recursiveDLS(node, depthLimit, 0, visitedNodes,0);
    }

    private static String recursiveDLS(Node node, int depthLimit, int currentDepth, HashSet<Node> visitedNodes,int numOfNodes) {
        if (isGoalState(node)) {
            return getPathToGoal(node, numOfNodes);
        }

        if (currentDepth == depthLimit || isEndState(node) || visitedNodes.contains(node)) {
            return null;
        }

        visitedNodes.add(node);
        numOfNodes++;
        List<Node> successors = generateSuccessorNodes(node);

        for (Node successor : successors) {
            String result = recursiveDLS(successor, depthLimit, currentDepth + 1, visitedNodes,numOfNodes);
            if (result != null) {
                return result;
            }
        }

        return null;
    }


    public static String searchStrategy(Node root, int strategy, int heuristic,int numofNodes) {
        PriorityQueue<Node> queue = createPriorityQueue(strategy, heuristic);
        Set<Node> visitedNodes = new HashSet<>();
        queue.add(root);
        visitedNodes.add(root);
        while (!queue.isEmpty()) {
            Node currentNode = queue.poll();

            if (isGoalState(currentNode)) {
                return getPathToGoal(currentNode, numofNodes);
            }

            if (isEndState(currentNode)) {
                continue;
            }
            if (visitedNodes.contains(currentNode) && currentNode.action() != Action.DEFAULT) {
                continue;
            }
            visitedNodes.add(currentNode);
            numofNodes++;

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
            return new PriorityQueue<>(Comparator.comparingInt(Node::pathCost));
        } else if (strategy == 1) {
            return new PriorityQueue<>(Comparator.comparingInt(heuristic == 1 ? (node) -> node.getHeuristicOne(problem.variables) : (node)-> node.getHeuristicTwo(problem.variables)));
        } else {
            return new PriorityQueue<>(Comparator.comparingInt(heuristic == 1 ? (node) -> node.getHeuristicOne(problem.variables) + node.pathCost() : (node)-> node.getHeuristicTwo(problem.variables) + node.pathCost()));
        }
    }


    public static List<Node> generateSuccessorNodes(Node node) {
        List<Node> successors = new ArrayList<>();
        if (node.state().requestState() == RequestState.DEFAULT) {
            generateBuild(node, successors, node.state());
            generateDefaultStateSuccessors(node, successors);
        } else if (node.state().delayTime() == 1) {
            State newState = node.state().resourceDelivered(node.state().requestState(), problem.variables);
            generateBuild(node, successors, newState);
            generateWaitSuccessor(node, newState, successors);
        } else {
            if (node.state().delayTime() > 1) {
                State newState = node.state().decrementDelayTime();
                generateBuild(node, successors, newState);
                generateWaitSuccessor(node, newState, successors);
            }
        }

        return successors;
    }

    private static void generateWaitSuccessor(Node node, State newState, List<Node> successors) {
        if (isEnoughMoneyForDefault(node)) {
            State doWaitState = newState.doWait();
            successors.add(new Node(doWaitState, node, doWaitState.moneySpent(), Action.WAIT, node.depth() + 1));
        }

    }

    private static void generateDefaultStateSuccessors(Node node, List<Node> successors) {

        if (isEnoughMoneyForDefault(node)) {
            if (node.state().food() - 1 + problem.variables.get(Attribute.AMOUNT_REQUEST_FOOD) <= 50) {
                State newRequestFoodState = node.state().requestFood(problem.variables.get(Attribute.DELAY_REQUEST_FOOD));
                successors.add(new Node(newRequestFoodState, node, newRequestFoodState.moneySpent(), Action.RequestFood, node.depth() + 1));
            }


            if (node.state().materials() - 1 + problem.variables.get(Attribute.AMOUNT_REQUEST_MATERIALS) <= 50) {
                State newStateRequestMaterials = node.state().requestMaterials(problem.variables.get(Attribute.DELAY_REQUEST_MATERIALS));
                successors.add(new Node(newStateRequestMaterials, node, newStateRequestMaterials.moneySpent(), Action.RequestMaterials, node.depth() + 1));
            }


            if (node.state().energy() - 1 + problem.variables.get(Attribute.AMOUNT_REQUEST_ENERGY) <= 50) {
                State newStateRequestEnergy = node.state().requestEnergy(problem.variables.get(Attribute.DELAY_REQUEST_ENERGY));
                successors.add(new Node(newStateRequestEnergy, node, newStateRequestEnergy.moneySpent(), Action.RequestEnergy, node.depth() + 1));
            }
        }


    }

    private static void generateBuild(Node node, List<Node> successors, State newState) {
        if (checkIfCanBuild(node, 1)) {
            State buildStateOne = newState.doBuild(problem.variables, 1);
            successors.add(new Node(buildStateOne, node, buildStateOne.moneySpent(), Action.BUILD1, node.depth() + 1));
        }
        if (checkIfCanBuild(node, 2)) {
            State buildStateTwo = newState.doBuild(problem.variables, 2);
            successors.add(new Node(buildStateTwo, node, buildStateTwo.moneySpent(), Action.BUILD2, node.depth() + 1));
        }
    }

    private static boolean checkIfCanBuild(Node node, int buildType) {
        return node.canBuild(problem.variables, buildType);
    }

    public static String getPathToGoal(Node node, int numOfNodes) {
        StringBuilder pathString = new StringBuilder();
        int moneySpent = node.state().moneySpent();
        while (node != null) {
            if (node.action() != Action.DEFAULT) {
                pathString.insert(0, node.action()).insert(0, ",");
            }
            node = node.parent();
        }
        pathString.replace(0, 1, "");
        pathString.append(";");
        pathString.append(moneySpent).append(";");
        pathString.append(numOfNodes).append(";");
        return pathString.toString();

    }


    public static boolean isEndState(Node node) {
        return node.state().moneySpent() >= 100000 || node.state().food() <= 0 || node.state().materials() <= 0 || node.state().energy() <= 0;
    }

    public static boolean isGoalState(Node node) {
        return node.state().prosperity() >= 100;
    }

    public static boolean isEnoughMoneyForDefault(Node node) {
        return node.isEnoughMoneyDefaultRequest(problem.variables, node.state().moneySpent());
    }


}
