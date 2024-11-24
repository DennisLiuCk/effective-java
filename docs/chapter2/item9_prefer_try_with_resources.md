# Item 9: Prefer try-with-resources to try-finally

## Key Points

1. **try-with-resources is cleaner and safer**
   - Automatically closes resources
   - Handles multiple resources elegantly
   - Preserves important exception information

2. **Resources must implement AutoCloseable**
   - Most Java standard library classes that need closing already implement it
   - Your own classes can implement it too

3. **Advantages over try-finally**
   - Shorter and clearer code
   - Better exception handling
   - Multiple resources handled elegantly
   - Resources closed in reverse order of acquisition

## Code Examples

### 1. Traditional try-finally (problematic)
```java
// Bad - try-finally is verbose and can mask exceptions
static String firstLineOfFile(String path) throws IOException {
    BufferedReader br = new BufferedReader(new FileReader(path));
    try {
        return br.readLine();
    } finally {
        br.close(); // This could throw an exception that masks the original one
    }
}
```

### 2. try-with-resources (preferred)
```java
// Good - concise and handles exceptions properly
static String firstLineOfFile(String path) throws IOException {
    try (BufferedReader br = new BufferedReader(new FileReader(path))) {
        return br.readLine();
    }
}
```

### 3. Multiple Resources
```java
// Good - handles multiple resources elegantly
static void copy(String src, String dst) throws IOException {
    try (InputStream in = new FileInputStream(src);
         OutputStream out = new FileOutputStream(dst)) {
        byte[] buf = new byte[1024];
        int n;
        while ((n = in.read(buf)) >= 0)
            out.write(buf, 0, n);
    }
}
```

## Common Resource Types

1. **File System Resources**
   - FileInputStream/FileOutputStream
   - FileReader/FileWriter
   - BufferedReader/BufferedWriter

2. **Network Resources**
   - Socket
   - ServerSocket
   - InputStream/OutputStream from network connections

3. **Database Resources**
   - Connection
   - Statement
   - ResultSet

4. **System Resources**
   - Process
   - ProcessBuilder
   - Scanner

## Best Practices

### 1. Always Use try-with-resources for:
- File operations
- Database connections
- Network connections
- Any resource that implements AutoCloseable

### 2. Combining with catch Clauses
```java
try (Resource resource = new Resource()) {
    // Use resource
} catch (Exception e) {
    // Handle exception
}
```

### 3. Custom Resource Classes
```java
public class MyResource implements AutoCloseable {
    @Override
    public void close() throws Exception {
        // Cleanup code here
    }
}
```

## Common Pitfalls

1. **Not Using try-with-resources When Available**
   - Makes code more complex
   - Can lead to resource leaks
   - Can mask important exceptions

2. **Mixing try-finally with Resources**
   - Unnecessary complexity
   - Harder to maintain
   - More prone to errors

3. **Manual Resource Management**
   - Forgetting to close resources
   - Incorrect order of closing
   - Not handling exceptions properly

## Exception Handling Behavior

1. **Primary Exception Preservation**
   - First exception is preserved
   - Subsequent exceptions are suppressed
   - Suppressed exceptions are accessible via getSuppressed()

2. **Multiple Resource Closing**
   - Resources closed in reverse order
   - Each close operation can throw an exception
   - All exceptions are properly handled

## Understanding Suppressed Exceptions

When using try-with-resources, it's important to understand how Java handles multiple exceptions that might occur during resource cleanup. Here's what you need to know about suppressed exceptions:

### What are Suppressed Exceptions?

Suppressed exceptions occur when:
1. The primary operation in the try block throws an exception
2. The automatic closing of resources also throws one or more exceptions

In traditional try-finally blocks, if both the try block and finally block throw exceptions, the exception from the finally block masks the original exception, making debugging harder. Try-with-resources solves this by:
- Preserving the primary exception from the try block
- Adding any exceptions from close() operations as "suppressed" exceptions

