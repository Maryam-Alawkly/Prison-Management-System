package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Employee;
import javafx.scene.image.Image;

import java.net.URL;
import java.util.ResourceBundle;
import service.*;

/**
 * Controller for the Administrator Dashboard. Implements Initializable for
 * JavaFX initialization. Uses Singleton pattern for service instances.
 */
public class AdminDashboardController implements Initializable {

    // FXML injected UI components
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

    // Current logged-in employee
    private Employee currentEmployee;

    // Service instances (Singleton pattern)
    private PrisonerService prisonerService;
    private EmployeeService employeeService;
    private VisitorService visitorService;
    private PrisonCellService prisonCellService;
    private ApplicationController appController;
    private String employeeId;

    /**
     * Initializes the controller class. Called automatically after FXML
     * loading.
     *
     * @param location The location used to resolve relative paths for the root
     * object
     * @param resources The resources used to localize the root object
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeServices();
        loadDashboardData();
        scheduleIconLoading();
        setupWindowCloseHandler();
    }

    /**
     * Initializes service instances using Singleton pattern.
     */
    private void initializeServices() {
        prisonerService = PrisonerService.getInstance();
        employeeService = EmployeeService.getInstance();
        visitorService = VisitorService.getInstance();
        prisonCellService = PrisonCellService.getInstance();
    }

    /**
     * Sets up window close handler for X button.
     */
    private void setupWindowCloseHandler() {
        javafx.application.Platform.runLater(() -> {
            try {
                Stage stage = (Stage) welcomeLabel.getScene().getWindow();
                if (stage != null) {
                    stage.setOnCloseRequest(event -> {
                        event.consume();
                        handleExitApplication();
                    });
                    System.out.println("Window close handler set up for admin dashboard");
                }
            } catch (Exception e) {
                System.err.println("Error setting up window close handler: " + e.getMessage());
            }
        });
    }

    /**
     * Schedules window icon loading after UI is fully rendered.
     */
    private void scheduleIconLoading() {
        javafx.application.Platform.runLater(this::setWindowIcon);
    }

    /**
     * Sets the window icon for the current stage.
     */
    private void setWindowIcon() {
        try {
            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            if (stage != null) {
                loadIconWithFallback(stage, "/images/admin.png");
            }
        } catch (Exception e) {
            System.err.println("Error loading window icon: " + e.getMessage());
        }
    }

    /**
     * For dependency injection from ApplicationController
     */
    public void setApplicationController(ApplicationController appController) {
        this.appController = appController;
    }

