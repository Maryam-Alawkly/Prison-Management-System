package service;

import database.VisitDAO;
import model.Visit;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Service layer for Visit management operations. Implements Singleton pattern
 * to provide a single instance throughout the application. Handles business
 * logic for visit scheduling, tracking, and reporting.
 */
public class VisitService {

    private static VisitService instance;
    private final VisitDAO visitDAO;

    /**
     * Private constructor to enforce Singleton pattern. Initializes the DAO
     * instance.
     */
    private VisitService() {
        this.visitDAO = VisitDAO.getInstance();
    }

    /**
     * Returns the single instance of VisitService. Implements thread-safe lazy
     * initialization.
     *
     * @return VisitService instance
     */
    public static synchronized VisitService getInstance() {
        if (instance == null) {
            instance = new VisitService();
        }
        return instance;
    }

    /**
     * Adds a new visit to the database.
     *
     * @param visit Visit object containing visit details
     * @return true if visit was added successfully, false otherwise
     */
    public boolean addVisit(Visit visit) {
        return visitDAO.addVisit(visit);
    }

    /**
     * Retrieves all visits from the database.
     *
     * @return List of all Visit objects
     */
    public List<Visit> getAllVisits() {
        return visitDAO.getAllVisits();
    }

    /**
     * Finds a visit by its unique identifier.
     *
     * @param visitId Unique identifier of the visit
     * @return Visit object if found, null otherwise
     */
    public Visit getVisitById(String visitId) {
        return visitDAO.getVisitById(visitId);
    }

    /**
     * Updates visit information in the database.
     *
     * @param visit Visit object with updated data
     * @return true if update was successful, false otherwise
     */
    public boolean updateVisit(Visit visit) {
        return visitDAO.updateVisit(visit);
    }

    /**
     * Deletes a visit from the database.
     *
     * @param visitId Unique identifier of the visit to delete
     * @return true if deletion was successful, false otherwise
     */
    public boolean deleteVisit(String visitId) {
        return visitDAO.deleteVisit(visitId);
    }

    /**
     * Retrieves visits by prisoner identifier.
     *
     * @param prisonerId Unique identifier of the prisoner
     * @return List of Visit objects for the specified prisoner
     */
    public List<Visit> getVisitsByPrisoner(String prisonerId) {
        return visitDAO.getVisitsByPrisoner(prisonerId);
    }

    /**
     * Retrieves visits by visitor identifier.
     *
     * @param visitorId Unique identifier of the visitor
     * @return List of Visit objects for the specified visitor
     */
    public List<Visit> getVisitsByVisitor(String visitorId) {
        return visitDAO.getVisitsByVisitor(visitorId);
    }

    /**
     * Retrieves visits by status.
     *
     * @param status Status to filter by
     * @return List of Visit objects with specified status
     */
    public List<Visit> getVisitsByStatus(String status) {
        return visitDAO.getVisitsByStatus(status);
    }

    /**
     * Retrieves visits scheduled for today.
     *
     * @return List of Visit objects scheduled for today
     */
    public List<Visit> getTodaysVisits() {
        return visitDAO.getTodaysVisits();
    }

    /**
     * Starts a visit by marking it as in progress.
     *
     * @param visitId Unique identifier of the visit to start
     * @return true if visit was started successfully, false otherwise
     */
    public boolean startVisit(String visitId) {
        return visitDAO.startVisit(visitId);
    }

    /**
     * Completes a visit by marking it as completed.
     *
     * @param visitId Unique identifier of the visit to complete
     * @return true if visit was completed successfully, false otherwise
     */
    public boolean completeVisit(String visitId) {
        return visitDAO.completeVisit(visitId);
    }

    /**
     * Cancels a visit with cancellation notes.
     *
     * @param visitId Unique identifier of the visit to cancel
     * @param cancellationNotes Reason for cancellation
     * @return true if visit was cancelled successfully, false otherwise
     */
    public boolean cancelVisit(String visitId, String cancellationNotes) {
        return visitDAO.cancelVisit(visitId, cancellationNotes);
    }

    /**
     * Gets comprehensive visit statistics.
     *
     * @return Array containing [totalVisits, completedVisits, scheduledVisits,
     * cancelledVisits]
     */
    public int[] getVisitStatistics() {
        return visitDAO.getVisitStatistics();
    }

    /**
     * Searches for visits based on multiple criteria.
     *
     * @param prisonerId Prisoner ID to filter by
     * @param visitorId Visitor ID to filter by
     * @param status Status to filter by
     * @param dateFrom Start date for date range
     * @param dateTo End date for date range
     * @return List of Visit objects matching search criteria
     */
    public List<Visit> searchVisits(String prisonerId, String visitorId, String status,
            LocalDate dateFrom, LocalDate dateTo) {
        return visitDAO.searchVisits(prisonerId, visitorId, status, dateFrom, dateTo);
    }

    /**
     * Generates a unique visit identifier.
     *
     * @return Unique visit identifier string
     */
    public String generateVisitId() {
        return visitDAO.generateVisitId();
    }

    /**
     * Gets visits that are currently in progress.
     *
     * @return List of Visit objects currently in progress
     */
    public List<Visit> getInProgressVisits() {
        return visitDAO.getInProgressVisits();
    }

    /**
     * Gets visits that have exceeded their scheduled duration.
     *
     * @return List of Visit objects that are overdue
     */
    public List<Visit> getOverdueVisits() {
        return visitDAO.getOverdueVisits();
    }

    /**
     * Retrieves upcoming visits scheduled for future dates.
     *
     * @return List of upcoming Visit objects
     */
    public List<Visit> getUpcomingVisits() {
        return visitDAO.getUpcomingVisits();
    }
}
