package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.PrisonCell;
import model.Employee;
import model.Prisoner;
import service.PrisonCellService;
import service.PrisonerService;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for PrisonCell Management Interface. Implements Initializable for
 * JavaFX initialization. Uses Singleton pattern for service instances and
 * Strategy pattern for search filtering.
 */
public class CellManagementController implements Initializable {

    // Table components
    @FXML
    private TableView<PrisonCell> cellTable;
    @FXML
    private TableColumn<PrisonCell, String> cellNumberColumn;
    @FXML
    private TableColumn<PrisonCell, String> cellTypeColumn;
    @FXML
    private TableColumn<PrisonCell, Integer> capacityColumn;
    @FXML
    private TableColumn<PrisonCell, Integer> occupancyColumn;
    @FXML
    private TableColumn<PrisonCell, String> occupancyRateColumn;
    @FXML
    private TableColumn<PrisonCell, String> securityLevelColumn;
    @FXML
    private TableColumn<PrisonCell, String> statusColumn;

    // Form components
    @FXML
    private TextField cellNumberField;
    @FXML
    private ComboBox<String> cellTypeComboBox;
    @FXML
    private TextField capacityField;
    @FXML
    private TextField occupancyField;
    @FXML
    private ComboBox<String> securityLevelComboBox;
    @FXML
    private ComboBox<String> statusComboBox;
    @FXML
    private Label availableSpaceLabel;
    @FXML
    private Label occupancyRateLabel;
    @FXML
    private ListView<String> prisonersListView;
    @FXML
    private TextArea cellDetailsArea;

    // Search and filter components
    @FXML
    private TextField searchField;
    @FXML
    private ComboBox<String> filterSecurityComboBox;
    @FXML
    private ComboBox<String> filterStatusComboBox;
    @FXML
    private ComboBox<String> filterAvailabilityComboBox;

    // Statistics labels
    @FXML
    private Label totalPrisonCellsLabel;
    @FXML
    private Label totalCapacityLabel;
    @FXML
    private Label totalOccupancyLabel;
    @FXML
    private Label overallOccupancyRateLabel;

    // Service instances (Singleton pattern)
    private PrisonCellService cellService;
    private PrisonerService prisonerService;

    // Data models
    private Employee currentEmployee;
    private ObservableList<PrisonCell> cellList;
    private ObservableList<String> prisonerList;

    /**
     * Initializes the controller class. Called automatically after FXML
     * loading.
     *
     * @param location The location used to resolve relative paths
     * @param resources The resources used to localize the root object
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeServices();
        initializeDataStructures();
        setupUIComponents();
        loadAllData();
    }

    /**
     * Initializes service instances using Singleton pattern.
     */
    private void initializeServices() {
        cellService = PrisonCellService.getInstance();
        prisonerService = PrisonerService.getInstance();
    }

    /**
     * Initializes data structures for the controller.
     */
    private void initializeDataStructures() {
        cellList = FXCollections.observableArrayList();
        prisonerList = FXCollections.observableArrayList();
    }

    /**
     * Sets up all UI components including table columns, combo boxes, and
     * listeners.
     */
    private void setupUIComponents() {
        setupTableColumns();
        setupComboBoxes();
        setupTableSelectionListener();
    }

    /**
     * Sets current employee for permission-based access control.
     *
     * @param employee The employee object representing the current user
     */
    public void setCurrentEmployee(Employee employee) {
        this.currentEmployee = employee;
        updateUIBasedOnPermissions();
    }

    /**
     * Updates UI components based on user permissions.
     */
    private void updateUIBasedOnPermissions() {
        if (currentEmployee != null) {
            boolean isAdmin = currentEmployee.isAdministrator();
            boolean isOfficer = currentEmployee.isOfficer();
            // Permission-based UI adjustments can be implemented here
        }
    }

