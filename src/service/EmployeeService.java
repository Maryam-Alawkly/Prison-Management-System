package service;

import database.EmployeeDAO;
import model.Employee;
import java.util.List;

/**
 * Service layer for Employee management operations.
 * Implements Singleton pattern to provide a single instance throughout the application.
 * Handles business logic for employee registration, authentication, and management.
 */
public class EmployeeService {
    
    private static EmployeeService instance;
    private final EmployeeDAO employeeDAO;
    
    /**
     * Private constructor to enforce Singleton pattern.
     * Initializes the DAO instance.
     */
    private EmployeeService() {
        this.employeeDAO = EmployeeDAO.getInstance();
    }
    
    /**
     * Returns the single instance of EmployeeService.
     * Implements thread-safe lazy initialization.
     *
     * @return EmployeeService instance
     */
    public static synchronized EmployeeService getInstance() {
        if (instance == null) {
            instance = new EmployeeService();
        }
        return instance;
    }
    
    /**
     * Adds a new employee record to the database.
     *
     * @param employee Employee object containing all employee details
     * @return true if employee was added successfully, false otherwise
     */
    public boolean addEmployee(Employee employee) {
        return employeeDAO.addEmployee(employee);
    }
    
    /**
     * Retrieves all employees from the database.
     *
     * @return List of all Employee objects in the database
     */
    public List<Employee> getAllEmployees() {
        return employeeDAO.getAllEmployees();
    }
    
    /**
     * Finds an employee by their name.
     *
     * @param name Name of the employee to find
     * @return Employee object if found, null otherwise
     */
    public Employee getEmployeeByName(String name) {
        return employeeDAO.getEmployeeByName(name);
    }
    
    /**
     * Finds an employee by their unique identifier.
     *
     * @param employeeId Unique identifier of the employee
     * @return Employee object if found, null otherwise
     */
    public Employee getEmployeeById(String employeeId) {
        return employeeDAO.getEmployeeById(employeeId);
    }
    
    /**
     * Finds an employee by their username for login purposes.
     *
     * @param username Username to search for
     * @return Employee object if found and active, null otherwise
     */
    public Employee getEmployeeByUsername(String username) {
        return employeeDAO.getEmployeeByUsername(username);
    }
    
    /**
     * Updates an existing employee's information in the database.
     *
     * @param employee Employee object with updated data
     * @return true if update was successful, false otherwise
     */
    public boolean updateEmployee(Employee employee) {
        return employeeDAO.updateEmployee(employee);
    }
    
    /**
     * Deletes an employee from the database by their ID.
     *
     * @param employeeId Unique identifier of the employee to delete
     * @return true if deletion was successful, false otherwise
     */
    public boolean deleteEmployee(String employeeId) {
        return employeeDAO.deleteEmployee(employeeId);
    }
    
    /**
     * Authenticates an employee using username and password.
     *
     * @param username Employee's username
     * @param password Employee's password
     * @return Employee object if authentication successful, null otherwise
     */
    public Employee authenticateEmployee(String username, String password) {
        return employeeDAO.authenticateEmployee(username, password);
    }
    
    /**
     * Updates an employee's password.
     *
     * @param employeeId Unique identifier of the employee
     * @param newPassword New password to set
     * @return true if password update was successful, false otherwise
     */
    public boolean updatePassword(String employeeId, String newPassword) {
        return employeeDAO.updatePassword(employeeId, newPassword);
    }
    
    /**
     * Retrieves employees belonging to a specific department.
     *
     * @param department Department name to filter by
     * @return List of Employee objects in the specified department
     */
    public List<Employee> getEmployeesByDepartment(String department) {
        return employeeDAO.getEmployeesByDepartment(department);
    }
    
    /**
     * Retrieves employees with a specific role.
     *
     * @param role Role to filter by
     * @return List of Employee objects with the specified role
     */
    public List<Employee> getEmployeesByRole(String role) {
        return employeeDAO.getEmployeesByRole(role);
    }
    
    /**
     * Retrieves only active employees from the database.
     *
     * @return List of active Employee objects
     */
    public List<Employee> getActiveEmployees() {
        return employeeDAO.getActiveEmployees();
    }
    
    /**
     * Updates an employee's salary.
     *
     * @param employeeId Unique identifier of the employee
     * @param newSalary New salary amount
     * @return true if salary update was successful, false otherwise
     */
    public boolean updateEmployeeSalary(String employeeId, double newSalary) {
        return employeeDAO.updateEmployeeSalary(employeeId, newSalary);
    }
    
    /**
     * Gets the total number of employees in the database.
     *
     * @return Total count of employees
     */
    public int getTotalEmployees() {
        return employeeDAO.getTotalEmployees();
    }
    
    /**
     * Searches for employees by name or ID using a search term.
     *
     * @param searchTerm Term to search in employee name or ID
     * @return List of Employee objects matching the search criteria
     */
    public List<Employee> searchEmployees(String searchTerm) {
        return employeeDAO.searchEmployees(searchTerm);
    }
    
    /**
     * Gets the total number of active employees.
     *
     * @return Count of active employees
     */
    public int getActiveEmployeesCount() {
        return employeeDAO.getActiveEmployeesCount();
    }
    
    /**
     * Deactivates an employee account without deleting the record.
     *
     * @param employeeId Unique identifier of the employee
     * @return true if deactivation was successful, false otherwise
     */
    public boolean deactivateEmployee(String employeeId) {
        return employeeDAO.deactivateEmployee(employeeId);
    }
    
    /**
     * Reactivates a previously deactivated employee account.
     *
     * @param employeeId Unique identifier of the employee
     * @return true if reactivation was successful, false otherwise
     */
    public boolean reactivateEmployee(String employeeId) {
        return employeeDAO.reactivateEmployee(employeeId);
    }
}