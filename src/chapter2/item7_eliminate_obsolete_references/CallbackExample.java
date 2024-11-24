package chapter2.item7_eliminate_obsolete_references;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

/**
 * This class demonstrates memory leak issues with event listeners and callbacks,
 * simulating a real-world application with UI components and event handling.
 */

// Interface for handling UI component events
interface ComponentListener {
    void onComponentEvent(String event);
}

// Interface for handling network events
interface NetworkListener {
    void onNetworkEvent(String event);
}

/**
 * Simulates a UI Component (like a button or window) that can have listeners
 * This demonstrates the common memory leak with event listeners
 */
class UIComponent {
    private final String name;
    private final List<ComponentListener> listeners = new ArrayList<>();

    public UIComponent(String name) {
        this.name = name;
    }

    public void addListener(ComponentListener listener) {
        listeners.add(listener);
    }

    public void removeListener(ComponentListener listener) {
        listeners.remove(listener);
    }

    public void simulateEvent(String event) {
        System.out.printf("%s triggering event: %s%n", name, event);
        for (ComponentListener listener : listeners) {
            listener.onComponentEvent(event);
        }
    }

    public int getListenerCount() {
        return listeners.size();
    }
}

/**
 * NetworkManager using WeakHashMap to prevent memory leaks with network callbacks
 * This demonstrates the proper way to handle callbacks
 */
class NetworkManager {
    private final WeakHashMap<NetworkListener, Object> listeners = new WeakHashMap<>();
    private final String name;

    public NetworkManager(String name) {
        this.name = name;
    }

    public void addListener(NetworkListener listener) {
        listeners.put(listener, null);
    }

    public void simulateEvent(String event) {
        System.out.printf("%s triggering event: %s%n", name, event);
        for (NetworkListener listener : listeners.keySet()) {
            listener.onNetworkEvent(event);
        }
    }

    public int getListenerCount() {
        return listeners.size();
    }
}

/**
 * Screen class that simulates a UI screen in an application
 * Used to demonstrate how screens can cause memory leaks if listeners aren't properly removed
 */
class Screen implements ComponentListener, NetworkListener {
    private final String name;
    private final byte[] screenData; // Simulate screen holding significant memory

    public Screen(String name) {
        this.name = name;
        // Simulate screen data (e.g., images, state) using 1MB of memory
        this.screenData = new byte[1000000];
    }

    @Override
    public void onComponentEvent(String event) {
        System.out.printf("Screen %s received UI event: %s%n", name, event);
    }

    @Override
    public void onNetworkEvent(String event) {
        System.out.printf("Screen %s received network event: %s%n", name, event);
    }
}

public class CallbackExample {
    public static void main(String[] args) throws InterruptedException {
        demonstrateUIMemoryLeak();
        demonstrateProperNetworkCallbacks();
    }

    /**
     * Demonstrates how forgetting to remove listeners can cause memory leaks
     * Common scenario: Screens registering for events but not unregistering when closed
     */
    private static void demonstrateUIMemoryLeak() throws InterruptedException {
        System.out.println("=== Scenario 1: UI Component Memory Leak ===");
        System.out.println("Simulating an app where screens listen to UI component events...");

        UIComponent button = new UIComponent("LoginButton");
        
        // Simulate opening and closing multiple screens
        for (int i = 0; i < 10; i++) {
            Screen screen = new Screen("Screen" + i);
            button.addListener(screen);
            System.out.printf("Screen%d opened and registered with button%n", i);
        }

        System.out.println("\nProblem: Screens were 'closed' but their listeners remain!");
        System.out.println("Button listener count: " + button.getListenerCount());
        printMemoryUsage("Memory before GC");

        // Try garbage collection
        System.gc();
        Thread.sleep(1000);

        System.out.println("\nAfter garbage collection:");
        System.out.println("Button listener count: " + button.getListenerCount());
        System.out.println("Notice: Listeners are still there, preventing GC of screens!");
        printMemoryUsage("Memory after GC");

        // Simulate an event to show listeners are still active
        System.out.println("\nTriggering button event:");
        button.simulateEvent("click");
    }

    /**
     * Demonstrates proper memory management using WeakHashMap for callbacks
     * Common scenario: Components listening for network events
     */
    private static void demonstrateProperNetworkCallbacks() throws InterruptedException {
        System.out.println("\n=== Scenario 2: Proper Network Callback Management ===");
        System.out.println("Simulating network event handling with proper cleanup...");

        NetworkManager network = new NetworkManager("NetworkService");
        List<Screen> activeScreens = new ArrayList<>();

        // Create and register screens
        for (int i = 0; i < 10; i++) {
            Screen screen = new Screen("Screen" + i);
            network.addListener(screen);
            if (i < 5) {
                activeScreens.add(screen); // Keep references to only first 5 screens
            }
            System.out.printf("Screen%d registered for network events%n", i);
        }

        System.out.println("\nInitial state:");
        System.out.println("Network listener count: " + network.getListenerCount());
        printMemoryUsage("Memory before GC");

        // Try garbage collection
        System.gc();
        Thread.sleep(1000);

        System.out.println("\nAfter garbage collection:");
        System.out.println("Network listener count: " + network.getListenerCount());
        System.out.println("Notice: Only listeners for active screens remain!");
        printMemoryUsage("Memory after GC");

        // Simulate network event to show only active listeners receive it
        System.out.println("\nTriggering network event:");
        network.simulateEvent("dataReceived");
    }

    private static void printMemoryUsage(String label) {
        Runtime runtime = Runtime.getRuntime();
        long usedMemory = runtime.totalMemory() - runtime.freeMemory();
        System.out.printf("%s: %.2f MB%n", label, usedMemory / (1024.0 * 1024.0));
    }
}
