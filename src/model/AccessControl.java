package model;

/**
 * AccessControl class represents access permissions for system modules
 */
public class AccessControl {

    private String controlId;
    private String employeeId;
    private String employeeName;
    private String module; // e.g., "PRISONER_MANAGEMENT", "SECURITY", "REPORTS"
    private String permissionLevel; // "None", "View", "Edit", "Full"
    private String grantedBy;
    private String grantedDate;
    private String expiresOn; // null for permanent
    private boolean isActive;

    /**
     * Constructor for AccessControl
     */
    public AccessControl(String controlId, String employeeId, String employeeName,
            String module, String permissionLevel) {
        this.controlId = controlId;
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.module = module;
        this.permissionLevel = permissionLevel;
        this.grantedDate = java.time.LocalDate.now().toString();
        this.isActive = true;
    }

    // Getters and Setters
    public String getControlId() {
        return controlId;
    }

    public void setControlId(String controlId) {
        this.controlId = controlId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getPermissionLevel() {
        return permissionLevel;
    }

    public void setPermissionLevel(String permissionLevel) {
        this.permissionLevel = permissionLevel;
    }

    public String getGrantedBy() {
        return grantedBy;
    }

    public void setGrantedBy(String grantedBy) {
        this.grantedBy = grantedBy;
    }

    public String getGrantedDate() {
        return grantedDate;
    }

    public void setGrantedDate(String grantedDate) {
        this.grantedDate = grantedDate;
    }

    public String getExpiresOn() {
        return expiresOn;
    }

    public void setExpiresOn(String expiresOn) {
        this.expiresOn = expiresOn;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    /**
     * Check if permission has expired
     */
    public boolean isExpired() {
        if (expiresOn == null) {
            return false;
        }

        java.time.LocalDate expiryDate = java.time.LocalDate.parse(expiresOn);
        return java.time.LocalDate.now().isAfter(expiryDate);
    }

    /**
     * Revoke permission
     */
    public void revoke() {
        this.isActive = false;
    }

    /**
     * Check if has view permission
     */
    public boolean canView() {
        return isActive && !isExpired()
                && (permissionLevel.equals("View")
                || permissionLevel.equals("Edit")
                || permissionLevel.equals("Full"));
    }

    /**
     * Check if has edit permission
     */
    public boolean canEdit() {
        return isActive && !isExpired()
                && (permissionLevel.equals("Edit")
                || permissionLevel.equals("Full"));
    }

    /**
     * Check if has full permission
     */
    public boolean hasFullAccess() {
        return isActive && !isExpired() && permissionLevel.equals("Full");
    }

    /**
     * Get permission status
     */
    public String getStatus() {
        if (!isActive) {
            return "Revoked";
        }
        if (isExpired()) {
            return "Expired";
        }
        return "Active";
    }
}
