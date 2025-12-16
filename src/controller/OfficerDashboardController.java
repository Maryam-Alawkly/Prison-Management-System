package controller;

import java.io.InputStream;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.Employee;
import model.SecurityAlert;
import model.Task;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.*;
import javafx.application.Platform;
import javafx.scene.image.Image;
import model.Prisoner;
import service.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * Controller for Officer Dashboard interface. Implements Initializable for
 * JavaFX initialization. Uses Singleton pattern for service instances and
 * Factory pattern for window management. Handles dashboard data loading
 * asynchronously and provides proper window close functionality.
 */
public class OfficerDashboardController implements Initializable {

    // Dashboard UI components
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
    @FXML
    private Label recentAlertsLabel;
    @FXML
    private ProgressIndicator loadingIndicator;
    @FXML
    private VBox contentArea;
    @FXML
    private StackPane mainContainer;

    // Data models
    private Employee currentEmployee;

    // Service instances (Singleton pattern)
    private PrisonerService prisonerService;
    private VisitService visitService;
    private PrisonCellService prisonCellService;
    private SecurityAlertService securityAlertService;
    private TaskService taskService;
    private ApplicationController appController;
    private String employeeId;

    // Constants for configuration
    private static final int DEFAULT_WORKER_THREADS = 4;
    private static final int DATA_LOAD_TIMEOUT_SECONDS = 30;
    private static final String DEFAULT_SHIFT = "Shift: 08:00 - 16:00";
    private static final String LOADING_TEXT = "Loading...";
    private static final String ERROR_TEXT = "Error";
    private static final long SHUTDOWN_TIMEOUT_MS = 1000;

    // Thread pool for asynchronous operations
    private ExecutorService executorService;

