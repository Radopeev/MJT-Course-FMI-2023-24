package bg.sofia.uni.fmi.mjt.cookingcompass;

import bg.sofia.uni.fmi.mjt.cookingcompass.exceptions.FailedRecipeSearch;
import bg.sofia.uni.fmi.mjt.cookingcompass.exceptions.RecipeAppException;
import bg.sofia.uni.fmi.mjt.cookingcompass.labels.HealthLabel;
import bg.sofia.uni.fmi.mjt.cookingcompass.labels.MealLabel;
import bg.sofia.uni.fmi.mjt.cookingcompass.recipe.RecipeResponse;

import java.util.List;
import java.util.Set;

public interface RecipeAPI {
    List<RecipeResponse.Recipe> searchRecipes(List<String> keywords, Set<HealthLabel> healthLabels, Set<MealLabel> mealLabels)
        throws FailedRecipeSearch, RecipeAppException;
}
