package code.generic;

import code.enums.Attribute;
import code.enums.RequestState;
import code.structure.State;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;

public class Problem {


    public HashMap<Attribute, Integer> variables;

    // Constructor
    public Problem(String input) {
        String[] values = input.split(";");

        variables = new HashMap<>();
        variables.put(Attribute.INITIAL_PROSPERITY, Integer.parseInt(values[0]));

        String[] initialResources = values[1].split(",");
        variables.put(Attribute.INITIAL_FOOD, Integer.parseInt(initialResources[0]));
        variables.put(Attribute.INITIAL_MATERIALS, Integer.parseInt(initialResources[1]));
        variables.put(Attribute.INITIAL_ENERGY, Integer.parseInt(initialResources[2]));

        String[] unitPrices = values[2].split(",");
        variables.put(Attribute.UNIT_PRICE_FOOD, Integer.parseInt(unitPrices[0]));
        variables.put(Attribute.UNIT_PRICE_MATERIALS, Integer.parseInt(unitPrices[1]));
        variables.put(Attribute.UNIT_PRICE_ENERGY, Integer.parseInt(unitPrices[2]));

        String[] requestFood = values[3].split(",");
        variables.put(Attribute.AMOUNT_REQUEST_FOOD, Integer.parseInt(requestFood[0]));
        variables.put(Attribute.DELAY_REQUEST_FOOD, Integer.parseInt(requestFood[1]));

        String[] requestMaterials = values[4].split(",");
        variables.put(Attribute.AMOUNT_REQUEST_MATERIALS, Integer.parseInt(requestMaterials[0]));
        variables.put(Attribute.DELAY_REQUEST_MATERIALS, Integer.parseInt(requestMaterials[1]));

        String[] requestEnergy = values[5].split(",");
        variables.put(Attribute.AMOUNT_REQUEST_ENERGY, Integer.parseInt(requestEnergy[0]));
        variables.put(Attribute.DELAY_REQUEST_ENERGY, Integer.parseInt(requestEnergy[1]));

        String[] build1 = values[6].split(",");
        variables.put(Attribute.PRICE_BUILD1, Integer.parseInt(build1[0]));
        variables.put(Attribute.FOOD_USE_BUILD1, Integer.parseInt(build1[1]));
        variables.put(Attribute.MATERIALS_USE_BUILD1, Integer.parseInt(build1[2]));
        variables.put(Attribute.ENERGY_USE_BUILD1, Integer.parseInt(build1[3]));
        variables.put(Attribute.PROSPERITY_BUILD1, Integer.parseInt(build1[4]));

        String[] build2 = values[7].split(",");
        variables.put(Attribute.PRICE_BUILD2, Integer.parseInt(build2[0]));
        variables.put(Attribute.FOOD_USE_BUILD2, Integer.parseInt(build2[1]));
        variables.put(Attribute.MATERIALS_USE_BUILD2, Integer.parseInt(build2[2]));
        variables.put(Attribute.ENERGY_USE_BUILD2, Integer.parseInt(build2[3]));
        variables.put(Attribute.PROSPERITY_BUILD2, Integer.parseInt(build2[4]));

    }

    public State getInitialState() {
        return new State(variables.get(Attribute.INITIAL_PROSPERITY),
            variables.get(Attribute.INITIAL_FOOD),
            variables.get(Attribute.INITIAL_MATERIALS),
            variables.get(Attribute.INITIAL_ENERGY), 0,0, RequestState.DEFAULT);
    }
}
