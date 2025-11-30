package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Class to manage database connection
 */
public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/prison_management_system";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "7187480";

    private static Connection connection = null;

    /**
     * Get database connection
     *
     * @return Connection object
     * @throws SQLException if connection fails
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                // Load MySQL JDBC Driver
                Class.forName("com.mysql.cj.jdbc.Driver");

                // Establish connection
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                System.out.println("Database connected successfully!");

            } catch (ClassNotFoundException e) {
                throw new SQLException("MySQL JDBC Driver not found", e);
            }
        }
        return connection;
    }

    /**
     * Close database connection
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Database connection closed! ");
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }

    /**
     * Test database connection
     */
    public static void testConnection() {
        try (Connection conn = getConnection()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("Database connection test: SUCCESS!");
            }
        } catch (SQLException e) {
            System.err.println("Database connection test: FAILED!");
            System.err.println("Error: " + e.getMessage());
        }
    }

    /**
     * Clear all data from all tables (for testing/reset purposes) Use with
     * caution - this will delete ALL data permanently!
     */
    public static boolean clearAllData() {
        try (Connection conn = getConnection();
                Statement stmt = conn.createStatement()) {

            // Disable foreign key checks
            stmt.execute("SET FOREIGN_KEY_CHECKS = 0");

            // Delete all data in correct order
            stmt.executeUpdate("DELETE FROM visits");
            stmt.executeUpdate("DELETE FROM visitors");
            stmt.executeUpdate("DELETE FROM prisoners");
            stmt.executeUpdate("DELETE FROM employees");
            stmt.executeUpdate("DELETE FROM cells");

            // Enable foreign key checks
            stmt.execute("SET FOREIGN_KEY_CHECKS = 1");

            System.out.println("All data cleared successfully!");
            return true;

        } catch (SQLException e) {
            System.err.println("Error clearing data: " + e.getMessage());
            return false;
        }
    }
}
