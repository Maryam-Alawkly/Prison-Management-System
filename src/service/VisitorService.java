package service;

import database.VisitorDAO;
import model.Visitor;
import java.util.List;

/**
 * Service layer for Visitor management operations.
 * Implements Singleton pattern to provide a single instance throughout the application.
 * Handles business logic for visitor registration, approval, and tracking.
 */
public class VisitorService {
    
    private static VisitorService instance;
    private final VisitorDAO visitorDAO;
    
    /**
     * Private constructor to enforce Singleton pattern.
     * Initializes the DAO instance.
     */
    private VisitorService() {
        this.visitorDAO = VisitorDAO.getInstance();
    }
    
    /**
     * Returns the single instance of VisitorService.
     * Implements thread-safe lazy initialization.
     *
     * @return VisitorService instance
     */
    public static synchronized VisitorService getInstance() {
        if (instance == null) {
            instance = new VisitorService();
        }
        return instance;
    }
    
    /**
     * Adds a new visitor to the database.
     *
     * @param visitor Visitor object containing visitor details
     * @return true if visitor was added successfully, false otherwise
     */
    public boolean addVisitor(Visitor visitor) {
        return visitorDAO.addVisitor(visitor);
    }
    
    /**
     * Retrieves all visitors from the database.
     *
     * @return List of all Visitor objects
     */
    public List<Visitor> getAllVisitors() {
        return visitorDAO.getAllVisitors();
    }
    
    /**
     * Finds a visitor by their unique identifier.
     *
     * @param visitorId Unique identifier of the visitor
     * @return Visitor object if found, null otherwise
     */
    public Visitor getVisitorById(String visitorId) {
        return visitorDAO.getVisitorById(visitorId);
    }
    
    /**
     * Updates visitor information in the database.
     *
     * @param visitor Visitor object with updated data
     * @return true if update was successful, false otherwise
     */
    public boolean updateVisitor(Visitor visitor) {
        return visitorDAO.updateVisitor(visitor);
    }
    
    /**
     * Deletes a visitor from the database.
     *
     * @param visitorId Unique identifier of the visitor to delete
     * @return true if deletion was successful, false otherwise
     */
    public boolean deleteVisitor(String visitorId) {
        return visitorDAO.deleteVisitor(visitorId);
    }
    
    /**
     * Retrieves visitors associated with a specific prisoner.
     *
     * @param prisonerId Unique identifier of the prisoner
     * @return List of Visitor objects for the specified prisoner
     */
    public List<Visitor> getVisitorsByPrisoner(String prisonerId) {
        return visitorDAO.getVisitorsByPrisoner(prisonerId);
    }
    
    /**
     * Retrieves visitors by their status.
     *
     * @param status Status to filter by
     * @return List of Visitor objects with specified status
     */
    public List<Visitor> getVisitorsByStatus(String status) {
        return visitorDAO.getVisitorsByStatus(status);
    }
    
    /**
     * Approves a visitor by updating their status to 'Approved'.
     *
     * @param visitorId Unique identifier of the visitor to approve
     * @return true if approval was successful, false otherwise
     */
    public boolean approveVisitor(String visitorId) {
        return visitorDAO.approveVisitor(visitorId);
    }
    
    /**
     * Bans a visitor by updating their status to 'Banned'.
     *
     * @param visitorId Unique identifier of the visitor to ban
     * @return true if ban was successful, false otherwise
     */
    public boolean banVisitor(String visitorId) {
        return visitorDAO.banVisitor(visitorId);
    }
    
    /**
     * Records a visit for a visitor by incrementing visit count and updating last visit date.
     *
     * @param visitorId Unique identifier of the visitor
     * @return true if visit was recorded successfully, false otherwise
     */
    public boolean recordVisit(String visitorId) {
        return visitorDAO.recordVisit(visitorId);
    }
    
    /**
     * Gets the total number of visitors in the database.
     *
     * @return Total count of visitors
     */
    public int getTotalVisitors() {
        return visitorDAO.getTotalVisitors();
    }
    
    /**
     * Gets the number of approved visitors.
     *
     * @return Number of approved visitors
     */
    public int getApprovedVisitorsCount() {
        return visitorDAO.getApprovedVisitorsCount();
    }
    
    /**
     * Retrieves the top visitors with the most visits.
     *
     * @param limit Maximum number of visitors to return
     * @return List of Visitor objects sorted by visit count descending
     */
    public List<Visitor> getTopVisitors(int limit) {
        return visitorDAO.getTopVisitors(limit);
    }
    
    /**
     * Searches for visitors by name or phone number.
     *
     * @param searchTerm Search term to match against visitor name or phone
     * @return List of Visitor objects matching search criteria
     */
    public List<Visitor> searchVisitors(String searchTerm) {
        return visitorDAO.searchVisitors(searchTerm);
    }
    
    /**
     * Generates a unique visitor identifier.
     *
     * @return Unique visitor identifier string
     */
    public String generateVisitorId() {
        return visitorDAO.generateVisitorId();
    }
    
    /**
     * Gets visitor statistics including counts by status.
     *
     * @return Object array containing [total, approved, pending, banned]
     */
    public Object[] getVisitorStatistics() {
        return visitorDAO.getVisitorStatistics();
    }
    
    /**
     * Gets visitors who haven't visited within a specified number of days.
     *
     * @param days Number of days threshold
     * @return List of Visitor objects who haven't visited within specified days
     */
    public List<Visitor> getInactiveVisitors(int days) {
        return visitorDAO.getInactiveVisitors(days);
    }
    
    /**
     * Updates a visitor's relationship with a prisoner.
     *
     * @param visitorId Unique identifier of the visitor
     * @param prisonerId New prisoner identifier to associate with
     * @param relationship New relationship description
     * @return true if update was successful, false otherwise
     */
    public boolean updateVisitorRelationship(String visitorId, String prisonerId, String relationship) {
        return visitorDAO.updateVisitorRelationship(visitorId, prisonerId, relationship);
    }
    
    /**
     * Gets the number of visits for a specific visitor.
     *
     * @param visitorId Unique identifier of the visitor
     * @return Number of visits recorded for the visitor
     */
    public int getVisitorVisitCount(String visitorId) {
        return visitorDAO.getVisitorVisitCount(visitorId);
    }
}