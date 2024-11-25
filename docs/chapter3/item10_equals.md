# Item 10: Obey the general contract when overriding equals

## Overview

The `equals` method implements an equivalence relation and must follow five key requirements:

1. **Reflexivity**: `x.equals(x)` must return `true`
2. **Symmetry**: `x.equals(y)` must return `true` if and only if `y.equals(x)` returns `true`
3. **Transitivity**: if `x.equals(y)` and `y.equals(z)`, then `x.equals(z)` must return `true`
4. **Consistency**: multiple invocations of `x.equals(y)` must consistently return `true` or `false`
5. **Non-nullity**: `x.equals(null)` must return `false`

## When to Override equals

Override equals when a class has a notion of logical equality different from object identity. This is generally the case for value classes like `Integer` or `String`.

Do not override equals when:
1. Each instance is inherently unique (e.g., `Thread`)
2. A "logical equality" test is not needed (e.g., `Pattern`)
3. A superclass has already overridden equals and the behavior is appropriate
4. The class is private or package-private and equals will never be called

## Recipe for a High-Quality equals Method

1. Use the `==` operator to check if the argument is a reference to this object
2. Use `instanceof` to check if the argument has the correct type
3. Cast the argument to the correct type
4. Check if significant fields are equal

Example:
```java
@Override
public boolean equals(Object obj) {
    // 1. Performance optimization - check for same object
    if (obj == this) {
        return true;
    }

    // 2. Check for correct type
    if (!(obj instanceof Point)) {
        return false;
    }

    // 3. Cast to correct type
    Point p = (Point) obj;

    // 4. Compare significant fields
    return x == p.x && y == p.y;
}
```

## Common Pitfalls

### 1. Breaking Symmetry

Incorrect example that breaks symmetry:
```java
// Broken - violates symmetry!
@Override
public boolean equals(Object obj) {
    if (obj == null || obj.getClass() != getClass())
        return false;
    Point p = (Point) obj;
    return p.x == x && p.y == y;
}
```

### 2. Breaking Transitivity with Inheritance

The problem often occurs when extending a class and adding a value component:

```java
public class ColorPoint extends Point {
    private final Color color;
    
    // Broken - violates transitivity!
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Point))
            return false;
        if (!(obj instanceof ColorPoint))
            return obj.equals(this);
        return super.equals(obj) && ((ColorPoint) obj).color == color;
    }
}
```

### 3. Using Wrong Parameter Type

```java
// Broken - wrong parameter type!
public boolean equals(Point p) {
    if (p == null)
        return false;
    return p.x == x && p.y == y;
}
```

## Solutions

### 1. Favor Composition Over Inheritance

Instead of extending `Point` to add color:

```java
public class ColorPoint {
    private final Point point;
    private final Color color;

    public Point asPoint() {
        return point;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ColorPoint))
            return false;
        ColorPoint cp = (ColorPoint) obj;
        return cp.point.equals(point) && cp.color.equals(color);
    }
}
```

### 2. Using getClass Instead of instanceof

When inheritance is required and strict equivalence is needed:

```java
@Override
public boolean equals(Object obj) {
    if (obj == null || obj.getClass() != getClass())
        return false;
    Point p = (Point) obj;
    return p.x == x && p.y == y;
}
```

## Best Practices

1. **Always override hashCode when overriding equals**
2. **Make equals methods symmetric, transitive, and consistent**
3. **Don't try to be too clever**
4. **Don't write an equals method that depends on unreliable resources**
5. **Consider using tools to help write equals methods**
   - IDE-generated equals methods
   - Libraries like Apache Commons Lang's `EqualsBuilder`
   - Lombok's `@EqualsAndHashCode`

## Field Comparison Guidelines

1. For primitive fields, use `==`
2. For object references, use `equals`
3. For `float`, use `Float.compare`
4. For `double`, use `Double.compare`
5. For arrays, use `Arrays.equals`

Example with different field types:
```java
public class ComplexClass {
    private int primitive;
    private String object;
    private float floatValue;
    private double doubleValue;
    private int[] array;

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof ComplexClass)) return false;
        ComplexClass other = (ComplexClass) obj;
        return primitive == other.primitive &&                    // primitive
               Objects.equals(object, other.object) &&           // object
               Float.compare(floatValue, other.floatValue) == 0 && // float
               Double.compare(doubleValue, other.doubleValue) == 0 && // double
               Arrays.equals(array, other.array);                // array
    }
}
```

## Performance Considerations

1. Compare fields most likely to differ first
2. Compare less expensive computations before more expensive ones
3. Cache expensive computations if they are used frequently

## Common Questions

### Q1: When should I use getClass vs instanceof?
- Use `instanceof` when subclasses can have a different notion of equality
- Use `getClass` when equality must be strictly based on class type

### Q2: Should equals check for null?
- The `instanceof` operator handles null automatically by returning false
- No need for an explicit null check if using `instanceof`

### Q3: How to handle inheritance with equals?
- Prefer composition over inheritance for value components
- If inheritance is required, carefully document the equals contract

### Q4: What about floating-point comparisons?
- Use `Float.compare` and `Double.compare` instead of `==`
- Consider whether exact comparison is what you want

## Summary

1. Override equals when a class has a notion of logical equality
2. Follow the equals contract: reflexivity, symmetry, transitivity, consistency
3. Always override hashCode when overriding equals
4. Don't try to be too clever
5. Consider using existing tools to generate equals methods
6. Test equals implementations thoroughly
