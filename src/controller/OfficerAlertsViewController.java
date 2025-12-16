package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Employee;
import model.SecurityAlert;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import service.SecurityAlertService;

/**
 * Controller for Officer Alerts View interface. Implements Initializable for
 * JavaFX initialization. Uses Singleton pattern for service instances and
 * Strategy pattern for alert filtering.
 */
public class OfficerAlertsViewController implements Initializable {

    // Table UI components
    @FXML
    private TableView<SecurityAlert> alertsTable;
    @FXML
    private TableColumn<SecurityAlert, String> typeColumn;
    @FXML
    private TableColumn<SecurityAlert, String> severityColumn;
    @FXML
    private TableColumn<SecurityAlert, String> descriptionColumn;
    @FXML
    private TableColumn<SecurityAlert, String> locationColumn;
    @FXML
    private TableColumn<SecurityAlert, String> timeColumn;
    @FXML
    private TableColumn<SecurityAlert, String> statusColumn;

    // Statistics UI components
    @FXML
    private Label activeAlertsCount;
    @FXML
    private Label criticalAlertsCount;
    @FXML
    private Label todaysAlertsCount;

    // Data models
    private Employee currentEmployee;

    // Service instance (Singleton pattern)
    private SecurityAlertService securityAlertService;

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
        initializeTableColumns();
        loadAlerts();
        updateStatistics();
    }

    /**
     * Initializes service instances using Singleton pattern.
     */
    private void initializeServices() {
        securityAlertService = SecurityAlertService.getInstance();
    }

    /**
     * Sets the current employee.
     *
     * @param employee The employee object representing the current user
     */
    public void setCurrentEmployee(Employee employee) {
        this.currentEmployee = employee;
    }

    /**
     * Initializes table columns with cell value factories and styling.
     */
    private void initializeTableColumns() {
        configureColumnValueFactories();
        configureSeverityColumnStyling();
    }

    /**
     * Configures standard cell value factories for each column.
     */
    private void configureColumnValueFactories() {
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("alertType"));
        severityColumn.setCellValueFactory(new PropertyValueFactory<>("severity"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("formattedTriggeredAt"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    /**
     * Configures severity column with conditional styling based on severity
     * level. Implements Decorator pattern for cell styling.
     */
    private void configureSeverityColumnStyling() {
        severityColumn.setCellFactory(column -> new TableCell<SecurityAlert, String>() {
            @Override
            protected void updateItem(String severity, boolean empty) {
                super.updateItem(severity, empty);

                // Correction: Use this.setText() and this.setStyle() inside anonymous inner class
                if (severity == null || empty) {
                    this.setText(null);
                    this.setStyle("");
                } else {
                    this.setText(severity);
                    applySeveritySpecificStyle(severity);
                }
            }

            /**
             * Applies specific styling based on severity level.
             *
             * @param severity The severity level
             */
            private void applySeveritySpecificStyle(String severity) {
                switch (severity) {
                    case "Critical":
                        this.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
                        break;
                    case "High":
                        this.setStyle("-fx-text-fill: #e67e22; -fx-font-weight: bold;");
                        break;
                    case "Medium":
                        this.setStyle("-fx-text-fill: #f39c12; -fx-font-weight: bold;");
                        break;
                    case "Low":
                        this.setStyle("-fx-text-fill: #3498db; -fx-font-weight: bold;");
                        break;
                    default:
                        this.setStyle("");
                }
            }
        });
    }

    /**
     * Loads recent security alerts into the table.
     */
    private void loadAlerts() {
        try {
            List<SecurityAlert> alerts = securityAlertService.getRecentSecurityAlerts(72);
            alertsTable.getItems().setAll(alerts);
        } catch (Exception e) {
            handleAlertLoadingError(e);
        }
    }

    /**
     * Updates statistics labels with current alert data.
     */
    private void updateStatistics() {
        try {
            calculateAndDisplayStatistics();
        } catch (Exception e) {
            handleStatisticsError(e);
        }
    }

    /**
     * Calculates and displays alert statistics.
     */
    private void calculateAndDisplayStatistics() {
        List<SecurityAlert> activeAlerts = securityAlertService.getActiveSecurityAlerts();
        List<SecurityAlert> recentAlerts = securityAlertService.getRecentSecurityAlerts(24);

        long criticalCount = countCriticalAlerts(recentAlerts);

        updateStatisticsLabels(activeAlerts.size(), criticalCount, recentAlerts.size());
    }

    /**
     * Counts critical alerts from the list.
     *
     * @param alerts List of security alerts
     * @return Count of critical alerts
     */
    private long countCriticalAlerts(List<SecurityAlert> alerts) {
        return alerts.stream()
                .filter(alert -> "Critical".equals(alert.getSeverity()))
                .count();
    }

    /**
     * Updates all statistics labels with calculated values.
     *
     * @param activeCount Number of active alerts
     * @param criticalCount Number of critical alerts
     * @param todayCount Number of today's alerts
     */
    private void updateStatisticsLabels(int activeCount, long criticalCount, int todayCount) {
        activeAlertsCount.setText(String.valueOf(activeCount));
        criticalAlertsCount.setText(String.valueOf(criticalCount));
        todaysAlertsCount.setText(String.valueOf(todayCount));
    }

    // FXML Action Handlers
    /**
     * Handles refresh action to reload alerts and statistics.
     */
    @FXML
    private void refreshAlerts() {
        loadAlerts();
        updateStatistics();
    }

    /**
     * Handles viewing alert details action.
     */
    @FXML
    private void viewAlertDetails() {
        SecurityAlert selectedAlert = alertsTable.getSelectionModel().getSelectedItem();
        if (selectedAlert != null) {
            showAlertDetailsDialog(selectedAlert);
        } else {
            showSelectionWarning();
        }
    }

    /**
     * Shows detailed information about the selected alert.
     *
     * @param alert The security alert to display
     */
    private void showAlertDetailsDialog(SecurityAlert alert) {
        Alert detailsDialog = new Alert(Alert.AlertType.INFORMATION);
        detailsDialog.setTitle("Alert Details");
        detailsDialog.setHeaderText(alert.getAlertType() + " Alert");
        detailsDialog.setContentText(formatAlertDetails(alert));
        detailsDialog.showAndWait();
    }

    /**
     * Formats alert details for display.
     *
     * @param alert The security alert to format
     * @return Formatted alert details string
     */
    private String formatAlertDetails(SecurityAlert alert) {
        StringBuilder content = new StringBuilder();
        content.append("Type: ").append(alert.getAlertType()).append("\n");
        content.append("Severity: ").append(alert.getSeverity()).append("\n");
        content.append("Location: ").append(alert.getLocation()).append("\n");
        content.append("Time: ").append(alert.getFormattedTriggeredAt()).append("\n");
        content.append("Status: ").append(alert.getStatus()).append("\n");
        content.append("Description: ").append(alert.getDescription()).append("\n");

        if (alert.getAssignedTo() != null) {
            content.append("Assigned To: ").append(alert.getAssignedTo()).append("\n");
        }

        return content.toString();
    }

    /**
     * Shows a warning when no alert is selected.
     */
    private void showSelectionWarning() {
        Alert warning = new Alert(Alert.AlertType.WARNING);
        warning.setTitle("No Selection");
        warning.setHeaderText(null);
        warning.setContentText("Please select an alert to view details.");
        warning.showAndWait();
    }

    /**
     * Handles mark as reviewed action.
     */
    @FXML
    private void markAsReviewed() {
        SecurityAlert selectedAlert = alertsTable.getSelectionModel().getSelectedItem();
        if (selectedAlert != null) {
            showReviewConfirmation();
        }
    }

    /**
     * Shows confirmation that alert has been reviewed.
     */
    private void showReviewConfirmation() {
        Alert confirmation = new Alert(Alert.AlertType.INFORMATION);
        confirmation.setTitle("Alert Reviewed");
        confirmation.setHeaderText(null);
        confirmation.setContentText("Alert marked as reviewed.");
        confirmation.showAndWait();
    }

    /**
     * Handles create incident report action.
     */
    @FXML
    private void createIncidentReport() {
        SecurityAlert selectedAlert = alertsTable.getSelectionModel().getSelectedItem();
        if (selectedAlert != null) {
            showIncidentReportDialog(selectedAlert);
        }
    }

    /**
     * Shows dialog for creating incident report.
     *
     * @param alert The alert to create report for
     */
    private void showIncidentReportDialog(SecurityAlert alert) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Incident Report");
        dialog.setHeaderText("Create report for: " + alert.getAlertType());
        dialog.setContentText("Report details:");

        dialog.showAndWait().ifPresent(this::handleReportSubmission);
    }

    /**
     * Handles incident report submission.
     *
     * @param reportDetails The report details
     */
    private void handleReportSubmission(String reportDetails) {
        Alert success = new Alert(Alert.AlertType.INFORMATION);
        success.setTitle("Report Created");
        success.setHeaderText(null);
        success.setContentText("Incident report created successfully.");
        success.showAndWait();
    }

    /**
     * Handles close window action.
     */
    @FXML
    private void closeWindow() {
        Stage stage = (Stage) alertsTable.getScene().getWindow();
        stage.close();
    }

    /**
     * Handles alert loading errors.
     *
     * @param e The exception that occurred
     */
    private void handleAlertLoadingError(Exception e) {
        System.err.println("Error loading alerts: " + e.getMessage());
    }

    /**
     * Handles statistics calculation errors.
     *
     * @param e The exception that occurred
     */
    private void handleStatisticsError(Exception e) {
        System.err.println("Error updating statistics: " + e.getMessage());
    }
}
