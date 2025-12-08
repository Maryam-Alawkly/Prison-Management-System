package model;

import javafx.beans.property.*;
import java.time.LocalDate;

/**
 * Task class represents a task or assignment in the prison management system.
 * This class manages task information including assignment details, deadlines,
 * priorities, and completion tracking. It uses JavaFX properties for data
 * binding with UI components.
 */
public class Task {

    // Task status constants
    public static final String STATUS_PENDING = "Pending";
    public static final String STATUS_IN_PROGRESS = "In Progress";
    public static final String STATUS_COMPLETED = "Completed";
    public static final String STATUS_CANCELLED = "Cancelled";
    public static final String STATUS_OVERDUE = "Overdue";

    // Priority level constants
    public static final String PRIORITY_LOW = "Low";
    public static final String PRIORITY_MEDIUM = "Medium";
    public static final String PRIORITY_HIGH = "High";
    public static final String PRIORITY_CRITICAL = "Critical";

    // Task category constants
    public static final String CATEGORY_SECURITY = "Security";
    public static final String CATEGORY_MAINTENANCE = "Maintenance";
    public static final String CATEGORY_ADMINISTRATIVE = "Administrative";
    public static final String CATEGORY_OPERATIONAL = "Operational";
    public static final String CATEGORY_EMERGENCY = "Emergency";

    // Task properties
    private final StringProperty taskId;
    private final StringProperty taskName;
    private final StringProperty description;
    private final StringProperty assignedToId;
    private final StringProperty assignedToName;
    private final StringProperty priority;
    private final StringProperty status;
    private final ObjectProperty<LocalDate> dueDate;
    private final ObjectProperty<LocalDate> completedDate;
    private final ObjectProperty<LocalDate> createdAt;
    private final StringProperty createdBy;
    private final StringProperty completedBy;
    private final StringProperty completionNotes;
    private final StringProperty category;
    private final IntegerProperty estimatedHours;

    /**
     * Default constructor initializes all properties with default values.
     * Creates a new Task with empty properties and default values.
     */
    public Task() {
        this.taskId = new SimpleStringProperty("");
        this.taskName = new SimpleStringProperty("");
        this.description = new SimpleStringProperty("");
        this.assignedToId = new SimpleStringProperty("");
        this.assignedToName = new SimpleStringProperty("");
        this.priority = new SimpleStringProperty(PRIORITY_MEDIUM);
        this.status = new SimpleStringProperty(STATUS_PENDING);
        this.dueDate = new SimpleObjectProperty<>();
        this.completedDate = new SimpleObjectProperty<>();
        this.createdAt = new SimpleObjectProperty<>(LocalDate.now());
        this.createdBy = new SimpleStringProperty("");
        this.completedBy = new SimpleStringProperty("");
        this.completionNotes = new SimpleStringProperty("");
        this.category = new SimpleStringProperty(CATEGORY_OPERATIONAL);
        this.estimatedHours = new SimpleIntegerProperty(0);
    }

    // =================== Getters and Setters ===================
    /**
     * Gets the unique task identifier.
     *
     * @return The task ID
     */
    public String getTaskId() {
        return taskId.get();
    }

    /**
     * Sets the unique task identifier.
     *
     * @param taskId The task ID to set
     */
    public void setTaskId(String taskId) {
        this.taskId.set(taskId);
    }

    /**
     * Gets the task ID property for JavaFX binding.
     *
     * @return The task ID property
     */
    public StringProperty taskIdProperty() {
        return taskId;
    }

    /**
     * Gets the name/title of the task.
     *
     * @return The task name
     */
    public String getTaskName() {
        return taskName.get();
    }

    /**
     * Sets the name/title of the task.
     *
     * @param taskName The task name to set
     */
    public void setTaskName(String taskName) {
        this.taskName.set(taskName);
    }

    /**
     * Gets the task name property for JavaFX binding.
     *
     * @return The task name property
     */
    public StringProperty taskNameProperty() {
        return taskName;
    }

    /**
     * Gets the title of the task (alias for getTaskName).
     *
     * @return The task title
     */
    public String getTitle() {
        return taskName.get();
    }

    /**
     * Sets the title of the task (alias for setTaskName).
     *
     * @param title The task title to set
     */
    public void setTitle(String title) {
        this.taskName.set(title);
    }

    /**
     * Gets the detailed description of the task.
     *
     * @return The task description
     */
    public String getDescription() {
        return description.get();
    }

    /**
     * Sets the detailed description of the task.
     *
     * @param description The task description to set
     */
    public void setDescription(String description) {
        this.description.set(description);
    }

    /**
     * Gets the description property for JavaFX binding.
     *
     * @return The description property
     */
    public StringProperty descriptionProperty() {
        return description;
    }

