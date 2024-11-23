package chapter2.item4_private_constructor;

/**
 * Test class demonstrating the differences between proper and improper utility classes.
 */
public class UtilityClassTest {
    
    public static void main(String[] args) {
        testStringUtils();
        testWrongUtilityClass();
        
        System.out.println("\nAll tests completed!");
    }
    
    private static void testStringUtils() {
        System.out.println("Testing StringUtils:");
        System.out.println("-----------------");
        
        // Test isEmpty
        System.out.println("isEmpty(null): " + StringUtils.isEmpty(null));
        System.out.println("isEmpty(\"\"): " + StringUtils.isEmpty(""));
        System.out.println("isEmpty(\"  \"): " + StringUtils.isEmpty("  "));
        System.out.println("isEmpty(\"test\"): " + StringUtils.isEmpty("test"));
        
        // Test isBlank
        System.out.println("\nisBlank(null): " + StringUtils.isBlank(null));
        System.out.println("isBlank(\"\"): " + StringUtils.isBlank(""));
        System.out.println("isBlank(\"  \"): " + StringUtils.isBlank("  "));
        System.out.println("isBlank(\"test\"): " + StringUtils.isBlank("test"));
        
        // Test safeTrim
        System.out.println("\nsafeTrim(null): " + StringUtils.safeTrim(null));
        System.out.println("safeTrim(\"  test  \"): " + StringUtils.safeTrim("  test  "));
        
        // Test nullToString
        System.out.println("\nnullToString(null): " + StringUtils.nullToString(null));
        System.out.println("nullToString(\"test\"): " + StringUtils.nullToString("test"));
        
        // Try to create instance (should fail)
        try {
            // This would fail at compile time if we tried to access the constructor directly
            // But we demonstrate that even reflection would fail
            Class<?> clazz = StringUtils.class;
            clazz.getDeclaredConstructor().newInstance();
            System.out.println("ERROR: Should not be able to create StringUtils instance!");
        } catch (Exception e) {
            System.out.println("\nCorrectly prevented instantiation of StringUtils");
        }
    }
    
    private static void testWrongUtilityClass() {
        System.out.println("\nTesting WrongUtilityClass:");
        System.out.println("-------------------------");
        
        // This class can be instantiated (bad!)
        WrongUtilityClass wrongUtil1 = new WrongUtilityClass();
        System.out.println("WARNING: Was able to create instance of WrongUtilityClass! This is bad! (1st instance)");

        WrongUtilityClass wrongUtil2 = new WrongUtilityClass();
        System.out.println("WARNING: Was able to create instance of WrongUtilityClass! This is bad! (2nd instance)");
        
        // Methods still work, but through instance (unnecessary)
        System.out.println("2 + 3 = " + wrongUtil1.add(2, 3));
        System.out.println("4 * 5 = " + wrongUtil2.multiply(4, 5));
        
        // Can also call statically (this is how it should be used)
        System.out.println("\nCalling statically (proper way):");
        System.out.println("2 + 3 = " + WrongUtilityClass.add(2, 3));
        System.out.println("4 * 5 = " + WrongUtilityClass.multiply(4, 5));
    }
}
