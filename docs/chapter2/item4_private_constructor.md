# Item 4: Enforce noninstantiability with a private constructor

## Overview
Sometimes you want to create a class that is just a grouping of static methods and static fields. These utility classes were not designed to be instantiated - attempting to do so is a logical error. However, in the absence of explicit constructors, the compiler provides a public, parameterless default constructor, making the class instantiable.

## The Problem with Default Constructor

```java
// Wrong way to create a utility class
public class WrongUtilityClass {
    // Compiler provides default constructor - BAD!
    
    public static int add(int a, int b) {
        return a + b;
    }
}

// Can be instantiated unnecessarily
WrongUtilityClass util = new WrongUtilityClass(); // Should not be possible!
```

Problems with this approach:
1. Allows unnecessary instantiation
2. Wastes memory
3. Can be misleading to other developers
4. Allows inheritance (which is inappropriate for utility classes)

## The Solution: Private Constructor

```java
// Proper way to create a utility class
public class StringUtils {
    // Private constructor prevents instantiation
    private StringUtils() {
        throw new AssertionError("No StringUtils instances for you!");
    }
    
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }
}
```

Key points:
1. Private constructor prevents instantiation
2. AssertionError prevents instantiation via reflection
3. Comment explains why the constructor is private
4. Class cannot be subclassed (because constructor is private)

## Common Use Cases

### 1. String Utilities
```java
public final class Strings {
    private Strings() {}
    
    public static String nullToEmpty(String s) {
        return s == null ? "" : s;
    }
    
    public static String emptyToNull(String s) {
        return s != null && s.isEmpty() ? null : s;
    }
}
```

### 2. Math Utilities
```java
public final class MathUtils {
    private MathUtils() {
        throw new AssertionError();
    }
    
    public static int gcd(int a, int b) {
        return b == 0 ? Math.abs(a) : gcd(b, a % b);
    }
    
    public static boolean isPrime(int n) {
        if (n < 2) return false;
        for (int i = 2; i <= Math.sqrt(n); i++) {
            if (n % i == 0) return false;
        }
        return true;
    }
}
```

### 3. Collection Utilities
```java
public final class CollectionUtils {
    private CollectionUtils() {}
    
    public static <T> List<T> nullToEmpty(List<T> list) {
        return list == null ? Collections.emptyList() : list;
    }
    
    public static boolean isNullOrEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }
}
```

## Real-World Examples

### 1. Java Standard Library
- `java.lang.Math`: Mathematical functions
- `java.util.Arrays`: Array utilities
- `java.util.Collections`: Collection utilities
- `java.lang.System`: System utilities

### 2. Apache Commons
```java
// From Apache Commons Lang
public class StringUtils {
    private StringUtils() {}  // Prevents instantiation
    
    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }
}
```

### 3. Google Guava
```java
// From Google Guava
public final class Preconditions {
    private Preconditions() {}  // Prevents instantiation
    
    public static <T> T checkNotNull(T reference) {
        if (reference == null) {
            throw new NullPointerException();
        }
        return reference;
    }
}
```

## Best Practices

1. **Always Document the Private Constructor**
```java
/**
 * Utility class - private constructor prevents instantiation.
 */
private UtilityClass() {
    throw new AssertionError();
}
```

2. **Make the Class Final**
```java
public final class UtilityClass {
    private UtilityClass() {}
    // ... static methods
}
```

3. **Consider Using Interfaces**
```java
public interface StringUtils {
    static boolean isEmpty(String s) {
        return s == null || s.length() == 0;
    }
}
```

4. **Group Related Functions**
- Keep utility methods focused on a single theme
- Split large utility classes into smaller, more focused ones
- Use meaningful class names that describe the utility's purpose

## Common Pitfalls

1. **Forgetting the Private Constructor**
- Allows instantiation
- Makes the class inheritable
- Wastes memory

2. **Not Making the Class Final**
- Allows inheritance
- Can lead to confusion about class purpose

3. **Missing Documentation**
- Other developers might not understand why constructor is private
- May lead to attempts to instantiate via reflection

4. **Too Many Responsibilities**
- Utility classes should have a single, clear purpose
- Avoid "god classes" with too many unrelated methods

## When to Use

✅ **Good Use Cases:**
- Pure functions with no state
- Commonly used algorithms
- Helper methods for specific types
- Extension methods for existing classes

❌ **Avoid When:**
- Methods need to maintain state
- Functionality belongs to a specific instance
- Better suited for instance methods
- Could use static imports instead

## Testing Considerations

1. **Static Methods are Easier to Test**
```java
@Test
public void testStringUtils() {
    assertTrue(StringUtils.isEmpty(null));
    assertTrue(StringUtils.isEmpty(""));
    assertFalse(StringUtils.isEmpty(" "));
}
```

2. **Verify Noninstantiability**
```java
@Test(expected = AssertionError.class)
public void testCannotInstantiate() {
    Constructor<StringUtils> constructor 
        = StringUtils.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    constructor.newInstance();
}
```

## Related Items
- Item 1: Consider static factory methods instead of constructors
- Item 3: Enforce the singleton property with a private constructor
- Item 17: Minimize mutability
- Item 22: Use interfaces only to define types
