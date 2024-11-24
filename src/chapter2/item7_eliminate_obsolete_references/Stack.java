package chapter2.item7_eliminate_obsolete_references;

import java.util.Arrays;
import java.util.EmptyStackException;

// A simple stack implementation demonstrating memory leak issues
public class Stack {
    private Object[] elements;
    private int size = 0;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    public Stack() {
        elements = new Object[DEFAULT_INITIAL_CAPACITY];
    }

    public void push(Object e) {
        ensureCapacity();
        elements[size++] = e;
    }

    // Version that causes memory leak
    public Object pop() {
        if (size == 0)
            throw new EmptyStackException();
        return elements[--size]; // Obsolete reference not cleared
    }

    // Corrected version that fixes memory leak
    public Object popFixed() {
        if (size == 0)
            throw new EmptyStackException();
        Object result = elements[--size];
        elements[size] = null; // Clear obsolete reference
        return result;
    }

    /**
     * Ensure space for at least one more element, roughly
     * doubling the capacity each time the array needs to grow.
     */
    private void ensureCapacity() {
        if (elements.length == size)
            elements = Arrays.copyOf(elements, 2 * size + 1);
    }

    // Added for testing
    public int getSize() {
        return size;
    }

    public int getCapacity() {
        return elements.length;
    }

    // For debugging
    public Object[] getInternalArray() {
        return Arrays.copyOf(elements, elements.length);
    }
}
