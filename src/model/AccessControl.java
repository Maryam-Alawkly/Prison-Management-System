package model;

import java.time.LocalDate;
import java.util.Objects;

/**
 * AccessControl class represents access permissions for system modules.
 * It manages permissions for employees to access different system modules
 * with varying permission levels.
 */
public class AccessControl {

    // Permission level constants
    public static final String PERMISSION_NONE = "None";
    public static final String PERMISSION_VIEW = "View";
    public static final String PERMISSION_EDIT = "Edit";
    public static final String PERMISSION_FULL = "Full";

    // Status constants
    public static final String STATUS_ACTIVE = "Active";
    public static final String STATUS_REVOKED = "Revoked";
    public static final String STATUS_EXPIRED = "Expired";

    private String controlId;
    private String employeeId;
    private String employeeName;
    private String module;
    private String permissionLevel;
    private String grantedBy;
    private String grantedDate;
    private String expiresOn;
    private boolean isActive;

    /**
     * Constructs a new AccessControl with required parameters.
     * Sets granted date to current date and active status to true.
     *
     * @param controlId Unique identifier for this access control record
     * @param employeeId ID of the employee
     * @param employeeName Name of the employee
     * @param module System module name (e.g., "PRISONER_MANAGEMENT", "SECURITY")
     * @param permissionLevel Permission level ("None", "View", "Edit", "Full")
     * @throws IllegalArgumentException if any required parameter is null or empty
     */
    public AccessControl(String controlId, String employeeId, String employeeName,
                        String module, String permissionLevel) {
        validateParameters(controlId, employeeId, employeeName, module, permissionLevel);
        
        this.controlId = controlId;
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.module = module;
        this.permissionLevel = permissionLevel;
        this.grantedDate = LocalDate.now().toString();
        this.isActive = true;
    }

    /**
     * Validates constructor parameters.
     *
     * @param controlId Control ID to validate
     * @param employeeId Employee ID to validate
     * @param employeeName Employee name to validate
     * @param module Module name to validate
     * @param permissionLevel Permission level to validate
     */
    private void validateParameters(String controlId, String employeeId, String employeeName,
                                   String module, String permissionLevel) {
        if (controlId == null || controlId.trim().isEmpty()) {
            throw new IllegalArgumentException("Control ID cannot be null or empty");
        }
        if (employeeId == null || employeeId.trim().isEmpty()) {
            throw new IllegalArgumentException("Employee ID cannot be null or empty");
        }
        if (employeeName == null || employeeName.trim().isEmpty()) {
            throw new IllegalArgumentException("Employee name cannot be null or empty");
        }
        if (module == null || module.trim().isEmpty()) {
            throw new IllegalArgumentException("Module cannot be null or empty");
        }
        if (permissionLevel == null || permissionLevel.trim().isEmpty()) {
            throw new IllegalArgumentException("Permission level cannot be null or empty");
        }
    }

    /**
     * Default constructor for frameworks that require it.
     * Note: This creates an invalid object and should only be used by serialization frameworks.
     */
    public AccessControl() {
        // This constructor is intentionally left empty for frameworks like JPA, Jackson, etc.
        // In production, consider using @JsonCreator or similar annotations instead.
    }

    // Getters and Setters with documentation

    /**
     * @return The control ID
     */
    public String getControlId() {
        return controlId;
    }

    /**
     * @param controlId The control ID to set
     */
    public void setControlId(String controlId) {
        if (controlId == null || controlId.trim().isEmpty()) {
            throw new IllegalArgumentException("Control ID cannot be null or empty");
        }
        this.controlId = controlId;
    }

    /**
     * @return The employee ID
     */
    public String getEmployeeId() {
        return employeeId;
    }

    /**
     * @param employeeId The employee ID to set
     */
    public void setEmployeeId(String employeeId) {
        if (employeeId == null || employeeId.trim().isEmpty()) {
            throw new IllegalArgumentException("Employee ID cannot be null or empty");
        }
        this.employeeId = employeeId;
    }

    /**
     * @return The employee name
     */
    public String getEmployeeName() {
        return employeeName;
    }

    /**
     * @param employeeName The employee name to set
     */
    public void setEmployeeName(String employeeName) {
        if (employeeName == null || employeeName.trim().isEmpty()) {
            throw new IllegalArgumentException("Employee name cannot be null or empty");
        }
        this.employeeName = employeeName;
    }

    /**
     * @return The module name
     */
    public String getModule() {
        return module;
    }

    /**
     * @param module The module name to set
     */
    public void setModule(String module) {
        if (module == null || module.trim().isEmpty()) {
            throw new IllegalArgumentException("Module cannot be null or empty");
        }
        this.module = module;
    }

    /**
     * @return The permission level
     */
    public String getPermissionLevel() {
        return permissionLevel;
    }

    /**
     * @param permissionLevel The permission level to set
     */
    public void setPermissionLevel(String permissionLevel) {
        if (permissionLevel == null || permissionLevel.trim().isEmpty()) {
            throw new IllegalArgumentException("Permission level cannot be null or empty");
        }
        this.permissionLevel = permissionLevel;
    }

    /**
     * @return The user who granted this permission
     */
    public String getGrantedBy() {
        return grantedBy;
    }

