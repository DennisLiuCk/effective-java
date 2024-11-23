package chapter2.item4_private_constructor;

/**
 * This class demonstrates the wrong way to write a utility class.
 * Problems with this approach:
 * 1. Can be instantiated (wastes memory)
 * 2. Can be subclassed (violates utility class design)
 * 3. Default constructor is created automatically
 */
public class WrongUtilityClass {
    // No explicit constructor = public default constructor
    
    /**
     * Static utility method
     */
    public static int add(int a, int b) {
        return a + b;
    }
    
    /**
     * Static utility method
     */
    public static int multiply(int a, int b) {
        return a * b;
    }
}
