package controller;

import service.DatabaseHealthService;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.InputStream;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Employee;

/**
 * Main application controller that coordinates all application flow. Implements
 * Facade pattern to provide a single point of access for UI navigation. Follows
 * Layered Architecture by delegating business logic to services.
 */
public class ApplicationController {

    private static final Logger LOGGER = Logger.getLogger(ApplicationController.class.getName());
    private static final int DB_CONNECTION_RETRIES = 3;
    private static final long DB_CONNECTION_RETRY_DELAY = 2000;

    private static ApplicationController instance;
    private Stage primaryStage;
    private DatabaseHealthService databaseHealthService;

    // Private constructor for Singleton pattern
    private ApplicationController() {
        initializeServices();
    }

    /**
     * Returns the Singleton instance of ApplicationController
     */
    public static synchronized ApplicationController getInstance() {
        if (instance == null) {
            instance = new ApplicationController();
        }
        return instance;
    }

    /**
     * Initialize all required services
     */
    private void initializeServices() {
        this.databaseHealthService = DatabaseHealthService.getInstance();
    }

    /**
     * Set the primary stage for the application
     */
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    /**
     * Get the primary stage
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * Main initialization method called from MainApp Orchestrates the entire
     * startup process
     */
    public void initializeApplication() {
        LOGGER.info("Starting application initialization...");

        // Run initialization in a separate thread to keep UI responsive
        new Thread(() -> {
            try {
                // Step 1: Test database connection
                boolean dbConnected = initializeDatabaseConnection();

                Platform.runLater(() -> {
                    if (dbConnected) {
                        // Step 2: Show login screen
                        showLoginWindow();
                        LOGGER.info("Application initialization completed successfully");
                    } else {
                        // Database connection failed
                        showDatabaseConnectionError();
                    }
                });

            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Application initialization failed", e);
                Platform.runLater(() -> showFatalErrorDialog(
                        "Initialization Error",
                        "Failed to initialize application: " + e.getMessage()
                ));
            }
        }).start();
    }

    /**
     * Initialize and test database connection with retry logic
     */
    private boolean initializeDatabaseConnection() {
        LOGGER.info("Testing database connection...");

        boolean isConnected = databaseHealthService.testConnectionWithRetry(
                DB_CONNECTION_RETRIES,
                DB_CONNECTION_RETRY_DELAY
        );

        if (isConnected) {
            LOGGER.info("Database connection established successfully");
            return true;
        } else {
            LOGGER.severe("Failed to establish database connection after "
                    + DB_CONNECTION_RETRIES + " attempts");
            return false;
        }
    }

    /**
     * Display the login screen
     */
    public void showLoginWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
            Parent root = loader.load();

            // Pass ApplicationController to LoginController
            LoginController loginController = loader.getController();
            loginController.setApplicationController(this);

            Stage stage = new Stage();
            stage.setTitle("Prison Management System - Login");
            stage.setScene(new Scene(root));

            // Set application icon for login window - FIXED HERE
            setStageIcon(stage);

