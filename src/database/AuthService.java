package database;

import model.Employee;
import java.sql.*;

/**
 * Authentication service for employee login management. Handles user
 * authentication, password validation, and login session tracking. This service
 * uses the Singleton DatabaseConnection for database operations.
 */
public class AuthService {

    /**
     * Authenticates an employee using username and password credentials.
     * Validates credentials against the database and returns employee
     * information if successful. Updates the last login timestamp upon
     * successful authentication.
     *
     * @param username Employee username for authentication
     * @param password Employee password for authentication (note: should use
     * hashed passwords in production)
     * @return Employee object if authentication is successful, null if
     * authentication fails
     */
    public Employee authenticate(String username, String password) {
        String sql = "SELECT * FROM employees WHERE username = ? AND password = ? AND is_active = TRUE";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Set query parameters
            stmt.setString(1, username);
            stmt.setString(2, password); // Note: In production applications, use password hashing

            ResultSet resultSet = stmt.executeQuery();

            // Check if user exists and credentials are valid
            if (resultSet.next()) {
                Employee employee = createEmployeeFromResultSet(resultSet);

                // Update last login timestamp
                updateLastLogin(employee.getId());

                return employee;
            }

        } catch (SQLException e) {
            System.err.println("Authentication error occurred: " + e.getMessage());
        }

        return null;
    }

    /**
     * Updates the last login timestamp for an employee. This method is called
     * after successful authentication to track login activity.
     *
     * @param employeeId Unique identifier of the employee
     */
    private void updateLastLogin(String employeeId) {
        String sql = "UPDATE employees SET last_login = NOW() WHERE employee_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, employeeId);
            int updatedRows = stmt.executeUpdate();

            if (updatedRows == 0) {
                System.err.println("No employee found with ID: " + employeeId + " for last login update");
            }

        } catch (SQLException e) {
            System.err.println("Error updating last login timestamp: " + e.getMessage());
        }
    }

    /**
     * Checks if a username already exists in the system. This is useful for
     * user registration or username validation processes.
     *
     * @param username Username to check for existence
     * @return true if username exists in the database, false otherwise
     */
    public boolean usernameExists(String username) {
        String sql = "SELECT COUNT(*) as user_count FROM employees WHERE username = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                int count = resultSet.getInt("user_count");
                return count > 0;
            }

        } catch (SQLException e) {
            System.err.println("Error checking username existence: " + e.getMessage());
        }

        return false;
    }

    /**
     * Creates an Employee object from a ResultSet. Maps database columns to
     * Employee object properties.
     *
     * @param resultSet ResultSet containing employee data from database
     * @return Populated Employee object
     * @throws SQLException if database error occurs
     */
    private Employee createEmployeeFromResultSet(ResultSet resultSet) throws SQLException {
        Employee employee = new Employee(
                resultSet.getString("employee_id"),
                resultSet.getString("name"),
                resultSet.getString("phone"),
                resultSet.getString("position"),
                resultSet.getString("department"),
                resultSet.getDouble("salary")
        );

        // Set additional employee properties
        employee.setHireDate(resultSet.getString("hire_date"));
        employee.setStatus(resultSet.getString("status"));
        employee.setUsername(resultSet.getString("username"));
        employee.setRole(resultSet.getString("role"));

        // Handle last login (may be null for first-time users)
        String lastLogin = resultSet.getString("last_login");
        if (lastLogin != null) {
            employee.setLastLogin(lastLogin);
        }

        // Note: Password is not set for security reasons
        return employee;
    }

    /**
     * Validates employee credentials without creating a full login session.
     * This is useful for quick credential validation without updating last
     * login.
     *
     * @param username Employee username
     * @param password Employee password
     * @return true if credentials are valid, false otherwise
     */
    public boolean validateCredentials(String username, String password) {
        String sql = "SELECT COUNT(*) as valid_count FROM employees "
                + "WHERE username = ? AND password = ? AND is_active = TRUE";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                int count = resultSet.getInt("valid_count");
                return count > 0;
            }

        } catch (SQLException e) {
            System.err.println("Error validating credentials: " + e.getMessage());
        }

        return false;
    }

    /**
     * Checks if an employee account is active. Useful for validating account
     * status before authentication attempts.
     *
     * @param username Employee username
     * @return true if employee account is active, false otherwise
     */
    public boolean isAccountActive(String username) {
        String sql = "SELECT is_active FROM employees WHERE username = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                return resultSet.getBoolean("is_active");
            }

        } catch (SQLException e) {
            System.err.println("Error checking account status: " + e.getMessage());
        }

        return false;
    }

    /**
     * Retrieves an employee by username without password validation. Useful for
     * password reset or account management scenarios.
     *
     * @param username Employee username
     * @return Employee object if found, null otherwise
     */
    public Employee getEmployeeByUsername(String username) {
        String sql = "SELECT * FROM employees WHERE username = ? AND is_active = TRUE";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                return createEmployeeFromResultSet(resultSet);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving employee by username: " + e.getMessage());
        }

        return null;
    }

    /**
     * Locks an employee account after multiple failed login attempts. This is a
     * security feature to prevent brute force attacks.
     *
     * @param username Employee username
     * @return true if account was successfully locked, false otherwise
     */
    public boolean lockAccount(String username) {
        String sql = "UPDATE employees SET is_active = FALSE WHERE username = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            int updatedRows = stmt.executeUpdate();

            return updatedRows > 0;

        } catch (SQLException e) {
            System.err.println("Error locking employee account: " + e.getMessage());
            return false;
        }
    }

    /**
     * Unlocks an employee account and resets it to active status.
     *
     * @param username Employee username
     * @return true if account was successfully unlocked, false otherwise
     */
    public boolean unlockAccount(String username) {
        String sql = "UPDATE employees SET is_active = TRUE WHERE username = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            int updatedRows = stmt.executeUpdate();

            return updatedRows > 0;

        } catch (SQLException e) {
            System.err.println("Error unlocking employee account: " + e.getMessage());
            return false;
        }
    }
}
