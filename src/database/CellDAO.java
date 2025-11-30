package database;

import model.Cell;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Cell entity Handles all database operations for prison
 * cells
 */
public class CellDAO {

    /**
     * Add a new cell to the database
     *
     * @param cell Cell object to be added
     * @return true if successful, false otherwise
     */
    public boolean addCell(Cell cell) {
        String sql = "INSERT INTO cells (cell_number, cell_type, capacity, security_level) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cell.getCellNumber());
            stmt.setString(2, cell.getCellType());
            stmt.setInt(3, cell.getCapacity());
            stmt.setString(4, cell.getSecurityLevel());

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            System.err.println("Error adding cell: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieve all cells from the database
     *
     * @return List of all cells
     */
    public List<Cell> getAllCells() {
        List<Cell> cells = new ArrayList<>();
        String sql = "SELECT * FROM cells";

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Cell cell = new Cell(
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
            System.err.println("Error retrieving cells: " + e.getMessage());
        }

        return cells;
    }

    /**
     * Find cell by cell number
     *
     * @param cellNumber Cell number to find
     * @return Cell object if found, null otherwise
     */
    public Cell getCellByNumber(String cellNumber) {
        String sql = "SELECT * FROM cells WHERE cell_number = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cellNumber);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Cell cell = new Cell(
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
            System.err.println("Error finding cell: " + e.getMessage());
        }

        return null;
    }

    /**
     * Update cell information
     *
     * @param cell Cell object with updated data
     * @return true if successful, false otherwise
     */
    public boolean updateCell(Cell cell) {
        String sql = "UPDATE cells SET cell_type=?, capacity=?, security_level=?, status=? WHERE cell_number=?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cell.getCellType());
            stmt.setInt(2, cell.getCapacity());
            stmt.setString(3, cell.getSecurityLevel());
            stmt.setString(4, cell.getStatus());
            stmt.setString(5, cell.getCellNumber());

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            System.err.println("Error updating cell: " + e.getMessage());
            return false;
        }
    }

    /**
     * Delete cell from database
     *
     * @param cellNumber Cell number to delete
     * @return true if successful, false otherwise
     */
    public boolean deleteCell(String cellNumber) {
        String sql = "DELETE FROM cells WHERE cell_number = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cellNumber);
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting cell: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get cells by security level
     *
     * @param securityLevel Security level to filter by
     * @return List of cells with specified security level
     */
    public List<Cell> getCellsBySecurityLevel(String securityLevel) {
        List<Cell> cells = new ArrayList<>();
        String sql = "SELECT * FROM cells WHERE security_level = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, securityLevel);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Cell cell = new Cell(
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
            System.err.println("Error getting cells by security level: " + e.getMessage());
        }

        return cells;
    }

    /**
     * Get cells by status
     *
     * @param status Status to filter by
     * @return List of cells with specified status
     */
    public List<Cell> getCellsByStatus(String status) {
        List<Cell> cells = new ArrayList<>();
        String sql = "SELECT * FROM cells WHERE status = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Cell cell = new Cell(
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
            System.err.println("Error getting cells by status: " + e.getMessage());
        }

        return cells;
    }

    /**
     * Get available cells (with space)
     *
     * @return List of cells that have available space
     */
    public List<Cell> getAvailableCells() {
        List<Cell> cells = new ArrayList<>();
        String sql = "SELECT * FROM cells WHERE current_occupancy < capacity AND status != 'Under Maintenance'";

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Cell cell = new Cell(
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
            System.err.println("Error getting available cells: " + e.getMessage());
        }

        return cells;
    }

    /**
     * Update cell occupancy
     *
     * @param cellNumber Cell number to update
     * @param newOccupancy New occupancy count
     * @return true if successful, false otherwise
     */
    public boolean updateCellOccupancy(String cellNumber, int newOccupancy) {
        String sql = "UPDATE cells SET current_occupancy = ? WHERE cell_number = ?";

        try (Connection conn = DatabaseConnection.getConnection();
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
     * Increment cell occupancy by 1
     *
     * @param cellNumber Cell number to update
     * @return true if successful, false otherwise
     */
    public boolean incrementCellOccupancy(String cellNumber) {
        Cell cell = getCellByNumber(cellNumber);
        if (cell != null && cell.hasAvailableSpace()) {
            return updateCellOccupancy(cellNumber, cell.getCurrentOccupancy() + 1);
        }
        return false;
    }

    /**
     * Decrement cell occupancy by 1
     *
     * @param cellNumber Cell number to update
     * @return true if successful, false otherwise
     */
    public boolean decrementCellOccupancy(String cellNumber) {
        Cell cell = getCellByNumber(cellNumber);
        if (cell != null && cell.getCurrentOccupancy() > 0) {
            return updateCellOccupancy(cellNumber, cell.getCurrentOccupancy() - 1);
        }
        return false;
    }

    /**
     * Set cell under maintenance
     *
     * @param cellNumber Cell number to set under maintenance
     * @return true if successful, false otherwise
     */
    public boolean setCellUnderMaintenance(String cellNumber) {
        String sql = "UPDATE cells SET status = 'Under Maintenance' WHERE cell_number = ?";

        try (Connection conn = DatabaseConnection.getConnection();
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
     * Get total number of cells
     *
     * @return Total count of cells
     */
    public int getTotalCells() {
        String sql = "SELECT COUNT(*) as total FROM cells";
        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt("total");
            }

        } catch (SQLException e) {
            System.err.println("Error getting total cells: " + e.getMessage());
        }

        return 0;
    }

    /**
     * Get total capacity of all cells
     *
     * @return Total capacity across all cells
     */
    public int getTotalCapacity() {
        String sql = "SELECT SUM(capacity) as total_capacity FROM cells";

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt("total_capacity");
            }

        } catch (SQLException e) {
            System.err.println("Error getting total capacity: " + e.getMessage());
        }

        return 0;
    }

    /**
     * Get total current occupancy
     *
     * @return Total current occupancy across all cells
     */
    public int getTotalOccupancy() {
        String sql = "SELECT SUM(current_occupancy) as total_occupancy FROM cells";

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt("total_occupancy");
            }

        } catch (SQLException e) {
            System.err.println("Error getting total occupancy: " + e.getMessage());
        }

        return 0;
    }

    /**
     * Get occupancy statistics
     *
     * @return Array with [totalCapacity, totalOccupancy, availableSpace]
     */
    public int[] getOccupancyStatistics() {
        int totalCapacity = getTotalCapacity();
        int totalOccupancy = getTotalOccupancy();
        int availableSpace = totalCapacity - totalOccupancy;

        return new int[]{totalCapacity, totalOccupancy, availableSpace};
    }
}
