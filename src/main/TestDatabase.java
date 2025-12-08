package main;

import database.DatabaseConnection;
import database.PrisonerDAO;
import database.EmployeeDAO;
import database.PrisonCellDAO;
import database.VisitorDAO;
import database.VisitDAO;
import model.Prisoner;
import model.Employee;
import model.PrisonCell;
import model.Visitor;
import model.Visit;
import java.time.LocalDateTime;
import java.util.List;

/**
 * TestDatabase - Comprehensive test class for validating database operations in
 * the Prison Management System. This class tests all DAO operations including
 * CRUD operations and statistical reporting.
 *
 * The test follows a structured approach: 1. Database connection validation 2.
 * Individual DAO operation testing 3. Statistical analysis 4. Clean resource
 * management
 */
public class TestDatabase {

    /**
     * Main entry point for the database test suite.
     *
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        System.out.println("=== Prison Management System - Database Test Suite ===\n");

        try {
            runDatabaseTests();
        } catch (Exception e) {
            System.err.println("CRITICAL ERROR: Database testing failed - " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Ensure database connection is always closed
            DatabaseConnection.closeStaticConnection();
            System.out.println("\n=== Database test suite execution completed ===");
        }
    }

    /**
     * Executes all database test operations in sequence. If the initial
     * connection test fails, all subsequent tests are aborted.
     */
    private static void runDatabaseTests() {
        // Test database connection first - abort if connection fails
        if (!testDatabaseConnection()) {
            System.err.println("Database connection test failed. Aborting all tests.");
            return;
        }

        // Execute individual DAO tests
        testPrisonerOperations();
        testEmployeeOperations();
        testPrisonCellOperations();
        testVisitorOperations();
        testVisitOperations();

        // Test statistical operations
        testStatistics();
    }

    /**
     * Tests the database connection using the Singleton DatabaseConnection
     * instance.
     *
     * @return true if connection is successful and valid, false otherwise
     */
    private static boolean testDatabaseConnection() {
        System.out.println("Testing Database Connection...");

        // Test connection using static method
        boolean connectionTest = DatabaseConnection.testStaticConnection();

        if (!connectionTest) {
            System.err.println("Database connection test FAILED!");
            return false;
        }

        // Additional connection validation
        try {
            boolean isValid = DatabaseConnection.getInstance().isConnectionValid();
            if (isValid) {
                System.out.println("Database connection is valid and active.");
                return true;
            } else {
                System.err.println("Database connection is not valid.");
                return false;
            }
        } catch (Exception e) {
            System.err.println("Error checking connection validity: " + e.getMessage());
            return false;
        }
    }

    /**
     * Tests PrisonerDAO operations including: - Adding new prisoners -
     * Retrieving all prisoners
     */
    private static void testPrisonerOperations() {
        System.out.println("\n--- Testing PrisonerDAO Operations ---");

        PrisonerDAO prisonerDAO = new PrisonerDAO();

        // Test adding a new prisoner
        Prisoner newPrisoner = new Prisoner(
                "P005",
                "Test Prisoner",
                "0912998888",
                "Test Crime",
                "C-303",
                "2 years"
        );

        boolean added = prisonerDAO.addPrisoner(newPrisoner);
        System.out.println(added ? "SUCCESS: Prisoner added successfully!"
                : "FAILED: Could not add prisoner");

        // Test retrieving all prisoners
        try {
            List<Prisoner> prisoners = prisonerDAO.getAllPrisoners();
            System.out.println("\nAll Prisoners (Total: " + prisoners.size() + "):");

            if (prisoners.isEmpty()) {
                System.out.println("No prisoners found in database.");
            } else {
                for (Prisoner prisoner : prisoners) {
                    System.out.println("  - " + prisoner.toString());
                }
            }
        } catch (Exception e) {
            System.err.println("ERROR: Failed to retrieve prisoners - " + e.getMessage());
        }
    }

