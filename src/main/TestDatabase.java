package main;

import database.DatabaseConnection;
import database.PrisonerDAO;
import database.EmployeeDAO;
import database.CellDAO;
import model.Prisoner;
import model.Employee;
import model.Cell;
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

        // Test CellDAO operations
        CellDAO cellDAO = new CellDAO();

        // Add a new cell
        Cell newCell = new Cell("D-404", "General", 8, "Minimum");
        boolean cellAdded = cellDAO.addCell(newCell);
        System.out.println(cellAdded ? "Cell added successfully!" : "Failed to add cell");

        // Get all cells
        List<Cell> cells = cellDAO.getAllCells();
        System.out.println("\n=== All Cells ===");
        for (Cell c : cells) {
            System.out.println(c.toString());
        }

        // Test available cells
        List<Cell> availableCells = cellDAO.getAvailableCells();
        System.out.println("\n=== Available Cells ===");
        for (Cell c : availableCells) {
            System.out.println(c.toString());
        }

        // Test occupancy statistics
        int[] stats = cellDAO.getOccupancyStatistics();
        System.out.println("\n=== Occupancy Statistics ===");
        System.out.println("Total Capacity: " + stats[0]);
        System.out.println("Total Occupancy: " + stats[1]);
        System.out.println("Available Space: " + stats[2]);

        // Test cell by security level
        List<Cell> mediumSecurityCells = cellDAO.getCellsBySecurityLevel("Medium");
        System.out.println("\n=== Medium Security Cells ===");
        for (Cell c : mediumSecurityCells) {
            System.out.println(c.toString());
        }

        // Close connection
        DatabaseConnection.closeConnection();
    }
}
