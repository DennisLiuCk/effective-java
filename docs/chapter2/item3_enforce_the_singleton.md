# Item 3: Enforce the singleton property with a private constructor or an enum type

## Overview
A singleton is a class that is instantiated exactly once. Singletons typically represent either a stateless object such as a function or a system component that is intrinsically unique.

## Implementation Approaches

### 1. Public Static Final Field
```java
public class Elvis {
    public static final Elvis INSTANCE = new Elvis();
    private Elvis() { }
    
    public void leaveTheBuilding() { ... }
    
    // For serialization safety
    private Object readResolve() {
        return INSTANCE;
    }
}
```
**Advantages:**
- Simple and clear that the class is a singleton
- Performance advantage (JVM guarantees initialization when class is loaded)
- Serialization safety with readResolve

**Disadvantages:**
- Less flexible if requirements change
- Can't control instance creation timing

### 2. Static Factory Method
```java
public class Elvis {
    private static final Elvis INSTANCE = new Elvis();
    private Elvis() { }
    
    public static Elvis getInstance() {
        return INSTANCE;
    }
    
    public void leaveTheBuilding() { ... }
    
    private Object readResolve() {
        return INSTANCE;
    }
}
```
**Advantages:**
- API flexibility without changing implementation
- Can be modified to return different instances for different threads
- Can be converted to generic singleton factory
- Can be used as a supplier: `Supplier<Elvis> elvis = Elvis::getInstance`

**Disadvantages:**
- Slightly more verbose
- Not as clear that class is a singleton

### 3. Enum Singleton (Preferred Approach)
```java
public enum Elvis {
    INSTANCE;
    
    private final String[] favoriteMovies = { ... };
    
    public void leaveTheBuilding() { ... }
    public void singFavoriteMovie() { ... }
}
```
**Advantages:**
- Most concise approach
- Free serialization safety
- Guaranteed against multiple instantiation (even with reflection)
- Naturally thread-safe

**Disadvantages:**
- Less flexible (can't extend other classes)
- May feel unnatural for singleton

## Related Design Patterns

### 1. Factory Method Pattern
```java
public class SingletonFactory {
    private static Map<String, Singleton> instances = new HashMap<>();
    
    public static synchronized Singleton getInstance(String key) {
        return instances.computeIfAbsent(key, k -> new Singleton());
    }
}
```
- Used when you need multiple singletons based on keys
- Common in resource management systems

### 2. Registry Pattern
```java
public class ServiceRegistry {
    private static final ServiceRegistry INSTANCE = new ServiceRegistry();
    private final Map<Class<?>, Object> services = new ConcurrentHashMap<>();
    
    public static ServiceRegistry getInstance() {
        return INSTANCE;
    }
    
    public <T> void register(Class<T> serviceType, T service) {
        services.put(serviceType, service);
    }
    
    @SuppressWarnings("unchecked")
    public <T> T getService(Class<T> serviceType) {
        return (T) services.get(serviceType);
    }
}
```
- Combines singleton with service locator pattern
- Used in dependency injection frameworks

### 3. Monostate Pattern
```java
public class Monostate {
    private static String state;
    
    public String getState() { return state; }
    public void setState(String s) { state = s; }
}
```
- Alternative to singleton where all instances share state
- Less obvious but more flexible than singleton

## Real-World Examples

### 1. Java Standard Library
- `java.lang.Runtime`: Singleton representing the JVM runtime
- `java.awt.Desktop`: Singleton for desktop integration
- `java.lang.System`: Utility class with static methods (pseudo-singleton)

### 2. Spring Framework
```java
@Configuration
public class AppConfig {
    @Bean
    @Singleton
    public UserService userService() {
        return new UserService();
    }
}
```
- Uses singleton scope as default for beans
- Implements registry pattern for bean management

### 3. Database Connection Pools
```java
public class ConnectionPool {
    private static volatile ConnectionPool instance;
    private final List<Connection> connections;
    private static final int MAX_CONNECTIONS = 10;
    
    private ConnectionPool() {
        connections = new ArrayList<>(MAX_CONNECTIONS);
        // Initialize connection pool
    }
    
    public static ConnectionPool getInstance() {
        if (instance == null) {
            synchronized (ConnectionPool.class) {
                if (instance == null) {
                    instance = new ConnectionPool();
                }
            }
        }
        return instance;
    }
    
    public Connection getConnection() {
        // Return available connection
    }
    
    public void releaseConnection(Connection conn) {
        // Return connection to pool
    }
}
```
- Common use of singleton in database connection management
- Demonstrates thread-safe lazy initialization (double-checked locking)
- Controls access to limited resources

### 4. Logging Frameworks
```java
// Log4j example
private static final Logger logger = LogManager.getLogger(MyClass.class);
```
- Logger instances are typically managed as singletons per class
- Ensures unified logging behavior

## Best Practices and Considerations

1. **Thread Safety**
   - Enum singletons are inherently thread-safe
   - Other approaches need careful synchronization
   - Consider using volatile for double-checked locking

2. **Serialization**
   - Implement readResolve() for non-enum singletons
   - Document serialization behavior
   - Test serialization scenarios

3. **Testing**
   - Singletons can make testing difficult
   - Consider dependency injection for better testability
   - Use interfaces to abstract singleton behavior

4. **Memory Management**
   - Singletons live for entire application lifecycle
   - Be cautious with heavy resources
   - Consider lazy initialization when appropriate

## Common Pitfalls

1. **Reflection Attacks**
   - Non-enum singletons are vulnerable
   - Can be broken using reflection
   - Enum singletons are safe

2. **Serialization Issues**
   - Missing readResolve can break singleton
   - Serialization creates new instances
   - Enum automatically handles this

3. **Inheritance Problems**
   - Singletons are difficult to inherit from
   - Consider composition over inheritance
   - Use interfaces for flexibility

## When to Use Singleton

✅ **Good Use Cases:**
- System components that must be unique
- Resource managers
- Configuration managers
- Heavy resource handlers

❌ **Avoid When:**
- State can vary by context
- Multiple instances might be needed later
- Testing isolation is critical
- Component has no state
