package database;

import model.PrisonCell;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for PrisonCell entity. Handles all database operations for
 * prison cell management including cell creation, occupancy tracking, status
 * management, and statistical analysis. Uses Singleton pattern for database
 * connection management.
 */
public class PrisonCellDAO {

    /**
     * Adds a new prison cell to the database.
     *
     * @param cell PrisonCell object containing cell details
     * @return true if cell was added successfully, false otherwise
     */
    public boolean addPrisonCell(PrisonCell cell) {
        String sql = "INSERT INTO cells (cell_number, cell_type, capacity, security_level) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cell.getPrisonCellNumber());
            stmt.setString(2, cell.getPrisonCellType());
            stmt.setInt(3, cell.getCapacity());
            stmt.setString(4, cell.getSecurityLevel());

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            System.err.println("Error adding prison cell to database: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves all prison cells from the database.
     *
     * @return List of all PrisonCell objects
     */
    public List<PrisonCell> getAllPrisonCells() {
        List<PrisonCell> cells = new ArrayList<>();
        String sql = "SELECT * FROM cells";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                PrisonCell cell = new PrisonCell(
                        rs.getString("cell_number"),
                        rs.getString("cell_type"),
                        rs.getInt("capacity"),
                        rs.getString("security_level")
                );
                cell.setCurrentOccupancy(rs.getInt("current_occupancy"));
                cell.setStatus(rs.getString("status"));
                cells.add(cell);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving all prison cells: " + e.getMessage());
        }

        return cells;
    }

    /**
     * Finds a prison cell by its cell number.
     *
     * @param cellNumber Unique identifier of the cell
     * @return PrisonCell object if found, null otherwise
     */
    public PrisonCell getPrisonCellByNumber(String cellNumber) {
        String sql = "SELECT * FROM cells WHERE cell_number = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cellNumber);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                PrisonCell cell = new PrisonCell(
                        rs.getString("cell_number"),
                        rs.getString("cell_type"),
                        rs.getInt("capacity"),
                        rs.getString("security_level")
                );
                cell.setCurrentOccupancy(rs.getInt("current_occupancy"));
                cell.setStatus(rs.getString("status"));
                return cell;
            }

        } catch (SQLException e) {
            System.err.println("Error finding prison cell by number: " + e.getMessage());
        }

        return null;
    }

    /**
     * Updates prison cell information in the database.
     *
     * @param cell PrisonCell object with updated data
     * @return true if update was successful, false otherwise
     */
    public boolean updatePrisonCell(PrisonCell cell) {
        String sql = "UPDATE cells SET cell_type=?, capacity=?, security_level=?, status=? WHERE cell_number=?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cell.getPrisonCellType());
            stmt.setInt(2, cell.getCapacity());
            stmt.setString(3, cell.getSecurityLevel());
            stmt.setString(4, cell.getStatus());
            stmt.setString(5, cell.getPrisonCellNumber());

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            System.err.println("Error updating prison cell information: " + e.getMessage());
            return false;
        }
    }

    /**
     * Deletes a prison cell from the database.
     *
     * @param cellNumber Unique identifier of the cell to delete
     * @return true if deletion was successful, false otherwise
     */
    public boolean deletePrisonCell(String cellNumber) {
        String sql = "DELETE FROM cells WHERE cell_number = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cellNumber);
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting prison cell from database: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves prison cells by security level.
     *
     * @param securityLevel Security level to filter by
     * @return List of PrisonCell objects with specified security level
     */
    public List<PrisonCell> getPrisonCellsBySecurityLevel(String securityLevel) {
        List<PrisonCell> cells = new ArrayList<>();
        String sql = "SELECT * FROM cells WHERE security_level = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, securityLevel);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                PrisonCell cell = new PrisonCell(
                        rs.getString("cell_number"),
                        rs.getString("cell_type"),
                        rs.getInt("capacity"),
                        rs.getString("security_level")
                );
                cell.setCurrentOccupancy(rs.getInt("current_occupancy"));
                cell.setStatus(rs.getString("status"));
                cells.add(cell);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving cells by security level: " + e.getMessage());
        }

        return cells;
    }

    /**
     * Retrieves prison cells by status.
     *
     * @param status Status to filter by
     * @return List of PrisonCell objects with specified status
     */
    public List<PrisonCell> getPrisonCellsByStatus(String status) {
        List<PrisonCell> cells = new ArrayList<>();
        String sql = "SELECT * FROM cells WHERE status = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                PrisonCell cell = new PrisonCell(
                        rs.getString("cell_number"),
                        rs.getString("cell_type"),
                        rs.getInt("capacity"),
                        rs.getString("security_level")
                );
                cell.setCurrentOccupancy(rs.getInt("current_occupancy"));
                cell.setStatus(rs.getString("status"));
                cells.add(cell);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving cells by status: " + e.getMessage());
        }

        return cells;
    }

    /**
     * Retrieves available prison cells with space for additional occupants.
     * Excludes cells under maintenance.
     *
     * @return List of available PrisonCell objects
     */
    public List<PrisonCell> getAvailablePrisonCells() {
        List<PrisonCell> cells = new ArrayList<>();
        String sql = "SELECT * FROM cells WHERE current_occupancy < capacity AND status != 'Under Maintenance'";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                PrisonCell cell = new PrisonCell(
                        rs.getString("cell_number"),
                        rs.getString("cell_type"),
                        rs.getInt("capacity"),
                        rs.getString("security_level")
                );
                cell.setCurrentOccupancy(rs.getInt("current_occupancy"));
                cell.setStatus(rs.getString("status"));
                cells.add(cell);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving available prison cells: " + e.getMessage());
        }

        return cells;
    }

    /**
     * Updates the occupancy count for a specific cell.
     *
     * @param cellNumber Unique identifier of the cell
     * @param newOccupancy New occupancy count
     * @return true if update was successful, false otherwise
     */
    public boolean updatePrisonCellOccupancy(String cellNumber, int newOccupancy) {
        String sql = "UPDATE cells SET current_occupancy = ? WHERE cell_number = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, newOccupancy);
            stmt.setString(2, cellNumber);

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            System.err.println("Error updating cell occupancy: " + e.getMessage());
            return false;
        }
    }

    /**
     * Increments the occupancy count of a cell by 1. Only increments if the
     * cell has available space.
     *
     * @param cellNumber Unique identifier of the cell
     * @return true if increment was successful, false otherwise
     */
    public boolean incrementPrisonCellOccupancy(String cellNumber) {
        PrisonCell cell = getPrisonCellByNumber(cellNumber);
        if (cell != null && cell.hasAvailableSpace()) {
            return updatePrisonCellOccupancy(cellNumber, cell.getCurrentOccupancy() + 1);
        }
        return false;
    }

    /**
     * Decrements the occupancy count of a cell by 1. Only decrements if the
     * cell has at least one occupant.
     *
     * @param cellNumber Unique identifier of the cell
     * @return true if decrement was successful, false otherwise
     */
    public boolean decrementPrisonCellOccupancy(String cellNumber) {
        PrisonCell cell = getPrisonCellByNumber(cellNumber);
        if (cell != null && cell.getCurrentOccupancy() > 0) {
            return updatePrisonCellOccupancy(cellNumber, cell.getCurrentOccupancy() - 1);
        }
        return false;
    }

    /**
     * Sets a cell to "Under Maintenance" status.
     *
     * @param cellNumber Unique identifier of the cell
     * @return true if status update was successful, false otherwise
     */
    public boolean setPrisonCellUnderMaintenance(String cellNumber) {
        String sql = "UPDATE cells SET status = 'Under Maintenance' WHERE cell_number = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cellNumber);
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            System.err.println("Error setting cell under maintenance: " + e.getMessage());
            return false;
        }
    }

    /**
     * Removes a cell from maintenance status.
     *
     * @param cellNumber Unique identifier of the cell
     * @param newStatus New status to set (usually "Operational")
     * @return true if status update was successful, false otherwise
     */
    public boolean removeFromMaintenance(String cellNumber, String newStatus) {
        String sql = "UPDATE cells SET status = ? WHERE cell_number = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newStatus);
            stmt.setString(2, cellNumber);
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            System.err.println("Error removing cell from maintenance: " + e.getMessage());
            return false;
        }
    }

    /**
     * Gets the total number of prison cells.
     *
     * @return Total count of prison cells
     */
    public int getTotalPrisonCells() {
        String sql = "SELECT COUNT(*) as total FROM cells";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt("total");
            }

        } catch (SQLException e) {
            System.err.println("Error getting total prison cells count: " + e.getMessage());
        }

        return 0;
    }

    /**
     * Gets the total capacity across all prison cells.
     *
     * @return Total capacity of all cells
     */
    public int getTotalCapacity() {
        String sql = "SELECT SUM(capacity) as total_capacity FROM cells";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt("total_capacity");
            }

        } catch (SQLException e) {
            System.err.println("Error getting total prison capacity: " + e.getMessage());
        }

        return 0;
    }

    /**
     * Gets the total current occupancy across all prison cells.
     *
     * @return Total current occupancy
     */
    public int getTotalOccupancy() {
        String sql = "SELECT SUM(current_occupancy) as total_occupancy FROM cells";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt("total_occupancy");
            }

        } catch (SQLException e) {
            System.err.println("Error getting total prison occupancy: " + e.getMessage());
        }

        return 0;
    }

    /**
     * Gets occupancy statistics for the prison.
     *
     * @return Array containing [totalCapacity, totalOccupancy, availableSpace]
     */
    public int[] getOccupancyStatistics() {
        int totalCapacity = getTotalCapacity();
        int totalOccupancy = getTotalOccupancy();
        int availableSpace = totalCapacity - totalOccupancy;

        return new int[]{totalCapacity, totalOccupancy, availableSpace};
    }

    /**
     * Gets the utilization percentage of prison cells.
     *
     * @return Utilization percentage (0-100), or -1 if calculation fails
     */
    public double getUtilizationPercentage() {
        int totalCapacity = getTotalCapacity();
        int totalOccupancy = getTotalOccupancy();

        if (totalCapacity > 0) {
            return ((double) totalOccupancy / totalCapacity) * 100.0;
        }

        return 0.0;
    }

    /**
     * Searches for prison cells by cell number or type.
     *
     * @param searchTerm Search term to match against cell number or type
     * @return List of PrisonCell objects matching search criteria
     */
    public List<PrisonCell> searchPrisonCells(String searchTerm) {
        List<PrisonCell> cells = new ArrayList<>();
        String sql = "SELECT * FROM cells WHERE cell_number LIKE ? OR cell_type LIKE ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            String searchPattern = "%" + searchTerm + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                PrisonCell cell = new PrisonCell(
                        rs.getString("cell_number"),
                        rs.getString("cell_type"),
                        rs.getInt("capacity"),
                        rs.getString("security_level")
                );
                cell.setCurrentOccupancy(rs.getInt("current_occupancy"));
                cell.setStatus(rs.getString("status"));
                cells.add(cell);
            }

        } catch (SQLException e) {
            System.err.println("Error searching prison cells: " + e.getMessage());
        }

        return cells;
    }

    /**
     * Gets cells that are at or near capacity.
     *
     * @param threshold Percentage threshold for capacity warning (e.g., 90 for
     * 90%)
     * @return List of PrisonCell objects at or above capacity threshold
     */
    public List<PrisonCell> getCellsNearCapacity(double threshold) {
        List<PrisonCell> cells = new ArrayList<>();
        String sql = "SELECT * FROM cells WHERE (current_occupancy * 100.0 / capacity) >= ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, threshold);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                PrisonCell cell = new PrisonCell(
                        rs.getString("cell_number"),
                        rs.getString("cell_type"),
                        rs.getInt("capacity"),
                        rs.getString("security_level")
                );
                cell.setCurrentOccupancy(rs.getInt("current_occupancy"));
                cell.setStatus(rs.getString("status"));
                cells.add(cell);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving cells near capacity: " + e.getMessage());
        }

        return cells;
    }

    /**
     * Gets cells with available capacity for a given number of prisoners.
     *
     * @param requiredSpaces Number of spaces needed
     * @return List of PrisonCell objects with sufficient available space
     */
    public List<PrisonCell> getCellsWithAvailableSpace(int requiredSpaces) {
        List<PrisonCell> cells = new ArrayList<>();
        String sql = "SELECT * FROM cells WHERE (capacity - current_occupancy) >= ? AND status != 'Under Maintenance'";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, requiredSpaces);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                PrisonCell cell = new PrisonCell(
                        rs.getString("cell_number"),
                        rs.getString("cell_type"),
                        rs.getInt("capacity"),
                        rs.getString("security_level")
                );
                cell.setCurrentOccupancy(rs.getInt("current_occupancy"));
                cell.setStatus(rs.getString("status"));
                cells.add(cell);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving cells with available space: " + e.getMessage());
        }

        return cells;
    }
}