### Example of Suppressed Exceptions

```java
try (CustomResource resource = new CustomResource("Demo")) {
    resource.doWork();  // Throws Exception A
    // When exiting the try block, resource.close() automatically runs
    // and throws Exception B
} catch (Exception e) {
    System.out.println("Primary exception: " + e.getMessage());  // Shows Exception A
    
    // Access suppressed exceptions
    Throwable[] suppressed = e.getSuppressed();
    for (Throwable t : suppressed) {
        System.out.println("Suppressed: " + t.getMessage());  // Shows Exception B
    }
}
```

### Key Points About Suppressed Exceptions

1. **Exception Hierarchy**
   - The first exception thrown becomes the primary exception
   - Subsequent exceptions during resource cleanup become suppressed exceptions
   - All exceptions are preserved and accessible

2. **Accessing Suppressed Exceptions**
   - Use `getSuppressed()` to get an array of suppressed exceptions
   - The array is empty if no exceptions were suppressed
   - Suppressed exceptions maintain their full stack traces

3. **Multiple Resources**
   ```java
   try (ResourceOne r1 = new ResourceOne();
        ResourceTwo r2 = new ResourceTwo()) {
       // If this throws an exception
       doWork();
   } // If r1.close() and r2.close() also throw exceptions,
     // they become suppressed
   ```

4. **Best Practices**
   - Always check for suppressed exceptions when debugging
   - Log both primary and suppressed exceptions
   - Consider suppressed exceptions when implementing custom resources

### Real-World Impact

Understanding suppressed exceptions is crucial for:
- Proper error handling and debugging
- Implementing robust cleanup mechanisms
- Writing maintainable resource management code

Example with multiple resources and exception handling:

```java
public void processWithResources() throws Exception {
    try (DatabaseConnection db = new DatabaseConnection();
         FileWriter log = new FileWriter("audit.log")) {
        
        db.executeQuery();  // Might throw SQLException
        
    } catch (Exception e) {
        System.err.println("Primary error: " + e.getMessage());
        
        // Always check for suppressed exceptions
        for (Throwable suppressed : e.getSuppressed()) {
            System.err.println("Resource cleanup error: " + suppressed.getMessage());
        }
        
        throw e;  // Rethrow with suppressed exceptions intact
    }
}
```

This preservation of all exceptions makes debugging easier and provides a complete picture of what went wrong during both the operation and cleanup phases.

## Logging Exceptions with Log4j

When using Log4j, it's essential to properly log both primary and suppressed exceptions to maintain a complete error trail. Here's how to do it effectively:

```java
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ResourceManager {
    private static final Logger logger = LogManager.getLogger(ResourceManager.class);

    public void processData() {
        try (DatabaseConnection db = new DatabaseConnection();
             FileProcessor file = new FileProcessor("data.txt")) {
            
            db.executeQuery();
            file.process();
            
        } catch (Exception e) {
            // Log the primary exception with full stack trace
            logger.error("Error during data processing", e);
            
            // Log all suppressed exceptions
            Throwable[] suppressed = e.getSuppressed();
            if (suppressed.length > 0) {
                logger.error("Additional errors occurred during resource cleanup:");
                for (Throwable t : suppressed) {
                    logger.error("Suppressed exception:", t);
                }
            }
            
            // Optionally create a structured log entry
            Map<String, Object> errorContext = new HashMap<>();
            errorContext.put("operation", "processData");
            errorContext.put("primaryException", e.getMessage());
            errorContext.put("suppressedCount", suppressed.length);
            
            List<String> suppressedMessages = Arrays.stream(suppressed)
                .map(Throwable::getMessage)
                .collect(Collectors.toList());
            errorContext.put("suppressedExceptions", suppressedMessages);
            
            logger.error("Complete error context: {}", errorContext);
        }
    }
}
```

#### Log4j Configuration Example

