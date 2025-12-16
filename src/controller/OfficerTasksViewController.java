package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import model.Employee;
import model.Task;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import service.TaskService;

/**
 * Controller for Officer Tasks View interface. Implements Initializable for
 * JavaFX initialization. Uses Singleton pattern for service instances and
 * Strategy pattern for task filtering.
 */
public class OfficerTasksViewController implements Initializable {

    // Table UI components
    @FXML
    private TableView<Task> tasksTable;

    // Statistics UI components
    @FXML
    private Label pendingTasksLabel;
    @FXML
    private Label inProgressTasksLabel;
    @FXML
    private Label completedTasksLabel;
    @FXML
    private Label overdueTasksLabel;

    // Filter UI components
    @FXML
    private ComboBox<String> statusFilterComboBox;
    @FXML
    private ComboBox<String> priorityFilterComboBox;

    // Details UI components
    @FXML
    private TextArea taskDetailsArea;

    // Table column UI components
    @FXML
    private TableColumn<Task, String> taskIdColumn;
    @FXML
    private TableColumn<Task, String> titleColumn;
    @FXML
    private TableColumn<Task, String> priorityColumn;
    @FXML
    private TableColumn<Task, String> statusColumn;
    @FXML
    private TableColumn<Task, String> dueDateColumn;
    @FXML
    private TableColumn<Task, String> createdDateColumn;
    @FXML
    private TableColumn<Task, String> categoryColumn;
    @FXML
    private TableColumn<Task, String> actionsColumn;

    // Data models
    private Employee currentEmployee;

    // Service instance (Singleton pattern)
    private TaskService taskService;

    // Data collections
    private ObservableList<Task> tasksList;

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
        initializeFilterComboBoxes();
        initializeTableColumns();

