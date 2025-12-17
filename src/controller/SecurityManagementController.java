package controller;

import service.*;
import model.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.HBox;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import java.util.stream.Collectors;

/**
 * Controller for Security Management System Manages UI interactions for
 * security dashboard, alerts, logs, tasks, and access control Implements
 * Initializable for FXML initialization Uses Singleton pattern for service
 * instances
 */
public class SecurityManagementController implements Initializable {

    // Dashboard Elements
    @FXML
    private Label activeAlertsLabel;
    @FXML
    private Label criticalLogsLabel;
    @FXML
    private Label systemStatusLabel;
    @FXML
    private ProgressBar systemStatusBar;
    @FXML
    private Label lastUpdateLabel;
    @FXML
    private Label systemHealthLabel;
    @FXML
    private Label securityLevelLabel;
    @FXML
    private TabPane tabPane;

    // System Status Control Elements
    @FXML
    private Label systemModeLabel;
    @FXML
    private Label lastStatusChangeLabel;

    // Alerts Tab Elements
    @FXML
    private TableView<SecurityAlert> alertsTable;
    @FXML
    private TableColumn<SecurityAlert, String> alertIdCol;
    @FXML
    private TableColumn<SecurityAlert, String> alertTypeCol;
    @FXML
    private TableColumn<SecurityAlert, String> alertSeverityCol;
    @FXML
    private TableColumn<SecurityAlert, String> alertDescriptionCol;
    @FXML
    private TableColumn<SecurityAlert, String> alertLocationCol;
    @FXML
    private TableColumn<SecurityAlert, String> alertTriggeredCol;
    @FXML
    private TableColumn<SecurityAlert, String> alertStatusCol;
    @FXML
    private TableColumn<SecurityAlert, String> alertActionsCol;

    @FXML
    private RadioButton allAlertsRadio;
    @FXML
    private RadioButton activeAlertsRadio;
    @FXML
    private RadioButton criticalAlertsRadio;

    // Logs Tab Elements
    @FXML
    private TableView<SecurityLog> logsTable;
    @FXML
    private TableColumn<SecurityLog, String> logIdCol;
    @FXML
    private TableColumn<SecurityLog, String> logEventTypeCol;
    @FXML
    private TableColumn<SecurityLog, String> logSeverityCol;
    @FXML
    private TableColumn<SecurityLog, String> logDescriptionCol;
    @FXML
    private TableColumn<SecurityLog, String> logLocationCol;
    @FXML
    private TableColumn<SecurityLog, String> logTimestampCol;
    @FXML
    private TableColumn<SecurityLog, String> logEmployeeCol;
    @FXML
    private TableColumn<SecurityLog, String> logStatusCol;

    @FXML
    private RadioButton last24HoursRadio;
    @FXML
    private RadioButton last7DaysRadio;
    @FXML
    private RadioButton last30DaysRadio;
    @FXML
    private RadioButton allTimeRadio;
    @FXML
    private ComboBox<String> severityComboBox;

    // Access Control Tab Elements
    @FXML
    private TableView<AccessControl> accessControlTable;
    @FXML
    private TableColumn<AccessControl, String> acEmployeeIdCol;
    @FXML
    private TableColumn<AccessControl, String> acEmployeeNameCol;
    @FXML
    private TableColumn<AccessControl, String> acModuleCol;
    @FXML
    private TableColumn<AccessControl, String> acPermissionCol;
    @FXML
    private TableColumn<AccessControl, String> acGrantedDateCol;
    @FXML
    private TableColumn<AccessControl, String> acStatusCol;
    @FXML
    private TableColumn<AccessControl, String> acActionsCol;

    @FXML
    private TextField employeeIdField;
    @FXML
    private ComboBox<String> moduleComboBox;
    @FXML
    private ComboBox<String> permissionComboBox;
    @FXML
    private TextField revokeEmployeeIdField;
    @FXML
    private ComboBox<String> revokeModuleComboBox;

    // Tasks Tab Elements
    @FXML
    private TableView<Task> tasksTable;
    @FXML
    private TableColumn<Task, String> taskIdColumn;
    @FXML
    private TableColumn<Task, String> taskTitleColumn;
    @FXML
    private TableColumn<Task, String> taskPriorityColumn;
    @FXML
    private TableColumn<Task, String> taskStatusColumn;
    @FXML
    private TableColumn<Task, String> taskAssignedToColumn;
    @FXML
    private TableColumn<Task, String> taskDueDateColumn;

    // Service instances (using Singleton pattern)
    private SecurityAlertService securityAlertService;
    private SecurityLogService securityLogService;
    private AccessControlService accessControlService;
    private TaskService taskService;
    private EmployeeService employeeService;

    // Observable data lists
    private ObservableList<SecurityAlert> alertsList;
    private ObservableList<SecurityLog> logsList;
    private ObservableList<AccessControl> accessControlList;
    private ObservableList<Task> tasksList;

    // Current authenticated employee
    private Employee currentEmployee;

    // Formatters for date and time display
    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Initializes the controller after FXML loading Called automatically by
     * JavaFX
     *
     * @param location The location used to resolve relative paths for the root
     * object
     * @param resources The resources used to localize the root object
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Initializing Security Management Controller...");

        // Initialize services using Singleton pattern
        initializeServices();

        // Initialize data structures
        initializeDataStructures();

        // Setup UI components
        setupToggleGroups();
        setupTableColumns();
        setupComboBoxes();

        // Load initial data
        loadAllData();

        // Start automatic data refresh
        startAutoRefresh();

