package chapter2.item8_avoid_finalizers_and_cleaners;

/**
 * Item 8: Avoid finalizers and cleaners
 *
 * Key points:
 * 1. Finalizers are unpredictable, dangerous, and unnecessary
 *    - No guarantee when they'll run
 *    - May not run at all
 *    - Severely degrade performance
 *    - Can cause security issues
 *
 * 2. Cleaners are better than finalizers but still problematic
 *    - No guaranteed timing of cleanup
 *    - No guarantee cleanup will happen
 *    - Complex to use correctly
 *
 * 3. To properly cleanup resources:
 *    - Implement AutoCloseable
 *    - Use try-with-resources
 *    - Consider using cleaners only as a safety net
 *
 * 4. Common resource types needing explicit cleanup:
 *    - Files
 *    - Streams
 *    - Database connections
 *    - Native peer objects
 *
 * Example of proper resource management vs problematic finalizer usage.
 */
public class FileResourceDemo {
    
    public static void main(String[] args) {
        demonstrateProperResourceManagement();
        System.out.println("\n" + "=".repeat(50) + "\n");
        
        demonstrateFinalizerProblem();
        System.out.println("\n" + "=".repeat(50) + "\n");
        
        demonstrateResourceLeak();
    }
    
    /**
     * Proper way: Use try-with-resources for automatic cleanup
     * Benefits:
     * - Guaranteed cleanup
     * - Immediate resource release
     * - Exception safety
     */
    private static void demonstrateProperResourceManagement() {
        System.out.println("Demonstration 1: Proper Resource Management");
        System.out.println("Using try-with-resources ensures immediate cleanup\n");
        
        try (FileResource file = new FileResource("important.txt", 1)) {
            file.writeData();
            // Resource automatically closed here, even if an exception occurs
        }
        // By this point, the resource is guaranteed to be closed
    }
    
    /**
     * Bad practice: Relying on finalizers
     * Problems:
     * - No guarantee when or if cleanup will happen
     * - Can cause resource exhaustion
     * - Poor performance
     */
    private static void demonstrateFinalizerProblem() {
        System.out.println("Demonstration 2: Finalizer Problems");
        System.out.println("Creating resources without proper cleanup (DON'T DO THIS!)\n");
        
        // Create resources without closing them
        new FileResource("temp1.txt", 1);
        new FileResource("temp2.txt", 1);
        
        System.out.println("\nTrying to trigger garbage collection...");
        System.out.println("Note: Finalizers may or may not run, and timing is unpredictable!");
        
        // Try to trigger garbage collection (no guarantee this will work)
        System.gc();
        try {
            Thread.sleep(1000); // Wait a bit to see if finalizers run
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Common mistake: Resource leak
     * Problems:
     * - Memory leaks
     * - File handle exhaustion
     * - System resource depletion
     */
    private static void demonstrateResourceLeak() {
        System.out.println("Demonstration 3: Resource Leak");
        System.out.println("Creating multiple resources in a loop without cleanup\n");
        
        // Simulate a common programming mistake: forgetting to close resources in a loop
        for (int i = 0; i < 10; i++) {
            FileResource file = new FileResource("leak" + i + ".txt", 1);
            file.writeData();
            // Resource not closed! In a real program, this could:
            // 1. Exhaust system resources
            // 2. Cause performance problems
            // 3. Lead to resource unavailability
        }
        
        System.out.println("\nResources were never closed properly!");
        System.out.println("In a real application, this could lead to:");
        System.out.println("- Memory leaks");
        System.out.println("- File handle exhaustion");
        System.out.println("- Other resource constraints");
    }
}
