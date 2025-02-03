package org.taskntech.tech_flow.models;

public enum StatusUpdates {
    NOT_STARTED("Not Started"),
    IN_PROGRESS("In Progress"),
    DELAYED("Delayed"),
    RESOLVED("Resolved"),
    CLOSED("Closed");

    private final String displayStatus;

    //Sets displayStatus to Not started, in progress ...etc
    private StatusUpdates(String value){
        this.displayStatus=value;
    }

    //get String value of Enum for display
    public String getDisplayStatus(){
        return displayStatus;
    }

    // toString method to return string
    @Override
    public String toString() {
        return name();
    }
}
