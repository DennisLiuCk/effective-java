package chapter2.item5_dependency_injection;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

// Example of dependency injection using Supplier as a factory
public class SupplierSpellChecker {
    private final Dictionary dictionary;

    // Dependency is injected through a factory (Supplier)
    public SupplierSpellChecker(Supplier<? extends Dictionary> dictionaryFactory) {
        this.dictionary = Objects.requireNonNull(dictionaryFactory.get());
    }

    public boolean isValid(String word) {
        return dictionary.contains(word);
    }

    public List<String> suggestions(String typo) {
        return dictionary.getSuggestions(typo);
    }
}
