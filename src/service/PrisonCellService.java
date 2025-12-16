package service;

import database.PrisonCellDAO;
import model.PrisonCell;
import java.util.List;

/**
 * Service layer for Prison Cell management operations. Implements Singleton
 * pattern to provide a single instance throughout the application. Handles
 * business logic for cell allocation, occupancy tracking, and maintenance.
 */
public class PrisonCellService {

    private static PrisonCellService instance;
    private final PrisonCellDAO prisonCellDAO;

    /**
     * Private constructor to enforce Singleton pattern. Initializes the DAO
     * instance.
     */
    private PrisonCellService() {
        this.prisonCellDAO = PrisonCellDAO.getInstance();
    }

    /**
     * Returns the single instance of PrisonCellService. Implements thread-safe
     * lazy initialization.
     *
     * @return PrisonCellService instance
     */
    public static synchronized PrisonCellService getInstance() {
        if (instance == null) {
            instance = new PrisonCellService();
        }
        return instance;
    }

    /**
     * Adds a new prison cell to the database.
     *
     * @param cell PrisonCell object containing cell details
     * @return true if cell was added successfully, false otherwise
     */
    public boolean addPrisonCell(PrisonCell cell) {
        return prisonCellDAO.addPrisonCell(cell);
    }

    /**
     * Retrieves all prison cells from the database.
     *
     * @return List of all PrisonCell objects
     */
    public List<PrisonCell> getAllPrisonCells() {
        return prisonCellDAO.getAllPrisonCells();
    }

    /**
     * Finds a prison cell by its cell number.
     *
     * @param cellNumber Unique identifier of the cell
     * @return PrisonCell object if found, null otherwise
     */
    public PrisonCell getPrisonCellByNumber(String cellNumber) {
        return prisonCellDAO.getPrisonCellByNumber(cellNumber);
    }

    /**
     * Updates prison cell information in the database.
     *
     * @param cell PrisonCell object with updated data
     * @return true if update was successful, false otherwise
     */
    public boolean updatePrisonCell(PrisonCell cell) {
        return prisonCellDAO.updatePrisonCell(cell);
    }

    /**
     * Deletes a prison cell from the database.
     *
     * @param cellNumber Unique identifier of the cell to delete
     * @return true if deletion was successful, false otherwise
     */
    public boolean deletePrisonCell(String cellNumber) {
        return prisonCellDAO.deletePrisonCell(cellNumber);
    }

    /**
     * Retrieves prison cells by security level.
     *
     * @param securityLevel Security level to filter by
     * @return List of PrisonCell objects with specified security level
     */
    public List<PrisonCell> getPrisonCellsBySecurityLevel(String securityLevel) {
        return prisonCellDAO.getPrisonCellsBySecurityLevel(securityLevel);
    }

    /**
     * Retrieves prison cells by status.
     *
     * @param status Status to filter by
     * @return List of PrisonCell objects with specified status
     */
    public List<PrisonCell> getPrisonCellsByStatus(String status) {
        return prisonCellDAO.getPrisonCellsByStatus(status);
    }

    /**
     * Retrieves available prison cells with space for additional occupants.
     *
     * @return List of available PrisonCell objects
     */
    public List<PrisonCell> getAvailablePrisonCells() {
        return prisonCellDAO.getAvailablePrisonCells();
    }

    /**
     * Updates the occupancy count for a specific cell.
     *
     * @param cellNumber Unique identifier of the cell
     * @param newOccupancy New occupancy count
     * @return true if update was successful, false otherwise
     */
    public boolean updatePrisonCellOccupancy(String cellNumber, int newOccupancy) {
        return prisonCellDAO.updatePrisonCellOccupancy(cellNumber, newOccupancy);
    }

    /**
     * Increments the occupancy count of a cell by 1.
     *
     * @param cellNumber Unique identifier of the cell
     * @return true if increment was successful, false otherwise
     */
    public boolean incrementPrisonCellOccupancy(String cellNumber) {
        return prisonCellDAO.incrementPrisonCellOccupancy(cellNumber);
    }

    /**
     * Decrements the occupancy count of a cell by 1.
     *
     * @param cellNumber Unique identifier of the cell
     * @return true if decrement was successful, false otherwise
     */
    public boolean decrementPrisonCellOccupancy(String cellNumber) {
        return prisonCellDAO.decrementPrisonCellOccupancy(cellNumber);
    }

    /**
     * Sets a cell to "Under Maintenance" status.
     *
     * @param cellNumber Unique identifier of the cell
     * @return true if status update was successful, false otherwise
     */
    public boolean setPrisonCellUnderMaintenance(String cellNumber) {
        return prisonCellDAO.setPrisonCellUnderMaintenance(cellNumber);
    }

    /**
     * Removes a cell from maintenance status.
     *
     * @param cellNumber Unique identifier of the cell
     * @param newStatus New status to set
     * @return true if status update was successful, false otherwise
     */
    public boolean removeFromMaintenance(String cellNumber, String newStatus) {
        return prisonCellDAO.removeFromMaintenance(cellNumber, newStatus);
    }

    /**
     * Gets occupancy statistics for the prison.
     *
     * @return Array containing [totalCapacity, totalOccupancy, availableSpace]
     */
    public int[] getOccupancyStatistics() {
        return prisonCellDAO.getOccupancyStatistics();
    }

    /**
     * Gets the utilization percentage of prison cells.
     *
     * @return Utilization percentage (0-100)
     */
    public double getUtilizationPercentage() {
        return prisonCellDAO.getUtilizationPercentage();
    }

    /**
     * Searches for prison cells by cell number or type.
     *
     * @param searchTerm Search term to match against cell number or type
     * @return List of PrisonCell objects matching search criteria
     */
    public List<PrisonCell> searchPrisonCells(String searchTerm) {
        return prisonCellDAO.searchPrisonCells(searchTerm);
    }

    /**
     * Gets cells that are at or near capacity.
     *
     * @param threshold Percentage threshold for capacity warning
     * @return List of PrisonCell objects at or above capacity threshold
     */
    public List<PrisonCell> getCellsNearCapacity(double threshold) {
        return prisonCellDAO.getCellsNearCapacity(threshold);
    }

    /**
     * Gets cells with available capacity for a given number of prisoners.
     *
     * @param requiredSpaces Number of spaces needed
     * @return List of PrisonCell objects with sufficient available space
     */
    public List<PrisonCell> getCellsWithAvailableSpace(int requiredSpaces) {
        return prisonCellDAO.getCellsWithAvailableSpace(requiredSpaces);
    }

    /**
     * Gets the total number of prison cells.
     *
     * @return Total count of prison cells
     */
    public int getTotalPrisonCells() {
        return prisonCellDAO.getTotalPrisonCells();
    }

    /**
     * Gets the total capacity across all prison cells.
     *
     * @return Total capacity of all cells
     */
    public int getTotalCapacity() {
        return prisonCellDAO.getTotalCapacity();
    }

    /**
     * Gets the total current occupancy across all prison cells.
     *
     * @return Total current occupancy
     */
    public int getTotalOccupancy() {
        return prisonCellDAO.getTotalOccupancy();
    }
}
