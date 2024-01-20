package bg.sofia.uni.fmi.mjt.cookingcompass.uri;

import bg.sofia.uni.fmi.mjt.cookingcompass.labels.HealthLabel;
import bg.sofia.uni.fmi.mjt.cookingcompass.labels.Label;
import bg.sofia.uni.fmi.mjt.cookingcompass.labels.MealLabel;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class URICreater {

    private static final String QUERY_PARAM_KEYWORD = "q=";
    private static final String QUERY_PARAM_APP_ID = "app_id=";
    private static final String QUERY_PARAM_APP_KEY = "app_key=";
    private static final String QUERY_PARAM_HEALTH = "health=";
    private static final String QUERY_PARAM_MEAL_TYPES = "mealTypes=";
    private static final String QUERY_PARAM_FROM = "from=";
    private static final String QUERY_PARAM_TO = "to=";
    private static final String AMPERSAND = "&";
    private static final String SPACE = "%20";
    private static final String API_ENDPOINT = "https://api.edamam.com/api/recipes/v2?type=public";
    private static final String APP_ID = "48a0b944";
    private static final String APP_KEY = "252f247f5085801a533c4c922f46bd40";
    private final String keywords;
    private final String healthLabels;
    private final String mealLabels;
    public URI getURI() throws URISyntaxException {
        return new URI(keywords + healthLabels + mealLabels);
    }

    public static URIBuilder builder() {
        return new URIBuilder();
    }

    private URICreater(URIBuilder builder) {
        this.keywords = builder.keywords;
        this.healthLabels = builder.healthLabels;
        this.mealLabels = builder.mealLabels;
    }

    public static class URIBuilder {
        private String keywords;
        private String healthLabels;
        private String mealLabels;

        private URIBuilder() {

        }

        public URIBuilder setKeywords(List<String> keywords, int from, int to) {
            StringBuilder keywordsBuilder = new StringBuilder();
            if (!keywords.isEmpty()) {
                keywordsBuilder.append(String.format("%s&%s%s&%s%s&%s%s&%s%s&%s%s", API_ENDPOINT, QUERY_PARAM_KEYWORD,
                    String.join(SPACE, keywords), QUERY_PARAM_APP_ID, APP_ID, QUERY_PARAM_APP_KEY,
                    APP_KEY, QUERY_PARAM_FROM, from, QUERY_PARAM_TO, to));
            } else {
                keywordsBuilder.append(String.format("%s&%s%s&%s%s",
                    API_ENDPOINT, QUERY_PARAM_APP_ID, APP_ID, QUERY_PARAM_APP_KEY, APP_KEY));
            }
            this.keywords = keywordsBuilder.toString();
            return this;
        }

        private String appendLabels(Set<? extends Label> labels, String queryParam) {
            StringBuilder labelsBuilder = new StringBuilder();
            if (!labels.isEmpty()) {
                String paramValues = labels.stream()
                    .map(label -> queryParam + label.getValue()).collect(Collectors.joining(AMPERSAND));
                labelsBuilder.append(AMPERSAND).append(paramValues);
            }
            return labelsBuilder.toString();
        }

        public URIBuilder setHealthLabels(Set<HealthLabel> healthLabels) {
            this.healthLabels = appendLabels(healthLabels, QUERY_PARAM_HEALTH);
            return this;
        }

        public URIBuilder setMealLabels(Set<MealLabel> mealLabels) {
            this.mealLabels = appendLabels(mealLabels, QUERY_PARAM_MEAL_TYPES);
            return this;
        }

        public URICreater build() {
            return new URICreater(this);
        }
    }

}