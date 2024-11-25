# Item 8: Avoid finalizers and cleaners

## Overview

Finalizers and cleaners are Java's two mechanisms for object cleanup when an object becomes eligible for garbage collection. However, they are unpredictable, dangerous, and generally unnecessary. You should avoid them in favor of explicit resource management.

## Key Points

### Problems with Finalizers

1. **No Timing Guarantees**
   - No guarantee when finalizers will run
   - May take arbitrarily long between object becoming eligible for GC and finalizer running
   - Some objects may never have their finalizers run at all

2. **Performance Impact**
   - Severe performance penalty
   - Can slow down object creation and destruction
   - JVM must do more bookkeeping for objects with finalizers

3. **Security Issues**
   - Can allow finalizer attacks
   - Objects can be "resurrected" during finalization
   - Can break invariants of security-critical classes

### Problems with Cleaners

1. **Less Dangerous but Still Unreliable**
   - More flexible than finalizers
   - Still no guarantees about execution timing
   - Still no guarantee they'll run at all

2. **Complex to Use**
   - Require more complex setup than finalizers
   - Need to create separate cleaner and state objects
   - Easy to get wrong

## Best Practices

### 1. Implement AutoCloseable

```java
public class FileResource implements AutoCloseable {
    private boolean closed;
    private final String filename;

    @Override
    public void close() {
        if (closed) return;
        closed = true;
        // cleanup resources
    }
}
```

### 2. Use try-with-resources

```java
try (FileResource file = new FileResource("data.txt", 1)) {
    file.writeData();
    // Resource automatically closed here
}
```

### 3. Explicit Cleanup

```java
FileResource file = new FileResource("data.txt", 1);
try {
    file.writeData();
} finally {
    file.close();
}
```

## When to Use Cleaners

Cleaners and finalizers should only be used as a safety net for critical native resources. Two legitimate uses are:

1. **Safety Net**: As a backup in case the owner forgets to call `close()`
2. **Native Peer Objects**: For cleaning up native objects that the garbage collector knows nothing about

Example of a safety net:

```java
public class Room implements AutoCloseable {
    private static final Cleaner cleaner = Cleaner.create();
    private final State state;
    private final Cleaner.Cleanable cleanable;

    private static class State implements Runnable {
        @Override
        public void run() {
            // Cleanup native resources
        }
    }

    public Room() {
        this.state = new State();
        this.cleanable = cleaner.register(this, state);
    }

    @Override
    public void close() {
        cleanable.clean();
    }
}
```

## Common Resource Types Needing Cleanup

1. **File Resources**
   - File handles
   - Directory streams
   - File locks

2. **Network Resources**
   - Socket connections
   - Network channels
   - Database connections

3. **Native Resources**
   - Direct memory buffers
   - Native peer objects
   - System handles

## Real-World Applications

### 1. Database Connection Management

Bad approach using finalizers:
```java
public class DatabaseConnection {
    private Connection conn;
    
    public DatabaseConnection(String url) throws SQLException {
        this.conn = DriverManager.getConnection(url);
    }
    
    @Override
    protected void finalize() throws Throwable {
        if (conn != null) {
            conn.close();  // Unreliable cleanup!
        }
    }
}
```

Good approach using AutoCloseable:
```java
public class DatabaseConnection implements AutoCloseable {
    private Connection conn;
    private boolean closed;
    
    public DatabaseConnection(String url) throws SQLException {
        this.conn = DriverManager.getConnection(url);
    }
    
    @Override
    public void close() throws SQLException {
        if (!closed) {
            closed = true;
            conn.close();
        }
    }
}

// Usage
try (DatabaseConnection db = new DatabaseConnection("jdbc:mysql://localhost/db")) {
    // Use database
    // Connection automatically closed after try block
}
```

### 2. Native Image Processing

Example using a cleaner as a safety net:
```java
public class NativeImage implements AutoCloseable {
    private static final Cleaner cleaner = Cleaner.create();
    
    private static class ImageState implements Runnable {
        private long nativeAddress;
        
        ImageState(long address) {
            this.nativeAddress = address;
        }
        
        @Override
        public void run() {
            if (nativeAddress != 0) {
                freeNativeMemory(nativeAddress);
                nativeAddress = 0;
            }
        }
    }
    
    private final ImageState state;
    private final Cleaner.Cleanable cleanable;
    
    public NativeImage(String filename) {
        long address = loadImageNative(filename);
        this.state = new ImageState(address);
        this.cleanable = cleaner.register(this, state);
    }
    
    @Override
    public void close() {
        cleanable.clean();
    }
}
```

### 3. DirectByteBuffer Management

Real example from Java's NIO:
```java
public class CustomDirectBuffer implements AutoCloseable {
    private ByteBuffer buffer;
    private final Cleaner.Cleanable cleanable;
    
    public CustomDirectBuffer(int capacity) {
        this.buffer = ByteBuffer.allocateDirect(capacity);
        // DirectByteBuffer already uses Cleaner internally
        // This is just for demonstration
        this.cleanable = Cleaner.create().register(this, () -> {
            if (buffer != null) {
                // Force cleanup of direct buffer
                ((DirectBuffer) buffer).cleaner().clean();
                buffer = null;
            }
        });
    }
    
    @Override
    public void close() {
        cleanable.clean();
    }
}
```

