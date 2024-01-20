package bg.sofia.uni.fmi.mjt.cookingcompass.recipe;

import java.util.List;
import java.util.Map;

public record RecipeResponse(int from, int to, int count, Link next, List<Hit> hits) {
    public record Links(Link self, Link next) {
    }

    public record Link(String href, String title) {
    }

    public record Hit(Recipe recipe, Links links) {
    }

    public record Recipe(String uri, String label, String image, Images images, String source, String url,
                         String shareAs, double yield, List<String> dietLabels, List<String> healthLabels,
                         List<String> cautions,
                         List<String> ingredientLines, List<Ingredient> ingredients, double calories,
                         double glycemicIndex, double inflammatoryIndex, double totalCO2Emissions,
                         String co2EmissionsClass, double totalWeight, List<String> cuisineType, List<String> mealType,
                         List<String> dishType,
                         List<String> instructions, List<String> tags, String externalId,
                         Map<String, Object> totalNutrients, Map<String, Object> totalDaily,
                         List<Digest> digest) {
    }

    public record Images(Image THUMBNAIL, Image SMALL, Image REGULAR, Image LARGE) {
    }

    public record Image(String url, int width, int height) {
    }

    public record Ingredient(String text, double quantity, String measure, String food, double weight, String foodId) {
    }

    public record Digest(String label, String tag, String schemaOrgTag, double total, boolean hasRDI,
                         double daily, String unit, List<Map<String, Object>> sub) {
    }
}






