package model;

import javafx.beans.property.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * GuardDuty class represents a guard duty assignment in the prison management
 * system. It contains information about duty assignments including officer
 * details, duty schedule, location, and status. This class uses JavaFX
 * properties for data binding with UI components.
 */
public class GuardDuty {

    // Constants for duty types
    public static final String TYPE_PATROL = "Patrol";
    public static final String TYPE_GATE_DUTY = "Gate Duty";
    public static final String TYPE_TOWER_WATCH = "Tower Watch";
    public static final String TYPE_CONTROL_ROOM = "Control Room";
    public static final String TYPE_CELL_INSPECTION = "Cell Inspection";
    public static final String TYPE_VISITOR_SCREENING = "Visitor Screening";
    public static final String TYPE_SPECIAL_ASSIGNMENT = "Special Assignment";

    // Constants for duty status
    public static final String STATUS_SCHEDULED = "Scheduled";
    public static final String STATUS_IN_PROGRESS = "In Progress";
    public static final String STATUS_COMPLETED = "Completed";
    public static final String STATUS_CANCELLED = "Cancelled";
    public static final String STATUS_OVERDUE = "Overdue";

    // Constants for priority levels
    public static final String PRIORITY_LOW = "Low";
    public static final String PRIORITY_MEDIUM = "Medium";
    public static final String PRIORITY_HIGH = "High";
    public static final String PRIORITY_CRITICAL = "Critical";

    // Time format for parsing and display
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    // Core duty properties
    private final StringProperty dutyId;
    private final StringProperty officerId;
    private final StringProperty officerName;
    private final StringProperty dutyType;
    private final StringProperty location;
    private final StringProperty startTime;
    private final StringProperty endTime;
    private final ObjectProperty<LocalDate> date;
    private final StringProperty status;
    private final StringProperty priority;
    private final StringProperty completedTime;
    private final StringProperty notes;

    /**
     * Default constructor initializes all properties with default values.
     * Creates a new GuardDuty with empty properties and default
     * status/priority.
     */
    public GuardDuty() {
        this.dutyId = new SimpleStringProperty("");
        this.officerId = new SimpleStringProperty("");
        this.officerName = new SimpleStringProperty("");
        this.dutyType = new SimpleStringProperty("");
        this.location = new SimpleStringProperty("");
        this.startTime = new SimpleStringProperty("");
        this.endTime = new SimpleStringProperty("");
        this.date = new SimpleObjectProperty<>(LocalDate.now());
        this.status = new SimpleStringProperty(STATUS_SCHEDULED);
        this.priority = new SimpleStringProperty(PRIORITY_MEDIUM);
        this.completedTime = new SimpleStringProperty("");
        this.notes = new SimpleStringProperty("");
    }

    /**
     * Constructs a GuardDuty with specified parameters.
     *
     * @param dutyId Unique identifier for the duty assignment
     * @param officerId ID of the assigned officer
     * @param officerName Name of the assigned officer
     * @param dutyType Type of duty assignment
     * @param location Location where duty will be performed
     * @param date Date of the duty assignment
     * @param startTime Start time of the duty (HH:mm format)
     * @param endTime End time of the duty (HH:mm format)
     * @param priority Priority level of the duty
     * @throws IllegalArgumentException if any required parameter is invalid
     */
    public GuardDuty(String dutyId, String officerId, String officerName,
            String dutyType, String location, LocalDate date,
            String startTime, String endTime, String priority) {
        validateParameters(dutyId, officerId, officerName, dutyType, location,
                date, startTime, endTime, priority);

        this.dutyId = new SimpleStringProperty(dutyId);
        this.officerId = new SimpleStringProperty(officerId);
        this.officerName = new SimpleStringProperty(officerName);
        this.dutyType = new SimpleStringProperty(dutyType);
        this.location = new SimpleStringProperty(location);
        this.startTime = new SimpleStringProperty(startTime);
        this.endTime = new SimpleStringProperty(endTime);
        this.date = new SimpleObjectProperty<>(date);
        this.status = new SimpleStringProperty(STATUS_SCHEDULED);
        this.priority = new SimpleStringProperty(priority);
        this.completedTime = new SimpleStringProperty("");
        this.notes = new SimpleStringProperty("");
    }

