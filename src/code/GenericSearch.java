package code;

 public class GenericSearch {


    public GenericSearch() {
    }


    public static void main(String[] args) {
        String initialState =
                "29;" +
                        "14,9,26;" +
                        "650,400,710;" +
                        "20,2;29,2;38,1;" +
                        "8255,8,7,9,36;" +
                        "30670,12,12,11,36;";
        String strategy = "BF";
        boolean visualize = true;
        LLAPSearch llapSearch = new LLAPSearch();

        String result = llapSearch.solve(initialState, strategy, visualize);
        System.out.println(result);
    }
}
