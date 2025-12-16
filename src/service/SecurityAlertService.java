package service;

import database.SecurityAlertDAO;
import model.SecurityAlert;
import java.util.List;

/**
 * Service layer for Security Alert management operations. Implements Singleton
 * pattern to provide a single instance throughout the application. Handles
 * business logic for alert creation, tracking, and resolution.
 */
public class SecurityAlertService {

    private static SecurityAlertService instance;
    private final SecurityAlertDAO securityAlertDAO;

    /**
     * Private constructor to enforce Singleton pattern. Initializes the DAO
     * instance.
     */
    private SecurityAlertService() {
        this.securityAlertDAO = SecurityAlertDAO.getInstance();
    }

    /**
     * Returns the single instance of SecurityAlertService. Implements
     * thread-safe lazy initialization.
     *
     * @return SecurityAlertService instance
     */
    public static synchronized SecurityAlertService getInstance() {
        if (instance == null) {
            instance = new SecurityAlertService();
        }
        return instance;
    }

    /**
     * Retrieves all active security alerts from the database.
     *
     * @return List of active SecurityAlert objects
     */
    public List<SecurityAlert> getActiveSecurityAlerts() {
        return securityAlertDAO.getActiveSecurityAlerts();
    }

    /**
     * Creates a new security alert in the database.
     *
     * @param alert SecurityAlert object containing alert details
     * @return true if alert was created successfully, false otherwise
     */
    public boolean createSecurityAlert(SecurityAlert alert) {
        return securityAlertDAO.createSecurityAlert(alert);
    }

    /**
     * Retrieves security alerts triggered within the specified hours.
     *
     * @param hours Number of hours to look back for alerts
     * @return List of recent SecurityAlert objects
     */
    public List<SecurityAlert> getRecentSecurityAlerts(int hours) {
        return securityAlertDAO.getRecentSecurityAlerts(hours);
    }

    /**
     * Retrieves security alerts by severity level.
     *
     * @param severity Severity level to filter by
     * @return List of SecurityAlert objects with specified severity
     */
    public List<SecurityAlert> getSecurityAlertsBySeverity(String severity) {
        return securityAlertDAO.getSecurityAlertsBySeverity(severity);
    }

    /**
     * Acknowledges a security alert by updating its status.
     *
     * @param alertId Unique identifier of the alert
     * @param acknowledgedBy Identifier of the person acknowledging the alert
     * @return true if acknowledgment was successful, false otherwise
     */
    public boolean acknowledgeAlert(String alertId, String acknowledgedBy) {
        return securityAlertDAO.acknowledgeAlert(alertId, acknowledgedBy);
    }

    /**
     * Resolves a security alert by updating its status and adding resolution
     * details.
     *
     * @param alertId Unique identifier of the alert
     * @param resolvedBy Identifier of the person resolving the alert
     * @param resolutionNotes Notes describing how the alert was resolved
     * @return true if resolution was successful, false otherwise
     */
    public boolean resolveAlert(String alertId, String resolvedBy, String resolutionNotes) {
        return securityAlertDAO.resolveAlert(alertId, resolvedBy, resolutionNotes);
    }

    /**
     * Generates a unique alert identifier.
     *
     * @return Unique alert identifier string
     */
    public String generateAlertId() {
        return securityAlertDAO.generateAlertId();
    }

    /**
     * Retrieves a security alert by its unique identifier.
     *
     * @param alertId Unique identifier of the alert
     * @return SecurityAlert object if found, null otherwise
     */
    public SecurityAlert getAlertById(String alertId) {
        return securityAlertDAO.getAlertById(alertId);
    }

    /**
     * Retrieves all security alerts from the database.
     *
     * @return List of all SecurityAlert objects
     */
    public List<SecurityAlert> getAllSecurityAlerts() {
        return securityAlertDAO.getAllSecurityAlerts();
    }

    /**
     * Retrieves alerts by alert type.
     *
     * @param alertType Type of alert to filter by
     * @return List of SecurityAlert objects with specified type
     */
    public List<SecurityAlert> getAlertsByType(String alertType) {
        return securityAlertDAO.getAlertsByType(alertType);
    }

    /**
     * Retrieves alerts by location.
     *
     * @param location Location to filter by
     * @return List of SecurityAlert objects from specified location
     */
    public List<SecurityAlert> getAlertsByLocation(String location) {
        return securityAlertDAO.getAlertsByLocation(location);
    }

    /**
     * Gets the count of alerts by status.
     *
     * @param status Status to filter by
     * @return Number of alerts with specified status
     */
    public int getAlertCountByStatus(String status) {
        return securityAlertDAO.getAlertCountByStatus(status);
    }

    /**
     * Gets the total number of security alerts.
     *
     * @return Total count of security alerts
     */
    public int getTotalAlertCount() {
        return securityAlertDAO.getTotalAlertCount();
    }

    /**
     * Updates an existing security alert.
     *
     * @param alert SecurityAlert object with updated information
     * @return true if update was successful, false otherwise
     */
    public boolean updateAlert(SecurityAlert alert) {
        return securityAlertDAO.updateAlert(alert);
    }

    /**
     * Deletes a security alert from the database.
     *
     * @param alertId Unique identifier of the alert to delete
     * @return true if deletion was successful, false otherwise
     */
    public boolean deleteAlert(String alertId) {
        return securityAlertDAO.deleteAlert(alertId);
    }
}
