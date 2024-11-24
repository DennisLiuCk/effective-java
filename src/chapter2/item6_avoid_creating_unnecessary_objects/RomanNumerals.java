package chapter2.item6_avoid_creating_unnecessary_objects;

import java.util.regex.Pattern;

public class RomanNumerals {
    // Bad - creates a new Pattern instance on every invocation
    static boolean isRomanNumeralSlow(String s) {
        return s.matches("^(?=.)M*(C[MD]|D?C{0,3})"
                + "(X[CL]|L?X{0,3})(I[XV]|V?I{0,3})$");
    }

    // Good - compiles Pattern once and reuses it
    private static final Pattern ROMAN = Pattern.compile(
            "^(?=.)M*(C[MD]|D?C{0,3})(X[CL]|L?X{0,3})(I[XV]|V?I{0,3})$");

    static boolean isRomanNumeralFast(String s) {
        return ROMAN.matcher(s).matches();
    }

    public static void main(String[] args) {
        // Test strings
        String[] testStrings = {
            "MCMLXXVI",  // 1976 - valid
            "MCMXCIX",   // 1999 - valid
            "MMXXI",     // 2021 - valid
            "ABC",       // invalid
            "IIII"       // invalid (should be IV)
        };

        // Warm up the JVM
        for (int i = 0; i < 10000; i++) {
            isRomanNumeralSlow("MCMLXXVI");
            isRomanNumeralFast("MCMLXXVI");
        }

        // Test performance
        long startTime = System.nanoTime();
        for (int i = 0; i < 100000; i++) {
            for (String s : testStrings) {
                isRomanNumeralSlow(s);
            }
        }
        long endTime = System.nanoTime();
        long slowTime = endTime - startTime;

        startTime = System.nanoTime();
        for (int i = 0; i < 100000; i++) {
            for (String s : testStrings) {
                isRomanNumeralFast(s);
            }
        }
        endTime = System.nanoTime();
        long fastTime = endTime - startTime;

        // Print results
        System.out.println("Validating Roman numerals 100,000 times:");
        System.out.println("Slow version (creating Pattern each time):");
        for (String s : testStrings) {
            System.out.printf("  %s is %s%n", s, 
                    isRomanNumeralSlow(s) ? "valid" : "invalid");
        }
        System.out.printf("Time taken: %.3f seconds%n%n", slowTime / 1_000_000_000.0);

        System.out.println("Fast version (reusing Pattern):");
        for (String s : testStrings) {
            System.out.printf("  %s is %s%n", s, 
                    isRomanNumeralFast(s) ? "valid" : "invalid");
        }
        System.out.printf("Time taken: %.3f seconds%n%n", fastTime / 1_000_000_000.0);

        System.out.printf("Fast version is %.1fx faster%n", 
                (double) slowTime / fastTime);
    }
}
