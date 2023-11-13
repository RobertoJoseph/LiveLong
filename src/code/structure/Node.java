package code.structure;

import code.LLAPSearch;
import code.enums.Action;
import code.enums.Attribute;

import java.util.HashMap;

public record Node(State state, code.structure.Node parent, int pathCost, Action action, int depth) {

    public String toString() {
        return "State: " + state.toString() + "Action: " + this.action().toString() + " Path Cost: " + pathCost + " Depth: " + depth;
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

    public int getHeuristicOne() {
        return Math.max(0, 100 - state().prosperity());
    }

    public int getHeuristicTwo() {
        State currentState = state();

        // Assuming a linear relationship between prosperity and cost
        int estimatedCost = Math.max(0, 100 - currentState.prosperity());

        // Penalize the heuristic based on the scarcity of resources
        estimatedCost += 2 * (currentState.food() + currentState.materials() + currentState.energy());

        return estimatedCost;
    }

    public int totalCost(int heuristic) {
        if (heuristic == 1)
            return pathCost() + getHeuristicOne();
        else
            return pathCost() + getHeuristicTwo();
    }

    public boolean isEnoughMoneyForBuild(HashMap<Attribute, Integer> variables, int numOfBuild, int moneySpent) {
        int givenFood = (numOfBuild == 1) ? variables.get(Attribute.FOOD_USE_BUILD1) : variables.get(Attribute.FOOD_USE_BUILD2);
        int givenMaterials = (numOfBuild == 1) ? variables.get(Attribute.MATERIALS_USE_BUILD1) : variables.get(Attribute.MATERIALS_USE_BUILD2);
        int givenEnergy = (numOfBuild == 1) ? variables.get(Attribute.ENERGY_USE_BUILD1) : variables.get(Attribute.ENERGY_USE_BUILD2);
        int givenPrice = (numOfBuild == 1) ? variables.get(Attribute.PRICE_BUILD1) : variables.get(Attribute.PRICE_BUILD2);
        int givenProsperity = (numOfBuild == 1) ? variables.get(Attribute.PROSPERITY_BUILD1) : variables.get(Attribute.PROSPERITY_BUILD2);

        return 100000 - (moneySpent + givenPrice + (LLAPSearch.UNIT_PRICE_FOOD * givenFood) + (LLAPSearch.UNIT_PRICE_ENERGY * givenEnergy) + (LLAPSearch.UNIT_PRICE_MATERIALS * givenMaterials)) >= 0;

    }

    public boolean isEnoughMoneyDefaultRequest(HashMap<Attribute, Integer> variables, int moneySpent) {
        return 100000 - (moneySpent + LLAPSearch.UNIT_PRICE_FOOD + LLAPSearch.UNIT_PRICE_ENERGY + LLAPSearch.UNIT_PRICE_MATERIALS) >= 0;

    }

    public boolean canBuild(HashMap<Attribute, Integer> variables, int buildType) {
        return this.state().food() >= variables.get(buildType == 1 ? Attribute.FOOD_USE_BUILD1 : Attribute.FOOD_USE_BUILD2)
            && this.state().energy() >= variables.get(buildType == 1 ? Attribute.ENERGY_USE_BUILD1 : Attribute.ENERGY_USE_BUILD2)
            && this.state().materials() >= variables.get(buildType == 1 ? Attribute.MATERIALS_USE_BUILD1 : Attribute.MATERIALS_USE_BUILD2)
            && this.isEnoughMoneyForBuild(variables, buildType, this.state().moneySpent());
    }


}

