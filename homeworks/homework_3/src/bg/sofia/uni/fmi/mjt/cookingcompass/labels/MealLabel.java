package bg.sofia.uni.fmi.mjt.cookingcompass.labels;

import bg.sofia.uni.fmi.mjt.cookingcompass.labels.Label;

public enum MealLabel implements Label {
    BREAKFAST("Breakfast"),
    DINNER("Dinner"),
    LUNCH("Lunch"),
    SNACK("Snack"),
    TEATIME("Teatime");

    private final String value;

    MealLabel(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }
}
