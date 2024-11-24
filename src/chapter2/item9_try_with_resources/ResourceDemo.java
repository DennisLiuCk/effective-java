package chapter2.item9_try_with_resources;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Demonstrates different approaches to resource management and why
 * try-with-resources is superior to traditional try-finally.
 * 
 * Key limitations of traditional try-finally:
 * 1. Exception masking: If both try and finally blocks throw exceptions,
 *    the exception from finally masks the original exception
 * 2. Code verbosity: Requires nested try-catch blocks for multiple resources
 * 3. Error-prone resource ordering: Manual handling of resource closure order
 * 4. Easy to forget cleanup: Might forget finally block or null checks
 * 5. Thread safety issues: Potential race conditions during cleanup
 */
public class ResourceDemo {

    public static void main(String[] args) {
        demonstrateTraditionalApproach();
        demonstrateTryWithResources();
        demonstrateMultipleResources();
        demonstrateExceptionHandling();
    }

    /**
     * Demonstrates the traditional try-finally approach and its limitations.
     * 
     * Limitations shown here:
     * 1. Exception masking: If resource.doWork() throws an exception and
     *    resource.close() also throws an exception, only the close() exception
     *    is propagated, and the original exception is lost.
     * 2. Verbosity: Requires nested try-catch blocks
     * 3. Null checking: Must manually check if resource is null before closing
     */
    private static void demonstrateTraditionalApproach() {
        System.out.println("\n=== Traditional try-finally Approach ===");
        CustomResource resource = null;
        try {
            resource = new CustomResource("Traditional");
            resource.doWork();  // If this throws an exception...
        } catch (Exception e) {
            System.out.println("Error during work: " + e.getMessage());
        } finally {
            if (resource != null) {  // Must check for null
                try {
                    resource.close();  // If this also throws, it masks any previous exception
                } catch (Exception e) {
                    System.out.println("Error during close: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Demonstrates the modern try-with-resources approach.
     * 
     * Advantages shown here:
     * 1. Preserves original exceptions: Primary exception is propagated while
     *    close() exceptions are added as suppressed exceptions
     * 2. Automatic null handling: No need to check for null
     * 3. Guaranteed cleanup: Resource is always closed
     * 4. Cleaner code: No nested try-catch blocks needed
     */
    private static void demonstrateTryWithResources() {
        System.out.println("\n=== Modern try-with-resources Approach ===");
        try (CustomResource resource = new CustomResource("Modern")) {
            resource.doWork();  // If this throws an exception...
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            
            // We can still access any exceptions that occurred during close()
            Throwable[] suppressed = e.getSuppressed();
            if (suppressed.length > 0) {
                System.out.println("Suppressed exception: " + suppressed[0].getMessage());
            }
        }
    }

    /**
     * Demonstrates handling multiple resources with try-with-resources.
     * 
     * Advantages shown here:
     * 1. Automatic resource ordering: Resources are closed in reverse order
     * 2. Clean syntax: Multiple resources declared in one try statement
     * 3. All resources are guaranteed to be closed
     * 4. All exceptions are properly handled and preserved
     */
    private static void demonstrateMultipleResources() {
        System.out.println("\n=== Multiple Resources Example ===");
        // Compare this clean syntax with the traditional nested try-finally blocks
        try (CustomResource resource1 = new CustomResource("First");
             CustomResource resource2 = new CustomResource("Second");
             CustomResource resource3 = new CustomResource("Third")) {
            
            resource1.doWork();
            resource2.doWork();
            resource3.doWork();
            // Resources will be closed in reverse order: Third, Second, First
            
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            
            // All exceptions during close() are preserved as suppressed exceptions
            for (Throwable suppressed : e.getSuppressed()) {
                System.out.println("Suppressed: " + suppressed.getMessage());
            }
        }
    }

    /**
     * Demonstrates real-world exception handling with resources.
     * Shows how try-with-resources handles different types of exceptions
     * and provides access to suppressed exceptions.
     */
    private static void demonstrateExceptionHandling() {
        System.out.println("\n=== Exception Handling Example ===");
        
        // Example 1: File operations with automatic resource management
        try (BufferedReader reader = new BufferedReader(
                new FileReader("nonexistent.txt"))) {
            String line = reader.readLine();
            System.out.println("Read line: " + line);
        } catch (IOException e) {
            System.out.println("File error: " + e.getMessage());
        }

        // Example 2: Multiple exception types with suppression
        try (CustomResource resource = new CustomResource("ExceptionTest")) {
            resource.doWork();
        } catch (IllegalStateException e) {
            System.out.println("State error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("General error: " + e.getMessage());
            
            // Demonstrate access to suppressed exceptions
            for (Throwable suppressed : e.getSuppressed()) {
                System.out.println("Suppressed error: " + suppressed.getMessage());
            }
        }
    }

    /**
     * Example of a method that uses try-with-resources with a return statement.
     * The resource is properly closed even when returning early from the method.
     */
    public static List<String> readAllLines(String path) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            List<String> lines = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            return lines;  // Resource is closed even with early return
        }
    }

    /**
     * Example of database operations with try-with-resources.
     * Shows how multiple database resources are properly managed.
     */
    public static void databaseExample(Connection connection, String query) throws SQLException {
        // Resources are closed in reverse order: ResultSet, then PreparedStatement
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                // Process results
                String data = rs.getString(1);
                System.out.println("Data: " + data);
            }
        }
    }
}