    /**
     * Validates constructor parameters.
     *
     * @param dutyId Duty ID to validate
     * @param officerId Officer ID to validate
     * @param officerName Officer name to validate
     * @param dutyType Duty type to validate
     * @param location Location to validate
     * @param date Date to validate
     * @param startTime Start time to validate
     * @param endTime End time to validate
     * @param priority Priority to validate
     */
    private void validateParameters(String dutyId, String officerId, String officerName,
            String dutyType, String location, LocalDate date,
            String startTime, String endTime, String priority) {
        if (dutyId == null || dutyId.trim().isEmpty()) {
            throw new IllegalArgumentException("Duty ID cannot be null or empty");
        }
        if (officerId == null || officerId.trim().isEmpty()) {
            throw new IllegalArgumentException("Officer ID cannot be null or empty");
        }
        if (officerName == null || officerName.trim().isEmpty()) {
            throw new IllegalArgumentException("Officer name cannot be null or empty");
        }
        if (dutyType == null || dutyType.trim().isEmpty()) {
            throw new IllegalArgumentException("Duty type cannot be null or empty");
        }
        if (location == null || location.trim().isEmpty()) {
            throw new IllegalArgumentException("Location cannot be null or empty");
        }
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }
        if (startTime == null || !isValidTimeFormat(startTime)) {
            throw new IllegalArgumentException("Invalid start time format. Expected HH:mm");
        }
        if (endTime == null || !isValidTimeFormat(endTime)) {
            throw new IllegalArgumentException("Invalid end time format. Expected HH:mm");
        }
        if (priority == null || priority.trim().isEmpty()) {
            throw new IllegalArgumentException("Priority cannot be null or empty");
        }

