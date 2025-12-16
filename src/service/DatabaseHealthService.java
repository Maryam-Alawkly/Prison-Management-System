// DatabaseHealthService.java
package service;

import database.DatabaseConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Service for database health checks and monitoring.
 */
public class DatabaseHealthService {
    
    private static final Logger LOGGER = Logger.getLogger(DatabaseHealthService.class.getName());
    private static DatabaseHealthService instance;
    
    private DatabaseHealthService() {}
    
    public static synchronized DatabaseHealthService getInstance() {
        if (instance == null) {
            instance = new DatabaseHealthService();
        }
        return instance;
    }
    
    /**
     * Test database connection with retry logic
     */
    public boolean testConnection() {
        try {
            boolean isConnected = DatabaseConnection.testStaticConnection();
            if (isConnected) {
                LOGGER.info("Database connection successful");
            } else {
                LOGGER.warning("Database connection failed");
            }
            return isConnected;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Database connection test failed", e);
            return false;
        }
    }
    
    /**
     * Test connection with retry mechanism
     */
    public boolean testConnectionWithRetry(int maxRetries, long delayMillis) {
        for (int i = 0; i < maxRetries; i++) {
            if (testConnection()) {
                return true;
            }
            
            if (i < maxRetries - 1) {
                LOGGER.info("Retrying database connection (" + (i + 1) + "/" + maxRetries + ")");
                try {
                    Thread.sleep(delayMillis);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
        return false;
    }
    
    /**
     * Get database connection status for UI display
     */
    public String getConnectionStatus() {
        boolean isConnected = testConnection();
        return isConnected ? "Connected" : "Disconnected";
    }
    
    /**
     * Check if database is ready for operations
     */
    public boolean isDatabaseReady() {
        return testConnection();
    }
}