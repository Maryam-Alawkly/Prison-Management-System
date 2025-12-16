package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.PrisonCell;
import model.Prisoner;
import service.PrisonerService;
import service.PrisonCellService;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller for Prisoner Management Interface. Implements Initializable for
 * JavaFX initialization. Uses Singleton pattern for service instances and
 * Strategy pattern for search filtering.
 */
public class PrisonerManagementController implements Initializable {

    // Table UI components
    @FXML
    private TableView<Prisoner> prisonerTable;
    @FXML
    private TableColumn<Prisoner, String> prisonerIdColumn;
    @FXML
    private TableColumn<Prisoner, String> nameColumn;
    @FXML
    private TableColumn<Prisoner, String> crimeColumn;
    @FXML
    private TableColumn<Prisoner, String> cellColumn;
    @FXML
    private TableColumn<Prisoner, String> sentenceColumn;
    @FXML
    private TableColumn<Prisoner, String> statusColumn;

    // Form UI components
    @FXML
    private TextField prisonerIdField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField crimeField;
    @FXML
    private ComboBox<String> cellComboBox;
    @FXML
    private ComboBox<String> sentenceComboBox;
    @FXML
    private DatePicker admissionDatePicker;
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
    private ComboBox<String> filterPrisonCellComboBox;

    // Service instances (Singleton pattern)
    private PrisonerService prisonerService;
    private PrisonCellService prisonCellService;

