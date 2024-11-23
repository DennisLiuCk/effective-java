# Item 2: Consider a Builder When Faced with Many Constructor Parameters

## Overview
The Builder pattern is particularly useful when dealing with classes that require many parameters, especially when some are optional. It provides a clear, flexible alternative to telescoping constructors and JavaBeans patterns.

## Advantages

1. **Clearer Code**
   - More readable than telescoping constructors
   - More maintainable than JavaBeans pattern
   - Self-documenting parameter names

2. **Immutability**
   - Objects can be made immutable
   - All parameter validation happens before object creation
   - No inconsistent state between calls

3. **Flexibility**
   - Optional parameters have default values
   - Can add parameters without breaking existing code
   - Can enforce invariants across multiple parameters

## Basic Implementation Example

```java
// Instead of telescoping constructors:
public class NutritionFacts {
    private final int servingSize;
    private final int servings;
    private final int calories;
    private final int fat;
    
    public NutritionFacts(int servingSize, int servings) {
        this(servingSize, servings, 0);
    }
    
    public NutritionFacts(int servingSize, int servings, int calories) {
        this(servingSize, servings, calories, 0);
    }
    // ... and so on
}

// Use Builder pattern instead:
NutritionFacts cocaCola = new NutritionFacts.Builder(240, 8)
    .calories(100)
    .sodium(35)
    .carbohydrate(27)
    .build();
```

## Advanced Usage: Hierarchical Builders

The Builder pattern can be used with class hierarchies using recursive generics:

```java
public abstract class Pizza {
    abstract static class Builder<T extends Builder<T>> {
        // Recursive generic type for method chaining
        abstract Pizza build();
        protected abstract T self();
    }
}

public class NyPizza extends Pizza {
    public static class Builder extends Pizza.Builder<Builder> {
        @Override protected Builder self() { return this; }
        @Override Pizza build() { return new NyPizza(this); }
    }
}
```

## Real-World Examples

### 1. StringBuilder (Java Core)
```java
StringBuilder sb = new StringBuilder()
    .append("Hello")
    .append(" ")
    .append("World")
    .append("!");
```

### 2. Retrofit (Square)
```java
Retrofit retrofit = new Retrofit.Builder()
    .baseUrl("https://api.example.com")
    .addConverterFactory(GsonConverterFactory.create())
    .client(okHttpClient)
    .build();
```

### 3. Spring Framework
```java
// UriComponents.Builder
UriComponents uriComponents = UriComponentsBuilder.newInstance()
    .scheme("http")
    .host("example.com")
    .path("/hotels/{hotel}")
    .queryParam("q", "{q}")
    .encode()
    .build();
```

### 4. Apache Commons
```java
// HashCodeBuilder
int hashCode = new HashCodeBuilder(17, 37)
    .append(name)
    .append(age)
    .append(address)
    .toHashCode();
```

## Related Design Patterns

### 1. Fluent Interface Pattern
- **Relationship**: Builder often implements fluent interface
```java
public class EmailBuilder {
    private Email email = new Email();
    
    public EmailBuilder from(String address) {
        email.setFrom(address);
        return this;
    }
    
    public EmailBuilder to(String address) {
        email.getToAddresses().add(address);
        return this;
    }
    
    public Email build() {
        return email;
    }
}
```

### 2. Abstract Factory Pattern
- **Combination Example**:
```java
public interface UIComponentBuilder {
    UIComponentBuilder addButton();
    UIComponentBuilder addLabel();
    UIComponent build();
    
    static UIComponentBuilder getBuilder(String platform) {
        return switch (platform) {
            case "web" -> new WebUIBuilder();
            case "mobile" -> new MobileUIBuilder();
            default -> throw new IllegalArgumentException();
        };
    }
}
```

### 3. Prototype Pattern
- **Integration Example**:
```java
public class TemplateBuilder {
    private final Template prototype;
    
    public TemplateBuilder(Template prototype) {
        this.prototype = prototype.clone();
    }
    
    public TemplateBuilder withTitle(String title) {
        prototype.setTitle(title);
        return this;
    }
    
    public Template build() {
        return prototype.clone();
    }
}
```

## Best Practices

1. **Parameter Validation**
```java
public Builder calories(int val) {
    if (val < 0) {
        throw new IllegalArgumentException("Calories cannot be negative");
    }
    calories = val;
    return this;
}
```

2. **Default Values**
```java
public static class Builder {
    private int servingSize = 1;  // Default value
    private boolean spicy = false; // Default value
}
```

3. **Builder Reuse**
```java
Pizza.Builder builder = new Pizza.Builder();
Pizza plain = builder.build();
Pizza spicy = builder.addTopping(PEPPER).build();
```

## Common Use Cases

1. **Configuration Objects**
```java
Configuration config = new Configuration.Builder()
    .setTimeout(1000)
    .setRetryCount(3)
    .setDebugMode(true)
    .build();
```

2. **Complex Object Creation**
```java
Report report = new Report.Builder()
    .setTitle("Monthly Summary")
    .addSection("Revenue")
    .addSection("Expenses")
    .addGraph(revenueData)
    .setFooter("Confidential")
    .build();
```

3. **Test Data Builders**
```java
User testUser = new UserBuilder()
    .withDefaultValues()
    .withName("John")
    .withAge(30)
    .build();
```

## Performance Considerations

1. **Creation Cost**
   - Builder objects add overhead
   - Consider static factory for simple cases
   - Worth it for complex objects

2. **Memory Impact**
   - Builder instance per object
   - Consider reusing builders
   - Use static builders when possible

## Framework Integration

### 1. Lombok
```java
@Builder
public class User {
    private final String name;
    private final int age;
    private final String email;
}
```

### 2. AutoValue (Google)
```java
@AutoValue
abstract class Animal {
    abstract String name();
    abstract int numberOfLegs();
    
    static Builder builder() {
        return new AutoValue_Animal.Builder();
    }
    
    @AutoValue.Builder
    abstract static class Builder {
        abstract Builder setName(String value);
        abstract Builder setNumberOfLegs(int value);
        abstract Animal build();
    }
}
```

## Common Pitfalls

1. **Mutable Builders**
   - Be careful with builder reuse
   - Consider making builders immutable
   - Document thread safety concerns

2. **Parameter Validation**
   - Validate early in setter methods
   - Cross-parameter validation in build()
   - Clear error messages

3. **Inheritance Issues**
   - Complex with deep hierarchies
   - Consider composition alternatives
   - Document inheritance requirements

## Related Items
- Item 1: Consider static factory methods instead of constructors
- Item 3: Enforce the singleton property with a private constructor or an enum type
- Item 17: Minimize mutability
- Item 50: Make defensive copies when needed 