package scrap.heap.refactor.enums;

public enum Shape {
	CIRCLE ("circle"), 
	SQUARE ("square");

	private final String shapeString;

    Shape(final String shapeString) {
        this.shapeString = shapeString;
    }

    @Override
    public String toString() {
        return shapeString;
    }
}