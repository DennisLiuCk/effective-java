package chapter2.item5_dependency_injection;

public class StaticSpellCheckerTest {
    public static void main(String[] args) {
        // No way to inject a different dictionary implementation!
        // We're stuck with whatever dictionary StaticSpellChecker uses
        
        String[] wordsToCheck = {"hello", "wrld", "JAVA", "dependensy"};
        
        System.out.println("Using Static Spell Checker (Bad Example):");
        System.out.println("----------------------------------------");
        
        for (String word : wordsToCheck) {
            System.out.println("Checking word: " + word);
            if (StaticSpellChecker.isValid(word)) {
                System.out.println("\"" + word + "\" is valid");
            } else {
                System.out.println("\"" + word + "\" is not valid");
                System.out.println("Suggestions: " + StaticSpellChecker.suggestions(word));
            }
            System.out.println();
        }
        
        // Problems with this approach:
        // 1. Can't use a different dictionary implementation
        // 2. Can't test with a mock dictionary
        // 3. Can't have multiple instances with different dictionaries
        // 4. Resource is hardwired into the class
    }
}
