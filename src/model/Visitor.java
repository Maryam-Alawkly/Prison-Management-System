package model;

import java.time.LocalDate;

/**
 * Visitor class represents a person who visits prisoners in the facility. This
 * class extends the Person class and adds visitor-specific properties including
 * relationship to prisoner, visit history, and approval status.
 */
public class Visitor extends Person {

    // Visitor status constants
    public static final String STATUS_PENDING = "Pending";
    public static final String STATUS_APPROVED = "Approved";
    public static final String STATUS_BANNED = "Banned";
    public static final String STATUS_SUSPENDED = "Suspended";

    // Relationship constants
    public static final String RELATIONSHIP_FAMILY = "Family";
    public static final String RELATIONSHIP_FRIEND = "Friend";
    public static final String RELATIONSHIP_LAWYER = "Lawyer";
    public static final String RELATIONSHIP_OFFICIAL = "Official";
    public static final String RELATIONSHIP_OTHER = "Other";

    // Visitor properties
    private String relationship;
    private String prisonerId;
    private int visitCount;
    private String lastVisitDate; // Stored as String for database compatibility
    private String status;

    /**
     * Constructor for Visitor class. Initializes a visitor with basic
     * information and default values.
     *
     * @param id Unique visitor identifier
     * @param name Full name of the visitor
     * @param phone Contact phone number
     * @param relationship Relationship to the prisoner being visited
     * @param prisonerId ID of the prisoner being visited
     * @throws IllegalArgumentException if any required parameter is invalid
     */
    public Visitor(String id, String name, String phone,
            String relationship, String prisonerId) {
        super(id, name, phone);
        validateParameters(relationship, prisonerId);

        this.relationship = relationship;
        this.prisonerId = prisonerId;
        this.visitCount = 0;
        this.lastVisitDate = null;
        this.status = STATUS_PENDING;
    }

    /**
     * Validates constructor parameters.
     *
     * @param relationship Relationship to validate
     * @param prisonerId Prisoner ID to validate
     */
    private void validateParameters(String relationship, String prisonerId) {
        if (relationship == null || relationship.trim().isEmpty()) {
            throw new IllegalArgumentException("Relationship cannot be null or empty");
        }
        if (prisonerId == null || prisonerId.trim().isEmpty()) {
            throw new IllegalArgumentException("Prisoner ID cannot be null or empty");
        }
    }

