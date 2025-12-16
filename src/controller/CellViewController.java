package controller;

import model.PrisonCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import service.PrisonCellService;

/**
 * Controller for the Cell View interface. Implements Initializable for JavaFX
 * initialization. Uses Singleton pattern for service instances and Strategy
 * pattern for filtering.
 */
public class CellViewController implements Initializable {

    // Table UI components
    @FXML
    private TableView<PrisonCell> cellsTable;
    @FXML
    private TableColumn<PrisonCell, String> cellNumberColumn;
    @FXML
    private TableColumn<PrisonCell, String> cellTypeColumn;
    @FXML
    private TableColumn<PrisonCell, Integer> capacityColumn;
    @FXML
    private TableColumn<PrisonCell, Integer> occupiedColumn;
    @FXML
    private TableColumn<PrisonCell, Integer> availableColumn;
    @FXML
    private TableColumn<PrisonCell, String> statusColumn;
    @FXML
    private TableColumn<PrisonCell, String> securityColumn;

    // Statistics UI components
    @FXML
    private Label totalCellsLabel;
    @FXML
    private Label occupiedCellsLabel;
    @FXML
    private Label occupancyRateLabel;
    @FXML
    private Label availableCellsLabel;

    // Service instance (Singleton pattern)
    private PrisonCellService prisonCellService;

