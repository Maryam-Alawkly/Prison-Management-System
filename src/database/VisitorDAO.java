package database;

import model.Visitor;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Visitor entity. Implements Singleton pattern for DAO
 * instance management.
 */
public class VisitorDAO {

    // Singleton instance
    private static VisitorDAO instance;

    /**
     * Private constructor to enforce Singleton pattern.
     */
    private VisitorDAO() {
        // Private constructor to prevent instantiation
    }

    /**
     * Returns the single instance of VisitorDAO.
     *
     * @return VisitorDAO instance
     */
    public static synchronized VisitorDAO getInstance() {
        if (instance == null) {
            instance = new VisitorDAO();
        }
        return instance;
    }

    /**
     * Adds a new visitor to the database.
     *
     * @param visitor Visitor object containing visitor details
     * @return true if visitor was added successfully, false otherwise
     */
    public boolean addVisitor(Visitor visitor) {
        String sql = "INSERT INTO visitors (visitor_id, name, phone, relationship, prisoner_id) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, visitor.getId());
            stmt.setString(2, visitor.getName());
            stmt.setString(3, visitor.getPhone());
            stmt.setString(4, visitor.getRelationship());
            stmt.setString(5, visitor.getPrisonerId());

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            System.err.println("Error adding visitor to database: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves all visitors from the database.
     *
     * @return List of all Visitor objects
     */
    public List<Visitor> getAllVisitors() {
        List<Visitor> visitors = new ArrayList<>();
        String sql = "SELECT * FROM visitors";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Visitor visitor = createVisitorFromResultSet(rs);
                visitors.add(visitor);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving all visitors: " + e.getMessage());
        }

        return visitors;
    }

    /**
     * Finds a visitor by their unique identifier.
     *
     * @param visitorId Unique identifier of the visitor
     * @return Visitor object if found, null otherwise
     */
    public Visitor getVisitorById(String visitorId) {
        String sql = "SELECT * FROM visitors WHERE visitor_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, visitorId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return createVisitorFromResultSet(rs);
            }

        } catch (SQLException e) {
            System.err.println("Error finding visitor by ID: " + e.getMessage());
        }

