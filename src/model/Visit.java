package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.Duration;
import java.util.Objects;

/**
 * Visit class represents a scheduled visit between a prisoner and visitor. This
 * class manages visit appointments, status tracking, duration, and provides
 * methods for visit lifecycle management including scheduling, starting,
 * completing, canceling, and rescheduling visits.
 */
public class Visit {

    // Visit status constants
    public static final String STATUS_SCHEDULED = "Scheduled";
    public static final String STATUS_IN_PROGRESS = "In Progress";
    public static final String STATUS_COMPLETED = "Completed";
    public static final String STATUS_CANCELLED = "Cancelled";

    // Date/time formatter for consistent display formatting
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    // Visit properties
    private String visitId;
    private String prisonerId;
    private String visitorId;
    private LocalDateTime visitDateTime;
    private int duration; // Duration in minutes
    private String status;
    private String notes;
    private LocalDateTime actualStartTime;
    private LocalDateTime actualEndTime;

    /**
     * Constructor for Visit class. Initializes a visit with scheduled details
     * and default status.
     *
     * @param visitId Unique visit identifier
     * @param prisonerId ID of the prisoner being visited
     * @param visitorId ID of the visitor
     * @param visitDateTime Scheduled date and time of the visit
     * @param duration Visit duration in minutes
     * @throws IllegalArgumentException if any required parameter is invalid
     */
    public Visit(String visitId, String prisonerId, String visitorId,
            LocalDateTime visitDateTime, int duration) {
        validateParameters(visitId, prisonerId, visitorId, visitDateTime, duration);

        this.visitId = visitId;
        this.prisonerId = prisonerId;
        this.visitorId = visitorId;
        this.visitDateTime = visitDateTime;
        this.duration = duration;
        this.status = STATUS_SCHEDULED;
        this.notes = "";
        this.actualStartTime = null;
        this.actualEndTime = null;
    }

    /**
     * Validates constructor parameters.
     *
     * @param visitId Visit ID to validate
     * @param prisonerId Prisoner ID to validate
     * @param visitorId Visitor ID to validate
     * @param visitDateTime Visit date/time to validate
     * @param duration Duration to validate
     */
    private void validateParameters(String visitId, String prisonerId, String visitorId,
            LocalDateTime visitDateTime, int duration) {
        if (visitId == null || visitId.trim().isEmpty()) {
            throw new IllegalArgumentException("Visit ID cannot be null or empty");
        }
        if (prisonerId == null || prisonerId.trim().isEmpty()) {
            throw new IllegalArgumentException("Prisoner ID cannot be null or empty");
        }
        if (visitorId == null || visitorId.trim().isEmpty()) {
            throw new IllegalArgumentException("Visitor ID cannot be null or empty");
        }
        if (visitDateTime == null) {
            throw new IllegalArgumentException("Visit date and time cannot be null");
        }
        if (duration <= 0) {
            throw new IllegalArgumentException("Duration must be greater than zero");
        }
    }

    // =================== Getter Methods ===================
    /**
     * Gets the unique visit identifier.
     *
     * @return The visit ID
     */
    public String getVisitId() {
        return visitId;
    }

    /**
     * Gets the ID of the prisoner being visited.
     *
     * @return The prisoner ID
     */
    public String getPrisonerId() {
        return prisonerId;
    }

    /**
     * Gets the ID of the visitor.
     *
     * @return The visitor ID
     */
    public String getVisitorId() {
        return visitorId;
    }

    /**
     * Gets the scheduled date and time of the visit.
     *
     * @return The scheduled visit date/time
     */
    public LocalDateTime getVisitDateTime() {
        return visitDateTime;
    }

    /**
     * Gets the scheduled duration of the visit in minutes.
     *
     * @return The visit duration in minutes
     */
    public int getDuration() {
        return duration;
    }

    /**
     * Gets the current status of the visit.
     *
     * @return The visit status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Gets any notes associated with the visit.
     *
     * @return Visit notes, or empty string if none
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Gets the actual start time of the visit.
     *
     * @return The actual start time, or null if not started
     */
    public LocalDateTime getActualStartTime() {
        return actualStartTime;
    }

