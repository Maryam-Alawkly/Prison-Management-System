package service;

import model.Employee;

/**
 * Service layer for Authentication operations. Implements Singleton pattern to
 * provide a single instance throughout the application. Acts as a business
 * logic layer for user authentication and account management. Note: Renamed
 * database.AuthService to DatabaseAuthService to avoid naming conflict.
 */
public class AuthService {

    private static AuthService instance;
    private final database.AuthService databaseAuthService;

    /**
     * Private constructor to enforce Singleton pattern. Initializes the
     * database AuthService instance.
     */
    private AuthService() {
        this.databaseAuthService = database.AuthService.getInstance();
    }

    /**
     * Returns the single instance of AuthService. Implements thread-safe lazy
     * initialization.
     *
     * @return AuthService instance
     */
    public static synchronized AuthService getInstance() {
        if (instance == null) {
            instance = new AuthService();
        }
        return instance;
    }

    /**
     * Authenticates an employee using username and password credentials.
     *
     * @param username Employee username for authentication
     * @param password Employee password for authentication
     * @return Employee object if authentication is successful, null if
     * authentication fails
     */
    public Employee authenticate(String username, String password) {
        return databaseAuthService.authenticate(username, password);
    }

    /**
     * Checks if a username already exists in the system.
     *
     * @param username Username to check for existence
     * @return true if username exists in the database, false otherwise
     */
    public boolean usernameExists(String username) {
        return databaseAuthService.usernameExists(username);
    }

    /**
     * Validates employee credentials without creating a full login session.
     *
     * @param username Employee username
     * @param password Employee password
     * @return true if credentials are valid, false otherwise
     */
    public boolean validateCredentials(String username, String password) {
        return databaseAuthService.validateCredentials(username, password);
    }

    /**
     * Checks if an employee account is active.
     *
     * @param username Employee username
     * @return true if employee account is active, false otherwise
     */
    public boolean isAccountActive(String username) {
        return databaseAuthService.isAccountActive(username);
    }

    /**
     * Retrieves an employee by username without password validation.
     *
     * @param username Employee username
     * @return Employee object if found, null otherwise
     */
    public Employee getEmployeeByUsername(String username) {
        return databaseAuthService.getEmployeeByUsername(username);
    }

    /**
     * Locks an employee account after multiple failed login attempts.
     *
     * @param username Employee username
     * @return true if account was successfully locked, false otherwise
     */
    public boolean lockAccount(String username) {
        return databaseAuthService.lockAccount(username);
    }

    /**
     * Unlocks an employee account and resets it to active status.
     *
     * @param username Employee username
     * @return true if account was successfully unlocked, false otherwise
     */
    public boolean unlockAccount(String username) {
        return databaseAuthService.unlockAccount(username);
    }
}