        return null;
    }

    /**
     * Updates visitor information in the database.
     *
     * @param visitor Visitor object with updated data
     * @return true if update was successful, false otherwise
     */
    public boolean updateVisitor(Visitor visitor) {
        String sql = "UPDATE visitors SET name=?, phone=?, relationship=?, prisoner_id=?, status=? WHERE visitor_id=?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
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
            System.err.println("Error updating visitor information: " + e.getMessage());
            return false;
        }
    }

    /**
     * Deletes a visitor from the database.
     *
     * @param visitorId Unique identifier of the visitor to delete
     * @return true if deletion was successful, false otherwise
     */
    public boolean deleteVisitor(String visitorId) {
        String sql = "DELETE FROM visitors WHERE visitor_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, visitorId);
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting visitor from database: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves visitors associated with a specific prisoner.
     *
     * @param prisonerId Unique identifier of the prisoner
     * @return List of Visitor objects for the specified prisoner
     */
    public List<Visitor> getVisitorsByPrisoner(String prisonerId) {
        List<Visitor> visitors = new ArrayList<>();
        String sql = "SELECT * FROM visitors WHERE prisoner_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, prisonerId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Visitor visitor = createVisitorFromResultSet(rs);
                visitors.add(visitor);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving visitors by prisoner: " + e.getMessage());
        }

        return visitors;
    }

    /**
     * Retrieves visitors by their status.
     *
     * @param status Status to filter by
     * @return List of Visitor objects with specified status
     */
    public List<Visitor> getVisitorsByStatus(String status) {
        List<Visitor> visitors = new ArrayList<>();
        String sql = "SELECT * FROM visitors WHERE status = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
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
            System.err.println("Error retrieving visitors by status: " + e.getMessage());
        }

        return visitors;
    }

    /**
     * Approves a visitor by updating their status to 'Approved'.
     *
     * @param visitorId Unique identifier of the visitor to approve
     * @return true if approval was successful, false otherwise
     */
    public boolean approveVisitor(String visitorId) {
        String sql = "UPDATE visitors SET status = 'Approved' WHERE visitor_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
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
     * Bans a visitor by updating their status to 'Banned'.
     *
     * @param visitorId Unique identifier of the visitor to ban
     * @return true if ban was successful, false otherwise
     */
    public boolean banVisitor(String visitorId) {
        String sql = "UPDATE visitors SET status = 'Banned' WHERE visitor_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
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
     * Records a visit for a visitor by incrementing visit count and updating
     * last visit date.
     *
     * @param visitorId Unique identifier of the visitor
     * @return true if visit was recorded successfully, false otherwise
     */
    public boolean recordVisit(String visitorId) {
        String sql = "UPDATE visitors SET visit_count = visit_count + 1, last_visit_date = CURDATE() WHERE visitor_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
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
     * Gets the total number of visitors in the database.
     *
     * @return Total count of visitors
     */
    public int getTotalVisitors() {
        String sql = "SELECT COUNT(*) as total FROM visitors";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt("total");
            }

        } catch (SQLException e) {
            System.err.println("Error getting total visitors count: " + e.getMessage());
        }

        return 0;
    }

    /**
     * Gets the number of approved visitors.
     *
     * @return Number of approved visitors
     */
    public int getApprovedVisitorsCount() {
        String sql = "SELECT COUNT(*) as total FROM visitors WHERE status = 'Approved'";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                Statement stmt = conn.createStatement();
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
     * Retrieves the top visitors with the most visits.
     *
     * @param limit Maximum number of visitors to return
     * @return List of Visitor objects sorted by visit count descending
     */
    public List<Visitor> getTopVisitors(int limit) {
        List<Visitor> visitors = new ArrayList<>();
        String sql = "SELECT * FROM visitors ORDER BY visit_count DESC LIMIT ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, limit);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Visitor visitor = createVisitorFromResultSet(rs);
                visitors.add(visitor);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving top visitors: " + e.getMessage());
        }

        return visitors;
    }

    /**
     * Searches for visitors by name or phone number.
     *
     * @param searchTerm Search term to match against visitor name or phone
     * @return List of Visitor objects matching search criteria
     */
    public List<Visitor> searchVisitors(String searchTerm) {
        List<Visitor> visitors = new ArrayList<>();
        String sql = "SELECT * FROM visitors WHERE name LIKE ? OR phone LIKE ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            String searchPattern = "%" + searchTerm + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Visitor visitor = createVisitorFromResultSet(rs);
                visitors.add(visitor);
            }

        } catch (SQLException e) {
            System.err.println("Error searching visitors: " + e.getMessage());
        }

        return visitors;
    }

    /**
     * Generates a unique visitor identifier. Format: VIS-XXXX where X is random
     * alphanumeric characters.
     *
     * @return Unique visitor identifier string
     */
    public String generateVisitorId() {
        String prefix = "VIS-";
        String random = java.util.UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return prefix + random;
    }

    /**
     * Gets visitor statistics including counts by status.
     *
     * @return Object array containing [total, approved, pending, banned]
     */
    public Object[] getVisitorStatistics() {
        String sql = "SELECT "
                + "COUNT(*) as total, "
                + "SUM(CASE WHEN status = 'Approved' THEN 1 ELSE 0 END) as approved, "
                + "SUM(CASE WHEN status = 'Pending' THEN 1 ELSE 0 END) as pending, "
                + "SUM(CASE WHEN status = 'Banned' THEN 1 ELSE 0 END) as banned "
                + "FROM visitors";

        int total = 0;
        int approved = 0;
        int pending = 0;
        int banned = 0;

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                total = rs.getInt("total");
                approved = rs.getInt("approved");
                pending = rs.getInt("pending");
                banned = rs.getInt("banned");
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving visitor statistics: " + e.getMessage());
        }

        return new Object[]{total, approved, pending, banned};
    }

    /**
     * Gets visitors who haven't visited within a specified number of days.
     *
     * @param days Number of days threshold
     * @return List of Visitor objects who haven't visited within specified days
     */
    public List<Visitor> getInactiveVisitors(int days) {
        List<Visitor> visitors = new ArrayList<>();
        String sql = "SELECT * FROM visitors WHERE last_visit_date < DATE_SUB(CURDATE(), INTERVAL ? DAY) "
                + "OR last_visit_date IS NULL";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, days);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Visitor visitor = createVisitorFromResultSet(rs);
                visitors.add(visitor);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving inactive visitors: " + e.getMessage());
        }

        return visitors;
    }

    /**
     * Updates a visitor's relationship with a prisoner.
     *
     * @param visitorId Unique identifier of the visitor
     * @param prisonerId New prisoner identifier to associate with
     * @param relationship New relationship description
     * @return true if update was successful, false otherwise
     */
    public boolean updateVisitorRelationship(String visitorId, String prisonerId, String relationship) {
        String sql = "UPDATE visitors SET prisoner_id = ?, relationship = ? WHERE visitor_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, prisonerId);
            stmt.setString(2, relationship);
            stmt.setString(3, visitorId);

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            System.err.println("Error updating visitor relationship: " + e.getMessage());
            return false;
        }
    }

    /**
     * Gets the number of visits for a specific visitor.
     *
     * @param visitorId Unique identifier of the visitor
     * @return Number of visits recorded for the visitor
     */
    public int getVisitorVisitCount(String visitorId) {
        String sql = "SELECT visit_count FROM visitors WHERE visitor_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, visitorId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("visit_count");
            }

        } catch (SQLException e) {
            System.err.println("Error getting visitor visit count: " + e.getMessage());
        }

        return 0;
    }

    /**
     * Creates a Visitor object from a database ResultSet.
     *
     * @param resultSet ResultSet containing visitor data
     * @return Visitor object populated with data
     * @throws SQLException if database error occurs
     */
    private Visitor createVisitorFromResultSet(ResultSet resultSet) throws SQLException {
        Visitor visitor = new Visitor(
                resultSet.getString("visitor_id"),
                resultSet.getString("name"),
                resultSet.getString("phone"),
                resultSet.getString("relationship"),
                resultSet.getString("prisoner_id")
        );

        visitor.setVisitCount(resultSet.getInt("visit_count"));
        visitor.setLastVisitDate(resultSet.getString("last_visit_date"));
        visitor.setStatus(resultSet.getString("status"));

        return visitor;
    }
}
