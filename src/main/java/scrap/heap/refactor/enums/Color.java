package scrap.heap.refactor.enums;

public enum Color {
	RED ("red"), 
	BLUE ("blue"), 
	YELLOW ("yellow"),
	BROWN ("brown");

	private final String colorString;

    Color(final String colorString) {
        this.colorString = colorString;
    }

    @Override
    public String toString() {
        return colorString;
    }
}