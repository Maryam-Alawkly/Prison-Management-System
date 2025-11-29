package model;

/**
 * Employee class represents a staff member in the prison system
 * Inherits from Person class and adds employee-specific properties
 */
public class Employee extends Person {
    private String position;
    private String department;
    private double salary;
    private String hireDate;
    private String status; // "Active", "Inactive", "Suspended"
    
    /**
     * Constructor for Employee class
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
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    /**
     * Calculate annual salary
     * @return Annual salary (monthly salary * 12)
     */
    public double calculateAnnualSalary() {
        return this.salary * 12;
    }
    
    /**
     * Promote employee to new position with salary increase
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
    }
    
    /**
     * Override toString method to display employee information
     */
    @Override
    public String toString() {
        return "Employee: " + getName() + 
               " | Position: " + position + 
               " | Department: " + department + 
               " | Salary: $" + salary;
    }
    
    /**
     * Get detailed employee information
     * @return Detailed string with all employee data
     */
    public String getEmployeeDetails() {
        return "Employee ID: " + getId() +
               "\nName: " + getName() +
               "\nPosition: " + position +
               "\nDepartment: " + department +
               "\nSalary: $" + salary +
               "\nHire Date: " + hireDate +
               "\nStatus: " + status +
               "\nAnnual Salary: $" + calculateAnnualSalary();
    }
    
    /**
     * Check if employee is active
     * @return true if employee status is "Active"
     */
    public boolean isActive() {
        return "Active".equals(this.status);
    }
}



