package chapter3.item10_equals;

/**
 * Example class demonstrating proper equals() implementation.
 * 
 * The equals method implements an equivalence relation:
 * 1. Reflexive: x.equals(x) returns true
 * 2. Symmetric: x.equals(y) returns true if and only if y.equals(x) returns true
 * 3. Transitive: if x.equals(y) and y.equals(z), then x.equals(z)
 * 4. Consistent: multiple invocations of x.equals(y) consistently return true or false
 * 5. Non-nullity: x.equals(null) returns false
 */
public class Point {
    private final int x;
    private final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        // 1. Use == operator to check if argument is a reference to this object
        if (obj == this) {
            return true;
        }

        // 2. Use instanceof to check if argument has the correct type
        if (!(obj instanceof Point)) {
            return false;
        }

        // 3. Cast argument to the correct type
        Point p = (Point) obj;

        // 4. Check if significant fields are equal
        return x == p.x && y == p.y;
    }

    // Always override hashCode when you override equals
    @Override
    public int hashCode() {
        int result = Integer.hashCode(x);
        result = 31 * result + Integer.hashCode(y);
        return result;
    }

    @Override
    public String toString() {
        return String.format("Point[x=%d, y=%d]", x, y);
    }
}
