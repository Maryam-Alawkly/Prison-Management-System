package database;

import model.Prisoner;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Prisoner operations
 */
public class PrisonerDAO {
    
    /**
     * Add new prisoner to database
     */
    public boolean addPrisoner(Prisoner prisoner) {
        String sql = "INSERT INTO prisoners (prisoner_id, name, phone, crime, cell_number, sentence_duration) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, prisoner.getId());
            stmt.setString(2, prisoner.getName());
            stmt.setString(3, prisoner.getPhone());
            stmt.setString(4, prisoner.getCrime());
            stmt.setString(5, prisoner.getCellNumber());
            stmt.setString(6, prisoner.getSentenceDuration());
            
            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
            
        } catch (SQLException e) {
            System.err.println("Error adding prisoner: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Get all prisoners from database
     */
    public List<Prisoner> getAllPrisoners() {
        List<Prisoner> prisoners = new ArrayList<>();
        String sql = "SELECT * FROM prisoners";
        
        try (Connection conn = DatabaseConnection.getConnection();
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
            System.err.println("Error getting prisoners: " + e.getMessage());
        }
        
        return prisoners;
    }
    
    /**
     * Update prisoner information
     */
    public boolean updatePrisoner(Prisoner prisoner) {
        String sql = "UPDATE prisoners SET name=?, phone=?, crime=?, cell_number=?, sentence_duration=? WHERE prisoner_id=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, prisoner.getName());
            stmt.setString(2, prisoner.getPhone());
            stmt.setString(3, prisoner.getCrime());
            stmt.setString(4, prisoner.getCellNumber());
            stmt.setString(5, prisoner.getSentenceDuration());
            stmt.setString(6, prisoner.getId());
            
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating prisoner: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Delete prisoner from database
     */
    public boolean deletePrisoner(String prisonerId) {
        String sql = "DELETE FROM prisoners WHERE prisoner_id=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, prisonerId);
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting prisoner: " + e.getMessage());
            return false;
        }
    }
}