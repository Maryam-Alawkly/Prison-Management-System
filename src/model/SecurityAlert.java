package model;

import database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * SecurityAlert class represents a security alert in the prison management
 * system. This class manages alert information including type, severity,
 * status, and lifecycle timestamps. It also provides database operations for
 * retrieving and managing security alerts.
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
    public SecurityAlert(String alertId, String alertType, String severity, String description, String location) {
        this.alertId = alertId;
        this.alertType = alertType;
        this.severity = severity;
        this.description = description;
        this.location = location;
        this.status = STATUS_ACTIVE;
        this.triggeredAt = LocalDateTime.now();
    }

    // =================== Getter and Setter Methods ===================
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

    // =================== Database Operation Methods ===================
    /**
     * Retrieves all active security alerts from the database. Active alerts are
     * those with status 'Active'. Results are ordered by trigger time in
     * descending order (newest first).
     *
     * @return List of active SecurityAlert objects, or empty list if none found
     */
    public List<SecurityAlert> getActiveSecurityAlerts() {
        List<SecurityAlert> alerts = new ArrayList<>();
        String query = "SELECT * FROM security_alerts WHERE status = 'Active' ORDER BY triggered_at DESC";

        try (Connection conn = getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                SecurityAlert alert = extractAlertFromResultSet(rs);
                alerts.add(alert);
            }
        } catch (SQLException e) {
            System.err.println("Error getting active security alerts: " + e.getMessage());
            e.printStackTrace();
        }
        return alerts;
    }

    /**
     * Retrieves security alerts triggered within the specified time period.
     * Alerts are filtered by trigger time relative to current database time.
     *
     * @param hours Number of hours to look back from current time
     * @return List of recent SecurityAlert objects, or empty list if none found
     */
    public List<SecurityAlert> getRecentSecurityAlerts(int hours) {
        List<SecurityAlert> alerts = new ArrayList<>();
        String query = "SELECT * FROM security_alerts WHERE triggered_at >= DATE_SUB(NOW(), INTERVAL ? HOUR) "
                + "ORDER BY triggered_at DESC";

        try (Connection conn = getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, hours);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                SecurityAlert alert = extractAlertFromResultSet(rs);
                alerts.add(alert);
            }
        } catch (SQLException e) {
            System.err.println("Error getting recent security alerts: " + e.getMessage());
            e.printStackTrace();
        }
        return alerts;
    }

    /**
     * Helper method to get a database connection. Creates a new connection each
     * time it's called.
     *
     * @return Database Connection object
     * @throws SQLException if connection cannot be established
     */
    private Connection getConnection() throws SQLException {
        return DatabaseConnection.getConnection();
    }

    // =================== Formatting and Utility Methods ===================
    /**
     * Gets the triggered timestamp in a formatted string. Returns "N/A" if
     * timestamp is null.
     *
     * @return Formatted trigger timestamp, or "N/A" if null
     */
    public String getFormattedTriggeredAt() {
        if (triggeredAt != null) {
            return triggeredAt.toString();
        }
        return "N/A";
    }

    /**
     * Gets the acknowledged timestamp in a formatted string (yyyy-MM-dd
     * HH:mm:ss). Returns "N/A" if timestamp is null.
     *
     * @return Formatted acknowledgment timestamp, or "N/A" if null
     */
    public String getFormattedAcknowledgedAt() {
        if (acknowledgedAt == null) {
            return "N/A";
        }
        return acknowledgedAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * Gets the resolved timestamp in a formatted string (yyyy-MM-dd HH:mm:ss).
     * Returns "N/A" if timestamp is null.
     *
     * @return Formatted resolution timestamp, or "N/A" if null
     */
    public String getFormattedResolvedAt() {
        if (resolvedAt == null) {
            return "N/A";
        }
        return resolvedAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * Extracts SecurityAlert data from a ResultSet and creates a SecurityAlert
     * object. This helper method maps database columns to SecurityAlert
     * properties. Handles null values for timestamp fields.
     *
     * @param rs ResultSet containing alert data from database query
     * @return SecurityAlert object populated with data from ResultSet
     * @throws SQLException if database access error occurs
     */
    private SecurityAlert extractAlertFromResultSet(ResultSet rs) throws SQLException {
        SecurityAlert alert = new SecurityAlert();

        // Set basic alert properties
        alert.setAlertId(rs.getString("alert_id"));
        alert.setAlertType(rs.getString("alert_type"));
        alert.setSeverity(rs.getString("severity"));
        alert.setDescription(rs.getString("description"));
        alert.setLocation(rs.getString("location"));
        alert.setStatus(rs.getString("status"));
        alert.setTriggeredBy(rs.getString("triggered_by"));
        alert.setAcknowledgedBy(rs.getString("acknowledged_by"));
        alert.setResolvedBy(rs.getString("resolved_by"));
        alert.setResolutionNotes(rs.getString("resolution_notes"));
        alert.setAssignedTo(rs.getString("assigned_to"));

        // Convert SQL Timestamp to LocalDateTime, handling null values
        Timestamp triggeredAt = rs.getTimestamp("triggered_at");
        if (triggeredAt != null) {
            alert.setTriggeredAt(triggeredAt.toLocalDateTime());
        }

        Timestamp acknowledgedAt = rs.getTimestamp("acknowledged_at");
        if (acknowledgedAt != null) {
            alert.setAcknowledgedAt(acknowledgedAt.toLocalDateTime());
        }

        Timestamp resolvedAt = rs.getTimestamp("resolved_at");
        if (resolvedAt != null) {
            alert.setResolvedAt(resolvedAt.toLocalDateTime());
        }

        return alert;
    }

    // =================== Business Logic Methods ===================
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
     * Returns a string representation of the alert. Provides a concise summary
     * for display purposes.
     *
     * @return String representation of the alert
     */
    @Override
    public String toString() {
        return String.format("SecurityAlert[ID=%s, Type=%s, Severity=%s, Status=%s]",
                alertId, alertType, severity, status);
    }
}
