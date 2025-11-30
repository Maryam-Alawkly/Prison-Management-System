package database;

import model.Employee;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Employee entity
 * Handles all database operations for employees
 */
public class EmployeeDAO {
    
    /**
     * Add a new employee to the database
     * @param employee Employee object to be added
     * @return true if successful, false otherwise
     */
    public boolean addEmployee(Employee employee) {
        String sql = "INSERT INTO employees (employee_id, name, phone, position, department, salary, hire_date) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, employee.getId());
            stmt.setString(2, employee.getName());
            stmt.setString(3, employee.getPhone());
            stmt.setString(4, employee.getPosition());
            stmt.setString(5, employee.getDepartment());
            stmt.setDouble(6, employee.getSalary());
            stmt.setDate(7, Date.valueOf(employee.getHireDate()));
            
            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
            
        } catch (SQLException e) {
            System.err.println("Error adding employee: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Retrieve all employees from the database
     * @return List of all employees
     */
    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM employees";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
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
                employees.add(employee);
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving employees: " + e.getMessage());
        }
        
        return employees;
    }
    
    /**
     * Find employee by ID
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
                return employee;
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding employee: " + e.getMessage());
            }
        
        return null;
    }
    
    /**
     * Update employee information
     * @param employee Employee object with updated data
     * @return true if successful, false otherwise
     */
    public boolean updateEmployee(Employee employee) {
        String sql = "UPDATE employees SET name=?, phone=?, position=?, department=?, salary=?, status=? WHERE employee_id=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, employee.getName());
            stmt.setString(2, employee.getPhone());
            stmt.setString(3, employee.getPosition());
            stmt.setString(4, employee.getDepartment());
            stmt.setDouble(5, employee.getSalary());
            stmt.setString(6, employee.getStatus());
            stmt.setString(7, employee.getId());
            
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating employee: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Delete employee from database
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
     * Get employees by department
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
                employees.add(employee);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting employees by department: " + e.getMessage());
        }
        
        return employees;
    }
    
    /**
     * Get employees by position
     * @param position Position to filter by
     * @return List of employees with the specified position
     */
    public List<Employee> getEmployeesByPosition(String position) {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM employees WHERE position = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {stmt.setString(1, position);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
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
                employees.add(employee);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting employees by position: " + e.getMessage());
        }
        
        return employees;
    }
    
    /**
     * Update employee salary
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
                employees.add(employee);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting employees by salary: " + e.getMessage());
        }
        
        return employees;
    }
}