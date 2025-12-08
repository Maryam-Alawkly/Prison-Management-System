package model;

import java.util.ArrayList;
import java.util.List;

/**
 * PrisonCell class represents a prison cell in the facility. Manages cell
 * information, capacity, security level, and assigned prisoners. This class
 * provides methods for prisoner assignment, removal, and cell status
 * management.
 */
public class PrisonCell {

    // Cell type constants
    public static final String CELL_TYPE_SINGLE = "Single";
    public static final String CELL_TYPE_DOUBLE = "Double";
    public static final String CELL_TYPE_GENERAL = "General";

    // Security level constants
    public static final String SECURITY_MINIMUM = "Minimum";
    public static final String SECURITY_MEDIUM = "Medium";
    public static final String SECURITY_MAXIMUM = "Maximum";

    // Status constants
    public static final String STATUS_VACANT = "Vacant";
    public static final String STATUS_OCCUPIED = "Occupied";
    public static final String STATUS_FULL = "Full";
    public static final String STATUS_UNDER_MAINTENANCE = "Under Maintenance";

    // Cell properties
    private String cellNumber;
    private String cellType;
    private int capacity;
    private int currentOccupancy;
    private String securityLevel;
    private String status;
    private List<String> assignedPrisoners;

    /**
     * Constructor for PrisonCell class. Initializes a prison cell with
     * specified parameters and default values.
     *
     * @param cellNumber Unique cell identifier
     * @param cellType Type of cell (Single, Double, General)
     * @param capacity Maximum prisoner capacity
     * @param securityLevel Security level of cell (Minimum, Medium, Maximum)
     * @throws IllegalArgumentException if any parameter is invalid
     */
    public PrisonCell(String cellNumber, String cellType, int capacity, String securityLevel) {
        validateParameters(cellNumber, cellType, capacity, securityLevel);

        this.cellNumber = cellNumber;
        this.cellType = cellType;
        this.capacity = capacity;
        this.securityLevel = securityLevel;
        this.currentOccupancy = 0;
        this.status = STATUS_VACANT;
        this.assignedPrisoners = new ArrayList<>();
    }

