# Item 11: Always Override hashCode When You Override equals

## Overview
When overriding the `equals()` method, you must also override the `hashCode()` method to maintain the general contract between these two methods. Failure to do so will result in objects breaking when used in hash-based collections like `HashMap`, `HashSet`, and `Hashtable`.

## The Contract
The `hashCode` method must follow these rules:
1. During an execution of an application, calling `hashCode()` multiple times on the same object must consistently return the same value.
2. If two objects are equal according to `equals()`, their `hashCode()` values must be the same.
3. If two objects are unequal according to `equals()`, their `hashCode()` values are not required to be different (but different values may improve performance).

## Common Mistakes

### 1. Not Overriding hashCode
```java
public class Point {
    private final int x;
    private final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Point)) return false;
        Point p = (Point) o;
        return p.x == x && p.y == y;
    }
    // Broken - no hashCode method!
}
```

### 2. Poor hashCode Implementation
```java
// Broken - too simple
public int hashCode() {
    return 42; // Every instance has the same hash code
}
```

## Best Practices

### 1. Use Objects.hash for Simple Cases
```java
public class Point {
    private final int x;
    private final int y;

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
```

### 2. Manual Implementation for Performance
```java
public class Point {
    private final int x;
    private final int y;

    @Override
    public int hashCode() {
        int result = Integer.hashCode(x);
        result = 31 * result + Integer.hashCode(y);
        return result;
    }
}
```

### 3. Lazy Initialization for Expensive hashCode
```java
public class ExpensiveObject {
    private final BigDecimal value;
    private int hashCode; // Cached hash code

    @Override
    public int hashCode() {
        int result = hashCode;
        if (result == 0) {
            result = value.hashCode();
            hashCode = result;
        }
        return result;
    }
}
```

## Guidelines for Good hashCode Methods

1. **Include Significant Fields**
   - Use all fields that are used in equals()
   - Exclude any fields that are not part of equals() comparison

2. **Order Matters**
   - For better hash distribution, multiply the result by an odd prime number (typically 31)
   - This helps in distributing hash codes more evenly

3. **Handle Special Cases**
   - For arrays, use Arrays.hashCode()
   - For nullable fields, use Objects.hashCode(field)
   - For floating-point fields, use Float.hashCode() and Double.hashCode()

## Common Patterns

### 1. Builder Pattern with hashCode
```java
public class Person {
    private final String firstName;
    private final String lastName;
    private final int age;
    private final String email;

    public static class Builder {
        // Builder implementation
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Person)) return false;
        Person person = (Person) o;
        return age == person.age &&
               Objects.equals(firstName, person.firstName) &&
               Objects.equals(lastName, person.lastName) &&
               Objects.equals(email, person.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, age, email);
    }
}
```

### 2. Inheritance and hashCode
```java
public class ColorPoint extends Point {
    private final Color color;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ColorPoint)) return false;
        ColorPoint cp = (ColorPoint) o;
        return super.equals(o) && cp.color == color;
    }

    @Override
    public int hashCode() {
        return 31 * super.hashCode() + color.hashCode();
    }
}
```

## Performance Considerations

1. **Caching Hash Codes**
   - Consider caching if the hash code is expensive to compute
   - Only cache if the object is immutable
   - Be careful with synchronization in mutable objects

2. **Hash Code Distribution**
   - Poor distribution leads to more collisions
   - More collisions mean slower hash table operations
   - Use prime numbers and bit manipulation for better distribution

## Testing hashCode Implementation

```java
@Test
public void testHashCodeContract() {
    Point p1 = new Point(1, 2);
    Point p2 = new Point(1, 2);
    Point p3 = new Point(2, 1);

    // Test consistency
    assertEquals(p1.hashCode(), p1.hashCode());

    // Test equals objects have same hash code
    assertEquals(p1.hashCode(), p2.hashCode());

    // Test unequal objects may have different hash codes
    assertNotEquals(p1.hashCode(), p3.hashCode());
}
```

## Common Pitfalls to Avoid

1. **Using Mutable Fields**
   - Hash codes should be consistent
   - Mutable fields can change hash codes
   - Consider using only immutable fields for hash code calculation

2. **Ignoring Null Fields**
   - Always handle null fields properly
   - Use Objects.hashCode(field) for null-safe handling

3. **Poor Performance**
   - Avoid expensive computations in hashCode()
   - Consider caching for immutable objects
   - Don't include unnecessary fields

## References
- Effective Java, 3rd Edition
- Java API Documentation
- Java Collections Framework