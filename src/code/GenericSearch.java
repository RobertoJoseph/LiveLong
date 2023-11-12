package code;

 public class GenericSearch {


    public GenericSearch() {
    }


    public static void main(String[] args) {
        String initialState =
            "17;" +
                "49,30,46;" +
                "7,57,6;" +
                "7,1;20,2;29,2;" +
                "350,10,9,8,28;" +
                "408,8,12,13,34;";
        String strategy = "BF";
        boolean visualize = true;
        LLAPSearch llapSearch = new LLAPSearch();
        String result = llapSearch.solve(initialState, strategy, visualize);
        System.out.println("THE RESULT IS: " + result);


    }
}
