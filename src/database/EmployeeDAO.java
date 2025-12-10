package database;

import model.Employee;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Employee entity. Implements Singleton pattern for DAO
 * instance management.
 */
public class EmployeeDAO {

    // Singleton instance
    private static EmployeeDAO instance;

    /**
     * Private constructor to enforce Singleton pattern.
     */
    private EmployeeDAO() {
        // Private constructor to prevent instantiation
    }

    /**
     * Returns the single instance of EmployeeDAO.
     *
     * @return EmployeeDAO instance
     */
    public static synchronized EmployeeDAO getInstance() {
        if (instance == null) {
            instance = new EmployeeDAO();
        }
        return instance;
    }

    /**
     * Adds a new employee record to the database. Includes all employee details
     * including login credentials and status.
     *
     * @param employee Employee object containing all employee details
     * @return true if employee was added successfully, false otherwise
     */
    public boolean addEmployee(Employee employee) {
        String sql = "INSERT INTO employees (employee_id, name, phone, position, department, salary, "
                + "hire_date, status, username, password, role, last_login, is_active) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
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
            System.err.println("Error adding employee to database: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves all employees from the database. Returns a complete list of all
     * employee records.
     *
     * @return List of all Employee objects in the database
     */
    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM employees";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Employee employee = createEmployeeFromResultSet(rs);
                employees.add(employee);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving all employees: " + e.getMessage());
        }

        return employees;
    }

