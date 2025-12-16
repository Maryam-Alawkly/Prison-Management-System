package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * SecurityAlert class represents a security alert in the prison management
 * system. This class encapsulates alert information including type, severity,
 * status, and lifecycle timestamps. It follows the Single Responsibility
 * Principle by focusing only on alert data representation and validation.
 */
public class SecurityAlert {

    // Alert status constants
    public static final String STATUS_ACTIVE = "Active";
    public static final String STATUS_ACKNOWLEDGED = "Acknowledged";
    public static final String STATUS_RESOLVED = "Resolved";

    // Severity level constants
    public static final String SEVERITY_LOW = "Low";
    public static final String SEVERITY_MEDIUM = "Medium";
    public static final String SEVERITY_HIGH = "High";
    public static final String SEVERITY_CRITICAL = "Critical";

    // Alert type constants
    public static final String TYPE_UNAUTHORIZED_ACCESS = "Unauthorized Access";
    public static final String TYPE_SECURITY_BREACH = "Security Breach";
    public static final String TYPE_SYSTEM_ALERT = "System Alert";
    public static final String TYPE_EMERGENCY = "Emergency";
    public static final String TYPE_EQUIPMENT_FAILURE = "Equipment Failure";

    // Alert properties
    private String alertId;
    private String alertType;
    private String severity;
    private String description;
    private String location;
    private String status;
    private String triggeredBy;
    private LocalDateTime triggeredAt;
    private String acknowledgedBy;
    private LocalDateTime acknowledgedAt;
    private String resolvedBy;
    private LocalDateTime resolvedAt;
    private String assignedTo;
    private String resolutionNotes;

    /**
     * Default constructor initializes a SecurityAlert with default values. Use
     * this constructor when creating alerts to be populated from database or
     * user input.
     */
    public SecurityAlert() {
        // Default constructor for frameworks and manual instantiation
    }

    /**
     * Constructs a SecurityAlert with basic alert information. Sets default
     * status to "Active" and triggered timestamp to current time.
     *
     * @param alertId Unique identifier for the alert
     * @param alertType Type of security alert
     * @param severity Severity level of the alert
     * @param description Detailed description of the alert
     * @param location Location where alert was triggered
     */
    public SecurityAlert(String alertId, String alertType, String severity,
            String description, String location) {
        this.alertId = alertId;
        this.alertType = alertType;
        this.severity = severity;
        this.description = description;
        this.location = location;
        this.status = STATUS_ACTIVE;
        this.triggeredAt = LocalDateTime.now();
    }

    /**
     * Gets the unique alert identifier.
     *
     * @return The alert ID
     */
    public String getAlertId() {
        return alertId;
    }

    /**
     * Sets the unique alert identifier.
     *
     * @param alertId The alert ID to set
     */
    public void setAlertId(String alertId) {
        this.alertId = alertId;
    }

    /**
     * Gets the type of security alert.
     *
     * @return The alert type
     */
    public String getAlertType() {
        return alertType;
    }

    /**
     * Sets the type of security alert.
     *
     * @param alertType The alert type to set
     */
    public void setAlertType(String alertType) {
        this.alertType = alertType;
    }

    /**
     * Gets the severity level of the alert.
     *
     * @return The severity level
     */
    public String getSeverity() {
        return severity;
    }

    /**
     * Sets the severity level of the alert.
     *
     * @param severity The severity level to set
     */
    public void setSeverity(String severity) {
        this.severity = severity;
    }

    /**
     * Gets the detailed description of the alert.
     *
     * @return The alert description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the detailed description of the alert.
     *
     * @param description The alert description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the location where the alert was triggered.
     *
     * @return The alert location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the location where the alert was triggered.
     *
     * @param location The alert location to set
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Gets the timestamp when the alert was triggered.
     *
     * @return The trigger timestamp
     */
    public LocalDateTime getTriggeredAt() {
        return triggeredAt;
    }

    /**
     * Sets the timestamp when the alert was triggered.
     *
     * @param triggeredAt The trigger timestamp to set
     */
    public void setTriggeredAt(LocalDateTime triggeredAt) {
        this.triggeredAt = triggeredAt;
    }

    /**
     * Gets the user who triggered the alert.
     *
     * @return The user who triggered the alert
     */
    public String getTriggeredBy() {
        return triggeredBy;
    }

    /**
     * Sets the user who triggered the alert.
     *
     * @param triggeredBy The user who triggered the alert
     */
    public void setTriggeredBy(String triggeredBy) {
        this.triggeredBy = triggeredBy;
    }

