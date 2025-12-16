package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Visit;
import model.Visitor;
import model.Prisoner;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import service.VisitService;
import service.PrisonerService;
import service.VisitorService;

/**
 * Controller for Visit Management interface. Implements Initializable for
 * JavaFX initialization. Uses Singleton pattern for service instances and
 * Strategy pattern for search filtering.
 */
public class VisitManagementController implements Initializable {

    // Table UI components
    @FXML
    private TableView<Visit> visitTable;
    @FXML
    private TableColumn<Visit, String> visitIdColumn;
    @FXML
    private TableColumn<Visit, String> prisonerIdColumn;
    @FXML
    private TableColumn<Visit, String> visitorIdColumn;
    @FXML
    private TableColumn<Visit, String> dateTimeColumn;
    @FXML
    private TableColumn<Visit, String> statusColumn;
    @FXML
    private TableColumn<Visit, Integer> durationColumn;

    // Form UI components
    @FXML
    private TextField visitIdField;
    @FXML
    private ComboBox<String> prisonerComboBox;
    @FXML
    private ComboBox<String> visitorComboBox;
    @FXML
    private DatePicker visitDatePicker;
    @FXML
    private ComboBox<String> visitTimeComboBox;
    @FXML
    private TextField durationField;
    @FXML
    private TextArea notesArea;
    @FXML
    private ComboBox<String> statusComboBox;

    // Search UI components
    @FXML
    private TextField searchField;
    @FXML
    private ComboBox<String> filterStatusComboBox;

    // Service instances (Singleton pattern)
    private VisitService visitService;
    private PrisonerService prisonerService;
    private VisitorService visitorService;

    // Data collections
    private ObservableList<Visit> visitList;
    private ObservableList<String> prisonerIds;
    private ObservableList<String> visitorIds;

