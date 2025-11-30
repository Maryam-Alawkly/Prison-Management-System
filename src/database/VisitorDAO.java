package database;

import model.Visitor;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Visitor entity Handles all database operations for
 * visitors
 */
public class VisitorDAO {

    /**
     * Add a new visitor to the database
     *
     * @param visitor Visitor object to be added
     * @return true if successful, false otherwise
     */
    public boolean addVisitor(Visitor visitor) {
        String sql = "INSERT INTO visitors (visitor_id, name, phone, relationship, prisoner_id) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, visitor.getId());
            stmt.setString(2, visitor.getName());
            stmt.setString(3, visitor.getPhone());
            stmt.setString(4, visitor.getRelationship());
            stmt.setString(5, visitor.getPrisonerId());

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            System.err.println("Error adding visitor: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieve all visitors from the database
     *
     * @return List of all visitors
     */
    public List<Visitor> getAllVisitors() {
        List<Visitor> visitors = new ArrayList<>();
        String sql = "SELECT * FROM visitors";

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Visitor visitor = new Visitor(
                        rs.getString("visitor_id"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("relationship"),
                        rs.getString("prisoner_id")
                );
                visitor.setVisitCount(rs.getInt("visit_count"));
                visitor.setLastVisitDate(rs.getString("last_visit_date"));
                visitor.setStatus(rs.getString("status"));
                visitors.add(visitor);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving visitors: " + e.getMessage());
        }

        return visitors;
    }

    /**
     * Find visitor by ID
     *
     * @param visitorId ID of the visitor to find
     * @return Visitor object if found, null otherwise
     */
    public Visitor getVisitorById(String visitorId) {
        String sql = "SELECT * FROM visitors WHERE visitor_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, visitorId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Visitor visitor = new Visitor(
                        rs.getString("visitor_id"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("relationship"),
                        rs.getString("prisoner_id")
                );
                visitor.setVisitCount(rs.getInt("visit_count"));
                visitor.setLastVisitDate(rs.getString("last_visit_date"));
                visitor.setStatus(rs.getString("status"));
                return visitor;
            }

        } catch (SQLException e) {
            System.err.println("Error finding visitor: " + e.getMessage());
        }

        return null;
    }

    /**
     * Update visitor information
     *
     * @param visitor Visitor object with updated data* @return true if
     * successful, false otherwise
     */
    public boolean updateVisitor(Visitor visitor) {
        String sql = "UPDATE visitors SET name=?, phone=?, relationship=?, prisoner_id=?, status=? WHERE visitor_id=?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, visitor.getName());
            stmt.setString(2, visitor.getPhone());
            stmt.setString(3, visitor.getRelationship());
            stmt.setString(4, visitor.getPrisonerId());
            stmt.setString(5, visitor.getStatus());
            stmt.setString(6, visitor.getId());

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            System.err.println("Error updating visitor: " + e.getMessage());
            return false;
        }
    }

    /**
     * Delete visitor from database
     *
     * @param visitorId ID of visitor to delete
     * @return true if successful, false otherwise
     */
    public boolean deleteVisitor(String visitorId) {
        String sql = "DELETE FROM visitors WHERE visitor_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, visitorId);
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting visitor: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get visitors by prisoner ID
     *
     * @param prisonerId Prisoner ID to filter by
     * @return List of visitors for the specified prisoner
     */
    public List<Visitor> getVisitorsByPrisoner(String prisonerId) {
        List<Visitor> visitors = new ArrayList<>();
        String sql = "SELECT * FROM visitors WHERE prisoner_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, prisonerId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Visitor visitor = new Visitor(
                        rs.getString("visitor_id"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("relationship"),
                        rs.getString("prisoner_id")
                );
                visitor.setVisitCount(rs.getInt("visit_count"));
                visitor.setLastVisitDate(rs.getString("last_visit_date"));
                visitor.setStatus(rs.getString("status"));
                visitors.add(visitor);
            }

        } catch (SQLException e) {
            System.err.println("Error getting visitors by prisoner: " + e.getMessage());
        }

        return visitors;
    }

    /**
     * Get visitors by status
     *
     * @param status Status to filter by
     * @return List of visitors with specified status
     */
    public List<Visitor> getVisitorsByStatus(String status) {
        List<Visitor> visitors = new ArrayList<>();
        String sql = "SELECT * FROM visitors WHERE status = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Visitor visitor = new Visitor(
                        rs.getString("visitor_id"),
                        rs.getString("name"), rs.getString("phone"),
                        rs.getString("relationship"),
                        rs.getString("prisoner_id")
                );
                visitor.setVisitCount(rs.getInt("visit_count"));
                visitor.setLastVisitDate(rs.getString("last_visit_date"));
                visitor.setStatus(rs.getString("status"));
                visitors.add(visitor);
            }

        } catch (SQLException e) {
            System.err.println("Error getting visitors by status: " + e.getMessage());
        }

        return visitors;
    }

    /**
     * Approve visitor
     *
     * @param visitorId ID of visitor to approve
     * @return true if successful, false otherwise
     */
    public boolean approveVisitor(String visitorId) {
        String sql = "UPDATE visitors SET status = 'Approved' WHERE visitor_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, visitorId);
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            System.err.println("Error approving visitor: " + e.getMessage());
            return false;
        }
    }

    /**
     * Ban visitor
     *
     * @param visitorId ID of visitor to ban
     * @return true if successful, false otherwise
     */
    public boolean banVisitor(String visitorId) {
        String sql = "UPDATE visitors SET status = 'Banned' WHERE visitor_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, visitorId);
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            System.err.println("Error banning visitor: " + e.getMessage());
            return false;
        }
    }

    /**
     * Record a visit for a visitor (increment visit count and update last visit
     * date)
     *
     * @param visitorId ID of visitor
     * @return true if successful, false otherwise
     */
    public boolean recordVisit(String visitorId) {
        String sql = "UPDATE visitors SET visit_count = visit_count + 1, last_visit_date = CURDATE() WHERE visitor_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, visitorId);
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            System.err.println("Error recording visit: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get total number of visitors
     *
     * @return Total count of visitors
     */
    public int getTotalVisitors() {
        String sql = "SELECT COUNT(*) as total FROM visitors";

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt("total");
            }

        } catch (SQLException e) {
            System.err.println("Error getting total visitors: " + e.getMessage());
        }

        return 0;
    }

    /**
     * Get approved visitors count
     *
     * @return Number of approved visitors
     */
    public int getApprovedVisitorsCount() {
        String sql = "SELECT COUNT(*) as total FROM visitors WHERE status = 'Approved'";

        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt("total");
            }

        } catch (SQLException e) {
            System.err.println("Error getting approved visitors count: " + e.getMessage());
        }

        return 0;
    }

    /**
     * Get visitors with most visits
     *
     * @param limit Maximum number of visitors to return
     * @return List of visitors sorted by visit count descending
     */
    public List<Visitor> getTopVisitors(int limit) {
        List<Visitor> visitors = new ArrayList<>();
        String sql = "SELECT * FROM visitors ORDER BY visit_count DESC LIMIT ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, limit);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Visitor visitor = new Visitor(
                        rs.getString("visitor_id"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("relationship"),
                        rs.getString("prisoner_id")
                );
                visitor.setVisitCount(rs.getInt("visit_count"));
                visitor.setLastVisitDate(rs.getString("last_visit_date"));
                visitor.setStatus(rs.getString("status"));
                visitors.add(visitor);
            }

        } catch (SQLException e) {
            System.err.println("Error getting top visitors: " + e.getMessage());
        }

        return visitors;
    }
}
