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

    public int getHeuristicOne(HashMap<Attribute, Integer> variables) {
        int maxProsperity = Math.max(variables.get(Attribute.PROSPERITY_BUILD1), variables.get(Attribute.PROSPERITY_BUILD2));
        int minPrice = Math.min(variables.get(Attribute.PRICE_BUILD1), variables.get(Attribute.PRICE_BUILD2));
        int neededProsperity = 100 - this.state.prosperity();
        int factor = neededProsperity / maxProsperity;
        return pathCost() + (factor * minPrice);
    }

    public int getHeuristicTwo(HashMap<Attribute, Integer> variables) {
        int maxProsperity = Math.max(variables.get(Attribute.PROSPERITY_BUILD1), variables.get(Attribute.PROSPERITY_BUILD2));
        int neededProsperity = 100 - this.state.prosperity();
        int neededFood = variables.get(Attribute.FOOD_USE_BUILD1);
        int factor = neededProsperity / maxProsperity;
        return ((factor*neededFood)-this.state.food()) * variables.get(Attribute.UNIT_PRICE_FOOD);
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

