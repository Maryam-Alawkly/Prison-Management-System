package service;

import database.AccessControlDAO;
import model.AccessControl;
import java.util.List;

/**
 * Service layer for Access Control management operations. Implements Singleton
 * pattern to provide a single instance throughout the application. Handles
 * business logic for permission management, authorization, and access control.
 */
public class AccessControlService {

    private static AccessControlService instance;
    private final AccessControlDAO accessControlDAO;

    /**
     * Private constructor to enforce Singleton pattern. Initializes the DAO
     * instance.
     */
    private AccessControlService() {
        this.accessControlDAO = AccessControlDAO.getInstance();
    }

    /**
     * Returns the single instance of AccessControlService. Implements
     * thread-safe lazy initialization.
     *
     * @return AccessControlService instance
     */
    public static synchronized AccessControlService getInstance() {
        if (instance == null) {
            instance = new AccessControlService();
        }
        return instance;
    }

    /**
     * Adds a new access control record to the database.
     *
     * @param control AccessControl object containing control details
     * @return true if insertion was successful, false otherwise
     */
    public boolean addAccessControl(AccessControl control) {
        return accessControlDAO.addAccessControl(control);
    }

    /**
     * Retrieves all access control records from the database.
     *
     * @return List of all AccessControl objects
     */
    public List<AccessControl> getAllAccessControls() {
        return accessControlDAO.getAllAccessControls();
    }

    /**
     * Retrieves access controls for a specific employee.
     *
     * @param employeeId ID of the employee
     * @return List of AccessControl objects for the specified employee
     */
    public List<AccessControl> getAccessControlsByEmployee(String employeeId) {
        return accessControlDAO.getAccessControlsByEmployee(employeeId);
    }

    /**
     * Retrieves access controls for a specific module.
     *
     * @param module Name of the module
     * @return List of AccessControl objects for the specified module
     */
    public List<AccessControl> getAccessControlsByModule(String module) {
        return accessControlDAO.getAccessControlsByModule(module);
    }

    /**
     * Updates an existing access control record.
     *
     * @param control AccessControl object with updated values
     * @return true if update was successful, false otherwise
     */
    public boolean updateAccessControl(AccessControl control) {
        return accessControlDAO.updateAccessControl(control);
    }

    /**
     * Revokes access control for an employee in a specific module.
     *
     * @param employeeId ID of the employee
     * @param module Name of the module
     * @return true if revocation was successful, false otherwise
     */
    public boolean revokeAccessControl(String employeeId, String module) {
        return accessControlDAO.revokeAccessControl(employeeId, module);
    }

    /**
     * Deletes an access control record by its ID.
     *
     * @param controlId ID of the access control record
     * @return true if deletion was successful, false otherwise
     */
    public boolean deleteAccessControl(String controlId) {
        return accessControlDAO.deleteAccessControl(controlId);
    }

    /**
     * Checks if an employee has the required permission for a module.
     *
     * @param employeeId ID of the employee
     * @param module Name of the module
     * @param requiredPermission Required permission level (View, Edit, Full)
     * @return true if employee has required permission, false otherwise
     */
    public boolean hasPermission(String employeeId, String module, String requiredPermission) {
        return accessControlDAO.hasPermission(employeeId, module, requiredPermission);
    }

    /**
     * Retrieves a summary of all permissions for an employee.
     *
     * @param employeeId ID of the employee
     * @return List of permission strings in format "module: permission_level"
     */
    public List<String> getEmployeePermissions(String employeeId) {
        return accessControlDAO.getEmployeePermissions(employeeId);
    }

    /**
     * Retrieves all expired access controls that are still marked as active.
     *
     * @return List of expired AccessControl objects
     */
    public List<AccessControl> getExpiredAccessControls() {
        return accessControlDAO.getExpiredAccessControls();
    }

    /**
     * Counts total number of active access controls.
     *
     * @return Number of active access controls
     */
    public int getActiveAccessControlsCount() {
        return accessControlDAO.getActiveAccessControlsCount();
    }

    /**
     * Counts number of access controls expiring within specified days.
     *
     * @param days Number of days to check
     * @return Number of access controls expiring within specified days
     */
    public int getExpiringSoonCount(int days) {
        return accessControlDAO.getExpiringSoonCount(days);
    }
}
