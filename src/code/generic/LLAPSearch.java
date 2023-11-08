package code.generic;

import code.enums.Attribute;
import code.enums.RequestState;
import code.structure.Node;
import code.structure.State;

public class LLAPSearch extends GenericSearch {



    public static Integer UNIT_PRICE_FOOD;
    public static Integer UNIT_PRICE_MATERIALS;
    public static Integer UNIT_PRICE_ENERGY;
    public static Integer DELAY_TIME;

    public static RequestState requestState = RequestState.DEFAULT;

    public void solve(String initialState,String strategy,boolean visualize) {
        Problem problem = new Problem(initialState);
        UNIT_PRICE_FOOD = problem.variables.get(Attribute.UNIT_PRICE_FOOD);
        UNIT_PRICE_MATERIALS = problem.variables.get(Attribute.UNIT_PRICE_MATERIALS);
        UNIT_PRICE_ENERGY = problem.variables.get(Attribute.UNIT_PRICE_ENERGY);

        State state = problem.getInitialState();
        Node root = new Node(state, null, 0, null);
        switch(strategy){
            case "BF":

                break;
            case "DF":
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

    }

}