    /**
     * Configures table columns with appropriate cell value factories.
     */
    private void setupTableColumns() {
        cellNumberColumn.setCellValueFactory(new PropertyValueFactory<>("prisonCellNumber"));
        cellTypeColumn.setCellValueFactory(new PropertyValueFactory<>("prisonCellType"));
        capacityColumn.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        occupancyColumn.setCellValueFactory(new PropertyValueFactory<>("currentOccupancy"));

        // Custom cell value factory for occupancy rate percentage
        occupancyRateColumn.setCellValueFactory(cellData -> {
            PrisonCell cell = cellData.getValue();
            double rate = cell.getCapacity() > 0
                    ? (double) cell.getCurrentOccupancy() / cell.getCapacity() * 100 : 0;
            return new javafx.beans.property.SimpleStringProperty(String.format("%.1f%%", rate));
        });

        securityLevelColumn.setCellValueFactory(new PropertyValueFactory<>("securityLevel"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    /**
     * Loads all data including cells, prisoners, and statistics.
     */
    private void loadAllData() {
        loadCells();
        loadPrisoners();
        loadStatistics();
    }

    /**
     * Loads prison cells from the service and populates the table.
     */
    private void loadCells() {
        cellList.clear();
        cellList.addAll(cellService.getAllPrisonCells());
        cellTable.setItems(cellList);
    }

    /**
     * Loads prisoners from the service and populates the list view.
     */
    private void loadPrisoners() {
        prisonerList.clear();
        for (Prisoner prisoner : prisonerService.getAllPrisoners()) {
            prisonerList.add(prisoner.getId() + " - " + prisoner.getName()
                    + " (PrisonCell: " + prisoner.getPrisonCellNumber() + ")");
        }
        prisonersListView.setItems(prisonerList);
    }

    /**
     * Loads and displays statistical data.
     */
    private void loadStatistics() {
        int[] stats = cellService.getOccupancyStatistics();

        totalPrisonCellsLabel.setText(String.valueOf(cellService.getTotalPrisonCells()));
        totalCapacityLabel.setText(String.valueOf(stats[0]));
        totalOccupancyLabel.setText(String.valueOf(stats[1]));

        if (stats[0] > 0) {
            double occupancyRate = (double) stats[1] / stats[0] * 100;
            overallOccupancyRateLabel.setText(String.format("%.1f%%", occupancyRate));
        } else {
            overallOccupancyRateLabel.setText("0%");
        }
    }

    /**
     * Sets up all combo boxes with appropriate options.
     */
    private void setupComboBoxes() {
        setupCellTypeComboBox();
        setupSecurityLevelComboBox();
        setupStatusComboBox();
        setupFilterComboBoxes();
    }

    /**
     * Sets up the cell type combo box with predefined options.
     */
    private void setupCellTypeComboBox() {
        ObservableList<String> cellTypeOptions = FXCollections.observableArrayList(
                "Single", "Double", "General", "Solitary", "Medical", "Maximum Security"
        );
        cellTypeComboBox.setItems(cellTypeOptions);
        cellTypeComboBox.getSelectionModel().selectFirst();
    }

    /**
     * Sets up the security level combo box with predefined options.
     */
    private void setupSecurityLevelComboBox() {
        ObservableList<String> securityOptions = FXCollections.observableArrayList(
                "Minimum", "Medium", "Maximum", "Super Maximum"
        );
        securityLevelComboBox.setItems(securityOptions);
        securityLevelComboBox.getSelectionModel().selectFirst();
    }

    /**
     * Sets up the status combo box with predefined options.
     */
    private void setupStatusComboBox() {
        ObservableList<String> statusOptions = FXCollections.observableArrayList(
                "Occupied", "Vacant", "Full", "Under Maintenance", "Cleaning", "Closed"
        );
        statusComboBox.setItems(statusOptions);
        statusComboBox.getSelectionModel().selectFirst();
    }

    /**
     * Sets up filter combo boxes for search functionality.
     */
    private void setupFilterComboBoxes() {
        setupFilterSecurityComboBox();
        setupFilterStatusComboBox();
        setupFilterAvailabilityComboBox();
    }

    /**
     * Sets up security filter combo box.
     */
    private void setupFilterSecurityComboBox() {
        ObservableList<String> filterSecurityOptions = FXCollections.observableArrayList();
        filterSecurityOptions.add("All Security Levels");
        filterSecurityOptions.addAll(securityLevelComboBox.getItems());
        filterSecurityComboBox.setItems(filterSecurityOptions);
        filterSecurityComboBox.getSelectionModel().selectFirst();
    }

    /**
     * Sets up status filter combo box.
     */
    private void setupFilterStatusComboBox() {
        ObservableList<String> filterStatusOptions = FXCollections.observableArrayList();
        filterStatusOptions.add("All Status");
        filterStatusOptions.addAll(statusComboBox.getItems());
        filterStatusComboBox.setItems(filterStatusOptions);
        filterStatusComboBox.getSelectionModel().selectFirst();
    }

    /**
     * Sets up availability filter combo box.
     */
    private void setupFilterAvailabilityComboBox() {
        ObservableList<String> filterAvailabilityOptions = FXCollections.observableArrayList(
                "All", "Available", "Full", "Under Maintenance"
        );
        filterAvailabilityComboBox.setItems(filterAvailabilityOptions);
        filterAvailabilityComboBox.getSelectionModel().selectFirst();
    }

    /**
     * Sets up table selection listener to display cell details when selected.
     */
    private void setupTableSelectionListener() {
        cellTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        displayPrisonCellDetails(newValue);
                    }
                });
    }

