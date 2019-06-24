package scrap.heap.refactor.items;

import scrap.heap.refactor.enums.*;

public class Cake {
    public String flavor;
    public String frostingFlavor;
    public String shape;
    public String size;
    public String color;

    public Cake(Flavor flavor, Flavor frostingFlavor, Shape shape, Size size, Color color) {
        this.flavor = flavor.toString();
        this.frostingFlavor = frostingFlavor.toString();
        this.shape = shape.toString();
        this.size = size.toString();
        this.color = color.toString();
    }
}