    // Data collections
    private ObservableList<PrisonCell> cellList;
    private FilteredList<PrisonCell> filteredData;

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
        loadCells();
        updateStatistics();
    }

    /**
     * Initializes service instances using Singleton pattern.
     */
    private void initializeServices() {
        prisonCellService = PrisonCellService.getInstance();
    }

    /**
     * Initializes data structures for the controller.
     */
    private void initializeDataStructures() {
        cellList = FXCollections.observableArrayList();
        filteredData = new FilteredList<>(cellList, cell -> true); // Default predicate shows all cells
    }

    /**
     * Sets up table columns with cell value factories and styling.
     */
    private void setupTableColumns() {
        configureColumnValueFactories();
        configureAvailableColumn();
        applyTableStyling();
    }

    /**
     * Configures standard cell value factories for each column.
     */
    private void configureColumnValueFactories() {
        cellNumberColumn.setCellValueFactory(new PropertyValueFactory<>("prisonCellNumber"));
        cellTypeColumn.setCellValueFactory(new PropertyValueFactory<>("prisonCellType"));
        capacityColumn.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        occupiedColumn.setCellValueFactory(new PropertyValueFactory<>("currentOccupancy"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        securityColumn.setCellValueFactory(new PropertyValueFactory<>("securityLevel"));
    }

    /**
     * Configures the available column to calculate available space dynamically.
     */
    private void configureAvailableColumn() {
        availableColumn.setCellValueFactory(cellData -> {
            PrisonCell cell = cellData.getValue();
            int available = cell.getCapacity() - cell.getCurrentOccupancy();
            return new javafx.beans.property.SimpleIntegerProperty(available).asObject();
        });
    }

    /**
     * Applies styling to table cells and rows.
     */
    private void applyTableStyling() {
        applyCellStyling();
        applyRowStyling();
    }

    /**
     * Applies styling to individual table cells.
     */
    private void applyCellStyling() {
        cellNumberColumn.setCellFactory(col -> createStyledTableCell());
        cellTypeColumn.setCellFactory(col -> createStyledTableCell());
        capacityColumn.setCellFactory(col -> createStyledTableCell());
        occupiedColumn.setCellFactory(col -> createStyledTableCell());
        availableColumn.setCellFactory(col -> createStyledTableCell());
        statusColumn.setCellFactory(col -> createStyledTableCell());
        securityColumn.setCellFactory(col -> createStyledTableCell());
    }

    /**
     * Applies styling to table rows.
     */
    private void applyRowStyling() {
        cellsTable.setRowFactory(tv -> new TableRow<PrisonCell>() {
            @Override
            protected void updateItem(PrisonCell item, boolean empty) {
                super.updateItem(item, empty);

                // Correct: Use this.setStyle() inside anonymous inner class
                if (empty || item == null) {
                    this.setStyle("-fx-background-color: white;");
                } else {
                    this.setStyle("-fx-background-color: white;");
                }
            }
        });
    }

    /**
     * Creates a styled table cell with specific formatting.
     *
     * @param <T> The type of data in the cell
     * @return A styled TableCell instance
     */
    private <T> TableCell<PrisonCell, T> createStyledTableCell() {
        return new TableCell<PrisonCell, T>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);

                // Correct: Use this.setText() and this.setStyle() inside anonymous inner class
                if (empty || item == null) {
                    this.setText(null);
                    this.setStyle("-fx-background-color: white;");
                } else {
                    this.setText(item.toString());
                    this.setStyle("-fx-background-color: white; "
                            + "-fx-text-fill: #1e2a3a; "
                            + "-fx-font-weight: bold; "
                            + "-fx-alignment: CENTER; "
                            + "-fx-border-color: #f0f0f0; "
                            + "-fx-border-width: 0 0 1 0;");
                }
            }
        };
    }

    /**
     * Loads prison cells from the service and populates the table.
     */
    private void loadCells() {
        try {
            cellList.clear();
            List<PrisonCell> cells = prisonCellService.getAllPrisonCells();
            cellList.addAll(cells);

            setupSortedTableView();
        } catch (Exception e) {
            showError("Error loading cells: " + e.getMessage());
        }
    }

    /**
     * Sets up sorted table view with filtered data.
     */
    private void setupSortedTableView() {
        SortedList<PrisonCell> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(cellsTable.comparatorProperty());
        cellsTable.setItems(sortedData);
    }

    /**
     * Updates statistics labels with current data.
     */
    private void updateStatistics() {
        try {
            calculateAndDisplayStatistics();
        } catch (Exception e) {
            showError("Error updating statistics: " + e.getMessage());
        }
    }

    /**
     * Calculates and displays all statistical data.
     */
    private void calculateAndDisplayStatistics() {
        int totalCells = calculateTotalCells();
        int occupiedCells = calculateOccupiedCells();
        int availableCells = calculateAvailableCells();
        double occupancyRate = calculateOccupancyRate();

        updateStatisticsLabels(totalCells, occupiedCells, availableCells, occupancyRate);
    }

    /**
     * Calculates total number of cells.
     *
     * @return Total number of cells
     */
    private int calculateTotalCells() {
        return prisonCellService.getTotalPrisonCells();
    }

    /**
     * Calculates number of occupied cells.
     *
     * @return Number of occupied cells
     */
    private int calculateOccupiedCells() {
        return (int) cellList.stream()
                .filter(cell -> cell.getCurrentOccupancy() > 0)
                .count();
    }

    /**
     * Calculates number of available cells.
     *
     * @return Number of available cells
     */
    private int calculateAvailableCells() {
        return (int) cellList.stream()
                .filter(cell -> cell.hasAvailableSpace() && !"Under Maintenance".equals(cell.getStatus()))
                .count();
    }

    /**
     * Calculates overall occupancy rate.
     *
     * @return Occupancy rate as percentage
     */
    private double calculateOccupancyRate() {
        int[] stats = prisonCellService.getOccupancyStatistics();
        return stats[0] > 0 ? (double) stats[1] / stats[0] * 100 : 0;
    }

    /**
     * Updates all statistics labels with calculated values.
     *
     * @param totalCells Total number of cells
     * @param occupiedCells Number of occupied cells
     * @param availableCells Number of available cells
     * @param occupancyRate Occupancy rate percentage
     */
    private void updateStatisticsLabels(int totalCells, int occupiedCells, int availableCells, double occupancyRate) {
        totalCellsLabel.setText(String.valueOf(totalCells));
        occupiedCellsLabel.setText(String.valueOf(occupiedCells));
        availableCellsLabel.setText(String.valueOf(availableCells));
        occupancyRateLabel.setText(String.format("%.1f%%", occupancyRate));
    }

    // FXML Action Handlers
    /**
     * Handles refresh action to reload cells and update statistics.
     */
    @FXML
    private void handleRefresh() {
        loadCells();
        updateStatistics();
    }

    /**
     * Handles show available cells action. Uses Strategy pattern for filtering
     * logic.
     */
    @FXML
    private void handleShowAvailable() {
        filteredData.setPredicate(this::isCellAvailable);
    }

    /**
     * Checks if a cell is available (has space and not under maintenance).
     * Implements Strategy pattern for filtering criteria.
     *
     * @param cell The prison cell to check
     * @return true if cell is available, false otherwise
     */
    private boolean isCellAvailable(PrisonCell cell) {
        if (cell == null) {
            return false;
        }
        return cell.hasAvailableSpace() && !"Under Maintenance".equals(cell.getStatus());
    }

    /**
     * Handles show all cells action.
     */
    @FXML
    private void handleShowAll() {
        filteredData.setPredicate(cell -> true);
    }

    /**
     * Shows an error alert dialog.
     *
     * @param message The error message to display
     */
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
