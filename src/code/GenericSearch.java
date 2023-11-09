package code;

 public class GenericSearch {


    public GenericSearch() {
    }


    public static void main(String[] args) {
        String initialState = "72;" +
            "36,13,35;" +
            "75,96,62;" +
            "20,2;5,2;33,2;" +
            "30013,7,6,3,36;" +
            "40050,5,10,14,44;";
        String strategy = "DF";
        boolean visualize = true;
        LLAPSearch llapSearch = new LLAPSearch();
        String result = llapSearch.solve(initialState, strategy, visualize);
        System.out.println("THE RESULT " +result);


    }
}
