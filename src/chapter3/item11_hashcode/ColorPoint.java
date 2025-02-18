package chapter3.item11_hashcode;

import java.awt.Color;

public class ColorPoint extends Point {
    private final Color color;

    public ColorPoint(int x, int y, Color color) {
        super(x, y);
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ColorPoint)) return false;
        ColorPoint cp = (ColorPoint) o;
        return super.equals(o) && Objects.equals(color, cp.color);
    }

    @Override
    public int hashCode() {
        return 31 * super.hashCode() + Objects.hashCode(color);
    }
}