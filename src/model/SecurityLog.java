package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * SecurityLog class represents a security-related event log in the prison
 * management system. This class tracks security events including logins, access
 * attempts, breaches, and system alerts. It provides comprehensive logging and
 * resolution tracking capabilities.
 */
public class SecurityLog {

    // Event type constants
    public static final String EVENT_LOGIN = "Login";
    public static final String EVENT_LOGOUT = "Logout";
    public static final String EVENT_ACCESS_DENIED = "Access_Denied";
    public static final String EVENT_SECURITY_BREACH = "Security_Breach";
    public static final String EVENT_SYSTEM_ALERT = "System_Alert";
    public static final String EVENT_DATA_MODIFICATION = "Data_Modification";
    public static final String EVENT_CONFIGURATION_CHANGE = "Configuration_Change";

    // Severity level constants
    public static final String SEVERITY_LOW = "Low";
    public static final String SEVERITY_MEDIUM = "Medium";
    public static final String SEVERITY_HIGH = "High";
    public static final String SEVERITY_CRITICAL = "Critical";

    // Status constants
    public static final String STATUS_ACTIVE = "Active";
    public static final String STATUS_PENDING = "Pending";
    public static final String STATUS_INVESTIGATING = "Investigating";
    public static final String STATUS_RESOLVED = "Resolved";

    // Date/time formatter for consistent formatting
    private static final DateTimeFormatter TIMESTAMP_FORMATTER
            = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Log properties
    private String logId;
    private String eventType;
    private String severity;
    private String description;
    private String location;
    private LocalDateTime timestamp;
    private String employeeId;
    private String affectedEntity;
    private String ipAddress;
    private String status;
    private String resolutionNotes;
    private LocalDateTime resolvedAt;
    private String resolvedBy;

    /**
     * Default constructor for JavaBeans compatibility. Initializes timestamp to
     * current time and status to "Active".
     */
    public SecurityLog() {
        this.timestamp = LocalDateTime.now();
        this.status = STATUS_ACTIVE;
    }

    /**
     * Basic constructor for SecurityLog with essential parameters. Initializes
     * timestamp to current time and status to "Active".
     *
     * @param logId Unique identifier for the log entry
     * @param eventType Type of security event
     * @param severity Severity level of the event
     * @param description Detailed description of the event
     * @param location Location where event occurred
     */
    public SecurityLog(String logId, String eventType, String severity,
            String description, String location) {
        this.logId = logId;
        this.eventType = eventType;
        this.severity = severity;
        this.description = description;
        this.location = location;
        this.timestamp = LocalDateTime.now();
        this.status = STATUS_ACTIVE;
    }

    /**
     * Extended constructor for SecurityLog with additional parameters. Includes
     * employee, affected entity, and IP address information.
     *
     * @param logId Unique identifier for the log entry
     * @param eventType Type of security event
     * @param severity Severity level of the event
     * @param description Detailed description of the event
     * @param location Location where event occurred
     * @param employeeId ID of employee involved in the event
     * @param affectedEntity Entity affected by the event
     * @param ipAddress IP address associated with the event
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
        this.status = STATUS_ACTIVE;
    }

    /**
     * Complete constructor for SecurityLog with all parameters. Allows
     * specification of all properties including resolution details.
     *
     * @param logId Unique identifier for the log entry
     * @param eventType Type of security event
     * @param severity Severity level of the event
     * @param description Detailed description of the event
     * @param location Location where event occurred
     * @param timestamp When the event occurred (null for current time)
     * @param employeeId ID of employee involved in the event
     * @param affectedEntity Entity affected by the event
     * @param ipAddress IP address associated with the event
     * @param status Current status of the log entry (null for "Active")
     * @param resolutionNotes Notes about how the event was resolved
     * @param resolvedBy Who resolved the event
     * @param resolvedAt When the event was resolved
     */
    public SecurityLog(String logId, String eventType, String severity,
            String description, String location, LocalDateTime timestamp,
            String employeeId, String affectedEntity, String ipAddress,
            String status, String resolutionNotes, String resolvedBy,
            LocalDateTime resolvedAt) {
        this.logId = logId;
        this.eventType = eventType;
        this.severity = severity;
        this.description = description;
        this.location = location;
        this.timestamp = timestamp != null ? timestamp : LocalDateTime.now();
        this.employeeId = employeeId;
        this.affectedEntity = affectedEntity;
        this.ipAddress = ipAddress;
        this.status = status != null ? status : STATUS_ACTIVE;
        this.resolutionNotes = resolutionNotes;
        this.resolvedBy = resolvedBy;
        this.resolvedAt = resolvedAt;
    }

    // =================== Getter Methods ===================
    /**
     * Gets the unique log identifier.
     *
     * @return The log ID
     */
    public String getLogId() {
        return logId;
    }

    /**
     * Gets the type of security event.
     *
     * @return The event type
     */
    public String getEventType() {
        return eventType;
    }