    // =================== Getter Methods ===================
    /**
     * Gets the relationship of the visitor to the prisoner.
     *
     * @return The relationship description
     */
    public String getRelationship() {
        return relationship;
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
     * Gets the total number of visits made by this visitor.
     *
     * @return The visit count
     */
    public int getVisitCount() {
        return visitCount;
    }

    /**
     * Gets the date of the last visit made by this visitor. Returns null if no
     * visits have been made.
     *
     * @return Last visit date as String, or null if no visits
     */
    public String getLastVisitDate() {
        return lastVisitDate;
    }

    /**
     * Gets the current status of the visitor.
     *
     * @return The visitor status
     */
    public String getStatus() {
        return status;
    }

    // =================== Setter Methods ===================
    /**
     * Sets the relationship of the visitor to the prisoner.
     *
     * @param relationship The relationship to set
     */
    public void setRelationship(String relationship) {
        this.relationship = relationship;
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
     * Sets the total number of visits made by this visitor.
     *
     * @param visitCount The visit count to set
     * @throws IllegalArgumentException if visit count is negative
     */
    public void setVisitCount(int visitCount) {
        if (visitCount < 0) {
            throw new IllegalArgumentException("Visit count cannot be negative");
        }
        this.visitCount = visitCount;
    }

    /**
     * Sets the date of the last visit made by this visitor.
     *
     * @param lastVisitDate The last visit date to set (as String)
     */
    public void setLastVisitDate(String lastVisitDate) {
        this.lastVisitDate = lastVisitDate;
    }

    /**
     * Sets the current status of the visitor.
     *
     * @param status The visitor status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    // =================== Business Logic Methods ===================
    /**
     * Approves the visitor for future visits. Sets status to "Approved".
     */
    public void approveVisitor() {
        this.status = STATUS_APPROVED;
    }

    /**
     * Bans the visitor from future visits. Sets status to "Banned".
     */
    public void banVisitor() {
        this.status = STATUS_BANNED;
    }

    /**
     * Suspends the visitor temporarily. Sets status to "Suspended".
     */
    public void suspendVisitor() {
        this.status = STATUS_SUSPENDED;
    }

    /**
     * Records a new visit by this visitor. Increments visit count and updates
     * last visit date to current date.
     */
    public void recordVisit() {
        this.visitCount++;
        this.lastVisitDate = LocalDate.now().toString(); // Store as String for database compatibility
    }

    /**
     * Checks if the visitor is approved for visits.
     *
     * @return True if visitor status is "Approved", false otherwise
     */
    public boolean isApproved() {
        return STATUS_APPROVED.equals(this.status);
    }

    /**
     * Checks if the visitor can visit today. A visitor can visit if they are
     * approved and not banned or suspended.
     *
     * @return True if visitor can visit, false otherwise
     */
    public boolean canVisit() {
        return STATUS_APPROVED.equals(this.status);
    }

    /**
     * Checks if the visitor is currently banned.
     *
     * @return True if visitor status is "Banned", false otherwise
     */
    public boolean isBanned() {
        return STATUS_BANNED.equals(this.status);
    }

    /**
     * Checks if the visitor is currently suspended.
     *
     * @return True if visitor status is "Suspended", false otherwise
     */
    public boolean isSuspended() {
        return STATUS_SUSPENDED.equals(this.status);
    }

    /**
     * Checks if the visitor has ever visited before.
     *
     * @return True if visitor has made at least one visit, false otherwise
     */
    public boolean hasVisitedBefore() {
        return visitCount > 0;
    }

    /**
     * Gets the last visit date as LocalDate object. Returns null if last visit
     * date is not set or cannot be parsed.
     *
     * @return Last visit date as LocalDate, or null if unavailable
     */
    public LocalDate getLastVisitDateAsLocalDate() {
        if (lastVisitDate == null || lastVisitDate.trim().isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(lastVisitDate);
        } catch (Exception e) {
            // Log warning in production: logger.warn("Invalid date format for visitor {}: {}", getId(), lastVisitDate);
            return null;
        }
    }

    /**
     * Calculates days since last visit. Returns null if visitor has never
     * visited.
     *
     * @return Days since last visit, or null if no previous visits
     */
    public Long getDaysSinceLastVisit() {
        LocalDate lastVisit = getLastVisitDateAsLocalDate();
        if (lastVisit == null) {
            return null;
        }
        return java.time.temporal.ChronoUnit.DAYS.between(lastVisit, LocalDate.now());
    }

    /**
     * Checks if the visitor configuration is valid. Validates all required
     * fields and logical constraints.
     *
     * @return True if visitor configuration is valid, false otherwise
     */
    public boolean isValid() {
        return getId() != null && !getId().trim().isEmpty()
                && getName() != null && !getName().trim().isEmpty()
                && relationship != null && !relationship.trim().isEmpty()
                && prisonerId != null && !prisonerId.trim().isEmpty()
                && visitCount >= 0
                && status != null && !status.trim().isEmpty();
    }

    // =================== Information Methods ===================
    /**
     * Returns a string representation of the visitor. Provides a concise
     * summary for display purposes.
     *
     * @return String representation of the visitor
     */
    @Override
    public String toString() {
        return String.format("Visitor: %s | Relationship: %s | Prisoner ID: %s | Status: %s",
                getName(), relationship, prisonerId, status);
    }

    /**
     * Gets detailed visitor information. Includes all visitor properties for
     * display or reporting.
     *
     * @return Detailed string with all visitor data
     */
    public String getVisitorDetails() {
        StringBuilder details = new StringBuilder();
        details.append("Visitor ID: ").append(getId()).append("\n");
        details.append("Name: ").append(getName()).append("\n");
        details.append("Phone: ").append(getPhone()).append("\n");
        details.append("Relationship: ").append(relationship).append("\n");
        details.append("Prisoner ID: ").append(prisonerId).append("\n");
        details.append("Total Visits: ").append(visitCount).append("\n");
        details.append("Last Visit: ").append(lastVisitDate != null ? lastVisitDate : "Never").append("\n");
        details.append("Status: ").append(status).append("\n");
        details.append("Can Visit: ").append(canVisit() ? "Yes" : "No").append("\n");

        Long daysSinceLastVisit = getDaysSinceLastVisit();
        if (daysSinceLastVisit != null) {
            details.append("Days Since Last Visit: ").append(daysSinceLastVisit);
        }

        return details.toString();
    }

    /**
     * Gets visit statistics for the visitor. Focuses on visit history and
     * frequency.
     *
     * @return String with visit statistics
     */
    public String getVisitStatistics() {
        return String.format("Visitor: %s%nTotal Visits: %d%nLast Visit: %s%nStatus: %s",
                getName(), visitCount,
                lastVisitDate != null ? lastVisitDate : "Never",
                status);
    }
}