    // Cache for static data
    private List<Prisoner> cachedPrisoners;
    private long prisonersCacheTimestamp = 0;
    private static final long CACHE_TTL_MS = 300000; // 5 minutes

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
    try {
        System.out.println("=== Initializing OfficerDashboardController ===");
        
        // Test welcomeLabel immediately
        System.out.println("welcomeLabel reference: " + welcomeLabel);
        System.out.println("welcomeLabel ID: " + (welcomeLabel != null ? welcomeLabel.getId() : "null"));
        System.out.println("welcomeLabel text initially: " + (welcomeLabel != null ? welcomeLabel.getText() : "null"));
        
        initializeServices();
        initializeExecutorService();
        initializeUI();
        showLoadingState();
        loadDashboardDataAsync();
        setupShiftInformation();

        System.out.println("OfficerDashboardController initialized successfully");
    } catch (Exception e) {
        System.err.println("Error initializing dashboard controller: " + e.getMessage());
        e.printStackTrace();
        handleInitializationError(e);
    }
}

    /**
     * Initializes service instances using Singleton pattern.
     */
    private void initializeServices() {
        prisonerService = PrisonerService.getInstance();
        visitService = VisitService.getInstance();
        prisonCellService = PrisonCellService.getInstance();
        securityAlertService = SecurityAlertService.getInstance();
        taskService = TaskService.getInstance();
    }

    /**
     * Initializes the thread pool for asynchronous operations.
     */
    private void initializeExecutorService() {
        executorService = Executors.newFixedThreadPool(DEFAULT_WORKER_THREADS, r -> {
            Thread t = new Thread(r);
            t.setDaemon(true);
            t.setName("Dashboard-Worker-" + t.getId());
            return t;
        });
    }

    /**
     * Initializes UI components with default values.
     */
    private void initializeUI() {
        try {
            setDefaultLabelValues();
            if (loadingIndicator != null) {
                loadingIndicator.setVisible(false);
            }
        } catch (Exception e) {
            System.err.println("Error initializing UI: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Shows loading state in the UI.
     */
    private void showLoadingState() {
        Platform.runLater(() -> {
            try {
                if (loadingIndicator != null) {
                    loadingIndicator.setVisible(true);
                    loadingIndicator.setProgress(-1);
                }
                if (contentArea != null) {
                    contentArea.setDisable(true);
                }
                setDefaultLabelValues();
            } catch (Exception e) {
                System.err.println("Error showing loading state: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    /**
     * Hides loading state and enables UI.
     */
    private void hideLoadingState() {
        Platform.runLater(() -> {
            try {
                if (loadingIndicator != null) {
                    loadingIndicator.setVisible(false);
                }
                if (contentArea != null) {
                    contentArea.setDisable(false);
                }
            } catch (Exception e) {
                System.err.println("Error hiding loading state: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    /**
     * Loads dashboard data asynchronously to prevent UI freezing.
     */
    private void loadDashboardDataAsync() {
        CompletableFuture<Void> dashboardLoadFuture = CompletableFuture.runAsync(() -> {
            try {
                updateSupervisedPrisonersCount();
                loadVisitStatistics();
                loadCellOccupancyStatistics();
                updateActiveAlertsCount();
                loadRecentSecurityAlerts();

                System.out.println("Dashboard data loaded successfully");
            } catch (Exception e) {
                System.err.println("Error loading dashboard data: " + e.getMessage());
                e.printStackTrace();
                Platform.runLater(() -> showDataLoadError());
            }
        }, executorService);

        dashboardLoadFuture.thenRun(() -> {
            Platform.runLater(() -> {
                hideLoadingState();
            });
        }).exceptionally(throwable -> {
            Platform.runLater(() -> {
                hideLoadingState();
                showAlert("Load Error", "Failed to load dashboard data: " + throwable.getMessage(), Alert.AlertType.ERROR);
            });
            return null;
        });
    }

    /**
     * Shows data load error in the UI.
     */
    private void showDataLoadError() {
        Platform.runLater(() -> {
            try {
                if (supervisedPrisonersLabel != null) {
                    supervisedPrisonersLabel.setText(ERROR_TEXT);
                }
                if (todaysVisitsLabel != null) {
                    todaysVisitsLabel.setText(ERROR_TEXT);
                }
                if (cellStatusLabel != null) {
                    cellStatusLabel.setText(ERROR_TEXT);
                }
                if (activeAlertsLabel != null) {
                    activeAlertsLabel.setText(ERROR_TEXT);
                }
            } catch (Exception e) {
                System.err.println("Error showing data load error: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    /**
     * Sets default values for all dashboard labels.
     */
    private void setDefaultLabelValues() {
        try {
            System.out.println("=== Setting Default Label Values ===");
            System.out.println("welcomeLabel is null: " + (welcomeLabel == null));
            System.out.println("supervisedPrisonersLabel is null: " + (supervisedPrisonersLabel == null));
            System.out.println("todaysVisitsLabel is null: " + (todaysVisitsLabel == null));
            System.out.println("cellStatusLabel is null: " + (cellStatusLabel == null));
            System.out.println("activeAlertsLabel is null: " + (activeAlertsLabel == null));
            System.out.println("recentAlertsLabel is null: " + (recentAlertsLabel == null));
            System.out.println("currentShiftLabel is null: " + (currentShiftLabel == null));
            System.out.println("=== End Debug ===");

            // Set values only for non-null labels
            if (welcomeLabel != null) {
                welcomeLabel.setText("Welcome, Officer");
            } else {
                System.err.println("ERROR: welcomeLabel is null!");
            }

            if (supervisedPrisonersLabel != null) {
                supervisedPrisonersLabel.setText(LOADING_TEXT);
            } else {
                System.err.println("ERROR: supervisedPrisonersLabel is null!");
            }

            if (todaysVisitsLabel != null) {
                todaysVisitsLabel.setText(LOADING_TEXT);
            } else {
                System.err.println("ERROR: todaysVisitsLabel is null!");
            }

            if (cellStatusLabel != null) {
                cellStatusLabel.setText(LOADING_TEXT);
            } else {
                System.err.println("ERROR: cellStatusLabel is null!");
            }

            if (activeAlertsLabel != null) {
                activeAlertsLabel.setText(LOADING_TEXT);
            } else {
                System.err.println("ERROR: activeAlertsLabel is null!");
            }

            if (recentAlertsLabel != null) {
                recentAlertsLabel.setText(LOADING_TEXT);
            } else {
                System.err.println("ERROR: recentAlertsLabel is null!");
            }

            System.out.println("Default label values set successfully");
        } catch (Exception e) {
            System.err.println("Error setting default label values: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void setApplicationController(ApplicationController appController) {
        this.appController = appController;
        Platform.runLater(this::setupWindowCloseHandler);
        Platform.runLater(this::setWindowIcon);
    }

    public void setEmployeeId(String employeeId) {
        try {
            this.employeeId = employeeId;
            System.out.println("Setting employee ID: " + employeeId);
            loadEmployeeData();
        } catch (Exception e) {
            System.err.println("Error setting employee ID: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadEmployeeData() {
        try {
            if (employeeId != null && !employeeId.trim().isEmpty()) {
                System.out.println("Loading data for officer: " + employeeId);
            } else {
                System.err.println("Employee ID is null or empty!");
            }
        } catch (Exception e) {
            System.err.println("Error loading employee data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Sets up window close handler to properly handle close button clicks.
     */
    private void setupWindowCloseHandler() {
        try {
            if (welcomeLabel != null && welcomeLabel.getScene() != null) {
                Stage stage = (Stage) welcomeLabel.getScene().getWindow();
                if (stage != null) {
                    stage.setOnCloseRequest(event -> {
                        event.consume();
                        handleExitApplication();
                    });

                    System.out.println("Window close handler set up successfully");
                }
            }
        } catch (Exception e) {
            System.err.println("Error setting up window close handler: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Sets the window icon for the dashboard.
     */
    private void setWindowIcon() {
        try {
            if (welcomeLabel != null && welcomeLabel.getScene() != null) {
                Stage stage = (Stage) welcomeLabel.getScene().getWindow();
                if (stage != null) {
                    if (loadIconFromResource(stage, "/images/officer.png")) {
                        System.out.println("Officer dashboard icon loaded successfully");
                    } else if (loadIconFromResource(stage, "images/officer.png")) {
                        System.out.println("Officer dashboard icon loaded from alternative path");
                    } else {
                        System.err.println("Could not load officer dashboard icon");
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error setting window icon: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Loads an icon from resource path.
     *
     * @param stage The stage to set the icon for
     * @param resourcePath The resource path
     * @return true if icon loaded successfully, false otherwise
     */
    private boolean loadIconFromResource(Stage stage, String resourcePath) {
        try {
            System.out.println("Attempting to load icon from: " + resourcePath);

            InputStream iconStream = getClass().getResourceAsStream(resourcePath);
            if (iconStream == null) {
                System.out.println("Icon not found at: " + resourcePath);
                return false;
            }

            Image icon = new Image(iconStream);
            iconStream.close();

            if (icon.isError()) {
                System.err.println("Error loading icon from: " + resourcePath);
                return false;
            }

            stage.getIcons().clear();
            stage.getIcons().add(icon);
            return true;

        } catch (Exception e) {
            System.err.println("Exception loading icon from " + resourcePath + ": " + e.getMessage());
            return false;
        }
    }

    /**
     * Handles exit application action.
     */
    @FXML
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
                    quickCleanupResources();
                    System.out.println("Exiting application from dashboard close button...");
                    Platform.exit();
                    System.exit(0);
                }
            });
        } catch (Exception e) {
            System.err.println("Error handling exit application: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handles exit action - closes the application immediately.
     */
    @FXML
    private void handleExit() {
        handleExitApplication();
    }

    @FXML
    private void handleLogout() {
        showLogoutConfirmation();
    }

    /**
     * Shows logout confirmation dialog.
     */
    private void showLogoutConfirmation() {
        try {
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
                    Platform.runLater(() -> {
                        try {
                            if (loadingIndicator != null) {
                                loadingIndicator.setVisible(true);
                                loadingIndicator.setProgress(-1);
                            }
                        } catch (Exception e) {
                            System.err.println("Error showing loading indicator: " + e.getMessage());
                            e.printStackTrace();
                        }
                    });

                    performLogout();
                }
            });
        } catch (Exception e) {
            System.err.println("Error showing logout confirmation: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Performs the actual logout operation.
     */
    private void performLogout() {
        System.out.println("Starting logout process...");

        Platform.runLater(() -> {
            try {
                if (loadingIndicator != null) {
                    loadingIndicator.setVisible(true);
                    loadingIndicator.setProgress(-1);
                }
                if (contentArea != null) {
                    contentArea.setDisable(true);
                }
            } catch (Exception e) {
                System.err.println("Error setting up UI for logout: " + e.getMessage());
                e.printStackTrace();
            }
        });

        quickCleanupResources();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        Platform.runLater(() -> {
            try {
                if (appController != null) {
                    System.out.println("Using ApplicationController to navigate to login");
                    appController.navigateToLogin();
                } else {
                    System.out.println("ApplicationController not available, opening login window directly");
                    showLoginWindow();
                }

                closeCurrentWindow();

            } catch (Exception e) {
                System.err.println("Error during logout navigation: " + e.getMessage());
                e.printStackTrace();
                showEmergencyError("Logout failed: " + e.getMessage());
            }
        });
    }

    /**
     * Shows the login window.
     */
    private void showLoginWindow() {
        try {
            System.out.println("Attempting to load login window...");

            URL fxmlUrl = getClass().getResource("/view/Login.fxml");
            if (fxmlUrl == null) {
                System.err.println("ERROR: Cannot find Login.fxml at /view/Login.fxml");
                showEmergencyError("Login screen not found. Please reinstall the application.");
                return;
            }

            System.out.println("Found Login.fxml at: " + fxmlUrl);

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Prison Management System - Login");

            String[] iconPaths = {
                "/images/login.png",
                "/images/login_icon.png",
                "/images/app_icon.png",
                "images/login.png",
                "images/login_icon.png",
                "images/app_icon.png"
            };

            boolean iconLoaded = false;
            for (String path : iconPaths) {
                if (loadIconFromResource(stage, path)) {
                    System.out.println("Login icon loaded from: " + path);
                    iconLoaded = true;
                    break;
                }
            }

            if (!iconLoaded) {
                System.out.println("Warning: No login icon loaded, using default JavaFX icon");
            }

            Scene scene = new Scene(root);
            stage.setScene(scene);

            stage.setMinWidth(400);
            stage.setMinHeight(500);

            stage.show();

            System.out.println("Login window opened successfully");

            closeCurrentWindow();

        } catch (Exception e) {
            System.err.println("CRITICAL ERROR: Cannot open login window");
            e.printStackTrace();
            showEmergencyError("Cannot open login screen: " + e.getMessage());
        }
    }

    /**
     * Shows emergency error message.
     */
    private void showEmergencyError(String message) {
        Platform.runLater(() -> {
            try {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("System Error");
                alert.setHeaderText("Application Error");
                alert.setContentText(message + "\n\nApplication will now close.");
                alert.showAndWait();
                System.exit(1);
            } catch (Exception e) {
                System.err.println("Error showing emergency error: " + e.getMessage());
                e.printStackTrace();
                System.exit(1);
            }
        });
    }

    /**
     * Closes the current window.
     */
    private void closeCurrentWindow() {
        Platform.runLater(() -> {
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
     * Cleans up resources quickly without waiting for thread pool shutdown.
     */
    private void quickCleanupResources() {
        try {
            if (executorService != null && !executorService.isShutdown()) {
                List<Runnable> pendingTasks = executorService.shutdownNow();
                System.out.println("Cancelled " + pendingTasks.size() + " pending tasks");
            }

            cachedPrisoners = null;
        } catch (Exception e) {
            System.err.println("Error during quick cleanup: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Loads and displays today's visit statistics.
     */
    private void loadVisitStatistics() {
        try {
            List<model.Visit> todaysVisits = visitService.getTodaysVisits();
            int visitCount = todaysVisits != null ? todaysVisits.size() : 0;
            String displayText = visitCount + (visitCount == 1 ? " visit today" : " visits today");

            Platform.runLater(() -> {
                try {
                    if (todaysVisitsLabel != null) {
                        todaysVisitsLabel.setText(displayText);
                    }
                } catch (Exception e) {
                    System.err.println("Error updating visits label: " + e.getMessage());
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            System.err.println("Error loading visit statistics: " + e.getMessage());
            e.printStackTrace();
            Platform.runLater(() -> {
                try {
                    if (todaysVisitsLabel != null) {
                        todaysVisitsLabel.setText(ERROR_TEXT);
                    }
                } catch (Exception ex) {
                    System.err.println("Error setting error text for visits: " + ex.getMessage());
                    ex.printStackTrace();
                }
            });
        }
    }

    /**
     * Loads and displays cell occupancy statistics.
     */
    private void loadCellOccupancyStatistics() {
        try {
            int[] stats = prisonCellService.getOccupancyStatistics();
            if (stats != null && stats.length >= 2) {
                String displayText = stats[1] + "/" + stats[0] + " occupied";

                Platform.runLater(() -> {
                    try {
                        if (cellStatusLabel != null) {
                            cellStatusLabel.setText(displayText);
                        }
                    } catch (Exception e) {
                        System.err.println("Error updating cell status label: " + e.getMessage());
                        e.printStackTrace();
                    }
                });
            } else {
                throw new IllegalStateException("Invalid occupancy statistics array");
            }
        } catch (Exception e) {
            System.err.println("Error loading cell occupancy statistics: " + e.getMessage());
            e.printStackTrace();
            Platform.runLater(() -> {
                try {
                    if (cellStatusLabel != null) {
                        cellStatusLabel.setText(ERROR_TEXT);
                    }
                } catch (Exception ex) {
                    System.err.println("Error setting error text for cell status: " + ex.getMessage());
                    ex.printStackTrace();
                }
            });
        }
    }

    /**
     * Sets up shift information display.
     */
    private void setupShiftInformation() {
        Platform.runLater(() -> {
            try {
                if (currentShiftLabel != null) {
                    currentShiftLabel.setText(DEFAULT_SHIFT);
                }
            } catch (Exception e) {
                System.err.println("Error setting up shift information: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    /**
     * Sets the current employee and updates dashboard data.
     *
     * @param employee The employee object representing the current user
     */
    public void setCurrentEmployee(Employee employee) {
        try {
            System.out.println("=== setCurrentEmployee called ===");
            System.out.println("Employee object: " + (employee != null ? "NOT NULL" : "NULL"));

            if (employee != null) {
                System.out.println("Employee Name: " + employee.getName());
                System.out.println("Employee ID: " + employee.getId());
            }

            this.currentEmployee = employee;

            if (employee != null) {
                System.out.println("Employee set to " + employee.getName() + " (ID: " + employee.getId() + ")");

                // Update welcome message immediately
                updateWelcomeMessage();

                // Load employee-specific data asynchronously
                updateEmployeeSpecificData();
            } else {
                System.err.println("Employee is null!");
                Platform.runLater(() -> {
                    try {
                        if (welcomeLabel != null) {
                            welcomeLabel.setText("Welcome, Officer");
                        }
                    } catch (Exception e) {
                        System.err.println("Error setting default welcome message: " + e.getMessage());
                    }
                });
            }

            System.out.println("=== End setCurrentEmployee ===");
        } catch (Exception e) {
            System.err.println("Error setting current employee: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Updates the welcome message with current employee's name.
     */
    private void updateWelcomeMessage() {
        try {
            System.out.println("=== updateWelcomeMessage called ===");
            System.out.println("currentEmployee: " + (currentEmployee != null ? "NOT NULL" : "NULL"));
            System.out.println("welcomeLabel: " + (welcomeLabel != null ? "NOT NULL" : "NULL"));

            if (currentEmployee != null && welcomeLabel != null) {
                String employeeName = currentEmployee.getName();
                System.out.println("Employee Name from object: " + employeeName);

                if (employeeName != null && !employeeName.trim().isEmpty()) {
                    final String welcomeText = "Welcome, Officer " + employeeName;
                    System.out.println("Setting welcome text to: " + welcomeText);

                    Platform.runLater(() -> {
                        try {
                            welcomeLabel.setText(welcomeText);
                            System.out.println("Welcome message updated successfully in UI thread");
                        } catch (Exception e) {
                            System.err.println("Error setting welcome text in UI thread: " + e.getMessage());
                        }
                    });
                } else {
                    System.err.println("Employee name is null or empty!");
                    Platform.runLater(() -> {
                        try {
                            welcomeLabel.setText("Welcome, Officer");
                        } catch (Exception e) {
                            System.err.println("Error setting default welcome text: " + e.getMessage());
                        }
                    });
                }
            } else {
                if (currentEmployee == null) {
                    System.err.println("currentEmployee is null in updateWelcomeMessage");
                }
                if (welcomeLabel == null) {
                    System.err.println("welcomeLabel is null in updateWelcomeMessage");
                }

                Platform.runLater(() -> {
                    try {
                        if (welcomeLabel != null) {
                            welcomeLabel.setText("Welcome, Officer");
                        }
                    } catch (Exception e) {
                        System.err.println("Error resetting welcome label: " + e.getMessage());
                    }
                });
            }

            System.out.println("=== End updateWelcomeMessage ===");
        } catch (Exception e) {
            System.err.println("Error in updateWelcomeMessage: " + e.getMessage());
            e.printStackTrace();
        }
    }


    /**
     * Updates dashboard data specific to the current employee.
     */
    private void updateEmployeeSpecificData() {
        if (currentEmployee == null) {
            System.err.println("Cannot update employee-specific data: employee is null");
            return;
        }

        CompletableFuture.runAsync(() -> {
            try {
                updateSupervisedPrisonersCount();
                updateEmployeeTasksCount();
                loadRecentSecurityAlerts();
                updateActiveAlertsCount();

                System.out.println("Employee-specific data updated for " + currentEmployee.getName());
            } catch (Exception e) {
                handleEmployeeDataError(e);
            }
        }, executorService);
    }

    /**
     * Updates supervised prisoners count with caching.
     */
    private void updateSupervisedPrisonersCount() {
        try {
            int prisonerCount;

            if (isPrisonerCacheValid()) {
                prisonerCount = cachedPrisoners.size();
                System.out.println("Using cached prisoner data");
            } else {
                List<Prisoner> supervisedPrisoners = prisonerService.getAllPrisoners();
                prisonerCount = supervisedPrisoners != null ? supervisedPrisoners.size() : 0;

                cachedPrisoners = supervisedPrisoners;
                prisonersCacheTimestamp = System.currentTimeMillis();
            }

            final String displayText = prisonerCount + (prisonerCount == 1 ? " prisoner" : " prisoners");

            Platform.runLater(() -> {
                try {
                    if (supervisedPrisonersLabel != null) {
                        supervisedPrisonersLabel.setText(displayText);
                    }
                } catch (Exception e) {
                    System.err.println("Error updating supervised prisoners label: " + e.getMessage());
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            System.err.println("Error updating supervised prisoners: " + e.getMessage());
            e.printStackTrace();
            Platform.runLater(() -> {
                try {
                    if (supervisedPrisonersLabel != null) {
                        supervisedPrisonersLabel.setText(ERROR_TEXT);
                    }
                } catch (Exception ex) {
                    System.err.println("Error setting error text for prisoners: " + ex.getMessage());
                    ex.printStackTrace();
                }
            });
        }
    }

    /**
     * Checks if the prisoner cache is still valid.
     *
     * @return true if cache is valid, false otherwise
     */
    private boolean isPrisonerCacheValid() {
        if (cachedPrisoners == null) {
            return false;
        }

        long currentTime = System.currentTimeMillis();
        return (currentTime - prisonersCacheTimestamp) < CACHE_TTL_MS;
    }

    /**
     * Updates employee tasks count.
     */
    private void updateEmployeeTasksCount() {
        try {
            int taskCount = getEmployeeTaskCount();
            System.out.println("Task count for " + currentEmployee.getName() + ": " + taskCount);
        } catch (Exception e) {
            System.err.println("Error updating task count: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Gets the task count for the current employee.
     *
     * @return Number of tasks assigned to the employee
     */
    private int getEmployeeTaskCount() {
        if (currentEmployee == null || currentEmployee.getId() == null) {
            return 0;
        }

        try {
            List<Task> tasks = taskService.getTasksByOfficer(currentEmployee.getId());
            return tasks != null ? tasks.size() : 0;
        } catch (Exception e) {
            System.err.println("Error getting task count: " + e.getMessage());
            return 0;
        }
    }

    /**
     * Updates active alerts count.
     */
    private void updateActiveAlertsCount() {
        try {
            List<SecurityAlert> activeAlerts = securityAlertService.getActiveSecurityAlerts();
            int activeCount = activeAlerts != null ? activeAlerts.size() : 0;

            Platform.runLater(() -> {
                try {
                    if (activeAlertsLabel != null) {
                        activeAlertsLabel.setText(activeCount + (activeCount == 1 ? " active alert" : " active alerts"));
                    }
                } catch (Exception e) {
                    System.err.println("Error updating active alerts label: " + e.getMessage());
                    e.printStackTrace();
                }
            });

            if (recentAlertsLabel != null) {
                List<SecurityAlert> recentAlerts = securityAlertService.getRecentSecurityAlerts(24);
                int recentCount = recentAlerts != null ? recentAlerts.size() : 0;

                Platform.runLater(() -> {
                    try {
                        if (recentAlertsLabel != null) {
                            recentAlertsLabel.setText(recentCount + " recent (24h)");
                        }
                    } catch (Exception e) {
                        System.err.println("Error updating recent alerts label: " + e.getMessage());
                        e.printStackTrace();
                    }
                });
            }
        } catch (Exception e) {
            System.err.println("Error updating active alerts: " + e.getMessage());
            e.printStackTrace();
            Platform.runLater(() -> {
                try {
                    if (activeAlertsLabel != null) {
                        activeAlertsLabel.setText(ERROR_TEXT);
                    }
                    if (recentAlertsLabel != null) {
                        recentAlertsLabel.setText(ERROR_TEXT);
                    }
                } catch (Exception ex) {
                    System.err.println("Error setting error text for alerts: " + ex.getMessage());
                    ex.printStackTrace();
                }
            });
        }
    }

    /**
     * Loads and displays recent security alerts.
     */
    private void loadRecentSecurityAlerts() {
        try {
            List<SecurityAlert> recentAlerts = securityAlertService.getRecentSecurityAlerts(24);
            System.out.println("Loaded " + (recentAlerts != null ? recentAlerts.size() : 0) + " recent alerts");
        } catch (Exception e) {
            System.err.println("Error loading recent alerts: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // FXML Action Handlers
    /**
     * Handles open my tasks action.
     */
    @FXML
    private void openMyTasks() {
        try {
            CompletableFuture.runAsync(() -> {
                Platform.runLater(() -> {
                    try {
                        openWindow("/view/OfficerTasksView.fxml",
                                "My Tasks - Officer",
                                "/images/tasks.png");
                    } catch (Exception e) {
                        System.err.println("Error opening my tasks: " + e.getMessage());
                        e.printStackTrace();
                    }
                });
            }, executorService);
        } catch (Exception e) {
            System.err.println("Error in openMyTasks: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handles open daily reports action.
     */
    @FXML
    private void openDailyReports() {
        try {
            CompletableFuture.runAsync(() -> {
                Platform.runLater(() -> {
                    try {
                        openWindow("/view/DailyReportForm.fxml", "Create Daily Report - Officer", "/images/reports.png");
                    } catch (Exception e) {
                        System.err.println("Error opening daily reports: " + e.getMessage());
                        e.printStackTrace();
                    }
                });
            }, executorService);
        } catch (Exception e) {
            System.err.println("Error in openDailyReports: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handles open guard duties action.
     */
    @FXML
    private void openGuardDuties() {
        try {
            CompletableFuture.runAsync(() -> {
                Platform.runLater(() -> {
                    try {
                        openWindow("/view/GuardDutiesView.fxml",
                                "Guard Duties Management",
                                "/images/guardDuties.png");
                    } catch (Exception e) {
                        System.err.println("Error opening guard duties: " + e.getMessage());
                        e.printStackTrace();
                    }
                });
            }, executorService);
        } catch (Exception e) {
            System.err.println("Error in openGuardDuties: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handles open alerts action.
     */
    @FXML
    private void openAlerts() {
        try {
            CompletableFuture.runAsync(() -> {
                Platform.runLater(() -> {
                    try {
                        openWindow("/view/OfficerAlertsView.fxml", "Security Alerts - Officer View", "/images/alert.png");
                    } catch (Exception e) {
                        System.err.println("Error opening alerts: " + e.getMessage());
                        e.printStackTrace();
                        showBasicAlertsInfo();
                    }
                });
            }, executorService);
        } catch (Exception e) {
            System.err.println("Error in openAlerts: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handles open prisoner management action.
     */
    @FXML
    private void openPrisonerManagement() {
        try {
            CompletableFuture.runAsync(() -> {
                Platform.runLater(() -> {
                    try {
                        openWindow("/view/PrisonerManagement.fxml", "Prisoner Management - Officer", "/images/prisoners.png");
                    } catch (Exception e) {
                        System.err.println("Error opening prisoner management: " + e.getMessage());
                        e.printStackTrace();
                    }
                });
            }, executorService);
        } catch (Exception e) {
            System.err.println("Error in openPrisonerManagement: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handles open visit management action.
     */
    @FXML
    private void openVisitManagement() {
        try {
            CompletableFuture.runAsync(() -> {
                Platform.runLater(() -> {
                    try {
                        openWindow("/view/VisitManagement.fxml", "Visit Management", "/images/visits.png");
                    } catch (Exception e) {
                        System.err.println("Error opening visit management: " + e.getMessage());
                        e.printStackTrace();
                    }
                });
            }, executorService);
        } catch (Exception e) {
            System.err.println("Error in openVisitManagement: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handles open prison cell monitoring action.
     */
    @FXML
    private void openPrisonCellMonitoring() {
        try {
            CompletableFuture.runAsync(() -> {
                Platform.runLater(() -> {
                    try {
                        openWindow("/view/CellViewOnly.fxml", "Cell Monitoring", "/images/cell.png");
                    } catch (Exception e) {
                        System.err.println("Error opening prison cell monitoring: " + e.getMessage());
                        e.printStackTrace();
                    }
                });
            }, executorService);
        } catch (Exception e) {
            System.err.println("Error in openPrisonCellMonitoring: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handles refresh dashboard action.
     */
    @FXML
    private void refreshDashboard() {
        try {
            showAlert("Refreshing", "Refreshing dashboard data...", Alert.AlertType.INFORMATION);

            cachedPrisoners = null;
            prisonersCacheTimestamp = 0;

            showLoadingState();

            loadDashboardDataAsync();
        } catch (Exception e) {
            System.err.println("Error in refreshDashboard: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handles logout action.
     */
    @FXML
    private void logout() {
        showLogoutConfirmation();
    }

    // Window Management Methods
    /**
     * Opens a new window with specified FXML, title, and icon.
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

            String iconName = getIconNameFromPath(iconPath);
            if (!loadIconFromResource(stage, "/images/" + iconName + ".png")) {
                loadIconFromResource(stage, "images/" + iconName + ".png");
            }

            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            System.err.println("Error opening " + title + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Sets employee in controller if supported.
     *
     * @param controller The controller to set employee in
     * @param employee The employee to set
     */
    private void setEmployeeInController(Object controller, Employee employee) {
        try {
            if (controller instanceof AdminDashboardController) {
                ((AdminDashboardController) controller).setCurrentEmployee(employee);
            } else if (controller instanceof OfficerDashboardController) {
                ((OfficerDashboardController) controller).setCurrentEmployee(employee);
            } else if (controller instanceof OfficerTasksViewController) {
                ((OfficerTasksViewController) controller).setCurrentEmployee(employee);
            } else if (controller instanceof DailyReportController) {
                ((DailyReportController) controller).setCurrentEmployee(employee);
            } else if (controller instanceof GuardDutiesController) {
                ((GuardDutiesController) controller).setCurrentEmployee(employee);
            } else if (controller instanceof OfficerAlertsViewController) {
                ((OfficerAlertsViewController) controller).setCurrentEmployee(employee);
            }
        } catch (Exception e) {
            System.err.println("Error setting employee in controller: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Extracts icon name from a path.
     *
     * @param path The full path (e.g., "/images/login.png")
     * @return The icon name (e.g., "login")
     */
    private String getIconNameFromPath(String path) {
        try {
            if (path == null || path.isEmpty()) {
                return "officer";
            }

            String fileName = path.substring(path.lastIndexOf('/') + 1);
            int dotIndex = fileName.lastIndexOf('.');
            if (dotIndex > 0) {
                return fileName.substring(0, dotIndex);
            }
            return fileName;
        } catch (Exception e) {
            System.err.println("Error extracting icon name from path: " + e.getMessage());
            e.printStackTrace();
            return "officer";
        }
    }

    /**
     * Validates that current employee is not null.
     *
     * @return true if current employee is valid, false otherwise
     */
    private boolean validateCurrentEmployee() {
        try {
            if (currentEmployee == null) {
                Platform.runLater(() -> {
                    try {
                        showAlert("Error", "No employee session found. Please login again.", Alert.AlertType.ERROR);
                    } catch (Exception e) {
                        System.err.println("Error showing validation alert: " + e.getMessage());
                        e.printStackTrace();
                    }
                });
                return false;
            }
            return true;
        } catch (Exception e) {
            System.err.println("Error validating current employee: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Shows basic alerts information when detailed view cannot be opened.
     */
    private void showBasicAlertsInfo() {
        try {
            List<SecurityAlert> activeAlerts = securityAlertService.getActiveSecurityAlerts();
            List<SecurityAlert> recentAlerts = securityAlertService.getRecentSecurityAlerts(24);

            StringBuilder content = new StringBuilder();
            content.append("Active Alerts: ").append(activeAlerts != null ? activeAlerts.size() : 0).append("\n");
            content.append("Recent Alerts (24h): ").append(recentAlerts != null ? recentAlerts.size() : 0).append("\n\n");

            if (activeAlerts != null && !activeAlerts.isEmpty()) {
                content.append("--- Active Alerts ---\n");
                for (int i = 0; i < Math.min(5, activeAlerts.size()); i++) {
                    SecurityAlert alert = activeAlerts.get(i);
                    content.append("• ").append(alert.getAlertType())
                            .append(" (").append(alert.getSeverity()).append(")")
                            .append(" - ").append(alert.getLocation())
                            .append("\n");
                }
            }

            Platform.runLater(() -> {
                try {
                    showAlert("Security Alerts", "Current Security Status\n\n" + content.toString(), Alert.AlertType.INFORMATION);
                } catch (Exception e) {
                    System.err.println("Error showing basic alerts info: " + e.getMessage());
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            Platform.runLater(() -> {
                try {
                    showAlert("Error", "Cannot Load Alerts\n\nUnable to load security alerts: " + e.getMessage(), Alert.AlertType.ERROR);
                } catch (Exception ex) {
                    System.err.println("Error showing alerts error: " + ex.getMessage());
                    ex.printStackTrace();
                }
            });
        }
    }

    /**
     * Shows an alert dialog with specified parameters.
     *
     * @param title The alert title
     * @param message The alert message
     * @param alertType The type of alert
     */
    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Platform.runLater(() -> {
            try {
                Alert alert = new Alert(alertType);
                alert.setTitle(title);
                alert.setHeaderText(null);
                alert.setContentText(message);

                if (welcomeLabel != null && welcomeLabel.getScene() != null) {
                    Stage currentStage = (Stage) welcomeLabel.getScene().getWindow();
                    alert.initOwner(currentStage);
                }

                alert.showAndWait();
            } catch (Exception e) {
                System.err.println("Error showing alert: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    /**
     * Handles initialization errors.
     *
     * @param e The exception that occurred
     */
    private void handleInitializationError(Exception e) {
        System.err.println("Error initializing dashboard: " + e.getMessage());
        e.printStackTrace();
        Platform.runLater(() -> {
            try {
                showAlert("Initialization Error", "Failed to initialize dashboard: " + e.getMessage(), Alert.AlertType.ERROR);
            } catch (Exception ex) {
                System.err.println("Error showing initialization error: " + ex.getMessage());
                ex.printStackTrace();
            }
        });
    }

    /**
     * Handles employee data errors.
     *
     * @param e The exception that occurred
     */
    private void handleEmployeeDataError(Exception e) {
        System.err.println("Error updating employee-specific data: " + e.getMessage());
        e.printStackTrace();
    }

    /**
     * Handles window open errors.
     *
     * @param windowName The name of the window that failed to open
     * @param e The exception that occurred
     */
    private void handleWindowOpenError(String windowName, Exception e) {
        System.err.println("Error opening " + windowName + ": " + e.getMessage());
        e.printStackTrace();

        Platform.runLater(() -> {
            try {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Cannot Open " + windowName.substring(0, 1).toUpperCase() + windowName.substring(1));
                alert.setContentText("An error occurred while opening the window. Please try again.");

                if (welcomeLabel != null && welcomeLabel.getScene() != null) {
                    Stage currentStage = (Stage) welcomeLabel.getScene().getWindow();
                    alert.initOwner(currentStage);
                }

                alert.showAndWait();
            } catch (Exception ex) {
                System.err.println("Error showing window open error: " + ex.getMessage());
                ex.printStackTrace();
            }
        });
    }

    /**
     * Configuration class for window settings.
     */
    private static class WindowConfig {

        private final String fxmlPath;
        private final String title;
        private final String iconPath;

        public WindowConfig(String fxmlPath, String title, String iconPath) {
            this.fxmlPath = fxmlPath;
            this.title = title;
            this.iconPath = iconPath;
        }

        public String getFxmlPath() {
            return fxmlPath;
        }

        public String getTitle() {
            return title;
        }

        public String getIconPath() {
            return iconPath;
        }
    }

    /**
     * Configuration class for controller settings.
     */
    private static class WindowControllerConfig {

        private final Class<?> targetClass;
        private final Employee employee;

        public WindowControllerConfig(Class<?> targetClass, Employee employee) {
            this.targetClass = targetClass;
            this.employee = employee;
        }

        public Class<?> getTargetClass() {
            return targetClass;
        }

        public Employee getEmployee() {
            return employee;
        }
    }
}
