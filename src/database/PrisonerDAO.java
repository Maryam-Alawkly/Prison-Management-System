package database;

import model.Prisoner;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Prisoner entity. Implements Singleton pattern for DAO
 * instance management.
 */
public class PrisonerDAO {

    // Singleton instance
    private static PrisonerDAO instance;

    /**
     * Private constructor to enforce Singleton pattern.
     */
    private PrisonerDAO() {
        // Private constructor to prevent instantiation
    }

    /**
     * Returns the single instance of PrisonerDAO.
     *
     * @return PrisonerDAO instance
     */
    public static synchronized PrisonerDAO getInstance() {
        if (instance == null) {
            instance = new PrisonerDAO();
        }
        return instance;
    }

    /**
     * Adds a new prisoner record to the database.
     *
     * @param prisoner Prisoner object containing all prisoner details
     * @return true if prisoner was added successfully, false otherwise
     */
    public boolean addPrisoner(Prisoner prisoner) {
        String sql = "INSERT INTO prisoners (prisoner_id, name, phone, crime, cell_number, sentence_duration) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, prisoner.getId());
            stmt.setString(2, prisoner.getName());
            stmt.setString(3, prisoner.getPhone());
            stmt.setString(4, prisoner.getCrime());
            stmt.setString(5, prisoner.getPrisonCellNumber());
            stmt.setString(6, prisoner.getSentenceDuration());

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            System.err.println("Error adding prisoner to database: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves all prisoners from the database.
     *
     * @return List of all Prisoner objects
     */
    public List<Prisoner> getAllPrisoners() {
        List<Prisoner> prisoners = new ArrayList<>();
        String sql = "SELECT * FROM prisoners";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Prisoner prisoner = new Prisoner(
                        rs.getString("prisoner_id"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("crime"),
                        rs.getString("cell_number"),
                        rs.getString("sentence_duration")
                );
                prisoners.add(prisoner);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving all prisoners: " + e.getMessage());
        }

        return prisoners;
    }

    /**
     * Updates an existing prisoner's information in the database.
     *
     * @param prisoner Prisoner object with updated values
     * @return true if update was successful, false otherwise
     */
    public boolean updatePrisoner(Prisoner prisoner) {
        String sql = "UPDATE prisoners SET name=?, phone=?, crime=?, cell_number=?, sentence_duration=? WHERE prisoner_id=?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, prisoner.getName());
            stmt.setString(2, prisoner.getPhone());
            stmt.setString(3, prisoner.getCrime());
            stmt.setString(4, prisoner.getPrisonCellNumber());
            stmt.setString(5, prisoner.getSentenceDuration());
            stmt.setString(6, prisoner.getId());

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            System.err.println("Error updating prisoner information: " + e.getMessage());
            return false;
        }
    }

    /**
     * Deletes a prisoner from the database.
     *
     * @param prisonerId Unique identifier of the prisoner to delete
     * @return true if deletion was successful, false otherwise
     */
    public boolean deletePrisoner(String prisonerId) {
        String sql = "DELETE FROM prisoners WHERE prisoner_id=?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, prisonerId);
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting prisoner from database: " + e.getMessage());
            return false;
        }
    }

