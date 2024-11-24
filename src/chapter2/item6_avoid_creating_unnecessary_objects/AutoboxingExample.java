package chapter2.item6_avoid_creating_unnecessary_objects;

public class AutoboxingExample {
    public static void main(String[] args) {
        // Test with a smaller number for demonstration
        int numIterations = 10_000_000;

        // Bad - uses autoboxing
        Long sumWithAutoboxing = 0L;
        long startTime = System.nanoTime();
        for (long i = 0; i < numIterations; i++) {
            sumWithAutoboxing += i;  // Autoboxing happens here!
        }
        long autoboxingTime = System.nanoTime() - startTime;

        // Good - uses primitive
        long sumWithPrimitive = 0L;
        startTime = System.nanoTime();
        for (long i = 0; i < numIterations; i++) {
            sumWithPrimitive += i;
        }
        long primitiveTime = System.nanoTime() - startTime;

        // Print results
        System.out.printf("Sum up to %,d:%n", numIterations);
        System.out.printf("Using autoboxing (Long): %,d%n", sumWithAutoboxing);
        System.out.printf("Time taken: %.3f seconds%n%n", autoboxingTime / 1_000_000_000.0);

        System.out.printf("Using primitive (long): %,d%n", sumWithPrimitive);
        System.out.printf("Time taken: %.3f seconds%n%n", primitiveTime / 1_000_000_000.0);

        System.out.printf("Using primitive is %.1fx faster%n", 
                (double) autoboxingTime / primitiveTime);

        // Memory usage demonstration
        System.out.println("\nMemory Impact Demonstration:");
        System.out.println("Each Long object typically uses 16 bytes of memory");
        System.out.printf("Total extra memory for Long objects: %,d bytes%n",
                numIterations * 16L);
    }
}
