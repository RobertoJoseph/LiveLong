package code;

 public class GenericSearch {


    public GenericSearch() {
    }


    public static void main(String[] args) {
        String initialState =
            "50;" +
                "4,4,4;" +
                "50,60,70;" +
                "2,1;2,1;2,1;" +
                "300,2,2,2,40;" +
                "300,2,2,2,50;";
        String strategy = "DF";
        boolean visualize = true;
        LLAPSearch llapSearch = new LLAPSearch();
        String result = llapSearch.solve(initialState, strategy, visualize);
        System.out.println("THE RESULT IS: " + result);


    }
}
