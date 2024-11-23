package chapter2.item2_builder_pattern;

import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;

/**
 * Demonstrates a hierarchical builder pattern.
 * This example shows how the Builder pattern can be used with class hierarchies.
 */
public abstract class Pizza {
    public enum Topping { HAM, MUSHROOM, ONION, PEPPER, SAUSAGE }
    final Set<Topping> toppings;

    abstract static class Builder<T extends Builder<T>> {
        EnumSet<Topping> toppings = EnumSet.noneOf(Topping.class);
        private int maxToppings = 6; // Maximum number of toppings allowed

        public T addTopping(Topping topping) {
            if (toppings.size() >= maxToppings) {
                throw new IllegalStateException("Cannot add more than " + maxToppings + " toppings");
            }
            toppings.add(Objects.requireNonNull(topping));
            return self();
        }

        public T addToppings(Topping... toppings) {
            for (Topping topping : toppings) {
                addTopping(topping);
            }
            return self();
        }

        protected void setMaxToppings(int max) {
            if (max < 1) {
                throw new IllegalArgumentException("Max toppings must be positive");
            }
            this.maxToppings = max;
        }

        abstract Pizza build();

        // Subclasses must override this method to return "this"
        protected abstract T self();
    }

    Pizza(Builder<?> builder) {
        toppings = builder.toppings.clone();
    }
}

// Concrete Pizza class: NyPizza
class NyPizza extends Pizza {
    public enum Size { SMALL, MEDIUM, LARGE }
    final Size size;

    public static class Builder extends Pizza.Builder<Builder> {
        private final Size size;

        public Builder(Size size) {
            this.size = Objects.requireNonNull(size);
        }

        @Override
        public NyPizza build() {
            return new NyPizza(this);
        }

        @Override
        protected Builder self() {
            return this;
        }
    }

    private NyPizza(Builder builder) {
        super(builder);
        size = builder.size;
    }
}

// Concrete Pizza class: Calzone
class Calzone extends Pizza {
    final boolean sauceInside;

    public static class Builder extends Pizza.Builder<Builder> {
        private boolean sauceInside = false;  // default

        public Builder sauceInside() {
            sauceInside = true;
            return this;
        }

        @Override
        public Calzone build() {
            return new Calzone(this);
        }

        @Override
        protected Builder self() {
            return this;
        }
    }

    private Calzone(Builder builder) {
        super(builder);
        sauceInside = builder.sauceInside;
    }
}

// Concrete Pizza class: ChicagoPizza
class ChicagoPizza extends Pizza {
    public enum CrustThickness { THIN, MEDIUM, THICK }
    final CrustThickness crustThickness;
    final boolean extraCheese;
    final boolean stuffedCrust;

    public static class Builder extends Pizza.Builder<Builder> {
        private CrustThickness crustThickness = CrustThickness.MEDIUM;
        private boolean extraCheese = false;
        private boolean stuffedCrust = false;

        public Builder() {
            setMaxToppings(8); // Chicago style allows more toppings
        }

        public Builder crustThickness(CrustThickness thickness) {
            this.crustThickness = Objects.requireNonNull(thickness);
            return this;
        }

        public Builder extraCheese() {
            this.extraCheese = true;
            return this;
        }

        public Builder stuffedCrust() {
            if (crustThickness == CrustThickness.THIN) {
                throw new IllegalStateException("Cannot have stuffed crust with thin crust");
            }
            this.stuffedCrust = true;
            return this;
        }

        @Override
        public ChicagoPizza build() {
            if (toppings.isEmpty()) {
                throw new IllegalStateException("Chicago pizza must have at least one topping");
            }
            return new ChicagoPizza(this);
        }

        @Override
        protected Builder self() {
            return this;
        }
    }

    private ChicagoPizza(Builder builder) {
        super(builder);
        crustThickness = builder.crustThickness;
        extraCheese = builder.extraCheese;
        stuffedCrust = builder.stuffedCrust;
    }

    @Override
    public String toString() {
        return "ChicagoPizza{" +
                "toppings=" + toppings +
                ", crustThickness=" + crustThickness +
                ", extraCheese=" + extraCheese +
                ", stuffedCrust=" + stuffedCrust +
                '}';
    }
} 