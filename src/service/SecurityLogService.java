package service;

import database.SecurityLogDAO;
import model.SecurityLog;
import java.util.List;

/**
 * Service layer for Security Log management operations.
 * Implements Singleton pattern to provide a single instance throughout the application.
 * Handles business logic for security event logging, monitoring, and reporting.
 */
public class SecurityLogService {
    
    private static SecurityLogService instance;
    private final SecurityLogDAO securityLogDAO;
    
    /**
     * Private constructor to enforce Singleton pattern.
     * Initializes the DAO instance.
     */
    private SecurityLogService() {
        this.securityLogDAO = SecurityLogDAO.getInstance();
    }
    
    /**
     * Returns the single instance of SecurityLogService.
     * Implements thread-safe lazy initialization.
     *
     * @return SecurityLogService instance
     */
    public static synchronized SecurityLogService getInstance() {
        if (instance == null) {
            instance = new SecurityLogService();
        }
        return instance;
    }
    
    /**
     * Adds a new security log entry to the database.
     *
     * @param log SecurityLog object containing log details
     * @return true if log was added successfully, false otherwise
     */
    public boolean addSecurityLog(SecurityLog log) {
        return securityLogDAO.addSecurityLog(log);
    }
    
    /**
     * Retrieves all security logs from the database.
     *
     * @return List of all SecurityLog objects
     */
    public List<SecurityLog> getAllSecurityLogs() {
        return securityLogDAO.getAllSecurityLogs();
    }
    
    /**
     * Retrieves security logs by severity level.
     *
     * @param severity Severity level to filter by
     * @return List of SecurityLog objects with specified severity
     */
    public List<SecurityLog> getSecurityLogsBySeverity(String severity) {
        return securityLogDAO.getSecurityLogsBySeverity(severity);
    }
    
    /**
     * Retrieves unresolved security logs.
     *
     * @return List of unresolved SecurityLog objects
     */
    public List<SecurityLog> getUnresolvedSecurityLogs() {
        return securityLogDAO.getUnresolvedSecurityLogs();
    }
    
    /**
     * Resolves a security log by updating its status.
     *
     * @param logId Unique identifier of the log to resolve
     * @param resolvedBy Identifier of the person resolving the log
     * @param resolutionNotes Notes describing the resolution
     * @return true if resolution was successful, false otherwise
     */
    public boolean resolveSecurityLog(String logId, String resolvedBy, String resolutionNotes) {
        return securityLogDAO.resolveSecurityLog(logId, resolvedBy, resolutionNotes);
    }
    
    /**
     * Retrieves recent security logs within the specified time frame.
     *
     * @param hours Number of hours to look back for logs
     * @return List of recent SecurityLog objects
     */
    public List<SecurityLog> getRecentSecurityLogs(int hours) {
        return securityLogDAO.getRecentSecurityLogs(hours);
    }
    
    /**
     * Gets comprehensive security log statistics.
     *
     * @return Array containing [total, critical, high, medium, low, resolved] counts
     */
    public int[] getSecurityLogStatistics() {
        return securityLogDAO.getSecurityLogStatistics();
    }
    
    /**
     * Clears old security logs older than specified days.
     *
     * @param days Number of days to retain logs
     * @return true if cleanup was successful, false otherwise
     */
    public boolean clearOldLogs(int days) {
        return securityLogDAO.clearOldLogs(days);
    }
    
    /**
     * Updates an existing security log.
     *
     * @param log SecurityLog object with updated information
     * @return true if update was successful, false otherwise
     */
    public boolean updateSecurityLog(SecurityLog log) {
        return securityLogDAO.updateSecurityLog(log);
    }
    
    /**
     * Retrieves a security log by its unique identifier.
     *
     * @param logId Unique identifier of the log
     * @return SecurityLog object if found, null otherwise
     */
    public SecurityLog getLogById(String logId) {
        return securityLogDAO.getLogById(logId);
    }
    
    /**
     * Retrieves security logs by event type.
     *
     * @param eventType Type of event to filter by
     * @return List of SecurityLog objects with specified event type
     */
    public List<SecurityLog> getLogsByEventType(String eventType) {
        return securityLogDAO.getLogsByEventType(eventType);
    }
    
    /**
     * Retrieves security logs by location.
     *
     * @param location Location to filter by
     * @return List of SecurityLog objects from specified location
     */
    public List<SecurityLog> getLogsByLocation(String location) {
        return securityLogDAO.getLogsByLocation(location);
    }
    
    /**
     * Gets the total count of security logs.
     *
     * @return Total number of security logs
     */
    public int getTotalLogCount() {
        return securityLogDAO.getTotalLogCount();
    }
    
    /**
     * Deletes a security log from the database.
     *
     * @param logId Unique identifier of the log to delete
     * @return true if deletion was successful, false otherwise
     */
    public boolean deleteSecurityLog(String logId) {
        return securityLogDAO.deleteSecurityLog(logId);
    }
}