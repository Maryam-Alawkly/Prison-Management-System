package database;

import model.SecurityAlert;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for SecurityAlert entity
 */
public class SecurityAlertDAO {

    /**
     * Create a new security alert
     */
    public boolean createSecurityAlert(SecurityAlert alert) {
        String sql = "INSERT INTO security_alerts (alert_id, alert_type, severity, description, "
                + "location, triggered_at, triggered_by, status, requires_response, response_time) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, alert.getAlertId());
            stmt.setString(2, alert.getAlertType());
            stmt.setString(3, alert.getSeverity());
            stmt.setString(4, alert.getDescription());
            stmt.setString(5, alert.getLocation());
            stmt.setTimestamp(6, Timestamp.valueOf(alert.getTriggeredAt()));
            stmt.setString(7, alert.getTriggeredBy());
            stmt.setString(8, alert.getStatus());
            stmt.setBoolean(9, alert.isRequiresResponse());
            stmt.setInt(10, alert.getResponseTime());

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            System.err.println("Error creating security alert: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get active security alerts
     */
    public List<SecurityAlert> getActiveSecurityAlerts() {
        List<SecurityAlert> alerts = new ArrayList<>();
        String sql = "SELECT * FROM security_alerts WHERE status IN ('Active', 'Acknowledged') "
                + "ORDER BY triggered_at DESC";

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                SecurityAlert alert = createSecurityAlertFromResultSet(rs);
                alerts.add(alert);
            }

        } catch (SQLException e) {
            System.err.println("Error getting active security alerts: " + e.getMessage());
        }

        return alerts;
    }

    /**
     * Get critical alerts requiring immediate attention
     */
    public List<SecurityAlert> getCriticalAlerts() {
        List<SecurityAlert> alerts = new ArrayList<>();
        String sql = "SELECT * FROM security_alerts WHERE severity = 'Critical' AND status = 'Active' "
                + "ORDER BY triggered_at DESC";

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                SecurityAlert alert = createSecurityAlertFromResultSet(rs);
                alerts.add(alert);
            }

        } catch (SQLException e) {
            System.err.println("Error getting critical alerts: " + e.getMessage());
        }

        return alerts;
    }

    /**
     * Acknowledge a security alert
     */
    public boolean acknowledgeAlert(String alertId, String employeeId) {
        String sql = "UPDATE security_alerts SET status = 'Acknowledged', "
                + "acknowledged_by = ?, acknowledged_at = NOW() WHERE alert_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, employeeId);
            stmt.setString(2, alertId);

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            System.err.println("Error acknowledging alert: " + e.getMessage());
            return false;
        }
    }

    /**
     * Resolve a security alert
     */
    public boolean resolveAlert(String alertId, String employeeId, String notes) {
        String sql = "UPDATE security_alerts SET status = 'Resolved', "
                + "resolved_by = ?, resolved_at = NOW(), notes = ? WHERE alert_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, employeeId);
            stmt.setString(2, notes);
            stmt.setString(3, alertId);

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            System.err.println("Error resolving alert: " + e.getMessage());
            return false;
        }
    }

    /**
     * Assign alert to employee
     */
    public boolean assignAlert(String alertId, String employeeId) {
        String sql = "UPDATE security_alerts SET assigned_to = ? WHERE alert_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, employeeId);
            stmt.setString(2, alertId);

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            System.err.println("Error assigning alert: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get alert statistics
     */
    public int[] getAlertStatistics() {
        String sql = "SELECT "
                + "COUNT(*) as total, "
                + "SUM(CASE WHEN status = 'Active' THEN 1 ELSE 0 END) as active, "
                + "SUM(CASE WHEN status = 'Acknowledged' THEN 1 ELSE 0 END) as acknowledged, "
                + "SUM(CASE WHEN status = 'Resolved' THEN 1 ELSE 0 END) as resolved, "
                + "SUM(CASE WHEN severity = 'Critical' THEN 1 ELSE 0 END) as critical "
                + "FROM security_alerts";

        int total = 0, active = 0, acknowledged = 0, resolved = 0, critical = 0;

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                total = rs.getInt("total");
                active = rs.getInt("active");
                acknowledged = rs.getInt("acknowledged");
                resolved = rs.getInt("resolved");
                critical = rs.getInt("critical");
            }

        } catch (SQLException e) {
            System.err.println("Error getting alert statistics: " + e.getMessage());
        }

        return new int[]{total, active, acknowledged, resolved, critical};
    }

    /**
     * Create SecurityAlert object from ResultSet
     */
    private SecurityAlert createSecurityAlertFromResultSet(ResultSet rs) throws SQLException {
        SecurityAlert alert = new SecurityAlert(
                rs.getString("alert_id"),
                rs.getString("alert_type"),
                rs.getString("severity"),
                rs.getString("description"),
                rs.getString("location")
        );

        alert.setTriggeredAt(rs.getTimestamp("triggered_at").toLocalDateTime());
        alert.setTriggeredBy(rs.getString("triggered_by"));
        alert.setStatus(rs.getString("status"));
        alert.setRequiresResponse(rs.getBoolean("requires_response"));
        alert.setResponseTime(rs.getInt("response_time"));
        alert.setAssignedTo(rs.getString("assigned_to"));
        alert.setNotes(rs.getString("notes"));

        if (rs.getTimestamp("acknowledged_at") != null) {
            alert.setAcknowledgedAt(rs.getTimestamp("acknowledged_at").toLocalDateTime());
        }

        if (rs.getTimestamp("resolved_at") != null) {
            alert.setResolvedAt(rs.getTimestamp("resolved_at").toLocalDateTime());
        }

        alert.setAcknowledgedBy(rs.getString("acknowledged_by"));
        alert.setResolvedBy(rs.getString("resolved_by"));

        return alert;
    }
}
