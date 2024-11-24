package chapter2.item5_dependency_injection;

import java.util.List;
import java.util.Objects;

// Good example using dependency injection
public class SpellChecker {
    private final Dictionary dictionary;

    // Dependency is injected through constructor
    public SpellChecker(Dictionary dictionary) {
        this.dictionary = Objects.requireNonNull(dictionary);
    }

    public boolean isValid(String word) {
        return dictionary.contains(word);
    }

    public List<String> suggestions(String typo) {
        return dictionary.getSuggestions(typo);
    }
}
