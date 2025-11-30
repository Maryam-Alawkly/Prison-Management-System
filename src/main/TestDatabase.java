package main;

import database.DatabaseConnection;
import database.PrisonerDAO;
import database.EmployeeDAO;
import database.CellDAO;
import database.VisitorDAO;
import database.VisitDAO;
import model.Prisoner;
import model.Employee;
import model.Cell;
import model.Visitor;
import model.Visit;
import java.time.LocalDateTime;
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

        // Test VisitorDAO operations
        VisitorDAO visitorDAO = new VisitorDAO();

        // Add a new visitor
        Visitor newVisitor = new Visitor("V001", "Test Visitor", "0912555444", "Brother", "P001");
        boolean visitorAdded = visitorDAO.addVisitor(newVisitor);
        System.out.println(visitorAdded ? "Visitor added successfully!" : "Failed to add visitor");

        // Approve visitor
        boolean approved = visitorDAO.approveVisitor("V001");
        System.out.println(approved ? "Visitor approved successfully!" : "Failed to approve visitor");

        // Get all visitors
        List<Visitor> visitors = visitorDAO.getAllVisitors();
        System.out.println("\n=== All Visitors ===");
        for (Visitor v : visitors) {
            System.out.println(v.toString());
        }

        // Test VisitDAO operations
        VisitDAO visitDAO = new VisitDAO();

        // Add a new visit
        Visit newVisit = new Visit("VIS001", "P001", "V001", LocalDateTime.now().plusDays(1), 60);
        boolean visitAdded = visitDAO.addVisit(newVisit);
        System.out.println(visitAdded ? "Visit added successfully!" : "Failed to add visit");

        // Get all visits
        List<Visit> visits = visitDAO.getAllVisits();
        System.out.println("\n=== All Visits ===");
        for (Visit v : visits) {
            System.out.println(v.toString());
        }

        // Test visit statistics
        int[] visitStats = visitDAO.getVisitStatistics();
        System.out.println("\n=== Visit Statistics ===");
        System.out.println("Total Visits: " + visitStats[0]);
        System.out.println("Completed Visits: " + visitStats[1]);
        System.out.println("Scheduled Visits: " + visitStats[2]);
        System.out.println("Cancelled Visits: " + visitStats[3]);

        // Test visitor statistics
        int totalVisitors = visitorDAO.getTotalVisitors();
        int approvedVisitors = visitorDAO.getApprovedVisitorsCount();
        System.out.println("\n=== Visitor Statistics ===");
        System.out.println("Total Visitors: " + totalVisitors);
        System.out.println("Approved Visitors: " + approvedVisitors);

        // Close connection
        DatabaseConnection.closeConnection();
    }
}
