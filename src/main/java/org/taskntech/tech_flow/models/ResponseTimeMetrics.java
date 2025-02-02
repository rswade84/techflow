package org.taskntech.tech_flow.models;


public class ResponseTimeMetrics {

        // Setup response time metrics with average initial response time and average resolution time
        private final double averageInitialResponseTime;
        private final double averageResolutionTime;

        // Constructor that takes average initial response time and average resolution time
        public ResponseTimeMetrics(double averageInitialResponseTime, double averageResolutionTime) {
                this.averageInitialResponseTime = averageInitialResponseTime;
                this.averageResolutionTime = averageResolutionTime;
        }

        // Method that returns "Xh Ym" formatted string (hours/mins)
        public String getFormattedInitialResponseTime() {
                long hours = (long) averageInitialResponseTime;
                long minutes = (long) ((averageInitialResponseTime - hours) * 60);
                return String.format("%dh %dm", hours, minutes);
        }

        // Get the formatted string for resolution time
        public String getFormattedResolutionTime() {
                long hours = (long) averageResolutionTime;
                long minutes = (long) ((averageResolutionTime - hours) * 60);
                return String.format("%dh %dm", hours, minutes);
        }
}