    /**
     * Searches for prisoners by name, ID, or crime.
     *
     * @param searchTerm Search term to match against prisoner name, ID, or
     * crime
     * @return Array of Prisoner objects matching search criteria
     */
    public Prisoner[] searchPrisoners(String searchTerm) {
        List<Prisoner> prisoners = new ArrayList<>();
        String sql = "SELECT * FROM prisoners WHERE name LIKE ? OR prisoner_id LIKE ? OR crime LIKE ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            String searchPattern = "%" + searchTerm + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Prisoner prisoner = new Prisoner(
                        rs.getString("prisoner_id"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("crime"),
                        rs.getString("cell_number"),
                        rs.getString("sentence_duration")
                );
                prisoners.add(prisoner);
            }

        } catch (SQLException e) {
            System.err.println("Error searching prisoners: " + e.getMessage());
        }

        return prisoners.toArray(new Prisoner[0]);
    }

    /**
     * Finds a prisoner by their unique identifier.
     *
     * @param prisonerId Unique identifier of the prisoner
     * @return Prisoner object if found, null otherwise
     */
    public Prisoner getPrisonerById(String prisonerId) {
        String sql = "SELECT * FROM prisoners WHERE prisoner_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, prisonerId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Prisoner(
                        rs.getString("prisoner_id"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("crime"),
                        rs.getString("cell_number"),
                        rs.getString("sentence_duration")
                );
            }

        } catch (SQLException e) {
            System.err.println("Error finding prisoner by ID: " + e.getMessage());
        }

        return null;
    }

    /**
     * Finds prisoners assigned to a specific cell.
     *
     * @param cellNumber Cell number to search for
     * @return List of Prisoner objects in the specified cell
     */
    public List<Prisoner> getPrisonersByCell(String cellNumber) {
        List<Prisoner> prisoners = new ArrayList<>();
        String sql = "SELECT * FROM prisoners WHERE cell_number = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cellNumber);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Prisoner prisoner = new Prisoner(
                        rs.getString("prisoner_id"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("crime"),
                        rs.getString("cell_number"),
                        rs.getString("sentence_duration")
                );
                prisoners.add(prisoner);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving prisoners by cell: " + e.getMessage());
        }

        return prisoners;
    }

    /**
     * Gets the total number of prisoners in the database.
     *
     * @return Total count of prisoners
     */
    public int getTotalPrisoners() {
        String sql = "SELECT COUNT(*) as total FROM prisoners";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt("total");
            }

        } catch (SQLException e) {
            System.err.println("Error getting total prisoner count: " + e.getMessage());
        }

        return 0;
    }

    /**
     * Gets the number of prisoners in a specific cell.
     *
     * @param cellNumber Cell number to count prisoners for
     * @return Number of prisoners in the specified cell
     */
    public int getPrisonerCountByCell(String cellNumber) {
        String sql = "SELECT COUNT(*) as count FROM prisoners WHERE cell_number = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cellNumber);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("count");
            }

        } catch (SQLException e) {
            System.err.println("Error counting prisoners by cell: " + e.getMessage());
        }

        return 0;
    }

    /**
     * Transfers a prisoner to a different cell.
     *
     * @param prisonerId Unique identifier of the prisoner
     * @param newCellNumber New cell number to assign
     * @return true if transfer was successful, false otherwise
     */
    public boolean transferPrisonerCell(String prisonerId, String newCellNumber) {
        String sql = "UPDATE prisoners SET cell_number = ? WHERE prisoner_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newCellNumber);
            stmt.setString(2, prisonerId);

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            System.err.println("Error transferring prisoner cell: " + e.getMessage());
            return false;
        }
    }

    /**
     * Generates a unique prisoner identifier. Format: PR + 6-digit random
     * number.
     *
     * @return Unique prisoner identifier string
     */
    public String generatePrisonerId() {
        String prefix = "PR";
        String random = String.format("%06d", (int) (Math.random() * 1000000));
        return prefix + random;
    }

    /**
     * Checks if a prisoner ID already exists in the database.
     *
     * @param prisonerId Prisoner ID to check
     * @return true if ID exists, false otherwise
     */
    public boolean prisonerIdExists(String prisonerId) {
        String sql = "SELECT COUNT(*) as count FROM prisoners WHERE prisoner_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, prisonerId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("count") > 0;
            }

        } catch (SQLException e) {
            System.err.println("Error checking prisoner ID existence: " + e.getMessage());
        }

        return false;
    }

    /**
     * Gets prisoners by crime type.
     *
     * @param crime Crime type to filter by
     * @return List of Prisoner objects with specified crime
     */
    public List<Prisoner> getPrisonersByCrime(String crime) {
        List<Prisoner> prisoners = new ArrayList<>();
        String sql = "SELECT * FROM prisoners WHERE crime LIKE ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + crime + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Prisoner prisoner = new Prisoner(
                        rs.getString("prisoner_id"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("crime"),
                        rs.getString("cell_number"),
                        rs.getString("sentence_duration")
                );
                prisoners.add(prisoner);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving prisoners by crime: " + e.getMessage());
        }

        return prisoners;
    }

    /**
     * Gets statistics about prisoner distribution.
     *
     * @return Array containing [totalPrisoners, prisonersPerCellAverage,
     * uniqueCrimesCount]
     */
    public Object[] getPrisonerStatistics() {
        int totalPrisoners = getTotalPrisoners();
        int uniqueCrimesCount = getUniqueCrimesCount();
        double averagePerCell = getAveragePrisonersPerCell();

        return new Object[]{totalPrisoners, averagePerCell, uniqueCrimesCount};
    }

    /**
     * Gets the count of unique crimes in the prisoner database.
     *
     * @return Number of unique crimes
     */
    private int getUniqueCrimesCount() {
        String sql = "SELECT COUNT(DISTINCT crime) as unique_crimes FROM prisoners";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt("unique_crimes");
            }

        } catch (SQLException e) {
            System.err.println("Error counting unique crimes: " + e.getMessage());
        }

        return 0;
    }

    /**
     * Gets the average number of prisoners per cell.
     *
     * @return Average prisoners per cell
     */
    private double getAveragePrisonersPerCell() {
        String sql = "SELECT AVG(cell_count) as average_per_cell FROM "
                + "(SELECT COUNT(*) as cell_count FROM prisoners GROUP BY cell_number) cell_counts";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getDouble("average_per_cell");
            }

        } catch (SQLException e) {
            System.err.println("Error calculating average prisoners per cell: " + e.getMessage());
        }

        return 0.0;
    }
}
