package controller;

import java.io.InputStream;
import service.AuthService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import model.Employee;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the Login interface. Implements Initializable for JavaFX
 * initialization. Uses Singleton pattern for authentication service and Factory
 * pattern for dashboard creation.
 */
public class LoginController implements Initializable {

    // Login form UI components
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField visiblePasswordField;
    @FXML
    private CheckBox rememberMeCheckbox;
    @FXML
    private Button loginButton;
    @FXML
    private Label messageLabel;
    @FXML
    private Button showPasswordBtn;

    // Password visibility state
    private boolean isPasswordVisible = false;

    // Service instance (Singleton pattern)
    private AuthService authService;

    private ApplicationController appController;

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
        setupUI();
        loadSavedCredentials();
    }

    /**
     * Initializes service instances using Singleton pattern.
     */
    private void initializeServices() {
        authService = AuthService.getInstance();
    }

    /**
     * Sets up UI components and event handlers.
     */
    private void setupUI() {
        setupLoginActionHandlers();
        setupPasswordFieldSynchronization();
        setupEnterKeySupport();
    }

    public void setApplicationController(ApplicationController appController) {
        this.appController = appController;
        System.out.println("ApplicationController set in LoginController");
    }

    /**
     * Sets up action handlers for login functionality.
     */
    private void setupLoginActionHandlers() {
        // Login button click handler
        loginButton.setOnAction(event -> handleLogin());

        // Enter key handlers are in setupEnterKeySupport()
    }

    /**
     * Sets up Enter key support for login
     */
    private void setupEnterKeySupport() {
        // Press Enter in username field to move to password
        usernameField.setOnAction(event -> {
            if (isPasswordVisible) {
                visiblePasswordField.requestFocus();
            } else {
                passwordField.requestFocus();
            }
        });

        // Press Enter in password fields to login
        passwordField.setOnAction(event -> handleLogin());
        visiblePasswordField.setOnAction(event -> handleLogin());
    }

    /**
     * Sets up synchronization between hidden and visible password fields.
     * Implements Observer pattern for text change synchronization.
     */
    private void setupPasswordFieldSynchronization() {
        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!visiblePasswordField.isFocused()) {
                visiblePasswordField.setText(newValue);
            }
        });

        visiblePasswordField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!passwordField.isFocused()) {
                passwordField.setText(newValue);
            }
        });
    }

    /**
     * Loads saved credentials if "Remember Me" was previously checked.
     */
    private void loadSavedCredentials() {
        // TODO: Implement secure credential loading
        // For now, this is a placeholder
        // Example: load from encrypted preferences
    }

    // FXML Action Handlers
    /**
     * Handles login button click and Enter key press.
     */
    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        if (validateInput(username, password)) {
            authenticateUser(username, password);
        }
    }

    /**
     * Handles exit button click.
     */
    @FXML
    private void handleExit() {
        if (appController != null) {
            appController.handleApplicationExit();
        } else {
            // Fallback: show confirmation dialog directly
            showDirectExitConfirmation();
        }
    }

    /**
     * Shows direct exit confirmation when ApplicationController is not
     * available.
     */
    private void showDirectExitConfirmation() {
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Exit Application");
            alert.setHeaderText("Are you sure you want to exit?");
            alert.setContentText("The Prison Management System will be closed.");

            // Set dialog icon
            try {
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                InputStream iconStream = getClass().getResourceAsStream("/images/login.png");
                if (iconStream != null) {
                    Image icon = new Image(iconStream);
                    stage.getIcons().add(icon);
                }
            } catch (Exception e) {
                // Icon is optional
            }

            // Get the current stage for dialog positioning
            Stage currentStage = null;
            if (loginButton != null && loginButton.getScene() != null) {
                currentStage = (Stage) loginButton.getScene().getWindow();
                alert.initOwner(currentStage);
            }

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    System.exit(0);
                }
            });

        } catch (Exception e) {
            System.err.println("Error showing exit confirmation: " + e.getMessage());
            e.printStackTrace();
            // If everything fails, just exit
            System.exit(0);
        }
    }

    /**
     * Toggles password visibility between hidden and visible states.
     */
    @FXML
    private void togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible;
        updatePasswordFieldVisibility();
        updatePasswordToggleButton();
    }

    /**
     * Updates the password toggle button text and style
     */
    private void updatePasswordToggleButton() {
        if (isPasswordVisible) {
            showPasswordBtn.setText("Hide");
            showPasswordBtn.setStyle("-fx-background-color: #e0e0e0;");
        } else {
            showPasswordBtn.setText("Show");
            showPasswordBtn.setStyle("");
        }
    }

    /**
     * Handles forgot password action.
     */
    @FXML
    private void handleForgotPassword() {
        showPasswordRecoveryDialog();
    }

    /**
     * Validates login input fields.
     *
     * @param username The username to validate
     * @param password The password to validate
     * @return true if input is valid, false otherwise
     */
    private boolean validateInput(String username, String password) {
        // Clear previous messages
        clearMessage();

        if (username.isEmpty()) {
            showMessage("Please enter username", true);
            usernameField.requestFocus();
            return false;
        }

        if (password.isEmpty()) {
            showMessage("Please enter password", true);
            requestFocusOnPasswordField();
            return false;
        }

        // Validate username format (alphanumeric, 3-20 characters)
        if (!username.matches("^[a-zA-Z0-9]{3,20}$")) {
            showMessage("Username must be 3-20 alphanumeric characters", true);
            usernameField.requestFocus();
            return false;
        }

        return true;
    }

    /**
     * Clears the message label
     */
    private void clearMessage() {
        messageLabel.setText("");
        messageLabel.setStyle("");
    }

    /**
     * Requests focus on the appropriate password field based on visibility
     * state.
     */
    private void requestFocusOnPasswordField() {
        if (isPasswordVisible) {
            visiblePasswordField.requestFocus();
        } else {
            passwordField.requestFocus();
        }
    }

    /**
     * Authenticates user with provided credentials.
     *
     * @param username The username to authenticate
     * @param password The password to authenticate
     */
    private void authenticateUser(String username, String password) {
        // Disable login button during authentication
        loginButton.setDisable(true);
        showMessage("Authenticating...", false);

        // Run authentication in background thread
        new Thread(() -> {
            try {
                Employee employee = authService.authenticate(username, password);

                javafx.application.Platform.runLater(() -> {
                    if (employee != null) {
                        handleSuccessfulAuthentication(employee);
                    } else {
                        showMessage("Invalid username or password", true);
                        loginButton.setDisable(false);
                        requestFocusOnPasswordField();
                        // Clear password for security
                        passwordField.clear();
                        visiblePasswordField.clear();
                    }
                });
            } catch (Exception e) {
                javafx.application.Platform.runLater(() -> {
                    showMessage("Authentication error: " + e.getMessage(), true);
                    loginButton.setDisable(false);
                });
            }
        }).start();
    }

    /**
     * Handles successful user authentication.
     *
     * @param employee The authenticated employee
     */
    private void handleSuccessfulAuthentication(Employee employee) {
        try {
            System.out.println("=== Authentication Successful ===");
            System.out.println("Employee Name: " + employee.getName());
            System.out.println("Employee ID: " + employee.getId());
            System.out.println("=== End Auth Info ===");

            saveCredentialsIfNeeded(employee);

            if (appController != null) {
                // Pass the Employee object, not just the ID
                appController.navigateToDashboard(employee, employee.isAdministrator());
            } else {
                openUserDashboard(employee);
            }

            closeLoginWindow();
        } catch (Exception e) {
            handleDashboardOpenError(e);
            loginButton.setDisable(false);
        }
    }

    /**
     * Saves credentials if "Remember Me" is checked.
     *
     * @param employee The authenticated employee
     */
    private void saveCredentialsIfNeeded(Employee employee) {
        if (rememberMeCheckbox.isSelected()) {
            saveCredentials(employee.getUsername());
        }
    }

    /**
     * Saves user credentials (placeholder for secure implementation).
     *
     * @param username The username to save
     */
    private void saveCredentials(String username) {
        // TODO: Implement secure credential storage
        // Example: use Java Preferences API with encryption
    }

    /**
     * Opens the appropriate dashboard based on user role. Uses Factory pattern
     * for dashboard creation.
     *
     * @param employee The authenticated employee
     * @throws Exception If dashboard cannot be loaded
     */
    private void openUserDashboard(Employee employee) throws Exception {
        DashboardInfo dashboardInfo = getDashboardInfoForEmployee(employee);
        loadAndShowDashboard(dashboardInfo, employee);
    }

    /**
     * Gets dashboard information based on employee role.
     *
     * @param employee The authenticated employee
     * @return DashboardInfo containing FXML file and title
     */
    private DashboardInfo getDashboardInfoForEmployee(Employee employee) {
        if (employee.isAdministrator()) {
            return new DashboardInfo("/view/AdminDashboard.fxml",
                    "Prison Management System - Administrator Dashboard");
        } else {
            return new DashboardInfo("/view/OfficerDashboard.fxml",
                    "Prison Management System - Officer Dashboard");
        }
    }

    /**
     * Loads and displays the dashboard.
     *
     * @param dashboardInfo Dashboard configuration information
     * @param employee The authenticated employee
     * @throws Exception If dashboard cannot be loaded
     */
    private void loadAndShowDashboard(DashboardInfo dashboardInfo, Employee employee) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(dashboardInfo.getFxmlFile()));
        Parent root = loader.load();

        setEmployeeInDashboardController(loader.getController(), employee);

        Stage stage = createDashboardStage(dashboardInfo.getTitle(), root);

        // Set application icon for dashboard
        setStageIcon(stage);

        stage.show();
    }

    /**
     * Sets application icon on stage - THIS IS WHERE THE FIX IS NEEDED
     */
    private void setStageIcon(Stage stage) {
        try {
            // CORRECTED: Use consistent icon path
            InputStream iconStream = getClass().getResourceAsStream("/images/login.png");

            // Fallback to login icon if app icon not found
            if (iconStream == null) {
                iconStream = getClass().getResourceAsStream("/images/login.png");
            }

            if (iconStream != null) {
                Image icon = new Image(iconStream);
                stage.getIcons().add(icon);
            }
        } catch (Exception e) {
            // Icon is optional, continue without it
            System.err.println("Warning: Could not load application icon: " + e.getMessage());
        }
    }

    /**
     * Sets the current employee in the dashboard controller.
     *
     * @param controller The dashboard controller
     * @param employee The authenticated employee
     */
    private void setEmployeeInDashboardController(Object controller, Employee employee) {
        if (controller instanceof AdminDashboardController) {
            ((AdminDashboardController) controller).setCurrentEmployee(employee);
        } else if (controller instanceof OfficerDashboardController) {
            ((OfficerDashboardController) controller).setCurrentEmployee(employee);
        }
    }

    /**
     * Creates a stage for the dashboard.
     *
     * @param title The window title
     * @param root The root node of the scene
     * @return Configured Stage object
     */
    private Stage createDashboardStage(String title, Parent root) {
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(new Scene(root));
        stage.setMaximized(true);
        return stage;
    }

    /**
     * Handles errors when opening dashboard.
     *
     * @param e The exception that occurred
     */
    private void handleDashboardOpenError(Exception e) {
        showMessage("Error opening dashboard: " + e.getMessage(), true);
        // Log the full exception for debugging
        e.printStackTrace();
    }

    /**
     * Closes the login window.
     */
    private void closeLoginWindow() {
        Stage stage = (Stage) loginButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Updates password field visibility based on current state.
     */
    private void updatePasswordFieldVisibility() {
        if (isPasswordVisible) {
            showPasswordField();
        } else {
            hidePasswordField();
        }
    }

    /**
     * Shows password in visible text field.
     */
    private void showPasswordField() {
        passwordField.setVisible(false);
        visiblePasswordField.setVisible(true);
        visiblePasswordField.requestFocus();
        visiblePasswordField.end();
    }

    /**
     * Hides password in password field.
     */
    private void hidePasswordField() {
        visiblePasswordField.setVisible(false);
        passwordField.setVisible(true);
        passwordField.requestFocus();
        passwordField.end();
    }

    /**
     * Shows password recovery dialog.
     */
    private void showPasswordRecoveryDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Password Recovery");
        alert.setHeaderText("Reset Your Password");
        alert.setContentText("Please contact your system administrator for password reset.\n\n"
                + "Administrator Email: admin@prison-management.local\n"
                + "Help Desk: ext. 5555");

        // Set dialog icon with proper path
        try {
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();

            // CORRECTED: Use consistent icon path
            InputStream iconStream = getClass().getResourceAsStream("/images/login.png");

            // Fallback to login icon if app icon not found
            if (iconStream == null) {
                iconStream = getClass().getResourceAsStream("/images/login.png");
            }

            if (iconStream != null) {
                Image icon = new Image(iconStream);
                stage.getIcons().add(icon);
            }
        } catch (Exception e) {
            // Icon is optional
            System.err.println("Warning: Could not load dialog icon: " + e.getMessage());
        }

        alert.showAndWait();
    }

    /**
     * Shows a message to the user.
     *
     * @param message The message to display
     * @param isError true if message is an error, false if success
     */
    private void showMessage(String message, boolean isError) {
        messageLabel.setText(message);
        if (isError) {
            messageLabel.setStyle("-fx-text-fill: #d32f2f; -fx-font-weight: bold;");
        } else {
            messageLabel.setStyle("-fx-text-fill: #388e3c; -fx-font-weight: bold;");
        }
    }

    /**
     * Helper class to encapsulate dashboard information. Uses Factory pattern
     * for dashboard configuration.
     */
    private static class DashboardInfo {

        private final String fxmlFile;
        private final String title;

        /**
         * Creates a new DashboardInfo instance.
         *
         * @param fxmlFile The FXML file path for the dashboard
         * @param title The window title for the dashboard
         */
        public DashboardInfo(String fxmlFile, String title) {
            this.fxmlFile = fxmlFile;
            this.title = title;
        }

        /**
         * Gets the FXML file path.
         *
         * @return The FXML file path
         */
        public String getFxmlFile() {
            return fxmlFile;
        }

        /**
         * Gets the window title.
         *
         * @return The window title
         */
        public String getTitle() {
            return title;
        }
    }
}
