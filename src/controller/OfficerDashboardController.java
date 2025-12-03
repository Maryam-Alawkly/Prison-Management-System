package controller;

import database.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import model.Employee;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for Officer Dashboard
 */
public class OfficerDashboardController implements Initializable {

    @FXML
    private Label welcomeLabel;
    @FXML
    private Label supervisedPrisonersLabel;
    @FXML
    private Label todaysVisitsLabel;
    @FXML
    private Label cellStatusLabel;
    @FXML
    private Label activeAlertsLabel;
    @FXML
    private Label currentShiftLabel;

    private Employee currentEmployee;
    private PrisonerDAO prisonerDAO;
    private VisitDAO visitDAO;
    private PrisonCellDAO cellDAO;

    /**
     * Initialize the controller
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        prisonerDAO = new PrisonerDAO();
        visitDAO = new VisitDAO();
        cellDAO = new PrisonCellDAO();

        loadDashboardData();
        setupShiftTimer();
    }

    /**
     * Set current employee
     *
     * @param employee Current logged in employee
     */
    public void setCurrentEmployee(Employee employee) {
        this.currentEmployee = employee;
        updateWelcomeMessage();
    }

    /**
     * Update welcome message with employee name
     */
    private void updateWelcomeMessage() {
        if (currentEmployee != null) {
            welcomeLabel.setText("Welcome, Officer " + currentEmployee.getName());
        }
    }

    /**
     * Load dashboard data for officer
     */
    private void loadDashboardData() {
        try {
            // For demo, using total prisoners (in real app, filter by officer assignment)
            supervisedPrisonersLabel.setText(String.valueOf(prisonerDAO.getAllPrisoners().size()) + " prisoners");

            // Today's visits
            int todaysVisits = visitDAO.getTodaysVisits().size();
            todaysVisitsLabel.setText(todaysVisits + " visits today");

            // PrisonCell status
            int[] stats = cellDAO.getOccupancyStatistics();
            cellStatusLabel.setText(stats[1] + "/" + stats[0] + " occupied");

            // Mock data for alerts
            activeAlertsLabel.setText("3 active alerts");

        } catch (Exception e) {
            System.err.println("Error loading officer dashboard data: " + e.getMessage());
        }
    }

    /**
     * Setup shift timer display
     */
    private void setupShiftTimer() {
        // In real app, connect to shift management system
        currentShiftLabel.setText("Shift: 08:00 - 16:00");
    }

    /**
     * Open Prisoner Management (Limited)
     */
    @FXML
    private void openPrisonerManagement() {
        openWindow("/view/PrisonerManagement.fxml", "Prisoner Management - Officer");
    }

    /**
     * Open Visit Management
     */
    @FXML
    private void openVisitManagement() {
        openWindow("/view/VisitManagement.fxml", "Visit Management");
    }

    /**
     * Open PrisonCell Monitoring
     */
    @FXML
    private void openPrisonCellMonitoring() {
        openWindow("/view/CellViewOnly.fxml", "Cell Monitoring");
    }

    /**
     * Open Guard Duties
     */
    @FXML
    private void openGuardDuties() {
        // Implementation for guard duties
        System.out.println("Opening guard duties...");
    }

    /**
     * Open Alerts
     */
    @FXML
    private void openAlerts() {
        // Implementation for alerts management
        System.out.println("Opening alerts...");
    }

    /**
     * Open Daily Reports
     */
    @FXML
    private void openDailyReports() {
        // Implementation for daily reports
        System.out.println("Opening daily reports...");
    }

    /**
     * Open window helper method
     */
    private void openWindow(String fxmlPath, String title) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root, 1000, 700));
            stage.show();
        } catch (Exception e) {
            System.err.println("Error opening " + title + ": " + e.getMessage());
        }
    }

    /**
     * Refresh dashboard
     */
    @FXML
    private void refreshDashboard() {
        loadDashboardData();
    }

    /**
     * Logout
     */
    @FXML
    private void logout() {
        try {
            // Open login window
            Parent root = FXMLLoader.load(getClass().getResource("/view/Login.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Prison Management System - Login");
            stage.setScene(new Scene(root));
            stage.show();

            // Close current window
            Stage currentStage = (Stage) welcomeLabel.getScene().getWindow();
            currentStage.close();

        } catch (Exception e) {
            System.err.println("Error during logout: " + e.getMessage());
        }
    }
}
