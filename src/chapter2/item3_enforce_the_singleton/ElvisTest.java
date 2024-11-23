package chapter2.item3_enforce_the_singleton;

/**
 * Test class demonstrating all three approaches to singleton implementation:
 * 1. Public static final field
 * 2. Static factory method
 * 3. Enum (preferred approach)
 */
public class ElvisTest {
    
    public static void main(String[] args) {
        testPublicFieldSingleton();
        testStaticFactorySingleton();
        testEnumSingleton();
        testSingletonIdentity();
        
        System.out.println("\nAll tests passed!");
    }
    
    private static void testPublicFieldSingleton() {
        Elvis elvis1 = Elvis.INSTANCE;
        Elvis elvis2 = Elvis.INSTANCE;
        
        // Verify we get the same instance
        if (elvis1 != elvis2) {
            throw new AssertionError("Public field singleton test failed: instances are not the same");
        }
        
        // Test functionality
        System.out.println("\nTesting public field singleton:");
        elvis1.leaveTheBuilding();
    }
    
    private static void testStaticFactorySingleton() {
        ElvisStaticFactory elvis1 = ElvisStaticFactory.getInstance();
        ElvisStaticFactory elvis2 = ElvisStaticFactory.getInstance();
        
        // Verify we get the same instance
        if (elvis1 != elvis2) {
            throw new AssertionError("Static factory singleton test failed: instances are not the same");
        }
        
        // Test functionality
        System.out.println("\nTesting static factory singleton:");
        elvis1.leaveTheBuilding();
    }
    
    private static void testEnumSingleton() {
        ElvisEnum elvis1 = ElvisEnum.INSTANCE;
        ElvisEnum elvis2 = ElvisEnum.INSTANCE;
        
        // Verify we get the same instance
        if (elvis1 != elvis2) {
            throw new AssertionError("Enum singleton test failed: instances are not the same");
        }
        
        // Test functionality
        System.out.println("\nTesting enum singleton:");
        elvis1.leaveTheBuilding();
        System.out.println("Demonstrating enum singleton state:");
        elvis1.singFavoriteMovie();
        elvis2.singFavoriteMovie();
    }
    
    private static void testSingletonIdentity() {
        // Test all three implementations
        Elvis elvis1 = Elvis.INSTANCE;
        if (elvis1 == null) {
            throw new AssertionError("Elvis instance should not be null");
        }
        
        ElvisStaticFactory elvis2 = ElvisStaticFactory.getInstance();
        if (elvis2 == null) {
            throw new AssertionError("ElvisStaticFactory instance should not be null");
        }
        
        ElvisEnum elvis3 = ElvisEnum.INSTANCE;
        if (elvis3 == null) {
            throw new AssertionError("ElvisEnum instance should not be null");
        }
        
        System.out.println("\nIdentity tests passed!");
    }
}