    /**
     * Gets the current status of the alert.
     *
     * @return The alert status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the current status of the alert.
     *
     * @param status The alert status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Gets the timestamp when the alert was acknowledged.
     *
     * @return The acknowledgment timestamp, or null if not acknowledged
     */
    public LocalDateTime getAcknowledgedAt() {
        return acknowledgedAt;
    }

    /**
     * Sets the timestamp when the alert was acknowledged.
     *
     * @param acknowledgedAt The acknowledgment timestamp to set
     */
    public void setAcknowledgedAt(LocalDateTime acknowledgedAt) {
        this.acknowledgedAt = acknowledgedAt;
    }

    /**
     * Gets the user who acknowledged the alert.
     *
     * @return The user who acknowledged the alert, or null if not acknowledged
     */
    public String getAcknowledgedBy() {
        return acknowledgedBy;
    }

    /**
     * Sets the user who acknowledged the alert.
     *
     * @param acknowledgedBy The user who acknowledged the alert
     */
    public void setAcknowledgedBy(String acknowledgedBy) {
        this.acknowledgedBy = acknowledgedBy;
    }

    /**
     * Gets the timestamp when the alert was resolved.
     *
     * @return The resolution timestamp, or null if not resolved
     */
    public LocalDateTime getResolvedAt() {
        return resolvedAt;
    }

    /**
     * Sets the timestamp when the alert was resolved.
     *
     * @param resolvedAt The resolution timestamp to set
     */
    public void setResolvedAt(LocalDateTime resolvedAt) {
        this.resolvedAt = resolvedAt;
    }

    /**
     * Gets the user who resolved the alert.
     *
     * @return The user who resolved the alert, or null if not resolved
     */
    public String getResolvedBy() {
        return resolvedBy;
    }

    /**
     * Sets the user who resolved the alert.
     *
     * @param resolvedBy The user who resolved the alert
     */
    public void setResolvedBy(String resolvedBy) {
        this.resolvedBy = resolvedBy;
    }

    /**
     * Gets the resolution notes describing how the alert was resolved.
     *
     * @return The resolution notes, or null if not resolved
     */
    public String getResolutionNotes() {
        return resolutionNotes;
    }

    /**
     * Sets the resolution notes describing how the alert was resolved.
     *
     * @param resolutionNotes The resolution notes to set
     */
    public void setResolutionNotes(String resolutionNotes) {
        this.resolutionNotes = resolutionNotes;
    }

    /**
     * Gets the user assigned to handle this alert.
     *
     * @return The assigned user, or null if not assigned
     */
    public String getAssignedTo() {
        return assignedTo;
    }