    /**
     * Gets the severity level of the event.
     *
     * @return The severity level
     */
    public String getSeverity() {
        return severity;
    }

    /**
     * Gets the detailed description of the event.
     *
     * @return The description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the location where the event occurred.
     *
     * @return The location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Gets the timestamp when the event occurred.
     *
     * @return The event timestamp
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Gets the ID of the employee involved in the event.
     *
     * @return The employee ID, or null if not applicable
     */
    public String getEmployeeId() {
        return employeeId;
    }

    /**
     * Gets the entity affected by the event.
     *
     * @return The affected entity, or null if not applicable
     */
    public String getAffectedEntity() {
        return affectedEntity;
    }

    /**
     * Gets the IP address associated with the event.
     *
     * @return The IP address, or null if not applicable
     */
    public String getIpAddress() {
        return ipAddress;
    }

    /**
     * Gets the current status of the log entry.
     *
     * @return The status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Gets the resolution notes for the event.
     *
     * @return The resolution notes, or null if not resolved
     */
    public String getResolutionNotes() {
        return resolutionNotes;
    }

    /**
     * Gets the timestamp when the event was resolved.
     *
     * @return The resolution timestamp, or null if not resolved
     */
    public LocalDateTime getResolvedAt() {
        return resolvedAt;
    }

    /**
     * Gets the identifier of who resolved the event.
     *
     * @return The resolver identifier, or null if not resolved
     */
    public String getResolvedBy() {
        return resolvedBy;
    }

    // =================== Setter Methods ===================
    /**
     * Sets the unique log identifier.
     *
     * @param logId The log ID to set
     */
    public void setLogId(String logId) {
        this.logId = logId;
    }

    /**
     * Sets the type of security event.
     *
     * @param eventType The event type to set
     */
    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    /**
     * Sets the severity level of the event.
     *
     * @param severity The severity level to set
     */
    public void setSeverity(String severity) {
        this.severity = severity;
    }

    /**
     * Sets the detailed description of the event.
     *
     * @param description The description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Sets the location where the event occurred.
     *
     * @param location The location to set
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Sets the timestamp when the event occurred.
     *
     * @param timestamp The event timestamp to set
     */
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Sets the ID of the employee involved in the event.
     *
     * @param employeeId The employee ID to set
     */
    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    /**
     * Sets the entity affected by the event.
     *
     * @param affectedEntity The affected entity to set
     */
    public void setAffectedEntity(String affectedEntity) {
        this.affectedEntity = affectedEntity;
    }

    /**
     * Sets the IP address associated with the event.
     *
     * @param ipAddress The IP address to set
     */
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    /**
     * Sets the current status of the log entry.
     *
     * @param status The status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Sets the resolution notes for the event.
     *
     * @param resolutionNotes The resolution notes to set
     */
    public void setResolutionNotes(String resolutionNotes) {
        this.resolutionNotes = resolutionNotes;
    }

    /**
     * Sets the timestamp when the event was resolved.
     *
     * @param resolvedAt The resolution timestamp to set
     */
    public void setResolvedAt(LocalDateTime resolvedAt) {
        this.resolvedAt = resolvedAt;
    }

    /**
     * Sets the identifier of who resolved the event.
     *
     * @param resolvedBy The resolver identifier to set
     */
    public void setResolvedBy(String resolvedBy) {
        this.resolvedBy = resolvedBy;
    }

    // =================== Business Logic Methods ===================
    /**
     * Marks the log as resolved with resolution details. Updates status to
     * "Resolved" and sets resolution timestamp to current time.
     *
     * @param resolvedBy Identifier of who resolved the event
     * @param resolutionNotes Notes describing how the event was resolved
     */
    public void resolve(String resolvedBy, String resolutionNotes) {
        this.status = STATUS_RESOLVED;
        this.resolvedBy = resolvedBy;
        this.resolutionNotes = resolutionNotes;
        this.resolvedAt = LocalDateTime.now();
    }

    /**
     * Updates resolution notes for the log. This method allows updating
     * resolution notes without changing other resolution details.
     *
     * @param notes New resolution notes
     */
    public void setResolvedNotes(String notes) {
        this.resolutionNotes = notes;
    }

    /**
     * Gets the formatted timestamp string. Returns "N/A" if timestamp is null.
     *
     * @return Formatted timestamp, or "N/A" if null
     */
    public String getFormattedTimestamp() {
        if (timestamp == null) {
            return "N/A";
        }
        return timestamp.format(TIMESTAMP_FORMATTER);
    }

    /**
     * Gets the formatted resolved timestamp string. Returns "N/A" if resolved
     * timestamp is null.
     *
     * @return Formatted resolution timestamp, or "N/A" if null
     */
    public String getFormattedResolvedAt() {
        if (resolvedAt == null) {
            return "N/A";
        }
        return resolvedAt.format(TIMESTAMP_FORMATTER);
    }

