package model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Employee class represents a staff member in the prison management system.
 * This class extends the Person class and adds employee-specific properties
 * such as position, department, salary, and authentication credentials.
 */
public class Employee extends Person {

    // Constants for employee status
    public static final String STATUS_ACTIVE = "Active";
    public static final String STATUS_INACTIVE = "Inactive";
    public static final String STATUS_SUSPENDED = "Suspended";
    public static final String STATUS_TERMINATED = "Terminated";

    // Constants for employee roles
    public static final String ROLE_OFFICER = "Officer";
    public static final String ROLE_SUPERVISOR = "Supervisor";
    public static final String ROLE_MANAGER = "Manager";
    public static final String ROLE_ADMINISTRATOR = "Administrator";
    public static final String ROLE_HR = "Human Resources";

    // Employee properties
    private String position;
    private String department;
    private double salary;
    private String hireDate;
    private String status;
    private String username;
    private String password;
    private String role;
    private String lastLogin;
    private boolean isActive;

    /**
     * Constructs an Employee with basic information. Sets default values for
     * hire date, status, role, and active status.
     *
     * @param id Unique employee identifier
     * @param name Full name of the employee
     * @param phone Contact phone number
     * @param position Job position/title
     * @param department Department where employee works
     * @param salary Monthly salary amount
     * @throws IllegalArgumentException if any required parameter is null or
     * empty
     */
    public Employee(String id, String name, String phone,
            String position, String department, double salary) {
        super(id, name, phone);
        validateParameters(position, department, salary);

        this.position = position;
        this.department = department;
        this.salary = salary;
        this.hireDate = LocalDate.now().toString();
        this.status = STATUS_ACTIVE;
        this.username = "";
        this.password = "";
        this.role = ROLE_OFFICER;
        this.lastLogin = null;
        this.isActive = true;
    }

    /**
     * Constructs an Employee with complete information including authentication
     * credentials.
     *
     * @param id Unique employee identifier
     * @param name Full name of the employee
     * @param phone Contact phone number
     * @param position Job position/title
     * @param department Department where employee works
     * @param salary Monthly salary amount
     * @param username System username for authentication
     * @param password System password for authentication
     * @param role System role/authorization level
     * @throws IllegalArgumentException if any required parameter is null or
     * empty
     */
    public Employee(String id, String name, String phone,
            String position, String department, double salary,
            String username, String password, String role) {
        super(id, name, phone);
        validateParameters(position, department, salary);
        validateCredentials(username, password, role);

        this.position = position;
        this.department = department;
        this.salary = salary;
        this.hireDate = LocalDate.now().toString();
        this.status = STATUS_ACTIVE;
        this.username = username;
        this.password = password;
        this.role = role;
        this.lastLogin = null;
        this.isActive = true;
    }

    /**
     * Validates required parameters for employee creation.
     *
     * @param position Job position to validate
     * @param department Department to validate
     * @param salary Salary amount to validate
     */
    private void validateParameters(String position, String department, double salary) {
        if (position == null || position.trim().isEmpty()) {
            throw new IllegalArgumentException("Position cannot be null or empty");
        }
        if (department == null || department.trim().isEmpty()) {
            throw new IllegalArgumentException("Department cannot be null or empty");
        }
        if (salary < 0) {
            throw new IllegalArgumentException("Salary cannot be negative");
        }
    }