    /**
     * Sets the user assigned to handle this alert.
     *
     * @param assignedTo The user to assign
     */
    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    /**
     * Gets the triggered timestamp in a formatted string.
     *
     * @return Formatted trigger timestamp, or "Not Triggered" if null
     */
    public String getFormattedTriggeredAt() {
        if (triggeredAt != null) {
            return triggeredAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
        return "Not Triggered";
    }

    /**
     * Gets the acknowledged timestamp in a formatted string.
     *
     * @return Formatted acknowledgment timestamp, or "Not Acknowledged" if null
     */
    public String getFormattedAcknowledgedAt() {
        if (acknowledgedAt != null) {
            return acknowledgedAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
        return "Not Acknowledged";
    }

    /**
     * Gets the resolved timestamp in a formatted string.
     *
     * @return Formatted resolution timestamp, or "Not Resolved" if null
     */
    public String getFormattedResolvedAt() {
        if (resolvedAt != null) {
            return resolvedAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
        return "Not Resolved";
    }

    /**
     * Checks if the alert is currently active.
     *
     * @return True if alert status is "Active", false otherwise
     */
    public boolean isActive() {
        return STATUS_ACTIVE.equals(this.status);
    }

    /**
     * Checks if the alert has been acknowledged.
     *
     * @return True if alert status is "Acknowledged", false otherwise
     */
    public boolean isAcknowledged() {
        return STATUS_ACKNOWLEDGED.equals(this.status);
    }

    /**
     * Checks if the alert has been resolved.
     *
     * @return True if alert status is "Resolved", false otherwise
     */
    public boolean isResolved() {
        return STATUS_RESOLVED.equals(this.status);
    }

    /**
     * Checks if the alert configuration is valid. Validates all required fields
     * are properly set.
     *
     * @return True if alert configuration is valid, false otherwise
     */
    public boolean isValid() {
        return alertId != null && !alertId.trim().isEmpty()
                && alertType != null && !alertType.trim().isEmpty()
                && severity != null && !severity.trim().isEmpty()
                && description != null && !description.trim().isEmpty()
                && location != null && !location.trim().isEmpty()
                && status != null && !status.trim().isEmpty()
                && triggeredAt != null;
    }

    /**
     * Checks if this alert has higher severity than the specified severity.
     *
     * @param otherSeverity Severity to compare against
     * @return True if this alert has higher severity, false otherwise
     */
    public boolean hasHigherSeverityThan(String otherSeverity) {
        int thisPriority = getSeverityPriority(this.severity);
        int otherPriority = getSeverityPriority(otherSeverity);
        return thisPriority > otherPriority;
    }

    /**
     * Calculates the age of the alert in minutes.
     *
     * @return Age in minutes, or -1 if triggeredAt is null
     */
    public long getAlertAgeInMinutes() {
        if (triggeredAt == null) {
            return -1;
        }
        return java.time.Duration.between(triggeredAt, LocalDateTime.now()).toMinutes();
    }

    /**
     * Determines if the alert needs immediate attention. Critical and High
     * severity alerts need immediate attention.
     *
     * @return True if alert needs immediate attention, false otherwise
     */
    public boolean needsImmediateAttention() {
        return SEVERITY_CRITICAL.equals(this.severity) || SEVERITY_HIGH.equals(this.severity);
    }

    /**
     * Returns a string representation of the alert. Provides a concise summary
     * for display purposes.
     *
     * @return String representation of the alert
     */
    @Override
    public String toString() {
        return String.format("SecurityAlert[ID=%s, Type=%s, Severity=%s, Status=%s, Location=%s]",
                alertId, alertType, severity, status, location);
    }

    /**
     * Compares this alert with another object for equality. Two alerts are
     * equal if they have the same alert ID.
     *
     * @param obj Object to compare with
     * @return True if objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        SecurityAlert that = (SecurityAlert) obj;
        return Objects.equals(alertId, that.alertId);
    }

    /**
     * Returns the hash code for this alert. Based on the alert ID.
     *
     * @return Hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(alertId);
    }

    /**
     * Gets the priority value for a severity level. Used for severity
     * comparison.
     *
     * @param severity Severity level
     * @return Priority value (higher = more severe)
     */
    private int getSeverityPriority(String severity) {
        switch (severity) {
            case SEVERITY_CRITICAL:
                return 4;
            case SEVERITY_HIGH:
                return 3;
            case SEVERITY_MEDIUM:
                return 2;
            case SEVERITY_LOW:
                return 1;
            default:
                return 0;
        }
    }

    /**
     * Gets the duration between triggered and resolved times.
     *
     * @return Duration in minutes, or -1 if either timestamp is null
     */
    public long getResolutionDurationInMinutes() {
        if (triggeredAt == null || resolvedAt == null) {
            return -1;
        }
        return java.time.Duration.between(triggeredAt, resolvedAt).toMinutes();
    }

    /**
     * Determines if the alert is overdue based on severity. Critical alerts are
     * overdue after 5 minutes, High after 15, Medium after 30, Low after 60.
     *
     * @return True if alert is overdue, false otherwise
     */
    public boolean isOverdue() {
        if (isResolved() || triggeredAt == null) {
            return false;
        }

        long ageInMinutes = getAlertAgeInMinutes();
        int threshold = getSeverityResponseThreshold(this.severity);

        return ageInMinutes > threshold;
    }

    /**
     * Gets the response time threshold in minutes for a severity level.
     *
     * @param severity Severity level
     * @return Response threshold in minutes
     */
    private int getSeverityResponseThreshold(String severity) {
        switch (severity) {
            case SEVERITY_CRITICAL:
                return 5;    // 5 minutes for critical
            case SEVERITY_HIGH:
                return 15;       // 15 minutes for high
            case SEVERITY_MEDIUM:
                return 30;     // 30 minutes for medium
            case SEVERITY_LOW:
                return 60;        // 60 minutes for low
            default:
                return 60;                  // Default 60 minutes
        }
    }

    /**
     * Creates a summary of the alert for quick reference.
     *
     * @return Alert summary string
     */
    public String getSummary() {
        return String.format("%s: %s (Severity: %s, Status: %s)",
                alertType, description, severity, status);
    }
}
