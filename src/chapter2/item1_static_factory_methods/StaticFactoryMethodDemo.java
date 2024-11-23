package chapter2.item1_static_factory_methods;

/**
 * Item 1: Consider static factory methods instead of constructors
 * 
 * This class demonstrates the advantages of static factory methods:
 * 1. They have names that describe the returned object
 * 2. They can cache instances
 * 3. They can return subtypes
 * 4. Return type can vary based on input parameters
 */
public class StaticFactoryMethodDemo {
    private final String value;
    
    // Private constructor
    private StaticFactoryMethodDemo(String value) {
        this.value = value;
    }
    
    // 1. They have names that describe the returned object
    
    // 1.1 Static factory method with a descriptive name : valueOf
    public static StaticFactoryMethodDemo valueOf(String value) {
        return new StaticFactoryMethodDemo(value);
    }

    // 1.2 Static factory method with a descriptive name : of
    public static StaticFactoryMethodDemo of(String value) {
        return new StaticFactoryMethodDemo(value);
    }


    
    // 2. They can cache instances

    // 2.1 Static factory method that demonstrates caching: empty
    private static final StaticFactoryMethodDemo EMPTY = 
    new StaticFactoryMethodDemo("");
    
    public static StaticFactoryMethodDemo empty() {
        return EMPTY;
    }

    // 2.2 Static factory method that demonstrates caching: singleton
    private static final StaticFactoryMethodDemo SINGLETON = 
    new StaticFactoryMethodDemo("singleton");
    
    public static StaticFactoryMethodDemo singleton() {
        return SINGLETON;
    }

    // 2.3 Static factory method that demonstrates caching: newInstance
    public static StaticFactoryMethodDemo newInstance() {
        return new StaticFactoryMethodDemo("newInstance");
    }

    
    // 3. They can return subtypes
    public interface Animal {
        String makeSound();
    }

    private static class Dog implements Animal {
        @Override
        public String makeSound() {
            return "Woof!";
        }
    }

    private static class Cat implements Animal {
        @Override
        public String makeSound() {
            return "Meow!";
        }
    }

    // 3.1 Static factory method that returns different subtypes
    public static Animal createAnimal(String type) {
        switch (type.toLowerCase()) {
            case "dog":
                return new Dog();
            case "cat":
                return new Cat();
            default:
                throw new IllegalArgumentException("Unknown animal type: " + type);
        }
    }

    // 4. Return type can vary based on input parameters
    public static StaticFactoryMethodDemo from(Object obj) {
        if (obj instanceof Integer) {
            return new StaticFactoryMethodDemo(String.valueOf(obj));
        } else if (obj instanceof Boolean) {
            return ((Boolean) obj) ? singleton() : empty();
        } else {
            return valueOf(obj.toString());
        }
    }

    
    public String getValue() {
        return value;
    }
} 