### 4. Network Socket Management

Example of proper socket cleanup:
```java
public class NetworkClient implements AutoCloseable {
    private final Socket socket;
    private final BufferedReader reader;
    private final PrintWriter writer;
    
    public NetworkClient(String host, int port) throws IOException {
        socket = new Socket(host, port);
        reader = new BufferedReader(
            new InputStreamReader(socket.getInputStream()));
        writer = new PrintWriter(socket.getOutputStream(), true);
    }
    
    @Override
    public void close() throws IOException {
        try {
            reader.close();
        } finally {
            try {
                writer.close();
            } finally {
                socket.close();
            }
        }
    }
}

// Usage
try (NetworkClient client = new NetworkClient("localhost", 8080)) {
    // Use the network client
    // All resources automatically closed in reverse order
}
```

These examples demonstrate common real-world scenarios where resource management is critical and show both what to avoid (finalizers) and what to use instead (AutoCloseable, try-with-resources, and occasionally cleaners as a safety net).

## Implementing AutoCloseable Properly

When implementing the `close()` method, follow these key guidelines:

### 1. Idempotency

The `close()` method should be idempotent (can be called multiple times safely):

```java
public class Resource implements AutoCloseable {
    private boolean closed = false;
    
    @Override
    public void close() throws Exception {
        if (closed) return;  // Already closed, return silently
        closed = true;
        // Perform actual cleanup
    }
    
    public void doWork() {
        if (closed) {
            throw new IllegalStateException("Resource is closed");
        }
        // Do work
    }
}
```

### 2. Thread Safety

If the resource might be used in multiple threads:

```java
public class ThreadSafeResource implements AutoCloseable {
    private volatile boolean closed = false;
    private final Object lock = new Object();
    
    @Override
    public void close() throws Exception {
        if (closed) return;
        synchronized (lock) {
            if (closed) return;  // Double-check under lock
            closed = true;
            // Perform cleanup
        }
    }
}
```

### 3. Proper Exception Handling

When dealing with multiple resources:

```java
public class MultiResource implements AutoCloseable {
    private final List<AutoCloseable> resources = new ArrayList<>();
    
    @Override
    public void close() throws Exception {
        Exception first = null;
        
        // Close all resources in reverse order
        for (int i = resources.size() - 1; i >= 0; i--) {
            try {
                resources.get(i).close();
            } catch (Exception e) {
                if (first == null) {
                    first = e;
                } else {
                    first.addSuppressed(e);  // Add as suppressed exception
                }
            }
        }
        
        if (first != null) {
            throw first;  // Throw the first exception with any others suppressed
        }
    }
}
```

### 4. State Management

Track state and prevent use after closing:

```java
public class StateAwareResource implements AutoCloseable {
    private enum State { OPEN, CLOSING, CLOSED }
    private volatile State state = State.OPEN;
    
    @Override
    public void close() throws Exception {
        if (state == State.CLOSED) return;
        
        synchronized (this) {
            if (state == State.CLOSED) return;
            state = State.CLOSING;
            try {
                // Perform cleanup
            } finally {
                state = State.CLOSED;
            }
        }
    }
    
    public void use() {
        switch (state) {
            case CLOSED:
                throw new IllegalStateException("Resource is closed");
            case CLOSING:
                throw new IllegalStateException("Resource is closing");
            default:
                // Proceed with operation
        }
    }
}
```

### Key Takeaways for close() Implementation

1. **Idempotency**
   - Make `close()` safe to call multiple times
   - Return silently if already closed
   - Use a boolean flag to track closed state

2. **Thread Safety**
   - Use `synchronized` or other synchronization mechanisms if needed
   - Consider using volatile for the closed flag
   - Implement proper double-checked locking if required

3. **Exception Handling**
   - Document exceptions that might be thrown
   - Use suppressed exceptions when handling multiple resources
   - Clean up all resources even if some fail

4. **Resource Ordering**
   - Close resources in reverse order of creation
   - Handle dependencies between resources
   - Document any specific ordering requirements

5. **State Management**
   - Track resource state (open, closing, closed)
   - Prevent use of closed resources
   - Consider using enums for state management

6. **Documentation**
   - Document thread safety guarantees
   - Specify if close() is idempotent
   - Document any specific cleanup behavior

7. **Best Practices**
   - Keep the closed flag private
   - Make state checks fast and efficient
   - Consider implementing a dispose pattern for non-AutoCloseable resources

## Summary

1. **Don't use finalizers**
   - They are unpredictable and dangerous
   - They've been deprecated since Java 9

2. **Avoid cleaners**
   - Use them only as a safety net
   - Never rely on them for critical resource cleanup

3. **Prefer try-with-resources**
   - Implement AutoCloseable
   - Use try-with-resources for automatic cleanup
   - Handle resources explicitly and promptly

4. **Clean up promptly**
   - Don't rely on garbage collection for cleanup
   - Close resources as soon as you're done with them
