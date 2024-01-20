package bg.sofia.uni.fmi.mjt.cookingcompass.exceptions;

public class FailedRecipeSearch extends Exception {
    public FailedRecipeSearch(String message) {
        super(message);
    }
    public FailedRecipeSearch(String message, Throwable cause) {
        super(message, cause);
    }
}