    /**
     * Finds an employee by their name. Returns the first employee matching the
     * specified name.
     *
     * @param name Name of the employee to find
     * @return Employee object if found, null otherwise
     */
    public Employee getEmployeeByName(String name) {
        String sql = "SELECT * FROM employees WHERE name = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return createEmployeeFromResultSet(rs);
            }

        } catch (SQLException e) {
            System.err.println("Error finding employee by name: " + e.getMessage());
        }

        return null;
    }

    /**
     * Finds an employee by their unique identifier.
     *
     * @param employeeId Unique identifier of the employee
     * @return Employee object if found, null otherwise
     */
    public Employee getEmployeeById(String employeeId) {
        String sql = "SELECT * FROM employees WHERE employee_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, employeeId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return createEmployeeFromResultSet(rs);
            }

        } catch (SQLException e) {
            System.err.println("Error finding employee by ID: " + e.getMessage());
        }

        return null;
    }

    /**
     * Finds an employee by their username for login purposes. Only returns
     * active employees.
     *
     * @param username Username to search for
     * @return Employee object if found and active, null otherwise
     */
    public Employee getEmployeeByUsername(String username) {
        String sql = "SELECT * FROM employees WHERE username = ? AND is_active = true";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
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
     * Updates an existing employee's information in the database. Updates all
     * fields except employee_id.
     *
     * @param employee Employee object with updated data
     * @return true if update was successful, false otherwise
     */
    public boolean updateEmployee(Employee employee) {
        String sql = "UPDATE employees SET name=?, phone=?, position=?, department=?, "
                + "salary=?, status=?, username=?, password=?, role=?, last_login=?, is_active=? "
                + "WHERE employee_id=?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
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
            System.err.println("Error updating employee information: " + e.getMessage());
            return false;
        }
    }

    /**
     * Deletes an employee from the database by their ID.
     *
     * @param employeeId Unique identifier of the employee to delete
     * @return true if deletion was successful, false otherwise
     */
    public boolean deleteEmployee(String employeeId) {
        String sql = "DELETE FROM employees WHERE employee_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, employeeId);
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting employee from database: " + e.getMessage());
            return false;
        }
    }

    /**
     * Authenticates an employee using username and password. Updates last login
     * timestamp upon successful authentication.
     *
     * @param username Employee's username
     * @param password Employee's password
     * @return Employee object if authentication successful, null otherwise
     */
    public Employee authenticateEmployee(String username, String password) {
        String sql = "SELECT * FROM employees WHERE username = ? AND password = ? AND is_active = true";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Employee employee = createEmployeeFromResultSet(rs);
                updateLastLogin(employee.getId());
                return employee;
            }

        } catch (SQLException e) {
            System.err.println("Error authenticating employee: " + e.getMessage());
        }

        return null;
    }

    /**
     * Updates the last login timestamp for an employee.
     *
     * @param employeeId Unique identifier of the employee
     * @return true if update was successful, false otherwise
     */
    public boolean updateLastLogin(String employeeId) {
        String sql = "UPDATE employees SET last_login = NOW() WHERE employee_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, employeeId);
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            System.err.println("Error updating last login timestamp: " + e.getMessage());
            return false;
        }
    }

    /**
     * Updates an employee's password.
     *
     * @param employeeId Unique identifier of the employee
     * @param newPassword New password to set
     * @return true if password update was successful, false otherwise
     */
    public boolean updatePassword(String employeeId, String newPassword) {
        String sql = "UPDATE employees SET password = ? WHERE employee_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newPassword);
            stmt.setString(2, employeeId);
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            System.err.println("Error updating employee password: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves employees belonging to a specific department.
     *
     * @param department Department name to filter by
     * @return List of Employee objects in the specified department
     */
    public List<Employee> getEmployeesByDepartment(String department) {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM employees WHERE department = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, department);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Employee employee = createEmployeeFromResultSet(rs);
                employees.add(employee);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving employees by department: " + e.getMessage());
        }

        return employees;
    }

    /**
     * Retrieves employees with a specific role.
     *
     * @param role Role to filter by
     * @return List of Employee objects with the specified role
     */
    public List<Employee> getEmployeesByRole(String role) {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM employees WHERE role = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, role);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Employee employee = createEmployeeFromResultSet(rs);
                employees.add(employee);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving employees by role: " + e.getMessage());
        }

        return employees;
    }

    /**
     * Retrieves only active employees from the database.
     *
     * @return List of active Employee objects
     */
    public List<Employee> getActiveEmployees() {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM employees WHERE is_active = true";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Employee employee = createEmployeeFromResultSet(rs);
                employees.add(employee);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving active employees: " + e.getMessage());
        }

        return employees;
    }

    /**
     * Updates an employee's salary.
     *
     * @param employeeId Unique identifier of the employee
     * @param newSalary New salary amount
     * @return true if salary update was successful, false otherwise
     */
    public boolean updateEmployeeSalary(String employeeId, double newSalary) {
        String sql = "UPDATE employees SET salary = ? WHERE employee_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
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
     * Gets the total number of employees in the database.
     *
     * @return Total count of employees
     */
    public int getTotalEmployees() {
        String sql = "SELECT COUNT(*) as total FROM employees";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt("total");
            }

        } catch (SQLException e) {
            System.err.println("Error getting total employee count: " + e.getMessage());
        }

        return 0;
    }

    /**
     * Retrieves employees with salary greater than a specified amount.
     *
     * @param minSalary Minimum salary threshold
     * @return List of Employee objects with salary greater than minSalary
     */
    public List<Employee> getEmployeesWithSalaryGreaterThan(double minSalary) {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM employees WHERE salary > ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, minSalary);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Employee employee = createEmployeeFromResultSet(rs);
                employees.add(employee);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving employees by salary range: " + e.getMessage());
        }

        return employees;
    }

    /**
     * Creates an Employee object from a database ResultSet. Maps database
     * columns to Employee object properties.
     *
     * @param resultSet ResultSet containing employee data
     * @return Employee object populated with data
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

        // Set additional properties
        employee.setHireDate(resultSet.getString("hire_date"));
        employee.setStatus(resultSet.getString("status"));
        employee.setUsername(resultSet.getString("username"));
        employee.setPassword(resultSet.getString("password"));
        employee.setRole(resultSet.getString("role"));

        // Handle last_login timestamp (may be null)
        Timestamp lastLogin = resultSet.getTimestamp("last_login");
        if (lastLogin != null) {
            employee.setLastLogin(lastLogin.toString());
        }

        employee.setActive(resultSet.getBoolean("is_active"));

        return employee;
    }

    /**
     * Searches for employees by name or ID using a search term.
     *
     * @param searchTerm Term to search in employee name or ID
     * @return List of Employee objects matching the search criteria
     */
    public List<Employee> searchEmployees(String searchTerm) {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM employees WHERE name LIKE ? OR employee_id LIKE ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            String searchPattern = "%" + searchTerm + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Employee employee = createEmployeeFromResultSet(rs);
                employees.add(employee);
            }

        } catch (SQLException e) {
            System.err.println("Error searching employees: " + e.getMessage());
        }

        return employees;
    }

    /**
     * Gets the total number of active employees.
     *
     * @return Count of active employees
     */
    public int getActiveEmployeesCount() {
        String sql = "SELECT COUNT(*) as active_count FROM employees WHERE is_active = true";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt("active_count");
            }

        } catch (SQLException e) {
            System.err.println("Error getting active employee count: " + e.getMessage());
        }

        return 0;
    }

    /**
     * Deactivates an employee account without deleting the record.
     *
     * @param employeeId Unique identifier of the employee
     * @return true if deactivation was successful, false otherwise
     */
    public boolean deactivateEmployee(String employeeId) {
        String sql = "UPDATE employees SET is_active = false WHERE employee_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, employeeId);
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            System.err.println("Error deactivating employee: " + e.getMessage());
            return false;
        }
    }

    /**
     * Reactivates a previously deactivated employee account.
     *
     * @param employeeId Unique identifier of the employee
     * @return true if reactivation was successful, false otherwise
     */
    public boolean reactivateEmployee(String employeeId) {
        String sql = "UPDATE employees SET is_active = true WHERE employee_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, employeeId);
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            System.err.println("Error reactivating employee: " + e.getMessage());
            return false;
        }
    }
}