    /**
     * Displays detailed information for the selected prison cell.
     *
     * @param cell The prison cell to display
     */
    private void displayPrisonCellDetails(PrisonCell cell) {
        populateCellForm(cell);
        calculateAndDisplayCellMetrics(cell);
        displayCellDetails(cell);
        filterPrisonersByCell(cell.getPrisonCellNumber());
    }

    /**
     * Populates the form fields with cell data.
     *
     * @param cell The prison cell with data to populate
     */
    private void populateCellForm(PrisonCell cell) {
        cellNumberField.setText(cell.getPrisonCellNumber());
        cellTypeComboBox.setValue(cell.getPrisonCellType());
        capacityField.setText(String.valueOf(cell.getCapacity()));
        occupancyField.setText(String.valueOf(cell.getCurrentOccupancy()));
        securityLevelComboBox.setValue(cell.getSecurityLevel());
        statusComboBox.setValue(cell.getStatus());
    }

    /**
     * Calculates and displays cell metrics like available space and occupancy
     * rate.
     *
     * @param cell The prison cell to calculate metrics for
     */
    private void calculateAndDisplayCellMetrics(PrisonCell cell) {
        int availableSpace = cell.getAvailableSpace();
        double occupancyRate = cell.getOccupancyPercentage();

        availableSpaceLabel.setText(String.valueOf(availableSpace));
        occupancyRateLabel.setText(String.format("%.1f%%", occupancyRate));
    }

    /**
     * Displays detailed information about the cell.
     *
     * @param cell The prison cell to display details for
     */
    private void displayCellDetails(PrisonCell cell) {
        cellDetailsArea.setText(cell.getPrisonCellDetails());
    }

    /**
     * Filters prisoners list to show only those assigned to a specific cell.
     *
     * @param cellNumber The cell number to filter by
     */
    private void filterPrisonersByCell(String cellNumber) {
        ObservableList<String> filteredPrisoners = FXCollections.observableArrayList();
        for (String prisonerInfo : prisonerList) {
            if (prisonerInfo.contains("PrisonCell: " + cellNumber)) {
                filteredPrisoners.add(prisonerInfo);
            }
        }
        prisonersListView.setItems(filteredPrisoners);
    }

    // FXML Action Handlers
    /**
     * Handles the add cell action.
     */
    @FXML
    private void handleAddPrisonCell() {
        if (validateInput()) {
            createAndAddPrisonCell();
        }
    }

