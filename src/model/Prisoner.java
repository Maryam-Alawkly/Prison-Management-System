package model;

/**
 * Prisoner class represents an inmate in the prison system
 * Inherits from Person class and adds prisoner-specific properties
 */
public class Prisoner extends Person {
    private String crime;
    private String cellNumber;
    private String sentenceDuration;
    private String status; // Possible values: "In Custody", "Released", "Transferred"
    
    /**
     * Constructor for Prisoner class
     * @param id Prisoner ID
     * @param name Prisoner name
     * @param phone Contact phone
     * @param crime Crime committed
     * @param cellNumber Cell assignment
     * @param sentenceDuration Sentence length
     */
    public Prisoner(String id, String name, String phone, 
                   String crime, String cellNumber, String sentenceDuration) {
        super(id, name, phone); // Call parent class constructor
        this.crime = crime;
        this.cellNumber = cellNumber;
        this.sentenceDuration = sentenceDuration;
        this.status = "In Custody"; // Default status
    }
    
    // Getter and Setter methods
    public String getCrime() { 
        return crime; 
    }
    
    public void setCrime(String crime) { 
        this.crime = crime; 
    }
    
    public String getCellNumber() { 
        return cellNumber; 
    }
    
    public void setCellNumber(String cellNumber) { 
        this.cellNumber = cellNumber; 
    }
    
    public String getSentenceDuration() { 
        return sentenceDuration; 
    }
    
    public void setSentenceDuration(String sentenceDuration) { 
        this.sentenceDuration = sentenceDuration; 
    }
    
    public String getStatus() { 
        return status; 
    }
    
    public void setStatus(String status) { 
        this.status = status; 
    }
    
    /**
     * Override toString method to display prisoner information
     * @return Formatted string with prisoner details
     */
    @Override
    public String toString() {
        return "Prisoner: " + getName() + 
               " | Crime: " + crime + 
               " | Cell: " + cellNumber + 
               " | Status: " + status;
    }
    
    /**
     * Get detailed prisoner information
     * @return Detailed string with all prisoner data
     */
    public String getPrisonerDetails() {
        return "ID: " + getId() + 
               "\nName: " + getName() + 
               "\nCrime: " + crime + 
               "\nCell: " + cellNumber + 
               "\nSentence: " + sentenceDuration + 
               "\nStatus: " + status;
    }
    
    /**
     * Method to release prisoner
     * Changes status to "Released"
     */
    public void releasePrisoner() {
        this.status = "Released";
    }
    
    /**
     * Method to transfer prisoner
     * Changes status to "Transferred"
     */
    public void transferPrisoner() {
        this.status = "Transferred";
    }
}