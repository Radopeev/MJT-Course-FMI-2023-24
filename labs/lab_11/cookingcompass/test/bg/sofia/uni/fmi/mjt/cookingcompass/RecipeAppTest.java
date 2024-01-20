package bg.sofia.uni.fmi.mjt.cookingcompass;

import bg.sofia.uni.fmi.mjt.cookingcompass.exceptions.FailedRecipeSearch;
import bg.sofia.uni.fmi.mjt.cookingcompass.exceptions.RecipeAppException;
import bg.sofia.uni.fmi.mjt.cookingcompass.labels.HealthLabel;
import bg.sofia.uni.fmi.mjt.cookingcompass.labels.MealLabel;
import bg.sofia.uni.fmi.mjt.cookingcompass.recipe.RecipeResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;


import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

public class RecipeAppTest {
    private static RecipeAPI recipeAppTest;

    @BeforeAll
    static void setup() {
        recipeAppTest = new RecipeAppHTTP();
    }

    @Test
    void testSearchRecipesWhenKeywordIsNull() {
        Set<HealthLabel> healthLabels = Set.of(HealthLabel.VEGAN, HealthLabel.SESAME_FREE);
        Set<MealLabel> mealLabels = Set.of(MealLabel.BREAKFAST, MealLabel.LUNCH);
        assertThrows(IllegalArgumentException.class, () -> recipeAppTest.searchRecipes(null, healthLabels, mealLabels),
            "Should throw an exception when keyword is null");
    }

    @Test
    void testSearchRecipesWhenHealthLabelsIsNull() {
        List<String> keywords = List.of("Chicken");
        Set<MealLabel> mealLabels = Set.of(MealLabel.BREAKFAST, MealLabel.LUNCH);
        assertThrows(IllegalArgumentException.class, () -> recipeAppTest.searchRecipes(keywords, null, mealLabels),
            "Should throw an exception when health labels is null");
    }

    @Test
    void testBadRequestException() throws IOException, InterruptedException, URISyntaxException {
        HttpClient httpClientMock = Mockito.mock(HttpClient.class);
        RecipeAppHTTP recipeAppHTTP = new RecipeAppHTTP(httpClientMock);

        HttpResponse<Object> badRequestResponse = mock(HttpResponse.class);
        Mockito.when(badRequestResponse.statusCode()).thenReturn(400);
        Mockito.when(httpClientMock.send(Mockito.any(HttpRequest.class), Mockito.any()))
            .thenReturn(badRequestResponse);

        assertThrows(FailedRecipeSearch.class, () ->
            recipeAppHTTP.searchRecipes(List.of("keyword"), Set.of(HealthLabel.ALCOHOL_FREE), Set.of(MealLabel.BREAKFAST))
        ,"BadRequestException should be thrown when status code is 400");
    }

    @Test
    void testForbiddenErrorException() throws IOException, InterruptedException {
        HttpClient httpClientMock = Mockito.mock(HttpClient.class);
        RecipeAppHTTP recipeAppHTTP = new RecipeAppHTTP(httpClientMock);

        HttpResponse<Object> badRequestResponse = mock(HttpResponse.class);
        Mockito.when(badRequestResponse.statusCode()).thenReturn(403);
        Mockito.when(httpClientMock.send(Mockito.any(HttpRequest.class), Mockito.any()))
            .thenReturn(badRequestResponse);

        assertThrows(FailedRecipeSearch.class, () ->
            recipeAppHTTP.searchRecipes(List.of("keyword"), Set.of(HealthLabel.ALCOHOL_FREE), Set.of(MealLabel.BREAKFAST))
        ,"ForbiddenErrorException should be thrown when status code is 403");
    }
    @Test
    void testLimitExceededException() throws IOException, InterruptedException{
        HttpClient httpClientMock = Mockito.mock(HttpClient.class);
        RecipeAppHTTP recipeAppHTTP = new RecipeAppHTTP(httpClientMock);

        HttpResponse<Object> badRequestResponse = mock(HttpResponse.class);
        Mockito.when(badRequestResponse.statusCode()).thenReturn(401);
        Mockito.when(httpClientMock.send(Mockito.any(HttpRequest.class), Mockito.any()))
            .thenReturn(badRequestResponse);

        assertThrows(FailedRecipeSearch.class, () ->
            recipeAppHTTP.searchRecipes(List.of("keyword"), Set.of(HealthLabel.ALCOHOL_FREE), Set.of(MealLabel.BREAKFAST))
        ,"LimitExceededException should be thrown when status code is 401");
    }