    /**
     * Creates a new prison cell from form data and adds it to the system.
     */
    private void createAndAddPrisonCell() {
        try {
            String prisonCellNumber = cellNumberField.getText().trim();
            String prisonCellType = cellTypeComboBox.getValue();
            int capacity = Integer.parseInt(capacityField.getText().trim());
            String securityLevel = securityLevelComboBox.getValue();

            PrisonCell newPrisonCell = new PrisonCell(prisonCellNumber, prisonCellType, capacity, securityLevel);
            newPrisonCell.setStatus(statusComboBox.getValue());

            if (cellService.addPrisonCell(newPrisonCell)) {
                showAlert("Success", "PrisonCell added successfully!", Alert.AlertType.INFORMATION);
                loadAllData();
                clearForm();
            } else {
                showAlert("Error", "Failed to add cell!", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            showAlert("Error", "Error adding cell: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    /**
     * Handles the update cell action.
     */
    @FXML
    private void handleUpdatePrisonCell() {
        PrisonCell selectedPrisonCell = cellTable.getSelectionModel().getSelectedItem();
        if (selectedPrisonCell == null) {
            showAlert("Warning", "Please select a cell to update!", Alert.AlertType.WARNING);
            return;
        }

        if (validateInput()) {
            updateSelectedPrisonCell(selectedPrisonCell);
        }
    }

    /**
     * Updates the selected prison cell with form data.
     *
     * @param selectedPrisonCell The prison cell to update
     */
    private void updateSelectedPrisonCell(PrisonCell selectedPrisonCell) {
        try {
            updateCellProperties(selectedPrisonCell);
            updateCellOccupancy(selectedPrisonCell);

            if (cellService.updatePrisonCell(selectedPrisonCell)) {
                showAlert("Success", "PrisonCell updated successfully!", Alert.AlertType.INFORMATION);
                loadAllData();
            } else {
                showAlert("Error", "Failed to update cell!", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            showAlert("Error", "Error updating cell: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    /**
     * Updates basic properties of the prison cell.
     *
     * @param cell The prison cell to update
     */
    private void updateCellProperties(PrisonCell cell) {
        cell.setPrisonCellType(cellTypeComboBox.getValue());
        cell.setCapacity(Integer.parseInt(capacityField.getText().trim()));
        cell.setSecurityLevel(securityLevelComboBox.getValue());
        cell.setStatus(statusComboBox.getValue());
    }

    /**
     * Updates occupancy of the prison cell if changed.
     *
     * @param cell The prison cell to update occupancy for
     */
    private void updateCellOccupancy(PrisonCell cell) {
        int newOccupancy = Integer.parseInt(occupancyField.getText().trim());
        if (newOccupancy != cell.getCurrentOccupancy()) {
            if (newOccupancy >= 0 && newOccupancy <= cell.getCapacity()) {
                cell.setCurrentOccupancy(newOccupancy);
            } else {
                showAlert("Error", "Occupancy cannot exceed capacity!", Alert.AlertType.ERROR);
                throw new IllegalArgumentException("Invalid occupancy value");
            }
        }
    }

    /**
     * Handles the delete cell action.
     */
    @FXML
    private void handleDeletePrisonCell() {
        PrisonCell selectedPrisonCell = cellTable.getSelectionModel().getSelectedItem();
        if (selectedPrisonCell == null) {
            showAlert("Warning", "Please select a cell to delete!", Alert.AlertType.WARNING);
            return;
        }

        if (validateCellForDeletion(selectedPrisonCell)) {
            confirmAndDeletePrisonCell(selectedPrisonCell);
        }
    }

    /**
     * Validates if a cell can be deleted.
     *
     * @param cell The prison cell to validate
     * @return true if cell can be deleted, false otherwise
     */
    private boolean validateCellForDeletion(PrisonCell cell) {
        if (cell.getCurrentOccupancy() > 0) {
            showAlert("Error", "Cannot delete cell with prisoners! Transfer prisoners first.", Alert.AlertType.ERROR);
            return false;
        }
        return true;
    }

    /**
     * Shows confirmation dialog and deletes the prison cell if confirmed.
     *
     * @param cell The prison cell to delete
     */
    private void confirmAndDeletePrisonCell(PrisonCell cell) {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Delete");
        confirmAlert.setHeaderText("Delete PrisonCell");
        confirmAlert.setContentText("Are you sure you want to delete cell " + cell.getPrisonCellNumber() + "?");

        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if (cellService.deletePrisonCell(cell.getPrisonCellNumber())) {
                    showAlert("Success", "PrisonCell deleted successfully!", Alert.AlertType.INFORMATION);
                    loadAllData();
                    clearForm();
                } else {
                    showAlert("Error", "Failed to delete cell!", Alert.AlertType.ERROR);
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
        String securityFilter = filterSecurityComboBox.getValue();
        String statusFilter = filterStatusComboBox.getValue();
        String availabilityFilter = filterAvailabilityComboBox.getValue();

        ObservableList<PrisonCell> filteredList = FXCollections.observableArrayList();

        for (PrisonCell cell : cellService.getAllPrisonCells()) {
            if (matchesSearchCriteria(cell, searchTerm, securityFilter, statusFilter, availabilityFilter)) {
                filteredList.add(cell);
            }
        }

        cellTable.setItems(filteredList);
    }

    /**
     * Checks if a cell matches all search criteria. Implements Strategy pattern
     * for filtering logic.
     *
     * @param cell The prison cell to check
     * @param searchTerm The search term
     * @param securityFilter Security level filter
     * @param statusFilter Status filter
     * @param availabilityFilter Availability filter
     * @return true if cell matches all criteria, false otherwise
     */
    private boolean matchesSearchCriteria(PrisonCell cell, String searchTerm,
            String securityFilter, String statusFilter,
            String availabilityFilter) {
        return matchesSearchTerm(cell, searchTerm)
                && matchesSecurityFilter(cell, securityFilter)
                && matchesStatusFilter(cell, statusFilter)
                && matchesAvailabilityFilter(cell, availabilityFilter);
    }

    /**
     * Checks if cell matches search term.
     */
    private boolean matchesSearchTerm(PrisonCell cell, String searchTerm) {
        if (searchTerm.isEmpty()) {
            return true;
        }
        return cell.getPrisonCellNumber().toLowerCase().contains(searchTerm)
                || cell.getPrisonCellType().toLowerCase().contains(searchTerm)
                || cell.getSecurityLevel().toLowerCase().contains(searchTerm);
    }

    /**
     * Checks if cell matches security filter.
     */
    private boolean matchesSecurityFilter(PrisonCell cell, String securityFilter) {
        return "All Security Levels".equals(securityFilter)
                || cell.getSecurityLevel().equals(securityFilter);
    }

    /**
     * Checks if cell matches status filter.
     */
    private boolean matchesStatusFilter(PrisonCell cell, String statusFilter) {
        return "All Status".equals(statusFilter)
                || cell.getStatus().equals(statusFilter);
    }

    /**
     * Checks if cell matches availability filter.
     */
    private boolean matchesAvailabilityFilter(PrisonCell cell, String availabilityFilter) {
        switch (availabilityFilter) {
            case "All":
                return true;
            case "Available":
                return cell.hasAvailableSpace() && !"Under Maintenance".equals(cell.getStatus());
            case "Full":
                return cell.getCurrentOccupancy() >= cell.getCapacity();
            case "Under Maintenance":
                return "Under Maintenance".equals(cell.getStatus());
            default:
                return true;
        }
    }

    /**
     * Handles clear action to reset search and form.
     */
    @FXML
    private void handleClear() {
        resetSearchFilters();
        clearForm();
        cellTable.getSelectionModel().clearSelection();
        loadAllData();
    }

    /**
     * Resets all search filter components to their default values.
     */
    private void resetSearchFilters() {
        searchField.clear();
        filterSecurityComboBox.getSelectionModel().selectFirst();
        filterStatusComboBox.getSelectionModel().selectFirst();
        filterAvailabilityComboBox.getSelectionModel().selectFirst();
    }

    /**
     * Handles setting a cell under maintenance.
     */
    @FXML
    private void handleMaintenance() {
        PrisonCell selectedPrisonCell = cellTable.getSelectionModel().getSelectedItem();
        if (selectedPrisonCell != null && !"Under Maintenance".equals(selectedPrisonCell.getStatus())) {
            if (selectedPrisonCell.getCurrentOccupancy() > 0) {
                showAlert("Warning", "Cannot put occupied cell under maintenance! Transfer prisoners first.", Alert.AlertType.WARNING);
                return;
            }

            if (cellService.setPrisonCellUnderMaintenance(selectedPrisonCell.getPrisonCellNumber())) {
                selectedPrisonCell.setUnderMaintenance();
                showAlert("Success", "PrisonCell set under maintenance successfully!", Alert.AlertType.INFORMATION);
                loadAllData();
            } else {
                showAlert("Error", "Failed to set cell under maintenance!", Alert.AlertType.ERROR);
            }
        } else {
            showAlert("Warning", "Please select a cell (not already under maintenance)!", Alert.AlertType.WARNING);
        }
    }

    /**
     * Handles activating a cell from maintenance status.
     */
    @FXML
    private void handleActivatePrisonCell() {
        PrisonCell selectedPrisonCell = cellTable.getSelectionModel().getSelectedItem();
        if (selectedPrisonCell != null && "Under Maintenance".equals(selectedPrisonCell.getStatus())) {
            selectedPrisonCell.setStatus("Vacant");
            if (cellService.updatePrisonCell(selectedPrisonCell)) {
                showAlert("Success", "PrisonCell activated successfully!", Alert.AlertType.INFORMATION);
                loadAllData();
            } else {
                showAlert("Error", "Failed to activate cell!", Alert.AlertType.ERROR);
            }
        } else {
            showAlert("Warning", "Please select a cell under maintenance to activate!", Alert.AlertType.WARNING);
        }
    }

    /**
     * Handles viewing detailed information for selected cell.
     */
    @FXML
    private void handleViewDetails() {
        PrisonCell selectedPrisonCell = cellTable.getSelectionModel().getSelectedItem();
        if (selectedPrisonCell != null) {
            Alert detailsAlert = new Alert(Alert.AlertType.INFORMATION);
            detailsAlert.setTitle("PrisonCell Details");
            detailsAlert.setHeaderText("Details for PrisonCell " + selectedPrisonCell.getPrisonCellNumber());
            detailsAlert.setContentText(selectedPrisonCell.getPrisonCellDetails());
            detailsAlert.showAndWait();
        } else {
            showAlert("Warning", "Please select a cell to view details!", Alert.AlertType.WARNING);
        }
    }

    /**
     * Handles viewing only available cells.
     */
    @FXML
    private void handleViewAvailablePrisonCells() {
        ObservableList<PrisonCell> availablePrisonCells
                = FXCollections.observableArrayList(cellService.getAvailablePrisonCells());
        cellTable.setItems(availablePrisonCells);

        showAlert("Available PrisonCells",
                "Showing " + availablePrisonCells.size() + " available cells",
                Alert.AlertType.INFORMATION);
    }

    /**
     * Handles refreshing all data.
     */
    @FXML
    private void handleRefresh() {
        loadAllData();
        showAlert("Success", "Data refreshed successfully!", Alert.AlertType.INFORMATION);
    }

    /**
     * Handles auto-assign feature (placeholder implementation).
     */
    @FXML
    private void handleAutoAssign() {
        showAlert("Auto Assign",
                "This feature would assign prisoners to cells based on availability and security level.",
                Alert.AlertType.INFORMATION);
    }

    /**
     * Validates form input data.
     *
     * @return true if input is valid, false otherwise
     */
    private boolean validateInput() {
        return validateCellNumber()
                && validateCapacity()
                && validateOccupancy();
    }

    /**
     * Validates cell number field.
     *
     * @return true if valid, false otherwise
     */
    private boolean validateCellNumber() {
        if (cellNumberField.getText().trim().isEmpty()) {
            showAlert("Validation Error", "PrisonCell number is required!", Alert.AlertType.ERROR);
            cellNumberField.requestFocus();
            return false;
        }
        return true;
    }

    /**
     * Validates capacity field.
     *
     * @return true if valid, false otherwise
     */
    private boolean validateCapacity() {
        try {
            int capacity = Integer.parseInt(capacityField.getText().trim());
            if (capacity <= 0 || capacity > 50) {
                showAlert("Validation Error", "Capacity must be between 1 and 50!", Alert.AlertType.ERROR);
                capacityField.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert("Validation Error", "Capacity must be a valid number!", Alert.AlertType.ERROR);
            capacityField.requestFocus();
            return false;
        }
        return true;
    }

    /**
     * Validates occupancy field.
     *
     * @return true if valid, false otherwise
     */
    private boolean validateOccupancy() {
        try {
            int occupancy = Integer.parseInt(occupancyField.getText().trim());
            int capacity = Integer.parseInt(capacityField.getText().trim());
            if (occupancy < 0 || occupancy > capacity) {
                showAlert("Validation Error",
                        "Occupancy cannot exceed capacity and must be positive!",
                        Alert.AlertType.ERROR);
                occupancyField.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert("Validation Error", "Occupancy must be a valid number!", Alert.AlertType.ERROR);
            occupancyField.requestFocus();
            return false;
        }
        return true;
    }

    /**
     * Clears all form fields and resets display data.
     */
    private void clearForm() {
        cellNumberField.clear();
        cellTypeComboBox.getSelectionModel().selectFirst();
        capacityField.clear();
        occupancyField.clear();
        securityLevelComboBox.getSelectionModel().selectFirst();
        statusComboBox.getSelectionModel().selectFirst();
        availableSpaceLabel.setText("0");
        occupancyRateLabel.setText("0%");
        cellDetailsArea.clear();
        prisonersListView.setItems(prisonerList);
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
