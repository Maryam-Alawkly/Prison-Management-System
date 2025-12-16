package service;

import database.DailyReportDAO;
import model.DailyReport;
import java.time.LocalDate;
import java.util.List;

/**
 * Service layer for Daily Report management operations.
 * Implements Singleton pattern to provide a single instance throughout the application.
 * Handles business logic for report creation, review, and management.
 */
public class DailyReportService {
    
    private static DailyReportService instance;
    private final DailyReportDAO dailyReportDAO;
    
    /**
     * Private constructor to enforce Singleton pattern.
     * Initializes the DAO instance.
     */
    private DailyReportService() {
        this.dailyReportDAO = DailyReportDAO.getInstance();
    }
    
    /**
     * Returns the single instance of DailyReportService.
     * Implements thread-safe lazy initialization.
     *
     * @return DailyReportService instance
     */
    public static synchronized DailyReportService getInstance() {
        if (instance == null) {
            instance = new DailyReportService();
        }
        return instance;
    }
    
    /**
     * Retrieves all daily reports from the database.
     *
     * @return List of all DailyReport objects
     */
    public List<DailyReport> getAllReports() {
        return dailyReportDAO.getAllReports();
    }
    
    /**
     * Searches for daily reports based on multiple criteria.
     *
     * @param searchText Text to search in report ID, officer name, or incidents summary
     * @param status Report status filter
     * @param type Report type filter
     * @param dateFrom Start date for date range filter
     * @param dateTo End date for date range filter
     * @return List of DailyReport objects matching search criteria
     */
    public List<DailyReport> searchReports(String searchText, String status, String type,
                                          LocalDate dateFrom, LocalDate dateTo) {
        return dailyReportDAO.searchReports(searchText, status, type, dateFrom, dateTo);
    }
    
    /**
     * Saves a daily report to the database.
     *
     * @param report DailyReport object to save
     * @return true if save operation was successful, false otherwise
     */
    public boolean saveReport(DailyReport report) {
        return dailyReportDAO.saveReport(report);
    }
    
    /**
     * Updates the status of a daily report and records review information.
     *
     * @param reportId Unique identifier of the report
     * @param status New status for the report
     * @param reviewedBy Name/ID of the reviewer
     * @param comments Review comments
     * @return true if update was successful, false otherwise
     */
    public boolean updateReportStatus(String reportId, String status, String reviewedBy, String comments) {
        return dailyReportDAO.updateReportStatus(reportId, status, reviewedBy, comments);
    }
    
    /**
     * Adds an admin comment to an existing report.
     *
     * @param reportId Unique identifier of the report
     * @param comment Comment text to add
     * @param adminId Identifier of the admin making the comment
     * @return true if comment was added successfully, false otherwise
     */
    public boolean addAdminComment(String reportId, String comment, String adminId) {
        return dailyReportDAO.addAdminComment(reportId, comment, adminId);
    }
    
    /**
     * Sends feedback to an officer regarding their report.
     *
     * @param reportId Report identifier
     * @param officerId Officer identifier
     * @param feedback Feedback text
     * @param fromAdmin Admin identifier sending the feedback
     * @return true if feedback was sent successfully, false otherwise
     */
    public boolean sendFeedbackToOfficer(String reportId, String officerId, String feedback, String fromAdmin) {
        return dailyReportDAO.sendFeedbackToOfficer(reportId, officerId, feedback, fromAdmin);
    }
    
    /**
     * Retrieves reports for a specific officer.
     *
     * @param officerId Officer identifier
     * @return List of DailyReport objects for the specified officer
     */
    public List<DailyReport> getReportsByOfficer(String officerId) {
        return dailyReportDAO.getReportsByOfficer(officerId);
    }
    
    /**
     * Deletes a daily report from the database.
     *
     * @param reportId Unique identifier of the report to delete
     * @return true if deletion was successful, false otherwise
     */
    public boolean deleteReport(String reportId) {
        return dailyReportDAO.deleteReport(reportId);
    }
    
    /**
     * Gets the total count of daily reports.
     *
     * @return Total number of daily reports
     */
    public int getTotalReportCount() {
        return dailyReportDAO.getTotalReportCount();
    }
}