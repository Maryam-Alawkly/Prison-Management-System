package database;

import model.Employee;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Employee entity Handles all database operations for
 * employees
 */
public class EmployeeDAO {

    /**
     * Add a new employee to the database
     *
     * @param employee Employee object to be added
     * @return true if successful, false otherwise
     */
    public boolean addEmployee(Employee employee) {
        String sql = "INSERT INTO employees (employee_id, name, phone, position, department, salary, "
                + "hire_date, status, username, password, role, last_login, is_active) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, employee.getId());
            stmt.setString(2, employee.getName());
            stmt.setString(3, employee.getPhone());
            stmt.setString(4, employee.getPosition());
            stmt.setString(5, employee.getDepartment());
            stmt.setDouble(6, employee.getSalary());
            stmt.setDate(7, Date.valueOf(employee.getHireDate()));
            stmt.setString(8, employee.getStatus());
            stmt.setString(9, employee.getUsername());
            stmt.setString(10, employee.getPassword());
            stmt.setString(11, employee.getRole());
            stmt.setString(12, employee.getLastLogin());
            stmt.setBoolean(13, employee.isActive());

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            System.err.println("Error adding employee: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieve all employees from the database
     *
     * @return List of all employees
     */
    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM employees";

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Employee employee = createEmployeeFromResultSet(rs);
                employees.add(employee);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving employees: " + e.getMessage());
        }

        return employees;
    }

