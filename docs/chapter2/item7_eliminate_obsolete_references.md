# Item 7: Eliminate Obsolete Object References

## Key Points

1. **Memory leaks in Java are subtle**
2. **Nulling references should be the exception, not the norm**
3. **Common sources of memory leaks:**
   - Obsolete references in self-managed memory structures
   - Caches
   - Listeners and callbacks

## Memory Leak Examples

### 1. Self-Managed Memory Structure
```java
public class Stack {
    private Object[] elements;
    private int size = 0;

    // Bad - memory leak
    public Object pop() {
        if (size == 0)
            throw new EmptyStackException();
        return elements[--size]; // Obsolete reference not cleared
    }

    // Good - clears obsolete reference
    public Object popFixed() {
        if (size == 0)
            throw new EmptyStackException();
        Object result = elements[--size];
        elements[size] = null; // Clear obsolete reference
        return result;
    }
}
```

### 2. Cache Memory Leaks
```java
// Bad - unbounded cache growth
private static final Map<String, BigInteger> cache = 
    new HashMap<>();

// Good - using WeakHashMap
private static final Map<String, BigInteger> cache = 
    new WeakHashMap<>();

// Also good - using time-based eviction
private static final Cache<String, BigInteger> cache = 
    CacheBuilder.newBuilder()
        .expireAfterWrite(10, TimeUnit.MINUTES)
        .maximumSize(1000)
        .build();
```

## Common Memory Leak Scenarios

1. **Self-Managed Memory Structures**
   - Collections
   - Arrays
   - Custom data structures

2. **Caches**
   - Unbounded caches
   - Forgotten cache entries
   - Missing eviction policies

3. **Listeners and Callbacks**
   - Unregistered listeners
   - Long-lived objects holding references to short-lived ones

4. **Class Loaders**
   - Memory leaks in web applications
   - Static fields holding references

## Best Practices

### 1. Clear Object References When:
- Managing your own memory
- Working with object pools
- Maintaining caches
- Working with listeners/callbacks

### 2. Use Proper Data Structures:
- WeakHashMap for caches
- Soft references for memory-sensitive caches
- LinkedHashMap with removeEldestEntry for size-bounded caches

### 3. Implement Cleanup Methods:
- close() methods in Closeable resources
- removeListener() methods for event listeners
- clear() methods for collections

## Tools for Memory Leak Detection

1. **Heap Profilers**
   - Java VisualVM
   - JProfiler
   - YourKit

2. **Debugging Tools**
   - jmap
   - jhat
   - Memory Analyzer (MAT)

## Common Patterns

### 1. Try-with-Resources
```java
// Good - automatically closes resources
try (FileInputStream fis = new FileInputStream("file.txt")) {
    // Use the resource
}
```

### 2. WeakReference Usage
```java
WeakReference<BigObject> weakRef = new WeakReference<>(new BigObject());
BigObject obj = weakRef.get(); // null if garbage collected
```

### 3. PhantomReference for Cleanup
```java
ReferenceQueue<Object> queue = new ReferenceQueue<>();
PhantomReference<Object> ref = new PhantomReference<>(object, queue);
```

## When to Worry About Memory Leaks

1. **Long-running applications**
2. **Resource-constrained environments**
3. **Applications with large object graphs**
4. **Applications using native resources**

## Common Misconceptions

1. **"Java handles all memory management"**
   - True for simple object references
   - Not always true for complex data structures

2. **"Nulling references is always good"**
   - Usually unnecessary
   - Only needed in specific cases

3. **"Memory leaks are obvious"**
   - Often subtle and hard to detect
   - May only appear under specific conditions

## Summary

While Java's garbage collector is excellent at managing memory, certain programming patterns can prevent proper garbage collection. Understanding and eliminating obsolete object references is crucial for preventing memory leaks, especially in long-running applications. Focus on proper resource management, appropriate data structure usage, and cleanup implementation.
