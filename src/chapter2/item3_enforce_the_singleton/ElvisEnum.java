package chapter2.item3_enforce_the_singleton;

/**
 * Item 3: Enforce the singleton property with a private constructor or an enum type
 * 
 * This class demonstrates the enum approach to implementing a singleton.
 * This is the preferred approach as it provides serialization safety for free
 * and guarantees against multiple instantiation, even in the face of sophisticated
 * serialization or reflection attacks.
 */
public enum ElvisEnum {
    INSTANCE;  // The one and only instance of Elvis
    
    private final String[] favoriteMovies = {
        "Jailhouse Rock", 
        "Blue Hawaii", 
        "Viva Las Vegas"
    };
    
    public void leaveTheBuilding() {
        System.out.println("I'm leaving the building - the enum way!");
    }
    
    public void singFavoriteMovie() {
        int movieIndex = (int) (Math.random() * favoriteMovies.length);
        System.out.println("~~ Now playing: " + favoriteMovies[movieIndex] + " ~~");
    }
}
