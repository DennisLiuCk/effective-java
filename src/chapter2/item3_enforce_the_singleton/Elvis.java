package chapter2.item3_enforce_the_singleton;

/**
 * Item 3: Enforce the singleton property with a private constructor or an enum type
 * 
 * This class demonstrates the public field approach to implementing a singleton.
 * The singleton property is enforced through a private constructor and
 * a public static final field.
 */
public class Elvis {
    // Approach 1: public static final field
    public static final Elvis INSTANCE = new Elvis();
    
    private Elvis() {} // Private constructor
    
    public void leaveTheBuilding() {
        System.out.println("Whoa baby, I'm outta here!");
    }
    
    // To preserve singleton property when serializing
    // Insight: When deserializing, readResolve() is called. 
    // This method returns the singleton instance, not a new instance.
    private Object readResolve() {
        return INSTANCE;
    }
}
