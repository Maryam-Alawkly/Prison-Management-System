package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.layout.VBox;
import model.DailyReport;
import model.Employee;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import service.DailyReportService;

/**
 * Controller for Report Management interface. Implements Initializable for
 * JavaFX initialization. Uses Singleton pattern for service instances and
 * Strategy pattern for report filtering.
 */
public class ReportManagementController implements Initializable {

    // Table UI components
    @FXML
    private TableView<DailyReport> reportTable;
    @FXML
    private TableColumn<DailyReport, String> reportIdColumn;
    @FXML
    private TableColumn<DailyReport, String> officerNameColumn;
    @FXML
    private TableColumn<DailyReport, String> reportTypeColumn;
    @FXML
    private TableColumn<DailyReport, String> reportDateColumn;
    @FXML
    private TableColumn<DailyReport, String> statusColumn;
    @FXML
    private TableColumn<DailyReport, String> priorityColumn;
    @FXML
    private TableColumn<DailyReport, String> createdAtColumn;

    // Form UI components
    @FXML
    private TextArea adminCommentsArea;

    // Search UI components
    @FXML
    private TextField searchField;
    @FXML
    private ComboBox<String> filterStatusComboBox;
    @FXML
    private ComboBox<String> filterTypeComboBox;
    @FXML
    private DatePicker filterDateFromPicker;
    @FXML
    private DatePicker filterDateToPicker;

    // Service instance (Singleton pattern)
    private DailyReportService reportService;

    // Data collections
    private ObservableList<DailyReport> reportsList;

