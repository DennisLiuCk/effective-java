package chapter2.item8_avoid_finalizers_and_cleaners;

/**
 * Example resource class demonstrating proper cleanup vs finalizers.
 * 
 * This class simulates a file resource that requires proper cleanup.
 * It shows both:
 * 1. The right way: implementing AutoCloseable and using close()
 * 2. The wrong way: using a finalizer (shown only as an example of what not to do)
 * 
 * In real code:
 * - Always use try-with-resources
 * - Never rely on finalizers
 * - Close resources promptly
 * - Implement AutoCloseable properly
 */
public class FileResource implements AutoCloseable {
    private boolean closed;
    private final String filename;
    private byte[] fileContent;  // Simulates file content in memory

    /**
     * Creates a new file resource.
     * 
     * @param filename name of the file
     * @param sizeInMB size of the file in megabytes (to simulate memory usage)
     */
    public FileResource(String filename, int sizeInMB) {
        this.filename = filename;
        // Simulate allocating memory for file content (1MB = 1024 * 1024 bytes)
        this.fileContent = new byte[sizeInMB * 1024 * 1024];
        System.out.println("Created file resource: " + filename);
    }

    /**
     * Simulates writing data to the file.
     * 
     * @throws IllegalStateException if the resource is already closed
     */
    public void writeData() {
        if (closed) {
            throw new IllegalStateException("Resource is closed: " + filename);
        }
        System.out.println("Writing to file: " + filename);
    }

    /**
     * Properly cleans up the resource.
     * This is the correct way to handle resource cleanup.
     */
    @Override
    public void close() {
        if (closed) return;
        
        // Cleanup the resource properly
        closed = true;
        fileContent = null;  // Release memory
        System.out.println("Closed file resource: " + filename);
    }

    /**
     * WRONG WAY - Never rely on finalizers!
     * This is shown only as an example of what not to do.
     * 
     * Problems with finalizers:
     * 1. No guarantee when they'll run
     * 2. May not run at all
     * 3. Severely impact performance
     * 4. Can cause security issues
     */
    @Override
    protected void finalize() throws Throwable {
        // Finalizer might run long after the resource should have been cleaned up
        if (!closed) {
            System.out.println("WARNING: Resource not properly closed: " + filename);
            close();  // Attempt cleanup in finalizer (unreliable!)
        }
        super.finalize();
    }
}
