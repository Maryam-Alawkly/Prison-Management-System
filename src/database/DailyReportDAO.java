package database;

import model.DailyReport;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for DailyReport entity.
 * Implements Singleton pattern for DAO instance management.
 */
public class DailyReportDAO {
    
    // Singleton instance
    private static DailyReportDAO instance;
    
    /**
     * Private constructor to enforce Singleton pattern.
     */
    private DailyReportDAO() {
        // Private constructor to prevent instantiation
    }
    
    /**
     * Returns the single instance of DailyReportDAO.
     *
     * @return DailyReportDAO instance
     */
    public static synchronized DailyReportDAO getInstance() {
        if (instance == null) {
            instance = new DailyReportDAO();
        }
        return instance;
    }
    /**
     * Retrieves all daily reports from the database. Reports are ordered by
     * report date (newest first) and creation timestamp.
     *
     * @return List of all DailyReport objects
     */
    public List<DailyReport> getAllReports() {
        List<DailyReport> reports = new ArrayList<>();
        String query = "SELECT * FROM daily_reports ORDER BY report_date DESC, created_at DESC";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                reports.add(extractReportFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving all daily reports: " + e.getMessage());
        }
        return reports;
    }

    /**
     * Searches for daily reports based on multiple criteria. Supports text
     * search, status filtering, type filtering, and date ranges.
     *
     * @param searchText Text to search in report ID, officer name, or incidents
     * summary
     * @param status Report status filter (or "All" for no filter)
     * @param type Report type filter (or "All" for no filter)
     * @param dateFrom Start date for date range filter (inclusive)
     * @param dateTo End date for date range filter (inclusive)
     * @return List of DailyReport objects matching search criteria
     */
    public List<DailyReport> searchReports(String searchText, String status, String type,
            LocalDate dateFrom, LocalDate dateTo) {
        List<DailyReport> reports = new ArrayList<>();
        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM daily_reports WHERE 1=1");
        List<Object> parameters = new ArrayList<>();

        // Add text search criteria
        if (searchText != null && !searchText.trim().isEmpty()) {
            queryBuilder.append(" AND (LOWER(report_id) LIKE ? OR LOWER(officer_name) LIKE ? OR LOWER(incidents_summary) LIKE ?)");
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

        // Add type filter
        if (type != null && !type.equals("All")) {
            queryBuilder.append(" AND report_type = ?");
            parameters.add(type);
        }

        // Add date range filters
        if (dateFrom != null) {
            queryBuilder.append(" AND report_date >= ?");
            parameters.add(Date.valueOf(dateFrom));
        }

        if (dateTo != null) {
            queryBuilder.append(" AND report_date <= ?");
            parameters.add(Date.valueOf(dateTo));
        }

        queryBuilder.append(" ORDER BY report_date DESC, created_at DESC");

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(queryBuilder.toString())) {

            // Set all parameters
            for (int i = 0; i < parameters.size(); i++) {
                pstmt.setObject(i + 1, parameters.get(i));
            }

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                reports.add(extractReportFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error searching daily reports: " + e.getMessage());
        }
        return reports;
    }

    /**
     * Saves a daily report to the database. Uses INSERT ... ON DUPLICATE KEY
     * UPDATE to handle both new reports and updates.
     *
     * @param report DailyReport object to save
     * @return true if save operation was successful, false otherwise
     */
    public boolean saveReport(DailyReport report) {
        String query = "INSERT INTO daily_reports (report_id, officer_id, officer_name, report_type, priority, "
                + "report_date, status, incidents_summary, actions_taken, patrols_completed, "
                + "cell_inspections, visitor_screenings, activity_details, additional_notes) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) "
                + "ON DUPLICATE KEY UPDATE "
                + "officer_id=?, officer_name=?, report_type=?, priority=?, report_date=?, status=?, "
                + "incidents_summary=?, actions_taken=?, patrols_completed=?, cell_inspections=?, "
                + "visitor_screenings=?, activity_details=?, additional_notes=?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            int paramIndex = 1;

            // Set insert parameters
            pstmt.setString(paramIndex++, report.getReportId());
            pstmt.setString(paramIndex++, report.getOfficerId());
            pstmt.setString(paramIndex++, report.getOfficerName());
            pstmt.setString(paramIndex++, report.getReportType());
            pstmt.setString(paramIndex++, report.getPriority());
            pstmt.setDate(paramIndex++, Date.valueOf(report.getReportDate()));

            String status = report.getStatus() != null ? report.getStatus() : "Draft";
            pstmt.setString(paramIndex++, status);

            pstmt.setString(paramIndex++, report.getIncidentsSummary());
            pstmt.setString(paramIndex++, report.getActionsTaken());
            pstmt.setString(paramIndex++, report.getPatrolsCompleted());
            pstmt.setString(paramIndex++, report.getCellInspections());
            pstmt.setString(paramIndex++, report.getVisitorScreenings());
            pstmt.setString(paramIndex++, report.getActivityDetails());
            pstmt.setString(paramIndex++, report.getAdditionalNotes());

            // Set update parameters
            pstmt.setString(paramIndex++, report.getOfficerId());
            pstmt.setString(paramIndex++, report.getOfficerName());
            pstmt.setString(paramIndex++, report.getReportType());
            pstmt.setString(paramIndex++, report.getPriority());
            pstmt.setDate(paramIndex++, Date.valueOf(report.getReportDate()));
            pstmt.setString(paramIndex++, status);
            pstmt.setString(paramIndex++, report.getIncidentsSummary());
            pstmt.setString(paramIndex++, report.getActionsTaken());
            pstmt.setString(paramIndex++, report.getPatrolsCompleted());
            pstmt.setString(paramIndex++, report.getCellInspections());
            pstmt.setString(paramIndex++, report.getVisitorScreenings());
            pstmt.setString(paramIndex++, report.getActivityDetails());
            pstmt.setString(paramIndex++, report.getAdditionalNotes());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error saving daily report: " + e.getMessage());
            return false;
        }
    }

