package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Employee;
import model.GuardDuty;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;
import service.EmployeeService;
import service.GuardDutyService;

/**
 * Controller for Guard Duties Management interface. Implements Initializable
 * for JavaFX initialization. Uses Singleton pattern for service instances and
 * Strategy pattern for search filtering.
 */
public class GuardDutiesController implements Initializable {

    // Table UI components
    @FXML
    private TableView<GuardDuty> guardDutiesTable;
    @FXML
    private TableColumn<GuardDuty, String> dutyIdColumn;
    @FXML
    private TableColumn<GuardDuty, String> officerNameColumn;
    @FXML
    private TableColumn<GuardDuty, String> dutyTypeColumn;
    @FXML
    private TableColumn<GuardDuty, String> locationColumn;
    @FXML
    private TableColumn<GuardDuty, String> startTimeColumn;
    @FXML
    private TableColumn<GuardDuty, String> endTimeColumn;
    @FXML
    private TableColumn<GuardDuty, String> statusColumn;
    @FXML
    private TableColumn<GuardDuty, String> priorityColumn;
    @FXML
    private TableColumn<GuardDuty, String> completedTimeColumn;

    // Search UI components
    @FXML
    private TextField searchField;
    @FXML
    private ComboBox<String> filterStatusComboBox;
    @FXML
    private DatePicker filterDatePicker;

    // Form UI components
    @FXML
    private TextField dutyIdField;
    @FXML
    private ComboBox<String> officerComboBox;
    @FXML
    private ComboBox<String> dutyTypeComboBox;
    @FXML
    private ComboBox<String> locationComboBox;
    @FXML
    private TextField startTimeField;
    @FXML
    private TextField endTimeField;
    @FXML
    private DatePicker datePicker;
    @FXML
    private ComboBox<String> statusComboBox;
    @FXML
    private ComboBox<String> priorityComboBox;
    @FXML
    private TextArea notesArea;

    // Data models
    private Employee currentEmployee;

    // Service instances (Singleton pattern)
    private GuardDutyService guardDutyService;
    private EmployeeService employeeService;

