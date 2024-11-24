package chapter2.item5_dependency_injection;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// Concrete implementation of Dictionary
public class EnglishDictionary implements Dictionary {
    private final Set<String> words = new HashSet<>(Arrays.asList(
        "hello", "world", "java", "programming", "dependency", "injection"
    ));

    @Override
    public boolean contains(String word) {
        return words.contains(word.toLowerCase());
    }

    @Override
    public List<String> getSuggestions(String typo) {
        // Simplified implementation - just returns some valid words
        return Arrays.asList("hello", "world");
    }
}
