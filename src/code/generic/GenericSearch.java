package code.generic;

 public class GenericSearch {


    public GenericSearch() {
    }


    public static void main(String[] args) {
        String initialState =
            "50;" +
                "4,4,4;" +
                "50,60,70;" +
                "2,1;2,1;2,1;" +
                "300,1,1,1,40;" +
                "300,1,1,1,50;";
        String strategy = "DF";
        boolean visualize = true;
        LLAPSearch llapSearch = new LLAPSearch();
        String result = llapSearch.solve(initialState, strategy, visualize);
        System.out.println(result);


    }
}
