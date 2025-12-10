package database;

import model.SecurityLog;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Data Access Object for SecurityLog entity. Handles all database operations
 * for security logging and monitoring. Implements Singleton pattern.
 */
public class SecurityLogDAO {

    // Singleton instance
    private static SecurityLogDAO instance;

    /**
     * Private constructor to enforce Singleton pattern.
     */
    private SecurityLogDAO() {
        // Private constructor to prevent instantiation
    }

    /**
     * Returns the single instance of SecurityLogDAO.
     *
     * @return SecurityLogDAO instance
     */
    public static synchronized SecurityLogDAO getInstance() {
        if (instance == null) {
            instance = new SecurityLogDAO();
        }
        return instance;
    }

    /**
     * Adds a new security log entry to the database. Generates log ID and
     * timestamp if not provided.
     *
     * @param log SecurityLog object containing log details
     * @return true if log was added successfully, false otherwise
     */
    public boolean addSecurityLog(SecurityLog log) {
        // Generate log ID if not provided
        if (log.getLogId() == null || log.getLogId().trim().isEmpty()) {
            log.setLogId(generateLogId());
        }

        // Set current timestamp if not provided
        if (log.getTimestamp() == null) {
            log.setTimestamp(LocalDateTime.now());
        }

        String sql = "INSERT INTO security_logs (log_id, event_type, severity, description, "
                + "location, timestamp, employee_id, affected_entity, ip_address, status) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
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
            System.err.println("Error adding security log to database: " + e.getMessage());
            return false;
        }
    }

    /**
     * Generates a unique log identifier. Format: LOG-XXXXXXX-YYYY where X is
     * UUID portion and Y is timestamp portion.
     *
     * @return Unique log identifier string
     */
    private String generateLogId() {
        return "LOG-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase()
                + "-" + System.currentTimeMillis() % 10000;
    }

    /**
     * Retrieves all security logs from the database. Logs are ordered by
     * timestamp (newest first).
     *
     * @return List of all SecurityLog objects
     */
    public List<SecurityLog> getAllSecurityLogs() {
        List<SecurityLog> logs = new ArrayList<>();
        String sql = "SELECT * FROM security_logs ORDER BY timestamp DESC";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                SecurityLog log = createSecurityLogFromResultSet(rs);
                logs.add(log);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving all security logs: " + e.getMessage());
        }

        return logs;
    }

    /**
     * Retrieves security logs by severity level.
     *
     * @param severity Severity level to filter by
     * @return List of SecurityLog objects with specified severity
     */
    public List<SecurityLog> getSecurityLogsBySeverity(String severity) {
        List<SecurityLog> logs = new ArrayList<>();
        String sql = "SELECT * FROM security_logs WHERE UPPER(severity) = ? ORDER BY timestamp DESC";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, severity.toUpperCase());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                SecurityLog log = createSecurityLogFromResultSet(rs);
                logs.add(log);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving security logs by severity: " + e.getMessage());
        }

        return logs;
    }

    /**
     * Retrieves unresolved security logs. Includes logs with status other than
     * 'Resolved'.
     *
     * @return List of unresolved SecurityLog objects
     */
    public List<SecurityLog> getUnresolvedSecurityLogs() {
        List<SecurityLog> logs = new ArrayList<>();
        String sql = "SELECT * FROM security_logs WHERE UPPER(status) != 'RESOLVED' ORDER BY timestamp DESC";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                SecurityLog log = createSecurityLogFromResultSet(rs);
                logs.add(log);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving unresolved security logs: " + e.getMessage());
        }

        return logs;
    }

    /**
     * Resolves a security log by updating its status.
     *
     * @param logId Unique identifier of the log to resolve
     * @param resolvedBy Identifier of the person resolving the log
     * @param resolutionNotes Notes describing the resolution
     * @return true if resolution was successful, false otherwise
     */
    public boolean resolveSecurityLog(String logId, String resolvedBy, String resolutionNotes) {
        String sql = "UPDATE security_logs SET status = 'Resolved', "
                + "resolved_by = ?, resolution_notes = ?, resolved_at = NOW() "
                + "WHERE log_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
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
     * Retrieves recent security logs within the specified time frame.
     *
     * @param hours Number of hours to look back for logs
     * @return List of recent SecurityLog objects
     */
    public List<SecurityLog> getRecentSecurityLogs(int hours) {
        List<SecurityLog> logs = new ArrayList<>();
        String sql = "SELECT * FROM security_logs WHERE timestamp >= DATE_SUB(NOW(), INTERVAL ? HOUR) "
                + "ORDER BY timestamp DESC";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, hours);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                SecurityLog log = createSecurityLogFromResultSet(rs);
                logs.add(log);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving recent security logs: " + e.getMessage());
        }

        return logs;
    }

    /**
     * Gets comprehensive security log statistics.
     *
     * @return Array containing [total, critical, high, medium, low, resolved]
     * counts
     */
    public int[] getSecurityLogStatistics() {
        String sql = "SELECT "
                + "COUNT(*) as total, "
                + "SUM(CASE WHEN UPPER(severity) = 'CRITICAL' THEN 1 ELSE 0 END) as critical, "
                + "SUM(CASE WHEN UPPER(severity) = 'HIGH' THEN 1 ELSE 0 END) as high, "
                + "SUM(CASE WHEN UPPER(severity) = 'MEDIUM' THEN 1 ELSE 0 END) as medium, "
                + "SUM(CASE WHEN UPPER(severity) = 'LOW' THEN 1 ELSE 0 END) as low, "
                + "SUM(CASE WHEN UPPER(status) = 'RESOLVED' THEN 1 ELSE 0 END) as resolved "
                + "FROM security_logs";

        int total = 0, critical = 0, high = 0, medium = 0, low = 0, resolved = 0;

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
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
            System.err.println("Error retrieving security log statistics: " + e.getMessage());
        }

        return new int[]{total, critical, high, medium, low, resolved};
    }

    /**
     * Clears old security logs older than specified days.
     *
     * @param days Number of days to retain logs
     * @return true if cleanup was successful, false otherwise
     */
    public boolean clearOldLogs(int days) {
        String sql = "DELETE FROM security_logs WHERE timestamp < DATE_SUB(NOW(), INTERVAL ? DAY)";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, days);
            int rowsDeleted = stmt.executeUpdate();
            System.out.println("Cleared " + rowsDeleted + " old security logs older than " + days + " days");
            return true;

        } catch (SQLException e) {
            System.err.println("Error clearing old security logs: " + e.getMessage());
            return false;
        }
    }

    /**
     * Updates an existing security log.
     *
     * @param log SecurityLog object with updated information
     * @return true if update was successful, false otherwise
     */
    public boolean updateSecurityLog(SecurityLog log) {
        String sql = "UPDATE security_logs SET "
                + "event_type = ?, severity = ?, description = ?, location = ?, "
                + "employee_id = ?, affected_entity = ?, ip_address = ?, status = ?, "
                + "resolution_notes = ?, resolved_by = ?, resolved_at = ? "
                + "WHERE log_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, log.getEventType());
            stmt.setString(2, log.getSeverity());
            stmt.setString(3, log.getDescription());
            stmt.setString(4, log.getLocation());
            stmt.setString(5, log.getEmployeeId());
            stmt.setString(6, log.getAffectedEntity());
            stmt.setString(7, log.getIpAddress());
            stmt.setString(8, log.getStatus());
            stmt.setString(9, log.getResolutionNotes());
            stmt.setString(10, log.getResolvedBy());

            if (log.getResolvedAt() != null) {
                stmt.setTimestamp(11, Timestamp.valueOf(log.getResolvedAt()));
            } else {
                stmt.setNull(11, Types.TIMESTAMP);
            }

            stmt.setString(12, log.getLogId());

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            System.err.println("Error updating security log: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves a security log by its unique identifier.
     *
     * @param logId Unique identifier of the log
     * @return SecurityLog object if found, null otherwise
     */
    public SecurityLog getLogById(String logId) {
        String sql = "SELECT * FROM security_logs WHERE log_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, logId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return createSecurityLogFromResultSet(rs);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving log by ID: " + e.getMessage());
        }

        return null;
    }

    /**
     * Retrieves security logs by event type.
     *
     * @param eventType Type of event to filter by
     * @return List of SecurityLog objects with specified event type
     */
    public List<SecurityLog> getLogsByEventType(String eventType) {
        List<SecurityLog> logs = new ArrayList<>();
        String sql = "SELECT * FROM security_logs WHERE event_type = ? ORDER BY timestamp DESC";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, eventType);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                SecurityLog log = createSecurityLogFromResultSet(rs);
                logs.add(log);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving logs by event type: " + e.getMessage());
        }

        return logs;
    }

    /**
     * Retrieves security logs by location.
     *
     * @param location Location to filter by
     * @return List of SecurityLog objects from specified location
     */
    public List<SecurityLog> getLogsByLocation(String location) {
        List<SecurityLog> logs = new ArrayList<>();
        String sql = "SELECT * FROM security_logs WHERE location LIKE ? ORDER BY timestamp DESC";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + location + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                SecurityLog log = createSecurityLogFromResultSet(rs);
                logs.add(log);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving logs by location: " + e.getMessage());
        }

        return logs;
    }

    /**
     * Gets the total count of security logs.
     *
     * @return Total number of security logs
     */
    public int getTotalLogCount() {
        String sql = "SELECT COUNT(*) as total FROM security_logs";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt("total");
            }

        } catch (SQLException e) {
            System.err.println("Error getting total log count: " + e.getMessage());
        }

        return 0;
    }

    /**
     * Deletes a security log from the database.
     *
     * @param logId Unique identifier of the log to delete
     * @return true if deletion was successful, false otherwise
     */
    public boolean deleteSecurityLog(String logId) {
        String sql = "DELETE FROM security_logs WHERE log_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, logId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting security log: " + e.getMessage());
            return false;
        }
    }

    /**
     * Creates a SecurityLog object from a database ResultSet.
     *
     * @param resultSet ResultSet containing security log data
     * @return SecurityLog object populated with data
     * @throws SQLException if database error occurs
     */
    private SecurityLog createSecurityLogFromResultSet(ResultSet resultSet) throws SQLException {
        SecurityLog log = new SecurityLog(
                resultSet.getString("log_id"),
                resultSet.getString("event_type"),
                resultSet.getString("severity"),
                resultSet.getString("description"),
                resultSet.getString("location")
        );

        log.setTimestamp(resultSet.getTimestamp("timestamp").toLocalDateTime());
        log.setEmployeeId(resultSet.getString("employee_id"));
        log.setAffectedEntity(resultSet.getString("affected_entity"));
        log.setIpAddress(resultSet.getString("ip_address"));
        log.setStatus(resultSet.getString("status"));
        log.setResolutionNotes(resultSet.getString("resolution_notes"));

        Timestamp resolvedAt = resultSet.getTimestamp("resolved_at");
        if (resolvedAt != null) {
            log.setResolvedAt(resolvedAt.toLocalDateTime());
        }

        log.setResolvedBy(resultSet.getString("resolved_by"));

        return log;
    }
}
