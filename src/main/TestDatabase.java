package main;

import database.DatabaseConnection;
import database.PrisonerDAO;
import model.Prisoner;
import java.util.List;

public class TestDatabase {
    public static void main(String[] args) {
        System.out.println("=== Testing Database Connection ===\n");
        
        // Test database connection
        DatabaseConnection.testConnection();
        
        // Test PrisonerDAO operations
        PrisonerDAO prisonerDAO = new PrisonerDAO();
        
        // Add a new prisoner
        Prisoner newPrisoner = new Prisoner("P003", "Test Prisoner", "0912998888", "Test Crime", "C-303", "2 years");
        boolean added = prisonerDAO.addPrisoner(newPrisoner);
        System.out.println(added ? "Prisoner added successfully!" : "Failed to add prisoner");
        
        // Get all prisoners
        List<Prisoner> prisoners = prisonerDAO.getAllPrisoners();
        System.out.println("\n=== All Prisoners ===");
        for (Prisoner p : prisoners) {
            System.out.println(p.toString());
        }
        
        // Close connection
        DatabaseConnection.closeConnection();
    }
}