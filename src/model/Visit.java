package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Visit class represents a scheduled visit between a prisoner and visitor
 * Manages visit appointments, status, and duration
 */
public class Visit {
    private String visitId;
    private String prisonerId;
    private String visitorId;
    private LocalDateTime visitDateTime;
    private int duration; // in minutes
    private String status; // "Scheduled", "In Progress", "Completed", "Cancelled"
    private String notes;
    private LocalDateTime actualStartTime;
    private LocalDateTime actualEndTime;
    
    // Date formatter for displaying dates
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    
    /**
     * Constructor for Visit class
     * @param visitId Unique visit ID
     * @param prisonerId ID of the prisoner being visited
     * @param visitorId ID of the visitor
     * @param visitDateTime Scheduled date and time of visit
     * @param duration Visit duration in minutes
     */
    public Visit(String visitId, String prisonerId, String visitorId, 
                 LocalDateTime visitDateTime, int duration) {
        this.visitId = visitId;
        this.prisonerId = prisonerId;
        this.visitorId = visitorId;
        this.visitDateTime = visitDateTime;
        this.duration = duration;
        this.status = "Scheduled";
        this.notes = "";
        this.actualStartTime = null;
        this.actualEndTime = null;
    }
    
    // Getter methods
    public String getVisitId() {
        return visitId;
    }
    
    public String getPrisonerId() {
        return prisonerId;
    }
    
    public String getVisitorId() {
        return visitorId;
    }
    
    public LocalDateTime getVisitDateTime() {
        return visitDateTime;
    }
    
    public int getDuration() {
        return duration;
    }
    
    public String getStatus() {
        return status;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public LocalDateTime getActualStartTime() {
        return actualStartTime;
    }
    
    public LocalDateTime getActualEndTime() {
        return actualEndTime;
    }
    
    // Setter methods - ADDED FOR DATABASE COMPATIBILITY
    public void setVisitId(String visitId) {
        this.visitId = visitId;
    }
    
    public void setPrisonerId(String prisonerId) {
        this.prisonerId = prisonerId;
    }
    
    public void setVisitorId(String visitorId) {
        this.visitorId = visitorId;
    }
    
    public void setVisitDateTime(LocalDateTime visitDateTime) {
        this.visitDateTime = visitDateTime;
    }
    
    public void setDuration(int duration) {
        this.duration = duration;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public void setActualStartTime(LocalDateTime actualStartTime) {
        this.actualStartTime = actualStartTime;
    }
    
    public void setActualEndTime(LocalDateTime actualEndTime) {
        this.actualEndTime = actualEndTime;
    }
    
    /**
     * Start the visit - mark as in progress
     */
    public void startVisit() {
        this.status = "In Progress";
        this.actualStartTime = LocalDateTime.now();
    }
    
    /**
     * Complete the visit - mark as completed
     */
    public void completeVisit() {
        this.status = "Completed";
        this.actualEndTime = LocalDateTime.now();
    }
    
    /**
     * Cancel the visit
     * @param cancellationNotes Reason for cancellation
     */
    public void cancelVisit(String cancellationNotes) {
        this.status = "Cancelled";
        this.notes = cancellationNotes;
    }/**
     * Reschedule the visit to a new date and time
     * @param newDateTime New date and time for the visit
     */
    public void rescheduleVisit(LocalDateTime newDateTime) {
        this.visitDateTime = newDateTime;
        this.status = "Scheduled";
        this.notes += "\nRescheduled to: " + newDateTime.format(formatter);
    }
    
    /**
     * Check if visit is scheduled for today
     * @return true if visit is scheduled for today
     */
    public boolean isScheduledForToday() {
        return visitDateTime.toLocalDate().equals(LocalDateTime.now().toLocalDate());
    }
    
    /**
     * Check if visit is overdue (scheduled time passed but not completed)
     * @return true if visit is overdue
     */
    public boolean isOverdue() {
        return visitDateTime.isBefore(LocalDateTime.now()) && "Scheduled".equals(status);
    }
    
    /**
     * Calculate actual visit duration in minutes
     * @return Actual duration in minutes, or 0 if not completed
     */
    public int calculateActualDuration() {
        if (actualStartTime != null && actualEndTime != null) {
            return (int) java.time.Duration.between(actualStartTime, actualEndTime).toMinutes();
        }
        return 0;
    }
    
    /**
     * Get formatted visit date and time
     * @return Formatted date string
     */
    public String getFormattedDateTime() {
        return visitDateTime.format(formatter);
    }
    
    /**
     * Override toString method to display visit information
     */
    @Override
    public String toString() {
        return "Visit ID: " + visitId + 
               " | Prisoner: " + prisonerId + 
               " | Visitor: " + visitorId + 
               " | Time: " + getFormattedDateTime() + 
               " | Status: " + status;
    }
    
    /**
     * Get detailed visit information
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
            details.append("\nActual Start: ").append(actualStartTime.format(formatter));
        }
        if (actualEndTime != null) {
            details.append("\nActual End: ").append(actualEndTime.format(formatter))
                   .append("\nActual Duration: ").append(calculateActualDuration()).append(" minutes");
        }
        
        return details.toString();
    }
    
    /**
     * Get visit summary for reports
     * @return Summary string
     */
    public String getVisitSummary() {
        return String.format("Visit %s: %s visiting %s at %s (%s)", 
                            visitId, visitorId, prisonerId, getFormattedDateTime(), status);
    }
}