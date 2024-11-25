package chapter3.item10_equals;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * Demonstrates various aspects of equals contract and common pitfalls.
 */
public class EqualsDemo {
    public static void main(String[] args) {
        demonstrateEqualsContract();
        System.out.println("\n" + "=".repeat(50) + "\n");
        
        demonstrateInheritanceProblem();
        System.out.println("\n" + "=".repeat(50) + "\n");
        
        demonstrateCompositionSolution();
    }

    private static void demonstrateEqualsContract() {
        System.out.println("Demonstration 1: Equals Contract");
        Point p1 = new Point(1, 2);
        Point p2 = new Point(1, 2);
        Point p3 = new Point(1, 2);

        // 1. Reflexivity: x.equals(x) returns true
        System.out.println("\nReflexivity:");
        System.out.println("p1.equals(p1) = " + p1.equals(p1));

        // 2. Symmetry: x.equals(y) iff y.equals(x)
        System.out.println("\nSymmetry:");
        System.out.println("p1.equals(p2) = " + p1.equals(p2));
        System.out.println("p2.equals(p1) = " + p2.equals(p1));

        // 3. Transitivity: if x.equals(y) and y.equals(z), then x.equals(z)
        System.out.println("\nTransitivity:");
        System.out.println("p1.equals(p2) = " + p1.equals(p2));
        System.out.println("p2.equals(p3) = " + p2.equals(p3));
        System.out.println("p1.equals(p3) = " + p1.equals(p3));

        // 4. Consistency
        System.out.println("\nConsistency:");
        System.out.println("Multiple calls to equals:");
        for (int i = 0; i < 3; i++) {
            System.out.println("p1.equals(p2) = " + p1.equals(p2));
        }

        // 5. Non-nullity
        System.out.println("\nNon-nullity:");
        System.out.println("p1.equals(null) = " + p1.equals(null));
    }

    private static void demonstrateInheritanceProblem() {
        System.out.println("Demonstration 2: Inheritance Problem");
        
        Point p = new Point(1, 2);
        ColorPoint.InheritanceColorPoint cp = 
            new ColorPoint.InheritanceColorPoint(1, 2, Color.RED);
        ColorPoint.InheritanceColorPoint cp2 = 
            new ColorPoint.InheritanceColorPoint(1, 2, Color.BLUE);

        // Violates symmetry!
        System.out.println("\nSymmetry violation with inheritance:");
        System.out.println("p.equals(cp) = " + p.equals(cp));
        System.out.println("cp.equals(p) = " + cp.equals(p));

        // Violates transitivity!
        System.out.println("\nTransitivity violation with inheritance:");
        System.out.println("p.equals(cp) = " + p.equals(cp));
        System.out.println("p.equals(cp2) = " + p.equals(cp2));
        System.out.println("cp.equals(cp2) = " + cp.equals(cp2));
    }

    private static void demonstrateCompositionSolution() {
        System.out.println("Demonstration 3: Composition Solution");
        
        // Create points with different colors using composition
        ColorPoint cp1 = new ColorPoint(1, 2, Color.RED);
        ColorPoint cp2 = new ColorPoint(1, 2, Color.RED);
        ColorPoint cp3 = new ColorPoint(1, 2, Color.BLUE);

        // Demonstrate proper equals behavior
        System.out.println("\nProper equals behavior with composition:");
        System.out.println("cp1.equals(cp2) = " + cp1.equals(cp2));  // true (same color)
        System.out.println("cp1.equals(cp3) = " + cp1.equals(cp3));  // false (different color)

        // Demonstrate view conversion
        System.out.println("\nView conversion behavior:");
        Point p1 = cp1.asPoint();
        Point p3 = cp3.asPoint();
        System.out.println("cp1.asPoint().equals(cp3.asPoint()) = " + 
                          p1.equals(p3));  // true (ignores color)

        // Demonstrate collection behavior
        List<ColorPoint> points = new ArrayList<>();
        points.add(cp1);
        System.out.println("\nCollection behavior:");
        System.out.println("points.contains(cp2) = " + points.contains(cp2));  // true
        System.out.println("points.contains(cp3) = " + points.contains(cp3));  // false
    }
}
