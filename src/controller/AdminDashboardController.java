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
 * Controller for Administrator Dashboard
 */
public class AdminDashboardController implements Initializable {

    @FXML
    private Label welcomeLabel;
    @FXML
    private Label prisonerCountLabel;
    @FXML
    private Label employeeCountLabel;
    @FXML
    private Label visitorCountLabel;
    @FXML
    private Label cellOccupancyLabel;

    private Employee currentEmployee;
    private PrisonerDAO prisonerDAO;
    private EmployeeDAO employeeDAO;
    private VisitorDAO visitorDAO;
    private PrisonCellDAO cellDAO;

    /**
     * Initialize the controller
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        prisonerDAO = new PrisonerDAO();
        employeeDAO = new EmployeeDAO();
        visitorDAO = new VisitorDAO();
        cellDAO = new PrisonCellDAO();

        loadDashboardData();
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
            welcomeLabel.setText("Welcome, " + currentEmployee.getName() + " (Administrator)");
        }
    }

    /**
     * Load dashboard statistics
     */
    private void loadDashboardData() {
        try {
            prisonerCountLabel.setText(String.valueOf(prisonerDAO.getAllPrisoners().size()));
            employeeCountLabel.setText(String.valueOf(employeeDAO.getAllEmployees().size()));
            visitorCountLabel.setText(String.valueOf(visitorDAO.getAllVisitors().size()));

            // Calculate cell occupancy percentage
            int[] stats = cellDAO.getOccupancyStatistics();
            if (stats[0] > 0) {
                double occupancyRate = (double) stats[1] / stats[0] * 100;
                cellOccupancyLabel.setText(String.format("%.1f%%", occupancyRate));
            } else {
                cellOccupancyLabel.setText("0%");
            }

        } catch (Exception e) {
            System.err.println("Error loading dashboard data: " + e.getMessage());
        }
    }

    /**
     * Open Prisoner Management
     */
    @FXML
    private void openPrisonerManagement() {
        openWindow("/view/PrisonerManagement.fxml", "Prisoner Management");
    }

    /**
     * Open Employee Management
     */
    @FXML
    private void openEmployeeManagement() {
        openWindow("/view/EmployeeManagement.fxml", "Employee Management");
    }

    /**
     * Open PrisonCell Management
     */
    @FXML
    private void openCellManagement() {
        openWindow("/view/CellManagement.fxml", "Cell Management");
    }

    /**
     * Open Reports
     */
    @FXML
    private void openReports() {
        // Implementation for reports window
       // openWindow("/view/Reports.fxml", "Reports Management");
    }

    /**
     * Open Security Management
     */
    @FXML
    private void openSecurityManagement() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SecurityManagement.fxml"));
            Parent root = loader.load();

            // Pass current employee to controller
            SecurityManagementController controller = loader.getController();
            controller.setCurrentEmployee(currentEmployee);

            Stage stage = new Stage();
            stage.setTitle("Security Management System");
            stage.setScene(new Scene(root, 1400, 900));
            stage.setMaximized(true);
            stage.show();
        } catch (Exception e) {
            System.err.println("Error opening security management: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Open Visitor Management
     */
    @FXML
    private void openVisitorManagement() {
        openWindow("/view/VisitorManagement.fxml", "Visitor Management");
    }

    /**
     * Open Visit Management
     */
    @FXML
    private void openVisitManagement() {
        openWindow("/view/VisitManagement.fxml", "Visit Management");
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
