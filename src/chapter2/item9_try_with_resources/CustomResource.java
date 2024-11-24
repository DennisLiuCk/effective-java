package chapter2.item9_try_with_resources;

/**
 * A custom resource class that demonstrates proper implementation of AutoCloseable.
 * This class simulates a resource that needs cleanup, like a database connection
 * or file handle.
 */
public class CustomResource implements AutoCloseable {
    private final String name;
    private boolean closed = false;

    public CustomResource(String name) {
        this.name = name;
        System.out.printf("Resource '%s' created%n", name);
    }

    public void doWork() throws Exception {
        if (closed) {
            throw new IllegalStateException("Resource is already closed");
        }
        
        System.out.printf("Resource '%s' doing work%n", name);
        
        // Simulate random failure during work
        if (Math.random() < 1) {
            throw new Exception("Random failure while doing work");
        }
    }

    @Override
    public void close() throws Exception {
        if (closed) {
            return;
        }
        
        System.out.printf("Closing resource '%s'%n", name);
        
        // Simulate random failure during close
        if (Math.random() < 1) {
            throw new Exception("Random failure while closing resource");
        }
        
        closed = true;
    }
}
