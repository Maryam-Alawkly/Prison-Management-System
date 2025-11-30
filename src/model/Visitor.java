package model;

import java.time.LocalDate;

/**
 * Visitor class represents a person visiting a prisoner Inherits from Person
 * class and adds visitor-specific properties
 */
public class Visitor extends Person {

    private String relationship;
    private String prisonerId;
    private int visitCount;
    private String lastVisitDate; // Changed from LocalDate to String for database compatibility
    private String status; // "Approved", "Pending", "Banned"

    /**
     * Constructor for Visitor class
     *
     * @param id Visitor ID
     * @param name Visitor name
     * @param phone Contact phone
     * @param relationship Relationship to prisoner
     * @param prisonerId ID of the prisoner being visited
     */
    public Visitor(String id, String name, String phone,
            String relationship, String prisonerId) {
        super(id, name, phone);
        this.relationship = relationship;
        this.prisonerId = prisonerId;
        this.visitCount = 0;
        this.lastVisitDate = null;
        this.status = "Pending";
    }

    // Getter methods
    public String getRelationship() {
        return relationship;
    }

    public String getPrisonerId() {
        return prisonerId;
    }

    public int getVisitCount() {
        return visitCount;
    }

    public String getLastVisitDate() {
        return lastVisitDate;
    }

    public String getStatus() {
        return status;
    }

    // Setter methods - ADDED FOR DATABASE COMPATIBILITY
    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public void setPrisonerId(String prisonerId) {
        this.prisonerId = prisonerId;
    }

    public void setVisitCount(int visitCount) {
        this.visitCount = visitCount;
    }

    public void setLastVisitDate(String lastVisitDate) {
        this.lastVisitDate = lastVisitDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Approve visitor for visits
     */
    public void approveVisitor() {
        this.status = "Approved";
    }

    /**
     * Ban visitor from future visits
     */
    public void banVisitor() {
        this.status = "Banned";
    }

    /**
     * Record a new visit
     */
    public void recordVisit() {
        this.visitCount++;
        this.lastVisitDate = LocalDate.now().toString(); // Convert to String for database
    }

    /**
     * Check if visitor is approved
     *
     * @return true if visitor status is "Approved"
     */
    public boolean isApproved() {
        return "Approved".equals(this.status);
    }

    /**
     * Check if visitor can visit today
     *
     * @return true if visitor can visit (approved and not banned)
     */
    public boolean canVisit() {
        return "Approved".equals(this.status) && !"Banned".equals(this.status);
    }

    /**
     * Override toString method to display visitor information
     */
    @Override
    public String toString() {
        return "Visitor: " + getName()
                + " | Relationship: " + relationship
                + " | Prisoner ID: " + prisonerId
                + " | Status: " + status;
    }

    /**
     * Get detailed visitor information
     *
     * @return Detailed string with all visitor data
     */
    public String getVisitorDetails() {
        return "Visitor ID: " + getId()
                + "\nName: " + getName()
                + "\nPhone: " + getPhone()
                + "\nRelationship: " + relationship
                + "\nPrisoner ID: " + prisonerId
                + "\nTotal Visits: " + visitCount
                + "\nLast Visit: " + (lastVisitDate != null ? lastVisitDate : "Never")
                + "\nStatus: " + status
                + "\nCan Visit: " + (canVisit() ? "Yes" : "No");
    }

    /**
     * Get visit statistics
     *
     * @return String with visit statistics
     */
    public String getVisitStatistics() {
        return "Visitor: " + getName()
                + "\nTotal Visits: " + visitCount
                + "\nLast Visit: " + (lastVisitDate != null ? lastVisitDate : "Never")
                + "\nStatus: " + status;
    }
}
