package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Database connection manager using Singleton pattern. Provides a single,
 * reusable connection instance throughout the application. This ensures
 * efficient resource usage and prevents multiple connections from being opened.
 */
public class DatabaseConnection {

    // Singleton instance
    private static DatabaseConnection instance;

    // Database connection parameters
    private static final String URL = "jdbc:mysql://localhost:3306/prison_management_system";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "7187480"; //your_password_in_mySql

    // Database connection object
    private Connection connection;

    /**
     * Private constructor to enforce Singleton pattern. Prevents direct
     * instantiation from outside the class.
     */
    private DatabaseConnection() {
        // Connection is lazily initialized in getConnection() method
    }

    /**
     * Returns the single instance of DatabaseConnection. Implements thread-safe
     * lazy initialization with double-checked locking.
     *
     * @return DatabaseConnection instance
     */
    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            synchronized (DatabaseConnection.class) {
                if (instance == null) {
                    instance = new DatabaseConnection();
                }
            }
        }
        return instance;
    }

    /**
     * Establishes and returns a database connection. Creates a new connection
     * if one doesn't exist or if the existing connection is closed.
     *
     * @return Active database connection
     * @throws SQLException if database connection fails
     */
    public Connection getConnection() throws SQLException {
        // Check if connection is null or closed
        if (connection == null || connection.isClosed()) {
            try {
                // Load MySQL JDBC driver
                Class.forName("com.mysql.cj.jdbc.Driver");

                // Establish database connection
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

                // Set connection properties for better performance
                connection.setAutoCommit(true);

                System.out.println("Database connection established successfully.");

            } catch (ClassNotFoundException e) {
                System.err.println("MySQL JDBC Driver not found: " + e.getMessage());
                throw new SQLException("Database driver not available", e);
            } catch (SQLException e) {
                System.err.println("Failed to establish database connection: " + e.getMessage());
                throw e;
            }
        }
        return connection;
    }

    /**
     * Safely closes the database connection. Ensures proper resource cleanup
     * when the connection is no longer needed.
     */
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null; // Clear the reference
                System.out.println("Database connection closed successfully.");
            } catch (SQLException e) {
                System.err.println("Error occurred while closing database connection: " + e.getMessage());
            }
        }
    }

    /**
     * Tests the database connection validity. Useful for health checks and
     * connection validation at application startup.
     *
     * @return true if connection is valid and active, false otherwise
     */
    public boolean testConnection() {
        try {
            Connection conn = getConnection();
            if (conn != null && !conn.isClosed() && conn.isValid(5)) {
                System.out.println("Database connection test: SUCCESS - Connection is valid and active.");
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Database connection test: FAILED - " + e.getMessage());
        }
        return false;
    }

    /**
     * Static convenience method for backward compatibility. Provides access to
     * the database connection without needing to get the instance first.
     *
     * @return Active database connection
     * @throws SQLException if database connection fails
     */
    public static Connection getStaticConnection() throws SQLException {
        return getInstance().getConnection();
    }

    /**
     * Static convenience method to close the connection. Provides easy access
     * to connection cleanup.
     */
    public static void closeStaticConnection() {
        if (instance != null) {
            instance.closeConnection();
        }
    }

    /**
     * Static convenience method to test the connection.
     *
     * @return true if connection is valid and active, false otherwise
     */
    public static boolean testStaticConnection() {
        return getInstance().testConnection();
    }

    /**
     * Checks if the connection is currently open and valid.
     *
     * @return true if connection is open and valid, false otherwise
     */
    public boolean isConnectionValid() {
        if (connection == null) {
            return false;
        }

        try {
            return !connection.isClosed() && connection.isValid(5);
        } catch (SQLException e) {
            System.err.println("Error checking connection validity: " + e.getMessage());
            return false;
        }
    }
}
