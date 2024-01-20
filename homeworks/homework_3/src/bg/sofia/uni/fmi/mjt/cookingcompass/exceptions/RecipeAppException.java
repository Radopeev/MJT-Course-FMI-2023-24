package bg.sofia.uni.fmi.mjt.cookingcompass.exceptions;

public class RecipeAppException extends Exception {
    public RecipeAppException(String message) {
        super(message);
    }

    public RecipeAppException(String message, Throwable cause) {
        super(message, cause);
    }
}
