package database;

import model.GuardDuty;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for GuardDuty entity. Handles all database operations
 * related to guard duty scheduling, tracking, and management. Uses Singleton
 * pattern for database connection management.
 */
public class GuardDutyDAO {

    /**
     * Retrieves all guard duties from the database. Duties are ordered by duty
     * date (newest first) and start time.
     *
     * @return List of all GuardDuty objects
     */
    public List<GuardDuty> getAllGuardDuties() {
        List<GuardDuty> duties = new ArrayList<>();
        String query = "SELECT * FROM guard_duties ORDER BY duty_date DESC, start_time";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                duties.add(extractGuardDutyFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving all guard duties: " + e.getMessage());
        }
        return duties;
    }

    /**
     * Retrieves guard duties assigned to a specific officer.
     *
     * @param officerId Unique identifier of the officer
     * @return List of GuardDuty objects for the specified officer
     */
    public List<GuardDuty> getDutiesByOfficer(String officerId) {
        List<GuardDuty> duties = new ArrayList<>();
        String query = "SELECT * FROM guard_duties WHERE officer_id = ? ORDER BY duty_date DESC";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, officerId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                duties.add(extractGuardDutyFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving duties by officer: " + e.getMessage());
        }
        return duties;
    }

    /**
     * Searches for guard duties based on multiple criteria. Supports text
     * search, status filtering, and date filtering.
     *
     * @param searchText Text to search in officer name, location, or duty type
     * @param status Duty status filter (or "All" for no filter)
     * @param date Specific date filter
     * @return List of GuardDuty objects matching search criteria
     */
    public List<GuardDuty> searchGuardDuties(String searchText, String status, LocalDate date) {
        List<GuardDuty> duties = new ArrayList<>();
        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM guard_duties WHERE 1=1");
        List<Object> parameters = new ArrayList<>();

        // Add text search criteria
        if (searchText != null && !searchText.trim().isEmpty()) {
            queryBuilder.append(" AND (LOWER(officer_name) LIKE ? OR LOWER(location) LIKE ? OR LOWER(duty_type) LIKE ?)");
            String searchPattern = "%" + searchText.toLowerCase().trim() + "%";
            parameters.add(searchPattern);
            parameters.add(searchPattern);
            parameters.add(searchPattern);
        }

        // Add status filter
        if (status != null && !status.equals("All")) {
            queryBuilder.append(" AND status = ?");
            parameters.add(status);
        }

        // Add date filter
        if (date != null) {
            queryBuilder.append(" AND duty_date = ?");
            parameters.add(Date.valueOf(date));
        }

        queryBuilder.append(" ORDER BY duty_date DESC, start_time");

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(queryBuilder.toString())) {

            // Set all parameters
            for (int i = 0; i < parameters.size(); i++) {
                pstmt.setObject(i + 1, parameters.get(i));
            }

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                duties.add(extractGuardDutyFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error searching guard duties: " + e.getMessage());
        }
        return duties;
    }

    /**
     * Generates a unique duty identifier. Format: DUTY + YYYYMMDD + 4-digit
     * random number.
     *
     * @return Unique duty identifier string
     */
    public String generateDutyId() {
        String prefix = "DUTY";
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String random = String.format("%04d", (int) (Math.random() * 10000));
        return prefix + date + random;
    }

