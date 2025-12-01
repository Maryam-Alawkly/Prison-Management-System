package database;

import model.Employee;
import java.sql.*;

/**
 * Authentication service for employee login
 */
public class AuthService {
    
    /**
     * Authenticate employee by username and password
     * @param username Employee username
     * @param password Employee password
     * @return Employee object if authentication successful, null otherwise
     */
    public Employee authenticate(String username, String password) {
        String sql = "SELECT * FROM employees WHERE username = ? AND password = ? AND is_active = TRUE";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            stmt.setString(2, password); // In real app, use hashed passwords
            
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Employee employee = new Employee(
                    rs.getString("employee_id"),
                    rs.getString("name"),
                    rs.getString("phone"),
                    rs.getString("position"),
                    rs.getString("department"),
                    rs.getDouble("salary")
                );
                employee.setHireDate(rs.getString("hire_date"));
                employee.setStatus(rs.getString("status"));
                employee.setUsername(rs.getString("username"));
                employee.setRole(rs.getString("role"));
                employee.setLastLogin(rs.getString("last_login"));
                
                // Update last login time
                updateLastLogin(employee.getId());
                
                return employee;
            }
            
        } catch (SQLException e) {
            System.err.println("Authentication error: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Update employee's last login time
     * @param employeeId Employee ID
     */
    private void updateLastLogin(String employeeId) {
        String sql = "UPDATE employees SET last_login = NOW() WHERE employee_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, employeeId);
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            System.err.println("Error updating last login: " + e.getMessage());
        }
    }
    
    /**
     * Check if username exists
     * @param username Username to check
     * @return true if username exists
     */
    public boolean usernameExists(String username) {
        String sql = "SELECT COUNT(*) FROM employees WHERE username = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            System.err.println("Error checking username: " + e.getMessage());
        }
        
        return false;
    }
}