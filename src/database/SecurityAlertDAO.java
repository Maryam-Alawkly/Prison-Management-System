package database;

import model.SecurityAlert;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for SecurityAlert entity. Implements Singleton pattern for
 * DAO instance management.
 */
public class SecurityAlertDAO {

    // Singleton instance
    private static SecurityAlertDAO instance;

    /**
     * Private constructor to enforce Singleton pattern.
     */
    private SecurityAlertDAO() {
        // Private constructor to prevent instantiation
    }

    /**
     * Returns the single instance of SecurityAlertDAO.
     *
     * @return SecurityAlertDAO instance
     */
    public static synchronized SecurityAlertDAO getInstance() {
        if (instance == null) {
            instance = new SecurityAlertDAO();
        }
        return instance;
    }

    /**
     * Retrieves all active security alerts from the database. Active alerts are
     * those with status 'Active'.
     *
     * @return List of active SecurityAlert objects
     */
    public List<SecurityAlert> getActiveSecurityAlerts() {
        List<SecurityAlert> alerts = new ArrayList<>();
        String query = "SELECT * FROM security_alerts WHERE status = 'Active' ORDER BY triggered_at DESC";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                SecurityAlert alert = extractAlertFromResultSet(rs);
                alerts.add(alert);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving active security alerts: " + e.getMessage());
        }
        return alerts;
    }

    /**
     * Creates a new security alert in the database.
     *
     * @param alert SecurityAlert object containing alert details
     * @return true if alert was created successfully, false otherwise
     */
    public boolean createSecurityAlert(SecurityAlert alert) {
        String query = "INSERT INTO security_alerts (alert_id, alert_type, severity, description, "
                + "location, triggered_by, status, triggered_at) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, NOW())";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, alert.getAlertId());
            pstmt.setString(2, alert.getAlertType());
            pstmt.setString(3, alert.getSeverity());
            pstmt.setString(4, alert.getDescription());
            pstmt.setString(5, alert.getLocation());
            pstmt.setString(6, alert.getTriggeredBy());
            pstmt.setString(7, alert.getStatus());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error creating security alert: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves security alerts triggered within the specified hours.
     *
     * @param hours Number of hours to look back for alerts
     * @return List of recent SecurityAlert objects
     */
    public List<SecurityAlert> getRecentSecurityAlerts(int hours) {
        List<SecurityAlert> alerts = new ArrayList<>();
        String query = "SELECT * FROM security_alerts WHERE triggered_at >= DATE_SUB(NOW(), INTERVAL ? HOUR) "
                + "ORDER BY triggered_at DESC";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, hours);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                SecurityAlert alert = extractAlertFromResultSet(rs);
                alerts.add(alert);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving recent security alerts: " + e.getMessage());
        }
        return alerts;
    }

    /**
     * Retrieves security alerts by severity level.
     *
     * @param severity Severity level to filter by
     * @return List of SecurityAlert objects with specified severity
     */
    public List<SecurityAlert> getSecurityAlertsBySeverity(String severity) {
        List<SecurityAlert> alerts = new ArrayList<>();
        String query = "SELECT * FROM security_alerts WHERE severity = ? ORDER BY triggered_at DESC";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, severity);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                alerts.add(extractAlertFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving alerts by severity: " + e.getMessage());
        }
        return alerts;
    }

    /**
     * Acknowledges a security alert by updating its status.
     *
     * @param alertId Unique identifier of the alert
     * @param acknowledgedBy Identifier of the person acknowledging the alert
     * @return true if acknowledgment was successful, false otherwise
     */
    public boolean acknowledgeAlert(String alertId, String acknowledgedBy) {
        String query = "UPDATE security_alerts SET status = 'Acknowledged', acknowledged_by = ?, "
                + "acknowledged_at = NOW() WHERE alert_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, acknowledgedBy);
            pstmt.setString(2, alertId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error acknowledging security alert: " + e.getMessage());
            return false;
        }
    }

    /**
     * Resolves a security alert by updating its status and adding resolution
     * details.
     *
     * @param alertId Unique identifier of the alert
     * @param resolvedBy Identifier of the person resolving the alert
     * @param resolutionNotes Notes describing how the alert was resolved
     * @return true if resolution was successful, false otherwise
     */
    public boolean resolveAlert(String alertId, String resolvedBy, String resolutionNotes) {
        String query = "UPDATE security_alerts SET status = 'Resolved', resolved_by = ?, "
                + "resolved_at = NOW(), resolution_notes = ? WHERE alert_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, resolvedBy);
            pstmt.setString(2, resolutionNotes);
            pstmt.setString(3, alertId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error resolving security alert: " + e.getMessage());
            return false;
        }
    }

    /**
     * Generates a unique alert identifier. Format: ALERT + YYYYMMDD + 4-digit
     * random number.
     *
     * @return Unique alert identifier string
     */
    public String generateAlertId() {
        String prefix = "ALERT";
        String date = java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd"));
        String random = String.format("%04d", (int) (Math.random() * 10000));
        return prefix + date + random;
    }

    /**
     * Retrieves a security alert by its unique identifier.
     *
     * @param alertId Unique identifier of the alert
     * @return SecurityAlert object if found, null otherwise
     */
    public SecurityAlert getAlertById(String alertId) {
        String query = "SELECT * FROM security_alerts WHERE alert_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, alertId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return extractAlertFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving alert by ID: " + e.getMessage());
        }
        return null;
    }

    /**
     * Retrieves all security alerts from the database.
     *
     * @return List of all SecurityAlert objects
     */
    public List<SecurityAlert> getAllSecurityAlerts() {
        List<SecurityAlert> alerts = new ArrayList<>();
        String query = "SELECT * FROM security_alerts ORDER BY triggered_at DESC";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                SecurityAlert alert = extractAlertFromResultSet(rs);
                alerts.add(alert);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving all security alerts: " + e.getMessage());
        }
        return alerts;
    }

    /**
     * Retrieves alerts by alert type.
     *
     * @param alertType Type of alert to filter by
     * @return List of SecurityAlert objects with specified type
     */
    public List<SecurityAlert> getAlertsByType(String alertType) {
        List<SecurityAlert> alerts = new ArrayList<>();
        String query = "SELECT * FROM security_alerts WHERE alert_type = ? ORDER BY triggered_at DESC";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, alertType);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                alerts.add(extractAlertFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving alerts by type: " + e.getMessage());
        }
        return alerts;
    }

    /**
     * Retrieves alerts by location.
     *
     * @param location Location to filter by
     * @return List of SecurityAlert objects from specified location
     */
    public List<SecurityAlert> getAlertsByLocation(String location) {
        List<SecurityAlert> alerts = new ArrayList<>();
        String query = "SELECT * FROM security_alerts WHERE location LIKE ? ORDER BY triggered_at DESC";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, "%" + location + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                alerts.add(extractAlertFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving alerts by location: " + e.getMessage());
        }
        return alerts;
    }

    /**
     * Gets the count of alerts by status.
     *
     * @param status Status to filter by
     * @return Number of alerts with specified status
     */
    public int getAlertCountByStatus(String status) {
        String query = "SELECT COUNT(*) as count FROM security_alerts WHERE status = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, status);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("count");
            }
        } catch (SQLException e) {
            System.err.println("Error counting alerts by status: " + e.getMessage());
        }
        return 0;
    }

    /**
     * Gets the total number of security alerts.
     *
     * @return Total count of security alerts
     */
    public int getTotalAlertCount() {
        String query = "SELECT COUNT(*) as total FROM security_alerts";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.err.println("Error getting total alert count: " + e.getMessage());
        }
        return 0;
    }

    /**
     * Updates an existing security alert.
     *
     * @param alert SecurityAlert object with updated information
     * @return true if update was successful, false otherwise
     */
    public boolean updateAlert(SecurityAlert alert) {
        String query = "UPDATE security_alerts SET alert_type = ?, severity = ?, description = ?, "
                + "location = ?, status = ? WHERE alert_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, alert.getAlertType());
            pstmt.setString(2, alert.getSeverity());
            pstmt.setString(3, alert.getDescription());
            pstmt.setString(4, alert.getLocation());
            pstmt.setString(5, alert.getStatus());
            pstmt.setString(6, alert.getAlertId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating security alert: " + e.getMessage());
            return false;
        }
    }

    /**
     * Deletes a security alert from the database.
     *
     * @param alertId Unique identifier of the alert to delete
     * @return true if deletion was successful, false otherwise
     */
    public boolean deleteAlert(String alertId) {
        String query = "DELETE FROM security_alerts WHERE alert_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, alertId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting security alert: " + e.getMessage());
            return false;
        }
    }

    /**
     * Extracts SecurityAlert object from a ResultSet.
     *
     * @param resultSet ResultSet containing security alert data
     * @return SecurityAlert object populated with data
     * @throws SQLException if database error occurs
     */
    private SecurityAlert extractAlertFromResultSet(ResultSet resultSet) throws SQLException {
        SecurityAlert alert = new SecurityAlert();

        alert.setAlertId(resultSet.getString("alert_id"));
        alert.setAlertType(resultSet.getString("alert_type"));
        alert.setSeverity(resultSet.getString("severity"));
        alert.setDescription(resultSet.getString("description"));
        alert.setLocation(resultSet.getString("location"));
        alert.setStatus(resultSet.getString("status"));

        // Set triggered timestamp
        Timestamp triggeredAt = resultSet.getTimestamp("triggered_at");
        if (triggeredAt != null) {
            alert.setTriggeredAt(triggeredAt.toLocalDateTime());
        }

        // Set acknowledged timestamp
        Timestamp acknowledgedAt = resultSet.getTimestamp("acknowledged_at");
        if (acknowledgedAt != null) {
            alert.setAcknowledgedAt(acknowledgedAt.toLocalDateTime());
        }

        // Set resolved timestamp
        Timestamp resolvedAt = resultSet.getTimestamp("resolved_at");
        if (resolvedAt != null) {
            alert.setResolvedAt(resolvedAt.toLocalDateTime());
        }

        alert.setTriggeredBy(resultSet.getString("triggered_by"));
        alert.setAcknowledgedBy(resultSet.getString("acknowledged_by"));
        alert.setResolvedBy(resultSet.getString("resolved_by"));
        alert.setResolutionNotes(resultSet.getString("resolution_notes"));

        return alert;
    }
}
