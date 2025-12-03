package controller;

import database.AuthService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.Employee;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for login interface
 */
public class LoginController implements Initializable {

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
    private ImageView logoImage;
    @FXML
    private Button showPasswordBtn;

    // Track password visibility state
    private boolean isPasswordVisible = false;

    private AuthService authService;

    /**
     * Initialize the controller
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        authService = new AuthService();
        setupUI();
        loadSavedCredentials();
    }

    /**
     * Setup UI components
     */
    private void setupUI() {
        // Set logo image (you need to add a logo image in resources)
        try {
            Image logo = new Image(getClass().getResourceAsStream("/images/logo.png"));
            logoImage.setImage(logo);
        } catch (Exception e) {
            System.err.println("Logo image not found");
        }

        // Set enter key handler for login
        passwordField.setOnAction(e -> handleLogin());
        visiblePasswordField.setOnAction(e -> handleLogin());

        // Setup text synchronization between password fields
        setupPasswordSync();
    }

    /**
     * Setup text synchronization between hidden and visible password fields
     */
    private void setupPasswordSync() {
        // When password field text changes, update visible field
        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            visiblePasswordField.setText(newValue);
        });

        // When visible field text changes, update password field
        visiblePasswordField.textProperty().addListener((observable, oldValue, newValue) -> {
            passwordField.setText(newValue);
        });
    }

    /**
     * Load saved credentials if remember me was checked
     */
    private void loadSavedCredentials() {
        // In a real application, you would load from secure storage
        // For demo purposes, we'll leave this empty
    }

    /**
     * Handle login button click
     */
    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText(); // Always get from hidden field

        if (validateInput(username, password)) {
            Employee employee = authService.authenticate(username, password);

            if (employee != null) {
                loginSuccessful(employee);
            } else {
                showMessage("Invalid username or password", true);
            }
        }
    }

    /**
     * Validate login input
     *
     * @param username Username
     * @param password Password
     * @return true if input is valid
     */
    private boolean validateInput(String username, String password) {
        if (username.isEmpty()) {
            showMessage("Please enter username", true);
            usernameField.requestFocus();
            return false;
        }

        if (password.isEmpty()) {
            showMessage("Please enter password", true);
            if (isPasswordVisible) {
                visiblePasswordField.requestFocus();
            } else {
                passwordField.requestFocus();
            }
            return false;
        }

        return true;
    }

    /**
     * Handle successful login
     *
     * @param employee Authenticated employee
     */
    private void loginSuccessful(Employee employee) {
        try {
            // Save credentials if remember me is checked
            if (rememberMeCheckbox.isSelected()) {
                saveCredentials(employee.getUsername());
            }

            // Open appropriate dashboard based on role
            openDashboard(employee);

            // Close login window
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.close();

        } catch (Exception e) {
            showMessage("Error opening dashboard: " + e.getMessage(), true);
        }
    }

    /**
     * Open dashboard based on employee role
     *
     * @param employee Authenticated employee
     */
    private void openDashboard(Employee employee) throws Exception {
        String fxmlFile;
        String title;

        if (employee.isAdministrator()) {
            fxmlFile = "/view/AdminDashboard.fxml";
            title = "Prison Management System - Administrator";
        } else {
            fxmlFile = "/view/OfficerDashboard.fxml";
            title = "Prison Management System - Officer";
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        Parent root = loader.load();

        // Pass employee data to dashboard controller
        Object controller = loader.getController();
        if (controller instanceof AdminDashboardController) {
            ((AdminDashboardController) controller).setCurrentEmployee(employee);
        } else if (controller instanceof OfficerDashboardController) {
            ((OfficerDashboardController) controller).setCurrentEmployee(employee);
        }

        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(new Scene(root, 1200, 800));
        stage.setMaximized(true);
        stage.show();
    }

    /**
     * Save credentials (basic implementation)
     *
     * @param username Username to save
     */
    private void saveCredentials(String username) {
        // In a real application, implement secure credential storage
        System.out.println("Credentials saved for: " + username);
    }

    /**
     * Show message to user
     *
     * @param message Message to display
     * @param isError true if error message
     */
    private void showMessage(String message, boolean isError) {
        messageLabel.setText(message);
        if (isError) {
            messageLabel.setStyle("-fx-text-fill: #e74c3c;");
        } else {
            messageLabel.setStyle("-fx-text-fill: #27ae60;");
        }
    }

    /**
     * Handle exit button
     */
    @FXML
    private void handleExit() {
        System.exit(0);
    }

    /**
     * Toggle password visibility between hidden and visible
     */
    @FXML
    private void togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible;

        if (isPasswordVisible) {
            // Show password in visible field
            passwordField.setVisible(false);
            visiblePasswordField.setVisible(true);
            showPasswordBtn.setText("Hide");
            visiblePasswordField.requestFocus();
            visiblePasswordField.end();
        } else {
            // Hide password, show password field
            visiblePasswordField.setVisible(false);
            passwordField.setVisible(true);
            showPasswordBtn.setText("Show");
            passwordField.requestFocus();
            passwordField.end();
        }
    }

    /**
     * Handle forgot password action Opens password recovery window or dialog
     */
    @FXML
    private void handleForgotPassword() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Password Recovery");
        alert.setHeaderText("Reset Your Password");
        alert.setContentText("Please contact system administrator for password reset.");
        alert.showAndWait();
    }
}
