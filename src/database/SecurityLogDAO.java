package database;

import model.SecurityLog;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for SecurityLog entity
 */
public class SecurityLogDAO {

    /**
     * Add a new security log
     */
    public boolean addSecurityLog(SecurityLog log) {
        String sql = "INSERT INTO security_logs (log_id, event_type, severity, description, "
                + "location, timestamp, employee_id, affected_entity, ip_address, status) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, log.getLogId());
            stmt.setString(2, log.getEventType());
            stmt.setString(3, log.getSeverity());
            stmt.setString(4, log.getDescription());
            stmt.setString(5, log.getLocation());
            stmt.setTimestamp(6, Timestamp.valueOf(log.getTimestamp()));
            stmt.setString(7, log.getEmployeeId());
            stmt.setString(8, log.getAffectedEntity());
            stmt.setString(9, log.getIpAddress());
            stmt.setString(10, log.getStatus());

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            System.err.println("Error adding security log: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get all security logs
     */
    public List<SecurityLog> getAllSecurityLogs() {
        List<SecurityLog> logs = new ArrayList<>();
        String sql = "SELECT * FROM security_logs ORDER BY timestamp DESC";

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                SecurityLog log = createSecurityLogFromResultSet(rs);
                logs.add(log);
            }

        } catch (SQLException e) {
            System.err.println("Error getting security logs: " + e.getMessage());
        }

        return logs;
    }

    /**
     * Get security logs by severity
     */
    public List<SecurityLog> getSecurityLogsBySeverity(String severity) {
        List<SecurityLog> logs = new ArrayList<>();
        String sql = "SELECT * FROM security_logs WHERE severity = ? ORDER BY timestamp DESC";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, severity);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                SecurityLog log = createSecurityLogFromResultSet(rs);
                logs.add(log);
            }

        } catch (SQLException e) {
            System.err.println("Error getting security logs by severity: " + e.getMessage());
        }

        return logs;
    }

    /**
     * Get unresolved security logs
     */
    public List<SecurityLog> getUnresolvedSecurityLogs() {
        List<SecurityLog> logs = new ArrayList<>();
        String sql = "SELECT * FROM security_logs WHERE status != 'Resolved' ORDER BY timestamp DESC";

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                SecurityLog log = createSecurityLogFromResultSet(rs);
                logs.add(log);
            }

        } catch (SQLException e) {
            System.err.println("Error getting unresolved security logs: " + e.getMessage());
        }

        return logs;
    }

    /**
     * Resolve a security log
     */
    public boolean resolveSecurityLog(String logId, String resolvedBy, String resolutionNotes) {
        String sql = "UPDATE security_logs SET status = 'Resolved', "
                + "resolved_by = ?, resolution_notes = ?, resolved_at = NOW() "
                + "WHERE log_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, resolvedBy);
            stmt.setString(2, resolutionNotes);
            stmt.setString(3, logId);

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            System.err.println("Error resolving security log: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get recent security logs (last 24 hours)
     */
    public List<SecurityLog> getRecentSecurityLogs(int hours) {
        List<SecurityLog> logs = new ArrayList<>();
        String sql = "SELECT * FROM security_logs WHERE timestamp >= DATE_SUB(NOW(), INTERVAL ? HOUR) "
                + "ORDER BY timestamp DESC";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, hours);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                SecurityLog log = createSecurityLogFromResultSet(rs);
                logs.add(log);
            }

        } catch (SQLException e) {
            System.err.println("Error getting recent security logs: " + e.getMessage());
        }

        return logs;
    }

    /**
     * Get security log statistics
     */
    public int[] getSecurityLogStatistics() {
        String sql = "SELECT "
                + "COUNT(*) as total, "
                + "SUM(CASE WHEN severity = 'Critical' THEN 1 ELSE 0 END) as critical, "
                + "SUM(CASE WHEN severity = 'High' THEN 1 ELSE 0 END) as high, "
                + "SUM(CASE WHEN severity = 'Medium' THEN 1 ELSE 0 END) as medium, "
                + "SUM(CASE WHEN severity = 'Low' THEN 1 ELSE 0 END) as low, "
                + "SUM(CASE WHEN status = 'Resolved' THEN 1 ELSE 0 END) as resolved "
                + "FROM security_logs";

        int total = 0, critical = 0, high = 0, medium = 0, low = 0, resolved = 0;

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                total = rs.getInt("total");
                critical = rs.getInt("critical");
                high = rs.getInt("high");
                medium = rs.getInt("medium");
                low = rs.getInt("low");
                resolved = rs.getInt("resolved");
            }

        } catch (SQLException e) {
            System.err.println("Error getting security log statistics: " + e.getMessage());
        }

        return new int[]{total, critical, high, medium, low, resolved};
    }

    /**
     * Create SecurityLog object from ResultSet
     */
    private SecurityLog createSecurityLogFromResultSet(ResultSet rs) throws SQLException {
        SecurityLog log = new SecurityLog(
                rs.getString("log_id"),
                rs.getString("event_type"),
                rs.getString("severity"),
                rs.getString("description"),
                rs.getString("location")
        );

        log.setTimestamp(rs.getTimestamp("timestamp").toLocalDateTime());
        log.setEmployeeId(rs.getString("employee_id"));
        log.setAffectedEntity(rs.getString("affected_entity"));
        log.setIpAddress(rs.getString("ip_address"));
        log.setStatus(rs.getString("status"));
        log.setResolutionNotes(rs.getString("resolution_notes"));

        if (rs.getTimestamp("resolved_at") != null) {
            log.setResolvedAt(rs.getTimestamp("resolved_at").toLocalDateTime());
        }

        log.setResolvedBy(rs.getString("resolved_by"));

        return log;
    }

    public boolean clearOldLogs(int i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
