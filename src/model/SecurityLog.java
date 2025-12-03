package model;

import java.time.LocalDateTime;

/**
 * SecurityLog class represents a security-related event log
 */
public class SecurityLog {

    private String logId;
    private String eventType; // "Login", "Logout", "Access_Denied", "Security_Breach", "System_Alert"
    private String severity; // "Low", "Medium", "High", "Critical"
    private String description;
    private String location; // e.g., "Main Gate", "Control Room", "Cell Block A"
    private LocalDateTime timestamp;
    private String employeeId; // Employee involved (if any)
    private String affectedEntity; // e.g., "Prisoner ID", "Cell Number", "System Module"
    private String ipAddress;
    private String status; // "Resolved", "Pending", "Investigating"
    private String resolutionNotes;
    private LocalDateTime resolvedAt;
    private String resolvedBy;

    /**
     * Constructor for SecurityLog
     */
    public SecurityLog(String logId, String eventType, String severity,
            String description, String location) {
        this.logId = logId;
        this.eventType = eventType;
        this.severity = severity;
        this.description = description;
        this.location = location;
        this.timestamp = LocalDateTime.now();
        this.status = "Pending";
    }

    /**
     * Full constructor
     */
    public SecurityLog(String logId, String eventType, String severity,
            String description, String location,
            String employeeId, String affectedEntity, String ipAddress) {
        this.logId = logId;
        this.eventType = eventType;
        this.severity = severity;
        this.description = description;
        this.location = location;
        this.timestamp = LocalDateTime.now();
        this.employeeId = employeeId;
        this.affectedEntity = affectedEntity;
        this.ipAddress = ipAddress;
        this.status = "Pending";
    }

    // Getters and Setters
    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
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

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getAffectedEntity() {
        return affectedEntity;
    }

    public void setAffectedEntity(String affectedEntity) {
        this.affectedEntity = affectedEntity;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResolutionNotes() {
        return resolutionNotes;
    }

    public void setResolutionNotes(String resolutionNotes) {
        this.resolutionNotes = resolutionNotes;
    }

    public LocalDateTime getResolvedAt() {
        return resolvedAt;
    }

    public void setResolvedAt(LocalDateTime resolvedAt) {
        this.resolvedAt = resolvedAt;
    }

    public String getResolvedBy() {
        return resolvedBy;
    }

    public void setResolvedBy(String resolvedBy) {
        this.resolvedBy = resolvedBy;
    }

    /**
     * Mark log as resolved
     */
    public void resolve(String resolvedBy, String resolutionNotes) {
        this.status = "Resolved";
        this.resolvedBy = resolvedBy;
        this.resolutionNotes = resolutionNotes;
        this.resolvedAt = LocalDateTime.now();
    }

    /**
     * Get formatted timestamp
     */
    public String getFormattedTimestamp() {
        return timestamp.toString().replace("T", " ");
    }

    /**
     * Check if log is critical
     */
    public boolean isCritical() {
        return "Critical".equals(severity);
    }

    /**
     * Check if log is resolved
     */
    public boolean isResolved() {
        return "Resolved".equals(status);
    }

    @Override
    public String toString() {
        return String.format("[%s] %s - %s (%s)",
                getFormattedTimestamp(),
                eventType,
                description,
                severity);
    }
}
