package scrap.heap.refactor.enums;

public enum Flavor {
	VANILLA ("vanilla"), 
	CHOCELATE ("chocelate");

	private final String flavorString;

    Flavor(final String flavorString) {
        this.flavorString = flavorString;
    }

    @Override
    public String toString() {
        return flavorString;
    }
}