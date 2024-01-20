package bg.sofia.uni.fmi.mjt.cookingcompass;

import bg.sofia.uni.fmi.mjt.cookingcompass.exceptions.BadRequestException;
import bg.sofia.uni.fmi.mjt.cookingcompass.exceptions.FailedRecipeSearch;
import bg.sofia.uni.fmi.mjt.cookingcompass.exceptions.ForbiddenErrorException;
import bg.sofia.uni.fmi.mjt.cookingcompass.exceptions.LimitExceededException;
import bg.sofia.uni.fmi.mjt.cookingcompass.exceptions.RecipeAppException;
import bg.sofia.uni.fmi.mjt.cookingcompass.labels.HealthLabel;
import bg.sofia.uni.fmi.mjt.cookingcompass.labels.MealLabel;

import bg.sofia.uni.fmi.mjt.cookingcompass.recipe.RecipeResponse;
import bg.sofia.uni.fmi.mjt.cookingcompass.uri.URICreater;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class RecipeAppHTTP implements RecipeAPI {
    private static final int MAX_REQUESTS = 2;
    private static final int STATUS_CODE_OK = 200;
    private static final int STATUS_CODE_BAD_REQUEST = 400;
    private static final int STATUS_CODE_LIMIT_EXCEEDED = 401;
    private static final int STATUS_CODE_FORBIDDEN_ERROR = 403;
    private static final int PAGE_BEGIN = 0;
    private static final int PAGE_END = 20;
    private static final int STARTING_NUMBER_OF_REQUESTS = 0;
    private final HttpClient httpClient;
    private final Gson gson;

    public RecipeAppHTTP() {
        httpClient = HttpClient.newBuilder().build();
        gson = new Gson();
    }

    public RecipeAppHTTP(HttpClient httpClientMock) {
        this.httpClient = httpClientMock;
        gson = new Gson();
    }

    private HttpResponse<String> makeHttpRequest(URI uri)
        throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder(uri)
            .header("Accept", "application/json")
            .build();

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private URI handleResponse(HttpResponse<String> response, List<RecipeResponse.Recipe> allRecipes,
                               AtomicInteger count,
                               AtomicInteger from, AtomicInteger to, List<String> keywords,
                               Set<HealthLabel> healthLabels,
                               Set<MealLabel> mealLabels)
        throws URISyntaxException, BadRequestException, ForbiddenErrorException, LimitExceededException {
        switch (response.statusCode()) {
            case STATUS_CODE_OK -> {
                RecipeResponse recipeResponse = parseRecipeResponse(response.body());
                List<RecipeResponse.Recipe> recipes = extractRecipes(recipeResponse);
                allRecipes.addAll(recipes);
                count.set(recipeResponse.count());
                from.set(to.get());
                to.set(to.get() + recipes.size());
                return URICreater.builder().setKeywords(keywords, from.get(), to.get()).setHealthLabels(healthLabels)
                    .setMealLabels(mealLabels)
                    .build().getURI();
            }
            case STATUS_CODE_BAD_REQUEST ->
                throw new BadRequestException("HTTP Request failed with bad request error");
            case STATUS_CODE_LIMIT_EXCEEDED ->
                throw new ForbiddenErrorException("HTTP request failed with forbidden error");
            case STATUS_CODE_FORBIDDEN_ERROR ->
                throw new LimitExceededException("HTTP request failed due to exceeded limit");
        }
        return null;
    }

    private List<RecipeResponse.Recipe> requestRecipes(URI uri, List<String> keywords,
                                                       Set<HealthLabel> healthLabels, Set<MealLabel> mealLabels)
        throws URISyntaxException, IOException, InterruptedException, BadRequestException,
        ForbiddenErrorException, LimitExceededException {
        List<RecipeResponse.Recipe> allRecipes = new ArrayList<>();
        AtomicInteger from = new AtomicInteger(0);
        AtomicInteger to = new AtomicInteger(PAGE_END);
        AtomicInteger count = new AtomicInteger(0);
        int numberOfMadeRequest = STARTING_NUMBER_OF_REQUESTS;

        do {
            numberOfMadeRequest++;
            HttpResponse<String> response = makeHttpRequest(uri);
            uri = handleResponse(response, allRecipes, count, from, to, keywords, healthLabels, mealLabels);
        } while (numberOfMadeRequest < MAX_REQUESTS && from.get() < count.get());
        return allRecipes;
    }

    private RecipeResponse parseRecipeResponse(String responseBody) {
        return gson.fromJson(responseBody, RecipeResponse.class);
    }

    private List<RecipeResponse.Recipe> extractRecipes(RecipeResponse recipeResponse) {
        return recipeResponse.hits().stream().map(RecipeResponse.Hit::recipe).toList();
    }

    private void assertKeywords(List<String> keywords) {
        if (keywords == null) {
            throw new IllegalArgumentException("Keywords cannot be null");
        }
    }

    private void assertHealthLabels(Set<HealthLabel> healthLabels) {
        if (healthLabels == null) {
            throw new IllegalArgumentException("Health labels cannot be null");
        }
    }

    private void assertMealLabels(Set<MealLabel> mealLabels) {
        if (mealLabels == null) {
            throw new IllegalArgumentException("Meal types cannot be null");
        }
    }

    @Override
    public List<RecipeResponse.Recipe> searchRecipes(List<String> keywords,
                                                     Set<HealthLabel> healthLabels, Set<MealLabel> mealLabels)
        throws FailedRecipeSearch, RecipeAppException {
        assertKeywords(keywords);
        assertHealthLabels(healthLabels);
        assertMealLabels(mealLabels);
        try {
            URI uri = URICreater.builder()
                .setKeywords(keywords, PAGE_BEGIN, PAGE_END)
                .setHealthLabels(healthLabels)
                .setMealLabels(mealLabels)
                .build().getURI();
            return requestRecipes(uri, keywords, healthLabels, mealLabels);
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RecipeAppException("Recipe search failed due to an internal error", e.getCause());
        } catch (BadRequestException | ForbiddenErrorException | LimitExceededException e) {
            throw new FailedRecipeSearch("Recipe search failed", e.getCause());
        }
    }
}
