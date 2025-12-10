package database;

import model.AccessControl;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for AccessControl entity. 
 * Implements Singleton pattern for DAO instance management.
 */
public class AccessControlDAO {
    
    // Singleton instance
    private static AccessControlDAO instance;
    
    /**
     * Private constructor to enforce Singleton pattern.
     */
    private AccessControlDAO() {
        // Private constructor to prevent instantiation
    }
    
    /**
     * Returns the single instance of AccessControlDAO.
     *
     * @return AccessControlDAO instance
     */
    public static synchronized AccessControlDAO getInstance() {
        if (instance == null) {
            instance = new AccessControlDAO();
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
        String sql = "INSERT INTO access_controls (control_id, employee_id, employee_name, "
                + "module, permission_level, granted_by, granted_date, expires_on, is_active) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, control.getControlId());
            stmt.setString(2, control.getEmployeeId());
            stmt.setString(3, control.getEmployeeName());
            stmt.setString(4, control.getModule());
            stmt.setString(5, control.getPermissionLevel());
            stmt.setString(6, control.getGrantedBy());
            stmt.setDate(7, Date.valueOf(control.getGrantedDate()));

            if (control.getExpiresOn() != null && !control.getExpiresOn().isEmpty()) {
                stmt.setDate(8, Date.valueOf(control.getExpiresOn()));
            } else {
                stmt.setNull(8, Types.DATE);
            }

            stmt.setBoolean(9, control.isActive());

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            System.err.println("Error adding access control: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves all access control records from the database.
     *
     * @return List of all AccessControl objects
     */
    public List<AccessControl> getAllAccessControls() {
        List<AccessControl> controls = new ArrayList<>();
        String sql = "SELECT * FROM access_controls ORDER BY employee_name, module";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                AccessControl control = createAccessControlFromResultSet(rs);
                controls.add(control);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving access controls: " + e.getMessage());
        }

        return controls;
    }

    /**
     * Retrieves access controls for a specific employee.
     *
     * @param employeeId ID of the employee
     * @return List of AccessControl objects for the specified employee
     */
    public List<AccessControl> getAccessControlsByEmployee(String employeeId) {
        List<AccessControl> controls = new ArrayList<>();
        String sql = "SELECT * FROM access_controls WHERE employee_id = ? ORDER BY module";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, employeeId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                AccessControl control = createAccessControlFromResultSet(rs);
                controls.add(control);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving access controls by employee: " + e.getMessage());
        }

        return controls;
    }

    /**
     * Retrieves access controls for a specific module.
     *
     * @param module Name of the module
     * @return List of AccessControl objects for the specified module
     */
    public List<AccessControl> getAccessControlsByModule(String module) {
        List<AccessControl> controls = new ArrayList<>();
        String sql = "SELECT * FROM access_controls WHERE module = ? ORDER BY employee_name";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, module);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                AccessControl control = createAccessControlFromResultSet(rs);
                controls.add(control);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving access controls by module: " + e.getMessage());
        }

