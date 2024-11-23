package chapter2.item2_builder_pattern;

public class BuilderPatternDemoTest {
    public static void main(String[] args) {
        testNutritionFactsBuilder();
        testPizzaBuilder();
        testValidation();
    }

    private static void testNutritionFactsBuilder() {
        System.out.println("\nTesting NutritionFacts Builder:");
        System.out.println("------------------------------");

        NutritionFacts cocaCola = new NutritionFacts.Builder(240, 8)
                .calories(100)
                .sodium(35)
                .carbohydrate(27)
                .build();
        System.out.println("Nutrition Facts of Coca Cola: " + cocaCola);

        NutritionFacts water = new NutritionFacts.Builder(500, 1)
                .build();
        System.out.println("Nutrition Facts of Water: " + water);
    }

    private static void testPizzaBuilder() {
        System.out.println("\nTesting Pizza Builder:");
        System.out.println("--------------------");

        // NY Pizza
        NyPizza nyPizza = new NyPizza.Builder(NyPizza.Size.SMALL)
                .addToppings(Pizza.Topping.MUSHROOM, Pizza.Topping.ONION)
                .build();
        System.out.println("NY Pizza: [Toppings] " + nyPizza.toppings + " [Size] " + nyPizza.size);

        // Calzone
        Calzone calzone = new Calzone.Builder()
                .addTopping(Pizza.Topping.HAM)
                .sauceInside()
                .build();
        System.out.println("Calzone: [Toppings] " + calzone.toppings + " [Sauce Inside] " + calzone.sauceInside);

        // Chicago Pizza
        ChicagoPizza chicagoPizza = new ChicagoPizza.Builder()
                .crustThickness(ChicagoPizza.CrustThickness.THICK)
                .addToppings(
                    Pizza.Topping.SAUSAGE,
                    Pizza.Topping.MUSHROOM,
                    Pizza.Topping.ONION
                )
                .extraCheese()
                .stuffedCrust()
                .build();
        System.out.println("Chicago Pizza: [Crust Thickness] " + chicagoPizza.crustThickness + " [Toppings] " + chicagoPizza.toppings + " [Extra Cheese] " + chicagoPizza.extraCheese + " [Stuffed Crust] " + chicagoPizza.stuffedCrust);
    }

    private static void testValidation() {
        System.out.println("\nTesting Validation:");
        System.out.println("------------------");

        try {
            // Test negative serving size
            new NutritionFacts.Builder(-1, 1).build();
        } catch (IllegalArgumentException e) {
            System.out.println("Caught expected error: " + e.getMessage());
        }

        try {
            // Test too many toppings
            new NyPizza.Builder(NyPizza.Size.LARGE)
                    .addTopping(Pizza.Topping.HAM)
                    .addTopping(Pizza.Topping.MUSHROOM)
                    .addTopping(Pizza.Topping.ONION)
                    .addTopping(Pizza.Topping.PEPPER)
                    .addTopping(Pizza.Topping.SAUSAGE)
                    .addTopping(Pizza.Topping.HAM)
                    .addTopping(Pizza.Topping.MUSHROOM)
                    .build();
        } catch (IllegalStateException e) {
            System.out.println("Caught expected error: " + e.getMessage());
        }

        try {
            // Test stuffed crust with thin crust
            new ChicagoPizza.Builder()
                    .crustThickness(ChicagoPizza.CrustThickness.THIN)
                    .addTopping(Pizza.Topping.SAUSAGE)
                    .stuffedCrust()
                    .build();
        } catch (IllegalStateException e) {
            System.out.println("Caught expected error: " + e.getMessage());
        }

        try {
            // Test Chicago pizza with no toppings
            new ChicagoPizza.Builder()
                    .extraCheese()
                    .build();
        } catch (IllegalStateException e) {
            System.out.println("Caught expected error: " + e.getMessage());
        }
    }
} 