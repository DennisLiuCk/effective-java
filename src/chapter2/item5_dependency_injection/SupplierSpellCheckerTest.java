package chapter2.item5_dependency_injection;

import java.util.function.Supplier;

public class SupplierSpellCheckerTest {
    public static void main(String[] args) {
        // Create suppliers (factories) for different dictionary implementations
        Supplier<Dictionary> englishDictFactory = EnglishDictionary::new;
        Supplier<Dictionary> simpleDictFactory = () -> new SimpleDictionary(3, 6);
        
        // Create spell checkers with different dictionary factories
        SupplierSpellChecker englishChecker = new SupplierSpellChecker(englishDictFactory);
        SupplierSpellChecker simpleChecker = new SupplierSpellChecker(simpleDictFactory);
        
        String[] wordsToCheck = {"hello", "wrld", "JAVA", "dependensy", "a", "verylongword"};
        
        System.out.println("Using English Dictionary (via Supplier):");
        System.out.println("-------------------------------------");
        checkWords(englishChecker, wordsToCheck);
        
        System.out.println("\nUsing Simple Dictionary (via Supplier):");
        System.out.println("-------------------------------------");
        checkWords(simpleChecker, wordsToCheck);
        
        // Example of creating a new instance each time (not recommended for this case)
        SupplierSpellChecker newInstanceChecker = new SupplierSpellChecker(() -> {
            System.out.println("Creating new dictionary instance...");
            return new EnglishDictionary();
        });
        
        System.out.println("\nUsing new Dictionary instance each time:");
        System.out.println("--------------------------------------");
        checkWords(newInstanceChecker, wordsToCheck);
    }
    
    private static void checkWords(SupplierSpellChecker checker, String[] words) {
        for (String word : words) {
            System.out.println("Checking word: " + word);
            if (checker.isValid(word)) {
                System.out.println("\"" + word + "\" is valid");
            } else {
                System.out.println("\"" + word + "\" is not valid");
                System.out.println("Suggestions: " + checker.suggestions(word));
            }
            System.out.println();
        }
    }
}
