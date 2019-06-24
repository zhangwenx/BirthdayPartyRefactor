package scrap.heap.refactor.enums;

public enum Material {
    MYLAR("mylar"), 
    LATEX("latex");

	private final String materialString;

    Material(final String materialString) {
        this.materialString = materialString;
    }

    @Override
    public String toString() {
        return materialString;
    }
}