        // Validate time logic
        if (!isStartTimeBeforeEndTime(startTime, endTime)) {
            throw new IllegalArgumentException("Start time must be before end time");
        }
    }

    // =================== Core Property Getters and Setters ===================
    /**
     * Gets the unique duty identifier.
     *
     * @return The duty ID
     */
    public String getDutyId() {
        return dutyId.get();
    }

    /**
     * Sets the unique duty identifier.
     *
     * @param dutyId The duty ID to set
     * @throws IllegalArgumentException if dutyId is null or empty
     */
    public void setDutyId(String dutyId) {
        if (dutyId == null || dutyId.trim().isEmpty()) {
            throw new IllegalArgumentException("Duty ID cannot be null or empty");
        }
        this.dutyId.set(dutyId);
    }

    /**
     * Gets the duty ID property for JavaFX binding.
     *
     * @return The duty ID property
     */
    public StringProperty dutyIdProperty() {
        return dutyId;
    }

    /**
     * Gets the officer ID assigned to this duty.
     *
     * @return The officer ID
     */
    public String getOfficerId() {
        return officerId.get();
    }

    /**
     * Sets the officer ID assigned to this duty.
     *
     * @param officerId The officer ID to set
     * @throws IllegalArgumentException if officerId is null or empty
     */
    public void setOfficerId(String officerId) {
        if (officerId == null || officerId.trim().isEmpty()) {
            throw new IllegalArgumentException("Officer ID cannot be null or empty");
        }
        this.officerId.set(officerId);
    }

    /**
     * Gets the officer ID property for JavaFX binding.
     *
     * @return The officer ID property
     */
    public StringProperty officerIdProperty() {
        return officerId;
    }

    /**
     * Gets the officer name assigned to this duty.
     *
     * @return The officer name
     */
    public String getOfficerName() {
        return officerName.get();
    }

    /**
     * Sets the officer name assigned to this duty.
     *
     * @param officerName The officer name to set
     * @throws IllegalArgumentException if officerName is null or empty
     */
    public void setOfficerName(String officerName) {
        if (officerName == null || officerName.trim().isEmpty()) {
            throw new IllegalArgumentException("Officer name cannot be null or empty");
        }
        this.officerName.set(officerName);
    }

    /**
     * Gets the officer name property for JavaFX binding.
     *
     * @return The officer name property
     */
    public StringProperty officerNameProperty() {
        return officerName;
    }

    /**
     * Gets the type of duty assignment.
     *
     * @return The duty type
     */
    public String getDutyType() {
        return dutyType.get();
    }

    /**
     * Sets the type of duty assignment.
     *
     * @param dutyType The duty type to set
     * @throws IllegalArgumentException if dutyType is null or empty
     */
    public void setDutyType(String dutyType) {
        if (dutyType == null || dutyType.trim().isEmpty()) {
            throw new IllegalArgumentException("Duty type cannot be null or empty");
        }
        this.dutyType.set(dutyType);
    }

    /**
     * Gets the duty type property for JavaFX binding.
     *
     * @return The duty type property
     */
    public StringProperty dutyTypeProperty() {
        return dutyType;
    }

    /**
     * Gets the location where the duty will be performed.
     *
     * @return The duty location
     */
    public String getLocation() {
        return location.get();
    }

    /**
     * Sets the location where the duty will be performed.
     *
     * @param location The location to set
     * @throws IllegalArgumentException if location is null or empty
     */
    public void setLocation(String location) {
        if (location == null || location.trim().isEmpty()) {
            throw new IllegalArgumentException("Location cannot be null or empty");
        }
        this.location.set(location);
    }

    /**
     * Gets the location property for JavaFX binding.
     *
     * @return The location property
     */
    public StringProperty locationProperty() {
        return location;
    }

    /**
     * Gets the start time of the duty.
     *
     * @return The start time in HH:mm format
     */
    public String getStartTime() {
        return startTime.get();
    }

    /**
     * Sets the start time of the duty.
     *
     * @param startTime The start time to set (HH:mm format)
     * @throws IllegalArgumentException if startTime is null or invalid format
     */
    public void setStartTime(String startTime) {
        if (startTime == null || !isValidTimeFormat(startTime)) {
            throw new IllegalArgumentException("Invalid start time format. Expected HH:mm");
        }
        this.startTime.set(startTime);
    }

    /**
     * Gets the start time property for JavaFX binding.
     *
     * @return The start time property
     */
    public StringProperty startTimeProperty() {
        return startTime;
    }

    /**
     * Gets the end time of the duty.
     *
     * @return The end time in HH:mm format
     */
    public String getEndTime() {
        return endTime.get();
    }

    /**
     * Sets the end time of the duty.
     *
     * @param endTime The end time to set (HH:mm format)
     * @throws IllegalArgumentException if endTime is null or invalid format
     */
    public void setEndTime(String endTime) {
        if (endTime == null || !isValidTimeFormat(endTime)) {
            throw new IllegalArgumentException("Invalid end time format. Expected HH:mm");
        }
        this.endTime.set(endTime);
    }

    /**
     * Gets the end time property for JavaFX binding.
     *
     * @return The end time property
     */
    public StringProperty endTimeProperty() {
        return endTime;
    }

    /**
     * Gets the date of the duty assignment.
     *
     * @return The duty date
     */
    public LocalDate getDate() {
        return date.get();
    }

    /**
     * Sets the date of the duty assignment.
     *
     * @param date The date to set
     * @throws IllegalArgumentException if date is null
     */
    public void setDate(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }
        this.date.set(date);
    }

    /**
     * Gets the date property for JavaFX binding.
     *
     * @return The date property
     */
    public ObjectProperty<LocalDate> dateProperty() {
        return date;
    }

    /**
     * Gets the current status of the duty.
     *
     * @return The duty status
     */
    public String getStatus() {
        return status.get();
    }

    /**
     * Sets the current status of the duty.
     *
     * @param status The status to set
     * @throws IllegalArgumentException if status is null or empty
     */
    public void setStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("Status cannot be null or empty");
        }
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
     * Gets the priority level of the duty.
     *
     * @return The duty priority
     */
    public String getPriority() {
        return priority.get();
    }

    /**
     * Sets the priority level of the duty.
     *
     * @param priority The priority to set
     * @throws IllegalArgumentException if priority is null or empty
     */
    public void setPriority(String priority) {
        if (priority == null || priority.trim().isEmpty()) {
            throw new IllegalArgumentException("Priority cannot be null or empty");
        }
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
     * Gets the time when the duty was completed.
     *
     * @return The completion time in HH:mm format, or empty string if not
     * completed
     */
    public String getCompletedTime() {
        return completedTime.get();
    }

    /**
     * Sets the time when the duty was completed.
     *
     * @param completedTime The completion time to set (HH:mm format)
     */
    public void setCompletedTime(String completedTime) {
        if (completedTime != null && !completedTime.trim().isEmpty()
                && !isValidTimeFormat(completedTime)) {
            throw new IllegalArgumentException("Invalid completed time format. Expected HH:mm");
        }
        this.completedTime.set(completedTime);
    }

    /**
     * Gets the completed time property for JavaFX binding.
     *
     * @return The completed time property
     */
    public StringProperty completedTimeProperty() {
        return completedTime;
    }

    /**
     * Gets additional notes about the duty.
     *
     * @return The duty notes
     */
    public String getNotes() {
        return notes.get();
    }

    /**
     * Sets additional notes about the duty.
     *
     * @param notes The notes to set
     */
    public void setNotes(String notes) {
        this.notes.set(notes);
    }

    /**
     * Gets the notes property for JavaFX binding.
     *
     * @return The notes property
     */
    public StringProperty notesProperty() {
        return notes;
    }

    // =================== Business Logic Methods ===================
    /**
     * Checks if the duty is currently active based on current time. A duty is
     * active if current time is between start and end times on the duty date.
     *
     * @return True if duty is currently active, false otherwise
     */
    public boolean isCurrentlyActive() {
        if (!STATUS_IN_PROGRESS.equals(getStatus())) {
            return false;
        }

        try {
            LocalTime now = LocalTime.now();
            LocalTime start = LocalTime.parse(getStartTime(), TIME_FORMATTER);
            LocalTime end = LocalTime.parse(getEndTime(), TIME_FORMATTER);

            return now.isAfter(start) && now.isBefore(end);
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Checks if the duty is overdue. A duty is overdue if current time is after
     * end time and status is not completed.
     *
     * @return True if duty is overdue, false otherwise
     */
    public boolean isOverdue() {
        if (STATUS_COMPLETED.equals(getStatus()) || STATUS_CANCELLED.equals(getStatus())) {
            return false;
        }

        try {
            LocalDate today = LocalDate.now();
            LocalTime now = LocalTime.now();

            // If duty date is in the past, it's overdue
            if (getDate().isBefore(today)) {
                return true;
            }

            // If duty date is today and current time is after end time
            if (getDate().isEqual(today)) {
                LocalTime end = LocalTime.parse(getEndTime(), TIME_FORMATTER);
                return now.isAfter(end);
            }

            return false;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Calculates the duration of the duty in minutes.
     *
     * @return Duration in minutes, or 0 if times are invalid
     */
    public int calculateDurationMinutes() {
        try {
            LocalTime start = LocalTime.parse(getStartTime(), TIME_FORMATTER);
            LocalTime end = LocalTime.parse(getEndTime(), TIME_FORMATTER);

            if (end.isBefore(start)) {
                // Handle overnight duties
                end = end.plusHours(24);
            }

            return (int) java.time.Duration.between(start, end).toMinutes();
        } catch (DateTimeParseException e) {
            return 0;
        }
    }

    /**
     * Gets the duration in a human-readable format (e.g., "2h 30m").
     *
     * @return Formatted duration string
     */
    public String getFormattedDuration() {
        int totalMinutes = calculateDurationMinutes();
        if (totalMinutes <= 0) {
            return "0h";
        }

        int hours = totalMinutes / 60;
        int minutes = totalMinutes % 60;

        if (minutes == 0) {
            return String.format("%dh", hours);
        } else {
            return String.format("%dh %dm", hours, minutes);
        }
    }

    /**
     * Marks the duty as in progress. Only allowed if duty is currently
     * scheduled.
     *
     * @throws IllegalStateException if duty is not in scheduled status
     */
    public void markAsInProgress() {
        if (!STATUS_SCHEDULED.equals(getStatus())) {
            throw new IllegalStateException("Only scheduled duties can be marked as in progress");
        }
        setStatus(STATUS_IN_PROGRESS);
    }

    /**
     * Marks the duty as completed. Sets completion time to current time.
     *
     * @throws IllegalStateException if duty is not in progress
     */
    public void markAsCompleted() {
        if (!STATUS_IN_PROGRESS.equals(getStatus())) {
            throw new IllegalStateException("Only duties in progress can be marked as completed");
        }
        setStatus(STATUS_COMPLETED);
        setCompletedTime(LocalTime.now().format(TIME_FORMATTER));
    }

    /**
     * Cancels the duty assignment.
     *
     * @param reason Reason for cancellation (optional)
     */
    public void cancelDuty(String reason) {
        setStatus(STATUS_CANCELLED);
        if (reason != null && !reason.trim().isEmpty()) {
            setNotes(getNotes() + "\nCancellation reason: " + reason);
        }
    }

    /**
     * Checks if the duty assignment is valid. Validates all required fields and
     * time logic.
     *
     * @return True if duty is valid, false otherwise
     */
    public boolean isValid() {
        return dutyId.get() != null && !dutyId.get().trim().isEmpty()
                && officerId.get() != null && !officerId.get().trim().isEmpty()
                && officerName.get() != null && !officerName.get().trim().isEmpty()
                && dutyType.get() != null && !dutyType.get().trim().isEmpty()
                && location.get() != null && !location.get().trim().isEmpty()
                && date.get() != null
                && isValidTimeFormat(getStartTime())
                && isValidTimeFormat(getEndTime())
                && isStartTimeBeforeEndTime(getStartTime(), getEndTime())
                && status.get() != null && !status.get().trim().isEmpty()
                && priority.get() != null && !priority.get().trim().isEmpty();
    }

    // =================== Helper Methods ===================
    /**
     * Validates if a time string is in HH:mm format.
     *
     * @param time Time string to validate
     * @return True if time format is valid, false otherwise
     */
    private boolean isValidTimeFormat(String time) {
        if (time == null || time.trim().isEmpty()) {
            return false;
        }

        try {
            LocalTime.parse(time, TIME_FORMATTER);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Validates if start time is before end time.
     *
     * @param startTime Start time string
     * @param endTime End time string
     * @return True if start time is before end time, false otherwise
     */
    private boolean isStartTimeBeforeEndTime(String startTime, String endTime) {
        try {
            LocalTime start = LocalTime.parse(startTime, TIME_FORMATTER);
            LocalTime end = LocalTime.parse(endTime, TIME_FORMATTER);
            return start.isBefore(end);
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Gets the date as a string property for TableView display.
     *
     * @return The date as a string property
     */
    public StringProperty dateStringProperty() {
        return new SimpleStringProperty(date.get() != null ? date.get().toString() : "");
    }

    /**
     * Gets a formatted string representation of the duty assignment.
     *
     * @return Formatted duty information
     */
    public String getFormattedInfo() {
        return String.format("%s: %s at %s (%s - %s)",
                getOfficerName(), getDutyType(), getLocation(),
                getStartTime(), getEndTime());
    }

    /**
     * Returns a string representation of the duty.
     *
     * @return String representation
     */
    @Override
    public String toString() {
        return String.format("GuardDuty[id=%s, officer=%s, type=%s, date=%s]",
                getDutyId(), getOfficerName(), getDutyType(), getDate());
    }
}