    /**
     * Gets the ID of the employee assigned to this task.
     *
     * @return The assigned employee ID
     */
    public String getAssignedToId() {
        return assignedToId.get();
    }

    /**
     * Sets the ID of the employee assigned to this task.
     *
     * @param assignedToId The assigned employee ID to set
     */
    public void setAssignedToId(String assignedToId) {
        this.assignedToId.set(assignedToId);
    }

    /**
     * Gets the assigned employee ID property for JavaFX binding.
     *
     * @return The assigned employee ID property
     */
    public StringProperty assignedToIdProperty() {
        return assignedToId;
    }

    /**
     * Gets the name of the employee assigned to this task.
     *
     * @return The assigned employee name
     */
    public String getAssignedToName() {
        return assignedToName.get();
    }

    /**
     * Sets the name of the employee assigned to this task.
     *
     * @param assignedToName The assigned employee name to set
     */
    public void setAssignedToName(String assignedToName) {
        this.assignedToName.set(assignedToName);
    }

    /**
     * Gets the assigned employee name property for JavaFX binding.
     *
     * @return The assigned employee name property
     */
    public StringProperty assignedToNameProperty() {
        return assignedToName;
    }

    /**
     * Gets the priority level of the task.
     *
     * @return The task priority
     */
    public String getPriority() {
        return priority.get();
    }

    /**
     * Sets the priority level of the task.
     *
     * @param priority The task priority to set
     */
    public void setPriority(String priority) {
        this.priority.set(priority);
    }

    /**
     * Gets the priority property for JavaFX binding.
     *
     * @return The priority property
     */
    public StringProperty priorityProperty() {
        return priority;
    }

    /**
     * Gets the current status of the task.
     *
     * @return The task status
     */
    public String getStatus() {
        return status.get();
    }

    /**
     * Sets the current status of the task.
     *
     * @param status The task status to set
     */
    public void setStatus(String status) {
        this.status.set(status);
    }

    /**
     * Gets the status property for JavaFX binding.
     *
     * @return The status property
     */
    public StringProperty statusProperty() {
        return status;
    }

    /**
     * Gets the due date for task completion.
     *
     * @return The due date, or null if not set
     */
    public LocalDate getDueDate() {
        return dueDate.get();
    }

    /**
     * Sets the due date for task completion.
     *
     * @param dueDate The due date to set
     */
    public void setDueDate(LocalDate dueDate) {
        this.dueDate.set(dueDate);
    }

    /**
     * Gets the due date property for JavaFX binding.
     *
     * @return The due date property
     */
    public ObjectProperty<LocalDate> dueDateProperty() {
        return dueDate;
    }

    /**
     * Gets the date when the task was completed.
     *
     * @return The completion date, or null if not completed
     */
    public LocalDate getCompletedDate() {
        return completedDate.get();
    }

    /**
     * Sets the date when the task was completed.
     *
     * @param completedDate The completion date to set
     */
    public void setCompletedDate(LocalDate completedDate) {
        this.completedDate.set(completedDate);
    }

    /**
     * Gets the completed date property for JavaFX binding.
     *
     * @return The completed date property
     */
    public ObjectProperty<LocalDate> completedDateProperty() {
        return completedDate;
    }

    /**
     * Gets the date when the task was created.
     *
     * @return The creation date
     */
    public LocalDate getCreatedAt() {
        return createdAt.get();
    }

