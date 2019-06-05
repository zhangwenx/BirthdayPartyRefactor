package scrap.heap.refactor;

public class App {
    public String getGreeting() {
        return "Hello world.";
    }

    public static void main(String[] args) {

         //Place birthday party orders
         order("red", "mylar", "4", "chocolate", "chocolate", "circle", "large", "brown" );
         order("blue", "latex", "7", "Vanilla", "chocelate", "square", "med", "brown" );
         order("yellow", "mylar", "4", "vanilla", "vanilla", "square", "small", "yellow" );

    }

    private static void order(String balloonColor, String material, String number, String flavor, String frostingFlavor, String shape, String size, String cakeColor){

        orderBalloons(balloonColor, material, number);

        orderCake(frostingFlavor, flavor, shape, size, cakeColor);
    }

    private static void orderBalloons(String balloonColor, String material, String number){

        //for the purposes of this exercise, pretend this method works and adds balloons to the order
        System.out.println("Balloons ordered; " + balloonColor + ", " + material  + ", " + number);

    }

    private static void orderCake(String flavor, String frostingFlavor, String shape, String size, String cakeColor){

        //for the purposes of this exercise, pretend that this method adds a cake to the order
        System.out.println("cake ordered; " + flavor + ", " + frostingFlavor  + ", " + shape + ", " + size + ", " + cakeColor);

    }

}
