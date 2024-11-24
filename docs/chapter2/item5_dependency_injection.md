# Item 5: Prefer Dependency Injection to Hardwiring Resources

## Key Points

1. **Don't use singleton or static utility classes to implement classes that depend on underlying resources**
2. **Don't have a class create its own dependent resources directly**
3. **Pass resources into the constructor (dependency injection)**
4. **Dependency injection improves flexibility, reusability, and testability**

## Example: Spell Checker Implementation

Consider a spell checker that depends on a dictionary. Here are three approaches, from worst to best:

### 1. Static Utility Class (Bad)
```java
// Static utility class - inappropriate for classes that depend on resources
public class SpellChecker {
    private static final Dictionary dictionary = new EnglishDictionary();
    
    private SpellChecker() {} // Noninstantiable
    
    public static boolean isValid(String word) { ... }
    public static List<String> suggestions(String typo) { ... }
}
```

### 2. Singleton Pattern (Bad)
```java
// Singleton - inappropriate for classes that depend on resources
public class SpellChecker {
    private final Dictionary dictionary = new EnglishDictionary();
    
    private static final SpellChecker INSTANCE = new SpellChecker();
    private SpellChecker() {}
    public static SpellChecker getInstance() { return INSTANCE; }
    
    public boolean isValid(String word) { ... }
    public List<String> suggestions(String typo) { ... }
}
```

### 3. Dependency Injection (Good)
```java
// Dependency injection provides flexibility and testability
public class SpellChecker {
    private final Dictionary dictionary;
    
    public SpellChecker(Dictionary dictionary) {
        this.dictionary = Objects.requireNonNull(dictionary);
    }
    
    public boolean isValid(String word) { ... }
    public List<String> suggestions(String typo) { ... }
}
```

## Benefits of Dependency Injection

1. **Flexibility**: The class works with any implementation of the dependency interface
2. **Testability**: Easy to test with mock implementations
3. **Reusability**: The class can be used in different contexts with different dependencies
4. **Clear Dependencies**: The class's dependencies are explicit in its constructor

## Common Forms of Dependency Injection

1. **Constructor Injection**: Pass dependency through constructor (as shown in example)
2. **Setter Injection**: Set dependency through setter method
3. **Interface Injection**: Dependency provides an injector method to inject itself
4. **Factory Injection**: Pass a factory (e.g., Supplier) that creates the dependency

### Factory-Based Dependency Injection Example
```java
// Using Supplier as a factory for dependency injection
public class SupplierSpellChecker {
    private final Dictionary dictionary;
    
    public SupplierSpellChecker(Supplier<? extends Dictionary> dictionaryFactory) {
        this.dictionary = Objects.requireNonNull(dictionaryFactory.get());
    }
    // ... rest of implementation
}

// Usage:
Supplier<Dictionary> factory = EnglishDictionary::new;
SupplierSpellChecker checker = new SupplierSpellChecker(factory);
```

This pattern is useful when:
- The dependency is expensive to create
- You want to delay creation until first use
- You need different instances for different use cases
- You want to abstract away the creation logic

## When to Use Dependency Injection

Use dependency injection when a class depends on one or more underlying resources that:
- Affect the behavior of the class
- May have multiple implementations
- May need to be changed or mocked for testing
- Are used by multiple instances of the class

## Real-World Applications

Dependency injection is widely used in:
- Spring Framework
- Android Development
- Unit Testing with Mockito
- Enterprise Applications

## Summary

Dependency injection is a powerful technique that promotes loose coupling, making your code more flexible, reusable, and testable. While it can increase code verbosity and complexity, the benefits usually outweigh these drawbacks, especially in larger systems.
