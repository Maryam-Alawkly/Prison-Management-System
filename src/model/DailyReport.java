package model;

import javafx.beans.property.*;
import java.time.LocalDate;

/**
 * DailyReport class represents a daily security report in the prison management
 * system. It contains comprehensive information about daily activities,
 * incidents, and security measures. This class uses JavaFX properties for data
 * binding with UI components.
 */
public class DailyReport {

    // Core report properties
    private final StringProperty reportId;
    private final StringProperty officerId;
    private final StringProperty officerName;
    private final StringProperty reportType;
    private final StringProperty priority;
    private final ObjectProperty<LocalDate> reportDate;
    private final StringProperty status;

    // Report content properties
    private final StringProperty incidentsSummary;
    private final StringProperty actionsTaken;
    private final StringProperty patrolsCompleted;
    private final StringProperty cellInspections;
    private final StringProperty visitorScreenings;
    private final StringProperty activityDetails;
    private final StringProperty additionalNotes;

    // Administrative properties
    private final ObjectProperty<LocalDate> createdAt;
    private final StringProperty reviewedBy;
    private final ObjectProperty<LocalDate> reviewDate;
    private final StringProperty adminComments;

    // Additional security properties
    private final StringProperty securityLevel;
    private final StringProperty alertsHandled;
    private final StringProperty headcount;
    private final StringProperty transfers;
    private final StringProperty disciplinaryActions;
    private final StringProperty behaviorNotes;
    private final StringProperty equipmentChecks;
    private final StringProperty issuesEncountered;
    private final StringProperty recommendations;

    // Constants for report types
    public static final String TYPE_SECURITY = "Security";
    public static final String TYPE_INCIDENT = "Incident";
    public static final String TYPE_DAILY_OPERATIONS = "Daily Operations";
    public static final String TYPE_SPECIAL_EVENT = "Special Event";

    // Constants for priority levels
    public static final String PRIORITY_LOW = "Low";
    public static final String PRIORITY_MEDIUM = "Medium";
    public static final String PRIORITY_HIGH = "High";
    public static final String PRIORITY_CRITICAL = "Critical";

    // Constants for report status
    public static final String STATUS_DRAFT = "Draft";
    public static final String STATUS_SUBMITTED = "Submitted";
    public static final String STATUS_UNDER_REVIEW = "Under Review";
    public static final String STATUS_APPROVED = "Approved";
    public static final String STATUS_REJECTED = "Rejected";

    /**
     * Default constructor initializes all properties. Creates a new DailyReport
     * with all properties set to their default values.
     */
    public DailyReport() {
        this.reportId = new SimpleStringProperty("");
        this.officerId = new SimpleStringProperty("");
        this.officerName = new SimpleStringProperty("");
        this.reportType = new SimpleStringProperty("");
        this.priority = new SimpleStringProperty(PRIORITY_MEDIUM);
        this.reportDate = new SimpleObjectProperty<>(LocalDate.now());
        this.status = new SimpleStringProperty(STATUS_DRAFT);

        this.incidentsSummary = new SimpleStringProperty("");
        this.actionsTaken = new SimpleStringProperty("");
        this.patrolsCompleted = new SimpleStringProperty("");
        this.cellInspections = new SimpleStringProperty("");
        this.visitorScreenings = new SimpleStringProperty("");
        this.activityDetails = new SimpleStringProperty("");
        this.additionalNotes = new SimpleStringProperty("");

        this.createdAt = new SimpleObjectProperty<>(LocalDate.now());
        this.reviewedBy = new SimpleStringProperty("");
        this.reviewDate = new SimpleObjectProperty<>();
        this.adminComments = new SimpleStringProperty("");

        this.securityLevel = new SimpleStringProperty("");
        this.alertsHandled = new SimpleStringProperty("");
        this.headcount = new SimpleStringProperty("");
        this.transfers = new SimpleStringProperty("");
        this.disciplinaryActions = new SimpleStringProperty("");
        this.behaviorNotes = new SimpleStringProperty("");
        this.equipmentChecks = new SimpleStringProperty("");
        this.issuesEncountered = new SimpleStringProperty("");
        this.recommendations = new SimpleStringProperty("");
    }