    /**
     * Tests EmployeeDAO operations including: - Adding regular employees -
     * Adding administrative employees - Retrieving all employees
     */
    private static void testEmployeeOperations() {
        System.out.println("\n--- Testing EmployeeDAO Operations ---");

        EmployeeDAO employeeDAO = new EmployeeDAO();

        // Test adding a regular employee
        Employee newEmployee = new Employee(
                "E005",
                "Test Employee",
                "0912777666",
                "Guard",
                "Security",
                2000.00,
                "officer1",
                "password123",
                "Officer"
        );

        boolean employeeAdded = employeeDAO.addEmployee(newEmployee);
        System.out.println(employeeAdded ? "SUCCESS: Employee added successfully!"
                : "FAILED: Could not add employee");

        // Test adding an administrator
        Employee newAdmin = new Employee(
                "ADMIN001",
                "Ahmed Ali",
                "0914785639",
                "System Administrator",
                "Management",
                10546.00,
                "admin2",
                "admin2",
                "Administrator"
        );

        boolean adminAdded = employeeDAO.addEmployee(newAdmin);
        System.out.println(adminAdded ? "SUCCESS: Administrator added successfully!"
                : "FAILED: Could not add administrator");

        // Test retrieving all employees
        try {
            List<Employee> employees = employeeDAO.getAllEmployees();
            System.out.println("\nAll Employees (Total: " + employees.size() + "):");

            if (employees.isEmpty()) {
                System.out.println("No employees found in database.");
            } else {
                for (Employee employee : employees) {
                    System.out.println("  - " + employee.toString());
                }
            }
        } catch (Exception e) {
            System.err.println("ERROR: Failed to retrieve employees - " + e.getMessage());
        }
    }

    /**
     * Tests PrisonCellDAO operations including: - Adding new prison cells -
     * Retrieving all prison cells
     */
    private static void testPrisonCellOperations() {
        System.out.println("\n--- Testing PrisonCellDAO Operations ---");

        PrisonCellDAO cellDAO = new PrisonCellDAO();

        // Test adding a new prison cell
        PrisonCell newPrisonCell = new PrisonCell(
                "D-405",
                "General",
                8,
                "Minimum"
        );

        boolean cellAdded = cellDAO.addPrisonCell(newPrisonCell);
        System.out.println(cellAdded ? "SUCCESS: Prison cell added successfully!"
                : "FAILED: Could not add prison cell");

        // Test retrieving all prison cells
        try {
            List<PrisonCell> cells = cellDAO.getAllPrisonCells();
            System.out.println("\nAll Prison Cells (Total: " + cells.size() + "):");

            if (cells.isEmpty()) {
                System.out.println("No prison cells found in database.");
            } else {
                for (PrisonCell cell : cells) {
                    System.out.println("  - " + cell.toString());
                }
            }
        } catch (Exception e) {
            System.err.println("ERROR: Failed to retrieve prison cells - " + e.getMessage());
        }
    }

    /**
     * Tests VisitorDAO operations including: - Adding new visitors - Approving
     * visitors - Retrieving all visitors
     */
    private static void testVisitorOperations() {
        System.out.println("\n--- Testing VisitorDAO Operations ---");

        VisitorDAO visitorDAO = new VisitorDAO();

        // Test adding a new visitor
        Visitor newVisitor = new Visitor(
                "V002",
                "Test Visitor",
                "0912555444",
                "Brother",
                "P005"
        );

        boolean visitorAdded = visitorDAO.addVisitor(newVisitor);
        System.out.println(visitorAdded ? "SUCCESS: Visitor added successfully!"
                : "FAILED: Could not add visitor");

        // Test approving the visitor
        if (visitorAdded) {
            boolean approved = visitorDAO.approveVisitor("V002");
            System.out.println(approved ? "SUCCESS: Visitor approved successfully!"
                    : "FAILED: Could not approve visitor");
        }

        // Test retrieving all visitors
        try {
            List<Visitor> visitors = visitorDAO.getAllVisitors();
            System.out.println("\nAll Visitors (Total: " + visitors.size() + "):");

            if (visitors.isEmpty()) {
                System.out.println("No visitors found in database.");
            } else {
                for (Visitor visitor : visitors) {
                    System.out.println("  - " + visitor.toString());
                }
            }
        } catch (Exception e) {
            System.err.println("ERROR: Failed to retrieve visitors - " + e.getMessage());
        }
    }

