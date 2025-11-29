package model;

import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Prison Management System - Complete Test ===\n");
        
        // Test Prisoner
        Prisoner prisoner1 = new Prisoner("P001", "Ahmed Mohamed", "0912345678", 
                                         "Theft", "Cell-105", "5 years");
        
        // Test Employee
        Employee officer1 = new Employee("E001", "John Smith", "0911223344",
                                       "Prison Officer", "Security", 2500.00);
        
        // Test Visitor
        Visitor visitor1 = new Visitor("V001", "Sarah Johnson", "0912334455",
                                      "Wife", "P001");
        
        // Test Visit
        Visit visit1 = new Visit("VIS001", "P001", "V001", 
                                LocalDateTime.now().plusDays(1), 60);
        
        // Display all information
        System.out.println("=== PRISONER ===");
        System.out.println(prisoner1.getPrisonerDetails());
        
        System.out.println("\n=== EMPLOYEE ===");
        System.out.println(officer1.getEmployeeDetails());
        
        System.out.println("\n=== VISITOR ===");
        System.out.println(visitor1.getVisitorDetails());
        
        System.out.println("\n=== VISIT ===");
        System.out.println(visit1.getVisitDetails());
        
        // Test interactions
        System.out.println("\n=== INTERACTIONS ===");
        visitor1.approveVisitor();
        visit1.startVisit();
        System.out.println("Visitor approved: " + visitor1.isApproved());
        System.out.println("Visit status: " + visit1.getStatus());
    }
}
