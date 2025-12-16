package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Visitor;
import model.Prisoner;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import service.VisitorService;
import service.PrisonerService;

/**
 * Controller for Visitor Management interface. Implements Initializable for
 * JavaFX initialization. Uses Singleton pattern for service instances and
 * Strategy pattern for search filtering.
 */
public class VisitorManagementController implements Initializable {

    // Table UI components
    @FXML
    private TableView<Visitor> visitorTable;
    @FXML
    private TableColumn<Visitor, String> visitorIdColumn;
    @FXML
    private TableColumn<Visitor, String> nameColumn;
    @FXML
    private TableColumn<Visitor, String> phoneColumn;
    @FXML
    private TableColumn<Visitor, String> relationshipColumn;
    @FXML
    private TableColumn<Visitor, String> prisonerIdColumn;
    @FXML
    private TableColumn<Visitor, String> statusColumn;
    @FXML
    private TableColumn<Visitor, Integer> visitCountColumn;
    @FXML
    private TableColumn<Visitor, String> lastVisitColumn;

    // Form UI components
    @FXML
    private TextField visitorIdField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField phoneField;
    @FXML
    private ComboBox<String> relationshipComboBox;
    @FXML
    private ComboBox<String> prisonerComboBox;
    @FXML
    private DatePicker registrationDatePicker;
    @FXML
    private ComboBox<String> statusComboBox;
    @FXML
    private TextArea notesArea;

    // Search UI components
    @FXML
    private TextField searchField;
    @FXML
    private ComboBox<String> filterStatusComboBox;
    @FXML
    private ComboBox<String> filterPrisonerComboBox;

    // Service instances (Singleton pattern)
    private VisitorService visitorService;
    private PrisonerService prisonerService;

    // Data collections
    private ObservableList<Visitor> visitorList;
    private ObservableList<String> prisonerList;