    /**
     * Find employee by ID
     *
     * @param employeeId ID of the employee to find
     * @return Employee object if found, null otherwise
     */
    public Employee getEmployeeById(String employeeId) {
        String sql = "SELECT * FROM employees WHERE employee_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, employeeId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return createEmployeeFromResultSet(rs);
            }

        } catch (SQLException e) {
            System.err.println("Error finding employee: " + e.getMessage());
        }

        return null;
    }

    /**
     * Find employee by username (for login)
     *
     * @param username Username to find
     * @return Employee object if found, null otherwise
     */
    public Employee getEmployeeByUsername(String username) {
        String sql = "SELECT * FROM employees WHERE username = ? AND is_active = true";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return createEmployeeFromResultSet(rs);
            }

        } catch (SQLException e) {
            System.err.println("Error finding employee by username: " + e.getMessage());
        }

        return null;
    }

    /**
     * Update employee information
     *
     * @param employee Employee object with updated data
     * @return true if successful, false otherwise
     */
    public boolean updateEmployee(Employee employee) {
        String sql = "UPDATE employees SET name=?, phone=?, position=?, department=?, "
                + "salary=?, status=?, username=?, password=?, role=?, last_login=?, is_active=? "
                + "WHERE employee_id=?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, employee.getName());
            stmt.setString(2, employee.getPhone());
            stmt.setString(3, employee.getPosition());
            stmt.setString(4, employee.getDepartment());
            stmt.setDouble(5, employee.getSalary());
            stmt.setString(6, employee.getStatus());
            stmt.setString(7, employee.getUsername());
            stmt.setString(8, employee.getPassword());
            stmt.setString(9, employee.getRole());
            stmt.setString(10, employee.getLastLogin());
            stmt.setBoolean(11, employee.isActive());
            stmt.setString(12, employee.getId());

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            System.err.println("Error updating employee: " + e.getMessage());
            return false;
        }
    }

    /**
     * Delete employee from database
     *
     * @param employeeId ID of employee to delete
     * @return true if successful, false otherwise
     */
    public boolean deleteEmployee(String employeeId) {
        String sql = "DELETE FROM employees WHERE employee_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, employeeId);
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting employee: " + e.getMessage());
            return false;
        }
    }

    /**
     * Authenticate employee login
     *
     * @param username Employee username
     * @param password Employee password
     * @return Employee object if authentication successful, null otherwise
     */
    public Employee authenticateEmployee(String username, String password) {
        String sql = "SELECT * FROM employees WHERE username = ? AND password = ? AND is_active = true";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Employee employee = createEmployeeFromResultSet(rs);
                // Update last login time
                updateLastLogin(employee.getId());
                return employee;
            }

        } catch (SQLException e) {
            System.err.println("Error authenticating employee: " + e.getMessage());
        }

        return null;
    }

    /**
     * Update last login time for employee
     *
     * @param employeeId ID of employee
     * @return true if successful, false otherwise
     */
    public boolean updateLastLogin(String employeeId) {
        String sql = "UPDATE employees SET last_login = NOW() WHERE employee_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, employeeId);
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            System.err.println("Error updating last login: " + e.getMessage());
            return false;
        }
    }

    /**
     * Update employee password
     *
     * @param employeeId ID of employee
     * @param newPassword New password
     * @return true if successful, false otherwise
     */
    public boolean updatePassword(String employeeId, String newPassword) {
        String sql = "UPDATE employees SET password = ? WHERE employee_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newPassword);
            stmt.setString(2, employeeId);
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            System.err.println("Error updating password: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get employees by department
     *
     * @param department Department to filter by
     * @return List of employees in the specified department
     */
    public List<Employee> getEmployeesByDepartment(String department) {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM employees WHERE department = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, department);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Employee employee = createEmployeeFromResultSet(rs);
                employees.add(employee);
            }

        } catch (SQLException e) {
            System.err.println("Error getting employees by department: " + e.getMessage());
        }

        return employees;
    }

    /**
     * Get employees by role
     *
     * @param role Role to filter by
     * @return List of employees with the specified role
     */
    public List<Employee> getEmployeesByRole(String role) {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM employees WHERE role = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, role);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Employee employee = createEmployeeFromResultSet(rs);
                employees.add(employee);
            }

        } catch (SQLException e) {
            System.err.println("Error getting employees by role: " + e.getMessage());
        }

        return employees;
    }

    /**
     * Get active employees only
     *
     * @return List of active employees
     */
    public List<Employee> getActiveEmployees() {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM employees WHERE is_active = true";

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Employee employee = createEmployeeFromResultSet(rs);
                employees.add(employee);
            }

        } catch (SQLException e) {
            System.err.println("Error getting active employees: " + e.getMessage());
        }

        return employees;
    }

    /**
     * Update employee salary
     *
     * @param employeeId ID of employee
     * @param newSalary New salary amount
     * @return true if successful, false otherwise
     */
    public boolean updateEmployeeSalary(String employeeId, double newSalary) {
        String sql = "UPDATE employees SET salary = ? WHERE employee_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, newSalary);
            stmt.setString(2, employeeId);

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            System.err.println("Error updating employee salary: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get total number of employees
     *
     * @return Total count of employees
     */
    public int getTotalEmployees() {
        String sql = "SELECT COUNT(*) as total FROM employees";

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt("total");
            }

        } catch (SQLException e) {
            System.err.println("Error getting total employees: " + e.getMessage());
        }

        return 0;
    }

    /**
     * Get employees with salary greater than specified amount
     *
     * @param minSalary Minimum salary threshold
     * @return List of employees with salary greater than minSalary
     */
    public List<Employee> getEmployeesWithSalaryGreaterThan(double minSalary) {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM employees WHERE salary > ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, minSalary);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Employee employee = createEmployeeFromResultSet(rs);
                employees.add(employee);
            }

        } catch (SQLException e) {
            System.err.println("Error getting employees by salary: " + e.getMessage());
        }

        return employees;
    }

    /**
     * Create Employee object from ResultSet
     *
     * @param rs ResultSet from database query
     * @return Employee object
     * @throws SQLException if database error occurs
     */
    private Employee createEmployeeFromResultSet(ResultSet rs) throws SQLException {
        Employee employee = new Employee(
                rs.getString("employee_id"),
                rs.getString("name"),
                rs.getString("phone"),
                rs.getString("position"),
                rs.getString("department"),
                rs.getDouble("salary")
        );

        // Set additional properties
        employee.setHireDate(rs.getString("hire_date"));
        employee.setStatus(rs.getString("status"));
        employee.setUsername(rs.getString("username"));
        employee.setPassword(rs.getString("password"));
        employee.setRole(rs.getString("role"));

        // Handle last_login (might be null)
        if (rs.getTimestamp("last_login") != null) {
            employee.setLastLogin(rs.getTimestamp("last_login").toString());
        }

        employee.setActive(rs.getBoolean("is_active"));

        return employee;
    }
}
