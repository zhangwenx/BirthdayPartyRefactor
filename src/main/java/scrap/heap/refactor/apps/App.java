package scrap.heap.refactor.apps;

import java.util.*; 

import scrap.heap.refactor.enums.*;
import scrap.heap.refactor.items.*;

public class App {


    public String getGreeting() {
        return "Hello world.";
    }

    public static void main(String[] args) {

        Hashtable<Balloon, Cake> orders = new Hashtable<Balloon, Cake>();

        // 1st order:
        Balloon balloon1 = new Balloon(Color.RED, Material.MYLAR, 4);
        Cake cake1 = new Cake(Flavor.CHOCELATE, Flavor.CHOCELATE, Shape.CIRCLE, Size.LARGE, Color.BROWN);
        order(balloon1, cake1);

        // 2nd order:
        Balloon balloon2 = new Balloon(Color.BLUE, Material.LATEX, 7);
        Cake cake2 = new Cake(Flavor.VANILLA, Flavor.CHOCELATE, Shape.SQUARE, Size.MEDIUM, Color.BROWN);
        order(balloon2, cake2);

        // 3rd order:
        Balloon balloon3 = new Balloon(Color.YELLOW, Material.MYLAR, 4);
        Cake cake3 = new Cake(Flavor.VANILLA, Flavor.VANILLA, Shape.SQUARE, Size.SMALL, Color.YELLOW);
        order(balloon3, cake3);
    }

    private static void order(Balloon balloon, Cake cake){
        orderBalloons(balloon);
        orderCake(cake);
    }

    private static void orderBalloons(Balloon balloon){

        //for the purposes of this exercise, pretend this method works and adds balloons to the order
        System.out.println("Balloons ordered; " + balloon.balloonColor + ", " + balloon.material + ", " + balloon.number);

    }

    private static void orderCake(Cake cake){

        //for the purposes of this exercise, pretend that this method adds a cake to the order
        System.out.println("Cake ordered; " + cake.flavor + ", " + cake.frostingFlavor + ", " + cake.shape + ", " + cake.size + ", " + cake.color);

    }

}
