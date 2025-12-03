package database;

import model.AccessControl;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for AccessControl entity
 */
public class AccessControlDAO {

    /**
     * Add a new access control
     */
    public boolean addAccessControl(AccessControl control) {
        String sql = "INSERT INTO access_controls (control_id, employee_id, employee_name, "
                + "module, permission_level, granted_by, granted_date, expires_on, is_active) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, control.getControlId());
            stmt.setString(2, control.getEmployeeId());
            stmt.setString(3, control.getEmployeeName());
            stmt.setString(4, control.getModule());
            stmt.setString(5, control.getPermissionLevel());
            stmt.setString(6, control.getGrantedBy());
            stmt.setDate(7, Date.valueOf(control.getGrantedDate()));

            if (control.getExpiresOn() != null) {
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
     * Get all access controls
     */
    public List<AccessControl> getAllAccessControls() {
        List<AccessControl> controls = new ArrayList<>();
        String sql = "SELECT * FROM access_controls ORDER BY employee_name, module";

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                AccessControl control = createAccessControlFromResultSet(rs);
                controls.add(control);
            }

        } catch (SQLException e) {
            System.err.println("Error getting access controls: " + e.getMessage());
        }

        return controls;
    }

    /**
     * Get access controls by employee
     */
    public List<AccessControl> getAccessControlsByEmployee(String employeeId) {
        List<AccessControl> controls = new ArrayList<>();
        String sql = "SELECT * FROM access_controls WHERE employee_id = ? ORDER BY module";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, employeeId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                AccessControl control = createAccessControlFromResultSet(rs);
                controls.add(control);
            }

        } catch (SQLException e) {
            System.err.println("Error getting access controls by employee: " + e.getMessage());
        }

        return controls;
    }

    /**
     * Get access controls by module
     */
    public List<AccessControl> getAccessControlsByModule(String module) {
        List<AccessControl> controls = new ArrayList<>();
        String sql = "SELECT * FROM access_controls WHERE module = ? ORDER BY employee_name";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, module);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                AccessControl control = createAccessControlFromResultSet(rs);
                controls.add(control);
            }

        } catch (SQLException e) {
            System.err.println("Error getting access controls by module: " + e.getMessage());
        }

        return controls;
    }

    /**
     * Update access control
     */
    public boolean updateAccessControl(AccessControl control) {
        String sql = "UPDATE access_controls SET permission_level = ?, expires_on = ?, "
                + "is_active = ? WHERE control_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, control.getPermissionLevel());

            if (control.getExpiresOn() != null) {
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
     * Revoke access control
     */
    public boolean revokeAccessControl(String employeeId, String module) {
        String sql = "UPDATE access_controls SET is_active = false WHERE employee_id = ? AND module = ?";

        try (Connection conn = DatabaseConnection.getConnection();
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
     * Delete access control
     */
    public boolean deleteAccessControl(String controlId) {
        String sql = "DELETE FROM access_controls WHERE control_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
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
     * Check if employee has permission for module
     */
    public boolean hasPermission(String employeeId, String module, String requiredPermission) {
        String sql = "SELECT permission_level FROM access_controls "
                + "WHERE employee_id = ? AND module = ? AND is_active = true";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, employeeId);
            stmt.setString(2, module);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String permissionLevel = rs.getString("permission_level");

                // Check permission hierarchy
                switch (requiredPermission) {
                    case "View":
                        return permissionLevel.equals("View")
                                || permissionLevel.equals("Edit")
                                || permissionLevel.equals("Full");
                    case "Edit":
                        return permissionLevel.equals("Edit")
                                || permissionLevel.equals("Full");
                    case "Full":
                        return permissionLevel.equals("Full");
                    default:
                        return false;
                }
            }

        } catch (SQLException e) {
            System.err.println("Error checking permission: " + e.getMessage());
        }

        return false;
    }

    /**
     * Get employee permissions summary
     */
    public List<String> getEmployeePermissions(String employeeId) {
        List<String> permissions = new ArrayList<>();
        String sql = "SELECT module, permission_level FROM access_controls "
                + "WHERE employee_id = ? AND is_active = true";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, employeeId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                permissions.add(rs.getString("module") + ": " + rs.getString("permission_level"));
            }

        } catch (SQLException e) {
            System.err.println("Error getting employee permissions: " + e.getMessage());
        }

        return permissions;
    }

    /**
     * Get expired access controls
     */
    public List<AccessControl> getExpiredAccessControls() {
        List<AccessControl> controls = new ArrayList<>();
        String sql = "SELECT * FROM access_controls WHERE expires_on < CURDATE() AND is_active = true";

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                AccessControl control = createAccessControlFromResultSet(rs);
                controls.add(control);
            }

        } catch (SQLException e) {
            System.err.println("Error getting expired access controls: " + e.getMessage());
        }

        return controls;
    }

    /**
     * Create AccessControl object from ResultSet
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

        if (rs.getDate("expires_on") != null) {
            control.setExpiresOn(rs.getString("expires_on"));
        }

        control.setActive(rs.getBoolean("is_active"));

        return control;
    }
}