        return controls;
    }

    /**
     * Updates an existing access control record.
     *
     * @param control AccessControl object with updated values
     * @return true if update was successful, false otherwise
     */
    public boolean updateAccessControl(AccessControl control) {
        String sql = "UPDATE access_controls SET permission_level = ?, expires_on = ?, "
                + "is_active = ? WHERE control_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, control.getPermissionLevel());

            if (control.getExpiresOn() != null && !control.getExpiresOn().isEmpty()) {
                stmt.setDate(2, Date.valueOf(control.getExpiresOn()));
            } else {
                stmt.setNull(2, Types.DATE);
            }

            stmt.setBoolean(3, control.isActive());
            stmt.setString(4, control.getControlId());

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            System.err.println("Error updating access control: " + e.getMessage());
            return false;
        }
    }

    /**
     * Revokes access control for an employee in a specific module.
     *
     * @param employeeId ID of the employee
     * @param module Name of the module
     * @return true if revocation was successful, false otherwise
     */
    public boolean revokeAccessControl(String employeeId, String module) {
        String sql = "UPDATE access_controls SET is_active = false WHERE employee_id = ? AND module = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, employeeId);
            stmt.setString(2, module);

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            System.err.println("Error revoking access control: " + e.getMessage());
            return false;
        }
    }

    /**
     * Deletes an access control record by its ID.
     *
     * @param controlId ID of the access control record
     * @return true if deletion was successful, false otherwise
     */
    public boolean deleteAccessControl(String controlId) {
        String sql = "DELETE FROM access_controls WHERE control_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, controlId);
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting access control: " + e.getMessage());
            return false;
        }
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
        String sql = "SELECT permission_level FROM access_controls "
                + "WHERE employee_id = ? AND module = ? AND is_active = true";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, employeeId);
            stmt.setString(2, module);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String permissionLevel = rs.getString("permission_level");
                return checkPermissionHierarchy(permissionLevel, requiredPermission);
            }

        } catch (SQLException e) {
            System.err.println("Error checking permission: " + e.getMessage());
        }

        return false;
    }

    /**
     * Checks permission hierarchy based on permission levels.
     *
     * @param userPermission Permission level of the user
     * @param requiredPermission Required permission level
     * @return true if user permission meets or exceeds required permission
     */
    private boolean checkPermissionHierarchy(String userPermission, String requiredPermission) {
        // Define permission hierarchy
        switch (requiredPermission) {
            case "View":
                return userPermission.equals("View")
                        || userPermission.equals("Edit")
                        || userPermission.equals("Full");
            case "Edit":
                return userPermission.equals("Edit")
                        || userPermission.equals("Full");
            case "Full":
                return userPermission.equals("Full");
            default:
                return false;
        }
    }

    /**
     * Retrieves a summary of all permissions for an employee.
     *
     * @param employeeId ID of the employee
     * @return List of permission strings in format "module: permission_level"
     */
    public List<String> getEmployeePermissions(String employeeId) {
        List<String> permissions = new ArrayList<>();
        String sql = "SELECT module, permission_level FROM access_controls "
                + "WHERE employee_id = ? AND is_active = true";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, employeeId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String permission = rs.getString("module") + ": " + rs.getString("permission_level");
                permissions.add(permission);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving employee permissions: " + e.getMessage());
        }

        return permissions;
    }

    /**
     * Retrieves all expired access controls that are still marked as active.
     *
     * @return List of expired AccessControl objects
     */
    public List<AccessControl> getExpiredAccessControls() {
        List<AccessControl> controls = new ArrayList<>();
        String sql = "SELECT * FROM access_controls WHERE expires_on < CURDATE() AND is_active = true";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                AccessControl control = createAccessControlFromResultSet(rs);
                controls.add(control);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving expired access controls: " + e.getMessage());
        }

        return controls;
    }

    /**
     * Creates an AccessControl object from a ResultSet.
     *
     * @param rs ResultSet containing access control data
     * @return AccessControl object populated with data from ResultSet
     * @throws SQLException if database error occurs
     */
    private AccessControl createAccessControlFromResultSet(ResultSet rs) throws SQLException {
        AccessControl control = new AccessControl(
                rs.getString("control_id"),
                rs.getString("employee_id"),
                rs.getString("employee_name"),
                rs.getString("module"),
                rs.getString("permission_level")
        );

        control.setGrantedBy(rs.getString("granted_by"));
        control.setGrantedDate(rs.getString("granted_date"));

        Date expiresDate = rs.getDate("expires_on");
        if (expiresDate != null) {
            control.setExpiresOn(expiresDate.toString());
        }

        control.setActive(rs.getBoolean("is_active"));

        return control;
    }

    /**
     * Counts total number of active access controls.
     *
     * @return Number of active access controls
     */
    public int getActiveAccessControlsCount() {
        String sql = "SELECT COUNT(*) FROM access_controls WHERE is_active = true";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            System.err.println("Error counting active access controls: " + e.getMessage());
        }

        return 0;
    }

    /**
     * Counts number of access controls expiring within specified days.
     *
     * @param days Number of days to check
     * @return Number of access controls expiring within specified days
     */
    public int getExpiringSoonCount(int days) {
        String sql = "SELECT COUNT(*) FROM access_controls "
                + "WHERE expires_on BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL ? DAY) "
                + "AND is_active = true";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, days);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            System.err.println("Error counting expiring access controls: " + e.getMessage());
        }

        return 0;
    }
}