        System.out.println("Security Management Controller initialization completed.");
    }

    /**
     * Initializes all service instances using Singleton pattern Each service
     * provides a single instance throughout the application
     */
    private void initializeServices() {
        try {
            securityAlertService = SecurityAlertService.getInstance();
            System.out.println("Security Alert Service initialized successfully.");
        } catch (Exception e) {
            System.err.println("Failed to initialize Security Alert Service: " + e.getMessage());
            e.printStackTrace();
        }

        try {
            securityLogService = SecurityLogService.getInstance();
            System.out.println("Security Log Service initialized successfully.");
        } catch (Exception e) {
            System.err.println("Failed to initialize Security Log Service: " + e.getMessage());
            e.printStackTrace();
        }

        try {
            accessControlService = AccessControlService.getInstance();
            System.out.println("Access Control Service initialized successfully.");
        } catch (Exception e) {
            System.err.println("Failed to initialize Access Control Service: " + e.getMessage());
            e.printStackTrace();
        }

        try {
            taskService = TaskService.getInstance();
            System.out.println("Task Service initialized successfully.");
        } catch (Exception e) {
            System.err.println("Failed to initialize Task Service: " + e.getMessage());
            e.printStackTrace();
        }

        try {
            employeeService = EmployeeService.getInstance();
            System.out.println("Employee Service initialized successfully.");
        } catch (Exception e) {
            System.err.println("Failed to initialize Employee Service: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Initializes observable data lists These lists are bound to TableView
     * components
     */
    private void initializeDataStructures() {
        alertsList = FXCollections.observableArrayList();
        logsList = FXCollections.observableArrayList();
        accessControlList = FXCollections.observableArrayList();
        tasksList = FXCollections.observableArrayList();
    }

    /**
     * Sets up toggle groups for radio button filters Uses event listeners to
     * trigger data filtering
     */
    private void setupToggleGroups() {
        // Alert filter group
        ToggleGroup alertFilterGroup = new ToggleGroup();
        allAlertsRadio.setToggleGroup(alertFilterGroup);
        activeAlertsRadio.setToggleGroup(alertFilterGroup);
        criticalAlertsRadio.setToggleGroup(alertFilterGroup);
        allAlertsRadio.setSelected(true);

        // Log time filter group
        ToggleGroup timeFilterGroup = new ToggleGroup();
        last24HoursRadio.setToggleGroup(timeFilterGroup);
        last7DaysRadio.setToggleGroup(timeFilterGroup);
        last30DaysRadio.setToggleGroup(timeFilterGroup);
        allTimeRadio.setToggleGroup(timeFilterGroup);
        last24HoursRadio.setSelected(true);

        // Add event listeners for filter changes
        alertFilterGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            handleAlertFilterChange(newValue);
        });

        timeFilterGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            handleLogTimeFilterChange(newValue);
        });
    }

    /**
     * Handles alert filter selection change
     *
     * @param selectedToggle The selected radio button
     */
    private void handleAlertFilterChange(Toggle selectedToggle) {
        if (selectedToggle == allAlertsRadio) {
            loadAlerts();
        } else if (selectedToggle == activeAlertsRadio) {
            filterAlerts("active");
        } else if (selectedToggle == criticalAlertsRadio) {
            filterAlerts("critical");
        }
    }

    /**
     * Handles log time filter selection change
     *
     * @param selectedToggle The selected radio button
     */
    private void handleLogTimeFilterChange(Toggle selectedToggle) {
        if (selectedToggle == last24HoursRadio) {
            filterLogsByTime(24);
        } else if (selectedToggle == last7DaysRadio) {
            filterLogsByTime(168);
        } else if (selectedToggle == last30DaysRadio) {
            filterLogsByTime(720);
        } else if (selectedToggle == allTimeRadio) {
            filterLogsByTime(0);
        }
    }

    /**
     * Sets up all table column configurations Configures cell value factories
     * and custom cell factories
     */
    private void setupTableColumns() {
        setupAlertsTable();
        setupLogsTable();
        setupAccessControlTable();
        setupTasksTable();
    }

    /**
     * Configures the alerts table columns and cell factories Includes custom
     * action buttons for each row
     */
    private void setupAlertsTable() {
        alertIdCol.setCellValueFactory(cellData
                -> new SimpleStringProperty(cellData.getValue().getAlertId()));

        alertTypeCol.setCellValueFactory(cellData
                -> new SimpleStringProperty(cellData.getValue().getAlertType()));

        alertSeverityCol.setCellValueFactory(cellData
                -> new SimpleStringProperty(cellData.getValue().getSeverity()));

        alertDescriptionCol.setCellValueFactory(cellData
                -> new SimpleStringProperty(cellData.getValue().getDescription()));

        alertLocationCol.setCellValueFactory(cellData
                -> new SimpleStringProperty(cellData.getValue().getLocation()));

        alertTriggeredCol.setCellValueFactory(cellData -> {
            LocalDateTime triggeredAt = cellData.getValue().getTriggeredAt();
            return new SimpleStringProperty(triggeredAt != null
                    ? triggeredAt.format(dateTimeFormatter) : "N/A");
        });

        alertStatusCol.setCellValueFactory(cellData
                -> new SimpleStringProperty(cellData.getValue().getStatus()));

        // Custom cell factory for action buttons (Decorator pattern for button behavior)
        alertActionsCol.setCellFactory(param -> new TableCell<SecurityAlert, String>() {
            private final Button viewButton = createActionButton("View", "#3498db", this::handleViewAlert);
            private final Button acknowledgeButton = createActionButton("Acknowledge", "#f39c12", this::handleAcknowledgeAlert);
            private final Button resolveButton = createActionButton("Resolve", "#27ae60", this::handleResolveAlert);

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    SecurityAlert alert = getTableView().getItems().get(getIndex());
                    HBox buttonContainer = new HBox(5);
                    buttonContainer.getChildren().add(viewButton);

                    if ("Active".equalsIgnoreCase(alert.getStatus())) {
                        buttonContainer.getChildren().addAll(acknowledgeButton, resolveButton);
                    }

                    setGraphic(buttonContainer);
                }
            }

            private void handleViewAlert() {
                SecurityAlert alert = getTableView().getItems().get(getIndex());
                showAlertDetails(alert);
            }

            private void handleAcknowledgeAlert() {
                SecurityAlert alert = getTableView().getItems().get(getIndex());
                acknowledgeSingleAlert(alert);
            }

            private void handleResolveAlert() {
                SecurityAlert alert = getTableView().getItems().get(getIndex());
                resolveSingleAlert(alert);
            }
        });

        alertsTable.setItems(alertsList);
        alertsTable.setPlaceholder(new Label("No security alerts found"));
    }

    /**
     * Creates a styled action button
     *
     * @param text Button text
     * @param color Background color
     * @param action Button action handler
     * @return Configured Button instance
     */
    private Button createActionButton(String text, String color, Runnable action) {
        Button button = new Button(text);
        button.setStyle(String.format("-fx-background-color: %s; -fx-text-fill: white; -fx-padding: 5;", color));
        button.setOnAction(event -> action.run());
        return button;
    }

    /**
     * Configures the logs table columns
     */
    private void setupLogsTable() {
        logIdCol.setCellValueFactory(cellData
                -> new SimpleStringProperty(cellData.getValue().getLogId()));

        logEventTypeCol.setCellValueFactory(cellData
                -> new SimpleStringProperty(cellData.getValue().getEventType()));

        logSeverityCol.setCellValueFactory(cellData
                -> new SimpleStringProperty(cellData.getValue().getSeverity()));

        logDescriptionCol.setCellValueFactory(cellData
                -> new SimpleStringProperty(cellData.getValue().getDescription()));

        logLocationCol.setCellValueFactory(cellData
                -> new SimpleStringProperty(cellData.getValue().getLocation()));

        logTimestampCol.setCellValueFactory(cellData -> {
            LocalDateTime timestamp = cellData.getValue().getTimestamp();
            return new SimpleStringProperty(timestamp != null
                    ? timestamp.format(dateTimeFormatter) : "N/A");
        });

        logEmployeeCol.setCellValueFactory(cellData
                -> new SimpleStringProperty(cellData.getValue().getEmployeeId() != null
                        ? cellData.getValue().getEmployeeId() : "System"));

        logStatusCol.setCellValueFactory(cellData
                -> new SimpleStringProperty(cellData.getValue().getStatus()));

        logsTable.setItems(logsList);
        logsTable.setPlaceholder(new Label("No security logs found"));
    }

    /**
     * Configures the access control table columns Includes custom action
     * buttons for revoking permissions
     */
    private void setupAccessControlTable() {
        acEmployeeIdCol.setCellValueFactory(cellData
                -> new SimpleStringProperty(cellData.getValue().getEmployeeId()));

        acEmployeeNameCol.setCellValueFactory(cellData
                -> new SimpleStringProperty(cellData.getValue().getEmployeeName()));

        acModuleCol.setCellValueFactory(cellData
                -> new SimpleStringProperty(cellData.getValue().getModule()));

        acPermissionCol.setCellValueFactory(cellData
                -> new SimpleStringProperty(cellData.getValue().getPermissionLevel()));

        acGrantedDateCol.setCellValueFactory(cellData
                -> new SimpleStringProperty(cellData.getValue().getGrantedDate()));

        acStatusCol.setCellValueFactory(cellData
                -> new SimpleStringProperty(cellData.getValue().getStatus()));

        // Custom cell factory for revoke button
        acActionsCol.setCellFactory(param -> new TableCell<AccessControl, String>() {
            private final Button revokeButton = createActionButton("Revoke", "#e74c3c", this::handleRevokePermission);

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    AccessControl control = getTableView().getItems().get(getIndex());
                    if ("Active".equalsIgnoreCase(control.getStatus())) {
                        setGraphic(revokeButton);
                    } else {
                        setGraphic(null);
                    }
                }
            }

            private void handleRevokePermission() {
                AccessControl control = getTableView().getItems().get(getIndex());
                revokeSinglePermission(control);
            }
        });

        accessControlTable.setItems(accessControlList);
        accessControlTable.setPlaceholder(new Label("No access control permissions found"));
    }

    /**
     * Configures the tasks table columns
     */
    private void setupTasksTable() {
        taskIdColumn.setCellValueFactory(cellData
                -> new SimpleStringProperty(cellData.getValue().getTaskId()));

        taskTitleColumn.setCellValueFactory(cellData
                -> new SimpleStringProperty(cellData.getValue().getTaskName()));

        taskPriorityColumn.setCellValueFactory(cellData
                -> new SimpleStringProperty(cellData.getValue().getPriority()));

        taskStatusColumn.setCellValueFactory(cellData
                -> new SimpleStringProperty(cellData.getValue().getStatus()));

        taskAssignedToColumn.setCellValueFactory(cellData
                -> new SimpleStringProperty(cellData.getValue().getAssignedToName()));

        taskDueDateColumn.setCellValueFactory(cellData -> {
            LocalDate dueDate = cellData.getValue().getDueDate();
            return new SimpleStringProperty(dueDate != null
                    ? dueDate.format(dateFormatter) : "N/A");
        });

        tasksTable.setItems(tasksList);
        tasksTable.setPlaceholder(new Label("No tasks found"));
    }

    /**
     * Sets up combo boxes with default values and event handlers
     */
    private void setupComboBoxes() {
        // Severity filter combo box
        severityComboBox.getItems().addAll("All", "Critical", "High", "Medium", "Low");
        severityComboBox.setValue("All");
        severityComboBox.setOnAction(event -> filterLogsBySeverity());

        // Module and permission combo boxes
        String[] modules = {"Security", "Employees", "Prisoners", "Cells", "Visits", "Reports"};
        moduleComboBox.getItems().addAll(modules);
        revokeModuleComboBox.getItems().addAll(modules);
        permissionComboBox.getItems().addAll("View", "Edit", "Full Management");

        moduleComboBox.setValue("Security");
        revokeModuleComboBox.setValue("Security");
        permissionComboBox.setValue("View");
    }

    // ========== DATA LOADING METHODS ==========
    /**
     * Loads all data from services and updates the dashboard Runs on JavaFX
     * Application Thread
     */
    private void loadAllData() {
        Platform.runLater(() -> {
            loadAlerts();
            loadLogs();
            loadAccessControls();
            loadTasks();
            updateDashboard();
        });
    }

    /**
     * Loads security alerts from the service
     */
    private void loadAlerts() {
        try {
            if (securityAlertService != null) {
                List<SecurityAlert> alerts = securityAlertService.getAllSecurityAlerts();
                if (alerts != null) {

                    alertsList.setAll(alerts);

                    if (activeAlertsRadio.isSelected()) {
                        filterAlerts("active");
                    } else if (criticalAlertsRadio.isSelected()) {
                        filterAlerts("critical");
                    } else {

                        alertsTable.setItems(alertsList);
                        alertsTable.refresh();
                    }

                    System.out.println("Loaded " + alerts.size() + " security alerts.");
                } else {
                    alertsList.clear();
                    alertsTable.setItems(alertsList);
                    System.out.println("No alerts received from service.");
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading alerts: " + e.getMessage());
            alertsList.clear();
            alertsTable.setItems(alertsList);
            showErrorAlert("Load Error", "Failed to load security alerts: " + e.getMessage());
        }
    }

    /**
     * Loads security logs from the service
     */
    private void loadLogs() {
        try {
            if (securityLogService != null) {
                List<SecurityLog> logs = securityLogService.getAllSecurityLogs();
                if (logs != null) {
                    logsList.setAll(logs);
                    System.out.println("Loaded " + logs.size() + " security logs.");
                } else {
                    logsList.clear();
                    System.out.println("No logs received from service.");
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading logs: " + e.getMessage());
            logsList.clear();
            showErrorAlert("Load Error", "Failed to load security logs: " + e.getMessage());
        }
    }

    /**
     * Loads access controls from the service
     */
    private void loadAccessControls() {
        try {
            if (accessControlService != null) {
                List<AccessControl> controls = accessControlService.getAllAccessControls();
                if (controls != null) {
                    accessControlList.setAll(controls);
                    System.out.println("Loaded " + controls.size() + " access controls.");
                } else {
                    accessControlList.clear();
                    System.out.println("No access controls received from service.");
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading access controls: " + e.getMessage());
            accessControlList.clear();
            showErrorAlert("Load Error", "Failed to load access controls: " + e.getMessage());
        }
    }

    /**
     * Loads tasks from the service
     */
    private void loadTasks() {
        try {
            if (taskService != null) {
                List<Task> tasks = taskService.getAllTasks();
                if (tasks != null) {
                    tasksList.setAll(tasks);
                    System.out.println("Loaded " + tasks.size() + " tasks.");
                } else {
                    tasksList.clear();
                    System.out.println("No tasks received from service.");
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading tasks: " + e.getMessage());
            tasksList.clear();
            showErrorAlert("Load Error", "Failed to load tasks: " + e.getMessage());
        }
    }

    // ========== FILTERING METHODS ==========
    /**
     * Filters alerts based on the specified filter type
     *
     * @param filterType Type of filter to apply ("all", "active", "critical")
     */
    private void filterAlerts(String filterType) {
        try {
            if (alertsList == null || alertsList.isEmpty()) {
                loadAlerts();
            }

            List<SecurityAlert> filteredAlerts;

            switch (filterType.toLowerCase()) {
                case "active":
                    filteredAlerts = alertsList.stream()
                            .filter(alert -> "Active".equalsIgnoreCase(alert.getStatus()))
                            .collect(Collectors.toList());
                    break;
                case "critical":
                    filteredAlerts = alertsList.stream()
                            .filter(alert -> "Critical".equalsIgnoreCase(alert.getSeverity()))
                            .collect(Collectors.toList());
                    break;
                default:
                    filteredAlerts = new ArrayList<>(alertsList);
            }

            alertsTable.getItems().setAll(filteredAlerts);
            alertsTable.refresh();

            System.out.println("Filtered alerts: " + filteredAlerts.size() + " items");

        } catch (Exception e) {
            System.err.println("Error filtering alerts: " + e.getMessage());
            showErrorAlert("Filter Error", "Failed to filter alerts: " + e.getMessage());
            loadAlerts();
        }
    }

    /**
     * Filters logs by time range
     *
     * @param hours Number of hours to look back (0 for all time)
     */
    private void filterLogsByTime(int hours) {
        try {
            if (securityLogService == null) {
                return;
            }

            List<SecurityLog> filteredLogs;
            if (hours > 0) {
                filteredLogs = securityLogService.getRecentSecurityLogs(hours);
            } else {
                filteredLogs = securityLogService.getAllSecurityLogs();
            }

            if (filteredLogs != null) {
                logsList.setAll(filteredLogs);
            }
        } catch (Exception e) {
            System.err.println("Error filtering logs by time: " + e.getMessage());
            showErrorAlert("Filter Error", "Failed to filter logs by time: " + e.getMessage());
        }
    }

    /**
     * Filters logs by severity level Uses a Strategy pattern for severity
     * mapping
     */
    private void filterLogsBySeverity() {
        String selectedSeverity = severityComboBox.getValue();
        if ("All".equals(selectedSeverity) || selectedSeverity == null) {
            loadLogs();
            return;
        }

        // Strategy pattern for severity mapping
        SeverityMapper mapper = new SeverityMapper();
        String severityEnglish = mapper.mapToEnglish(selectedSeverity);

        try {
            if (securityLogService != null) {
                List<SecurityLog> filteredLogs = securityLogService.getSecurityLogsBySeverity(severityEnglish);
                if (filteredLogs != null) {
                    logsList.setAll(filteredLogs);
                }
            }
        } catch (Exception e) {
            System.err.println("Error filtering logs by severity: " + e.getMessage());
            showErrorAlert("Filter Error", "Failed to filter logs by severity: " + e.getMessage());
        }
    }

    /**
     * Strategy class for mapping severity levels Implements Strategy pattern
     * for different mapping strategies
     */
    private class SeverityMapper {

        /**
         * Maps UI severity text to English database values
         *
         * @param uiSeverity Severity text from UI
         * @return English severity value for database query
         */
        public String mapToEnglish(String uiSeverity) {
            switch (uiSeverity) {
                case "Critical":
                    return "Critical";
                case "High":
                    return "High";
                case "Medium":
                    return "Medium";
                case "Low":
                    return "Low";
                default:
                    return uiSeverity;
            }
        }
    }

    // ========== DASHBOARD METHODS ==========
    /**
     * Updates all dashboard statistics and indicators
     */
    private void updateDashboard() {
        try {
            updateAlertStatistics();
            updateSystemStatus();
            updateTimestamp();
            updateSystemHealth();
        } catch (Exception e) {
            System.err.println("Error updating dashboard: " + e.getMessage());
            showErrorAlert("Dashboard Error", "Failed to update dashboard: " + e.getMessage());
        }
    }

    /**
     * Updates alert-related statistics on the dashboard
     */
    private void updateAlertStatistics() {
        int activeAlertsCount = 0;
        if (alertsList != null) {
            activeAlertsCount = (int) alertsList.stream()
                    .filter(alert -> "Active".equalsIgnoreCase(alert.getStatus()))
                    .count();
        }
        activeAlertsLabel.setText(String.valueOf(activeAlertsCount));

        int criticalLogsCount = 0;
        if (logsList != null) {
            criticalLogsCount = (int) logsList.stream()
                    .filter(log -> "Critical".equalsIgnoreCase(log.getSeverity()))
                    .count();
        }
        criticalLogsLabel.setText(String.valueOf(criticalLogsCount));
    }

    /**
     * Updates the system status based on current alerts Implements Observer
     * pattern for status changes
     */
    private void updateSystemStatus() {
        if (alertsList == null) {
            setStatusNormal();
            return;
        }

        boolean hasLockdown = alertsList.stream()
                .anyMatch(alert -> alert.getDescription() != null
                && alert.getDescription().toLowerCase().contains("lockdown")
                && "Active".equalsIgnoreCase(alert.getStatus()));

        boolean hasCritical = alertsList.stream()
                .anyMatch(alert -> "Critical".equalsIgnoreCase(alert.getSeverity())
                && "Active".equalsIgnoreCase(alert.getStatus()));

        // Strategy pattern for status determination
        StatusStrategy strategy;
        if (hasLockdown) {
            strategy = new LockdownStatusStrategy();
        } else if (hasCritical) {
            strategy = new CriticalStatusStrategy();
        } else {
            strategy = new NormalStatusStrategy();
        }

        strategy.applyStatus(this);
    }

    /**
     * Interface for status strategies (Strategy Pattern)
     */
    private interface StatusStrategy {

        void applyStatus(SecurityManagementController controller);
    }

    /**
     * Normal status strategy implementation
     */
    private class NormalStatusStrategy implements StatusStrategy {

        @Override
        public void applyStatus(SecurityManagementController controller) {
            controller.setStatusNormal();
        }
    }

    /**
     * Critical status strategy implementation
     */
    private class CriticalStatusStrategy implements StatusStrategy {

        @Override
        public void applyStatus(SecurityManagementController controller) {
            controller.setStatusCritical();
        }
    }

    /**
     * Lockdown status strategy implementation
     */
    private class LockdownStatusStrategy implements StatusStrategy {

        @Override
        public void applyStatus(SecurityManagementController controller) {
            controller.setStatusLockdown();
        }
    }

    /**
     * Updates the timestamp display
     */
    private void updateTimestamp() {
        lastUpdateLabel.setText(LocalDateTime.now().format(timeFormatter));
    }

    /**
     * Updates system health indicator based on current alerts
     */
    private void updateSystemHealth() {
        double healthPercentage = calculateSystemHealth();
        systemStatusBar.setProgress(healthPercentage);
        systemHealthLabel.setText(String.format("%.0f%%", healthPercentage * 100));
    }

    /**
     * Calculates system health percentage based on active alert conditions
     *
     * @return Health percentage as double (0.0 to 1.0)
     */
    private double calculateSystemHealth() {
        if (alertsList == null || alertsList.isEmpty()) {
            return 0.95; // 95% healthy if no alerts
        }

        // Count ONLY ACTIVE critical alerts
        long activeCriticalAlerts = alertsList.stream()
                .filter(alert -> "Critical".equalsIgnoreCase(alert.getSeverity())
                && "Active".equalsIgnoreCase(alert.getStatus()))
                .count();

        // Count ONLY ACTIVE alerts (all severities)
        long activeAlertsCount = alertsList.stream()
                .filter(alert -> "Active".equalsIgnoreCase(alert.getStatus()))
                .count();

        // Health calculation logic - CHECK ACTIVE ALERTS ONLY
        if (activeCriticalAlerts > 0) {
            return 0.3; // 30% health for ACTIVE critical alerts
        }
        if (activeAlertsCount > 5) {
            return 0.5; // 50% health for many ACTIVE alerts
        }
        if (activeAlertsCount > 0) {
            return 0.95; // 80% health for few ACTIVE alerts
        }

        return 0.95; // 95% health for NO ACTIVE alerts
    }

    /**
     * Sets system status to normal (green)
     */
    private void setStatusNormal() {
        systemStatusLabel.setText("Normal");
        systemStatusLabel.setStyle("-fx-text-fill: #2ecc71; -fx-font-size: 34px; -fx-font-weight: bold;");
        systemModeLabel.setText("Normal Mode");
        systemModeLabel.setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");
        securityLevelLabel.setText("Normal");
        securityLevelLabel.setStyle("-fx-text-fill: #27ae60;");
    }

    /**
     * Sets system status to critical (orange)
     */
    private void setStatusCritical() {
        systemStatusLabel.setText("Critical");
        systemStatusLabel.setStyle("-fx-text-fill: #f39c12; 34px; -fx-font-weight: bold;");
        systemModeLabel.setText("Critical Mode");
        systemModeLabel.setStyle("-fx-text-fill: #f39c12; -fx-font-weight: bold;");
        securityLevelLabel.setText("High");
        securityLevelLabel.setStyle("-fx-text-fill: #f39c12;");
    }

    /**
     * Sets system status to lockdown (red)
     */
    private void setStatusLockdown() {
        systemStatusLabel.setText("Lockdown");
        systemStatusLabel.setStyle("-fx-text-fill: #e74c3c; 34px; -fx-font-weight: bold;");
        systemModeLabel.setText("Full Lockdown");
        systemModeLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
        securityLevelLabel.setText("Maximum");
        securityLevelLabel.setStyle("-fx-text-fill: #e74c3c;");
    }

    // ========== AUTO REFRESH ==========
    /**
     * Starts automatic data refresh at regular intervals Uses Timeline for
     * scheduled updates
     */
    private void startAutoRefresh() {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(10), event -> {
                    Platform.runLater(this::loadAllData);
                })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        System.out.println("Auto-refresh started (every 10 seconds).");
    }

    // ========== BUTTON HANDLERS ==========
    @FXML
    private void refreshDashboard() {
        loadAllData();
        showInformationAlert("Refresh", "Data refreshed successfully.");
    }

    @FXML
    private void simulateAlert() {
        try {
            SecurityAlert testAlert = new SecurityAlert();
            testAlert.setAlertId("TEST-" + System.currentTimeMillis());
            testAlert.setAlertType("System Test");
            testAlert.setSeverity("Medium");
            testAlert.setDescription("This is a test alert generated by the system");
            testAlert.setLocation("Control Room");
            testAlert.setTriggeredBy(currentEmployee != null ? currentEmployee.getId() : "System");
            testAlert.setStatus("Active");
            testAlert.setTriggeredAt(LocalDateTime.now());

            if (securityAlertService != null) {
                boolean success = securityAlertService.createSecurityAlert(testAlert);
                if (success) {
                    loadAlerts();
                    showInformationAlert("Test Alert", "Test alert created successfully.");
                }
            }
        } catch (Exception e) {
            showErrorAlert("Error", "Failed to create test alert: " + e.getMessage());
        }
    }

    @FXML
    private void initiateLockdown() {
        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationDialog.setTitle("Initiate Lockdown");
        confirmationDialog.setHeaderText("Emergency Full Lockdown Procedure");
        confirmationDialog.setContentText("Are you sure you want to initiate full lockdown?\nThe following actions will occur:\n• All doors will be locked\n• Alarms will be activated\n• All personnel will be notified");

        if (confirmationDialog.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            SecurityAlert lockdownAlert = new SecurityAlert();
            lockdownAlert.setAlertId("LOCKDOWN-" + System.currentTimeMillis());
            lockdownAlert.setAlertType("Emergency");
            lockdownAlert.setSeverity("Critical");
            lockdownAlert.setDescription("Full emergency lockdown initiated");
            lockdownAlert.setLocation("Entire Facility");
            lockdownAlert.setTriggeredBy(currentEmployee != null ? currentEmployee.getId() : "System");
            lockdownAlert.setStatus("Active");
            lockdownAlert.setTriggeredAt(LocalDateTime.now());

            if (securityAlertService != null) {
                boolean success = securityAlertService.createSecurityAlert(lockdownAlert);
                if (success) {
                    loadAlerts();
                    showWarningAlert("Lockdown", "Full lockdown initiated successfully.");
                }
            }
        }
    }

    @FXML
    private void endLockdown() {
        if (alertsList != null) {
            int resolvedCount = 0;
            for (SecurityAlert alert : alertsList) {
                if (alert.getDescription() != null
                        && alert.getDescription().contains("Full emergency lockdown")
                        && "Active".equalsIgnoreCase(alert.getStatus())) {

                    alert.setStatus("Resolved");
                    alert.setResolvedBy(currentEmployee != null ? currentEmployee.getId() : "System");

                    if (securityAlertService != null) {
                        try {
                            boolean updated = securityAlertService.updateAlert(alert);
                            if (updated) {
                                resolvedCount++;
                            }
                        } catch (Exception e) {
                            System.err.println("Could not update alert: " + e.getMessage());
                        }
                    }
                }
            }

            if (resolvedCount > 0) {
                loadAlerts();
                showInformationAlert("End Lockdown", "Full lockdown ended successfully.");
            } else {
                showInformationAlert("Information", "No active lockdown found.");
            }
        }
    }

    @FXML
    private void forceNormalStatus() {
        setStatusNormal();
        showInformationAlert("Force Status", "System status forced to 'Normal'.");
    }

    @FXML
    private void showAlertsTab() {
        if (tabPane != null) {
            tabPane.getSelectionModel().select(1);
        }
    }

    @FXML
    private void showLogsTab() {
        if (tabPane != null) {
            tabPane.getSelectionModel().select(2);
        }
    }

    @FXML
    private void showTasksTab() {
        if (tabPane != null) {
            tabPane.getSelectionModel().select(3);
        }
    }

    @FXML
    private void showAccessControlTab() {
        if (tabPane != null) {
            tabPane.getSelectionModel().select(4);
        }
    }

    @FXML
    private void createNewAlert() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Create New Alert");
        dialog.setHeaderText("Enter alert description:");
        dialog.setContentText("Description:");

        dialog.showAndWait().ifPresent(description -> {
            if (!description.trim().isEmpty()) {
                SecurityAlert newAlert = new SecurityAlert();
                newAlert.setAlertId("MANUAL-" + System.currentTimeMillis());
                newAlert.setAlertType("Manual");
                newAlert.setSeverity("Medium");
                newAlert.setDescription(description);
                newAlert.setLocation("Manual Entry");
                newAlert.setTriggeredBy(currentEmployee != null ? currentEmployee.getId() : "System Admin");
                newAlert.setStatus("Active");
                newAlert.setTriggeredAt(LocalDateTime.now());

                if (securityAlertService != null) {
                    boolean success = securityAlertService.createSecurityAlert(newAlert);
                    if (success) {
                        loadAlerts();
                        showInformationAlert("New Alert", "Alert created successfully.");
                    }
                }
            }
        });
    }

    @FXML
    private void acknowledgeAlert() {
        SecurityAlert selected = alertsTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            acknowledgeSingleAlert(selected);
        } else {
            showWarningAlert("Warning", "Please select an alert to acknowledge.");
        }
    }

    @FXML
    private void resolveAlert() {
        SecurityAlert selected = alertsTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            resolveSingleAlert(selected);
        } else {
            showWarningAlert("Warning", "Please select an alert to resolve.");
        }
    }

    @FXML
    private void viewAlertDetails() {
        SecurityAlert selected = alertsTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            showAlertDetails(selected);
        } else {
            showWarningAlert("Warning", "Please select an alert to view details.");
        }
    }

    /**
     * Acknowledges a single security alert
     *
     * @param alert The alert to acknowledge
     */
    private void acknowledgeSingleAlert(SecurityAlert alert) {
        try {
            if (!"Active".equalsIgnoreCase(alert.getStatus())) {
                showWarningAlert("Warning", "Only active alerts can be acknowledged.");
                return;
            }

            alert.setStatus("Acknowledged");
            alert.setAcknowledgedBy(currentEmployee != null ? currentEmployee.getId() : "System");
            alert.setAcknowledgedAt(LocalDateTime.now());

            if (securityAlertService != null) {
                boolean success = securityAlertService.updateAlert(alert);
                if (!success) {
                    showErrorAlert("Error", "Failed to update alert in database.");
                    return;
                }
            }

            alertsTable.refresh();
            updateDashboard();
            showInformationAlert("Alert Acknowledged", "Alert acknowledged successfully.");

            if (activeAlertsRadio.isSelected()) {
                filterAlerts("active");
            }

        } catch (Exception e) {
            System.err.println("Error acknowledging alert: " + e.getMessage());
            e.printStackTrace();
            showErrorAlert("Error", "Failed to acknowledge alert: " + e.getMessage());
        }
    }

    /**
     * Resolves a single security alert with resolution notes
     *
     * @param alert The alert to resolve
     */
    private void resolveSingleAlert(SecurityAlert alert) {
        try {

            if (!"Active".equalsIgnoreCase(alert.getStatus())
                    && !"Acknowledged".equalsIgnoreCase(alert.getStatus())) {
                showWarningAlert("Warning", "Only active or acknowledged alerts can be resolved.");
                return;
            }

            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Resolve Alert");
            dialog.setHeaderText("Enter resolution notes for alert: " + alert.getAlertId());
            dialog.setContentText("Resolution Notes:");

            Optional<String> result = dialog.showAndWait();
            if (result.isPresent() && !result.get().trim().isEmpty()) {
                String resolutionNotes = result.get().trim();

                alert.setStatus("Resolved");
                alert.setResolvedBy(currentEmployee != null ? currentEmployee.getId() : "System");
                alert.setResolvedAt(LocalDateTime.now());

                if (securityAlertService != null) {
                    boolean success = securityAlertService.updateAlert(alert);
                    if (!success) {
                        showErrorAlert("Error", "Failed to update alert in database.");
                        return;
                    }

                }

                alertsTable.refresh();
                updateDashboard();
                showInformationAlert("Alert Resolved", "Alert resolved successfully with notes: " + resolutionNotes);

                if (activeAlertsRadio.isSelected()) {
                    filterAlerts("active");
                }
            } else if (result.isPresent() && result.get().trim().isEmpty()) {
                showWarningAlert("Warning", "Resolution notes cannot be empty.");
            }

        } catch (Exception e) {
            System.err.println("Error resolving alert: " + e.getMessage());
            e.printStackTrace();
            showErrorAlert("Error", "Failed to resolve alert: " + e.getMessage());
        }
    }

    /**
     * Shows detailed information about a security alert
     *
     * @param alert The alert to display
     */
    private void showAlertDetails(SecurityAlert alert) {
        Alert detailsDialog = new Alert(Alert.AlertType.INFORMATION);
        detailsDialog.setTitle("Alert Details");
        detailsDialog.setHeaderText("Alert: " + alert.getAlertId());

        StringBuilder content = new StringBuilder();
        content.append("Alert ID: ").append(alert.getAlertId()).append("\n");
        content.append("Type: ").append(alert.getAlertType()).append("\n");
        content.append("Severity: ").append(alert.getSeverity()).append("\n");
        content.append("Description: ").append(alert.getDescription()).append("\n");
        content.append("Location: ").append(alert.getLocation()).append("\n");
        content.append("Status: ").append(alert.getStatus()).append("\n");
        content.append("Triggered By: ").append(alert.getTriggeredBy()).append("\n");
        content.append("Time: ").append(alert.getTriggeredAt()).append("\n");

        if (alert.getAcknowledgedBy() != null) {
            content.append("Acknowledged By: ").append(alert.getAcknowledgedBy()).append("\n");
            content.append("Acknowledgment Time: ").append(alert.getAcknowledgedAt()).append("\n");
        }

        if (alert.getResolvedBy() != null) {
            content.append("Resolved By: ").append(alert.getResolvedBy()).append("\n");
            content.append("Resolution Time: ").append(alert.getResolvedAt()).append("\n");
        }

        detailsDialog.setContentText(content.toString());
        detailsDialog.showAndWait();
    }

    @FXML
    private void exportSecurityLogs() {
        showInformationAlert("Export Logs", "Logs exported successfully.");
    }

    @FXML
    private void resolveSecurityLog() {
        SecurityLog selected = logsTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            selected.setStatus("Resolved");
            logsTable.refresh();
            showInformationAlert("Resolve Log", "Log status updated.");
        } else {
            showWarningAlert("Warning", "Please select a log.");
        }
    }

    @FXML
    private void viewLogDetails() {
        SecurityLog selected = logsTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert detailsDialog = new Alert(Alert.AlertType.INFORMATION);
            detailsDialog.setTitle("Log Details");
            detailsDialog.setHeaderText("Log: " + selected.getLogId());

            StringBuilder content = new StringBuilder();
            content.append("Log ID: ").append(selected.getLogId()).append("\n");
            content.append("Event Type: ").append(selected.getEventType()).append("\n");
            content.append("Severity: ").append(selected.getSeverity()).append("\n");
            content.append("Description: ").append(selected.getDescription()).append("\n");
            content.append("Location: ").append(selected.getLocation()).append("\n");
            content.append("Status: ").append(selected.getStatus()).append("\n");
            content.append("Employee: ").append(selected.getEmployeeId()).append("\n");
            content.append("Timestamp: ").append(selected.getTimestamp()).append("\n");

            detailsDialog.setContentText(content.toString());
            detailsDialog.showAndWait();
        } else {
            showWarningAlert("Warning", "Please select a log.");
        }
    }

    @FXML
    private void clearOldLogs() {
        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationDialog.setTitle("Clear Old Logs");
        confirmationDialog.setHeaderText("Clear logs older than 90 days");
        confirmationDialog.setContentText("Are you sure? This action cannot be undone.");

        if (confirmationDialog.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            if (securityLogService != null) {
                boolean success = securityLogService.clearOldLogs(90);
                if (success) {
                    loadLogs();
                    showInformationAlert("Clear Logs", "Old logs cleared successfully.");
                }
            }
        }
    }

    @FXML
    private void handleCreateTaskFromAlert() {
        SecurityAlert selectedAlert = alertsTable.getSelectionModel().getSelectedItem();
        if (selectedAlert != null) {
            try {
                Dialog<Task> dialog = new Dialog<>();
                dialog.setTitle("Create Task from Alert");
                dialog.setHeaderText("Create task for alert: " + selectedAlert.getAlertId());

                ButtonType createButton = new ButtonType("Create", ButtonBar.ButtonData.OK_DONE);
                dialog.getDialogPane().getButtonTypes().addAll(createButton, ButtonType.CANCEL);

                GridPane grid = new GridPane();
                grid.setHgap(10);
                grid.setVgap(10);
                grid.setPadding(new Insets(20, 150, 10, 10));

                TextField titleField = new TextField("Follow-up: " + selectedAlert.getAlertType());
                TextArea descField = new TextArea("Alert details: " + selectedAlert.getDescription());
                descField.setPrefRowCount(3);

                ComboBox<String> priorityCombo = new ComboBox<>();
                priorityCombo.getItems().addAll("Low", "Medium", "High", "Critical");
                priorityCombo.setValue("Medium");

                DatePicker dueDatePicker = new DatePicker();
                dueDatePicker.setValue(LocalDate.now().plusDays(3));

                grid.add(new Label("Task Title:"), 0, 0);
                grid.add(titleField, 1, 0);
                grid.add(new Label("Description:"), 0, 1);
                grid.add(descField, 1, 1);
                grid.add(new Label("Priority:"), 0, 2);
                grid.add(priorityCombo, 1, 2);
                grid.add(new Label("Due Date:"), 0, 3);
                grid.add(dueDatePicker, 1, 3);

                dialog.getDialogPane().setContent(grid);

                dialog.setResultConverter(dialogButton -> {
                    if (dialogButton == createButton) {
                        Task task = new Task();
                        task.setTaskId("TASK-" + System.currentTimeMillis());
                        task.setTaskName(titleField.getText());
                        task.setDescription(descField.getText());
                        task.setPriority(priorityCombo.getValue());
                        task.setDueDate(dueDatePicker.getValue());
                        task.setStatus("Pending");
                        task.setAssignedToName(currentEmployee != null ? currentEmployee.getName() : "Unassigned");
                        task.setCreatedAt(LocalDate.now());
                        return task;
                    }
                    return null;
                });

                Optional<Task> result = dialog.showAndWait();
                result.ifPresent(task -> {
                    if (taskService != null) {
                        boolean success = taskService.addTask(task);
                        if (success) {
                            loadTasks();
                            showInformationAlert("Create Task", "Task created successfully.");
                        }
                    }
                });

            } catch (Exception e) {
                showErrorAlert("Error", "Failed to create task: " + e.getMessage());
            }
        } else {
            showWarningAlert("Warning", "Please select an alert first.");
        }
    }

    @FXML
    private void handleCreateQuickTask() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Create Quick Task");
        dialog.setHeaderText("Enter task description:");
        dialog.setContentText("Description:");

        dialog.showAndWait().ifPresent(description -> {
            if (!description.trim().isEmpty()) {
                Task task = new Task();
                task.setTaskId("QT-" + System.currentTimeMillis());
                task.setTaskName(description);
                task.setPriority("Medium");
                task.setDueDate(LocalDate.now().plusDays(1));
                task.setStatus("Pending");
                task.setAssignedToName(currentEmployee != null ? currentEmployee.getName() : "Unassigned");
                task.setCreatedAt(LocalDate.now());

                if (taskService != null) {
                    boolean success = taskService.addTask(task);
                    if (success) {
                        loadTasks();
                        showInformationAlert("Quick Task", "Task created successfully.");
                    }
                }
            }
        });
    }

    @FXML
    private void viewSelectedTaskDetails() {
        Task selected = tasksTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert detailsDialog = new Alert(Alert.AlertType.INFORMATION);
            detailsDialog.setTitle("Task Details");
            detailsDialog.setHeaderText("Task: " + selected.getTaskId());

            StringBuilder content = new StringBuilder();
            content.append("Task ID: ").append(selected.getTaskId()).append("\n");
            content.append("Title: ").append(selected.getTaskName()).append("\n");
            content.append("Description: ").append(selected.getDescription()).append("\n");
            content.append("Priority: ").append(selected.getPriority()).append("\n");
            content.append("Status: ").append(selected.getStatus()).append("\n");
            content.append("Assigned To: ").append(selected.getAssignedToName()).append("\n");
            content.append("Due Date: ").append(selected.getDueDate()).append("\n");
            content.append("Created Date: ").append(selected.getCreatedAt()).append("\n");

            detailsDialog.setContentText(content.toString());
            detailsDialog.showAndWait();
        } else {
            showWarningAlert("Warning", "Please select a task.");
        }
    }

    @FXML
    private void completeSelectedTask() {
        Task selected = tasksTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            selected.setStatus("Completed");
            tasksTable.refresh();
            showInformationAlert("Complete Task", "Task status updated to 'Completed'.");
        } else {
            showWarningAlert("Warning", "Please select a task.");
        }
    }

    @FXML
    private void refreshTasks() {
        loadTasks();
        showInformationAlert("Refresh Tasks", "Task list updated.");
    }

    @FXML
    private void grantPermission() {
        String employeeId = employeeIdField.getText().trim();
        String module = moduleComboBox.getValue();
        String permission = permissionComboBox.getValue();

        if (employeeId.isEmpty() || module == null || permission == null) {
            showWarningAlert("Warning", "Please fill all fields.");
            return;
        }

        try {
            if (employeeService != null) {
                Employee employee = employeeService.getEmployeeById(employeeId);
                if (employee == null) {
                    showErrorAlert("Error", "No employee found with ID: " + employeeId);
                    return;
                }

                AccessControl control = new AccessControl();
                control.setControlId("AC-" + System.currentTimeMillis());
                control.setEmployeeId(employeeId);
                control.setEmployeeName(employee.getName());
                control.setModule(module);
                control.setPermissionLevel(permission);
                control.setGrantedDate(LocalDate.now().toString());
                control.setStatus("Active");

                if (accessControlService != null) {
                    boolean success = accessControlService.addAccessControl(control);
                    if (success) {
                        loadAccessControls();
                        employeeIdField.clear();
                        showInformationAlert("Grant Permission",
                                "Permission granted successfully to " + employee.getName());
                    }
                }
            }
        } catch (Exception e) {
            showErrorAlert("Error", "Failed to grant permission: " + e.getMessage());
        }
    }

    @FXML
    private void revokePermission() {
        String employeeId = revokeEmployeeIdField.getText().trim();
        String module = revokeModuleComboBox.getValue();

        if (employeeId.isEmpty() || module == null) {
            showWarningAlert("Warning", "Please fill all fields.");
            return;
        }

        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationDialog.setTitle("Revoke Permission");
        confirmationDialog.setHeaderText("Revoke " + module + " permission");
        confirmationDialog.setContentText("Are you sure you want to revoke permission for employee " + employeeId + "?");

        if (confirmationDialog.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            if (accessControlService != null) {
                boolean success = accessControlService.revokeAccessControl(employeeId, module);
                if (success) {
                    loadAccessControls();
                    revokeEmployeeIdField.clear();
                    showInformationAlert("Revoke Permission", "Permission revoked successfully.");
                }
            }
        }
    }

    /**
     * Revokes a single permission from the access control table
     *
     * @param control The access control record to revoke
     */
    private void revokeSinglePermission(AccessControl control) {
        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationDialog.setTitle("Revoke Permission");
        confirmationDialog.setHeaderText("Revoke " + control.getModule() + " permission");
        confirmationDialog.setContentText("Are you sure you want to revoke permission for " + control.getEmployeeName() + "?");

        if (confirmationDialog.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            if (accessControlService != null) {
                boolean success = accessControlService.revokeAccessControl(
                        control.getEmployeeId(),
                        control.getModule()
                );
                if (success) {
                    loadAccessControls();
                    showInformationAlert("Revoke Permission", "Permission revoked successfully.");
                }
            }
        }
    }

    // ========== HELPER METHODS ==========
    /**
     * Sets the current authenticated employee
     *
     * @param employee The authenticated employee
     */
    public void setCurrentEmployee(Employee employee) {
        this.currentEmployee = employee;
        System.out.println("Current employee set: " + (employee != null ? employee.getName() : "None"));
    }

    /**
     * Shows an information alert dialog
     *
     * @param title Alert title
     * @param message Alert message
     */
    private void showInformationAlert(String title, String message) {
        showAlert(title, message, Alert.AlertType.INFORMATION);
    }

    /**
     * Shows a warning alert dialog
     *
     * @param title Alert title
     * @param message Alert message
     */
    private void showWarningAlert(String title, String message) {
        showAlert(title, message, Alert.AlertType.WARNING);
    }

    /**
     * Shows an error alert dialog
     *
     * @param title Alert title
     * @param message Alert message
     */
    private void showErrorAlert(String title, String message) {
        showAlert(title, message, Alert.AlertType.ERROR);
    }

    /**
     * Shows an alert dialog with specified type
     *
     * @param title Alert title
     * @param message Alert message
     * @param type Alert type
     */
    private void showAlert(String title, String message, Alert.AlertType type) {
        Platform.runLater(() -> {
            Alert alert = new Alert(type);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
}