    /**
     * Gets the actual end time of the visit.
     *
     * @return The actual end time, or null if not completed
     */
    public LocalDateTime getActualEndTime() {
        return actualEndTime;
    }

    // =================== Setter Methods ===================
    /**
     * Sets the unique visit identifier.
     *
     * @param visitId The visit ID to set
     */
    public void setVisitId(String visitId) {
        this.visitId = visitId;
    }

    /**
     * Sets the ID of the prisoner being visited.
     *
     * @param prisonerId The prisoner ID to set
     */
    public void setPrisonerId(String prisonerId) {
        this.prisonerId = prisonerId;
    }

    /**
     * Sets the ID of the visitor.
     *
     * @param visitorId The visitor ID to set
     */
    public void setVisitorId(String visitorId) {
        this.visitorId = visitorId;
    }

    /**
     * Sets the scheduled date and time of the visit.
     *
     * @param visitDateTime The visit date/time to set
     */
    public void setVisitDateTime(LocalDateTime visitDateTime) {
        this.visitDateTime = visitDateTime;
    }

    /**
     * Sets the scheduled duration of the visit in minutes.
     *
     * @param duration The duration in minutes to set
     * @throws IllegalArgumentException if duration is not positive
     */
    public void setDuration(int duration) {
        if (duration <= 0) {
            throw new IllegalArgumentException("Duration must be greater than zero");
        }
        this.duration = duration;
    }

    /**
     * Sets the current status of the visit.
     *
     * @param status The visit status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Sets any notes associated with the visit.
     *
     * @param notes The visit notes to set
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * Sets the actual start time of the visit.
     *
     * @param actualStartTime The actual start time to set
     */
    public void setActualStartTime(LocalDateTime actualStartTime) {
        this.actualStartTime = actualStartTime;
    }

    /**
     * Sets the actual end time of the visit.
     *
     * @param actualEndTime The actual end time to set
     */
    public void setActualEndTime(LocalDateTime actualEndTime) {
        this.actualEndTime = actualEndTime;
    }

    // =================== Business Logic Methods ===================
    /**
     * Starts the visit by marking it as in progress. Sets the actual start time
     * to current time.
     */
    public void startVisit() {
        this.status = STATUS_IN_PROGRESS;
        this.actualStartTime = LocalDateTime.now();
    }

    /**
     * Completes the visit by marking it as completed. Sets the actual end time
     * to current time.
     */
    public void completeVisit() {
        this.status = STATUS_COMPLETED;
        this.actualEndTime = LocalDateTime.now();
    }

    /**
     * Cancels the visit with cancellation notes. Updates status to cancelled
     * and records cancellation reason.
     *
     * @param cancellationNotes Reason for cancellation
     */
    public void cancelVisit(String cancellationNotes) {
        this.status = STATUS_CANCELLED;
        this.notes = cancellationNotes;
    }

    /**
     * Reschedules the visit to a new date and time. Updates scheduled time and
     * adds reschedule note.
     *
     * @param newDateTime New date and time for the visit
     */
    public void rescheduleVisit(LocalDateTime newDateTime) {
        this.visitDateTime = newDateTime;
        this.status = STATUS_SCHEDULED;
        this.notes += "\nRescheduled to: " + newDateTime.format(FORMATTER);
    }

    /**
     * Checks if the visit is scheduled for today.
     *
     * @return True if visit is scheduled for today, false otherwise
     */
    public boolean isScheduledForToday() {
        return visitDateTime.toLocalDate().equals(LocalDateTime.now().toLocalDate());
    }

    /**
     * Checks if the visit is overdue. A visit is overdue if scheduled time has
     * passed but status is still scheduled.
     *
     * @return True if visit is overdue, false otherwise
     */
    public boolean isOverdue() {
        return visitDateTime.isBefore(LocalDateTime.now()) && STATUS_SCHEDULED.equals(status);
    }

