package chapter2.item6_avoid_creating_unnecessary_objects;

public class StringConcatenation {
    public static void main(String[] args) {
        int iterations = 100_000;
        
        // Bad - creates many String instances
        long startTime = System.nanoTime();
        String resultWithConcatenation = "";
        for (int i = 0; i < iterations; i++) {
            resultWithConcatenation += "item" + i + ",";
        }
        long concatenationTime = System.nanoTime() - startTime;

        // Good - reuses the same StringBuilder
        startTime = System.nanoTime();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < iterations; i++) {
            sb.append("item").append(i).append(",");
        }
        String resultWithBuilder = sb.toString();
        long builderTime = System.nanoTime() - startTime;

        // Also good - specify initial capacity if known
        startTime = System.nanoTime();
        // StringBuilder with initial capacity: 8 chars per item
        StringBuilder sbWithCapacity = new StringBuilder(iterations * 8);
        for (int i = 0; i < iterations; i++) {
            sbWithCapacity.append("item").append(i).append(",");
        }
        String resultWithCapacity = sbWithCapacity.toString();
        long capacityTime = System.nanoTime() - startTime;

        // Print results
        System.out.println("String concatenation performance test:");
        System.out.printf("Using string concatenation: %.3f seconds%n", 
                concatenationTime / 1_000_000_000.0);
        System.out.printf("Using StringBuilder: %.3f seconds%n", 
                builderTime / 1_000_000_000.0);
        System.out.printf("Using StringBuilder with capacity: %.3f seconds%n%n", 
                capacityTime / 1_000_000_000.0);

        System.out.printf("StringBuilder is %.1fx faster than concatenation%n", 
                (double) concatenationTime / builderTime);
        System.out.printf("StringBuilder with capacity is %.1fx faster than concatenation%n", 
                (double) concatenationTime / capacityTime);

        // Verify results are the same
        System.out.println("\nVerifying results are identical:");
        System.out.println("All results match: " + 
                (resultWithConcatenation.equals(resultWithBuilder) && 
                 resultWithBuilder.equals(resultWithCapacity)));

        // Memory impact
        System.out.println("\nMemory Impact Analysis:");
        System.out.println("String concatenation creates a new String object on each iteration");
        long totalWastedMemory = 0;
        for (int i = 1; i <= iterations; i++) {
            // Each iteration creates a string approximately i characters long
            totalWastedMemory += i * 2; // Assuming 2 bytes per char
        }
        System.out.printf("Approximate wasted memory in concatenation: %,d bytes%n", 
                totalWastedMemory);
    }
}