```xml
<?xml version="1.0" encoding="UTF-8"?>
<Configuration status="WARN">
    <Appenders>
        <Console name="Console" target="SYSTEM_OUT">
            <PatternLayout pattern="%d{HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n%throwable"/>
        </Console>
        <RollingFile name="RollingFile" fileName="logs/app.log"
                     filePattern="logs/app-%d{MM-dd-yyyy}-%i.log.gz">
            <PatternLayout>
                <Pattern>%d %p %c{1.} [%t] %m%n%throwable</Pattern>
            </PatternLayout>
            <Policies>
                <TimeBasedTriggeringPolicy />
                <SizeBasedTriggeringPolicy size="10 MB"/>
            </Policies>
        </RollingFile>
    </Appenders>
    <Loggers>
        <Root level="error">
            <AppenderRef ref="Console" />
            <AppenderRef ref="RollingFile" />
        </Root>
    </Loggers>
</Configuration>
```

#### Example Output

```
2024-01-20 10:15:23.456 [main] ERROR ResourceManager - Error during data processing
java.sql.SQLException: Database connection failed
    at com.example.DatabaseConnection.executeQuery(DatabaseConnection.java:45)
    at com.example.ResourceManager.processData(ResourceManager.java:15)
    ... 28 more
2024-01-20 10:15:23.457 [main] ERROR ResourceManager - Additional errors occurred during resource cleanup:
2024-01-20 10:15:23.457 [main] ERROR ResourceManager - Suppressed exception:
java.io.IOException: Failed to close file processor
    at com.example.FileProcessor.close(FileProcessor.java:89)
    at com.example.ResourceManager.processData(ResourceManager.java:16)
    ... 28 more
2024-01-20 10:15:23.458 [main] ERROR ResourceManager - Complete error context: {
    "operation": "processData",
    "primaryException": "Database connection failed",
    "suppressedCount": 1,
    "suppressedExceptions": ["Failed to close file processor"]
}
```

#### Best Practices for Exception Logging

1. **Use Structured Logging**
   ```java
   // Instead of concatenating strings
   logger.error("Failed to process data: " + e.getMessage());
   
   // Use parameterized logging
   logger.error("Failed to process data: {}", e.getMessage());
   logger.error("Operation failed", e);  // Includes stack trace
   ```

2. **Create Utility Method for Reuse**
   ```java
   public static void logExceptionWithSuppressed(Logger logger, Throwable e, String message) {
       logger.error(message, e);
       
       Throwable[] suppressed = e.getSuppressed();
       if (suppressed.length > 0) {
           logger.error("Suppressed Exceptions:");
           for (Throwable t : suppressed) {
               logger.error("Suppressed:", t);
           }
       }
   }
   
   // Usage
   try (Resource r = new Resource()) {
       // ... operations
   } catch (Exception e) {
       logExceptionWithSuppressed(logger, e, "Operation failed");
   }
   ```

3. **Include Context Information**
   ```java
   public void processDataWithContext(String userId, String operation) {
       try (Resource r = new Resource()) {
           // ... operations
       } catch (Exception e) {
           logger.error("Operation failed. User: {}, Operation: {}", userId, operation, e);
           for (Throwable t : e.getSuppressed()) {
               logger.error("Cleanup failed for user {} during {}", userId, operation, t);
           }
       }
   }
   ```

This approach ensures that you have complete visibility into both the primary failure and any cleanup failures that occurred, making debugging and issue resolution much easier.

## Real-World Examples

### 1. File Processing
```java
public static List<String> readFileLines(String path) throws IOException {
    try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
        return reader.lines().collect(Collectors.toList());
    }
}
```

### 2. Database Operations
```java
public void executeQuery(String sql) throws SQLException {
    try (Connection conn = dataSource.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
            // Process results
        }
    }
}
```

## Summary

try-with-resources is a significant improvement over try-finally for resource management. It provides cleaner syntax, better exception handling, and automatic resource closure. Always prefer it when dealing with resources that need to be closed.