    // Data models
    private DailyReport selectedReport;
    private Employee currentEmployee;

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
        initializeDataStructures();
        initializeTableColumns();
        initializeFilters();
        loadReports();
        setupTableSelectionListener();
    }

    /**
     * Initializes service instances using Singleton pattern.
     */
    private void initializeServices() {
        reportService = DailyReportService.getInstance();
    }

    /**
     * Initializes data structures for the controller.
     */
    private void initializeDataStructures() {
        reportsList = FXCollections.observableArrayList();
    }

    /**
     * Initializes table columns with cell value factories.
     */
    private void initializeTableColumns() {
        configureReportIdColumn();
        configureOfficerNameColumn();
        configureReportTypeColumn();
        configureReportDateColumn();
        configureStatusColumn();
        configurePriorityColumn();
        configureCreatedAtColumn();

        reportTable.setItems(reportsList);
    }

    /**
     * Configures report ID column.
     */
    private void configureReportIdColumn() {
        reportIdColumn.setCellValueFactory(cellData -> cellData.getValue().reportIdProperty());
    }

    /**
     * Configures officer name column.
     */
    private void configureOfficerNameColumn() {
        officerNameColumn.setCellValueFactory(cellData -> cellData.getValue().officerNameProperty());
    }

    /**
     * Configures report type column.
     */
    private void configureReportTypeColumn() {
        reportTypeColumn.setCellValueFactory(cellData -> cellData.getValue().reportTypeProperty());
    }

    /**
     * Configures report date column with formatted date.
     */
    private void configureReportDateColumn() {
        reportDateColumn.setCellValueFactory(cellData -> {
            DailyReport report = cellData.getValue();
            return new SimpleStringProperty(report.getFormattedReportDate());
        });
    }

    /**
     * Configures status column.
     */
    private void configureStatusColumn() {
        statusColumn.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
    }

    /**
     * Configures priority column.
     */
    private void configurePriorityColumn() {
        priorityColumn.setCellValueFactory(cellData -> cellData.getValue().priorityProperty());
    }

    /**
     * Configures created at column with formatted date.
     */
    private void configureCreatedAtColumn() {
        createdAtColumn.setCellValueFactory(cellData -> {
            DailyReport report = cellData.getValue();
            return new SimpleStringProperty(report.getFormattedCreatedAt());
        });
    }

    /**
     * Initializes all filter components with default values.
     */
    private void initializeFilters() {
        initializeStatusFilter();
        initializeTypeFilter();
        initializeDateFilters();
    }

    /**
     * Initializes status filter combo box.
     */
    private void initializeStatusFilter() {
        filterStatusComboBox.getItems().addAll("All", "Draft", "Submitted", "Approved", "Rejected");
        filterStatusComboBox.setValue("All");
    }

    /**
     * Initializes type filter combo box.
     */
    private void initializeTypeFilter() {
        filterTypeComboBox.getItems().addAll("All", "Daily Security Report", "Shift Change Report",
                "Incident Report", "Weekly Summary", "Monthly Summary");
        filterTypeComboBox.setValue("All");
    }

    /**
     * Initializes date filter pickers with default values.
     */
    private void initializeDateFilters() {
        filterDateFromPicker.setValue(LocalDate.now().minusDays(7));
        filterDateToPicker.setValue(LocalDate.now());
    }

    /**
     * Loads all reports from the service.
     */
    private void loadReports() {
        try {
            reportsList.clear();
            reportsList.addAll(reportService.getAllReports());
        } catch (Exception e) {
            showAlert("Error", "Failed to load reports: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    /**
     * Sets up table selection listener to track selected report.
     */
    private void setupTableSelectionListener() {
        reportTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    selectedReport = newValue;
                });
    }

    /**
     * Sets the current employee.
     *
     * @param employee The employee object representing the current user
     */
    public void setCurrentEmployee(Employee employee) {
        this.currentEmployee = employee;
    }

    // FXML Action Handlers
    /**
     * Handles search functionality with filter criteria. Uses Strategy pattern
     * for report filtering.
     */
    @FXML
    private void handleSearch() {
        String searchText = searchField.getText().toLowerCase();
        String statusFilter = filterStatusComboBox.getValue();
        String typeFilter = filterTypeComboBox.getValue();
        LocalDate dateFrom = filterDateFromPicker.getValue();
        LocalDate dateTo = filterDateToPicker.getValue();

        reportsList.clear();
        reportsList.addAll(reportService.searchReports(searchText, statusFilter, typeFilter, dateFrom, dateTo));
    }

    /**
     * Handles clear action to reset all filters.
     */
    @FXML
    private void handleClear() {
        resetSearchFilters();
        loadReports();
    }

    /**
     * Resets all search filters to their default values.
     */
    private void resetSearchFilters() {
        searchField.clear();
        filterStatusComboBox.setValue("All");
        filterTypeComboBox.setValue("All");
        filterDateFromPicker.setValue(LocalDate.now().minusDays(7));
        filterDateToPicker.setValue(LocalDate.now());
    }

    /**
     * Handles refresh action to reload reports.
     */
    @FXML
    private void handleRefresh() {
        loadReports();
    }

    /**
     * Handles save comment action for selected report.
     */
    @FXML
    private void handleSaveComment() {
        if (selectedReport != null) {
            showSaveCommentDialog();
        } else {
            showAlert("Warning", "Please select a report first.", Alert.AlertType.WARNING);
        }
    }

    /**
     * Shows dialog for saving admin comment.
     */
    private void showSaveCommentDialog() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Save Comment");
        dialog.setHeaderText("Save comment for report: " + selectedReport.getReportId());
        dialog.setContentText("Comment:");

        dialog.showAndWait().ifPresent(this::saveComment);
    }

    /**
     * Saves admin comment to the report.
     *
     * @param comment The comment text to save
     */
    private void saveComment(String comment) {
        if (!comment.trim().isEmpty()) {
            try {
                String adminName = getCurrentAdminName();
                boolean success = reportService.addAdminComment(
                        selectedReport.getReportId(),
                        comment,
                        adminName
                );

                if (success) {
                    handleCommentSaveSuccess(comment);
                } else {
                    showAlert("Error", "Failed to save comment.", Alert.AlertType.ERROR);
                }
            } catch (Exception e) {
                showAlert("Error", "Error saving comment: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    /**
     * Gets the current admin's name.
     *
     * @return Admin name or default value
     */
    private String getCurrentAdminName() {
        return currentEmployee != null ? currentEmployee.getName() : "Admin";
    }

    /**
     * Handles successful comment save.
     *
     * @param comment The saved comment
     */
    private void handleCommentSaveSuccess(String comment) {
        showAlert("Success", "Comment saved successfully!", Alert.AlertType.INFORMATION);

        // Update local report
        String currentComments = selectedReport.getAdminComments();
        String updatedComments = (currentComments != null ? currentComments + "\n" : "") + comment;
        selectedReport.setAdminComments(updatedComments);
    }

    /**
     * Handles view full report action.
     */
    @FXML
    private void handleViewFullReport() {
        if (selectedReport != null) {
            showFullReportDialog();
        } else {
            showAlert("Warning", "Please select a report to view.", Alert.AlertType.WARNING);
        }
    }

    /**
     * Shows dialog with full report details.
     */
    private void showFullReportDialog() {
        try {
            Alert reportDialog = createReportDialog();
            TextArea reportContent = createReportContentArea();
            reportDialog.getDialogPane().setContent(reportContent);
            reportDialog.getDialogPane().setPrefSize(850, 650);
            reportDialog.showAndWait();
        } catch (Exception e) {
            showAlert("Error", "Cannot display full report: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    /**
     * Creates a dialog for displaying report.
     *
     * @return Configured Alert dialog
     */
    private Alert createReportDialog() {
        Alert reportDialog = new Alert(Alert.AlertType.INFORMATION);
        reportDialog.setTitle("Full Report: " + selectedReport.getReportId());
        reportDialog.setHeaderText("Officer: " + selectedReport.getOfficerName()
                + " | Date: " + selectedReport.getFormattedReportDate());
        return reportDialog;
    }

    /**
     * Creates a text area with formatted report content.
     *
     * @return Configured TextArea
     */
    private TextArea createReportContentArea() {
        TextArea reportContent = new TextArea(formatFullReport(selectedReport));
        reportContent.setEditable(false);
        reportContent.setWrapText(true);
        reportContent.setStyle("-fx-font-family: 'Monospaced'; -fx-font-size: 12px;");
        reportContent.setPrefSize(800, 600);
        return reportContent;
    }

    /**
     * Handles add comment action (adds comment to text area).
     */
    @FXML
    private void handleAddComment() {
        if (selectedReport != null) {
            showAddCommentDialog();
        } else {
            showAlert("Warning", "Please select a report first.", Alert.AlertType.WARNING);
        }
    }

    /**
     * Shows dialog for adding comment to text area.
     */
    private void showAddCommentDialog() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Comment");
        dialog.setHeaderText("Add Comment for Report: " + selectedReport.getReportId());
        dialog.setContentText("Enter your comment:");

        dialog.showAndWait().ifPresent(this::addCommentToTextArea);
    }

    /**
     * Adds comment to the text area.
     *
     * @param comment The comment to add
     */
    private void addCommentToTextArea(String comment) {
        if (!comment.trim().isEmpty()) {
            adminCommentsArea.setText(comment);
            showAlert("Info", "Comment added to text area. Click 'Save Comment' to save to database.",
                    Alert.AlertType.INFORMATION);
        }
    }

    /**
     * Handles approve report action.
     */
    @FXML
    private void handleApproveReport() {
        if (selectedReport != null) {
            confirmAndUpdateReportStatus("Approve", "Approved");
        } else {
            showAlert("Warning", "Please select a report to approve.", Alert.AlertType.WARNING);
        }
    }

    /**
     * Handles reject report action.
     */
    @FXML
    private void handleRejectReport() {
        if (selectedReport != null) {
            confirmAndUpdateReportStatus("Reject", "Rejected");
        } else {
            showAlert("Warning", "Please select a report to reject.", Alert.AlertType.WARNING);
        }
    }

    /**
     * Shows confirmation dialog and updates report status.
     *
     * @param action The action name (Approve/Reject)
     * @param newStatus The new status to set
     */
    private void confirmAndUpdateReportStatus(String action, String newStatus) {
        Alert confirmAlert = createConfirmationAlert(action);

        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                updateReportStatus(newStatus);
            }
        });
    }

    /**
     * Creates a confirmation alert for report status update.
     *
     * @param action The action name
     * @return Configured Alert dialog
     */
    private Alert createConfirmationAlert(String action) {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle(action + " Report");
        confirmAlert.setHeaderText(action + " Report");
        confirmAlert.setContentText(action + " report " + selectedReport.getReportId() + "?");
        return confirmAlert;
    }

    /**
     * Updates report status in the database.
     *
     * @param newStatus The new status to set
     */
    private void updateReportStatus(String newStatus) {
        try {
            String reviewedBy = getCurrentAdminName();
            boolean success = reportService.updateReportStatus(
                    selectedReport.getReportId(),
                    newStatus,
                    reviewedBy,
                    ""
            );

            if (success) {
                handleStatusUpdateSuccess(newStatus);
            } else {
                showAlert("Error", "Failed to " + newStatus.toLowerCase() + " report.", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            showAlert("Error", "Error updating report status: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    /**
     * Handles successful status update.
     *
     * @param newStatus The new status that was set
     */
    private void handleStatusUpdateSuccess(String newStatus) {
        String action = "Approved".equals(newStatus) ? "approved" : "rejected";
        showAlert("Success", "Report " + action + " successfully!", Alert.AlertType.INFORMATION);
        loadReports();
    }

    /**
     * Formats a full report for display.
     *
     * @param report The report to format
     * @return Formatted report string
     */
    private String formatFullReport(DailyReport report) {
        StringBuilder builder = new StringBuilder();

        builder.append("================================================================\n");
        builder.append("                 PRISON MANAGEMENT SYSTEM - DAILY REPORT\n");
        builder.append("================================================================\n\n");

        appendReportMetadata(builder, report);
        appendIncidentReport(builder, report);
        appendDailyActivities(builder, report);
        appendAdditionalNotes(builder, report);

        return builder.toString();
    }

    /**
     * Appends report metadata to the builder.
     *
     * @param builder The StringBuilder to append to
     * @param report The report containing metadata
     */
    private void appendReportMetadata(StringBuilder builder, DailyReport report) {
        builder.append("Report ID: ").append(report.getReportId()).append("\n");
        builder.append("Officer: ").append(report.getOfficerName()).append("\n");
        builder.append("Report Type: ").append(report.getReportType()).append("\n");
        builder.append("Report Date: ").append(report.getFormattedReportDate()).append("\n");
        builder.append("Status: ").append(report.getStatus()).append("\n");
        builder.append("Priority: ").append(report.getPriority()).append("\n\n");
    }

    /**
     * Appends incident report section to the builder.
     *
     * @param builder The StringBuilder to append to
     * @param report The report containing incident data
     */
    private void appendIncidentReport(StringBuilder builder, DailyReport report) {
        builder.append("==================== INCIDENT REPORT ====================\n");
        builder.append("Incidents Summary:\n").append(report.getIncidentsSummary()).append("\n\n");
        builder.append("Actions Taken:\n").append(report.getActionsTaken()).append("\n\n");
    }

    /**
     * Appends daily activities section to the builder.
     *
     * @param builder The StringBuilder to append to
     * @param report The report containing activity data
     */
    private void appendDailyActivities(StringBuilder builder, DailyReport report) {
        builder.append("==================== DAILY ACTIVITIES ====================\n");
        builder.append("Patrols Completed: ").append(report.getPatrolsCompleted()).append("\n");
        builder.append("Cell Inspections: ").append(report.getCellInspections()).append("\n");
        builder.append("Visitor Screenings: ").append(report.getVisitorScreenings()).append("\n");
        builder.append("Activity Details:\n").append(report.getActivityDetails()).append("\n\n");
    }

    /**
     * Appends additional notes section to the builder.
     *
     * @param builder The StringBuilder to append to
     * @param report The report containing additional notes
     */
    private void appendAdditionalNotes(StringBuilder builder, DailyReport report) {
        builder.append("==================== ADDITIONAL NOTES ====================\n");
        builder.append(report.getAdditionalNotes()).append("\n");
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