    // Data collections
    private ObservableList<GuardDuty> guardDutiesList;

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
        initializeComboBoxes();
        loadGuardDuties();
        setupTableSelectionListener();
    }

    /**
     * Initializes service instances using Singleton pattern.
     */
    private void initializeServices() {
        guardDutyService = GuardDutyService.getInstance();
        employeeService = EmployeeService.getInstance();
    }

    /**
     * Initializes data structures for the controller.
     */
    private void initializeDataStructures() {
        guardDutiesList = FXCollections.observableArrayList();
    }

    /**
     * Initializes table columns with cell value factories.
     */
    private void initializeTableColumns() {
        dutyIdColumn.setCellValueFactory(cellData -> cellData.getValue().dutyIdProperty());
        officerNameColumn.setCellValueFactory(cellData -> cellData.getValue().officerNameProperty());
        dutyTypeColumn.setCellValueFactory(cellData -> cellData.getValue().dutyTypeProperty());
        locationColumn.setCellValueFactory(cellData -> cellData.getValue().locationProperty());
        startTimeColumn.setCellValueFactory(cellData -> cellData.getValue().startTimeProperty());
        endTimeColumn.setCellValueFactory(cellData -> cellData.getValue().endTimeProperty());
        statusColumn.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
        priorityColumn.setCellValueFactory(cellData -> cellData.getValue().priorityProperty());
        completedTimeColumn.setCellValueFactory(cellData -> cellData.getValue().completedTimeProperty());

        guardDutiesTable.setItems(guardDutiesList);
    }

    /**
     * Initializes all combo boxes with appropriate options.
     */
    private void initializeComboBoxes() {
        initializeFilterComboBoxes();
        initializeFormComboBoxes();
        setDefaultDates();
    }

    /**
     * Initializes filter combo boxes.
     */
    private void initializeFilterComboBoxes() {
        filterStatusComboBox.getItems().addAll("All", "Scheduled", "In Progress", "Completed", "Cancelled", "Overdue");
        filterStatusComboBox.setValue("All");
    }

    /**
     * Initializes form combo boxes.
     */
    private void initializeFormComboBoxes() {
        initializeDutyTypeComboBox();
        initializeLocationComboBox();
        initializeStatusComboBox();
        initializePriorityComboBox();
    }

    /**
     * Initializes duty type combo box.
     */
    private void initializeDutyTypeComboBox() {
        dutyTypeComboBox.getItems().addAll(
                "Security Patrol",
                "Cell Inspection",
                "Visitor Screening",
                "Perimeter Check",
                "Control Room",
                "Tower Guard",
                "Transport Escort",
                "Emergency Response"
        );
    }

    /**
     * Initializes location combo box.
     */
    private void initializeLocationComboBox() {
        locationComboBox.getItems().addAll(
                "Main Gate",
                "Perimeter Fence",
                "Cell Block A",
                "Cell Block B",
                "Cell Block C",
                "Visitor Center",
                "Administration Building",
                "Yard Area",
                "Kitchen",
                "Infirmary"
        );
    }

    /**
     * Initializes status combo box.
     */
    private void initializeStatusComboBox() {
        statusComboBox.getItems().addAll("Scheduled", "In Progress", "Completed", "Cancelled");
        statusComboBox.setValue("Scheduled");
    }

    /**
     * Initializes priority combo box.
     */
    private void initializePriorityComboBox() {
        priorityComboBox.getItems().addAll("Low", "Medium", "High", "Critical");
        priorityComboBox.setValue("Medium");
    }

    /**
     * Sets default dates for date pickers.
     */
    private void setDefaultDates() {
        LocalDate today = LocalDate.now();
        datePicker.setValue(today);
        filterDatePicker.setValue(today);
    }

    /**
     * Loads guard duties from the service.
     */
    private void loadGuardDuties() {
        try {
            guardDutiesList.clear();
            guardDutiesList.addAll(guardDutyService.getAllGuardDuties());
        } catch (Exception e) {
            showAlert("Error", "Failed to load guard duties: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    /**
     * Sets up table selection listener to display duty details when selected.
     */
    private void setupTableSelectionListener() {
        guardDutiesTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showGuardDutyDetails(newValue));
    }

    /**
     * Shows guard duty details in the form.
     *
     * @param guardDuty The guard duty to display
     */
    private void showGuardDutyDetails(GuardDuty guardDuty) {
        if (guardDuty != null) {
            dutyIdField.setText(guardDuty.getDutyId());
            officerComboBox.setValue(guardDuty.getOfficerName());
            dutyTypeComboBox.setValue(guardDuty.getDutyType());
            locationComboBox.setValue(guardDuty.getLocation());
            startTimeField.setText(guardDuty.getStartTime());
            endTimeField.setText(guardDuty.getEndTime());
            statusComboBox.setValue(guardDuty.getStatus());
            priorityComboBox.setValue(guardDuty.getPriority());
            notesArea.setText(guardDuty.getNotes());

            if (guardDuty.getDate() != null) {
                datePicker.setValue(guardDuty.getDate());
            }
        }
    }

    // FXML Action Handlers
    /**
     * Handles search functionality with filter criteria. Uses Strategy pattern
     * for search filtering.
     */
    @FXML
    private void handleSearch() {
        String searchText = searchField.getText().toLowerCase();
        String statusFilter = filterStatusComboBox.getValue();
        LocalDate dateFilter = filterDatePicker.getValue();

        guardDutiesList.clear();
        guardDutiesList.addAll(guardDutyService.searchGuardDuties(searchText, statusFilter, dateFilter));
    }

    /**
     * Handles clear action to reset search and filters.
     */
    @FXML
    private void handleClear() {
        searchField.clear();
        filterStatusComboBox.setValue("All");
        filterDatePicker.setValue(LocalDate.now());
        loadGuardDuties();
    }

    /**
     * Handles add duty action.
     */
    @FXML
    private void handleAddDuty() {
        if (validateRequiredFields()) {
            createAndAddDuty();
        }
    }

    /**
     * Validates required form fields.
     *
     * @return true if all required fields are valid, false otherwise
     */
    private boolean validateRequiredFields() {
        if (dutyTypeComboBox.getValue() == null) {
            showAlert("Error", "Please select Duty Type", Alert.AlertType.ERROR);
            return false;
        }
        if (locationComboBox.getValue() == null) {
            showAlert("Error", "Please select Location", Alert.AlertType.ERROR);
            return false;
        }
        return true;
    }

    /**
     * Creates and adds a new guard duty from form data. Uses Factory pattern
     * for duty creation.
     */
    private void createAndAddDuty() {
        try {
            GuardDuty newDuty = createGuardDutyFromForm();

            boolean success = guardDutyService.addGuardDuty(newDuty);
            if (success) {
                showAlert("Success", "Duty added successfully with ID: " + newDuty.getDutyId(), Alert.AlertType.INFORMATION);
                loadGuardDuties();
                clearForm();
            } else {
                showAlert("Error", "Failed to add duty.", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            handleDutyActionError("adding", e);
        }
    }

    /**
     * Creates a GuardDuty object from form data.
     *
     * @return A populated GuardDuty object
     */
    private GuardDuty createGuardDutyFromForm() {
        GuardDuty newDuty = new GuardDuty();

        newDuty.setDutyId(generateDutyId());
        setOfficerInfo(newDuty);
        setDutyDetails(newDuty);
        setDutySchedule(newDuty);
        setDutyStatusAndPriority(newDuty);

        return newDuty;
    }

    /**
     * Generates a unique duty ID.
     *
     * @return Generated duty ID
     */
    private String generateDutyId() {
        return "DUTY" + System.currentTimeMillis();
    }

    /**
     * Sets officer information in the duty.
     *
     * @param duty The duty to update
     */
    private void setOfficerInfo(GuardDuty duty) {
        if (currentEmployee != null) {
            duty.setOfficerId(currentEmployee.getId());
            duty.setOfficerName(currentEmployee.getName());
        } else {
            duty.setOfficerId("UNKNOWN");
            duty.setOfficerName(officerComboBox.getValue());
        }
    }

    /**
     * Sets duty details from form fields.
     *
     * @param duty The duty to update
     */
    private void setDutyDetails(GuardDuty duty) {
        duty.setDutyType(dutyTypeComboBox.getValue());
        duty.setLocation(locationComboBox.getValue());
        duty.setNotes(notesArea.getText());
    }

    /**
     * Sets duty schedule from form fields.
     *
     * @param duty The duty to update
     */
    private void setDutySchedule(GuardDuty duty) {
        duty.setStartTime(startTimeField.getText());
        duty.setEndTime(endTimeField.getText());
        duty.setDate(datePicker.getValue() != null ? datePicker.getValue() : LocalDate.now());
    }

    /**
     * Sets duty status and priority from form fields.
     *
     * @param duty The duty to update
     */
    private void setDutyStatusAndPriority(GuardDuty duty) {
        duty.setStatus(statusComboBox.getValue());
        duty.setPriority(priorityComboBox.getValue());
    }

    /**
     * Handles refresh action.
     */
    @FXML
    private void handleRefresh() {
        loadGuardDuties();
        clearForm();
    }

    /**
     * Handles viewing current employee's duties.
     */
    @FXML
    private void handleMyDuties() {
        if (currentEmployee != null) {
            guardDutiesList.clear();
            guardDutiesList.addAll(guardDutyService.getDutiesByOfficer(currentEmployee.getId()));
        }
    }

    /**
     * Handles start duty action.
     */
    @FXML
    private void handleStartDuty() {
        GuardDuty selectedDuty = guardDutiesTable.getSelectionModel().getSelectedItem();
        if (selectedDuty != null) {
            updateDutyStatus(selectedDuty.getDutyId(), "In Progress");
        } else {
            showAlert("Warning", "Please select a duty to start", Alert.AlertType.WARNING);
        }
    }

    /**
     * Handles complete duty action.
     */
    @FXML
    private void handleCompleteDuty() {
        GuardDuty selectedDuty = guardDutiesTable.getSelectionModel().getSelectedItem();
        if (selectedDuty != null) {
            updateDutyStatus(selectedDuty.getDutyId(), "Completed");
        } else {
            showAlert("Warning", "Please select a duty to complete", Alert.AlertType.WARNING);
        }
    }

    /**
     * Updates duty status with completion time.
     *
     * @param dutyId The ID of the duty to update
     * @param newStatus The new status to set
     */
    private void updateDutyStatus(String dutyId, String newStatus) {
        try {
            String completionTime = LocalTime.now().toString();
            guardDutyService.updateDutyStatus(dutyId, newStatus, completionTime);
            showAlert("Success", "Duty " + newStatus.toLowerCase() + " successfully", Alert.AlertType.INFORMATION);
            loadGuardDuties();
        } catch (Exception e) {
            showAlert("Error", "Failed to update duty: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    /**
     * Handles update duty action.
     */
    @FXML
    private void handleUpdateDuty() {
        GuardDuty selectedDuty = guardDutiesTable.getSelectionModel().getSelectedItem();
        if (selectedDuty != null) {
            updateSelectedDuty(selectedDuty);
        } else {
            showAlert("Warning", "Please select a duty to update", Alert.AlertType.WARNING);
        }
    }

    /**
     * Updates the selected duty with form data.
     *
     * @param duty The duty to update
     */
    private void updateSelectedDuty(GuardDuty duty) {
        try {
            updateDutyFromForm(duty);
            guardDutyService.updateGuardDuty(duty);
            showAlert("Success", "Duty updated successfully", Alert.AlertType.INFORMATION);
            loadGuardDuties();
        } catch (Exception e) {
            handleDutyActionError("updating", e);
        }
    }

    /**
     * Updates duty object with form data.
     *
     * @param duty The duty to update
     */
    private void updateDutyFromForm(GuardDuty duty) {
        duty.setOfficerId(getSelectedOfficerId());
        duty.setOfficerName(officerComboBox.getValue());
        duty.setDutyType(dutyTypeComboBox.getValue());
        duty.setLocation(locationComboBox.getValue());
        duty.setStartTime(startTimeField.getText());
        duty.setEndTime(endTimeField.getText());
        duty.setDate(datePicker.getValue());
        duty.setStatus(statusComboBox.getValue());
        duty.setPriority(priorityComboBox.getValue());
        duty.setNotes(notesArea.getText());
    }

    /**
     * Gets the officer ID based on selected officer name.
     *
     * @return Officer ID or default value
     */
    private String getSelectedOfficerId() {
        String officerName = officerComboBox.getValue();
        if (officerName != null && !officerName.isEmpty()) {
            try {
                Employee employee = employeeService.getEmployeeByName(officerName);
                if (employee != null) {
                    return employee.getId();
                }
            } catch (Exception e) {
                System.err.println("Error getting officer ID: " + e.getMessage());
            }
        }
        return currentEmployee != null ? currentEmployee.getId() : "UNKNOWN";
    }

    /**
     * Handles delete duty action.
     */
    @FXML
    private void handleDeleteDuty() {
        GuardDuty selectedDuty = guardDutiesTable.getSelectionModel().getSelectedItem();
        if (selectedDuty != null) {
            confirmAndDeleteDuty(selectedDuty);
        } else {
            showAlert("Warning", "Please select a duty to delete", Alert.AlertType.WARNING);
        }
    }

    /**
     * Shows confirmation dialog and deletes duty if confirmed.
     *
     * @param duty The duty to delete
     */
    private void confirmAndDeleteDuty(GuardDuty duty) {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Delete");
        confirmAlert.setHeaderText("Delete Duty");
        confirmAlert.setContentText("Are you sure you want to delete duty: " + duty.getDutyId() + "?");

        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    guardDutyService.deleteGuardDuty(duty.getDutyId());
                    showAlert("Success", "Duty deleted successfully", Alert.AlertType.INFORMATION);
                    loadGuardDuties();
                    clearForm();
                } catch (Exception e) {
                    showAlert("Error", "Failed to delete duty: " + e.getMessage(), Alert.AlertType.ERROR);
                }
            }
        });
    }

    /**
     * Handles assign duty action (placeholder).
     */
    @FXML
    private void handleAssignDuty() {
        showAlert("Info", "Assign Duty functionality", Alert.AlertType.INFORMATION);
    }

    /**
     * Handles report issue action.
     */
    @FXML
    private void handleReportIssue() {
        GuardDuty selectedDuty = guardDutiesTable.getSelectionModel().getSelectedItem();
        if (selectedDuty != null) {
            showIssueReportDialog(selectedDuty);
        }
    }

    /**
     * Shows issue reporting dialog.
     *
     * @param duty The duty to report issue for
     */
    private void showIssueReportDialog(GuardDuty duty) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Report Issue");
        dialog.setHeaderText("Report Issue for Duty: " + duty.getDutyId());
        dialog.setContentText("Please describe the issue:");

        dialog.showAndWait().ifPresent(issue -> {
            if (!issue.isEmpty()) {
                reportDutyIssue(duty.getDutyId(), issue);
            }
        });
    }

    /**
     * Reports an issue for a duty.
     *
     * @param dutyId The ID of the duty
     * @param issue The issue description
     */
    private void reportDutyIssue(String dutyId, String issue) {
        try {
            String officerId = currentEmployee != null ? currentEmployee.getId() : "UNKNOWN";
            guardDutyService.reportDutyIssue(dutyId, issue, officerId);
            showAlert("Success", "Issue reported successfully", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            showAlert("Error", "Failed to report issue: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    /**
     * Handles view logs action (placeholder).
     */
    @FXML
    private void handleViewLogs() {
        showAlert("Info", "View Logs functionality", Alert.AlertType.INFORMATION);
    }

    /**
     * Handles generate report action (placeholder).
     */
    @FXML
    private void handleGenerateReport() {
        showAlert("Info", "Generate Report functionality", Alert.AlertType.INFORMATION);
    }

    /**
     * Clears all form fields.
     */
    private void clearForm() {
        dutyIdField.clear();
        officerComboBox.setValue(null);
        dutyTypeComboBox.setValue(null);
        locationComboBox.setValue(null);
        startTimeField.clear();
        endTimeField.clear();
        datePicker.setValue(LocalDate.now());
        statusComboBox.setValue("Scheduled");
        priorityComboBox.setValue("Medium");
        notesArea.clear();
    }

    /**
     * Handles duty action errors.
     *
     * @param action The action being performed
     * @param e The exception that occurred
     */
    private void handleDutyActionError(String action, Exception e) {
        showAlert("Error", "Failed to " + action + " duty: " + e.getMessage(), Alert.AlertType.ERROR);
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

    /**
     * Sets the current employee and updates UI accordingly.
     *
     * @param employee The employee object representing the current user
     */
    public void setCurrentEmployee(Employee employee) {
        this.currentEmployee = employee;
        if (employee != null) {
            officerComboBox.setValue(employee.getName());
        }
    }
}