    /**
     * Sets the date when the task was created.
     *
     * @param createdAt The creation date to set
     */
    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt.set(createdAt);
    }

    /**
     * Gets the creation date property for JavaFX binding.
     *
     * @return The creation date property
     */
    public ObjectProperty<LocalDate> createdAtProperty() {
        return createdAt;
    }

    /**
     * Gets the user who created the task.
     *
     * @return The creator identifier
     */
    public String getCreatedBy() {
        return createdBy.get();
    }

    /**
     * Sets the user who created the task.
     *
     * @param createdBy The creator identifier to set
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy.set(createdBy);
    }

    /**
     * Gets the created by property for JavaFX binding.
     *
     * @return The created by property
     */
    public StringProperty createdByProperty() {
        return createdBy;
    }

    /**
     * Gets the user who completed the task.
     *
     * @return The completer identifier, or null if not completed
     */
    public String getCompletedBy() {
        return completedBy.get();
    }

    /**
     * Sets the user who completed the task.
     *
     * @param completedBy The completer identifier to set
     */
    public void setCompletedBy(String completedBy) {
        this.completedBy.set(completedBy);
    }

    /**
     * Gets the completed by property for JavaFX binding.
     *
     * @return The completed by property
     */
    public StringProperty completedByProperty() {
        return completedBy;
    }

    /**
     * Gets the notes about task completion.
     *
     * @return The completion notes, or null if not completed or no notes
     */
    public String getCompletionNotes() {
        return completionNotes.get();
    }

    /**
     * Sets the notes about task completion.
     *
     * @param completionNotes The completion notes to set
     */
    public void setCompletionNotes(String completionNotes) {
        this.completionNotes.set(completionNotes);
    }

    /**
     * Gets the completion notes property for JavaFX binding.
     *
     * @return The completion notes property
     */
    public StringProperty completionNotesProperty() {
        return completionNotes;
    }

    /**
     * Gets the category of the task.
     *
     * @return The task category
     */
    public String getCategory() {
        return category.get();
    }

    /**
     * Sets the category of the task.
     *
     * @param category The task category to set
     */
    public void setCategory(String category) {
        this.category.set(category);
    }

    /**
     * Gets the category property for JavaFX binding.
     *
     * @return The category property
     */
    public StringProperty categoryProperty() {
        return category;
    }

    /**
     * Gets the estimated hours required to complete the task.
     *
     * @return The estimated hours
     */
    public int getEstimatedHours() {
        return estimatedHours.get();
    }

    /**
     * Sets the estimated hours required to complete the task.
     *
     * @param estimatedHours The estimated hours to set
     */
    public void setEstimatedHours(int estimatedHours) {
        this.estimatedHours.set(estimatedHours);
    }

    /**
     * Gets the estimated hours property for JavaFX binding.
     *
     * @return The estimated hours property
     */
    public IntegerProperty estimatedHoursProperty() {
        return estimatedHours;
    }

    // =================== Business Logic Methods ===================
    /**
     * Gets the formatted due date as a string. Returns "Not set" if due date is
     * null.
     *
     * @return Formatted due date, or "Not set" if null
     */
    public String getFormattedDueDate() {
        return dueDate.get() != null ? dueDate.get().toString() : "Not set";
    }

    /**
     * Checks if the task is overdue. A task is overdue if the current date is
     * past the due date and the task is not completed.
     *
     * @return True if task is overdue, false otherwise
     */
    public boolean isOverdue() {
        if (dueDate.get() == null || STATUS_COMPLETED.equals(status.get())) {
            return false;
        }
        return dueDate.get().isBefore(LocalDate.now());
    }

    /**
     * Checks if the task is currently active. A task is active if it's pending
     * or in progress.
     *
     * @return True if task is active, false otherwise
     */
    public boolean isActive() {
        return STATUS_PENDING.equals(getStatus())
                || STATUS_IN_PROGRESS.equals(getStatus());
    }

    /**
     * Checks if the task has been completed.
     *
     * @return True if task is completed, false otherwise
     */
    public boolean isCompleted() {
        return STATUS_COMPLETED.equals(getStatus());
    }

    /**
     * Marks the task as completed with completion details. Sets status to
     * "Completed" and updates completion timestamp.
     *
     * @param completedBy User who completed the task
     * @param notes Notes about the completion
     */
    public void markAsCompleted(String completedBy, String notes) {
        this.status.set(STATUS_COMPLETED);
        this.completedBy.set(completedBy);
        this.completionNotes.set(notes);
        this.completedDate.set(LocalDate.now());
    }

    /**
     * Marks the task as in progress. Sets status to "In Progress" if currently
     * pending.
     *
     * @throws IllegalStateException if task is not in pending status
     */
    public void markAsInProgress() {
        if (!STATUS_PENDING.equals(getStatus())) {
            throw new IllegalStateException("Only pending tasks can be marked as in progress");
        }
        this.status.set(STATUS_IN_PROGRESS);
    }

    /**
     * Gets the number of days until the task is due. Returns negative number if
     * task is overdue.
     *
     * @return Days until due date, or null if no due date set
     */
    public Integer getDaysUntilDue() {
        if (dueDate.get() == null) {
            return null;
        }
        return (int) java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), dueDate.get());
    }

    /**
     * Checks if the task configuration is valid. Validates all required fields
     * are properly set.
     *
     * @return True if task configuration is valid, false otherwise
     */
    public boolean isValid() {
        return taskId.get() != null && !taskId.get().trim().isEmpty()
                && taskName.get() != null && !taskName.get().trim().isEmpty()
                && status.get() != null && !status.get().trim().isEmpty()
                && priority.get() != null && !priority.get().trim().isEmpty()
                && category.get() != null && !category.get().trim().isEmpty()
                && estimatedHours.get() >= 0;
    }

    /**
     * Gets a summary of the task for display purposes.
     *
     * @return Task summary string
     */
    public String getTaskSummary() {
        return String.format("%s: %s (%s)",
                getTaskId(), getTaskName(), getStatus());
    }

    /**
     * Returns a string representation of the task.
     *
     * @return String representation of the task
     */
    @Override
    public String toString() {
        return String.format("Task[ID=%s, Name=%s, Status=%s, Due=%s]",
                getTaskId(), getTaskName(), getStatus(), getFormattedDueDate());
    }
}