    /**
     * Extracts DailyReport object from a ResultSet. Handles optional fields
     * gracefully by catching SQLException for missing columns.
     *
     * @param resultSet ResultSet containing report data
     * @return DailyReport object populated with data
     * @throws SQLException if database error occurs
     */
    private DailyReport extractReportFromResultSet(ResultSet resultSet) throws SQLException {
        DailyReport report = new DailyReport();

        // Set basic required fields
        report.setReportId(resultSet.getString("report_id"));
        report.setOfficerId(resultSet.getString("officer_id"));
        report.setOfficerName(resultSet.getString("officer_name"));
        report.setReportType(resultSet.getString("report_type"));
        report.setPriority(resultSet.getString("priority"));

        // Set report date
        Date reportDate = resultSet.getDate("report_date");
        if (reportDate != null) {
            report.setReportDate(reportDate.toLocalDate());
        }

        // Set standard fields
        report.setStatus(resultSet.getString("status"));
        report.setIncidentsSummary(resultSet.getString("incidents_summary"));
        report.setActionsTaken(resultSet.getString("actions_taken"));
        report.setPatrolsCompleted(resultSet.getString("patrols_completed"));
        report.setCellInspections(resultSet.getString("cell_inspections"));
        report.setVisitorScreenings(resultSet.getString("visitor_screenings"));
        report.setActivityDetails(resultSet.getString("activity_details"));
        report.setAdditionalNotes(resultSet.getString("additional_notes"));

        // Set creation date
        Date createdAt = resultSet.getDate("created_at");
        if (createdAt != null) {
            report.setCreatedAt(createdAt.toLocalDate());
        }

        // Set review fields (optional)
        report.setReviewedBy(getOptionalString(resultSet, "reviewed_by"));

        Date reviewDate = resultSet.getDate("review_date");
        if (reviewDate != null) {
            report.setReviewDate(reviewDate.toLocalDate());
        }

        report.setAdminComments(getOptionalString(resultSet, "admin_comments"));

        // Set additional optional fields
        report.setSecurityLevel(getOptionalString(resultSet, "security_level"));
        report.setAlertsHandled(getOptionalString(resultSet, "alerts_handled"));
        report.setHeadcount(getOptionalString(resultSet, "headcount"));
        report.setTransfers(getOptionalString(resultSet, "transfers"));
        report.setDisciplinaryActions(getOptionalString(resultSet, "disciplinary_actions"));
        report.setBehaviorNotes(getOptionalString(resultSet, "behavior_notes"));
        report.setEquipmentChecks(getOptionalString(resultSet, "equipment_checks"));
        report.setIssuesEncountered(getOptionalString(resultSet, "issues_encountered"));
        report.setRecommendations(getOptionalString(resultSet, "recommendations"));

        return report;
    }

