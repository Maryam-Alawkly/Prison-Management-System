package main;

import database.DatabaseConnection;
import database.PrisonerDAO;
import database.EmployeeDAO;
import model.Prisoner;
import model.Employee;
import java.util.List;

public class TestDatabase {
    public static void main(String[] args) {
        System.out.println("=== Testing Database Connection ===\n");
        
        // Test database connection
        DatabaseConnection.testConnection();
        
        // Test PrisonerDAO operations
        PrisonerDAO prisonerDAO = new PrisonerDAO();
        
        // Add a new prisoner
        Prisoner newPrisoner = new Prisoner("P003", "Test Prisoner", "0912998888", "Test Crime", "C-303", "2 years");
        boolean added = prisonerDAO.addPrisoner(newPrisoner);
        System.out.println(added ? "Prisoner added successfully!" : "Failed to add prisoner");
        
        // Get all prisoners
        List<Prisoner> prisoners = prisonerDAO.getAllPrisoners();
        System.out.println("\n=== All Prisoners ===");
        for (Prisoner p : prisoners) {
            System.out.println(p.toString());
        }
        
        // Test EmployeeDAO operations
        EmployeeDAO employeeDAO = new EmployeeDAO();
        
        // Add a new employee
        Employee newEmployee = new Employee("E003", "Test Employee", "0912777666", "Guard", "Security", 2000.00);
        boolean employeeAdded = employeeDAO.addEmployee(newEmployee);
        System.out.println(employeeAdded ? "Employee added successfully!" : "Failed to add employee");
        
        // Get all employees
        List<Employee> employees = employeeDAO.getAllEmployees();
        System.out.println("\n=== All Employees ===");
        for (Employee e : employees) {
            System.out.println(e.toString());
        }
        
        // Test department filter
        List<Employee> securityEmployees = employeeDAO.getEmployeesByDepartment("Security");
        System.out.println("\n=== Security Department Employees ===");
        for (Employee e : securityEmployees) {
            System.out.println(e.toString());
        }
        
        // Test salary update
        boolean salaryUpdated = employeeDAO.updateEmployeeSalary("E003", 2200.00);
        System.out.println(salaryUpdated ? "Salary updated successfully!" : "Failed to update salary");
        
        // Get total employees count
        int totalEmployees = employeeDAO.getTotalEmployees();
        System.out.println("\nTotal Employees: " + totalEmployees);
        
        // Close connection
        DatabaseConnection.closeConnection();
    }
}