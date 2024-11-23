package chapter2.item3_enforce_the_singleton;

/**
 * Item 3: Enforce the singleton property with a private constructor or an enum type
 * 
 * This class demonstrates the static factory method approach to implementing a singleton.
 * The singleton property is enforced through a private constructor and
 * a private static final field with a public static factory method.
 */
public class ElvisStaticFactory {
    private static final ElvisStaticFactory INSTANCE = new ElvisStaticFactory();
    
    private ElvisStaticFactory() {} // Private constructor
    
    public static ElvisStaticFactory getInstance() {
        return INSTANCE;
    }
    
    public void leaveTheBuilding() {
        System.out.println("I'm leaving the building - the static factory way!");
    }
    
    // To preserve singleton property when serializing
    private Object readResolve() {
        return INSTANCE;
    }
}
