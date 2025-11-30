package database;

import model.Visit;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Visit entity Handles all database operations for
 * visits
 */
public class VisitDAO {

    /**
     * Add a new visit to the database
     *
     * @param visit Visit object to be added
     * @return true if successful, false otherwise
     */
    public boolean addVisit(Visit visit) {
        String sql = "INSERT INTO visits (visit_id, prisoner_id, visitor_id, scheduled_datetime, duration) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, visit.getVisitId());
            stmt.setString(2, visit.getPrisonerId());
            stmt.setString(3, visit.getVisitorId());
            stmt.setTimestamp(4, Timestamp.valueOf(visit.getVisitDateTime()));
            stmt.setInt(5, visit.getDuration());

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            System.err.println("Error adding visit: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieve all visits from the database
     *
     * @return List of all visits
     */
    public List<Visit> getAllVisits() {
        List<Visit> visits = new ArrayList<>();
        String sql = "SELECT * FROM visits";

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Visit visit = new Visit(
                        rs.getString("visit_id"),
                        rs.getString("prisoner_id"),
                        rs.getString("visitor_id"),
                        rs.getTimestamp("scheduled_datetime").toLocalDateTime(),
                        rs.getInt("duration")
                );
                visit.setStatus(rs.getString("status"));
                visit.setNotes(rs.getString("notes"));
                if (rs.getTimestamp("actual_start_datetime") != null) {
                    visit.setActualStartTime(rs.getTimestamp("actual_start_datetime").toLocalDateTime());
                }
                if (rs.getTimestamp("actual_end_datetime") != null) {
                    visit.setActualEndTime(rs.getTimestamp("actual_end_datetime").toLocalDateTime());
                }
                visits.add(visit);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving visits: " + e.getMessage());
        }

        return visits;
    }

    /**
     * Find visit by ID
     *
     * @param visitId ID of the visit to find
     * @return Visit object if found, null otherwise
     */
    public Visit getVisitById(String visitId) {
        String sql = "SELECT * FROM visits WHERE visit_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, visitId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Visit visit = new Visit(
                        rs.getString("visit_id"),
                        rs.getString("prisoner_id"),
                        rs.getString("visitor_id"),
                        rs.getTimestamp("scheduled_datetime").toLocalDateTime(),
                        rs.getInt("duration")
                );
                visit.setStatus(rs.getString("status"));
                visit.setNotes(rs.getString("notes"));
                if (rs.getTimestamp("actual_start_datetime") != null) {
                    visit.setActualStartTime(rs.getTimestamp("actual_start_datetime").toLocalDateTime());
                }
                if (rs.getTimestamp("actual_end_datetime") != null) {
                    visit.setActualEndTime(rs.getTimestamp("actual_end_datetime").toLocalDateTime());
                }
                return visit;
            }

        } catch (SQLException e) {
            System.err.println("Error finding visit: " + e.getMessage());
        }

        return null;
    }

    /**
     * Update visit information
     *
     * @param visit Visit object with updated data
     * @return true if successful, false otherwise
     */
    public boolean updateVisit(Visit visit) {
        String sql = "UPDATE visits SET prisoner_id=?, visitor_id=?, scheduled_datetime=?, duration=?, status=?, notes=? WHERE visit_id=?";

        try (Connection conn = DatabaseConnection.getConnection();
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
            System.err.println("Error updating visit: " + e.getMessage());
            return false;
        }
    }

    /**
     * Delete visit from database
     *
     * @param visitId ID of visit to delete
     * @return true if successful, false otherwise
     */
    public boolean deleteVisit(String visitId) {
        String sql = "DELETE FROM visits WHERE visit_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, visitId);
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting visit: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get visits by prisoner ID
     *
     * @param prisonerId Prisoner ID to filter by
     * @return List of visits for the specified prisoner
     */
    public List<Visit> getVisitsByPrisoner(String prisonerId) {
        List<Visit> visits = new ArrayList<>();
        String sql = "SELECT * FROM visits WHERE prisoner_id = ? ORDER BY scheduled_datetime DESC";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, prisonerId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Visit visit = new Visit(
                        rs.getString("visit_id"),
                        rs.getString("prisoner_id"),
                        rs.getString("visitor_id"),
                        rs.getTimestamp("scheduled_datetime").toLocalDateTime(),
                        rs.getInt("duration")
                );
                visit.setStatus(rs.getString("status"));
                visit.setNotes(rs.getString("notes"));
                if (rs.getTimestamp("actual_start_datetime") != null) {
                    visit.setActualStartTime(rs.getTimestamp("actual_start_datetime").toLocalDateTime());
                }
                if (rs.getTimestamp("actual_end_datetime") != null) {
                    visit.setActualEndTime(rs.getTimestamp("actual_end_datetime").toLocalDateTime());
                }
                visits.add(visit);
            }

        } catch (SQLException e) {
            System.err.println("Error getting visits by prisoner: " + e.getMessage());
        }

        return visits;
    }

    /**
     * Get visits by visitor ID
     *
     * @param visitorId Visitor ID to filter by
     * @return List of visits for the specified visitor
     */
    public List<Visit> getVisitsByVisitor(String visitorId) {
        List<Visit> visits = new ArrayList<>();
        String sql = "SELECT * FROM visits WHERE visitor_id = ? ORDER BY scheduled_datetime DESC";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, visitorId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Visit visit = new Visit(
                        rs.getString("visit_id"),
                        rs.getString("prisoner_id"),
                        rs.getString("visitor_id"),
                        rs.getTimestamp("scheduled_datetime").toLocalDateTime(),
                        rs.getInt("duration")
                );
                visit.setStatus(rs.getString("status"));
                visit.setNotes(rs.getString("notes"));
                if (rs.getTimestamp("actual_start_datetime") != null) {
                    visit.setActualStartTime(rs.getTimestamp("actual_start_datetime").toLocalDateTime());
                }
                if (rs.getTimestamp("actual_end_datetime") != null) {
                    visit.setActualEndTime(rs.getTimestamp("actual_end_datetime").toLocalDateTime());
                }
                visits.add(visit);
            }

        } catch (SQLException e) {
            System.err.println("Error getting visits by visitor: " + e.getMessage());
        }

        return visits;
    }

    /**
     * Get visits by status
     *
     * @param status Status to filter by
     * @return List of visits with specified status
     */
    public List<Visit> getVisitsByStatus(String status) {
        List<Visit> visits = new ArrayList<>();
        String sql = "SELECT * FROM visits WHERE status = ? ORDER BY scheduled_datetime DESC";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Visit visit = new Visit(
                        rs.getString("visit_id"),
                        rs.getString("prisoner_id"),
                        rs.getString("visitor_id"),
                        rs.getTimestamp("scheduled_datetime").toLocalDateTime(),
                        rs.getInt("duration")
                );
                visit.setStatus(rs.getString("status"));
                visit.setNotes(rs.getString("notes"));
                if (rs.getTimestamp("actual_start_datetime") != null) {
                    visit.setActualStartTime(rs.getTimestamp("actual_start_datetime").toLocalDateTime());
                }
                if (rs.getTimestamp("actual_end_datetime") != null) {
                    visit.setActualEndTime(rs.getTimestamp("actual_end_datetime").toLocalDateTime());
                }
                visits.add(visit);
            }

        } catch (SQLException e) {
            System.err.println("Error getting visits by status: " + e.getMessage());
        }

        return visits;
    }

    /**
     * Get today's scheduled visits
     *
     * @return List of visits scheduled for today
     */
    public List<Visit> getTodaysVisits() {
        List<Visit> visits = new ArrayList<>();
        String sql = "SELECT * FROM visits WHERE DATE(scheduled_datetime) = CURDATE() ORDER BY scheduled_datetime ASC";

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Visit visit = new Visit(
                        rs.getString("visit_id"),
                        rs.getString("prisoner_id"),
                        rs.getString("visitor_id"),
                        rs.getTimestamp("scheduled_datetime").toLocalDateTime(),
                        rs.getInt("duration")
                );
                visit.setStatus(rs.getString("status"));
                visit.setNotes(rs.getString("notes"));
                if (rs.getTimestamp("actual_start_datetime") != null) {
                    visit.setActualStartTime(rs.getTimestamp("actual_start_datetime").toLocalDateTime());
                }
                if (rs.getTimestamp("actual_end_datetime") != null) {
                    visit.setActualEndTime(rs.getTimestamp("actual_end_datetime").toLocalDateTime());
                }
                visits.add(visit);
            }

        } catch (SQLException e) {
            System.err.println("Error getting today's visits: " + e.getMessage());
        }

        return visits;
    }

    /**
     * Start a visit (mark as in progress)
     *
     * @param visitId ID of visit to start
     * @return true if successful, false otherwise
     */
    public boolean startVisit(String visitId) {
        String sql = "UPDATE visits SET status = 'In Progress', actual_start_datetime = NOW() WHERE visit_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
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
     * Complete a visit (mark as completed)
     *
     * @param visitId ID of visit to complete
     * @return true if successful, false otherwise
     */
    public boolean completeVisit(String visitId) {
        String sql = "UPDATE visits SET status = 'Completed', actual_end_datetime = NOW() WHERE visit_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, visitId);
            int rowsUpdated = stmt.executeUpdate();

            // Also record the visit in visitor's history
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
     * Cancel a visit
     *
     * @param visitId ID of visit to cancel
     * @param cancellationNotes Reason for cancellation
     * @return true if successful, false otherwise
     */
    public boolean cancelVisit(String visitId, String cancellationNotes) {
        String sql = "UPDATE visits SET status = 'Cancelled', notes = ? WHERE visit_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
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
     * Get total number of visits
     *
     * @return Total count of visits
     */
    public int getTotalVisits() {
        String sql = "SELECT COUNT(*) as total FROM visits";

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt("total");
            }

        } catch (SQLException e) {
            System.err.println("Error getting total visits: " + e.getMessage());
        }

        return 0;
    }

    /**
     * Get completed visits count
     *
     * @return Number of completed visits
     */
    public int getCompletedVisitsCount() {
        String sql = "SELECT COUNT(*) as total FROM visits WHERE status = 'Completed'";

        try (Connection conn = DatabaseConnection.getConnection();
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
     * Get upcoming visits (scheduled for future dates)
     *
     * @return List of upcoming visits
     */
    public List<Visit> getUpcomingVisits() {
        List<Visit> visits = new ArrayList<>();
        String sql = "SELECT * FROM visits WHERE scheduled_datetime > NOW() AND status = 'Scheduled' ORDER BY scheduled_datetime ASC";

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Visit visit = new Visit(
                        rs.getString("visit_id"),
                        rs.getString("prisoner_id"),
                        rs.getString("visitor_id"),
                        rs.getTimestamp("scheduled_datetime").toLocalDateTime(),
                        rs.getInt("duration")
                );
                visit.setStatus(rs.getString("status"));
                visit.setNotes(rs.getString("notes"));
                visits.add(visit);
            }

        } catch (SQLException e) {
            System.err.println("Error getting upcoming visits: " + e.getMessage());
        }

        return visits;
    }

    /**
     * Get visit statistics
     *
     * @return Array with [totalVisits, completedVisits, scheduledVisits,
     * cancelledVisits]
     */
    public int[] getVisitStatistics() {
        int totalVisits = getTotalVisits();
        int completedVisits = getCompletedVisitsCount();

        String sqlScheduled = "SELECT COUNT(*) as total FROM visits WHERE status = 'Scheduled'";
        String sqlCancelled = "SELECT COUNT(*) as total FROM visits WHERE status = 'Cancelled'";

        int scheduledVisits = 0;
        int cancelledVisits = 0;

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs1 = stmt.executeQuery(sqlScheduled);
                ResultSet rs2 = stmt.executeQuery(sqlCancelled)) {

            if (rs1.next()) {
                scheduledVisits = rs1.getInt("total");
            }
            if (rs2.next()) {
                cancelledVisits = rs2.getInt("total");
            }

        } catch (SQLException e) {
            System.err.println("Error getting visit statistics: " + e.getMessage());
        }

        return new int[]{totalVisits, completedVisits, scheduledVisits, cancelledVisits};
    }
}