    /**
     * Validates constructor parameters.
     *
     * @param cellNumber Cell number to validate
     * @param cellType Cell type to validate
     * @param capacity Capacity to validate
     * @param securityLevel Security level to validate
     */
    private void validateParameters(String cellNumber, String cellType, int capacity, String securityLevel) {
        if (cellNumber == null || cellNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Cell number cannot be null or empty");
        }
        if (cellType == null || cellType.trim().isEmpty()) {
            throw new IllegalArgumentException("Cell type cannot be null or empty");
        }
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than zero");
        }
        if (securityLevel == null || securityLevel.trim().isEmpty()) {
            throw new IllegalArgumentException("Security level cannot be null or empty");
        }
    }

    /**
     * Default constructor for frameworks that require it. This constructor is
     * not intended for general use.
     *
     * @throws UnsupportedOperationException when used directly
     */
    public PrisonCell() {
        throw new UnsupportedOperationException("Default constructor not supported. Use parameterized constructor instead.");
    }

    // =================== Getter Methods ===================
    /**
     * Gets the unique cell number.
     *
     * @return The cell number
     */
    public String getPrisonCellNumber() {
        return cellNumber;
    }

    /**
     * Gets the type of cell.
     *
     * @return The cell type
     */
    public String getPrisonCellType() {
        return cellType;
    }

    /**
     * Gets the maximum prisoner capacity.
     *
     * @return The capacity
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Gets the current number of prisoners in the cell.
     *
     * @return The current occupancy
     */
    public int getCurrentOccupancy() {
        return currentOccupancy;
    }

    /**
     * Gets the security level of the cell.
     *
     * @return The security level
     */
    public String getSecurityLevel() {
        return securityLevel;
    }

    /**
     * Gets the current status of the cell.
     *
     * @return The status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Gets a copy of the list of assigned prisoner IDs. Returns a copy to
     * maintain encapsulation and prevent external modification.
     *
     * @return Copy of assigned prisoners list
     */
    public List<String> getAssignedPrisoners() {
        return new ArrayList<>(assignedPrisoners);
    }

    // =================== Setter Methods ===================
    /**
     * Sets the type of cell.
     *
     * @param cellType The new cell type
     */
    public void setPrisonCellType(String cellType) {
        this.cellType = cellType;
    }

    /**
     * Sets the maximum prisoner capacity. Updates cell status if necessary.
     *
     * @param capacity The new capacity
     * @throws IllegalArgumentException if capacity is not positive
     */
    public void setCapacity(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than zero");
        }
        this.capacity = capacity;
        updateStatus();
    }

    /**
     * Sets the current occupancy count. Updates cell status based on new
     * occupancy.
     *
     * @param currentOccupancy The new occupancy count
     * @throws IllegalArgumentException if occupancy is negative or exceeds
     * capacity
     */
    public void setCurrentOccupancy(int currentOccupancy) {
        if (currentOccupancy < 0) {
            throw new IllegalArgumentException("Occupancy cannot be negative");
        }
        if (currentOccupancy > capacity) {
            throw new IllegalArgumentException("Occupancy cannot exceed capacity");
        }
        this.currentOccupancy = currentOccupancy;
        updateStatus();
    }

    /**
     * Sets the security level of the cell.
     *
     * @param securityLevel The new security level
     */
    public void setSecurityLevel(String securityLevel) {
        this.securityLevel = securityLevel;
    }

    /**
     * Sets the status of the cell.
     *
     * @param status The new status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    // =================== Business Logic Methods ===================
    /**
     * Assigns a prisoner to this cell. The assignment is successful if the cell
     * has available space. If the prisoner is already assigned, returns true
     * without changes.
     *
     * @param prisonerId ID of prisoner to assign
     * @return true if assignment successful, false if cell is full
     */
    public boolean assignPrisoner(String prisonerId) {
        if (currentOccupancy < capacity) {
            if (assignedPrisoners.contains(prisonerId)) {
                return true; // Prisoner is already assigned to this cell
            }
            assignedPrisoners.add(prisonerId);
            currentOccupancy++;
            updateStatus();
            return true;
        }
        return false; // PrisonCell is full
    }

    /**
     * Removes a prisoner from this cell. The removal is successful if the
     * prisoner is currently assigned to this cell.
     *
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
     * Checks if the cell has available space for more prisoners.
     *
     * @return true if cell can accommodate more prisoners, false otherwise
     */
    public boolean hasAvailableSpace() {
        return currentOccupancy < capacity;
    }

    /**
     * Gets the number of available spaces in the cell.
     *
     * @return Number of available spots
     */
    public int getAvailableSpace() {
        return capacity - currentOccupancy;
    }

    /**
     * Calculates the occupancy percentage of the cell.
     *
     * @return Occupancy percentage (0-100), or 0 if capacity is zero
     */
    public double getOccupancyPercentage() {
        if (capacity == 0) {
            return 0;
        }
        return (double) currentOccupancy / capacity * 100;
    }

    /**
     * Updates the cell status based on current occupancy. Status is
     * automatically determined and should not be set manually.
     */
    private void updateStatus() {
        if (currentOccupancy == 0) {
            status = STATUS_VACANT;
        } else if (currentOccupancy >= capacity) {
            status = STATUS_FULL;
        } else {
            status = STATUS_OCCUPIED;
        }
    }

    /**
     * Puts the cell under maintenance. This status overrides normal
     * occupancy-based status.
     */
    public void setUnderMaintenance() {
        this.status = STATUS_UNDER_MAINTENANCE;
    }

    /**
     * Checks if the cell is available for new prisoners. A cell is available if
     * it's not under maintenance and has space.
     *
     * @return true if cell can accept new prisoners, false otherwise
     */
    public boolean isAvailable() {
        return (STATUS_VACANT.equals(status) || STATUS_OCCUPIED.equals(status))
                && currentOccupancy < capacity;
    }

    // =================== Information Methods ===================
    /**
     * Returns a string representation of the cell. Provides a concise summary
     * for display purposes.
     *
     * @return String representation of the cell
     */
    @Override
    public String toString() {
        return String.format("PrisonCell %s | Type: %s | Occupancy: %d/%d | Security: %s | Status: %s",
                cellNumber, cellType, currentOccupancy, capacity, securityLevel, status);
    }

    /**
     * Gets detailed cell information. Includes all cell properties for display
     * or reporting purposes.
     *
     * @return Detailed string with all cell data
     */
    public String getPrisonCellDetails() {
        StringBuilder details = new StringBuilder();
        details.append("PrisonCell Number: ").append(cellNumber).append("\n");
        details.append("PrisonCell Type: ").append(cellType).append("\n");
        details.append("Capacity: ").append(capacity).append("\n");
        details.append("Current Occupancy: ").append(currentOccupancy).append("\n");
        details.append("Available Space: ").append(getAvailableSpace()).append("\n");
        details.append("Occupancy Percentage: ").append(String.format("%.1f", getOccupancyPercentage())).append("%\n");
        details.append("Security Level: ").append(securityLevel).append("\n");
        details.append("Status: ").append(status).append("\n");
        details.append("Assigned Prisoners: ").append(assignedPrisoners.isEmpty() ? "None" : assignedPrisoners.toString());

        return details.toString();
    }

    /**
     * Gets a summary of the cell for reports. Provides a compact format
     * suitable for listings and reports.
     *
     * @return Summary string
     */
    public String getPrisonCellSummary() {
        return String.format("PrisonCell %s: %d/%d prisoners (%s) - %s",
                cellNumber, currentOccupancy, capacity, securityLevel, status);
    }

    // =================== Validation Methods ===================
    /**
     * Checks if the cell configuration is valid. Validates all required fields
     * and logical constraints.
     *
     * @return true if cell configuration is valid, false otherwise
     */
    public boolean isValid() {
        return cellNumber != null && !cellNumber.trim().isEmpty()
                && cellType != null && !cellType.trim().isEmpty()
                && capacity > 0
                && currentOccupancy >= 0
                && currentOccupancy <= capacity
                && securityLevel != null && !securityLevel.trim().isEmpty()
                && status != null && !status.trim().isEmpty();
    }

    /**
     * Checks if a prisoner is currently assigned to this cell.
     *
     * @param prisonerId Prisoner ID to check
     * @return true if prisoner is assigned to this cell, false otherwise
     */
    public boolean hasPrisoner(String prisonerId) {
        return assignedPrisoners.contains(prisonerId);
    }

    /**
     * Gets the number of prisoners assigned to this cell. This is an alias for
     * getCurrentOccupancy() for clarity in some contexts.
     *
     * @return Number of assigned prisoners
     */
    public int getPrisonerCount() {
        return currentOccupancy;
    }
}
