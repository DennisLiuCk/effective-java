# Item 6: Avoid Creating Unnecessary Objects

## Key Points

1. **Reuse immutable objects instead of creating new ones**
2. **Prefer primitive types to boxed primitives**
3. **Watch out for unintentional object creation in loops**
4. **Consider object pooling for extremely heavyweight objects**

## Examples of Unnecessary Object Creation

### 1. String Creation (Bad vs Good)
```java
// Bad - creates a new String instance each time
String s = new String("bikini");  // DON'T DO THIS!

// Good - reuses the String literal from the string pool
String s = "bikini";              // DO THIS INSTEAD
```

### 2. Boolean Constructor (Bad vs Good)
```java
// Bad - creates unnecessary Boolean objects
Boolean b = new Boolean("true");  // DON'T DO THIS!

// Good - uses static factory method
Boolean b = Boolean.valueOf("true");  // DO THIS INSTEAD
```

### 3. Pattern Matching in Loops
```java
// Bad - compiles Pattern every time
static boolean isRomanNumeral(String s) {
    return s.matches("^(?=.)M*(C[MD]|D?C{0,3})"
            + "(X[CL]|L?X{0,3})(I[XV]|V?I{0,3})$");
}

// Good - compiles Pattern once and reuses
private static final Pattern ROMAN = Pattern.compile(
    "^(?=.)M*(C[MD]|D?C{0,3})(X[CL]|L?X{0,3})(I[XV]|V?I{0,3})$");

static boolean isRomanNumeral(String s) {
    return ROMAN.matcher(s).matches();
}
```

## Performance Impact

### Autoboxing
```java
// Bad - creates ~2^31 unnecessary Long instances
Long sum = 0L;
for (long i = 0; i < Integer.MAX_VALUE; i++) {
    sum += i;  // Autoboxing happens here!
}

// Good - uses primitive long
long sum = 0L;
for (long i = 0; i < Integer.MAX_VALUE; i++) {
    sum += i;
}
```

## When to Create New Objects

1. **When working with mutable objects**
2. **When object represents distinct entities**
3. **When object creation is cheap**
4. **When object lifecycle is short**

## Object Pooling

- **Generally avoid** object pooling unless objects are extremely expensive to create
- Examples where pooling makes sense:
  - Database connections
  - Thread pools
  - Heavy-weight graphics objects

## Common Misconceptions

1. **Small Object Creation is NOT Expensive**
   - Modern JVMs are highly optimized for creating and garbage collecting small objects
   - Premature optimization through object pooling can make code more complex and less readable

2. **Immutable Objects are Thread-Safe**
   - Creating unnecessary defensive copies of immutable objects wastes resources
   - Immutable objects can be safely shared between threads

3. **Lazy Initialization**
   - Don't use lazy initialization unless necessary
   - The complexity and synchronization overhead often outweigh the benefits

## Best Practices

1. **Prefer Static Factory Methods**
   - They can cache and reuse objects
   - They provide better naming and documentation
   - They can return subtype objects

2. **Use StringBuilder for String Concatenation in Loops**
   - String concatenation creates new String objects
   - StringBuilder reuses the same buffer

3. **Cache Expensive Objects**
   - Store computation results that are expensive to create
   - Use when the same computation is needed multiple times

4. **Consider Using Primitive Types**
   - They're more efficient than boxed primitives
   - Use boxed primitives only when necessary (e.g., in collections)

## Summary

Creating unnecessary objects can impact both performance and memory usage. While modern JVMs are good at handling small object creation and garbage collection, being mindful of object creation can lead to more efficient and cleaner code. Focus on the most impactful cases like avoiding autoboxing in loops and caching expensive computations.