    @Test
    void testRecipeAppException() throws IOException, InterruptedException {
        HttpClient httpClientMock = Mockito.mock(HttpClient.class);
        RecipeAppHTTP recipeAppHTTP = new RecipeAppHTTP(httpClientMock);

        Mockito.when(httpClientMock.send(Mockito.any(HttpRequest.class), Mockito.any()))
            .thenThrow(IOException.class);

        assertThrows(RecipeAppException.class, () ->
            recipeAppHTTP.searchRecipes(List.of("keyword"), Set.of(HealthLabel.ALCOHOL_FREE), Set.of(MealLabel.BREAKFAST))
        ,"RecipeAppException should be thrown when there is an internal error");
    }

    @Test
    void testSearchRecipesWhenMealLabelsIsNull() {
        List<String> keywords = List.of("Chicken");
        Set<HealthLabel> healthLabels = Set.of(HealthLabel.VEGAN, HealthLabel.SESAME_FREE);
        assertThrows(IllegalArgumentException.class, () -> recipeAppTest.searchRecipes(keywords, healthLabels, null),
            "Should throw an exception when meal labels is null");
    }

    @Test
    void testSearchRecipesWhenThereAreKeyword() throws FailedRecipeSearch, RecipeAppException {
        List<String> keywords = List.of("Chicken","Rice","Beans");
        Set<HealthLabel> healthLabels = Set.of(HealthLabel.VEGAN,HealthLabel.SESAME_FREE);
        List<String> webHealthLabels = List.of("Vegan","Sesame-Free");
        Set<MealLabel> mealLabels = Set.of(MealLabel.BREAKFAST, MealLabel.LUNCH);
        List<String> webMealLabels = List.of("lunch/dinner", "breakfast","snack");
        List<RecipeResponse.Recipe> result = recipeAppTest.searchRecipes(keywords,healthLabels,mealLabels);

        assertTrue(result.stream().allMatch(recipe -> recipe.healthLabels().containsAll(webHealthLabels)),
            "All recipes must contain all health labels");
        assertTrue(result.stream().allMatch(recipe ->containsAnyMatchingItems(recipe.mealType(), webMealLabels)),
            "All recipes must contain at least one of the meal types");
    }

    @Test
    void testSearchRecipesWhenThereAreNoKeyword() throws FailedRecipeSearch, RecipeAppException {
        List<String> keyword =List.of();
        Set<HealthLabel> healthLabels = Set.of(HealthLabel.VEGAN, HealthLabel.SESAME_FREE);
        List<String> webHealthLabels = List.of("Vegan","Sesame-Free");
        Set<MealLabel> mealLabels = Set.of(MealLabel.BREAKFAST,MealLabel.LUNCH);
        List<String> webMealLabels = List.of("lunch/dinner", "teatime","breakfast","snack");
        List<RecipeResponse.Recipe> result = recipeAppTest.searchRecipes(keyword,healthLabels,mealLabels);

        assertTrue(result.stream().allMatch(recipe -> recipe.healthLabels().containsAll(webHealthLabels)),
            "All recipes must contain all health labels");
        assertTrue(result.stream().allMatch(recipe ->containsAnyMatchingItems(recipe.mealType(), webMealLabels)),
            "All recipes must contain at least one of the meal types");
    }

    private boolean containsAnyMatchingItems(List<String> recipeItems, List<String> items) {
        for (String item : items) {
            if (recipeItems.stream().anyMatch(recipeItem -> recipeItem.equalsIgnoreCase(item))) {
                return true;
            }
        }
        return false;
    }
}
