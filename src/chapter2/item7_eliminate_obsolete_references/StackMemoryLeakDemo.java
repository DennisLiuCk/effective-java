package chapter2.item7_eliminate_obsolete_references;

import java.util.Arrays;

public class StackMemoryLeakDemo {
    public static void main(String[] args) {
        demonstrateMemoryLeak();
        demonstrateFixedImplementation();
    }

    private static void demonstrateMemoryLeak() {
        System.out.println("Demonstrating memory leak with original pop():");
        Stack stack = new Stack();
        
        // Create some large objects and push them onto the stack
        for (int i = 0; i < 10; i++) {
            stack.push(new byte[1000000]); // Push 1MB byte arrays
        }
        
        System.out.println("After pushing 10 large objects:");
        printStackInfo(stack);
        
        // Pop all items using the leaky implementation
        for (int i = 0; i < 10; i++) {
            stack.pop();
        }
        
        System.out.println("\nAfter popping all items (with memory leak):");
        printStackInfo(stack);
        System.out.println("Note: References to large objects still exist in the array!");
    }

    private static void demonstrateFixedImplementation() {
        System.out.println("\nDemonstrating fixed implementation with popFixed():");
        Stack stack = new Stack();
        
        // Create some large objects and push them onto the stack
        for (int i = 0; i < 10; i++) {
            stack.push(new byte[1000000]); // Push 1MB byte arrays
        }
        
        System.out.println("After pushing 10 large objects:");
        printStackInfo(stack);
        
        // Pop all items using the fixed implementation
        for (int i = 0; i < 10; i++) {
            stack.popFixed();
        }
        
        System.out.println("\nAfter popping all items (with fixed implementation):");
        printStackInfo(stack);
        System.out.println("Note: All references have been cleared!");
    }

    private static void printStackInfo(Stack stack) {
        System.out.println("Stack size: " + stack.getSize());
        System.out.println("Stack capacity: " + stack.getCapacity());
        
        // Count non-null elements in the internal array
        Object[] internalArray = stack.getInternalArray();
        long nonNullCount = Arrays.stream(internalArray)
                .filter(obj -> obj != null)
                .count();
        
        System.out.println("Non-null elements in internal array: " + nonNullCount);
        
        // Print memory usage
        Runtime runtime = Runtime.getRuntime();
        runtime.gc(); // Request garbage collection
        long usedMemory = runtime.totalMemory() - runtime.freeMemory();
        System.out.printf("Used memory: %.2f MB%n", usedMemory / (1024.0 * 1024.0));
    }
}
