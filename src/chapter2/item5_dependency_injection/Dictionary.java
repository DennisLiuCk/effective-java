package chapter2.item5_dependency_injection;

import java.util.List;

// Interface representing the dependency
public interface Dictionary {
    boolean contains(String word);
    List<String> getSuggestions(String typo);
}
