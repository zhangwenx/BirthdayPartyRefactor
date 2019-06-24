package scrap.heap.refactor.enums;

public enum Size {
	LARGE ("large"), 
	MEDIUM ("med"),
    SMALL ("small");

	private final String sizeString;

    Size(final String sizeString) {
        this.sizeString = sizeString;
    }

    @Override
    public String toString() {
        return sizeString;
    }
}