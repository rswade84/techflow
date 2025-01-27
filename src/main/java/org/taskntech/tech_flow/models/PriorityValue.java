package org.taskntech.tech_flow.models;

public enum PriorityValue {

    HIGH(2),
    MEDIUM(1),
    LOW(0);

    private final int displayPriority;

    //sets displayPriority to 2,1,0
    private PriorityValue(int value) {
        this.displayPriority = value;
    }

    //get int value of Enum for display for sort
    public int getPriority() {
        return displayPriority;
    }

    @Override
    public String toString() {
        return this.name(); // Returns "HIGH", "MEDIUM", "LOW"
    }
}