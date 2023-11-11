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

    private State getState(int givenDelayTime, RequestState requestState) {
        return createState(givenDelayTime, requestState);
    }

    public State doWait() {
        return createState(delayTime, requestState);
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



//    private State getState(int givenDelayTime,RequestState requestState) {
//        int newFood =  food - 1;
//        int newMaterials =  materials - 1;
//        int newEnergy =  energy - 1;
//        int newMoneySpent = moneySpent + LLAPSearch.UNIT_PRICE_FOOD + LLAPSearch.UNIT_PRICE_ENERGY + LLAPSearch.UNIT_PRICE_MATERIALS;
//        return new State(prosperity, newFood, newMaterials, newEnergy, newMoneySpent, givenDelayTime,requestState);
//    }
//
//    public State doWait() {
//        int newFood = food - 1;
//        int newMaterials =  materials - 1;
//        int newEnergy =  energy - 1;
//        int newMoneySpent = moneySpent + LLAPSearch.UNIT_PRICE_FOOD + LLAPSearch.UNIT_PRICE_ENERGY + LLAPSearch.UNIT_PRICE_MATERIALS;
//        return new State(prosperity, newFood, newMaterials, newEnergy, newMoneySpent, delayTime,requestState);
//    }
//

    private State createState(int delayTime, RequestState requestState) {
        int newFood = food - 1;
        int newMaterials = materials - 1;
        int newEnergy = energy - 1;
        int newMoneySpent = moneySpent + LLAPSearch.UNIT_PRICE_FOOD + LLAPSearch.UNIT_PRICE_ENERGY + LLAPSearch.UNIT_PRICE_MATERIALS;
        return new State(prosperity, newFood, newMaterials, newEnergy, newMoneySpent, delayTime, requestState);
    }






    public State doBuild(HashMap<Attribute, Integer> variables, int numOfBuild) {
        int givenFood = (numOfBuild == 1) ? variables.get(Attribute.FOOD_USE_BUILD1) : variables.get(Attribute.FOOD_USE_BUILD2);
        int givenMaterials = (numOfBuild == 1) ? variables.get(Attribute.MATERIALS_USE_BUILD1) : variables.get(Attribute.MATERIALS_USE_BUILD2);
        int givenEnergy = (numOfBuild == 1) ? variables.get(Attribute.ENERGY_USE_BUILD1) : variables.get(Attribute.ENERGY_USE_BUILD2);
        int givenPrice = (numOfBuild == 1) ? variables.get(Attribute.PRICE_BUILD1) : variables.get(Attribute.PRICE_BUILD2);
        int givenProsperity = (numOfBuild == 1) ? variables.get(Attribute.PROSPERITY_BUILD1) : variables.get(Attribute.PROSPERITY_BUILD2);

        int newFood = food - givenFood;
        int newMaterials =  materials - givenMaterials;
        int newEnergy = energy - givenEnergy;
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
        System.out.println("MOS*TEL");
        return null;
    }


    public String toString(){
        return "Prosperity: " + prosperity + " Food: " + food + " Materials: " + materials + " Energy: " + energy + " Money Spent: " + moneySpent + " Delay Time: " + delayTime + " Request State: " + requestState+" ";
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
            delayTime == other.delayTime &&
            requestState == other.requestState;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + prosperity;
        result = 31 * result + food;
        result = 31 * result + materials;
        result = 31 * result + energy;
        result = 31 * result + moneySpent;
        result = 31 * result + delayTime;
        result = 31 * result + (requestState != RequestState.DEFAULT ? requestState.hashCode() : 0);
        return result;
    }







}