    // Formatters for date and time
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

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
        setupTableColumns();
        loadInitialData();
        setupComboBoxes();
        setupTableSelectionListener();
    }

    /**
     * Initializes service instances using Singleton pattern.
     */
    private void initializeServices() {
        visitService = VisitService.getInstance();
        prisonerService = PrisonerService.getInstance();
        visitorService = VisitorService.getInstance();
    }

    /**
     * Initializes data structures for the controller.
     */
    private void initializeDataStructures() {
        visitList = FXCollections.observableArrayList();
        prisonerIds = FXCollections.observableArrayList();
        visitorIds = FXCollections.observableArrayList();
    }

    /**
     * Sets up table columns with cell value factories.
     */
    private void setupTableColumns() {
        visitIdColumn.setCellValueFactory(new PropertyValueFactory<>("visitId"));
        prisonerIdColumn.setCellValueFactory(new PropertyValueFactory<>("prisonerId"));
        visitorIdColumn.setCellValueFactory(new PropertyValueFactory<>("visitorId"));

        dateTimeColumn.setCellValueFactory(cellData -> {
            Visit visit = cellData.getValue();
            return new javafx.beans.property.SimpleStringProperty(visit.getFormattedDateTime());
        });

        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));
    }

    /**
     * Loads initial data including visits, prisoners, and visitors.
     */
    private void loadInitialData() {
        loadAllVisits();
        loadPrisonerIds();
        loadVisitorIds();
    }

    /**
     * Loads all visits from the service.
     */
    private void loadAllVisits() {
        visitList.clear();
        visitList.addAll(visitService.getAllVisits());
        visitTable.setItems(visitList);
    }

    /**
     * Loads prisoner IDs and names for combo box.
     */
    private void loadPrisonerIds() {
        prisonerIds.clear();
        for (Prisoner prisoner : prisonerService.getAllPrisoners()) {
            prisonerIds.add(formatPersonDisplay(prisoner.getId(), prisoner.getName()));
        }
    }

    /**
     * Loads visitor IDs and names for combo box.
     */
    private void loadVisitorIds() {
        visitorIds.clear();
        for (Visitor visitor : visitorService.getAllVisitors()) {
            visitorIds.add(formatPersonDisplay(visitor.getId(), visitor.getName()));
        }
    }

    /**
     * Formats person information for display in combo box.
     *
     * @param id The person ID
     * @param name The person name
     * @return Formatted display string
     */
    private String formatPersonDisplay(String id, String name) {
        return id + " - " + name;
    }

    /**
     * Sets up all combo boxes with appropriate options.
     */
    private void setupComboBoxes() {
        setupTimeComboBox();
        setupStatusComboBox();
        setupFilterComboBox();
        setupPersonComboBoxes();
        setDefaultDate();
    }

    /**
     * Sets up time combo box with predefined time slots.
     */
    private void setupTimeComboBox() {
        ObservableList<String> timeSlots = generateTimeSlots();
        visitTimeComboBox.setItems(timeSlots);
        visitTimeComboBox.setValue("09:00");
    }

    /**
     * Generates time slots from 08:00 to 18:00 every 30 minutes.
     *
     * @return List of time slots
     */
    private ObservableList<String> generateTimeSlots() {
        ObservableList<String> timeSlots = FXCollections.observableArrayList();
        LocalTime time = LocalTime.of(8, 0);

        while (time.isBefore(LocalTime.of(18, 30))) {
            timeSlots.add(time.format(TIME_FORMATTER));
            time = time.plusMinutes(30);
        }

        return timeSlots;
    }

    /**
     * Sets up status combo box with visit status options.
     */
    private void setupStatusComboBox() {
        ObservableList<String> statusOptions = FXCollections.observableArrayList(
                "Scheduled", "In Progress", "Completed", "Cancelled"
        );
        statusComboBox.setItems(statusOptions);
        statusComboBox.setValue("Scheduled");
    }

    /**
     * Sets up filter combo box for search functionality.
     */
    private void setupFilterComboBox() {
        ObservableList<String> filterOptions = FXCollections.observableArrayList(
                "All", "Scheduled", "In Progress", "Completed", "Cancelled"
        );
        filterStatusComboBox.setItems(filterOptions);
        filterStatusComboBox.setValue("All");
    }

    /**
     * Sets up prisoner and visitor combo boxes.
     */
    private void setupPersonComboBoxes() {
        prisonerComboBox.setItems(prisonerIds);
        visitorComboBox.setItems(visitorIds);
    }

    /**
     * Sets default date to today.
     */
    private void setDefaultDate() {
        visitDatePicker.setValue(LocalDate.now());
    }

    /**
     * Sets up table selection listener to display visit details when selected.
     */
    private void setupTableSelectionListener() {
        visitTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> displayVisitDetails(newValue));
    }

    /**
     * Displays visit details in the form.
     *
     * @param visit The visit to display
     */
    private void displayVisitDetails(Visit visit) {
        if (visit != null) {
            visitIdField.setText(visit.getVisitId());
            setPrisonerComboBoxValue(visit.getPrisonerId());
            setVisitorComboBoxValue(visit.getVisitorId());
            setDateTimeFields(visit);
            durationField.setText(String.valueOf(visit.getDuration()));
            statusComboBox.setValue(visit.getStatus());
            notesArea.setText(visit.getNotes());
        }
    }

    /**
     * Sets prisoner combo box value based on prisoner ID.
     *
     * @param prisonerId The prisoner ID to find
     */
    private void setPrisonerComboBoxValue(String prisonerId) {
        for (Prisoner prisoner : prisonerService.getAllPrisoners()) {
            if (prisoner.getId().equals(prisonerId)) {
                prisonerComboBox.setValue(formatPersonDisplay(prisoner.getId(), prisoner.getName()));
                break;
            }
        }
    }

    /**
     * Sets visitor combo box value based on visitor ID.
     *
     * @param visitorId The visitor ID to find
     */
    private void setVisitorComboBoxValue(String visitorId) {
        for (Visitor visitor : visitorService.getAllVisitors()) {
            if (visitor.getId().equals(visitorId)) {
                visitorComboBox.setValue(formatPersonDisplay(visitor.getId(), visitor.getName()));
                break;
            }
        }
    }

    /**
     * Sets date and time fields from visit date time.
     *
     * @param visit The visit containing date time
     */
    private void setDateTimeFields(Visit visit) {
        visitDatePicker.setValue(visit.getVisitDateTime().toLocalDate());
        visitTimeComboBox.setValue(visit.getVisitDateTime().toLocalTime().format(TIME_FORMATTER));
    }

    // FXML Action Handlers
    /**
     * Handles add visit action.
     */
    @FXML
    private void handleAddVisit() {
        if (validateVisitInput()) {
            createAndAddVisit();
        }
    }

    /**
     * Creates and adds a new visit from form data. Uses Factory pattern for
     * visit creation.
     */
    private void createAndAddVisit() {
        try {
            Visit newVisit = createVisitFromForm();

            if (visitService.addVisit(newVisit)) {
                showAlert("Success", "Visit added successfully!", Alert.AlertType.INFORMATION);
                loadAllVisits();
                clearFormFields();
            } else {
                showAlert("Error", "Failed to add visit!", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            handleVisitActionError("adding", e);
        }
    }

    /**
     * Handles update visit action.
     */
    @FXML
    private void handleUpdateVisit() {
        Visit selectedVisit = visitTable.getSelectionModel().getSelectedItem();
        if (selectedVisit == null) {
            showAlert("Warning", "Please select a visit to update!", Alert.AlertType.WARNING);
            return;
        }

        if (validateVisitInput()) {
            updateSelectedVisit(selectedVisit);
        }
    }

    /**
     * Updates the selected visit with form data.
     *
     * @param visit The visit to update
     */
    private void updateSelectedVisit(Visit visit) {
        try {
            updateVisitFromForm(visit);

            if (visitService.updateVisit(visit)) {
                showAlert("Success", "Visit updated successfully!", Alert.AlertType.INFORMATION);
                loadAllVisits();
            } else {
                showAlert("Error", "Failed to update visit!", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            handleVisitActionError("updating", e);
        }
    }

    /**
     * Handles delete visit action.
     */
    @FXML
    private void handleDeleteVisit() {
        Visit selectedVisit = visitTable.getSelectionModel().getSelectedItem();
        if (selectedVisit == null) {
            showAlert("Warning", "Please select a visit to delete!", Alert.AlertType.WARNING);
            return;
        }

        confirmAndDeleteVisit(selectedVisit);
    }

    /**
     * Shows confirmation dialog and deletes visit if confirmed.
     *
     * @param visit The visit to delete
     */
    private void confirmAndDeleteVisit(Visit visit) {
        Alert confirmAlert = createConfirmationAlert("Delete Visit",
                "Delete Visit",
                "Are you sure you want to delete visit " + visit.getVisitId() + "?");

        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    if (visitService.deleteVisit(visit.getVisitId())) {
                        showAlert("Success", "Visit deleted successfully!", Alert.AlertType.INFORMATION);
                        loadAllVisits();
                        clearFormFields();
                    } else {
                        showAlert("Error", "Failed to delete visit!", Alert.AlertType.ERROR);
                    }
                } catch (Exception e) {
                    handleVisitActionError("deleting", e);
                }
            }
        });
    }

    /**
     * Creates a confirmation alert dialog.
     *
     * @param title Dialog title
     * @param header Dialog header text
     * @param content Dialog content text
     * @return Configured Alert dialog
     */
    private Alert createConfirmationAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        return alert;
    }

    /**
     * Handles search functionality with filter criteria. Uses Strategy pattern
     * for search filtering.
     */
    @FXML
    private void handleSearch() {
        String searchTerm = searchField.getText().toLowerCase().trim();
        String statusFilter = filterStatusComboBox.getValue();

        if (shouldShowAllVisits(searchTerm, statusFilter)) {
            loadAllVisits();
        } else {
            performFilteredSearch(searchTerm, statusFilter);
        }
    }

    /**
     * Checks if all visits should be shown based on search criteria.
     *
     * @param searchTerm The search term
     * @param statusFilter The status filter
     * @return true if all visits should be shown, false otherwise
     */
    private boolean shouldShowAllVisits(String searchTerm, String statusFilter) {
        return searchTerm.isEmpty() && "All".equals(statusFilter);
    }

    /**
     * Performs filtered search based on criteria.
     *
     * @param searchTerm The search term
     * @param statusFilter The status filter
     */
    private void performFilteredSearch(String searchTerm, String statusFilter) {
        ObservableList<Visit> filteredList = FXCollections.observableArrayList();

        for (Visit visit : visitService.getAllVisits()) {
            if (matchesSearchCriteria(visit, searchTerm, statusFilter)) {
                filteredList.add(visit);
            }
        }

        visitTable.setItems(filteredList);
    }

    /**
     * Checks if a visit matches search criteria.
     *
     * @param visit The visit to check
     * @param searchTerm The search term
     * @param statusFilter The status filter
     * @return true if visit matches criteria, false otherwise
     */
    private boolean matchesSearchCriteria(Visit visit, String searchTerm, String statusFilter) {
        boolean matchesSearch = searchTerm.isEmpty()
                || visit.getVisitId().toLowerCase().contains(searchTerm)
                || visit.getPrisonerId().toLowerCase().contains(searchTerm)
                || visit.getVisitorId().toLowerCase().contains(searchTerm);

        boolean matchesStatus = "All".equals(statusFilter)
                || visit.getStatus().equals(statusFilter);

        return matchesSearch && matchesStatus;
    }

    /**
     * Handles clear action to reset search and form.
     */
    @FXML
    private void handleClear() {
        clearFormFields();
        visitTable.getSelectionModel().clearSelection();
        searchField.clear();
        filterStatusComboBox.setValue("All");
        loadAllVisits();
    }

    /**
     * Handles start visit action.
     */
    @FXML
    private void handleStartVisit() {
        Visit selectedVisit = visitTable.getSelectionModel().getSelectedItem();
        if (selectedVisit != null && "Scheduled".equals(selectedVisit.getStatus())) {
            try {
                if (visitService.startVisit(selectedVisit.getVisitId())) {
                    showAlert("Success", "Visit started successfully!", Alert.AlertType.INFORMATION);
                    loadAllVisits();
                } else {
                    showAlert("Error", "Failed to start visit!", Alert.AlertType.ERROR);
                }
            } catch (Exception e) {
                handleVisitActionError("starting", e);
            }
        } else {
            showAlert("Warning", "Please select a scheduled visit to start!", Alert.AlertType.WARNING);
        }
    }

    /**
     * Handles complete visit action.
     */
    @FXML
    private void handleCompleteVisit() {
        Visit selectedVisit = visitTable.getSelectionModel().getSelectedItem();
        if (selectedVisit != null && "In Progress".equals(selectedVisit.getStatus())) {
            try {
                if (visitService.completeVisit(selectedVisit.getVisitId())) {
                    showAlert("Success", "Visit completed successfully!", Alert.AlertType.INFORMATION);
                    loadAllVisits();
                } else {
                    showAlert("Error", "Failed to complete visit!", Alert.AlertType.ERROR);
                }
            } catch (Exception e) {
                handleVisitActionError("completing", e);
            }
        } else {
            showAlert("Warning", "Please select a visit in progress to complete!", Alert.AlertType.WARNING);
        }
    }

    /**
     * Handles refresh action.
     */
    @FXML
    private void handleRefresh() {
        loadAllVisits();
        loadPrisonerIds();
        loadVisitorIds();
        showAlert("Success", "Data refreshed successfully!", Alert.AlertType.INFORMATION);
    }

    /**
     * Creates a Visit object from form data.
     *
     * @return A populated Visit object
     */
    private Visit createVisitFromForm() {
        String visitId = visitIdField.getText().trim();
        String prisonerId = extractIdFromComboBox(prisonerComboBox.getValue());
        String visitorId = extractIdFromComboBox(visitorComboBox.getValue());
        LocalDateTime dateTime = createLocalDateTime();
        int duration = Integer.parseInt(durationField.getText().trim());

        Visit newVisit = new Visit(visitId, prisonerId, visitorId, dateTime, duration);
        newVisit.setStatus(statusComboBox.getValue());
        newVisit.setNotes(notesArea.getText().trim());

        return newVisit;
    }

    /**
     * Creates LocalDateTime from date and time fields.
     *
     * @return LocalDateTime object
     */
    private LocalDateTime createLocalDateTime() {
        LocalDate date = visitDatePicker.getValue();
        LocalTime time = LocalTime.parse(visitTimeComboBox.getValue(), TIME_FORMATTER);
        return LocalDateTime.of(date, time);
    }

    /**
     * Updates visit object with form data.
     *
     * @param visit The visit to update
     */
    private void updateVisitFromForm(Visit visit) {
        visit.setPrisonerId(extractIdFromComboBox(prisonerComboBox.getValue()));
        visit.setVisitorId(extractIdFromComboBox(visitorComboBox.getValue()));
        visit.setVisitDateTime(createLocalDateTime());
        visit.setDuration(Integer.parseInt(durationField.getText().trim()));
        visit.setStatus(statusComboBox.getValue());
        visit.setNotes(notesArea.getText().trim());
    }

    /**
     * Validates form input data.
     *
     * @return true if form is valid, false otherwise
     */
    private boolean validateVisitInput() {
        return validateVisitId()
                && validatePrisonerSelection()
                && validateVisitorSelection()
                && validateDate()
                && validateTime()
                && validateDuration();
    }

    /**
     * Validates visit ID field.
     *
     * @return true if valid, false otherwise
     */
    private boolean validateVisitId() {
        if (visitIdField.getText().trim().isEmpty()) {
            showAlert("Validation Error", "Visit ID is required!", Alert.AlertType.ERROR);
            return false;
        }
        return true;
    }

    /**
     * Validates prisoner selection.
     *
     * @return true if valid, false otherwise
     */
    private boolean validatePrisonerSelection() {
        if (prisonerComboBox.getValue() == null || prisonerComboBox.getValue().isEmpty()) {
            showAlert("Validation Error", "Please select a prisoner!", Alert.AlertType.ERROR);
            return false;
        }
        return true;
    }

    /**
     * Validates visitor selection.
     *
     * @return true if valid, false otherwise
     */
    private boolean validateVisitorSelection() {
        if (visitorComboBox.getValue() == null || visitorComboBox.getValue().isEmpty()) {
            showAlert("Validation Error", "Please select a visitor!", Alert.AlertType.ERROR);
            return false;
        }
        return true;
    }

    /**
     * Validates date selection.
     *
     * @return true if valid, false otherwise
     */
    private boolean validateDate() {
        if (visitDatePicker.getValue() == null) {
            showAlert("Validation Error", "Please select a date!", Alert.AlertType.ERROR);
            return false;
        }
        return true;
    }

    /**
     * Validates time selection.
     *
     * @return true if valid, false otherwise
     */
    private boolean validateTime() {
        if (visitTimeComboBox.getValue() == null || visitTimeComboBox.getValue().isEmpty()) {
            showAlert("Validation Error", "Please select a time!", Alert.AlertType.ERROR);
            return false;
        }
        return true;
    }

    /**
     * Validates duration field.
     *
     * @return true if valid, false otherwise
     */
    private boolean validateDuration() {
        try {
            int duration = Integer.parseInt(durationField.getText().trim());
            if (duration <= 0 || duration > 120) {
                showAlert("Validation Error", "Duration must be between 1 and 120 minutes!", Alert.AlertType.ERROR);
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert("Validation Error", "Duration must be a valid number!", Alert.AlertType.ERROR);
            return false;
        }
        return true;
    }

    /**
     * Extracts ID from combo box value.
     *
     * @param comboValue The combo box value
     * @return Extracted ID
     */
    private String extractIdFromComboBox(String comboValue) {
        if (comboValue != null && comboValue.contains(" - ")) {
            return comboValue.split(" - ")[0];
        }
        return comboValue;
    }

    /**
     * Clears all form fields.
     */
    private void clearFormFields() {
        visitIdField.clear();
        prisonerComboBox.setValue(null);
        visitorComboBox.setValue(null);
        visitDatePicker.setValue(LocalDate.now());
        visitTimeComboBox.setValue("09:00");
        durationField.clear();
        statusComboBox.setValue("Scheduled");
        notesArea.clear();
    }

    /**
     * Handles visit action errors.
     *
     * @param action The action being performed
     * @param e The exception that occurred
     */
    private void handleVisitActionError(String action, Exception e) {
        showAlert("Error", "Error " + action + " visit: " + e.getMessage(), Alert.AlertType.ERROR);
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
