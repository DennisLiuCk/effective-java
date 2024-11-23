package chapter2.item2_builder_pattern;

/**
 * Item 2: Consider a builder when faced with many constructor parameters
 * 
 * This class demonstrates the Builder Pattern for a class that would otherwise
 * require many constructors or a telescoping constructor pattern.
 */
public class NutritionFacts {
    private final int servingSize;  // (mL)       required
    private final int servings;     // (per container) required
    private final int calories;     // (per serving)  optional
    private final int fat;          // (g/serving)    optional
    private final int sodium;       // (mg/serving)   optional
    private final int carbohydrate; // (g/serving)    optional

    public static class Builder {
        // Required parameters
        private final int servingSize;
        private final int servings;

        // Optional parameters - initialized to default values
        private int calories = 0;
        private int fat = 0;
        private int sodium = 0;
        private int carbohydrate = 0;

        public Builder(int servingSize, int servings) {
            // Validate required parameters
            if (servingSize <= 0) {
                throw new IllegalArgumentException("Serving size must be positive");
            }
            if (servings <= 0) {
                throw new IllegalArgumentException("Servings must be positive");
            }
            this.servingSize = servingSize;
            this.servings = servings;
        }

        public Builder calories(int val) {
            if (val < 0) {
                throw new IllegalArgumentException("Calories cannot be negative");
            }
            calories = val;
            return this;
        }

        public Builder fat(int val) {
            if (val < 0) {
                throw new IllegalArgumentException("Fat cannot be negative");
            }
            fat = val;
            return this;
        }

        public Builder sodium(int val) {
            if (val < 0) {
                throw new IllegalArgumentException("Sodium cannot be negative");
            }
            sodium = val;
            return this;
        }

        public Builder carbohydrate(int val) {
            if (val < 0) {
                throw new IllegalArgumentException("Carbohydrate cannot be negative");
            }
            carbohydrate = val;
            return this;
        }

        public NutritionFacts build() {
            // Additional validation if needed
            if (calories > 1000) {
                throw new IllegalStateException("Calories per serving cannot exceed 1000");
            }
            return new NutritionFacts(this);
        }
    }

    private NutritionFacts(Builder builder) {
        servingSize = builder.servingSize;
        servings = builder.servings;
        calories = builder.calories;
        fat = builder.fat;
        sodium = builder.sodium;
        carbohydrate = builder.carbohydrate;
    }

    // Getters
    public int getServingSize() { return servingSize; }
    public int getServings() { return servings; }
    public int getCalories() { return calories; }
    public int getFat() { return fat; }
    public int getSodium() { return sodium; }
    public int getCarbohydrate() { return carbohydrate; }

    @Override
    public String toString() {
        return "NutritionFacts{" +
                "servingSize=" + servingSize +
                ", servings=" + servings +
                ", calories=" + calories +
                ", fat=" + fat +
                ", sodium=" + sodium +
                ", carbohydrate=" + carbohydrate +
                '}';
    }
} 