    /**
     * Tests VisitDAO operations including: - Adding new visits - Retrieving all
     * visits
     */
    private static void testVisitOperations() {
        System.out.println("\n--- Testing VisitDAO Operations ---");

        VisitDAO visitDAO = new VisitDAO();

        // Test adding a new visit
        Visit newVisit = new Visit(
                "VIS003",
                "P005",
                "V002",
                LocalDateTime.now().plusDays(1),
                60
        );

        boolean visitAdded = visitDAO.addVisit(newVisit);
        System.out.println(visitAdded ? "SUCCESS: Visit added successfully!"
                : "FAILED: Could not add visit");

        // Test retrieving all visits
        try {
            List<Visit> visits = visitDAO.getAllVisits();
            System.out.println("\nAll Visits (Total: " + visits.size() + "):");

            if (visits.isEmpty()) {
                System.out.println("No visits found in database.");
            } else {
                for (Visit visit : visits) {
                    System.out.println("  - " + visit.toString());
                }
            }
        } catch (Exception e) {
            System.err.println("ERROR: Failed to retrieve visits - " + e.getMessage());
        }
    }

    /**
     * Tests statistical operations from both VisitorDAO and VisitDAO. Includes
     * visitor approval rates and visit completion statistics.
     */
    private static void testStatistics() {
        System.out.println("\n--- Testing Statistical Operations ---");

        VisitorDAO visitorDAO = new VisitorDAO();
        VisitDAO visitDAO = new VisitDAO();

        try {
            // Test visitor statistics
            int totalVisitors = visitorDAO.getTotalVisitors();
            int approvedVisitors = visitorDAO.getApprovedVisitorsCount();

            System.out.println("Visitor Statistics:");
            System.out.println("  - Total Visitors: " + totalVisitors);
            System.out.println("  - Approved Visitors: " + approvedVisitors);

            // Calculate and display approval rate
            if (totalVisitors > 0) {
                double approvalRate = (approvedVisitors * 100.0) / totalVisitors;
                System.out.println("  - Approval Rate: " + String.format("%.1f%%", approvalRate));
            } else {
                System.out.println("  - Approval Rate: 0% (no visitors)");
            }

            // Test visit statistics
            int[] visitStats = visitDAO.getVisitStatistics();
            System.out.println("\nVisit Statistics:");
            System.out.println("  - Total Visits: " + visitStats[0]);
            System.out.println("  - Completed Visits: " + visitStats[1]);
            System.out.println("  - Scheduled Visits: " + visitStats[2]);
            System.out.println("  - Cancelled Visits: " + visitStats[3]);

            // Calculate and display completion rate
            if (visitStats[0] > 0) {
                double completionRate = (visitStats[1] * 100.0) / visitStats[0];
                System.out.println("  - Completion Rate: " + String.format("%.1f%%", completionRate));
            }

        } catch (Exception e) {
            System.err.println("ERROR: Failed to retrieve statistics - " + e.getMessage());
        }
    }

    /**
     * Initializes test data for the database test suite. Currently includes
     * cleanup of old test data.
     */
    private static void initializeTestData() {
        System.out.println("Initializing test data...");
        cleanupOldTestData();
    }

    /**
     * Cleans up old test data from previous test runs. Note: Cleanup operations
     * are commented out for safety.
     */
    private static void cleanupOldTestData() {
        System.out.println("Cleaning up old test data...");

        try {
            PrisonerDAO prisonerDAO = new PrisonerDAO();
            // prisonerDAO.deletePrisoner("P005"); // Uncomment to enable prisoner cleanup

            EmployeeDAO employeeDAO = new EmployeeDAO();
            // employeeDAO.deleteEmployee("E005"); // Uncomment to enable employee cleanup
            // employeeDAO.deleteEmployee("ADMIN001");

            System.out.println("Cleanup operations are disabled for safety.");
        } catch (Exception e) {
            System.err.println("WARNING: Error during cleanup - " + e.getMessage());
        }
    }
}
