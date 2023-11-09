package code.structure;

import code.enums.Attribute;
import code.enums.RequestState;
import code.LLAPSearch;

import java.util.HashMap;

public class State {
    private int prosperity;
    private int food;
    private int materials;
    private int energy;
    private int moneySpent;
    private int delayTime;
    private RequestState requestState;


    public State(int prosperity, int food, int materials, int energy, int moneySpent, int delayTime,RequestState requestState) {
        this.prosperity = prosperity;
        this.food = food;
        this.materials = materials;
        this.energy = energy;
        this.moneySpent = moneySpent;
        this.delayTime = delayTime;
        this.requestState = requestState;
    }

    public RequestState getRequestState(){
        return requestState;
    }

    public void setRequestState(RequestState requestState){
        this.requestState = requestState;
    }

    public int getProsperity() {
        return prosperity;
    }

    public void setProsperity(int prosperity) {
        this.prosperity = prosperity;
    }

    public int getFood() {
        return food;
    }

    public void setFood(int food) {
        this.food = food;
    }

    public int getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(int delayTime) {
        this.delayTime = delayTime;
    }


    public int getMaterials() {
        return materials;
    }

    public void setMaterials(int materials) {
        this.materials = materials;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }


    public State requestMaterials(int delayTime) {
        return getState(delayTime,RequestState.MATERIALS);
    }

    public State requestFood(int delayTime) {
        return getState(delayTime,RequestState.FOOD);
    }

    public State requestEnergy(int delayTime) {
        return getState(delayTime,RequestState.ENERGY);
    }

    private State getState(int delayTime,RequestState requestState) {
        int newFood = Math.max(0, food - 1);
        int newMaterials = Math.max(0, materials - 1);
        int newEnergy = Math.max(0, energy - 1);
        int newMoneySpent = moneySpent + LLAPSearch.UNIT_PRICE_FOOD + LLAPSearch.UNIT_PRICE_ENERGY + LLAPSearch.UNIT_PRICE_MATERIALS;
        return new State(prosperity, newFood, newMaterials, newEnergy, newMoneySpent, delayTime,requestState);
    }

    public State doWait() {
        int newFood = Math.max(0, food - 1);
        int newMaterials = Math.max(0, materials - 1);
        int newEnergy = Math.max(0, energy - 1);
        int newMoneySpent = moneySpent + LLAPSearch.UNIT_PRICE_FOOD + LLAPSearch.UNIT_PRICE_ENERGY + LLAPSearch.UNIT_PRICE_MATERIALS;
        return new State(prosperity, newFood, newMaterials, newEnergy, newMoneySpent, delayTime,requestState);
    }

    public State doBuild(HashMap<Attribute, Integer> variables, int numOfBuild) {
        int givenFood = (numOfBuild == 1) ? variables.get(Attribute.FOOD_USE_BUILD1) : variables.get(Attribute.FOOD_USE_BUILD2);
        int givenMaterials = (numOfBuild == 1) ? variables.get(Attribute.MATERIALS_USE_BUILD1) : variables.get(Attribute.MATERIALS_USE_BUILD2);
        int givenEnergy = (numOfBuild == 1) ? variables.get(Attribute.ENERGY_USE_BUILD1) : variables.get(Attribute.ENERGY_USE_BUILD2);
        int givenPrice = (numOfBuild == 1) ? variables.get(Attribute.PRICE_BUILD1) : variables.get(Attribute.PRICE_BUILD2);
        int givenProsperity = (numOfBuild == 1) ? variables.get(Attribute.PROSPERITY_BUILD1) : variables.get(Attribute.PROSPERITY_BUILD2);

        int newFood = Math.max(0, food - givenFood);
        int newMaterials = Math.max(0, materials - givenMaterials);
        int newEnergy = Math.max(0, energy - givenEnergy);
        int newMoneySpent = moneySpent + givenPrice + LLAPSearch.UNIT_PRICE_FOOD * givenFood + LLAPSearch.UNIT_PRICE_ENERGY * givenEnergy + LLAPSearch.UNIT_PRICE_MATERIALS * givenMaterials;
        int newProsperity = prosperity + givenProsperity;
        return new State(newProsperity, newFood, newMaterials, newEnergy, newMoneySpent, delayTime,requestState);
    }


    public int getMoneySpent() {
        return moneySpent;
    }

    public State resourceDelivered(RequestState requestState, HashMap<Attribute, Integer> variables) {
        if (requestState == RequestState.FOOD)
            return new State(prosperity, Math.min(50, food + variables.get(Attribute.AMOUNT_REQUEST_FOOD)), materials, energy, moneySpent, delayTime,RequestState.DEFAULT);

        else if (requestState == RequestState.MATERIALS)
            return new State(prosperity, food, Math.min(50, materials + variables.get(Attribute.AMOUNT_REQUEST_MATERIALS)), energy, moneySpent, delayTime,RequestState.DEFAULT);

        else if (requestState == RequestState.ENERGY)
            return new State(prosperity, food, materials, Math.min(50, energy + variables.get(Attribute.AMOUNT_REQUEST_ENERGY)), moneySpent, delayTime,RequestState.DEFAULT);

        return null;
    }


    // Methods for updating the state based on different actions

    // Getter and setter methods for properties
}

