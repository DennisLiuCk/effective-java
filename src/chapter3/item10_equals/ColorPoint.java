package chapter3.item10_equals;

import java.awt.Color;

/**
 * Example demonstrating the difficulty of preserving the equals contract
 * when adding a value component to a subclass.
 * 
 * This example shows why inheritance can break equals contract and why
 * composition is often a better solution than inheritance for adding a
 * value component to a class.
 */
public class ColorPoint {
    private final Point point;
    private final Color color;

    public ColorPoint(int x, int y, Color color) {
        this.point = new Point(x, y);
        this.color = color;
    }

    /**
     * Returns the point-view of this color point.
     */
    public Point asPoint() {
        return point;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ColorPoint)) {
            return false;
        }
        ColorPoint cp = (ColorPoint) obj;
        return cp.point.equals(point) && cp.color.equals(color);
    }

    @Override
    public int hashCode() {
        int result = point.hashCode();
        result = 31 * result + color.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return String.format("ColorPoint[x=%s, color=%s]", point, color);
    }

    // Example of violating equals contract through inheritance
    // DO NOT DO THIS!
    static class InheritanceColorPoint extends Point {
        private final Color color;

        public InheritanceColorPoint(int x, int y, Color color) {
            super(x, y);
            this.color = color;
        }

        // Violates symmetry!
        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Point)) {
                return false;
            }

            // If obj is a normal point, do a color-blind comparison
            if (!(obj instanceof InheritanceColorPoint)) {
                return obj.equals(this);
            }

            // Do a full comparison
            return super.equals(obj) && 
                   ((InheritanceColorPoint) obj).color.equals(color);
        }
    }
}