    // Data collections
    private ObservableList<Prisoner> prisonerList;
    private ObservableList<String> cellList;

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
        prisonerService = PrisonerService.getInstance();
        prisonCellService = PrisonCellService.getInstance();
    }

    /**
     * Initializes data structures for the controller.
     */
    private void initializeDataStructures() {
        prisonerList = FXCollections.observableArrayList();
        cellList = FXCollections.observableArrayList();
    }

    /**
     * Sets up table columns with cell value factories.
     */
    private void setupTableColumns() {
        prisonerIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        crimeColumn.setCellValueFactory(new PropertyValueFactory<>("crime"));
        cellColumn.setCellValueFactory(new PropertyValueFactory<>("prisonCellNumber"));
        sentenceColumn.setCellValueFactory(new PropertyValueFactory<>("sentenceDuration"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    /**
     * Loads all prisoner and cell data.
     */
    private void loadAllData() {
        loadPrisoners();
        loadAvailableCells();
    }

    /**
     * Loads prisoners from the service.
     */
    private void loadPrisoners() {
        try {
            prisonerList.clear();
            List<Prisoner> prisoners = prisonerService.getAllPrisoners();
            if (prisoners != null) {
                prisonerList.addAll(prisoners);
            }
            prisonerTable.setItems(prisonerList);
        } catch (Exception e) {
            showAlert("Error", "Failed to load prisoners: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    /**
     * Loads available prison cells for selection.
     */
    private void loadAvailableCells() {
        try {
            cellList.clear();
            List<PrisonCell> cells = prisonCellService.getAllPrisonCells();
            if (cells != null) {
                for (PrisonCell cell : cells) {
                    if (cell.hasAvailableSpace()) {
                        cellList.add(formatCellDisplay(cell));
                    }
                }
            }
        } catch (Exception e) {
            showAlert("Error", "Failed to load prison cells: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    /**
     * Formats cell information for display in combo box.
     *
     * @param cell The prison cell to format
     * @return Formatted cell display string
     */
    private String formatCellDisplay(PrisonCell cell) {
        return cell.getPrisonCellNumber() + " (" + cell.getAvailableSpace() + " available)";
    }

    /**
     * Sets up all combo boxes with appropriate options.
     */
    private void setupComboBoxes() {
        setupCellComboBox();
        setupSentenceComboBox();
        setupStatusComboBox();
        setupFilterComboBoxes();
        setDefaultAdmissionDate();
    }

    /**
     * Sets up cell selection combo box.
     */
    private void setupCellComboBox() {
        cellComboBox.setItems(cellList);
    }

    /**
     * Sets up sentence duration combo box.
     */
    private void setupSentenceComboBox() {
        ObservableList<String> sentenceOptions = FXCollections.observableArrayList(
                "6 months", "1 year", "2 years", "5 years", "10 years",
                "15 years", "20 years", "Life", "Death"
        );
        sentenceComboBox.setItems(sentenceOptions);
        sentenceComboBox.getSelectionModel().selectFirst();
    }

    /**
     * Sets up status combo box.
     */
    private void setupStatusComboBox() {
        ObservableList<String> statusOptions = FXCollections.observableArrayList(
                "In Custody", "Released", "Transferred", "Escaped", "Deceased"
        );
        statusComboBox.setItems(statusOptions);
        statusComboBox.getSelectionModel().selectFirst();
    }

    /**
     * Sets up filter combo boxes.
     */
    private void setupFilterComboBoxes() {
        setupFilterStatusComboBox();
        setupFilterCellComboBox();
    }

    /**
     * Sets up status filter combo box.
     */
    private void setupFilterStatusComboBox() {
        ObservableList<String> filterStatusOptions = FXCollections.observableArrayList(
                "All", "In Custody", "Released", "Transferred", "Escaped", "Deceased"
        );
        filterStatusComboBox.setItems(filterStatusOptions);
        filterStatusComboBox.getSelectionModel().selectFirst();
    }

    /**
     * Sets up cell filter combo box.
     */
    private void setupFilterCellComboBox() {
        try {
            ObservableList<String> filterCellOptions = FXCollections.observableArrayList();
            filterCellOptions.add("All PrisonCells");

            List<PrisonCell> cells = prisonCellService.getAllPrisonCells();
            if (cells != null) {
                for (PrisonCell cell : cells) {
                    filterCellOptions.add(cell.getPrisonCellNumber());
                }
            }
            filterPrisonCellComboBox.setItems(filterCellOptions);
            filterPrisonCellComboBox.getSelectionModel().selectFirst();
        } catch (Exception e) {
            showAlert("Error", "Failed to setup cell filter: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    /**
     * Sets default admission date to today.
     */
    private void setDefaultAdmissionDate() {
        admissionDatePicker.setValue(LocalDate.now());
    }

    /**
     * Sets up table selection listener to display prisoner details when
     * selected.
     */
    private void setupTableSelectionListener() {
        prisonerTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        displayPrisonerDetails(newValue);
                    }
                });
    }

    /**
     * Displays prisoner details in the form.
     *
     * @param prisoner The prisoner to display
     */
    private void displayPrisonerDetails(Prisoner prisoner) {
        prisonerIdField.setText(prisoner.getId());
        nameField.setText(prisoner.getName());
        phoneField.setText(prisoner.getPhone());
        crimeField.setText(prisoner.getCrime());

        setCellComboBoxValue(prisoner.getPrisonCellNumber());
        sentenceComboBox.setValue(prisoner.getSentenceDuration());
        statusComboBox.setValue(prisoner.getStatus());
    }

    /**
     * Sets the cell combo box value based on prisoner's cell number.
     *
     * @param cellNumber The prisoner's cell number
     */
    private void setCellComboBoxValue(String cellNumber) {
        for (String cell : cellList) {
            if (cell.startsWith(cellNumber)) {
                cellComboBox.setValue(cell);
                return;
            }
        }
        cellComboBox.setValue(cellNumber);
    }

    // FXML Action Handlers
    /**
     * Handles add prisoner action.
     */
    @FXML
    private void handleAddPrisoner() {
        if (validateInput()) {
            createAndAddPrisoner();
        }
    }

    /**
     * Creates and adds a new prisoner from form data. Uses Factory pattern for
     * prisoner creation.
     */
    private void createAndAddPrisoner() {
        try {
            Prisoner newPrisoner = createPrisonerFromForm();

            if (prisonerService.addPrisoner(newPrisoner)) {
                showAlert("Success", "Prisoner added successfully!", Alert.AlertType.INFORMATION);
                loadAllData();
                clearForm();
            } else {
                showAlert("Error", "Failed to add prisoner!", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            handlePrisonerActionError("adding", e);
        }
    }

    /**
     * Handles update prisoner action.
     */
    @FXML
    private void handleUpdatePrisoner() {
        Prisoner selectedPrisoner = prisonerTable.getSelectionModel().getSelectedItem();
        if (selectedPrisoner == null) {
            showAlert("Warning", "Please select a prisoner to update!", Alert.AlertType.WARNING);
            return;
        }

        if (validateInput()) {
            updateSelectedPrisoner(selectedPrisoner);
        }
    }

    /**
     * Updates the selected prisoner with form data.
     *
     * @param prisoner The prisoner to update
     */
    private void updateSelectedPrisoner(Prisoner prisoner) {
        try {
            Prisoner originalPrisoner = prisonerService.getPrisonerById(prisoner.getId());
            updatePrisonerFromForm(prisoner);

            if (prisonerService.updatePrisoner(prisoner)) {
                showAlert("Success", "Prisoner updated successfully!", Alert.AlertType.INFORMATION);
                loadAllData();
            } else {
                handleUpdateFailure(originalPrisoner);
            }
        } catch (Exception e) {
            handlePrisonerActionError("updating", e);
        }
    }

    /**
     * Handles update failure by reverting to original prisoner data.
     *
     * @param originalPrisoner The original prisoner data
     */
    private void handleUpdateFailure(Prisoner originalPrisoner) {
        showAlert("Error", "Failed to update prisoner!", Alert.AlertType.ERROR);
        if (originalPrisoner != null) {
            displayPrisonerDetails(originalPrisoner);
        }
    }

    /**
     * Handles delete prisoner action.
     */
    @FXML
    private void handleDeletePrisoner() {
        Prisoner selectedPrisoner = prisonerTable.getSelectionModel().getSelectedItem();
        if (selectedPrisoner == null) {
            showAlert("Warning", "Please select a prisoner to delete!", Alert.AlertType.WARNING);
            return;
        }

        confirmAndDeletePrisoner(selectedPrisoner);
    }

    /**
     * Shows confirmation dialog and deletes prisoner if confirmed.
     *
     * @param prisoner The prisoner to delete
     */
    private void confirmAndDeletePrisoner(Prisoner prisoner) {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Delete");
        confirmAlert.setHeaderText("Delete Prisoner");
        confirmAlert.setContentText("Are you sure you want to delete prisoner " + prisoner.getId() + "?");

        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    if (prisonerService.deletePrisoner(prisoner.getId())) {
                        showAlert("Success", "Prisoner deleted successfully!", Alert.AlertType.INFORMATION);
                        loadAllData();
                        clearForm();
                    } else {
                        showAlert("Error", "Failed to delete prisoner!", Alert.AlertType.ERROR);
                    }
                } catch (Exception e) {
                    handlePrisonerActionError("deleting", e);
                }
            }
        });
    }

    /**
     * Handles search functionality with multiple filter criteria. Uses Strategy
     * pattern for search filtering.
     */
    @FXML
    private void handleSearch() {
        String searchTerm = searchField.getText().toLowerCase().trim();
        String statusFilter = filterStatusComboBox.getValue();
        String cellFilter = filterPrisonCellComboBox.getValue();

        try {
            List<Prisoner> filteredResults = performSearch(searchTerm, statusFilter, cellFilter);
            prisonerTable.setItems(FXCollections.observableArrayList(filteredResults));
        } catch (Exception e) {
            showAlert("Error", "Failed to search prisoners: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    /**
     * Performs search with filtering criteria. Implements Strategy pattern for
     * filtering logic.
     *
     * @param searchTerm The search term
     * @param statusFilter Status filter value
     * @param cellFilter Cell filter value
     * @return Filtered list of prisoners
     */
    private List<Prisoner> performSearch(String searchTerm, String statusFilter, String cellFilter) {
        Prisoner[] searchResults = prisonerService.searchPrisoners(searchTerm);
        List<Prisoner> results = searchResults != null
                ? java.util.Arrays.asList(searchResults)
                : java.util.Collections.emptyList();

        return filterSearchResults(results, statusFilter, cellFilter);
    }

    /**
     * Filters search results based on status and cell criteria.
     *
     * @param results Initial search results
     * @param statusFilter Status filter
     * @param cellFilter Cell filter
     * @return Filtered prisoner list
     */
    private List<Prisoner> filterSearchResults(List<Prisoner> results, String statusFilter, String cellFilter) {
        ObservableList<Prisoner> filteredList = FXCollections.observableArrayList();

        for (Prisoner prisoner : results) {
            if (matchesFilterCriteria(prisoner, statusFilter, cellFilter)) {
                filteredList.add(prisoner);
            }
        }

        return filteredList;
    }

    /**
     * Checks if a prisoner matches filter criteria.
     *
     * @param prisoner The prisoner to check
     * @param statusFilter Status filter
     * @param cellFilter Cell filter
     * @return true if prisoner matches criteria, false otherwise
     */
    private boolean matchesFilterCriteria(Prisoner prisoner, String statusFilter, String cellFilter) {
        boolean matchesStatus = "All".equals(statusFilter) || prisoner.getStatus().equals(statusFilter);
        boolean matchesCell = "All PrisonCells".equals(cellFilter) || prisoner.getPrisonCellNumber().equals(cellFilter);

        return matchesStatus && matchesCell;
    }

    /**
     * Handles clear action to reset search and form.
     */
    @FXML
    private void handleClear() {
        searchField.clear();
        filterStatusComboBox.getSelectionModel().selectFirst();
        filterPrisonCellComboBox.getSelectionModel().selectFirst();
        clearForm();
        prisonerTable.getSelectionModel().clearSelection();
        loadAllData();
    }

    /**
     * Handles release prisoner action.
     */
    @FXML
    private void handleReleasePrisoner() {
        Prisoner selectedPrisoner = prisonerTable.getSelectionModel().getSelectedItem();
        if (selectedPrisoner != null) {
            confirmAndReleasePrisoner(selectedPrisoner);
        } else {
            showAlert("Warning", "Please select a prisoner to release!", Alert.AlertType.WARNING);
        }
    }

    /**
     * Shows confirmation dialog and releases prisoner if confirmed.
     *
     * @param prisoner The prisoner to release
     */
    private void confirmAndReleasePrisoner(Prisoner prisoner) {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Release");
        confirmAlert.setHeaderText("Release Prisoner");
        confirmAlert.setContentText("Are you sure you want to release prisoner " + prisoner.getName() + "?");

        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                releasePrisoner(prisoner);
            }
        });
    }

    /**
     * Releases a prisoner by updating their status.
     *
     * @param prisoner The prisoner to release
     */
    private void releasePrisoner(Prisoner prisoner) {
        try {
            prisoner.setStatus("Released");
            if (prisonerService.updatePrisoner(prisoner)) {
                showAlert("Success", "Prisoner released successfully!", Alert.AlertType.INFORMATION);
                loadAllData();
            } else {
                showAlert("Error", "Failed to release prisoner!", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            handlePrisonerActionError("releasing", e);
        }
    }

    /**
     * Handles transfer prisoner action.
     */
    @FXML
    private void handleTransferPrisoner() {
        Prisoner selectedPrisoner = prisonerTable.getSelectionModel().getSelectedItem();
        if (selectedPrisoner != null) {
            showTransferDialog(selectedPrisoner);
        } else {
            showAlert("Warning", "Please select a prisoner to transfer!", Alert.AlertType.WARNING);
        }
    }

    /**
     * Shows transfer dialog for selecting new cell.
     *
     * @param prisoner The prisoner to transfer
     */
    private void showTransferDialog(Prisoner prisoner) {
        ChoiceDialog<String> dialog = new ChoiceDialog<>(prisoner.getPrisonCellNumber(), cellList);
        dialog.setTitle("Transfer Prisoner");
        dialog.setHeaderText("Transfer " + prisoner.getName());
        dialog.setContentText("Select new cell:");

        dialog.showAndWait().ifPresent(newCell -> {
            String newCellNumber = extractCellNumber(newCell);
            if (!newCellNumber.equals(prisoner.getPrisonCellNumber())) {
                transferPrisonerCell(prisoner.getId(), newCellNumber);
            }
        });
    }

    /**
     * Transfers a prisoner to a new cell.
     *
     * @param prisonerId The ID of the prisoner to transfer
     * @param newCellNumber The new cell number
     */
    private void transferPrisonerCell(String prisonerId, String newCellNumber) {
        try {
            if (prisonerService.transferPrisonerCell(prisonerId, newCellNumber)) {
                showAlert("Success", "Prisoner transferred successfully!", Alert.AlertType.INFORMATION);
                loadAllData();
            } else {
                showAlert("Error", "Failed to transfer prisoner!", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            handlePrisonerActionError("transferring", e);
        }
    }

    /**
     * Handles view details action.
     */
    @FXML
    private void handleViewDetails() {
        Prisoner selectedPrisoner = prisonerTable.getSelectionModel().getSelectedItem();
        if (selectedPrisoner != null) {
            Alert detailsAlert = new Alert(Alert.AlertType.INFORMATION);
            detailsAlert.setTitle("Prisoner Details");
            detailsAlert.setHeaderText("Details for " + selectedPrisoner.getName());
            detailsAlert.setContentText(selectedPrisoner.getPrisonerDetails());
            detailsAlert.showAndWait();
        } else {
            showAlert("Warning", "Please select a prisoner to view details!", Alert.AlertType.WARNING);
        }
    }

    /**
     * Handles refresh action.
     */
    @FXML
    private void handleRefresh() {
        loadAllData();
        showAlert("Success", "Data refreshed successfully!", Alert.AlertType.INFORMATION);
    }

    /**
     * Creates a Prisoner object from form data.
     *
     * @return A populated Prisoner object
     */
    private Prisoner createPrisonerFromForm() {
        String prisonerId = prisonerIdField.getText().trim();
        String name = nameField.getText().trim();
        String phone = phoneField.getText().trim();
        String crime = crimeField.getText().trim();
        String cellNumber = extractCellNumber(cellComboBox.getValue());
        String sentence = sentenceComboBox.getValue();
        String status = statusComboBox.getValue();

        Prisoner prisoner = new Prisoner(prisonerId, name, phone, crime, cellNumber, sentence);
        prisoner.setStatus(status);

        return prisoner;
    }

    /**
     * Updates prisoner object with form data.
     *
     * @param prisoner The prisoner to update
     */
    private void updatePrisonerFromForm(Prisoner prisoner) {
        prisoner.setName(nameField.getText().trim());
        prisoner.setPhone(phoneField.getText().trim());
        prisoner.setCrime(crimeField.getText().trim());
        prisoner.setPrisonCellNumber(extractCellNumber(cellComboBox.getValue()));
        prisoner.setSentenceDuration(sentenceComboBox.getValue());
        prisoner.setStatus(statusComboBox.getValue());
    }

    /**
     * Validates form input data.
     *
     * @return true if form is valid, false otherwise
     */
    private boolean validateInput() {
        return validatePrisonerId()
                && validateName()
                && validatePhone()
                && validateCrime()
                && validateCell();
    }

    /**
     * Validates prisoner ID field.
     *
     * @return true if valid, false otherwise
     */
    private boolean validatePrisonerId() {
        if (prisonerIdField.getText().trim().isEmpty()) {
            showAlert("Validation Error", "Prisoner ID is required!", Alert.AlertType.ERROR);
            prisonerIdField.requestFocus();
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
     * Validates crime field.
     *
     * @return true if valid, false otherwise
     */
    private boolean validateCrime() {
        if (crimeField.getText().trim().isEmpty()) {
            showAlert("Validation Error", "Crime is required!", Alert.AlertType.ERROR);
            crimeField.requestFocus();
            return false;
        }
        return true;
    }

    /**
     * Validates cell selection.
     *
     * @return true if valid, false otherwise
     */
    private boolean validateCell() {
        if (cellComboBox.getValue() == null || cellComboBox.getValue().isEmpty()) {
            showAlert("Validation Error", "Please select a cell!", Alert.AlertType.ERROR);
            cellComboBox.requestFocus();
            return false;
        }
        return true;
    }

    /**
     * Extracts cell number from combo box value.
     *
     * @param comboValue The combo box value
     * @return Extracted cell number
     */
    private String extractCellNumber(String comboValue) {
        if (comboValue != null && comboValue.contains(" ")) {
            return comboValue.split(" ")[0];
        }
        return comboValue;
    }

    /**
     * Clears all form fields.
     */
    private void clearForm() {
        prisonerIdField.clear();
        nameField.clear();
        phoneField.clear();
        crimeField.clear();
        cellComboBox.setValue(null);
        sentenceComboBox.getSelectionModel().selectFirst();
        admissionDatePicker.setValue(LocalDate.now());
        statusComboBox.getSelectionModel().selectFirst();
        notesArea.clear();
    }

    /**
     * Handles prisoner action errors.
     *
     * @param action The action being performed
     * @param e The exception that occurred
     */
    private void handlePrisonerActionError(String action, Exception e) {
        showAlert("Error", "Error " + action + " prisoner: " + e.getMessage(), Alert.AlertType.ERROR);
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