    /**
     * @param grantedBy The user who granted this permission
     */
    public void setGrantedBy(String grantedBy) {
        this.grantedBy = grantedBy;
    }

    /**
     * @return The date when permission was granted
     */
    public String getGrantedDate() {
        return grantedDate;
    }

    /**
     * @param grantedDate The date when permission was granted
     */
    public void setGrantedDate(String grantedDate) {
        this.grantedDate = grantedDate;
    }

    /**
     * @return The expiration date, or null if permanent
     */
    public String getExpiresOn() {
        return expiresOn;
    }

    /**
     * @param expiresOn The expiration date to set, or null for permanent access
     */
    public void setExpiresOn(String expiresOn) {
        this.expiresOn = expiresOn;
    }

    /**
     * @return True if the permission is active
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * @param active The active status to set
     */
    public void setActive(boolean active) {
        isActive = active;
    }

    // Business Logic Methods

    /**
     * Checks if the permission has expired.
     * Permanent permissions (null expiresOn) never expire.
     *
     * @return True if the permission has expired, false otherwise
     */
    public boolean isExpired() {
        if (expiresOn == null || expiresOn.trim().isEmpty()) {
            return false; // Permanent permission
        }

        try {
            LocalDate expiryDate = LocalDate.parse(expiresOn);
            return LocalDate.now().isAfter(expiryDate);
        } catch (Exception e) {
            // Log warning in production: logger.warn("Invalid expiry date format: {}", expiresOn);
            return false; // If date format is invalid, treat as not expired
        }
    }

    /**
     * Revokes the permission by setting active status to false.
     */
    public void revoke() {
        this.isActive = false;
    }

    /**
     * Checks if the user has view permission.
     * Requires active status, not expired, and permission level of View, Edit, or Full.
     *
     * @return True if user can view the module
     */
    public boolean canView() {
        if (!isActive || isExpired()) {
            return false;
        }
        
        return PERMISSION_VIEW.equals(permissionLevel) ||
               PERMISSION_EDIT.equals(permissionLevel) ||
               PERMISSION_FULL.equals(permissionLevel);
    }

    /**
     * Checks if the user has edit permission.
     * Requires active status, not expired, and permission level of Edit or Full.
     *
     * @return True if user can edit in the module
     */
    public boolean canEdit() {
        if (!isActive || isExpired()) {
            return false;
        }
        
        return PERMISSION_EDIT.equals(permissionLevel) ||
               PERMISSION_FULL.equals(permissionLevel);
    }

    /**
     * Checks if the user has full access permission.
     * Requires active status, not expired, and permission level of Full.
     *
     * @return True if user has full access to the module
     */
    public boolean hasFullAccess() {
        return isActive && !isExpired() && PERMISSION_FULL.equals(permissionLevel);
    }

    /**
     * Gets the current status of the permission.
     *
     * @return "Active", "Revoked", or "Expired"
     */
    public String getStatus() {
        if (!isActive) {
            return STATUS_REVOKED;
        }
        if (isExpired()) {
            return STATUS_EXPIRED;
        }
        return STATUS_ACTIVE;
    }

    /**
     * Sets the status of the permission.
     * Note: This is a convenience method that converts string status to boolean.
     *
     * @param status The status to set ("Active", "Revoked")
     * @throws IllegalArgumentException if status is not recognized
     */
    public void setStatus(String status) {
        if (STATUS_ACTIVE.equalsIgnoreCase(status)) {
            this.isActive = true;
        } else if (STATUS_REVOKED.equalsIgnoreCase(status)) {
            this.isActive = false;
        } else if (STATUS_EXPIRED.equalsIgnoreCase(status)) {
            this.isActive = false; // Expired permissions are not active
        } else {
            throw new IllegalArgumentException("Invalid status: " + status + 
                    ". Expected: Active, Revoked, or Expired");
        }
    }

    // Utility Methods

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccessControl that = (AccessControl) o;
        return Objects.equals(controlId, that.controlId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(controlId);
    }

    @Override
    public String toString() {
        return "AccessControl{" +
                "controlId='" + controlId + '\'' +
                ", employeeId='" + employeeId + '\'' +
                ", employeeName='" + employeeName + '\'' +
                ", module='" + module + '\'' +
                ", permissionLevel='" + permissionLevel + '\'' +
                ", status='" + getStatus() + '\'' +
                '}';
    }

    /**
     * Validates if the current permission configuration is valid.
     *
     * @return True if all required fields are set and permission level is valid
     */
    public boolean isValid() {
        return controlId != null && !controlId.trim().isEmpty() &&
               employeeId != null && !employeeId.trim().isEmpty() &&
               employeeName != null && !employeeName.trim().isEmpty() &&
               module != null && !module.trim().isEmpty() &&
               permissionLevel != null && !permissionLevel.trim().isEmpty() &&
               isValidPermissionLevel(permissionLevel);
    }

    /**
     * Checks if the given permission level is valid.
     *
     * @param permissionLevel Permission level to validate
     * @return True if permission level is one of the defined constants
     */
    private boolean isValidPermissionLevel(String permissionLevel) {
        return PERMISSION_NONE.equals(permissionLevel) ||
               PERMISSION_VIEW.equals(permissionLevel) ||
               PERMISSION_EDIT.equals(permissionLevel) ||
               PERMISSION_FULL.equals(permissionLevel);
    }
}