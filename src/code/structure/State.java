package code.structure;

import code.generic.GenericSearch;
import code.generic.LLAPSearch;

public class State {
    private int prosperity;
    private int food;
    private int materials;
    private int energy;
    private int moneySpent;

    public State(int prosperity, int food, int materials, int energy, int moneySpent) {
        this.prosperity = prosperity;
        this.food = food;
        this.materials = materials;
        this.energy = energy;
        this.moneySpent = moneySpent;
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


    public State requestMaterials(int delayTime){
        return getState(delayTime);
    }
    public State requestFood(int delayTime){
        return getState(delayTime);
    }

    public State requestEnergy(int delayTime){
        return getState(delayTime);
    }

    private State getState(int delayTime) {
        int newFood = food-1;
        int newMaterials = materials-1;
        int newEnergy = energy-1;
        int newMoneySpent = moneySpent+ LLAPSearch.UNIT_PRICE_FOOD+LLAPSearch.UNIT_PRICE_ENERGY+LLAPSearch.UNIT_PRICE_MATERIALS;
        LLAPSearch.DELAY_TIME = delayTime;
        return new State(prosperity,newFood,newMaterials,newEnergy,newMoneySpent);
    }

    public State doWait(){
        int newFood = food-1;
        int newMaterials = materials-1;
        int newEnergy = energy-1;
        LLAPSearch.DELAY_TIME--;
        return new State(prosperity,newFood,newMaterials,newEnergy,moneySpent);
    }

    public State doBuild(int givenFood,int givenMaterials,int givenEnergy ,int givenPrice,int givenProsperity){
        LLAPSearch.DELAY_TIME--;
        int newFood = food-givenFood;
        int newMaterials = materials-givenMaterials;
        int newEnergy = energy-givenEnergy;
        int newMoneySpent = moneySpent+givenPrice+LLAPSearch.UNIT_PRICE_FOOD*givenFood+LLAPSearch.UNIT_PRICE_ENERGY*givenEnergy+LLAPSearch.UNIT_PRICE_MATERIALS*givenMaterials;
        int newProsperity = prosperity+givenProsperity;
        return new State(newProsperity,newFood,newMaterials,newEnergy,newMoneySpent);

    }



    // Methods for updating the state based on different actions

    // Getter and setter methods for properties
}

