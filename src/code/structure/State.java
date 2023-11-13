package code.structure;

import code.enums.Attribute;
import code.enums.RequestState;
import code.LLAPSearch;

import java.util.HashMap;

public record State(int prosperity, int food, int materials, int energy, int moneySpent, int delayTime,
                    RequestState requestState) {

    private State getState(int givenDelayTime, RequestState requestState) {
        return createState(givenDelayTime, requestState);
    }

    public State doWait() {
        return createState(delayTime, requestState);
    }

    public State requestMaterials(int delayTime) {
        return getState(delayTime, RequestState.MATERIALS);
    }

    public State requestFood(int delayTime) {
        return getState(delayTime, RequestState.FOOD);
    }

    public State requestEnergy(int delayTime) {
        return getState(delayTime, RequestState.ENERGY);
    }

    private State createState(int delayTime, RequestState bodyRequestState) {
        int newFood = food - 1;
        int newMaterials = materials - 1;
        int newEnergy = energy - 1;
        int newMoneySpent = moneySpent + LLAPSearch.UNIT_PRICE_FOOD + LLAPSearch.UNIT_PRICE_ENERGY + LLAPSearch.UNIT_PRICE_MATERIALS;
        return new State(prosperity, newFood, newMaterials, newEnergy, newMoneySpent, delayTime, bodyRequestState);
    }

    public State doBuild(HashMap<Attribute, Integer> variables, int numOfBuild) {
        int givenFood = variables.get(numOfBuild == 1 ? Attribute.FOOD_USE_BUILD1 : Attribute.FOOD_USE_BUILD2);
        int givenMaterials = variables.get(numOfBuild == 1 ? Attribute.MATERIALS_USE_BUILD1 : Attribute.MATERIALS_USE_BUILD2);
        int givenEnergy = variables.get(numOfBuild == 1 ? Attribute.ENERGY_USE_BUILD1 : Attribute.ENERGY_USE_BUILD2);
        int givenPrice = variables.get(numOfBuild == 1 ? Attribute.PRICE_BUILD1 : Attribute.PRICE_BUILD2);
        int givenProsperity = variables.get(numOfBuild == 1 ? Attribute.PROSPERITY_BUILD1 : Attribute.PROSPERITY_BUILD2);

        return new State(prosperity + givenProsperity, food - givenFood, materials - givenMaterials, energy - givenEnergy, moneySpent + givenPrice + (LLAPSearch.UNIT_PRICE_FOOD * givenFood) + (LLAPSearch.UNIT_PRICE_ENERGY * givenEnergy) + (LLAPSearch.UNIT_PRICE_MATERIALS * givenMaterials), delayTime, requestState);
    }

    public State resourceDelivered(RequestState requestState, HashMap<Attribute, Integer> variables) {
        if (requestState == RequestState.FOOD)
            return new State(prosperity, Math.min(50, food + variables.get(Attribute.AMOUNT_REQUEST_FOOD)), materials, energy, moneySpent, delayTime - 1, RequestState.DEFAULT);

        if (requestState == RequestState.MATERIALS)
            return new State(prosperity, food, Math.min(50, materials + variables.get(Attribute.AMOUNT_REQUEST_MATERIALS)), energy, moneySpent, delayTime - 1, RequestState.DEFAULT);

        if (requestState == RequestState.ENERGY)
            return new State(prosperity, food, materials, Math.min(50, energy + variables.get(Attribute.AMOUNT_REQUEST_ENERGY)), moneySpent, delayTime - 1, RequestState.DEFAULT);
        return null;
    }

    public State decrementDelayTime() {
        return new State(
            this.prosperity(),
            this.food(),
            this.materials(),
            this.energy(),
            this.moneySpent(),
            this.delayTime() - 1,
            this.requestState()
        );
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        State other = (State) obj;
        return prosperity == other.prosperity &&
            food == other.food &&
            materials == other.materials &&
            energy == other.energy &&
            moneySpent == other.moneySpent &&
            delayTime == other.delayTime && requestState == other.requestState;
    }

    @Override
    public int hashCode(){
        int result = 17;
        result = 31 * result + prosperity;
        result = 31 * result + food;
        result = 31 * result + materials;
        result = 31 * result + energy;
        result = 31 * result + moneySpent;
        result = 31 * result + delayTime;
        result = 31 * result + requestState.hashCode();
        return result;

    }


}

