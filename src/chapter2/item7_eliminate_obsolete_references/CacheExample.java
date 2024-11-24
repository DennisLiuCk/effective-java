package chapter2.item7_eliminate_obsolete_references;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;

/**
 * This class demonstrates different caching strategies and their memory implications.
 * We'll simulate a simple image cache system where we store image data (represented as byte arrays).
 */
public class CacheExample {
    // Scenario 1: Unbounded Cache (Bad Practice)
    // This cache will keep growing without bounds as we add more items
    // Even if the images are no longer needed, they stay in memory
    private static final Map<String, byte[]> unboundedCache = new HashMap<>();
    
    // Scenario 2: WeakHashMap Cache (Good for memory-sensitive caching)
    // Entries in this cache can be garbage collected when there are no other references to the keys
    // Useful when you want automatic cleanup of unused entries
    private static final Map<String, byte[]> weakCache = new WeakHashMap<>();
    
    // Scenario 3: Bounded Cache with LRU eviction (Good for limiting memory usage)
    // This cache has a maximum size and removes least recently used entries when full
    private static final Map<String, byte[]> boundedCache = new LinkedHashMap<String, byte[]>(100, 0.75f, true) {
        @Override
        protected boolean removeEldestEntry(Map.Entry<String, byte[]> eldest) {
            return size() > 100; // Limit cache size to 100 entries
        }
    };

    // Simulate different real-world scenarios
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Cache Memory Management Demo ===\n");
        demonstrateUnboundedCache();
        demonstrateWeakCache();
        demonstrateBoundedCache();
    }

    /**
     * Scenario 1: Demonstrates how an unbounded cache can lead to memory leaks
     * Real-world example: Caching user profile images without cleanup strategy
     */
    private static void demonstrateUnboundedCache() throws InterruptedException {
        System.out.println("=== Scenario 1: Unbounded Cache (Memory Leak Risk) ===");
        System.out.println("Simulating an image cache that stores user profile pictures...");
        
        // Simulate storing profile pictures for many users
        for (int i = 0; i < 100; i++) {
            String userId = "user" + i;
            byte[] profilePic = new byte[1000000]; // 1MB profile picture
            unboundedCache.put(userId, profilePic);
            
            if (i % 25 == 0) {
                System.out.printf("Added %d user profile pictures%n", i + 1);
                printMemoryUsage("Current memory usage");
            }
        }
        
        System.out.println("\nProblem: Even if users delete their accounts, their profile pictures remain in cache!");
        System.out.println("Cache size before GC: " + unboundedCache.size() + " entries");
        printMemoryUsage("Memory before GC");

        // Try garbage collection
        System.gc();
        TimeUnit.SECONDS.sleep(1);

        System.out.println("\nAfter garbage collection:");
        System.out.println("Cache size after GC: " + unboundedCache.size() + " entries");
        System.out.println("Notice: GC couldn't free the memory because we hold strong references!");
        printMemoryUsage("Memory after GC");
    }

    /**
     * Scenario 2: Demonstrates WeakHashMap for automatic cache cleanup
     * Real-world example: Caching temporary session data that should be cleared
     * when no longer referenced
     */
    private static void demonstrateWeakCache() throws InterruptedException {
        System.out.println("\n=== Scenario 2: WeakHashMap Cache (Automatic Cleanup) ===");
        System.out.println("Simulating temporary session data cache...");
        
        // Create some temporary objects to hold references
        Object[] tempReferences = new Object[5];
        
        // Store session data with some references held
        for (int i = 0; i < 10; i++) {
            String sessionId = "session" + i;
            byte[] sessionData = new byte[1000000]; // 1MB session data
            weakCache.put(sessionId, sessionData);
            
            // Keep references to only first 5 sessions
            if (i < 5) {
                tempReferences[i] = sessionId;
            }
        }
        
        System.out.println("Initial cache state:");
        System.out.println("Cache size before GC: " + weakCache.size() + " entries");
        printMemoryUsage("Memory before GC");
        
        // Try garbage collection
        System.gc();
        TimeUnit.SECONDS.sleep(1);
        
        System.out.println("\nAfter garbage collection:");
        System.out.println("Cache size after GC: " + weakCache.size() + " entries");
        System.out.println("Notice: Only sessions with active references remain!");
        printMemoryUsage("Memory after GC");
    }

    /**
     * Scenario 3: Demonstrates bounded cache with LRU eviction
     * Real-world example: Caching most recently viewed product images in an e-commerce app
     */
    private static void demonstrateBoundedCache() throws InterruptedException {
        System.out.println("\n=== Scenario 3: Bounded Cache with LRU Eviction ===");
        System.out.println("Simulating product image cache in an e-commerce app...");
        
        // Simulate users viewing different products
        for (int i = 0; i < 150; i++) {
            String productId = "product" + i;
            byte[] productImage = new byte[1000000]; // 1MB product image
            boundedCache.put(productId, productImage);
            
            if (i % 50 == 0) {
                System.out.printf("Cached %d product images%n", i + 1);
                System.out.println("Current cache size: " + boundedCache.size());
                printMemoryUsage("Current memory usage");
            }
        }
        
        System.out.println("\nFinal cache state:");
        System.out.println("Cache size: " + boundedCache.size() + " entries");
        System.out.println("Notice: Only the 100 most recently accessed products are cached!");
        System.out.println("The oldest 50 entries were automatically removed.");
        printMemoryUsage("Final memory usage");

        // Try garbage collection
        System.gc();
        TimeUnit.SECONDS.sleep(1);
        
        System.out.println("\nAfter garbage collection:");
        System.out.println("Cache size after GC: " + boundedCache.size() + " entries");
        System.out.println("Notice: Size remains the same because this is a size-based eviction!");
        printMemoryUsage("Memory after GC");
    }

    private static void printMemoryUsage(String label) {
        Runtime runtime = Runtime.getRuntime();
        long usedMemory = runtime.totalMemory() - runtime.freeMemory();
        System.out.printf("%s: %.2f MB%n", label, usedMemory / (1024.0 * 1024.0));
    }
}