    /**
     * Calculates the actual visit duration in minutes.
     *
     * @return Actual duration in minutes, or 0 if visit not completed
     */
    public int calculateActualDuration() {
        if (actualStartTime != null && actualEndTime != null) {
            return (int) Duration.between(actualStartTime, actualEndTime).toMinutes();
        }
        return 0;
    }

    /**
     * Gets the formatted visit date and time.
     *
     * @return Formatted date/time string
     */
    public String getFormattedDateTime() {
        return visitDateTime.format(FORMATTER);
    }

    /**
     * Checks if the visit is currently in progress.
     *
     * @return True if visit status is "In Progress", false otherwise
     */
    public boolean isInProgress() {
        return STATUS_IN_PROGRESS.equals(status);
    }

    /**
     * Checks if the visit has been completed.
     *
     * @return True if visit status is "Completed", false otherwise
     */
    public boolean isCompleted() {
        return STATUS_COMPLETED.equals(status);
    }

    /**
     * Checks if the visit has been cancelled.
     *
     * @return True if visit status is "Cancelled", false otherwise
     */
    public boolean isCancelled() {
        return STATUS_CANCELLED.equals(status);
    }

    /**
     * Calculates the remaining time until scheduled visit.
     *
     * @return Minutes until scheduled visit, negative if overdue
     */
    public long getMinutesUntilVisit() {
        return Duration.between(LocalDateTime.now(), visitDateTime).toMinutes();
    }

    /**
     * Checks if the visit configuration is valid. Validates all required fields
     * and logical constraints.
     *
     * @return True if visit configuration is valid, false otherwise
     */
    public boolean isValid() {
        return visitId != null && !visitId.trim().isEmpty()
                && prisonerId != null && !prisonerId.trim().isEmpty()
                && visitorId != null && !visitorId.trim().isEmpty()
                && visitDateTime != null
                && duration > 0
                && status != null && !status.trim().isEmpty();
    }

    // =================== Information Methods ===================
    /**
     * Returns a string representation of the visit. Provides a concise summary
     * for display purposes.
     *
     * @return String representation of the visit
     */
    @Override
    public String toString() {
        return String.format("Visit ID: %s | Prisoner: %s | Visitor: %s | Time: %s | Status: %s",
                visitId, prisonerId, visitorId, getFormattedDateTime(), status);
    }

    /**
     * Gets detailed visit information. Includes all visit properties for
     * display or reporting.
     *
     * @return Detailed string with all visit data
     */
    public String getVisitDetails() {
        StringBuilder details = new StringBuilder();
        details.append("Visit ID: ").append(visitId)
                .append("\nPrisoner ID: ").append(prisonerId)
                .append("\nVisitor ID: ").append(visitorId)
                .append("\nScheduled Time: ").append(getFormattedDateTime())
                .append("\nDuration: ").append(duration).append(" minutes")
                .append("\nStatus: ").append(status)
                .append("\nNotes: ").append(notes.isEmpty() ? "None" : notes);

        if (actualStartTime != null) {
            details.append("\nActual Start: ").append(actualStartTime.format(FORMATTER));
        }
        if (actualEndTime != null) {
            details.append("\nActual End: ").append(actualEndTime.format(FORMATTER))
                    .append("\nActual Duration: ").append(calculateActualDuration()).append(" minutes");
        }

        return details.toString();
    }

    /**
     * Gets a summary of the visit for reports. Provides a compact format
     * suitable for listings and reports.
     *
     * @return Summary string
     */
    public String getVisitSummary() {
        return String.format("Visit %s: %s visiting %s at %s (%s)",
                visitId, visitorId, prisonerId, getFormattedDateTime(), status);
    }

    // =================== Overridden Methods ===================
    /**
     * Compares this visit with another object for equality. Two visits are
     * considered equal if they have the same visit ID.
     *
     * @param o Object to compare with
     * @return True if objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Visit visit = (Visit) o;
        return Objects.equals(visitId, visit.visitId);
    }

    /**
     * Returns a hash code value for the visit.
     *
     * @return Hash code based on visit ID
     */
    @Override
    public int hashCode() {
        return Objects.hash(visitId);
    }
}