        System.out.println("OfficerTasksViewController initialized");
    }

    /**
     * Initializes service instances using Singleton pattern.
     */
    private void initializeServices() {
        taskService = TaskService.getInstance();
    }

    /**
     * Initializes data structures for the controller.
     */
    private void initializeDataStructures() {
        tasksList = FXCollections.observableArrayList();
    }

    /**
     * Initializes filter combo boxes with appropriate options.
     */
    private void initializeFilterComboBoxes() {
        if (statusFilterComboBox != null) {
            statusFilterComboBox.getItems().addAll("All", "Pending", "In Progress", "Completed");
            statusFilterComboBox.setValue("All");
        }

        if (priorityFilterComboBox != null) {
            priorityFilterComboBox.getItems().addAll("All", "Low", "Medium", "High", "Critical");
            priorityFilterComboBox.setValue("All");
        }
    }

    /**
     * Initializes table columns with cell value factories.
     */
    private void initializeTableColumns() {
        if (tasksTable != null) {
            configureTaskIdColumn();
            configureTitleColumn();
            configurePriorityColumn();
            configureStatusColumn();
            configureDueDateColumn();

            tasksTable.setPlaceholder(new Label("No tasks found"));
            tasksTable.setItems(tasksList);
        }
    }

    /**
     * Configures task ID column.
     */
    private void configureTaskIdColumn() {
        if (taskIdColumn != null) {
            taskIdColumn.setCellValueFactory(cellData -> {
                Task task = cellData.getValue();
                return new javafx.beans.property.SimpleStringProperty(
                        task.getTaskId() != null ? task.getTaskId() : "N/A"
                );
            });
        }
    }

    /**
     * Configures title column.
     */
    private void configureTitleColumn() {
        if (titleColumn != null) {
            titleColumn.setCellValueFactory(cellData -> {
                Task task = cellData.getValue();
                return new javafx.beans.property.SimpleStringProperty(
                        task.getTaskName() != null ? task.getTaskName() : "N/A"
                );
            });
        }
    }

    /**
     * Configures priority column.
     */
    private void configurePriorityColumn() {
        if (priorityColumn != null) {
            priorityColumn.setCellValueFactory(cellData -> {
                Task task = cellData.getValue();
                return new javafx.beans.property.SimpleStringProperty(
                        task.getPriority() != null ? task.getPriority() : "N/A"
                );
            });
        }
    }

    /**
     * Configures status column.
     */
    private void configureStatusColumn() {
        if (statusColumn != null) {
            statusColumn.setCellValueFactory(cellData -> {
                Task task = cellData.getValue();
                return new javafx.beans.property.SimpleStringProperty(
                        task.getStatus() != null ? task.getStatus() : "N/A"
                );
            });
        }
    }

    /**
     * Configures due date column.
     */
    private void configureDueDateColumn() {
        if (dueDateColumn != null) {
            dueDateColumn.setCellValueFactory(cellData -> {
                Task task = cellData.getValue();
                String dueDate = task.getDueDate() != null
                        ? task.getDueDate().toString() : "N/A";
                return new javafx.beans.property.SimpleStringProperty(dueDate);
            });
        }
    }

    /**
     * Sets the current employee and loads their tasks.
     *
     * @param employee The employee object representing the current user
     */
    public void setCurrentEmployee(Employee employee) {
        this.currentEmployee = employee;
        System.out.println("Employee set to " + (employee != null ? employee.getName() : "null"));
        loadOfficerTasks();
    }

    /**
     * Loads tasks for the current officer.
     */
    private void loadOfficerTasks() {
        if (isValidEmployee()) {
            loadTasksFromDatabase();
        }
    }

    /**
     * Checks if current employee is valid.
     *
     * @return true if employee is valid, false otherwise
     */
    private boolean isValidEmployee() {
        return currentEmployee != null && currentEmployee.getId() != null;
    }

    /**
     * Loads tasks from database with fallback to sample tasks.
     */
    private void loadTasksFromDatabase() {
        try {
            List<Task> tasks = taskService.getTasksByOfficer(currentEmployee.getId());
            tasksList.clear();

            if (tasks != null && !tasks.isEmpty()) {
                handleExistingTasks(tasks);
            } else {
                handleNoTasks();
            }

            updateStatistics(tasksList);
        } catch (Exception e) {
            handleTaskLoadingError(e);
        }
    }

    /**
     * Handles existing tasks from database.
     *
     * @param tasks List of tasks from database
     */
    private void handleExistingTasks(List<Task> tasks) {
        tasksList.addAll(tasks);
        System.out.println("Loaded " + tasks.size() + " tasks from database for " + currentEmployee.getName());
    }

    /**
     * Handles scenario when no tasks are found in database.
     */
    private void handleNoTasks() {
        System.out.println("No tasks found in database for " + currentEmployee.getName() + ". Creating sample tasks...");

        if (!hasExistingTasks()) {
            createAndSaveSampleTasks();
        } else {
            reloadTasksFromDatabase();
        }
    }

    /**
     * Checks if tasks exist for current employee.
     *
     * @return true if tasks exist, false otherwise
     */
    private boolean hasExistingTasks() {
        try {
            List<Task> tasks = taskService.getTasksByOfficer(currentEmployee.getId());
            return tasks != null && !tasks.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Reloads tasks from database.
     */
    private void reloadTasksFromDatabase() {
        List<Task> tasks = taskService.getTasksByOfficer(currentEmployee.getId());
        if (tasks != null && !tasks.isEmpty()) {
            tasksList.addAll(tasks);
            System.out.println("Reloaded " + tasks.size() + " tasks after creating samples");
        }
    }

    /**
     * Handles task loading errors.
     *
     * @param e The exception that occurred
     */
    private void handleTaskLoadingError(Exception e) {
        System.err.println("Error loading tasks: " + e.getMessage());
        e.printStackTrace();

        System.out.println("Creating in-memory sample tasks as fallback");
        createInMemorySampleTasks();
    }

    /**
     * Creates and saves sample tasks to database. Uses Factory pattern for task
     * creation.
     */
    private void createAndSaveSampleTasks() {
        try {
            tasksList.clear();

            Task[] sampleTasks = createSampleTaskArray();

            for (Task task : sampleTasks) {
                tasksList.add(task);
                saveTaskToDatabase(task);
            }

            System.out.println("Created and saved " + tasksList.size() + " sample tasks to database");
            updateStatistics(tasksList);
        } catch (Exception e) {
            System.err.println("Error creating sample tasks: " + e.getMessage());
        }
    }

    /**
     * Creates an array of sample tasks.
     *
     * @return Array of sample tasks
     */
    private Task[] createSampleTaskArray() {
        Task[] sampleTasks = new Task[3];

        sampleTasks[0] = createSampleTask(
                "Daily Security Inspection",
                "Complete security rounds in all blocks",
                "High",
                "Pending",
                "Security"
        );

        sampleTasks[1] = createSampleTask(
                "Visitor Log Review",
                "Review and verify visitor logs from yesterday",
                "Medium",
                "In Progress",
                "Administration"
        );

        sampleTasks[2] = createSampleTask(
                "Emergency Equipment Check",
                "Check all emergency equipment in assigned area",
                "Critical",
                "Pending",
                "Safety"
        );

        return sampleTasks;
    }

    /**
     * Creates a sample task with specified parameters. Uses Factory pattern for
     * task creation.
     *
     * @param name Task name
     * @param description Task description
     * @param priority Task priority
     * @param status Task status
     * @param category Task category
     * @return Created Task object
     */
    private Task createSampleTask(String name, String description, String priority,
            String status, String category) {
        Task task = new Task();
        task.setTaskId("TASK-" + System.currentTimeMillis() + "-" + System.currentTimeMillis() % 1000);
        task.setTaskName(name);
        task.setDescription(description);
        task.setPriority(priority);
        task.setStatus(status);
        task.setAssignedToId(currentEmployee.getId());
        task.setAssignedToName(currentEmployee.getName());
        task.setDueDate(java.time.LocalDate.now().plusDays(getDueDateOffsetForTask(name)));
        task.setCategory(category);
        task.setCreatedAt(java.time.LocalDate.now());

        return task;
    }

    /**
     * Gets due date offset based on task type.
     *
     * @param taskName The task name
     * @return Number of days to add to current date
     */
    private int getDueDateOffsetForTask(String taskName) {
        switch (taskName) {
            case "Daily Security Inspection":
                return 1;
            case "Visitor Log Review":
                return 2;
            case "Emergency Equipment Check":
                return 0;
            default:
                return 1;
        }
    }

    /**
     * Saves a task to the database.
     *
     * @param task The task to save
     */
    private void saveTaskToDatabase(Task task) {
        try {
            boolean saved = taskService.addTask(task);
            if (saved) {
                System.out.println("Saved task to database: " + task.getTaskId());
            }
        } catch (Exception e) {
            System.err.println("Failed to save task to database: " + e.getMessage());
        }
    }

    /**
     * Creates in-memory sample tasks for fallback scenario.
     */
    private void createInMemorySampleTasks() {
        // Implementation for creating tasks only in memory (without database persistence)
    }

    /**
     * Updates task statistics labels.
     *
     * @param tasks List of tasks to calculate statistics from
     */
    private void updateStatistics(List<Task> tasks) {
        TaskStatistics statistics = calculateTaskStatistics(tasks);
        updateStatisticsLabels(statistics);
    }

    /**
     * Calculates task statistics.
     *
     * @param tasks List of tasks
     * @return TaskStatistics object with calculated counts
     */
    private TaskStatistics calculateTaskStatistics(List<Task> tasks) {
        int pending = 0, inProgress = 0, completed = 0, overdue = 0;

        for (Task task : tasks) {
            String status = task.getStatus();
            switch (status) {
                case "Pending":
                    pending++;
                    break;
                case "In Progress":
                    inProgress++;
                    break;
                case "Completed":
                    completed++;
                    break;
                case "Overdue":
                    overdue++;
                    break;
            }
        }

        return new TaskStatistics(pending, inProgress, completed, overdue);
    }

    /**
     * Updates statistics labels with calculated values.
     *
     * @param statistics Task statistics to display
     */
    private void updateStatisticsLabels(TaskStatistics statistics) {
        if (pendingTasksLabel != null) {
            pendingTasksLabel.setText(String.valueOf(statistics.getPendingCount()));
        }
        if (inProgressTasksLabel != null) {
            inProgressTasksLabel.setText(String.valueOf(statistics.getInProgressCount()));
        }
        if (completedTasksLabel != null) {
            completedTasksLabel.setText(String.valueOf(statistics.getCompletedCount()));
        }
        if (overdueTasksLabel != null) {
            overdueTasksLabel.setText(String.valueOf(statistics.getOverdueCount()));
        }
    }

    // FXML Action Handlers
    /**
     * Handles refresh action to reload tasks.
     */
    @FXML
    private void handleRefresh() {
        System.out.println("Refresh clicked");
        loadOfficerTasks();
    }

    /**
     * Handles clear filters action.
     */
    @FXML
    private void clearFilters() {
        System.out.println("Clear filters clicked");
        if (statusFilterComboBox != null) {
            statusFilterComboBox.setValue("All");
        }
        if (priorityFilterComboBox != null) {
            priorityFilterComboBox.setValue("All");
        }
        loadOfficerTasks();
    }

    /**
     * Handles update task status action.
     */
    @FXML
    private void handleUpdateTaskStatus() {
        Task selectedTask = tasksTable.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            showStatusUpdateDialog(selectedTask);
        } else {
            showAlert("Warning", "Please select a task.", Alert.AlertType.WARNING);
        }
    }

    /**
     * Shows dialog for updating task status.
     *
     * @param task The task to update
     */
    private void showStatusUpdateDialog(Task task) {
        ChoiceDialog<String> dialog = new ChoiceDialog<>("Pending",
                "Pending", "In Progress", "Completed", "Cancelled");
        dialog.setTitle("Update Task Status");
        dialog.setHeaderText("Update Status for: " + task.getTaskName());
        dialog.setContentText("Select new status:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(newStatus -> {
            updateTaskStatusInDatabase(task.getTaskId(), newStatus);
        });
    }

    /**
     * Updates task status in database.
     *
     * @param taskId The ID of the task to update
     * @param newStatus The new status to set
     */
    private void updateTaskStatusInDatabase(String taskId, String newStatus) {
        try {
            boolean success = taskService.updateTaskStatus(taskId, newStatus);
            if (success) {
                showAlert("Success", "Task status updated to: " + newStatus, Alert.AlertType.INFORMATION);
                loadOfficerTasks();
            }
        } catch (Exception e) {
            showAlert("Error", "Failed to update task: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    /**
     * Handles mark task as complete action.
     */
    @FXML
    private void handleMarkComplete() {
        Task selectedTask = tasksTable.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            updateTaskStatusInDatabase(selectedTask.getTaskId(), "Completed");
        } else {
            showAlert("Warning", "Please select a task to mark as complete.", Alert.AlertType.WARNING);
        }
    }

    /**
     * Handles close window action.
     */
    @FXML
    private void closeWindow() {
        Stage stage = (Stage) tasksTable.getScene().getWindow();
        stage.close();
    }

    /**
     * Shows an alert dialog with specified parameters.
     *
     * @param title The alert title
     * @param message The alert message
     * @param type The type of alert
     */
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Helper class to encapsulate task statistics.
     */
    private static class TaskStatistics {

        private final int pendingCount;
        private final int inProgressCount;
        private final int completedCount;
        private final int overdueCount;

        /**
         * Creates a new TaskStatistics instance.
         *
         * @param pendingCount Number of pending tasks
         * @param inProgressCount Number of in-progress tasks
         * @param completedCount Number of completed tasks
         * @param overdueCount Number of overdue tasks
         */
        public TaskStatistics(int pendingCount, int inProgressCount,
                int completedCount, int overdueCount) {
            this.pendingCount = pendingCount;
            this.inProgressCount = inProgressCount;
            this.completedCount = completedCount;
            this.overdueCount = overdueCount;
        }

        public int getPendingCount() {
            return pendingCount;
        }

        public int getInProgressCount() {
            return inProgressCount;
        }

        public int getCompletedCount() {
            return completedCount;
        }

        public int getOverdueCount() {
            return overdueCount;
        }
    }
}