            stage.show();

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to load login window", e);
            showErrorDialog("Login Error", "Cannot load login window: " + e.getMessage());
        }
    }

    /**
     * Set application icon on stage
     */
    private void setStageIcon(Stage stage) {
        try {
            // Try primary application icon
            InputStream iconStream = getClass().getResourceAsStream("/images/login.png");

            // Fallback to login icon if app icon not found
            if (iconStream == null) {
                LOGGER.warning("Primary application icon not found, trying fallback...");
                iconStream = getClass().getResourceAsStream("/images/login.png");
            }

            // Fallback to any available icon
            if (iconStream == null) {
                LOGGER.warning("Fallback icon not found, trying generic icon...");
                iconStream = getClass().getResourceAsStream("/images/login.png");
            }

            if (iconStream != null) {
                Image icon = new Image(iconStream);
                stage.getIcons().add(icon);
                LOGGER.info("Stage icon set successfully");
            } else {
                LOGGER.warning("No application icon found");
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Could not load application icon", e);
        }
    }

/**
 * Navigate to user dashboard based on role
 *
 * @param employee The authenticated employee object
 * @param isAdmin Whether the user is an administrator
 */
public void navigateToDashboard(Employee employee, boolean isAdmin) {
    try {
        String fxmlFile = isAdmin ? "/view/AdminDashboard.fxml" : "/view/OfficerDashboard.fxml";
        String title = isAdmin ? "Administrator Dashboard" : "Officer Dashboard";

        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        Parent root = loader.load();

        Object controller = loader.getController();

        if (controller instanceof AdminDashboardController) {
            AdminDashboardController adminController = (AdminDashboardController) controller;
            adminController.setCurrentEmployee(employee);
            adminController.setApplicationController(this);
        } else if (controller instanceof OfficerDashboardController) {
            OfficerDashboardController officerController = (OfficerDashboardController) controller;
            officerController.setCurrentEmployee(employee);  // THIS IS WHAT'S MISSING!
            officerController.setApplicationController(this);
            
            System.out.println("Employee object passed to dashboard: " + employee.getName());
        } else {
            LOGGER.warning("Unknown controller type for dashboard");
        }

        // Create new stage for dashboard
        Stage dashboardStage = new Stage();
        dashboardStage.setTitle("Prison Management System - " + title);
        dashboardStage.setScene(new Scene(root));
        dashboardStage.setMaximized(true);
        
        setStageIcon(dashboardStage);

        dashboardStage.setOnCloseRequest(event -> {
            event.consume();
            handleDashboardClose(dashboardStage);
        });

        dashboardStage.show();

        // Close login window
        if (primaryStage != null && primaryStage.isShowing()) {
            primaryStage.close();
        }

        LOGGER.info("Dashboard displayed for user: " + employee.getId()
                + " (Admin: " + isAdmin + ")");

    } catch (Exception e) {
        LOGGER.log(Level.SEVERE, "Failed to load dashboard", e);
        showErrorDialog("Navigation Error", "Cannot load dashboard: " + e.getMessage());

        showLoginWindow();
    }
}

    /**
     * Navigate back to login screen (for logout)
     */
    public void navigateToLogin() {
        LOGGER.info("Navigating back to login screen");
        showLoginWindow();
    }

    /**
     * Show error dialog
     */
    public void showErrorDialog(String title, String message) {
        Platform.runLater(() -> {
            try {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle(title);
                alert.setHeaderText(null);
                alert.setContentText(message);

                // Use primaryStage as owner
                if (primaryStage != null) {
                    alert.initOwner(primaryStage);
                }

                // Set icon for dialog
                setAlertIcon(alert);

                alert.show();
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Failed to show error dialog", e);
            }
        });
    }

    /**
     * Set icon for alert dialogs
     */
    private void setAlertIcon(Alert alert) {
        try {
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            setStageIcon(stage);
        } catch (Exception e) {
            LOGGER.log(Level.FINE, "Could not set alert icon", e);
        }
    }

    /**
     * Show warning dialog
     */
    public void showWarningDialog(String title, String message) {
        Platform.runLater(() -> {
            try {
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle(title);
                alert.setHeaderText(null);
                alert.setContentText(message);

                if (primaryStage != null) {
                    alert.initOwner(primaryStage);
                }

                // Set icon for dialog
                setAlertIcon(alert);

                alert.show();
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Failed to show warning dialog", e);
            }
        });
    }

    /**
     * Show confirmation dialog with callback
     */
    public void showConfirmationDialog(String title, String message, DialogCallback callback) {
        Platform.runLater(() -> {
            try {
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle(title);
                alert.setHeaderText(null);
                alert.setContentText(message);

                if (primaryStage != null) {
                    alert.initOwner(primaryStage);
                }

                // Set icon for dialog
                setAlertIcon(alert);

                Optional<ButtonType> result = alert.showAndWait();
                if (callback != null) {
                    callback.onDialogResult(result.isPresent() && result.get() == ButtonType.OK);
                }
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Failed to show confirmation dialog", e);
            }
        });
    }

    /**
     * Show confirmation dialog (simple version without callback)
     */
    public boolean showConfirmationDialog(String title, String message) {
        final boolean[] result = new boolean[1];

        // Use the callback version
        showConfirmationDialog(title, message, confirmed -> {
            result[0] = confirmed;
        });

        // Wait a bit for the response (simplified approach)
        try {
            Thread.sleep(300); // Small delay
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return result[0];
    }

    /**
     * Show information dialog
     */
    public void showInfoDialog(String title, String message) {
        Platform.runLater(() -> {
            try {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle(title);
                alert.setHeaderText(null);
                alert.setContentText(message);

                if (primaryStage != null) {
                    alert.initOwner(primaryStage);
                }

                // Set icon for dialog
                setAlertIcon(alert);

                alert.show();
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Failed to show info dialog", e);
            }
        });
    }

    /**
     * Handle application exit with confirmation
     */
    /**
     * Handle application exit with confirmation
     */
    public void handleApplicationExit() {
        Platform.runLater(() -> {
            try {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Exit Application");
                alert.setHeaderText("Are you sure you want to exit the Prison Management System?");
                alert.setContentText("All unsaved data will be lost.");

                // Set owner to primaryStage if available
                if (primaryStage != null) {
                    alert.initOwner(primaryStage);
                }

                // Try to set icon
                try {
                    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    setStageIcon(stage);
                } catch (Exception e) {
                    // Icon is optional
                    LOGGER.log(Level.FINE, "Could not set alert icon", e);
                }

                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    cleanup();
                    Platform.exit();
                    System.exit(0);
                }
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Failed to show exit confirmation dialog", e);
                // Fallback to simple exit
                cleanup();
                Platform.exit();
                System.exit(0);
            }
        });
    }

    /**
     * Handle dashboard window close
     */
    private void handleDashboardClose(Stage dashboardStage) {
        showConfirmationDialog(
                "Logout",
                "Are you sure you want to logout?",
                confirmed -> {
                    if (confirmed) {
                        dashboardStage.close();
                        showLoginWindow();
                    }
                }
        );
    }

    /**
     * Show fatal error dialog (for unrecoverable errors)
     */
    private void showFatalErrorDialog(String title, String message) {
        Platform.runLater(() -> {
            try {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle(title);
                alert.setHeaderText("Fatal Application Error");
                alert.setContentText(message + "\n\nApplication will now exit.");

                if (primaryStage != null) {
                    alert.initOwner(primaryStage);
                }

                // Set icon for dialog
                setAlertIcon(alert);

                alert.setOnCloseRequest(event -> Platform.exit());

                alert.showAndWait();
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Failed to show fatal error dialog", e);
            } finally {
                Platform.exit();
                System.exit(1);
            }
        });
    }

    /**
     * Show database connection error dialog
     */
    private void showDatabaseConnectionError() {
        Platform.runLater(() -> {
            try {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Database Connection Error");
                alert.setHeaderText("Cannot Connect to Database");
                alert.setContentText(
                        "The application cannot establish a connection to the database.\n\n"
                        + "Please ensure that:\n"
                        + "1. The database server is running\n"
                        + "2. Network connection is available\n"
                        + "3. Database credentials are correct\n\n"
                        + "Click 'Retry' to try again or 'Exit' to close the application."
                );

                ButtonType retryButton = new ButtonType("Retry");
                ButtonType exitButton = new ButtonType("Exit");

                alert.getButtonTypes().setAll(retryButton, exitButton);

                // Set icon for dialog
                setAlertIcon(alert);

                Optional<ButtonType> result = alert.showAndWait();

                if (result.isPresent() && result.get() == retryButton) {
                    // Retry initialization
                    initializeApplication();
                } else {
                    // Exit application
                    Platform.exit();
                    System.exit(1);
                }
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Failed to show database error dialog", e);
                Platform.exit();
                System.exit(1);
            }
        });
    }

    /**
     * Cleanup resources before application exit
     */
    public void cleanup() {
        LOGGER.info("Cleaning up application resources...");

        // Close database connections
        try {
            database.DatabaseConnection.closeStaticConnection();
            LOGGER.info("Database connections closed");
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error closing database connections", e);
        }

        LOGGER.info("Cleanup completed");
    }

    /**
     * Check if database is available
     */
    public boolean isDatabaseAvailable() {
        return databaseHealthService.testConnection();
    }

    /**
     * Get database connection status for UI display
     */
    public String getDatabaseStatus() {
        return databaseHealthService.getConnectionStatus();
    }

    /**
     * Refresh database connection
     */
    public boolean refreshDatabaseConnection() {
        LOGGER.info("Refreshing database connection...");
        return databaseHealthService.testConnection();
    }

    /**
     * Callback interface for dialog results
     */
    public interface DialogCallback {

        void onDialogResult(boolean confirmed);
    }
}
