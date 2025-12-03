package model;

import java.time.LocalDateTime;

/**
 * SecurityAlert class represents a security alert notification
 */
public class SecurityAlert {

    private String alertId;
    private String alertType; // "Intrusion", "Equipment_Failure", "Unauthorized_Access", "Emergency"
    private String severity; // "Low", "Medium", "High", "Critical"
    private String description;
    private String location;
    private LocalDateTime triggeredAt;
    private LocalDateTime acknowledgedAt;
    private LocalDateTime resolvedAt;
    private String triggeredBy; // System or Employee ID
    private String acknowledgedBy;
    private String resolvedBy;
    private String status; // "Active", "Acknowledged", "Resolved"
    private String assignedTo; // Employee ID assigned to handle
    private String notes;
    private boolean requiresResponse;
    private int responseTime; // in minutes

    /**
     * Constructor for SecurityAlert
     */
    public SecurityAlert(String alertId, String alertType, String severity,
            String description, String location) {
        this.alertId = alertId;
        this.alertType = alertType;
        this.severity = severity;
        this.description = description;
        this.location = location;
        this.triggeredAt = LocalDateTime.now();
        this.status = "Active";
        this.requiresResponse = true;
        this.responseTime = 5; // Default 5 minutes
    }

    // Getters and Setters
    public String getAlertId() {
        return alertId;
    }

    public void setAlertId(String alertId) {
        this.alertId = alertId;
    }

    public String getAlertType() {
        return alertType;
    }

    public void setAlertType(String alertType) {
        this.alertType = alertType;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDateTime getTriggeredAt() {
        return triggeredAt;
    }

    public void setTriggeredAt(LocalDateTime triggeredAt) {
        this.triggeredAt = triggeredAt;
    }

    public LocalDateTime getAcknowledgedAt() {
        return acknowledgedAt;
    }

    public void setAcknowledgedAt(LocalDateTime acknowledgedAt) {
        this.acknowledgedAt = acknowledgedAt;
    }

    public LocalDateTime getResolvedAt() {
        return resolvedAt;
    }

    public void setResolvedAt(LocalDateTime resolvedAt) {
        this.resolvedAt = resolvedAt;
    }

    public String getTriggeredBy() {
        return triggeredBy;
    }

    public void setTriggeredBy(String triggeredBy) {
        this.triggeredBy = triggeredBy;
    }

    public String getAcknowledgedBy() {
        return acknowledgedBy;
    }

    public void setAcknowledgedBy(String acknowledgedBy) {
        this.acknowledgedBy = acknowledgedBy;
    }

    public String getResolvedBy() {
        return resolvedBy;
    }

    public void setResolvedBy(String resolvedBy) {
        this.resolvedBy = resolvedBy;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public boolean isRequiresResponse() {
        return requiresResponse;
    }

    public void setRequiresResponse(boolean requiresResponse) {
        this.requiresResponse = requiresResponse;
    }

    public int getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(int responseTime) {
        this.responseTime = responseTime;
    }

    /**
     * Acknowledge the alert
     */
    public void acknowledge(String employeeId) {
        this.status = "Acknowledged";
        this.acknowledgedBy = employeeId;
        this.acknowledgedAt = LocalDateTime.now();
    }

    /**
     * Resolve the alert
     */
    public void resolve(String employeeId, String notes) {
        this.status = "Resolved";
        this.resolvedBy = employeeId;
        this.resolvedAt = LocalDateTime.now();
        this.notes = notes;
    }

    /**
     * Assign alert to employee
     */
    public void assignTo(String employeeId) {
        this.assignedTo = employeeId;
    }

    /**
     * Check if alert is active
     */
    public boolean isActive() {
        return "Active".equals(status);
    }

    /**
     * Check if alert requires immediate attention
     */
    public boolean requiresImmediateAttention() {
        return "Critical".equals(severity) && isActive();
    }

    /**
     * Get formatted triggered time
     */
    public String getFormattedTriggeredAt() {
        return triggeredAt.toString().replace("T", " ");
    }

    /**
     * Calculate response time in minutes
     */
    public int calculateResponseTime() {
        if (acknowledgedAt != null) {
            return (int) java.time.Duration.between(triggeredAt, acknowledgedAt).toMinutes();
        }
        return -1; // Not acknowledged yet
    }
}
