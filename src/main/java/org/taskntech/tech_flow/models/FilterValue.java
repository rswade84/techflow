package org.taskntech.tech_flow.models;

public enum FilterValue {

    OLDEST(5),
    YOUNGEST(1),
    HIGHEST_PRIORITY(0),
    LOWEST_PRIORITY(4),
    CLOSED(1),
    DEFAULT(2);

    private final int displayFilter;

    private FilterValue(int value) {
        this.displayFilter = value;
    }

    public int getPriority() {
        return displayFilter;
    }

    @Override
    public String toString() {
        return this.name(); // Returns "HIGH", "MEDIUM", "LOW"
    }
}
