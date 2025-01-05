package fr.imt.coffee.storage.container;

import fr.imt.coffee.storage.type.CoffeeType;

public class CoffeeMug extends CoffeeContainer{

    public CoffeeMug(double capacity, CoffeeType coffeeType) {
        super(capacity, coffeeType);
    }

    public CoffeeMug(Mug mug, CoffeeType coffeeType) {
        super(mug, coffeeType);
    }
}
