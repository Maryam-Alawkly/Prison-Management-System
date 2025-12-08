package database;

import model.Visit;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Visit entity. Handles all database operations for
 * prison visit management including visit scheduling, status tracking, and
 * statistical analysis. Uses Singleton pattern for database connection
 * management.
 */
public class VisitDAO {

    /**
     * Adds a new visit to the database.
     *
     * @param visit Visit object containing visit details
     * @return true if visit was added successfully, false otherwise
     */
    public boolean addVisit(Visit visit) {
        String sql = "INSERT INTO visits (visit_id, prisoner_id, visitor_id, scheduled_datetime, duration) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, visit.getVisitId());
            stmt.setString(2, visit.getPrisonerId());
            stmt.setString(3, visit.getVisitorId());
            stmt.setTimestamp(4, Timestamp.valueOf(visit.getVisitDateTime()));
            stmt.setInt(5, visit.getDuration());

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            System.err.println("Error adding visit to database: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves all visits from the database.
     *
     * @return List of all Visit objects
     */
    public List<Visit> getAllVisits() {
        List<Visit> visits = new ArrayList<>();
        String sql = "SELECT * FROM visits";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Visit visit = createVisitFromResultSet(rs);
                visits.add(visit);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving all visits: " + e.getMessage());
        }

        return visits;
    }

    /**
     * Finds a visit by its unique identifier.
     *
     * @param visitId Unique identifier of the visit
     * @return Visit object if found, null otherwise
     */
    public Visit getVisitById(String visitId) {
        String sql = "SELECT * FROM visits WHERE visit_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, visitId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return createVisitFromResultSet(rs);
            }

        } catch (SQLException e) {
            System.err.println("Error finding visit by ID: " + e.getMessage());
        }