    /**
     * Adds a new guard duty to the database. Generates a unique duty ID if not
     * provided.
     *
     * @param duty GuardDuty object to add
     * @return true if duty was added successfully, false otherwise
     */
    public boolean addGuardDuty(GuardDuty duty) {
        // Generate unique duty ID if not provided
        if (duty.getDutyId() == null || duty.getDutyId().trim().isEmpty()) {
            duty.setDutyId(generateDutyId());
        }

        String query = "INSERT INTO guard_duties (duty_id, officer_id, officer_name, duty_type, location, "
                + "start_time, end_time, duty_date, status, priority, notes) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, duty.getDutyId());
            pstmt.setString(2, duty.getOfficerId() != null ? duty.getOfficerId() : "UNKNOWN");
            pstmt.setString(3, duty.getOfficerName() != null ? duty.getOfficerName() : "Unknown Officer");
            pstmt.setString(4, duty.getDutyType());
            pstmt.setString(5, duty.getLocation());
            pstmt.setString(6, duty.getStartTime());
            pstmt.setString(7, duty.getEndTime());
            pstmt.setDate(8, Date.valueOf(duty.getDate()));

            String status = duty.getStatus() != null ? duty.getStatus() : "Scheduled";
            pstmt.setString(9, status);

            String priority = duty.getPriority() != null ? duty.getPriority() : "Medium";
            pstmt.setString(10, priority);

            pstmt.setString(11, duty.getNotes());

            int result = pstmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Error adding guard duty: " + e.getMessage());
            return false;
        }
    }

    /**
     * Updates an existing guard duty in the database.
     *
     * @param duty GuardDuty object with updated values
     * @return true if update was successful, false otherwise
     */
    public boolean updateGuardDuty(GuardDuty duty) {
        String query = "UPDATE guard_duties SET officer_id = ?, officer_name = ?, duty_type = ?, location = ?, "
                + "start_time = ?, end_time = ?, duty_date = ?, status = ?, priority = ?, notes = ? "
                + "WHERE duty_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, duty.getOfficerId() != null ? duty.getOfficerId() : "UNKNOWN");
            pstmt.setString(2, duty.getOfficerName() != null ? duty.getOfficerName() : "Unknown Officer");
            pstmt.setString(3, duty.getDutyType());
            pstmt.setString(4, duty.getLocation());
            pstmt.setString(5, duty.getStartTime());
            pstmt.setString(6, duty.getEndTime());
            pstmt.setDate(7, Date.valueOf(duty.getDate()));

            String status = duty.getStatus() != null ? duty.getStatus() : "Scheduled";
            pstmt.setString(8, status);

            String priority = duty.getPriority() != null ? duty.getPriority() : "Medium";
            pstmt.setString(9, priority);

            pstmt.setString(10, duty.getNotes());
            pstmt.setString(11, duty.getDutyId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating guard duty: " + e.getMessage());
            return false;
        }
    }

    /**
     * Updates the status of a guard duty.
     *
     * @param dutyId Unique identifier of the duty
     * @param status New status to set
     * @param completedTime Time when duty was completed (for completed duties)
     * @return true if status update was successful, false otherwise
     */
    public boolean updateDutyStatus(String dutyId, String status, String completedTime) {
        String query = "UPDATE guard_duties SET status = ?, completed_time = ? WHERE duty_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, status);

            // Extract short time format (HH:MM:SS) from completed time if provided
            String shortTime = null;
            if (completedTime != null && !completedTime.trim().isEmpty()) {
                if (completedTime.length() >= 8) {
                    shortTime = completedTime.substring(0, 8);
                } else {
                    shortTime = completedTime;
                }
            }
            pstmt.setString(2, shortTime);

            pstmt.setString(3, dutyId);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating duty status: " + e.getMessage());
            return false;
        }
    }

    /**
     * Deletes a guard duty from the database.
     *
     * @param dutyId Unique identifier of the duty to delete
     * @return true if deletion was successful, false otherwise
     */
    public boolean deleteGuardDuty(String dutyId) {
        String query = "DELETE FROM guard_duties WHERE duty_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, dutyId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting guard duty: " + e.getMessage());
            return false;
        }
    }

    /**
     * Reports an issue related to a guard duty. Creates a record in the
     * duty_issues table.
     *
     * @param dutyId Unique identifier of the duty with issue
     * @param issue Description of the issue
     * @param reportedBy Identifier of the person reporting the issue
     * @return true if issue report was successful, false otherwise
     */
    public boolean reportDutyIssue(String dutyId, String issue, String reportedBy) {
        String query = "INSERT INTO duty_issues (duty_id, issue_description, reported_by) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, dutyId);
            pstmt.setString(2, issue);
            pstmt.setString(3, reportedBy);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error reporting duty issue: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves guard duties for a specific date.
     *
     * @param date Date to retrieve duties for
     * @return List of GuardDuty objects for the specified date
     */
    public List<GuardDuty> getDutiesByDate(LocalDate date) {
        List<GuardDuty> duties = new ArrayList<>();
        String query = "SELECT * FROM guard_duties WHERE duty_date = ? ORDER BY start_time";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setDate(1, Date.valueOf(date));
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                duties.add(extractGuardDutyFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving duties by date: " + e.getMessage());
        }
        return duties;
    }

    /**
     * Retrieves guard duties by status.
     *
     * @param status Status to filter by
     * @return List of GuardDuty objects with specified status
     */
    public List<GuardDuty> getDutiesByStatus(String status) {
        List<GuardDuty> duties = new ArrayList<>();
        String query = "SELECT * FROM guard_duties WHERE status = ? ORDER BY duty_date DESC, start_time";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, status);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                duties.add(extractGuardDutyFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving duties by status: " + e.getMessage());
        }
        return duties;
    }

    /**
     * Retrieves today's guard duties for a specific officer.
     *
     * @param officerId Unique identifier of the officer
     * @return List of GuardDuty objects for today
     */
    public List<GuardDuty> getTodayDutiesByOfficer(String officerId) {
        List<GuardDuty> duties = new ArrayList<>();
        String query = "SELECT * FROM guard_duties WHERE officer_id = ? AND duty_date = CURDATE() ORDER BY start_time";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, officerId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                duties.add(extractGuardDutyFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving today's duties by officer: " + e.getMessage());
        }
        return duties;
    }

    /**
     * Gets the total count of guard duties.
     *
     * @return Total number of guard duties
     */
    public int getTotalDutiesCount() {
        String query = "SELECT COUNT(*) as total FROM guard_duties";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.err.println("Error counting guard duties: " + e.getMessage());
        }
        return 0;
    }

    /**
     * Extracts GuardDuty object from a ResultSet.
     *
     * @param resultSet ResultSet containing guard duty data
     * @return GuardDuty object populated with data
     * @throws SQLException if database error occurs
     */
    private GuardDuty extractGuardDutyFromResultSet(ResultSet resultSet) throws SQLException {
        GuardDuty duty = new GuardDuty();

        duty.setDutyId(resultSet.getString("duty_id"));
        duty.setOfficerId(resultSet.getString("officer_id"));
        duty.setOfficerName(resultSet.getString("officer_name"));
        duty.setDutyType(resultSet.getString("duty_type"));
        duty.setLocation(resultSet.getString("location"));
        duty.setStartTime(resultSet.getString("start_time"));
        duty.setEndTime(resultSet.getString("end_time"));

        // Set duty date
        Date dutyDate = resultSet.getDate("duty_date");
        if (dutyDate != null) {
            duty.setDate(dutyDate.toLocalDate());
        }

        duty.setStatus(resultSet.getString("status"));
        duty.setPriority(resultSet.getString("priority"));
        duty.setCompletedTime(resultSet.getString("completed_time"));
        duty.setNotes(resultSet.getString("notes"));

        return duty;
    }
}
