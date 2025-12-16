package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.DailyReport;
import model.Employee;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import service.DailyReportService;

/**
 * Controller for Daily Report management interface. Implements Initializable
 * for JavaFX initialization. Uses Singleton pattern for service instances and
 * Factory pattern for report creation.
 */
public class DailyReportController implements Initializable {

    // UI components for officer information
    @FXML
    private Label officerNameLabel;
    @FXML
    private Label currentDateLabel;
    @FXML
    private Label shiftLabel;

    // UI components for report details
    @FXML
    private TextField reportIdField;
    @FXML
    private ComboBox<String> reportTypeComboBox;
    @FXML
    private ComboBox<String> priorityComboBox;
    @FXML
    private DatePicker reportDatePicker;

    // UI components for report content
    @FXML
    private TextArea incidentsArea;
    @FXML
    private TextArea actionsArea;
    @FXML
    private TextArea notesArea;

    // Data models
    private Employee currentEmployee;

    // Service instance (Singleton pattern)
    private DailyReportService reportService;

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
        initializeForm();
        generateReportId();
    }

    /**
     * Initializes service instances using Singleton pattern.
     */
    private void initializeServices() {
        reportService = DailyReportService.getInstance();
    }

    /**
     * Initializes form components with default values.
     */
    private void initializeForm() {
        setCurrentDate();
        initializeComboBoxes();
        setDefaultShift();
    }

    /**
     * Sets current date in labels and date picker.
     */
    private void setCurrentDate() {
        LocalDate today = LocalDate.now();
        currentDateLabel.setText(today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        reportDatePicker.setValue(today);
    }

    /**
     * Initializes combo boxes with predefined options.
     */
    private void initializeComboBoxes() {
        initializeReportTypeComboBox();
        initializePriorityComboBox();
    }

    /**
     * Initializes report type combo box.
     */
    private void initializeReportTypeComboBox() {
        reportTypeComboBox.getItems().addAll(
                "Daily Security Report",
                "Shift Change Report",
                "Incident Report",
                "Weekly Summary"
        );
        reportTypeComboBox.setValue("Daily Security Report");
    }

    /**
     * Initializes priority combo box.
     */
    private void initializePriorityComboBox() {
        priorityComboBox.getItems().addAll("Low", "Medium", "High", "Urgent");
        priorityComboBox.setValue("Medium");
    }

    /**
     * Sets default shift time.
     */
    private void setDefaultShift() {
        shiftLabel.setText("08:00 - 16:00");
    }

    /**
     * Generates a unique report ID based on current date and random number.
     */
    private void generateReportId() {
        String prefix = "REP";
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String random = String.format("%04d", (int) (Math.random() * 10000));
        reportIdField.setText(prefix + date + random);
    }

    /**
     * Sets the current employee and updates UI accordingly.
     *
     * @param employee The employee object representing the current user
     */
    public void setCurrentEmployee(Employee employee) {
        this.currentEmployee = employee;
        if (employee != null) {
            officerNameLabel.setText("Officer: " + employee.getName());
        }
    }

    // FXML Action Handlers
    /**
     * Handles save draft action.
     */
    @FXML
    private void handleSaveDraft() {
        if (validateForm()) {
            saveReportWithStatus("Draft");
        }
    }

    /**
     * Handles submit report action.
     */
    @FXML
    private void handleSubmitReport() {
        showConfirmationDialog(
                "Submit Report",
                "Confirm Submission",
                "Are you sure you want to submit this report?",
                this::submitReport
        );
    }

    /**
     * Handles clear form action.
     */
    @FXML
    private void handleClearForm() {
        showConfirmationDialog(
                "Clear Form",
                "Confirm Clear",
                "Clear all fields?",
                this::clearFormAndGenerateId
        );
    }

    /**
     * Shows a confirmation dialog with specified parameters. Uses Strategy
     * pattern for callback execution.
     *
     * @param title Dialog title
     * @param header Dialog header text
     * @param content Dialog content text
     * @param confirmationCallback Callback to execute if confirmed
     */
    private void showConfirmationDialog(String title, String header, String content, Runnable confirmationCallback) {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle(title);
        confirmAlert.setHeaderText(header);
        confirmAlert.setContentText(content);

        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                confirmationCallback.run();
            }
        });
    }

    /**
     * Saves report with specified status.
     *
     * @param status The status to set for the report
     */
    private void saveReportWithStatus(String status) {
        try {
            DailyReport report = createReportFromForm();
            report.setStatus(status);

            boolean success = reportService.saveReport(report);
            if (success) {
                handleSaveSuccess(status);
            } else {
                showAlert("Error", "Failed to save report.", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            handleSaveError(e);
        }
    }

    /**
     * Handles successful report save.
     *
     * @param status The status of the saved report
     */
    private void handleSaveSuccess(String status) {
        String message = "Draft".equals(status)
                ? "Report saved as draft successfully!"
                : "Report submitted successfully!";
        showAlert("Success", message, Alert.AlertType.INFORMATION);

        if ("Submitted".equals(status)) {
            clearForm();
            generateReportId();
            closeWindow();
        }
    }

    /**
     * Handles report save error.
     *
     * @param e The exception that occurred
     */
    private void handleSaveError(Exception e) {
        showAlert("Error", "Error saving report: " + e.getMessage(), Alert.AlertType.ERROR);
    }

    /**
     * Submits the report with "Submitted" status.
     */
    private void submitReport() {
        saveReportWithStatus("Submitted");
    }

    /**
     * Clears form and generates new report ID.
     */
    private void clearFormAndGenerateId() {
        clearForm();
        generateReportId();
    }

    /**
     * Validates form input data.
     *
     * @return true if form is valid, false otherwise
     */
    private boolean validateForm() {
        if (!validateReportType()) {
            return false;
        }
        if (!validateIncidentsField()) {
            return false;
        }
        return true;
    }

    /**
     * Validates report type selection.
     *
     * @return true if valid, false otherwise
     */
    private boolean validateReportType() {
        if (reportTypeComboBox.getValue() == null) {
            showAlert("Validation Error", "Please select Report Type", Alert.AlertType.ERROR);
            return false;
        }
        return true;
    }

    /**
     * Validates incidents field.
     *
     * @return true if valid, false otherwise
     */
    private boolean validateIncidentsField() {
        if (incidentsArea.getText().trim().isEmpty()) {
            showAlert("Validation Error", "Please enter Incident Description", Alert.AlertType.ERROR);
            return false;
        }
        return true;
    }

    /**
     * Creates a DailyReport object from form data. Uses Factory pattern for
     * report creation.
     *
     * @return A populated DailyReport object
     */
    private DailyReport createReportFromForm() {
        DailyReport report = new DailyReport();

        report.setReportId(reportIdField.getText());
        setOfficerInfo(report);
        setReportDetails(report);
        setReportContent(report);
        setDefaultValues(report);

        return report;
    }

    /**
     * Sets officer information in the report.
     *
     * @param report The report to update
     */
    private void setOfficerInfo(DailyReport report) {
        if (currentEmployee != null) {
            report.setOfficerId(currentEmployee.getId());
            report.setOfficerName(currentEmployee.getName());
        } else {
            report.setOfficerId("UNKNOWN");
            report.setOfficerName("Unknown Officer");
        }
    }

    /**
     * Sets report details from form fields.
     *
     * @param report The report to update
     */
    private void setReportDetails(DailyReport report) {
        report.setReportType(reportTypeComboBox.getValue());
        report.setPriority(priorityComboBox.getValue());
        report.setReportDate(reportDatePicker.getValue() != null ? reportDatePicker.getValue() : LocalDate.now());
    }

    /**
     * Sets report content from text areas.
     *
     * @param report The report to update
     */
    private void setReportContent(DailyReport report) {
        report.setIncidentsSummary(incidentsArea.getText());
        report.setActionsTaken(actionsArea.getText());
        report.setAdditionalNotes(notesArea.getText());
    }

    /**
     * Sets default values for report fields.
     *
     * @param report The report to update
     */
    private void setDefaultValues(DailyReport report) {
        report.setPatrolsCompleted("0");
        report.setCellInspections("0");
        report.setVisitorScreenings("0");
    }

    /**
     * Clears all form fields.
     */
    private void clearForm() {
        reportTypeComboBox.setValue("Daily Security Report");
        priorityComboBox.setValue("Medium");
        incidentsArea.clear();
        actionsArea.clear();
        notesArea.clear();
        reportDatePicker.setValue(LocalDate.now());
    }

    /**
     * Closes the current window.
     */
    private void closeWindow() {
        Stage stage = (Stage) officerNameLabel.getScene().getWindow();
        stage.close();
    }

    /**
     * Shows an alert dialog with specified parameters.
     *
     * @param title The alert title
     * @param message The alert message
     * @param alertType The type of alert
     */
    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