    // =================== Core Report Getters and Setters ===================
    /**
     * Gets the unique identifier of the report.
     *
     * @return The report ID
     */
    public String getReportId() {
        return reportId.get();
    }

    /**
     * Sets the unique identifier of the report.
     *
     * @param reportId The report ID to set
     */
    public void setReportId(String reportId) {
        this.reportId.set(reportId);
    }

    /**
     * Gets the report ID property for JavaFX binding.
     *
     * @return The report ID property
     */
    public StringProperty reportIdProperty() {
        return reportId;
    }

    /**
     * Gets the ID of the officer who created the report.
     *
     * @return The officer ID
     */
    public String getOfficerId() {
        return officerId.get();
    }

    /**
     * Sets the ID of the officer who created the report.
     *
     * @param officerId The officer ID to set
     */
    public void setOfficerId(String officerId) {
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
     * Gets the name of the officer who created the report.
     *
     * @return The officer name
     */
    public String getOfficerName() {
        return officerName.get();
    }

    /**
     * Sets the name of the officer who created the report.
     *
     * @param officerName The officer name to set
     */
    public void setOfficerName(String officerName) {
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
     * Gets the type of the report.
     *
     * @return The report type
     */
    public String getReportType() {
        return reportType.get();
    }

    /**
     * Sets the type of the report.
     *
     * @param reportType The report type to set
     */
    public void setReportType(String reportType) {
        this.reportType.set(reportType);
    }

    /**
     * Gets the report type property for JavaFX binding.
     *
     * @return The report type property
     */
    public StringProperty reportTypeProperty() {
        return reportType;
    }

    /**
     * Gets the priority level of the report.
     *
     * @return The priority level
     */
    public String getPriority() {
        return priority.get();
    }

    /**
     * Sets the priority level of the report.
     *
     * @param priority The priority level to set
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
     * Gets the date when the report was filed.
     *
     * @return The report date
     */
    public LocalDate getReportDate() {
        return reportDate.get();
    }

    /**
     * Sets the date when the report was filed.
     *
     * @param reportDate The report date to set
     */
    public void setReportDate(LocalDate reportDate) {
        this.reportDate.set(reportDate);
    }

    /**
     * Gets the report date property for JavaFX binding.
     *
     * @return The report date property
     */
    public ObjectProperty<LocalDate> reportDateProperty() {
        return reportDate;
    }

    /**
     * Gets the status of the report.
     *
     * @return The report status
     */
    public String getStatus() {
        return status.get();
    }

    /**
     * Sets the status of the report.
     *
     * @param status The report status to set
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

    // =================== Report Content Getters and Setters ===================
    /**
     * Gets the summary of incidents reported.
     *
     * @return The incidents summary
     */
    public String getIncidentsSummary() {
        return incidentsSummary.get();
    }

    /**
     * Sets the summary of incidents reported.
     *
     * @param incidentsSummary The incidents summary to set
     */
    public void setIncidentsSummary(String incidentsSummary) {
        this.incidentsSummary.set(incidentsSummary);
    }

    /**
     * Gets the incidents summary property for JavaFX binding.
     *
     * @return The incidents summary property
     */
    public StringProperty incidentsSummaryProperty() {
        return incidentsSummary;
    }

    /**
     * Gets the actions taken in response to incidents.
     *
     * @return The actions taken
     */
    public String getActionsTaken() {
        return actionsTaken.get();
    }

    /**
     * Sets the actions taken in response to incidents.
     *
     * @param actionsTaken The actions taken to set
     */
    public void setActionsTaken(String actionsTaken) {
        this.actionsTaken.set(actionsTaken);
    }

    /**
     * Gets the actions taken property for JavaFX binding.
     *
     * @return The actions taken property
     */
    public StringProperty actionsTakenProperty() {
        return actionsTaken;
    }

    /**
     * Gets the number or description of patrols completed.
     *
     * @return The patrols completed information
     */
    public String getPatrolsCompleted() {
        return patrolsCompleted.get();
    }

    /**
     * Sets the number or description of patrols completed.
     *
     * @param patrolsCompleted The patrols completed information to set
     */
    public void setPatrolsCompleted(String patrolsCompleted) {
        this.patrolsCompleted.set(patrolsCompleted);
    }

    /**
     * Gets the patrols completed property for JavaFX binding.
     *
     * @return The patrols completed property
     */
    public StringProperty patrolsCompletedProperty() {
        return patrolsCompleted;
    }

    /**
     * Gets the details of cell inspections performed.
     *
     * @return The cell inspections details
     */
    public String getCellInspections() {
        return cellInspections.get();
    }

    /**
     * Sets the details of cell inspections performed.
     *
     * @param cellInspections The cell inspections details to set
     */
    public void setCellInspections(String cellInspections) {
        this.cellInspections.set(cellInspections);
    }

    /**
     * Gets the cell inspections property for JavaFX binding.
     *
     * @return The cell inspections property
     */
    public StringProperty cellInspectionsProperty() {
        return cellInspections;
    }

    /**
     * Gets the details of visitor screenings conducted.
     *
     * @return The visitor screenings details
     */
    public String getVisitorScreenings() {
        return visitorScreenings.get();
    }

    /**
     * Sets the details of visitor screenings conducted.
     *
     * @param visitorScreenings The visitor screenings details to set
     */
    public void setVisitorScreenings(String visitorScreenings) {
        this.visitorScreenings.set(visitorScreenings);
    }

    /**
     * Gets the visitor screenings property for JavaFX binding.
     *
     * @return The visitor screenings property
     */
    public StringProperty visitorScreeningsProperty() {
        return visitorScreenings;
    }

    /**
     * Gets the details of daily activities.
     *
     * @return The activity details
     */
    public String getActivityDetails() {
        return activityDetails.get();
    }

    /**
     * Sets the details of daily activities.
     *
     * @param activityDetails The activity details to set
     */
    public void setActivityDetails(String activityDetails) {
        this.activityDetails.set(activityDetails);
    }

    /**
     * Gets the activity details property for JavaFX binding.
     *
     * @return The activity details property
     */
    public StringProperty activityDetailsProperty() {
        return activityDetails;
    }

    /**
     * Gets any additional notes about the daily report.
     *
     * @return The additional notes
     */
    public String getAdditionalNotes() {
        return additionalNotes.get();
    }

    /**
     * Sets any additional notes about the daily report.
     *
     * @param additionalNotes The additional notes to set
     */
    public void setAdditionalNotes(String additionalNotes) {
        this.additionalNotes.set(additionalNotes);
    }

    /**
     * Gets the additional notes property for JavaFX binding.
     *
     * @return The additional notes property
     */
    public StringProperty additionalNotesProperty() {
        return additionalNotes;
    }

    // =================== Administrative Getters and Setters ===================
    /**
     * Gets the date when the report was created.
     *
     * @return The creation date
     */
    public LocalDate getCreatedAt() {
        return createdAt.get();
    }

    /**
     * Sets the date when the report was created.
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
     * Gets the name of the administrator who reviewed the report.
     *
     * @return The reviewer name
     */
    public String getReviewedBy() {
        return reviewedBy.get();
    }

    /**
     * Sets the name of the administrator who reviewed the report.
     *
     * @param reviewedBy The reviewer name to set
     */
    public void setReviewedBy(String reviewedBy) {
        this.reviewedBy.set(reviewedBy);
    }

    /**
     * Gets the reviewer property for JavaFX binding.
     *
     * @return The reviewer property
     */
    public StringProperty reviewedByProperty() {
        return reviewedBy;
    }

    /**
     * Gets the date when the report was reviewed.
     *
     * @return The review date
     */
    public LocalDate getReviewDate() {
        return reviewDate.get();
    }

    /**
     * Sets the date when the report was reviewed.
     *
     * @param reviewDate The review date to set
     */
    public void setReviewDate(LocalDate reviewDate) {
        this.reviewDate.set(reviewDate);
    }

    /**
     * Gets the review date property for JavaFX binding.
     *
     * @return The review date property
     */
    public ObjectProperty<LocalDate> reviewDateProperty() {
        return reviewDate;
    }

    /**
     * Gets the administrator's comments on the report.
     *
     * @return The administrator comments
     */
    public String getAdminComments() {
        return adminComments.get();
    }

    /**
     * Sets the administrator's comments on the report.
     *
     * @param adminComments The administrator comments to set
     */
    public void setAdminComments(String adminComments) {
        this.adminComments.set(adminComments);
    }

    /**
     * Gets the administrator comments property for JavaFX binding.
     *
     * @return The administrator comments property
     */
    public StringProperty adminCommentsProperty() {
        return adminComments;
    }

    // =================== Additional Security Properties ===================
    /**
     * Gets the security level for the reported period.
     *
     * @return The security level
     */
    public String getSecurityLevel() {
        return securityLevel.get();
    }

    /**
     * Sets the security level for the reported period.
     *
     * @param securityLevel The security level to set
     */
    public void setSecurityLevel(String securityLevel) {
        this.securityLevel.set(securityLevel);
    }

    /**
     * Gets the security level property for JavaFX binding.
     *
     * @return The security level property
     */
    public StringProperty securityLevelProperty() {
        return securityLevel;
    }

    /**
     * Gets the description of alerts handled.
     *
     * @return The alerts handled description
     */
    public String getAlertsHandled() {
        return alertsHandled.get();
    }

    /**
     * Sets the description of alerts handled.
     *
     * @param alertsHandled The alerts handled description to set
     */
    public void setAlertsHandled(String alertsHandled) {
        this.alertsHandled.set(alertsHandled);
    }

    /**
     * Gets the alerts handled property for JavaFX binding.
     *
     * @return The alerts handled property
     */
    public StringProperty alertsHandledProperty() {
        return alertsHandled;
    }

    /**
     * Gets the inmate headcount for the day.
     *
     * @return The headcount information
     */
    public String getHeadcount() {
        return headcount.get();
    }

    /**
     * Sets the inmate headcount for the day.
     *
     * @param headcount The headcount information to set
     */
    public void setHeadcount(String headcount) {
        this.headcount.set(headcount);
    }

    /**
     * Gets the headcount property for JavaFX binding.
     *
     * @return The headcount property
     */
    public StringProperty headcountProperty() {
        return headcount;
    }

    /**
     * Gets the details of inmate transfers.
     *
     * @return The transfers information
     */
    public String getTransfers() {
        return transfers.get();
    }

    /**
     * Sets the details of inmate transfers.
     *
     * @param transfers The transfers information to set
     */
    public void setTransfers(String transfers) {
        this.transfers.set(transfers);
    }

    /**
     * Gets the transfers property for JavaFX binding.
     *
     * @return The transfers property
     */
    public StringProperty transfersProperty() {
        return transfers;
    }

    /**
     * Gets the details of disciplinary actions taken.
     *
     * @return The disciplinary actions information
     */
    public String getDisciplinaryActions() {
        return disciplinaryActions.get();
    }

    /**
     * Sets the details of disciplinary actions taken.
     *
     * @param disciplinaryActions The disciplinary actions information to set
     */
    public void setDisciplinaryActions(String disciplinaryActions) {
        this.disciplinaryActions.set(disciplinaryActions);
    }

    /**
     * Gets the disciplinary actions property for JavaFX binding.
     *
     * @return The disciplinary actions property
     */
    public StringProperty disciplinaryActionsProperty() {
        return disciplinaryActions;
    }

    /**
     * Gets notes on inmate behavior.
     *
     * @return The behavior notes
     */
    public String getBehaviorNotes() {
        return behaviorNotes.get();
    }

    /**
     * Sets notes on inmate behavior.
     *
     * @param behaviorNotes The behavior notes to set
     */
    public void setBehaviorNotes(String behaviorNotes) {
        this.behaviorNotes.set(behaviorNotes);
    }

    /**
     * Gets the behavior notes property for JavaFX binding.
     *
     * @return The behavior notes property
     */
    public StringProperty behaviorNotesProperty() {
        return behaviorNotes;
    }

    /**
     * Gets the details of equipment checks performed.
     *
     * @return The equipment checks information
     */
    public String getEquipmentChecks() {
        return equipmentChecks.get();
    }

    /**
     * Sets the details of equipment checks performed.
     *
     * @param equipmentChecks The equipment checks information to set
     */
    public void setEquipmentChecks(String equipmentChecks) {
        this.equipmentChecks.set(equipmentChecks);
    }

    /**
     * Gets the equipment checks property for JavaFX binding.
     *
     * @return The equipment checks property
     */
    public StringProperty equipmentChecksProperty() {
        return equipmentChecks;
    }

    /**
     * Gets the issues encountered during the shift.
     *
     * @return The issues encountered
     */
    public String getIssuesEncountered() {
        return issuesEncountered.get();
    }

    /**
     * Sets the issues encountered during the shift.
     *
     * @param issuesEncountered The issues encountered to set
     */
    public void setIssuesEncountered(String issuesEncountered) {
        this.issuesEncountered.set(issuesEncountered);
    }

    /**
     * Gets the issues encountered property for JavaFX binding.
     *
     * @return The issues encountered property
     */
    public StringProperty issuesEncounteredProperty() {
        return issuesEncountered;
    }

    /**
     * Gets the recommendations for improvement.
     *
     * @return The recommendations
     */
    public String getRecommendations() {
        return recommendations.get();
    }

    /**
     * Sets the recommendations for improvement.
     *
     * @param recommendations The recommendations to set
     */
    public void setRecommendations(String recommendations) {
        this.recommendations.set(recommendations);
    }

    /**
     * Gets the recommendations property for JavaFX binding.
     *
     * @return The recommendations property
     */
    public StringProperty recommendationsProperty() {
        return recommendations;
    }

    // =================== Helper Methods for TableView ===================
    /**
     * Gets the report date as a string property for TableView display.
     *
     * @return The report date as a string property
     */
    public StringProperty reportDateStringProperty() {
        return new SimpleStringProperty(reportDate.get() != null ? reportDate.get().toString() : "");
    }

    /**
     * Gets the creation date as a string property for TableView display.
     *
     * @return The creation date as a string property
     */
    public StringProperty createdAtStringProperty() {
        return new SimpleStringProperty(createdAt.get() != null ? createdAt.get().toString() : "");
    }

    // =================== Utility Methods ===================
    /**
     * Gets the formatted report date as a string.
     *
     * @return The formatted report date, or "Not set" if null
     */
    public String getFormattedReportDate() {
        return reportDate.get() != null ? reportDate.get().toString() : "Not set";
    }

    /**
     * Gets the formatted creation date as a string.
     *
     * @return The formatted creation date, or "Not set" if null
     */
    public String getFormattedCreatedAt() {
        return createdAt.get() != null ? createdAt.get().toString() : "Not set";
    }

    /**
     * Gets the formatted review date as a string.
     *
     * @return The formatted review date, or "Not set" if null
     */
    public String getFormattedReviewDate() {
        return reviewDate.get() != null ? reviewDate.get().toString() : "Not set";
    }

    /**
     * Checks if the report is in draft status.
     *
     * @return True if the report is a draft, false otherwise
     */
    public boolean isDraft() {
        return STATUS_DRAFT.equals(getStatus());
    }

    /**
     * Checks if the report is submitted for review.
     *
     * @return True if the report is submitted, false otherwise
     */
    public boolean isSubmitted() {
        return STATUS_SUBMITTED.equals(getStatus());
    }

    /**
     * Checks if the report is approved.
     *
     * @return True if the report is approved, false otherwise
     */
    public boolean isApproved() {
        return STATUS_APPROVED.equals(getStatus());
    }

    /**
     * Validates if the report has all required fields filled.
     *
     * @return True if the report is valid, false otherwise
     */
    public boolean isValid() {
        return reportId.get() != null && !reportId.get().trim().isEmpty()
                && officerId.get() != null && !officerId.get().trim().isEmpty()
                && officerName.get() != null && !officerName.get().trim().isEmpty()
                && reportDate.get() != null
                && status.get() != null && !status.get().trim().isEmpty();
    }
}
