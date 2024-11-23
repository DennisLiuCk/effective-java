package chapter2.item1_static_factory_methods;

public class StaticFactoryMethodDemoTest {
    public static void main(String[] args) {
        testNamedFactoryMethods();
        testCachedInstances();
        testSubtypeReturns();
        testVaryingReturnTypes();
    }

    // 1. Demonstrating named factory methods
    private static void testNamedFactoryMethods() {
        System.out.println("\n1. Testing Named Factory Methods:");
        System.out.println("--------------------------------");

        // Different named methods for object creation
        StaticFactoryMethodDemo valueOfDemo = StaticFactoryMethodDemo.valueOf("Hello");
        StaticFactoryMethodDemo ofDemo = StaticFactoryMethodDemo.of("World");
        StaticFactoryMethodDemo newInstance = StaticFactoryMethodDemo.newInstance();

        System.out.println("valueOf result: " + valueOfDemo.getValue());
        System.out.println("of result: " + ofDemo.getValue());
        System.out.println("newInstance result: " + newInstance.getValue());
    }

    // 2. Demonstrating instance caching
    private static void testCachedInstances() {
        System.out.println("\n2. Testing Cached Instances:");
        System.out.println("---------------------------");

        // Testing empty() caching
        StaticFactoryMethodDemo empty1 = StaticFactoryMethodDemo.empty();
        StaticFactoryMethodDemo empty2 = StaticFactoryMethodDemo.empty();
        System.out.println("empty() instances are same object: " + (empty1 == empty2));

        // Testing singleton() caching
        StaticFactoryMethodDemo singleton1 = StaticFactoryMethodDemo.singleton();
        StaticFactoryMethodDemo singleton2 = StaticFactoryMethodDemo.singleton();
        System.out.println("singleton() instances are same object: " + (singleton1 == singleton2));

        // Testing newInstance() (should create new instances)
        StaticFactoryMethodDemo new1 = StaticFactoryMethodDemo.newInstance();
        StaticFactoryMethodDemo new2 = StaticFactoryMethodDemo.newInstance();
        System.out.println("newInstance() creates different objects: " + (new1 != new2));
    }

    // 3. Demonstrating subtype returns
    private static void testSubtypeReturns() {
        System.out.println("\n3. Testing Subtype Returns:");
        System.out.println("--------------------------");

        // Create different animal types
        StaticFactoryMethodDemo.Animal dog = StaticFactoryMethodDemo.createAnimal("dog");
        StaticFactoryMethodDemo.Animal cat = StaticFactoryMethodDemo.createAnimal("cat");

        // Demonstrate polymorphic behavior
        System.out.println("Dog says: " + dog.makeSound());
        System.out.println("Cat says: " + cat.makeSound());

        // Demonstrate error handling
        try {
            StaticFactoryMethodDemo.createAnimal("elephant");
        } catch (IllegalArgumentException e) {
            System.out.println("Expected error for unknown animal: " + e.getMessage());
        }
    }

    // 4. Demonstrating varying return types based on input
    private static void testVaryingReturnTypes() {
        System.out.println("\n4. Testing Varying Return Types:");
        System.out.println("------------------------------");

        // Test different input types
        StaticFactoryMethodDemo fromInt = StaticFactoryMethodDemo.from(42);
        StaticFactoryMethodDemo fromTrue = StaticFactoryMethodDemo.from(true);
        StaticFactoryMethodDemo fromFalse = StaticFactoryMethodDemo.from(false);
        StaticFactoryMethodDemo fromString = StaticFactoryMethodDemo.from("test");

        // Verify results
        System.out.println("From Integer (42): " + fromInt.getValue());
        System.out.println("From Boolean (true) returns singleton: " + 
            (fromTrue == StaticFactoryMethodDemo.singleton()));
        System.out.println("From Boolean (false) returns empty: " + 
            (fromFalse == StaticFactoryMethodDemo.empty()));
        System.out.println("From String: " + fromString.getValue());

        // Demonstrate null handling
        try {
            StaticFactoryMethodDemo.from(null);
        } catch (NullPointerException e) {
            System.out.println("Expected error for null input: " + e.getMessage());
        }
    }
} 