    // Date formatter
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

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
        loadAllData();
        setupComboBoxes();
        setupTableSelectionListener();
    }

    /**
     * Initializes service instances using Singleton pattern.
     */
    private void initializeServices() {
        visitorService = VisitorService.getInstance();
        prisonerService = PrisonerService.getInstance();
    }

    /**
     * Initializes data structures for the controller.
     */
    private void initializeDataStructures() {
        visitorList = FXCollections.observableArrayList();
        prisonerList = FXCollections.observableArrayList();
    }

    /**
     * Sets up table columns with cell value factories.
     */
    private void setupTableColumns() {
        visitorIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        relationshipColumn.setCellValueFactory(new PropertyValueFactory<>("relationship"));
        prisonerIdColumn.setCellValueFactory(new PropertyValueFactory<>("prisonerId"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        visitCountColumn.setCellValueFactory(new PropertyValueFactory<>("visitCount"));

        lastVisitColumn.setCellValueFactory(cellData -> {
            Visitor visitor = cellData.getValue();
            return formatLastVisitDate(visitor.getLastVisitDate());
        });
    }

    /**
     * Formats last visit date for display.
     *
     * @param lastVisitDate The last visit date string
     * @return Formatted date or "Never" if null/empty
     */
    private javafx.beans.property.SimpleStringProperty formatLastVisitDate(String lastVisitDate) {
        if (lastVisitDate == null || lastVisitDate.isEmpty()) {
            return new javafx.beans.property.SimpleStringProperty("Never");
        }
        return new javafx.beans.property.SimpleStringProperty(lastVisitDate);
    }

    /**
     * Loads all visitor and prisoner data.
     */
    private void loadAllData() {
        loadVisitors();
        loadPrisoners();
    }

    /**
     * Loads visitors from the service.
     */
    private void loadVisitors() {
        visitorList.clear();
        visitorList.addAll(visitorService.getAllVisitors());
        visitorTable.setItems(visitorList);
    }

    /**
     * Loads prisoners for combo boxes.
     */
    private void loadPrisoners() {
        prisonerList.clear();
        prisonerList.add("Select Prisoner");

        for (Prisoner prisoner : prisonerService.getAllPrisoners()) {
            if ("In Custody".equals(prisoner.getStatus())) {
                prisonerList.add(prisoner.getId() + " - " + prisoner.getName());
            }
        }
    }

    /**
     * Sets up all combo boxes with appropriate options.
     */
    private void setupComboBoxes() {
        setupRelationshipComboBox();
        setupPrisonerComboBox();
        setupStatusComboBox();
        setupFilterComboBoxes();
        setDefaultRegistrationDate();
    }

    /**
     * Sets up relationship combo box.
     */
    private void setupRelationshipComboBox() {
        ObservableList<String> relationshipOptions = FXCollections.observableArrayList(
                "Family", "Friend", "Lawyer", "Relative", "Other"
        );
        relationshipComboBox.setItems(relationshipOptions);
        relationshipComboBox.getSelectionModel().selectFirst();
    }

    /**
     * Sets up prisoner combo box.
     */
    private void setupPrisonerComboBox() {
        prisonerComboBox.setItems(prisonerList);
        prisonerComboBox.getSelectionModel().selectFirst();
    }

    /**
     * Sets up status combo box.
     */
    private void setupStatusComboBox() {
        ObservableList<String> statusOptions = FXCollections.observableArrayList(
                "Pending", "Approved", "Banned"
        );
        statusComboBox.setItems(statusOptions);
        statusComboBox.getSelectionModel().selectFirst();
    }

    /**
     * Sets up filter combo boxes.
     */
    private void setupFilterComboBoxes() {
        setupFilterStatusComboBox();
        setupFilterPrisonerComboBox();
    }

    /**
     * Sets up status filter combo box.
     */
    private void setupFilterStatusComboBox() {
        ObservableList<String> filterStatusOptions = FXCollections.observableArrayList(
                "All", "Pending", "Approved", "Banned"
        );
        filterStatusComboBox.setItems(filterStatusOptions);
        filterStatusComboBox.getSelectionModel().selectFirst();
    }

    /**
     * Sets up prisoner filter combo box.
     */
    private void setupFilterPrisonerComboBox() {
        ObservableList<String> filterPrisonerOptions = FXCollections.observableArrayList();
        filterPrisonerOptions.add("All Prisoners");

        for (Prisoner prisoner : prisonerService.getAllPrisoners()) {
            if ("In Custody".equals(prisoner.getStatus())) {
                filterPrisonerOptions.add(prisoner.getId());
            }
        }

        filterPrisonerComboBox.setItems(filterPrisonerOptions);
        filterPrisonerComboBox.getSelectionModel().selectFirst();
    }

    /**
     * Sets default registration date to today.
     */
    private void setDefaultRegistrationDate() {
        registrationDatePicker.setValue(LocalDate.now());
    }

    /**
     * Sets up table selection listener to display visitor details when
     * selected.
     */
    private void setupTableSelectionListener() {
        visitorTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        displayVisitorDetails(newValue);
                    }
                });
    }

    /**
     * Displays visitor details in the form.
     *
     * @param visitor The visitor to display
     */
    private void displayVisitorDetails(Visitor visitor) {
        visitorIdField.setText(visitor.getId());
        nameField.setText(visitor.getName());
        phoneField.setText(visitor.getPhone());
        relationshipComboBox.setValue(visitor.getRelationship());
        prisonerComboBox.setValue(findPrisonerDisplayValue(visitor.getPrisonerId()));
        statusComboBox.setValue(visitor.getStatus());
    }

    /**
     * Finds prisoner display value from prisoner list.
     *
     * @param prisonerId The prisoner ID to find
     * @return Display value or prisoner ID if not found
     */
    private String findPrisonerDisplayValue(String prisonerId) {
        for (String prisoner : prisonerList) {
            if (prisoner.contains(prisonerId)) {
                return prisoner;
            }
        }
        return prisonerId;
    }

    // FXML Action Handlers
    /**
     * Handles add visitor action.
     */
    @FXML
    private void handleAddVisitor() {
        if (validateInput()) {
            createAndAddVisitor();
        }
    }

    /**
     * Creates and adds a new visitor from form data.
     */
    private void createAndAddVisitor() {
        try {
            Visitor newVisitor = createVisitorFromForm();

            if (visitorService.addVisitor(newVisitor)) {
                showAlert("Success", "Visitor added successfully!", Alert.AlertType.INFORMATION);
                loadAllData();
                clearForm();
            } else {
                showAlert("Error", "Failed to add visitor!", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            handleVisitorActionError("adding", e);
        }
    }

    /**
     * Handles update visitor action.
     */
    @FXML
    private void handleUpdateVisitor() {
        Visitor selectedVisitor = visitorTable.getSelectionModel().getSelectedItem();
        if (selectedVisitor == null) {
            showAlert("Warning", "Please select a visitor to update!", Alert.AlertType.WARNING);
            return;
        }

        if (validateInput()) {
            updateSelectedVisitor(selectedVisitor);
        }
    }

    /**
     * Updates the selected visitor with form data.
     *
     * @param visitor The visitor to update
     */
    private void updateSelectedVisitor(Visitor visitor) {
        try {
            updateVisitorFromForm(visitor);

            if (visitorService.updateVisitor(visitor)) {
                showAlert("Success", "Visitor updated successfully!", Alert.AlertType.INFORMATION);
                loadAllData();
            } else {
                showAlert("Error", "Failed to update visitor!", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            handleVisitorActionError("updating", e);
        }
    }

    /**
     * Handles delete visitor action.
     */
    @FXML
    private void handleDeleteVisitor() {
        Visitor selectedVisitor = visitorTable.getSelectionModel().getSelectedItem();
        if (selectedVisitor == null) {
            showAlert("Warning", "Please select a visitor to delete!", Alert.AlertType.WARNING);
            return;
        }

        confirmAndDeleteVisitor(selectedVisitor);
    }

    /**
     * Shows confirmation dialog and deletes visitor if confirmed.
     *
     * @param visitor The visitor to delete
     */
    private void confirmAndDeleteVisitor(Visitor visitor) {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Delete");
        confirmAlert.setHeaderText("Delete Visitor");
        confirmAlert.setContentText("Are you sure you want to delete visitor " + visitor.getId() + "?");

        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if (visitorService.deleteVisitor(visitor.getId())) {
                    showAlert("Success", "Visitor deleted successfully!", Alert.AlertType.INFORMATION);
                    loadAllData();
                    clearForm();
                } else {
                    showAlert("Error", "Failed to delete visitor!", Alert.AlertType.ERROR);
                }
            }
        });
    }

    /**
     * Handles search functionality with multiple filter criteria. Uses Strategy
     * pattern for filtering logic.
     */
    @FXML
    private void handleSearch() {
        String searchTerm = searchField.getText().toLowerCase().trim();
        String statusFilter = filterStatusComboBox.getValue();
        String prisonerFilter = filterPrisonerComboBox.getValue();

        ObservableList<Visitor> filteredList = FXCollections.observableArrayList();

        for (Visitor visitor : visitorService.getAllVisitors()) {
            if (matchesSearchCriteria(visitor, searchTerm, statusFilter, prisonerFilter)) {
                filteredList.add(visitor);
            }
        }

        visitorTable.setItems(filteredList);
    }

    /**
     * Checks if a visitor matches all search criteria. Implements Strategy
     * pattern for filtering logic.
     *
     * @param visitor The visitor to check
     * @param searchTerm The search term
     * @param statusFilter Status filter
     * @param prisonerFilter Prisoner filter
     * @return true if visitor matches all criteria, false otherwise
     */
    private boolean matchesSearchCriteria(Visitor visitor, String searchTerm,
            String statusFilter, String prisonerFilter) {
        return matchesSearchTerm(visitor, searchTerm)
                && matchesStatusFilter(visitor, statusFilter)
                && matchesPrisonerFilter(visitor, prisonerFilter);
    }

    /**
     * Checks if visitor matches search term.
     */
    private boolean matchesSearchTerm(Visitor visitor, String searchTerm) {
        if (searchTerm.isEmpty()) {
            return true;
        }
        return visitor.getId().toLowerCase().contains(searchTerm)
                || visitor.getName().toLowerCase().contains(searchTerm)
                || visitor.getPhone().toLowerCase().contains(searchTerm)
                || visitor.getRelationship().toLowerCase().contains(searchTerm);
    }

    /**
     * Checks if visitor matches status filter.
     */
    private boolean matchesStatusFilter(Visitor visitor, String statusFilter) {
        return "All".equals(statusFilter)
                || visitor.getStatus().equals(statusFilter);
    }

    /**
     * Checks if visitor matches prisoner filter.
     */
    private boolean matchesPrisonerFilter(Visitor visitor, String prisonerFilter) {
        return "All Prisoners".equals(prisonerFilter)
                || visitor.getPrisonerId().equals(prisonerFilter);
    }

    /**
     * Handles clear action to reset search and form.
     */
    @FXML
    private void handleClear() {
        resetSearchFilters();
        clearForm();
        visitorTable.getSelectionModel().clearSelection();
        loadAllData();
    }

    /**
     * Resets all search filter components to their default values.
     */
    private void resetSearchFilters() {
        searchField.clear();
        filterStatusComboBox.getSelectionModel().selectFirst();
        filterPrisonerComboBox.getSelectionModel().selectFirst();
    }

    /**
     * Handles approve visitor action.
     */
    @FXML
    private void handleApproveVisitor() {
        Visitor selectedVisitor = visitorTable.getSelectionModel().getSelectedItem();
        if (selectedVisitor != null && "Pending".equals(selectedVisitor.getStatus())) {
            if (visitorService.approveVisitor(selectedVisitor.getId())) {
                selectedVisitor.approveVisitor();
                showAlert("Success", "Visitor approved successfully!", Alert.AlertType.INFORMATION);
                loadAllData();
            } else {
                showAlert("Error", "Failed to approve visitor!", Alert.AlertType.ERROR);
            }
        } else {
            showAlert("Warning", "Please select a pending visitor to approve!", Alert.AlertType.WARNING);
        }
    }

    /**
     * Handles ban visitor action.
     */
    @FXML
    private void handleBanVisitor() {
        Visitor selectedVisitor = visitorTable.getSelectionModel().getSelectedItem();
        if (selectedVisitor != null && !"Banned".equals(selectedVisitor.getStatus())) {
            if (visitorService.banVisitor(selectedVisitor.getId())) {
                selectedVisitor.banVisitor();
                showAlert("Success", "Visitor banned successfully!", Alert.AlertType.INFORMATION);
                loadAllData();
            } else {
                showAlert("Error", "Failed to ban visitor!", Alert.AlertType.ERROR);
            }
        } else {
            showAlert("Warning", "Please select a visitor (not already banned) to ban!", Alert.AlertType.WARNING);
        }
    }

    /**
     * Handles unban visitor action.
     */
    @FXML
    private void handleUnbanVisitor() {
        Visitor selectedVisitor = visitorTable.getSelectionModel().getSelectedItem();
        if (selectedVisitor != null && "Banned".equals(selectedVisitor.getStatus())) {
            selectedVisitor.setStatus("Pending");
            if (visitorService.updateVisitor(selectedVisitor)) {
                showAlert("Success", "Visitor unbanned successfully!", Alert.AlertType.INFORMATION);
                loadAllData();
            } else {
                showAlert("Error", "Failed to unban visitor!", Alert.AlertType.ERROR);
            }
        } else {
            showAlert("Warning", "Please select a banned visitor to unban!", Alert.AlertType.WARNING);
        }
    }

    /**
     * Handles view details action.
     */
    @FXML
    private void handleViewDetails() {
        Visitor selectedVisitor = visitorTable.getSelectionModel().getSelectedItem();
        if (selectedVisitor != null) {
            Alert detailsAlert = new Alert(Alert.AlertType.INFORMATION);
            detailsAlert.setTitle("Visitor Details");
            detailsAlert.setHeaderText("Details for " + selectedVisitor.getName());
            detailsAlert.setContentText(selectedVisitor.getVisitorDetails());
            detailsAlert.showAndWait();
        } else {
            showAlert("Warning", "Please select a visitor to view details!", Alert.AlertType.WARNING);
        }
    }

    /**
     * Handles view visit statistics action.
     */
    @FXML
    private void handleViewVisitStatistics() {
        Visitor selectedVisitor = visitorTable.getSelectionModel().getSelectedItem();
        if (selectedVisitor != null) {
            Alert statsAlert = new Alert(Alert.AlertType.INFORMATION);
            statsAlert.setTitle("Visit Statistics");
            statsAlert.setHeaderText("Visit Statistics for " + selectedVisitor.getName());
            statsAlert.setContentText(selectedVisitor.getVisitStatistics());
            statsAlert.showAndWait();
        } else {
            showAlert("Warning", "Please select a visitor to view visit statistics!", Alert.AlertType.WARNING);
        }
    }

    /**
     * Handles refresh action.
     */
    @FXML
    private void handleRefresh() {
        loadAllData();
        loadPrisoners();
        showAlert("Success", "Data refreshed successfully!", Alert.AlertType.INFORMATION);
    }

    /**
     * Creates a Visitor object from form data. Uses Factory pattern for visitor
     * creation.
     *
     * @return A populated Visitor object
     */
    private Visitor createVisitorFromForm() {
        String visitorId = visitorIdField.getText().trim();
        String name = nameField.getText().trim();
        String phone = phoneField.getText().trim();
        String relationship = relationshipComboBox.getValue();
        String prisonerId = extractPrisonerId(prisonerComboBox.getValue());
        String status = statusComboBox.getValue();

        Visitor newVisitor = new Visitor(visitorId, name, phone, relationship, prisonerId);
        newVisitor.setStatus(status);

        return newVisitor;
    }

    /**
     * Updates visitor object with form data.
     *
     * @param visitor The visitor to update
     */
    private void updateVisitorFromForm(Visitor visitor) {
        visitor.setName(nameField.getText().trim());
        visitor.setPhone(phoneField.getText().trim());
        visitor.setRelationship(relationshipComboBox.getValue());
        visitor.setPrisonerId(extractPrisonerId(prisonerComboBox.getValue()));
        visitor.setStatus(statusComboBox.getValue());
    }

    /**
     * Validates form input data.
     *
     * @return true if form is valid, false otherwise
     */
    private boolean validateInput() {
        return validateVisitorId()
                && validateName()
                && validatePhone()
                && validatePrisonerSelection();
    }

    /**
     * Validates visitor ID field.
     *
     * @return true if valid, false otherwise
     */
    private boolean validateVisitorId() {
        if (visitorIdField.getText().trim().isEmpty()) {
            showAlert("Validation Error", "Visitor ID is required!", Alert.AlertType.ERROR);
            visitorIdField.requestFocus();
            return false;
        }
        return true;
    }

    /**
     * Validates name field.
     *
     * @return true if valid, false otherwise
     */
    private boolean validateName() {
        if (nameField.getText().trim().isEmpty()) {
            showAlert("Validation Error", "Name is required!", Alert.AlertType.ERROR);
            nameField.requestFocus();
            return false;
        }
        return true;
    }

    /**
     * Validates phone field.
     *
     * @return true if valid, false otherwise
     */
    private boolean validatePhone() {
        String phone = phoneField.getText().trim();
        if (!phone.isEmpty() && !phone.matches("\\d{10,15}")) {
            showAlert("Validation Error", "Phone number must be 10-15 digits!", Alert.AlertType.ERROR);
            phoneField.requestFocus();
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
        if (prisonerComboBox.getValue() == null || "Select Prisoner".equals(prisonerComboBox.getValue())) {
            showAlert("Validation Error", "Please select a prisoner!", Alert.AlertType.ERROR);
            prisonerComboBox.requestFocus();
            return false;
        }
        return true;
    }

    /**
     * Extracts prisoner ID from combo box value.
     *
     * @param comboValue The combo box value
     * @return Extracted prisoner ID
     */
    private String extractPrisonerId(String comboValue) {
        if (comboValue != null && comboValue.contains(" - ")) {
            return comboValue.split(" - ")[0];
        }
        return comboValue;
    }

    /**
     * Clears all form fields.
     */
    private void clearForm() {
        visitorIdField.clear();
        nameField.clear();
        phoneField.clear();
        relationshipComboBox.getSelectionModel().selectFirst();
        prisonerComboBox.getSelectionModel().selectFirst();
        registrationDatePicker.setValue(LocalDate.now());
        statusComboBox.getSelectionModel().selectFirst();
        notesArea.clear();
    }

    /**
     * Handles visitor action errors.
     *
     * @param action The action being performed
     * @param e The exception that occurred
     */
    private void handleVisitorActionError(String action, Exception e) {
        showAlert("Error", "Error " + action + " visitor: " + e.getMessage(), Alert.AlertType.ERROR);
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