    /**
     * Sets the employee ID and loads employee data
     */
    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
        loadEmployeeData();
    }

    /**
     * Sets the current logged-in employee and updates welcome message.
     *
     * @param employee The employee object representing the current user
     */
    public void setCurrentEmployee(Employee employee) {
        this.currentEmployee = employee;
        updateWelcomeMessage();
    }

    /**
     * Loads employee data based on employeeId
     */
    private void loadEmployeeData() {
        if (employeeId != null) {
            System.out.println("Loading data for employee: " + employeeId);
            // You can load employee data from database here
            // currentEmployee = employeeService.getEmployeeById(employeeId);
            // updateWelcomeMessage();
        }
    }

    /**
     * Updates the welcome message with current employee's name.
     */
    private void updateWelcomeMessage() {
        if (currentEmployee != null) {
            welcomeLabel.setText("Welcome, " + currentEmployee.getName() + " (Administrator)");
        }
    }

    /**
     * Handles exit application action (for X button).
     */
    private void handleExitApplication() {
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Exit Application");
            alert.setHeaderText("Are you sure you want to exit?");
            alert.setContentText("The Prison Management System will be closed.");

            ButtonType exitButton = new ButtonType("Exit");
            ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(exitButton, cancelButton);

            if (welcomeLabel != null && welcomeLabel.getScene() != null) {
                Stage currentStage = (Stage) welcomeLabel.getScene().getWindow();
                alert.initOwner(currentStage);
            }

            alert.showAndWait().ifPresent(response -> {
                if (response == exitButton) {
                    System.out.println("Exiting application from admin dashboard...");
                    javafx.application.Platform.exit();
                    System.exit(0);
                }
            });
        } catch (Exception e) {
            System.err.println("Error handling exit application: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Exit button handler in menu bar.
     */
    @FXML
    private void handleExit() {
        handleExitApplication();
    }

    /**
     * Logout method - returns to login page.
     */
    @FXML
    private void handleLogout() {
        try {
            // Show confirmation dialog
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Logout");
            alert.setHeaderText("Are you sure you want to logout?");
            alert.setContentText("You will be redirected to the login screen.");

            ButtonType logoutButton = new ButtonType("Logout");
            ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(logoutButton, cancelButton);

            if (welcomeLabel != null && welcomeLabel.getScene() != null) {
                Stage currentStage = (Stage) welcomeLabel.getScene().getWindow();
                alert.initOwner(currentStage);
            }

            alert.showAndWait().ifPresent(response -> {
                if (response == logoutButton) {
                    performLogout();
                }
            });

        } catch (Exception e) {
            System.err.println("Error during logout confirmation: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Performs the actual logout operation.
     */
    private void performLogout() {
        System.out.println("Starting logout process for admin...");

        try {
            if (appController != null) {
                // Use ApplicationController to navigate to login
                appController.navigateToLogin();
            } else {
                // Fallback if appController is not available
                fallbackLogout();
            }

            // Close current window
            closeCurrentWindow();

        } catch (Exception e) {
            System.err.println("Error during logout: " + e.getMessage());
            e.printStackTrace();
            fallbackLogout();
        }
    }

    /**
     * Closes the current window.
     */
    private void closeCurrentWindow() {
        javafx.application.Platform.runLater(() -> {
            try {
                if (welcomeLabel != null && welcomeLabel.getScene() != null) {
                    Stage currentStage = (Stage) welcomeLabel.getScene().getWindow();
                    if (currentStage != null) {
                        currentStage.close();
                    }
                }
            } catch (Exception e) {
                System.err.println("Error closing current window: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    /**
     * Fallback logout method if ApplicationController is not available
     */
    private void fallbackLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Prison Management System - Login");
            loadIconWithFallback(stage, "/images/login.png");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();

            // Close current window
            closeCurrentWindow();
        } catch (Exception e) {
            System.err.println("Critical error during fallback logout: " + e.getMessage());
        }
    }

    /**
     * Attempts to load an icon from multiple possible paths. Implements
     * fallback mechanism for icon loading.
     *
     * @param stage The stage to set the icon for
     * @param primaryIconPath The primary path to try for the icon
     */
    private void loadIconWithFallback(Stage stage, String primaryIconPath) {
        String[] possiblePaths = {
            primaryIconPath,
            "/images/admin.png",
            "/images/login.png",
            "images/admin.png"
        };

        boolean iconLoaded = false;
        for (String path : possiblePaths) {
            try {
                Image icon = new Image(getClass().getResourceAsStream(path));
                if (icon != null && !icon.isError()) {
                    stage.getIcons().add(icon);
                    iconLoaded = true;
                    break;
                }
            } catch (Exception e) {
                // Continue to next path
            }
        }

        if (!iconLoaded) {
            System.err.println("Warning: Could not load any icon for: " + stage.getTitle());
        }
    }

    /**
     * Loads and displays dashboard statistics from various services.
     */
    private void loadDashboardData() {
        try {
            prisonerCountLabel.setText(String.valueOf(prisonerService.getAllPrisoners().size()));
            employeeCountLabel.setText(String.valueOf(employeeService.getAllEmployees().size()));
            visitorCountLabel.setText(String.valueOf(visitorService.getAllVisitors().size()));
            updateCellOccupancyDisplay();
        } catch (Exception e) {
            System.err.println("Error loading dashboard data: " + e.getMessage());
        }
    }

    /**
     * Calculates and displays cell occupancy percentage.
     */
    private void updateCellOccupancyDisplay() {
        int[] stats = prisonCellService.getOccupancyStatistics();
        if (stats[0] > 0) {
            double occupancyRate = (double) stats[1] / stats[0] * 100;
            cellOccupancyLabel.setText(String.format("%.1f%%", occupancyRate));
        } else {
            cellOccupancyLabel.setText("0%");
        }
    }

    // FXML Action Handlers for menu navigation
    @FXML
    private void openPrisonerManagement() {
        openWindow("/view/PrisonerManagement.fxml", "Prisoner Management", "/images/prisoners.png");
    }

    @FXML
    private void openEmployeeManagement() {
        openWindow("/view/EmployeeManagement.fxml", "Employee Management", "/images/employee.png");
    }

    @FXML
    private void openCellManagement() {
        openWindow("/view/CellManagement.fxml", "Cell Management", "/images/cell.png");
    }

    @FXML
    private void openReports() {
        openWindow("/view/ReportManagement.fxml", "Reports Management", "/images/reports.png");
    }

    @FXML
    private void openSecurityManagement() {
        openWindow("/view/SecurityManagement.fxml", "Security Management System", "/images/security.png");
    }

    @FXML
    private void openVisitorManagement() {
        openWindow("/view/VisitorManagement.fxml", "Visitor Management", "/images/visitor.png");
    }

    @FXML
    private void openVisitManagement() {
        openWindow("/view/VisitManagement.fxml", "Visit Management", "/images/visits.png");
    }

    /**
     * Opens a new window with specified FXML, title, and icon. Uses
     * Factory-like pattern for window creation.
     *
     * @param fxmlPath Path to the FXML file
     * @param title Window title
     * @param iconPath Path to the icon file
     */
    private void openWindow(String fxmlPath, String title, String iconPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle(title);
            loadIconWithFallback(stage, iconPath);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            System.err.println("Error opening " + title + ": " + e.getMessage());
        }
    }

    /**
     * Refreshes dashboard data.
     */
    @FXML
    private void refreshDashboard() {
        loadDashboardData();
    }
}