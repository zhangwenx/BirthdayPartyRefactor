package scrap.heap.refactor.items;

import scrap.heap.refactor.enums.*;

public class Balloon {
    public String balloonColor;
    public String material;
    public int number;

    public Balloon(Color balloonColor, Material material, int number) {
        this.balloonColor = balloonColor.toString();
        this.material = material.toString();
        this.number = number;
    }
}