    /**
     * Safely retrieves an optional string field from ResultSet. Returns null if
     * the column doesn't exist or value is null.
     *
     * @param resultSet ResultSet containing data
     * @param columnName Name of the column to retrieve
     * @return Column value or null if column doesn't exist
     */
    private String getOptionalString(ResultSet resultSet, String columnName) {
        try {
            return resultSet.getString(columnName);
        } catch (SQLException e) {
            // Column doesn't exist or is null - return null
            return null;
        }
    }

    /**
     * Updates the status of a daily report and records review information.
     *
     * @param reportId Unique identifier of the report
     * @param status New status for the report
     * @param reviewedBy Name/ID of the reviewer
     * @param comments Review comments
     * @return true if update was successful, false otherwise
     */
    public boolean updateReportStatus(String reportId, String status, String reviewedBy, String comments) {
        String query = "UPDATE daily_reports SET status = ?, reviewed_by = ?, review_date = CURDATE(), "
                + "admin_comments = ? WHERE report_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, status);
            pstmt.setString(2, reviewedBy);
            pstmt.setString(3, comments);
            pstmt.setString(4, reportId);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating report status: " + e.getMessage());
            return false;
        }
    }

    /**
     * Adds an admin comment to an existing report. Appends comment to existing
     * comments with timestamp and admin identifier.
     *
     * @param reportId Unique identifier of the report
     * @param comment Comment text to add
     * @param adminId Identifier of the admin making the comment
     * @return true if comment was added successfully, false otherwise
     */
    public boolean addAdminComment(String reportId, String comment, String adminId) {
        String query = "UPDATE daily_reports SET admin_comments = CONCAT(COALESCE(admin_comments, ''), ?, '\n'), "
                + "reviewed_by = ?, review_date = CURDATE() WHERE report_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            String timestampedComment = "\n[" + java.time.LocalDateTime.now() + " - Admin " + adminId + "]: " + comment;
            pstmt.setString(1, timestampedComment);
            pstmt.setString(2, adminId);
            pstmt.setString(3, reportId);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error adding admin comment: " + e.getMessage());
            return false;
        }
    }

    /**
     * Sends feedback to an officer regarding their report. Creates a feedback
     * record in the database.
     *
     * @param reportId Report identifier
     * @param officerId Officer identifier
     * @param feedback Feedback text
     * @param fromAdmin Admin identifier sending the feedback
     * @return true if feedback was sent successfully, false otherwise
     */
    public boolean sendFeedbackToOfficer(String reportId, String officerId, String feedback, String fromAdmin) {
        String query = "INSERT INTO report_feedback (report_id, officer_id, feedback, from_admin, sent_at) "
                + "VALUES (?, ?, ?, ?, NOW())";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, reportId);
            pstmt.setString(2, officerId);
            pstmt.setString(3, feedback);
            pstmt.setString(4, fromAdmin);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error sending feedback to officer: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves reports for a specific officer.
     *
     * @param officerId Officer identifier
     * @return List of DailyReport objects for the specified officer
     */
    public List<DailyReport> getReportsByOfficer(String officerId) {
        List<DailyReport> reports = new ArrayList<>();
        String query = "SELECT * FROM daily_reports WHERE officer_id = ? ORDER BY report_date DESC";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, officerId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                reports.add(extractReportFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving reports by officer: " + e.getMessage());
        }
        return reports;
    }

    /**
     * Deletes a daily report from the database.
     *
     * @param reportId Unique identifier of the report to delete
     * @return true if deletion was successful, false otherwise
     */
    public boolean deleteReport(String reportId) {
        String query = "DELETE FROM daily_reports WHERE report_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, reportId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting daily report: " + e.getMessage());
            return false;
        }
    }

    /**
     * Gets the total count of daily reports.
     *
     * @return Total number of daily reports
     */
    public int getTotalReportCount() {
        String query = "SELECT COUNT(*) as total FROM daily_reports";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.err.println("Error counting daily reports: " + e.getMessage());
        }
        return 0;
    }
}