    /**
     * Checks if the log has critical severity.
     *
     * @return True if severity is "Critical", false otherwise
     */
    public boolean isCritical() {
        return SEVERITY_CRITICAL.equalsIgnoreCase(severity);
    }

    /**
     * Checks if the log is resolved.
     *
     * @return True if status is "Resolved", false otherwise
     */
    public boolean isResolved() {
        return STATUS_RESOLVED.equalsIgnoreCase(status);
    }

    /**
     * Checks if the log requires immediate attention. A log requires attention
     * if it's not resolved and has high or critical severity.
     *
     * @return True if log requires attention, false otherwise
     */
    public boolean requiresAttention() {
        return !isResolved() && (isCritical() || SEVERITY_HIGH.equalsIgnoreCase(severity));
    }

    /**
     * Gets a summary of the log entry. Provides a concise one-line summary for
     * display purposes.
     *
     * @return Log summary string
     */
    public String getSummary() {
        return String.format("%s - %s [%s]", eventType, description, severity);
    }

    /**
     * Gets full detailed information about the log entry. Provides complete
     * information for detailed viewing or reporting.
     *
     * @return Full log details as formatted string
     */
    public String getFullDetails() {
        StringBuilder details = new StringBuilder();
        details.append("Log ID: ").append(logId).append("\n");
        details.append("Event Type: ").append(eventType).append("\n");
        details.append("Severity: ").append(severity).append("\n");
        details.append("Description: ").append(description).append("\n");
        details.append("Location: ").append(location).append("\n");
        details.append("Timestamp: ").append(getFormattedTimestamp()).append("\n");
        details.append("Employee ID: ").append(employeeId != null ? employeeId : "N/A").append("\n");
        details.append("Affected Entity: ").append(affectedEntity != null ? affectedEntity : "N/A").append("\n");
        details.append("IP Address: ").append(ipAddress != null ? ipAddress : "N/A").append("\n");
        details.append("Status: ").append(status).append("\n");

        if (isResolved()) {
            details.append("Resolved By: ").append(resolvedBy).append("\n");
            details.append("Resolved At: ").append(getFormattedResolvedAt()).append("\n");
            details.append("Resolution Notes: ").append(resolutionNotes).append("\n");
        }

        return details.toString();
    }

    /**
     * Converts the log entry to CSV format. Suitable for export or data
     * exchange purposes.
     *
     * @return CSV-formatted string representing the log entry
     */
    public String toCSV() {
        return String.join(",",
                logId != null ? logId : "",
                eventType != null ? eventType : "",
                severity != null ? severity : "",
                description != null ? description.replace(",", ";") : "",
                location != null ? location : "",
                getFormattedTimestamp(),
                employeeId != null ? employeeId : "",
                affectedEntity != null ? affectedEntity : "",
                ipAddress != null ? ipAddress : "",
                status != null ? status : "",
                resolutionNotes != null ? resolutionNotes.replace(",", ";") : "",
                resolvedBy != null ? resolvedBy : "",
                resolvedAt != null ? getFormattedResolvedAt() : ""
        );
    }

    /**
     * Creates a sample log entry for testing purposes.
     *
     * @return A SecurityLog instance with sample data
     */
    public static SecurityLog createSampleLog() {
        SecurityLog log = new SecurityLog();
        log.setLogId("LOG-" + System.currentTimeMillis());
        log.setEventType(EVENT_LOGIN);
        log.setSeverity(SEVERITY_MEDIUM);
        log.setDescription("Multiple failed login attempts detected");
        log.setLocation("Control Room");
        log.setEmployeeId("EMP001");
        log.setAffectedEntity("Admin Account");
        log.setIpAddress("192.168.1.100");
        log.setStatus(STATUS_ACTIVE);
        return log;
    }

    // =================== Overridden Methods ===================
    /**
     * Returns a string representation of the log entry. Provides a concise
     * format for debugging and display.
     *
     * @return String representation of the log
     */
    @Override
    public String toString() {
        return String.format("[%s] %s - %s (%s | %s)",
                getFormattedTimestamp(),
                eventType,
                description,
                severity,
                status);
    }

    /**
     * Compares this log entry with another object for equality. Two log entries
     * are considered equal if they have the same log ID.
     *
     * @param o Object to compare with
     * @return True if objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SecurityLog that = (SecurityLog) o;
        return Objects.equals(logId, that.logId);
    }

    /**
     * Returns a hash code value for the log entry.
     *
     * @return Hash code based on log ID
     */
    @Override
    public int hashCode() {
        return Objects.hash(logId);
    }

    /**
     * Validates if the log entry has all required fields.
     *
     * @return True if log configuration is valid, false otherwise
     */
    public boolean isValid() {
        return logId != null && !logId.trim().isEmpty()
                && eventType != null && !eventType.trim().isEmpty()
                && severity != null && !severity.trim().isEmpty()
                && description != null && !description.trim().isEmpty()
                && location != null && !location.trim().isEmpty()
                && timestamp != null
                && status != null && !status.trim().isEmpty();
    }
}
