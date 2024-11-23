package chapter2.item4_private_constructor;

/**
 * Item 4: Enforce noninstantiability with a private constructor
 * 
 * This class demonstrates how to properly create a utility class
 * that is not meant to be instantiated. The private constructor
 * prevents instantiation and inheritance.
 */
public class StringUtils {
    /**
     * Private constructor to prevent instantiation.
     * Throws AssertionError if someone tries to access it via reflection.
     */
    private StringUtils() {
        throw new AssertionError("No StringUtils instances for you!");
    }
    
    /**
     * Returns true if the string is null or empty.
     */
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }
    
    /**
     * Returns true if the string is null, empty, or contains only whitespace.
     */
    public static boolean isBlank(String str) {
        if (str == null) {
            return true;
        }
        
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Safely trims a string, handling null input.
     */
    public static String safeTrim(String str) {
        return str == null ? null : str.trim();
    }
    
    /**
     * Returns the string "null" if the input is null, otherwise returns the string itself.
     */
    public static String nullToString(String str) {
        return str == null ? "null" : str;
    }
}