        return null;
    }

    /**
     * Updates visit information in the database.
     *
     * @param visit Visit object with updated data
     * @return true if update was successful, false otherwise
     */
    public boolean updateVisit(Visit visit) {
        String sql = "UPDATE visits SET prisoner_id=?, visitor_id=?, scheduled_datetime=?, duration=?, status=?, notes=? WHERE visit_id=?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, visit.getPrisonerId());
            stmt.setString(2, visit.getVisitorId());
            stmt.setTimestamp(3, Timestamp.valueOf(visit.getVisitDateTime()));
            stmt.setInt(4, visit.getDuration());
            stmt.setString(5, visit.getStatus());
            stmt.setString(6, visit.getNotes());
            stmt.setString(7, visit.getVisitId());

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            System.err.println("Error updating visit information: " + e.getMessage());
            return false;
        }
    }

    /**
     * Deletes a visit from the database.
     *
     * @param visitId Unique identifier of the visit to delete
     * @return true if deletion was successful, false otherwise
     */
    public boolean deleteVisit(String visitId) {
        String sql = "DELETE FROM visits WHERE visit_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, visitId);
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting visit from database: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves visits by prisoner identifier.
     *
     * @param prisonerId Unique identifier of the prisoner
     * @return List of Visit objects for the specified prisoner
     */
    public List<Visit> getVisitsByPrisoner(String prisonerId) {
        List<Visit> visits = new ArrayList<>();
        String sql = "SELECT * FROM visits WHERE prisoner_id = ? ORDER BY scheduled_datetime DESC";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, prisonerId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Visit visit = createVisitFromResultSet(rs);
                visits.add(visit);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving visits by prisoner: " + e.getMessage());
        }

        return visits;
    }

    /**
     * Retrieves visits by visitor identifier.
     *
     * @param visitorId Unique identifier of the visitor
     * @return List of Visit objects for the specified visitor
     */
    public List<Visit> getVisitsByVisitor(String visitorId) {
        List<Visit> visits = new ArrayList<>();
        String sql = "SELECT * FROM visits WHERE visitor_id = ? ORDER BY scheduled_datetime DESC";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, visitorId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Visit visit = createVisitFromResultSet(rs);
                visits.add(visit);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving visits by visitor: " + e.getMessage());
        }

        return visits;
    }

    /**
     * Retrieves visits by status.
     *
     * @param status Status to filter by
     * @return List of Visit objects with specified status
     */
    public List<Visit> getVisitsByStatus(String status) {
        List<Visit> visits = new ArrayList<>();
        String sql = "SELECT * FROM visits WHERE status = ? ORDER BY scheduled_datetime DESC";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Visit visit = createVisitFromResultSet(rs);
                visits.add(visit);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving visits by status: " + e.getMessage());
        }

        return visits;
    }

    /**
     * Retrieves visits scheduled for today.
     *
     * @return List of Visit objects scheduled for today
     */
    public List<Visit> getTodaysVisits() {
        List<Visit> visits = new ArrayList<>();
        String sql = "SELECT * FROM visits WHERE DATE(scheduled_datetime) = CURDATE() ORDER BY scheduled_datetime ASC";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Visit visit = createVisitFromResultSet(rs);
                visits.add(visit);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving today's visits: " + e.getMessage());
        }

        return visits;
    }

    /**
     * Starts a visit by marking it as in progress.
     *
     * @param visitId Unique identifier of the visit to start
     * @return true if visit was started successfully, false otherwise
     */
    public boolean startVisit(String visitId) {
        String sql = "UPDATE visits SET status = 'In Progress', actual_start_datetime = NOW() WHERE visit_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, visitId);
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            System.err.println("Error starting visit: " + e.getMessage());
            return false;
        }
    }

    /**
     * Completes a visit by marking it as completed. Also records the visit in
     * the visitor's history.
     *
     * @param visitId Unique identifier of the visit to complete
     * @return true if visit was completed successfully, false otherwise
     */
    public boolean completeVisit(String visitId) {
        String sql = "UPDATE visits SET status = 'Completed', actual_end_datetime = NOW() WHERE visit_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, visitId);
            int rowsUpdated = stmt.executeUpdate();

            // Record the visit in visitor's history
            if (rowsUpdated > 0) {
                Visit visit = getVisitById(visitId);
                if (visit != null) {
                    VisitorDAO visitorDAO = new VisitorDAO();
                    visitorDAO.recordVisit(visit.getVisitorId());
                }
            }

            return rowsUpdated > 0;

        } catch (SQLException e) {
            System.err.println("Error completing visit: " + e.getMessage());
            return false;
        }
    }

    /**
     * Cancels a visit with cancellation notes.
     *
     * @param visitId Unique identifier of the visit to cancel
     * @param cancellationNotes Reason for cancellation
     * @return true if visit was cancelled successfully, false otherwise
     */
    public boolean cancelVisit(String visitId, String cancellationNotes) {
        String sql = "UPDATE visits SET status = 'Cancelled', notes = ? WHERE visit_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cancellationNotes);
            stmt.setString(2, visitId);
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            System.err.println("Error cancelling visit: " + e.getMessage());
            return false;
        }
    }

    /**
     * Gets the total number of visits in the database.
     *
     * @return Total count of visits
     */
    public int getTotalVisits() {
        String sql = "SELECT COUNT(*) as total FROM visits";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt("total");
            }

        } catch (SQLException e) {
            System.err.println("Error getting total visits count: " + e.getMessage());
        }

        return 0;
    }

    /**
     * Gets the number of completed visits.
     *
     * @return Number of completed visits
     */
    public int getCompletedVisitsCount() {
        String sql = "SELECT COUNT(*) as total FROM visits WHERE status = 'Completed'";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt("total");
            }

        } catch (SQLException e) {
            System.err.println("Error getting completed visits count: " + e.getMessage());
        }

        return 0;
    }

    /**
     * Retrieves upcoming visits scheduled for future dates.
     *
     * @return List of upcoming Visit objects
     */
    public List<Visit> getUpcomingVisits() {
        List<Visit> visits = new ArrayList<>();
        String sql = "SELECT * FROM visits WHERE scheduled_datetime > NOW() AND status = 'Scheduled' ORDER BY scheduled_datetime ASC";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Visit visit = createVisitFromResultSet(rs);
                visits.add(visit);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving upcoming visits: " + e.getMessage());
        }

        return visits;
    }

    /**
     * Gets comprehensive visit statistics.
     *
     * @return Array containing [totalVisits, completedVisits, scheduledVisits,
     * cancelledVisits]
     */
    public int[] getVisitStatistics() {
        String sql = "SELECT "
                + "COUNT(*) as total, "
                + "SUM(CASE WHEN status = 'Completed' THEN 1 ELSE 0 END) as completed, "
                + "SUM(CASE WHEN status = 'Scheduled' THEN 1 ELSE 0 END) as scheduled, "
                + "SUM(CASE WHEN status = 'Cancelled' THEN 1 ELSE 0 END) as cancelled "
                + "FROM visits";

        int totalVisits = 0;
        int completedVisits = 0;
        int scheduledVisits = 0;
        int cancelledVisits = 0;

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                totalVisits = rs.getInt("total");
                completedVisits = rs.getInt("completed");
                scheduledVisits = rs.getInt("scheduled");
                cancelledVisits = rs.getInt("cancelled");
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving visit statistics: " + e.getMessage());
        }

        return new int[]{totalVisits, completedVisits, scheduledVisits, cancelledVisits};
    }

    /**
     * Gets visit counts grouped by status.
     *
     * @return Array containing counts for each status
     */
    public int[] getVisitsByStatus() {
        String sql = "SELECT status, COUNT(*) as count FROM visits GROUP BY status";

        // Initialize counters
        int scheduled = 0;
        int inProgress = 0;
        int completed = 0;
        int cancelled = 0;
        int other = 0;

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String status = rs.getString("status");
                int count = rs.getInt("count");

                switch (status) {
                    case "Scheduled":
                        scheduled = count;
                        break;
                    case "In Progress":
                        inProgress = count;
                        break;
                    case "Completed":
                        completed = count;
                        break;
                    case "Cancelled":
                        cancelled = count;
                        break;
                    default:
                        other += count;
                        break;
                }
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving visits by status counts: " + e.getMessage());
        }

        return new int[]{scheduled, inProgress, completed, cancelled, other};
    }

    /**
     * Generates a unique visit identifier. Format: VISIT-YYYYMMDD-XXXX where X
     * is random alphanumeric characters.
     *
     * @return Unique visit identifier string
     */
    public String generateVisitId() {
        String prefix = "VISIT-";
        String date = java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd"));
        String random = java.util.UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return prefix + date + "-" + random;
    }

    /**
     * Searches for visits based on multiple criteria.
     *
     * @param prisonerId Prisoner ID to filter by (optional)
     * @param visitorId Visitor ID to filter by (optional)
     * @param status Status to filter by (optional)
     * @param dateFrom Start date for date range (optional)
     * @param dateTo End date for date range (optional)
     * @return List of Visit objects matching search criteria
     */
    public List<Visit> searchVisits(String prisonerId, String visitorId, String status,
            java.time.LocalDate dateFrom, java.time.LocalDate dateTo) {
        List<Visit> visits = new ArrayList<>();
        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM visits WHERE 1=1");
        List<Object> parameters = new ArrayList<>();

        // Add prisoner filter
        if (prisonerId != null && !prisonerId.trim().isEmpty()) {
            queryBuilder.append(" AND prisoner_id = ?");
            parameters.add(prisonerId);
        }

        // Add visitor filter
        if (visitorId != null && !visitorId.trim().isEmpty()) {
            queryBuilder.append(" AND visitor_id = ?");
            parameters.add(visitorId);
        }

        // Add status filter
        if (status != null && !status.equals("All")) {
            queryBuilder.append(" AND status = ?");
            parameters.add(status);
        }

        // Add date range filters
        if (dateFrom != null) {
            queryBuilder.append(" AND DATE(scheduled_datetime) >= ?");
            parameters.add(Date.valueOf(dateFrom));
        }

        if (dateTo != null) {
            queryBuilder.append(" AND DATE(scheduled_datetime) <= ?");
            parameters.add(Date.valueOf(dateTo));
        }

        queryBuilder.append(" ORDER BY scheduled_datetime DESC");

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(queryBuilder.toString())) {

            for (int i = 0; i < parameters.size(); i++) {
                stmt.setObject(i + 1, parameters.get(i));
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Visit visit = createVisitFromResultSet(rs);
                visits.add(visit);
            }

        } catch (SQLException e) {
            System.err.println("Error searching visits: " + e.getMessage());
        }

        return visits;
    }

    /**
     * Gets visits that are currently in progress.
     *
     * @return List of Visit objects currently in progress
     */
    public List<Visit> getInProgressVisits() {
        List<Visit> visits = new ArrayList<>();
        String sql = "SELECT * FROM visits WHERE status = 'In Progress' ORDER BY actual_start_datetime ASC";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Visit visit = createVisitFromResultSet(rs);
                visits.add(visit);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving in-progress visits: " + e.getMessage());
        }

        return visits;
    }

    /**
     * Gets visits that have exceeded their scheduled duration.
     *
     * @return List of Visit objects that are overdue
     */
    public List<Visit> getOverdueVisits() {
        List<Visit> visits = new ArrayList<>();
        String sql = "SELECT * FROM visits WHERE status = 'In Progress' AND "
                + "TIMESTAMPDIFF(MINUTE, actual_start_datetime, NOW()) > duration";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Visit visit = createVisitFromResultSet(rs);
                visits.add(visit);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving overdue visits: " + e.getMessage());
        }

        return visits;
    }

    /**
     * Creates a Visit object from a database ResultSet.
     *
     * @param resultSet ResultSet containing visit data
     * @return Visit object populated with data
     * @throws SQLException if database error occurs
     */
    private Visit createVisitFromResultSet(ResultSet resultSet) throws SQLException {
        Visit visit = new Visit(
                resultSet.getString("visit_id"),
                resultSet.getString("prisoner_id"),
                resultSet.getString("visitor_id"),
                resultSet.getTimestamp("scheduled_datetime").toLocalDateTime(),
                resultSet.getInt("duration")
        );

        visit.setStatus(resultSet.getString("status"));
        visit.setNotes(resultSet.getString("notes"));

        Timestamp actualStart = resultSet.getTimestamp("actual_start_datetime");
        if (actualStart != null) {
            visit.setActualStartTime(actualStart.toLocalDateTime());
        }

        Timestamp actualEnd = resultSet.getTimestamp("actual_end_datetime");
        if (actualEnd != null) {
            visit.setActualEndTime(actualEnd.toLocalDateTime());
        }

        return visit;
    }
}
