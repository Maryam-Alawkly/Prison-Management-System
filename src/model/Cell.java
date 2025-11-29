package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Cell class represents a prison cell in the facility
 * Manages cell information, capacity, and assigned prisoners
 */
public class Cell {
    private String cellNumber;
    private String cellType; // "Single", "Double", "General"
    private int capacity;
    private int currentOccupancy;
    private String securityLevel; // "Minimum", "Medium", "Maximum"
    private String status; // "Occupied", "Vacant", "Under Maintenance"
    private List<String> assignedPrisoners; // List of prisoner IDs
    
    /**
     * Constructor for Cell class
     * @param cellNumber Unique cell identifier
     * @param cellType Type of cell
     * @param capacity Maximum prisoner capacity
     * @param securityLevel Security level of cell
     */
    public Cell(String cellNumber, String cellType, int capacity, String securityLevel) {
        this.cellNumber = cellNumber;
        this.cellType = cellType;
        this.capacity = capacity;
        this.securityLevel = securityLevel;
        this.currentOccupancy = 0;
        this.status = "Vacant";
        this.assignedPrisoners = new ArrayList<>();
    }
    
    // Getter methods
    public String getCellNumber() {
        return cellNumber;
    }
    
    public String getCellType() {
        return cellType;
    }
    
    public int getCapacity() {
        return capacity;
    }
    
    public int getCurrentOccupancy() {
        return currentOccupancy;
    }
    
    public String getSecurityLevel() {
        return securityLevel;
    }
    
    public String getStatus() {
        return status;
    }
    
    public List<String> getAssignedPrisoners() {
        return new ArrayList<>(assignedPrisoners); // Return copy for encapsulation
    }
    
    // Setter methods
    public void setCellType(String cellType) {
        this.cellType = cellType;
    }
    
    public void setSecurityLevel(String securityLevel) {
        this.securityLevel = securityLevel;
    }
    
    /**
     * Assign a prisoner to this cell
     * @param prisonerId ID of prisoner to assign
     * @return true if assignment successful, false if cell is full
     */
    public boolean assignPrisoner(String prisonerId) {
        if (currentOccupancy < capacity) {
            assignedPrisoners.add(prisonerId);
            currentOccupancy++;
            updateStatus();
            return true;
        }
        return false; // Cell is full
    }
    
    /**
     * Remove a prisoner from this cell
     * @param prisonerId ID of prisoner to remove
     * @return true if removal successful, false if prisoner not found
     */
    public boolean removePrisoner(String prisonerId) {
        boolean removed = assignedPrisoners.remove(prisonerId);
        if (removed) {
            currentOccupancy--;
            updateStatus();
        }
        return removed;
    }
    
    /**
     * Check if cell has available space
     * @return true if cell can accommodate more prisoners
     */
    public boolean hasAvailableSpace() {
        return currentOccupancy < capacity;
    }
    
    /**
     * Get available space in cell
     * @return number of available spots
     */
    public int getAvailableSpace() {
        return capacity - currentOccupancy;
    }
    
    /**
     * Calculate occupancy percentage
     * @return occupancy percentage (0-100)
     */
    public double getOccupancyPercentage() {
        return (double) currentOccupancy / capacity * 100;
    }
    
    /**
     * Update cell status based on occupancy
     */
    private void updateStatus() {
        if (currentOccupancy == 0) {
            status = "Vacant";
        } else if (currentOccupancy >= capacity) {
            status = "Full";
        } else {
            status = "Occupied";
        }
    }
    
    /**
     * Put cell under maintenance
     */
    public void setUnderMaintenance() {
        this.status = "Under Maintenance";
    }
    
    /**
     * Check if cell is available for new prisoners
     * @return true if cell can accept new prisoners
     */
    public boolean isAvailable() {
        return "Vacant".equals(status) || "Occupied".equals(status);
    }
    
    /**
     * Override toString method
     */
    @Override
    public String toString() {
        return "Cell " + cellNumber + 
               " | Type: " + cellType + 
               " | Occupancy: " + currentOccupancy + "/" + capacity + 
               " | Security: " + securityLevel + 
               " | Status: " + status;
    }
    
    /**
     * Get detailed cell information
     * @return Detailed string with all cell data
     */
    public String getCellDetails() {
        return "Cell Number: " + cellNumber +
               "\nCell Type: " + cellType +
               "\nCapacity: " + capacity +
               "\nCurrent Occupancy: " + currentOccupancy +
               "\nAvailable Space: " + getAvailableSpace() +
               "\nOccupancy Percentage: " + String.format("%.1f", getOccupancyPercentage()) + "%" +
               "\nSecurity Level: " + securityLevel +
               "\nStatus: " + status +
               "\nAssigned Prisoners: " + (assignedPrisoners.isEmpty() ? "None" : assignedPrisoners.toString());
    }
    
    /**
     * Get cell summary for reports
     * @return Summary string
     */
    public String getCellSummary() {
        return String.format("Cell %s: %d/%d prisoners (%s) - %s", 
                           cellNumber, currentOccupancy, capacity, securityLevel, status);
    }
}

