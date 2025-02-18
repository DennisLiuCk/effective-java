package chapter3.item11_hashcode;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

public class HashCodeDemo {
    public static void main(String[] args) {
        // Demonstrate Point hashCode
        demonstratePointHashCode();

        // Demonstrate Person hashCode with builder pattern
        demonstratePersonHashCode();

        // Demonstrate inheritance with ColorPoint
        demonstrateColorPointHashCode();

        // Demonstrate hash-based collections
        demonstrateHashMapUsage();
    }

    private static void demonstratePointHashCode() {
        Point p1 = new Point(1, 2);
        Point p2 = new Point(1, 2);
        Point p3 = new Point(2, 1);

        System.out.println("\nPoint HashCode Demonstration:");
        System.out.println("p1.hashCode() == p1.hashCode(): " + (p1.hashCode() == p1.hashCode()));
        System.out.println("p1.hashCode() == p2.hashCode(): " + (p1.hashCode() == p2.hashCode()));
        System.out.println("p1.hashCode() == p3.hashCode(): " + (p1.hashCode() == p3.hashCode()));
        
        // Compare with manual implementation
        System.out.println("Manual hashCode comparison:");
        System.out.println("p1.manualHashCode(): " + p1.manualHashCode());
        System.out.println("p2.manualHashCode(): " + p2.manualHashCode());
    }

    private static void demonstratePersonHashCode() {
        Person person1 = new Person.Builder()
                .firstName("John")
                .lastName("Doe")
                .age(30)
                .email("john@example.com")
                .build();

        Person person2 = new Person.Builder()
                .firstName("John")
                .lastName("Doe")
                .age(30)
                .email("john@example.com")
                .build();

        System.out.println("\nPerson HashCode Demonstration:");
        System.out.println("person1.equals(person2): " + person1.equals(person2));
        System.out.println("person1.hashCode() == person2.hashCode(): " + 
                         (person1.hashCode() == person2.hashCode()));
    }

    private static void demonstrateColorPointHashCode() {
        ColorPoint cp1 = new ColorPoint(1, 2, Color.RED);
        ColorPoint cp2 = new ColorPoint(1, 2, Color.RED);
        ColorPoint cp3 = new ColorPoint(1, 2, Color.BLUE);

        System.out.println("\nColorPoint HashCode Demonstration:");
        System.out.println("cp1.equals(cp2): " + cp1.equals(cp2));
        System.out.println("cp1.hashCode() == cp2.hashCode(): " + (cp1.hashCode() == cp2.hashCode()));
        System.out.println("cp1.equals(cp3): " + cp1.equals(cp3));
        System.out.println("cp1.hashCode() == cp3.hashCode(): " + (cp1.hashCode() == cp3.hashCode()));
    }

    private static void demonstrateHashMapUsage() {
        Map<Point, String> pointMap = new HashMap<>();
        Point p1 = new Point(1, 2);
        Point p2 = new Point(1, 2);

        System.out.println("\nHashMap Usage Demonstration:");
        pointMap.put(p1, "First Point");
        System.out.println("Value for p2: " + pointMap.get(p2));
        System.out.println("This works because p1 and p2 have the same hashCode and are equals()");
    }
}