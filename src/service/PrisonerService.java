package service;

import database.PrisonerDAO;
import model.Prisoner;
import java.util.List;

/**
 * Service layer for Prisoner management operations.
 * Implements Singleton pattern to provide a single instance throughout the application.
 * Handles business logic for prisoner registration, tracking, and management.
 */
public class PrisonerService {
    
    private static PrisonerService instance;
    private final PrisonerDAO prisonerDAO;
    
    /**
     * Private constructor to enforce Singleton pattern.
     * Initializes the DAO instance.
     */
    private PrisonerService() {
        this.prisonerDAO = PrisonerDAO.getInstance();
    }
    
    /**
     * Returns the single instance of PrisonerService.
     * Implements thread-safe lazy initialization.
     *
     * @return PrisonerService instance
     */
    public static synchronized PrisonerService getInstance() {
        if (instance == null) {
            instance = new PrisonerService();
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
        return prisonerDAO.addPrisoner(prisoner);
    }
    
    /**
     * Retrieves all prisoners from the database.
     *
     * @return List of all Prisoner objects
     */
    public List<Prisoner> getAllPrisoners() {
        return prisonerDAO.getAllPrisoners();
    }
    
    /**
     * Updates an existing prisoner's information in the database.
     *
     * @param prisoner Prisoner object with updated values
     * @return true if update was successful, false otherwise
     */
    public boolean updatePrisoner(Prisoner prisoner) {
        return prisonerDAO.updatePrisoner(prisoner);
    }
    
    /**
     * Deletes a prisoner from the database.
     *
     * @param prisonerId Unique identifier of the prisoner to delete
     * @return true if deletion was successful, false otherwise
     */
    public boolean deletePrisoner(String prisonerId) {
        return prisonerDAO.deletePrisoner(prisonerId);
    }
    
    /**
     * Searches for prisoners by name, ID, or crime.
     *
     * @param searchTerm Search term to match against prisoner name, ID, or crime
     * @return Array of Prisoner objects matching search criteria
     */
    public Prisoner[] searchPrisoners(String searchTerm) {
        return prisonerDAO.searchPrisoners(searchTerm);
    }
    
    /**
     * Finds a prisoner by their unique identifier.
     *
     * @param prisonerId Unique identifier of the prisoner
     * @return Prisoner object if found, null otherwise
     */
    public Prisoner getPrisonerById(String prisonerId) {
        return prisonerDAO.getPrisonerById(prisonerId);
    }
    
    /**
     * Finds prisoners assigned to a specific cell.
     *
     * @param cellNumber Cell number to search for
     * @return List of Prisoner objects in the specified cell
     */
    public List<Prisoner> getPrisonersByCell(String cellNumber) {
        return prisonerDAO.getPrisonersByCell(cellNumber);
    }
    
    /**
     * Gets the total number of prisoners in the database.
     *
     * @return Total count of prisoners
     */
    public int getTotalPrisoners() {
        return prisonerDAO.getTotalPrisoners();
    }
    
    /**
     * Gets the number of prisoners in a specific cell.
     *
     * @param cellNumber Cell number to count prisoners for
     * @return Number of prisoners in the specified cell
     */
    public int getPrisonerCountByCell(String cellNumber) {
        return prisonerDAO.getPrisonerCountByCell(cellNumber);
    }
    
    /**
     * Transfers a prisoner to a different cell.
     *
     * @param prisonerId Unique identifier of the prisoner
     * @param newCellNumber New cell number to assign
     * @return true if transfer was successful, false otherwise
     */
    public boolean transferPrisonerCell(String prisonerId, String newCellNumber) {
        return prisonerDAO.transferPrisonerCell(prisonerId, newCellNumber);
    }
    
    /**
     * Generates a unique prisoner identifier.
     *
     * @return Unique prisoner identifier string
     */
    public String generatePrisonerId() {
        return prisonerDAO.generatePrisonerId();
    }
    
    /**
     * Checks if a prisoner ID already exists in the database.
     *
     * @param prisonerId Prisoner ID to check
     * @return true if ID exists, false otherwise
     */
    public boolean prisonerIdExists(String prisonerId) {
        return prisonerDAO.prisonerIdExists(prisonerId);
    }
    
    /**
     * Gets prisoners by crime type.
     *
     * @param crime Crime type to filter by
     * @return List of Prisoner objects with specified crime
     */
    public List<Prisoner> getPrisonersByCrime(String crime) {
        return prisonerDAO.getPrisonersByCrime(crime);
    }
    
    /**
     * Gets statistics about prisoner distribution.
     *
     * @return Array containing [totalPrisoners, prisonersPerCellAverage, uniqueCrimesCount]
     */
    public Object[] getPrisonerStatistics() {
        return prisonerDAO.getPrisonerStatistics();
    }
}