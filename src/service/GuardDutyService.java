package service;

import database.GuardDutyDAO;
import model.GuardDuty;
import java.time.LocalDate;
import java.util.List;

/**
 * Service layer for Guard Duty management operations. Implements Singleton
 * pattern to provide a single instance throughout the application. Acts as a
 * business logic layer between controllers and data access layer.
 */
public class GuardDutyService {

    private static GuardDutyService instance;
    private final GuardDutyDAO guardDutyDAO;

    /**
     * Private constructor to enforce Singleton pattern. Initializes the DAO
     * instance.
     */
    private GuardDutyService() {
        this.guardDutyDAO = GuardDutyDAO.getInstance();
    }

    /**
     * Returns the single instance of GuardDutyService. Implements thread-safe
     * lazy initialization.
     *
     * @return GuardDutyService instance
     */
    public static synchronized GuardDutyService getInstance() {
        if (instance == null) {
            instance = new GuardDutyService();
        }
        return instance;
    }

    /**
     * Retrieves all guard duties from the database.
     *
     * @return List of all GuardDuty objects ordered by date and time
     */
    public List<GuardDuty> getAllGuardDuties() {
        return guardDutyDAO.getAllGuardDuties();
    }

    /**
     * Retrieves guard duties assigned to a specific officer.
     *
     * @param officerId Unique identifier of the officer
     * @return List of GuardDuty objects for the specified officer
     */
    public List<GuardDuty> getDutiesByOfficer(String officerId) {
        return guardDutyDAO.getDutiesByOfficer(officerId);
    }

    /**
     * Searches for guard duties based on multiple criteria.
     *
     * @param searchText Text to search in officer name, location, or duty type
     * @param status Duty status filter
     * @param date Specific date filter
     * @return List of GuardDuty objects matching search criteria
     */
    public List<GuardDuty> searchGuardDuties(String searchText, String status, LocalDate date) {
        return guardDutyDAO.searchGuardDuties(searchText, status, date);
    }

    /**
     * Adds a new guard duty to the database.
     *
     * @param duty GuardDuty object to add
     * @return true if duty was added successfully, false otherwise
     */
    public boolean addGuardDuty(GuardDuty duty) {
        return guardDutyDAO.addGuardDuty(duty);
    }

    /**
     * Updates an existing guard duty in the database.
     *
     * @param duty GuardDuty object with updated values
     * @return true if update was successful, false otherwise
     */
    public boolean updateGuardDuty(GuardDuty duty) {
        return guardDutyDAO.updateGuardDuty(duty);
    }

    /**
     * Updates the status of a guard duty.
     *
     * @param dutyId Unique identifier of the duty
     * @param status New status to set
     * @param completedTime Time when duty was completed
     * @return true if status update was successful, false otherwise
     */
    public boolean updateDutyStatus(String dutyId, String status, String completedTime) {
        return guardDutyDAO.updateDutyStatus(dutyId, status, completedTime);
    }

    /**
     * Deletes a guard duty from the database.
     *
     * @param dutyId Unique identifier of the duty to delete
     * @return true if deletion was successful, false otherwise
     */
    public boolean deleteGuardDuty(String dutyId) {
        return guardDutyDAO.deleteGuardDuty(dutyId);
    }

    /**
     * Retrieves guard duties for a specific date.
     *
     * @param date Date to retrieve duties for
     * @return List of GuardDuty objects for the specified date
     */
    public List<GuardDuty> getDutiesByDate(LocalDate date) {
        return guardDutyDAO.getDutiesByDate(date);
    }

    /**
     * Retrieves guard duties by status.
     *
     * @param status Status to filter by
     * @return List of GuardDuty objects with specified status
     */
    public List<GuardDuty> getDutiesByStatus(String status) {
        return guardDutyDAO.getDutiesByStatus(status);
    }

    /**
     * Retrieves today's guard duties for a specific officer.
     *
     * @param officerId Unique identifier of the officer
     * @return List of GuardDuty objects for today
     */
    public List<GuardDuty> getTodayDutiesByOfficer(String officerId) {
        return guardDutyDAO.getTodayDutiesByOfficer(officerId);
    }

    /**
     * Gets the total count of guard duties.
     *
     * @return Total number of guard duties
     */
    public int getTotalDutiesCount() {
        return guardDutyDAO.getTotalDutiesCount();
    }

    /**
     * Generates a unique duty identifier.
     *
     * @return Unique duty identifier string
     */
    public String generateDutyId() {
        return guardDutyDAO.generateDutyId();
    }

    /**
     * Reports an issue related to a guard duty.
     *
     * @param dutyId Unique identifier of the duty with issue
     * @param issue Description of the issue
     * @param reportedBy Identifier of the person reporting the issue
     * @return true if issue report was successful, false otherwise
     */
    public boolean reportDutyIssue(String dutyId, String issue, String reportedBy) {
        return guardDutyDAO.reportDutyIssue(dutyId, issue, reportedBy);
    }
}
