package chapter2.item5_dependency_injection;

import java.util.List;

// Bad example - Static utility class that creates its own resource
public class StaticSpellChecker {
    // Dictionary is hardwired - we can't change it!
    private static final Dictionary dictionary = new EnglishDictionary();
    
    // Noninstantiable
    private StaticSpellChecker() {
        throw new AssertionError("No instances for you!");
    }
    
    public static boolean isValid(String word) {
        return dictionary.contains(word);
    }
    
    public static List<String> suggestions(String typo) {
        return dictionary.getSuggestions(typo);
    }
}
