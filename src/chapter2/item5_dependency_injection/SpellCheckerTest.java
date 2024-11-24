package chapter2.item5_dependency_injection;

public class SpellCheckerTest {
    public static void main(String[] args) {
        // Test with English Dictionary
        Dictionary englishDict = new EnglishDictionary();
        SpellChecker englishChecker = new SpellChecker(englishDict);
        
        // Test with Simple Dictionary (accepts words between 3-6 characters)
        Dictionary simpleDict = new SimpleDictionary(3, 6);
        SpellChecker simpleChecker = new SpellChecker(simpleDict);
        
        String[] wordsToCheck = {"hello", "wrld", "JAVA", "dependensy", "a", "verylongword"};
        
        System.out.println("Using English Dictionary:");
        System.out.println("------------------------");
        checkWords(englishChecker, wordsToCheck);
        
        System.out.println("\nUsing Simple Dictionary (3-6 chars):");
        System.out.println("----------------------------------");
        checkWords(simpleChecker, wordsToCheck);
    }
    
    private static void checkWords(SpellChecker checker, String[] words) {
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
