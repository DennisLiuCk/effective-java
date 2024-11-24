package chapter2.item5_dependency_injection;

import java.util.List;
import java.util.ArrayList;

// A simpler dictionary implementation that just checks word length
public class SimpleDictionary implements Dictionary {
    private final int minLength;
    private final int maxLength;
    
    public SimpleDictionary(int minLength, int maxLength) {
        this.minLength = minLength;
        this.maxLength = maxLength;
    }
    
    @Override
    public boolean contains(String word) {
        int length = word.length();
        return length >= minLength && length <= maxLength;
    }
    
    @Override
    public List<String> getSuggestions(String typo) {
        List<String> suggestions = new ArrayList<>();
        if (typo.length() < minLength) {
            suggestions.add(typo + "x".repeat(minLength - typo.length()));
        } else if (typo.length() > maxLength) {
            suggestions.add(typo.substring(0, maxLength));
        }
        return suggestions;
    }
}
