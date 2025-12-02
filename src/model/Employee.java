package model;

import java.time.LocalDateTime;

/**
 * Employee class represents a staff member in the prison system Inherits from
 * Person class and adds employee-specific properties
 */
public class Employee extends Person {

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
     * Constructor for Employee class
     *
     * @param id Employee ID
     * @param name Employee name
     * @param phone Contact phone
     * @param position Job position
     * @param department Department
     * @param salary Monthly salary
     */
    public Employee(String id, String name, String phone,
            String position, String department, double salary) {
        super(id, name, phone);
        this.position = position;
        this.department = department;
        this.salary = salary;
        this.hireDate = java.time.LocalDate.now().toString();
        this.status = "Active";
        this.username = ""; // Initialize new fields
        this.password = "";
        this.role = "Officer";
        this.lastLogin = null;
        this.isActive = true;
    }

    /**
     * Full constructor with all fields
     *
     * @param id Employee ID
     * @param name Employee name
     * @param phone Contact phone
     * @param position Job position
     * @param department Department
     * @param salary Monthly salary
     * @param username System username
     * @param password System password
     * @param role User role
     */
    public Employee(String id, String name, String phone,
            String position, String department, double salary,
            String username, String password, String role) {
        super(id, name, phone);
        this.position = position;
        this.department = department;
        this.salary = salary;
        this.hireDate = java.time.LocalDate.now().toString();
        this.status = "Active";
        this.username = username;
        this.password = password;
        this.role = role;
        this.lastLogin = null;
        this.isActive = true;
    }

    // Getter methods
    public String getPosition() {
        return position;
    }

    public String getDepartment() {
        return department;
    }

    public double getSalary() {
        return salary;
    }

    public String getHireDate() {
        return hireDate;
    }

    public String getStatus() {
        return status;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public boolean isActive() {
        return isActive;
    }

    // Setter methods
    public void setPosition(String position) {
        this.position = position;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public void setHireDate(String hireDate) {
        this.hireDate = hireDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    /**
     * Calculate annual salary
     *
     * @return Annual salary (monthly salary * 12)
     */
    public double calculateAnnualSalary() {
        return this.salary * 12;
    }

    /**
     * Promote employee to new position with salary increase
     *
     * @param newPosition New job position
     * @param newSalary New salary amount
     */
    public void promoteEmployee(String newPosition, double newSalary) {
        this.position = newPosition;
        this.salary = newSalary;
    }

    /**
     * Terminate employee
     */
    public void terminateEmployee() {
        this.status = "Inactive";
        this.isActive = false;
    }

    /**
     * Activate employee
     */
    public void activateEmployee() {
        this.status = "Active";
        this.isActive = true;
    }

    /**
     * Record login activity
     */
    public void recordLogin() {
        this.lastLogin = LocalDateTime.now().toString();
    }

    /**
     * Override toString method to display employee information
     */
    @Override
    public String toString() {
        return "Employee: " + getName()
                + " | Position: " + position
                + " | Department: " + department
                + " | Salary: $" + salary;
    }

    /**
     * Get detailed employee information
     *
     * @return Detailed string with all employee data
     */
    public String getEmployeeDetails() {
        return "Employee ID: " + getId()
                + "\nName: " + getName()
                + "\nPosition: " + position
                + "\nDepartment: " + department
                + "\nSalary: $" + salary
                + "\nHire Date: " + hireDate
                + "\nStatus: " + status
                + "\nRole: " + role
                + "\nUsername: " + (username.isEmpty() ? "Not set" : username)
                + "\nAnnual Salary: $" + calculateAnnualSalary();
    }

    /**
     * Check if employee is administrator
     *
     * @return true if employee role is Administrator
     */
    public boolean isAdministrator() {
        return "Administrator".equalsIgnoreCase(this.role);
    }

    /**
     * Check if employee is officer
     *
     * @return true if employee role is Officer
     */
    public boolean isOfficer() {
        return "Officer".equalsIgnoreCase(this.role);
    }

    /**
     * Check if employee can manage system
     *
     * @return true if employee has administrative privileges
     */
    public boolean canManageSystem() {
        return isAdministrator() || "Manager".equalsIgnoreCase(this.role);
    }

    /**
     * Validate employee credentials
     *
     * @param inputUsername Username to validate
     * @param inputPassword Password to validate
     * @return true if credentials are valid
     */
    public boolean validateCredentials(String inputUsername, String inputPassword) {
        return this.username.equals(inputUsername)
                && this.password.equals(inputPassword)
                && this.isActive;
    }
}