    /**
     * Validates authentication credentials.
     *
     * @param username Username to validate
     * @param password Password to validate
     * @param role Role to validate
     */
    private void validateCredentials(String username, String password, String role) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        if (role == null || role.trim().isEmpty()) {
            throw new IllegalArgumentException("Role cannot be null or empty");
        }
    }

    // =================== Getter Methods ===================
    /**
     * Gets the employee's job position.
     *
     * @return The job position
     */
    public String getPosition() {
        return position;
    }

    /**
     * Gets the employee's department.
     *
     * @return The department
     */
    public String getDepartment() {
        return department;
    }

    /**
     * Gets the employee's monthly salary.
     *
     * @return The monthly salary
     */
    public double getSalary() {
        return salary;
    }

    /**
     * Gets the employee's hire date.
     *
     * @return The hire date in string format
     */
    public String getHireDate() {
        return hireDate;
    }

    /**
     * Gets the employee's current status.
     *
     * @return The status (Active, Inactive, Suspended, Terminated)
     */
    public String getStatus() {
        return status;
    }

    /**
     * Gets the employee's system username.
     *
     * @return The username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the employee's system password. Note: In production, passwords
     * should be encrypted/hashed.
     *
     * @return The password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Gets the employee's system role.
     *
     * @return The role
     */
    public String getRole() {
        return role;
    }

    /**
     * Gets the timestamp of the employee's last login.
     *
     * @return Last login timestamp, or null if never logged in
     */
    public String getLastLogin() {
        return lastLogin;
    }

    /**
     * Checks if the employee is currently active.
     *
     * @return True if the employee is active, false otherwise
     */
    public boolean isActive() {
        return isActive;
    }

    // =================== Setter Methods ===================
    /**
     * Sets the employee's job position.
     *
     * @param position The new job position
     * @throws IllegalArgumentException if position is null or empty
     */
    public void setPosition(String position) {
        if (position == null || position.trim().isEmpty()) {
            throw new IllegalArgumentException("Position cannot be null or empty");
        }
        this.position = position;
    }

    /**
     * Sets the employee's department.
     *
     * @param department The new department
     * @throws IllegalArgumentException if department is null or empty
     */
    public void setDepartment(String department) {
        if (department == null || department.trim().isEmpty()) {
            throw new IllegalArgumentException("Department cannot be null or empty");
        }
        this.department = department;
    }

    /**
     * Sets the employee's monthly salary.
     *
     * @param salary The new monthly salary
     * @throws IllegalArgumentException if salary is negative
     */
    public void setSalary(double salary) {
        if (salary < 0) {
            throw new IllegalArgumentException("Salary cannot be negative");
        }
        this.salary = salary;
    }

    /**
     * Sets the employee's hire date.
     *
     * @param hireDate The hire date in string format (YYYY-MM-DD)
     */
    public void setHireDate(String hireDate) {
        this.hireDate = hireDate;
    }

    /**
     * Sets the employee's status. Also updates the active flag based on status.
     *
     * @param status The new status
     */
    public void setStatus(String status) {
        this.status = status;
        this.isActive = STATUS_ACTIVE.equals(status);
    }

    /**
     * Sets the employee's system username.
     *
     * @param username The new username
     * @throws IllegalArgumentException if username is null or empty
     */
    public void setUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        this.username = username;
    }

    /**
     * Sets the employee's system password. Note: In production, passwords
     * should be encrypted/hashed before setting.
     *
     * @param password The new password
     * @throws IllegalArgumentException if password is null or empty
     */
    public void setPassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        this.password = password;
    }

    /**
     * Sets the employee's system role.
     *
     * @param role The new role
     * @throws IllegalArgumentException if role is null or empty
     */
    public void setRole(String role) {
        if (role == null || role.trim().isEmpty()) {
            throw new IllegalArgumentException("Role cannot be null or empty");
        }
        this.role = role;
    }

    /**
     * Sets the timestamp of the employee's last login.
     *
     * @param lastLogin Last login timestamp
     */
    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    /**
     * Sets the employee's active status. Also updates the status field
     * accordingly.
     *
     * @param isActive True to activate, false to deactivate
     */
    public void setActive(boolean isActive) {
        this.isActive = isActive;
        this.status = isActive ? STATUS_ACTIVE : STATUS_INACTIVE;
    }

    // =================== Business Logic Methods ===================
    /**
     * Calculates the employee's annual salary.
     *
     * @return Annual salary (monthly salary × 12)
     */
    public double calculateAnnualSalary() {
        return this.salary * 12;
    }

    /**
     * Promotes the employee to a new position with a new salary.
     *
     * @param newPosition The new job position
     * @param newSalary The new monthly salary
     * @throws IllegalArgumentException if newPosition is null/empty or
     * newSalary is negative
     */
    public void promoteEmployee(String newPosition, double newSalary) {
        if (newPosition == null || newPosition.trim().isEmpty()) {
            throw new IllegalArgumentException("New position cannot be null or empty");
        }
        if (newSalary < 0) {
            throw new IllegalArgumentException("New salary cannot be negative");
        }

        this.position = newPosition;
        this.salary = newSalary;
    }

    /**
     * Terminates the employee's employment. Sets status to terminated and marks
     * as inactive.
     */
    public void terminateEmployee() {
        this.status = STATUS_TERMINATED;
        this.isActive = false;
    }

    /**
     * Activates the employee's account. Sets status to active and marks as
     * active.
     */
    public void activateEmployee() {
        this.status = STATUS_ACTIVE;
        this.isActive = true;
    }

    /**
     * Records the current timestamp as the employee's last login. Uses ISO-8601
     * format for the timestamp.
     */
    public void recordLogin() {
        this.lastLogin = LocalDateTime.now().toString();
    }

    /**
     * Checks if the employee has administrator privileges.
     *
     * @return True if the employee's role is Administrator, false otherwise
     */
    public boolean isAdministrator() {
        return ROLE_ADMINISTRATOR.equalsIgnoreCase(this.role);
    }

    /**
     * Checks if the employee has officer privileges.
     *
     * @return True if the employee's role is Officer, false otherwise
     */
    public boolean isOfficer() {
        return ROLE_OFFICER.equalsIgnoreCase(this.role);
    }

    /**
     * Checks if the employee has system management privileges. Managers and
     * Administrators can manage the system.
     *
     * @return True if the employee can manage the system, false otherwise
     */
    public boolean canManageSystem() {
        return isAdministrator()
                || ROLE_MANAGER.equalsIgnoreCase(this.role)
                || ROLE_SUPERVISOR.equalsIgnoreCase(this.role);
    }

    /**
     * Validates the provided credentials against the employee's stored
     * credentials. Also checks if the employee account is active.
     *
     * @param inputUsername Username to validate
     * @param inputPassword Password to validate
     * @return True if credentials match and account is active, false otherwise
     */
    public boolean validateCredentials(String inputUsername, String inputPassword) {
        return this.isActive
                && this.username.equals(inputUsername)
                && this.password.equals(inputPassword);
    }

    /**
     * Gets detailed information about the employee. Includes all relevant
     * employee data for display or reporting purposes.
     *
     * @return Detailed employee information as a formatted string
     */
    public String getEmployeeDetails() {
        StringBuilder details = new StringBuilder();
        details.append("Employee ID: ").append(getId()).append("\n");
        details.append("Name: ").append(getName()).append("\n");
        details.append("Position: ").append(position).append("\n");
        details.append("Department: ").append(department).append("\n");
        details.append("Salary: $").append(String.format("%.2f", salary)).append("\n");
        details.append("Annual Salary: $").append(String.format("%.2f", calculateAnnualSalary())).append("\n");
        details.append("Hire Date: ").append(hireDate).append("\n");
        details.append("Status: ").append(status).append("\n");
        details.append("Role: ").append(role).append("\n");
        details.append("Username: ").append(username.isEmpty() ? "Not set" : username).append("\n");
        details.append("Last Login: ").append(lastLogin != null ? lastLogin : "Never");

        return details.toString();
    }

    /**
     * Checks if the employee has a valid configuration. Validates all required
     * fields are properly set.
     *
     * @return True if the employee configuration is valid, false otherwise
     */
    public boolean isValid() {
        return getId() != null && !getId().trim().isEmpty()
                && getName() != null && !getName().trim().isEmpty()
                && position != null && !position.trim().isEmpty()
                && department != null && !department.trim().isEmpty()
                && salary >= 0
                && hireDate != null && !hireDate.trim().isEmpty()
                && status != null && !status.trim().isEmpty();
    }

    // =================== Overridden Methods ===================
    /**
     * Returns a string representation of the employee. Provides a concise
     * summary for display purposes.
     *
     * @return String representation of the employee
     */
    @Override
    public String toString() {
        return String.format("Employee: %s | Position: %s | Department: %s | Salary: $%.2f",
                getName(), position, department, salary);
    }

    /**
     * Compares this employee with another object for equality. Two employees
     * are considered equal if they have the same ID.
     *
     * @param o Object to compare with
     * @return True if objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        Employee employee = (Employee) o;
        return Objects.equals(getId(), employee.getId());
    }

    /**
     * Returns a hash code value for the employee.
     *
     * @return Hash code based on employee ID
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getId());
    }
}
