package model;

import java.time.LocalDate;

/**
 * Prisoner class represents an inmate in the prison management system.
 * This class extends the Person class and adds prisoner-specific properties
 * such as crime details, cell assignment, sentence information, and status.
 */
public class Prisoner extends Person {
    
    /**
     * Crime committed by the prisoner. This describes the offense
     * for which the prisoner is incarcerated.
     */
    private String crime;
    
    /**
     * Cell number where the prisoner is currently assigned.
     * Represents the physical location within the prison facility.
     */
    private String cellNumber;
    
    /**
     * Duration of the prisoner's sentence. This can be in various
     * formats such as "5 years", "Life", or "10-15 years".
     */
    private String sentenceDuration;
    
    /**
     * Current status of the prisoner. Possible values include:
     * "In Custody", "Released", "Transferred", or other statuses
     * defined by the prison system.
     */
    private String status;
    
    /**
     * Constructs a new Prisoner with the specified information.
     * Initializes the prisoner with default status "In Custody".
     *
     * @param id               Unique prisoner identifier
     * @param name             Full name of the prisoner
     * @param phone            Contact phone number
     * @param crime            Crime committed by the prisoner
     * @param cellNumber       Cell assignment within the prison
     * @param sentenceDuration Duration of the sentence
     */
    public Prisoner(String id, String name, String phone, 
                   String crime, String cellNumber, String sentenceDuration) {
        super(id, name, phone); // Call parent class constructor
        this.crime = crime;
        this.cellNumber = cellNumber;
        this.sentenceDuration = sentenceDuration;
        this.status = "In Custody"; // Default status
    }
    
    /**
     * Gets the crime committed by the prisoner.
     *
     * @return The crime description
     */
    public String getCrime() { 
        return crime; 
    }
    
    /**
     * Sets the crime committed by the prisoner.
     *
     * @param crime The crime description to set
     */
    public void setCrime(String crime) { 
        this.crime = crime; 
    }
    
    /**
     * Gets the cell number where the prisoner is assigned.
     *
     * @return The cell number
     */
    public String getPrisonCellNumber() { 
        return cellNumber; 
    }
    
    /**
     * Sets the cell number for the prisoner's assignment.
     *
     * @param cellNumber The cell number to set
     */
    public void setPrisonCellNumber(String cellNumber) { 
        this.cellNumber = cellNumber; 
    }
    
    /**
     * Gets the duration of the prisoner's sentence.
     *
     * @return The sentence duration
     */
    public String getSentenceDuration() { 
        return sentenceDuration; 
    }
    
    /**
     * Sets the duration of the prisoner's sentence.
     *
     * @param sentenceDuration The sentence duration to set
     */
    public void setSentenceDuration(String sentenceDuration) { 
        this.sentenceDuration = sentenceDuration; 
    }
    
    /**
     * Gets the current status of the prisoner.
     *
     * @return The prisoner status
     */
    public String getStatus() { 
        return status; 
    }
    
    /**
     * Sets the current status of the prisoner.
     *
     * @param status The status to set
     */
    public void setStatus(String status) { 
        this.status = status; 
    }
    
    /**
     * Returns a string representation of the prisoner.
     * The format is "Prisoner: [name] | Crime: [crime] | PrisonCell: [cell] | Status: [status]"
     *
     * @return String representation of the prisoner
     */
    @Override
    public String toString() {
        return "Prisoner: " + getName() + 
               " | Crime: " + crime + 
               " | PrisonCell: " + cellNumber + 
               " | Status: " + status;
    }
    
    /**
     * Gets detailed information about the prisoner.
     * Returns a formatted string with all prisoner data.
     *
     * @return Detailed prisoner information
     */
    public String getPrisonerDetails() {
        return "ID: " + getId() + 
               "\nName: " + getName() + 
               "\nCrime: " + crime + 
               "\nPrisonCell: " + cellNumber + 
               "\nSentence: " + sentenceDuration + 
               "\nStatus: " + status;
    }
    
    /**
     * Releases the prisoner from custody.
     * Changes the prisoner's status to "Released".
     */
    public void releasePrisoner() {
        this.status = "Released";
    }
    
    /**
     * Transfers the prisoner to another facility.
     * Changes the prisoner's status to "Transferred".
     */
    public void transferPrisoner() {
        this.status = "Transferred";
    }
    
    /**
     * Sets the admission date for the prisoner.
     * This method is currently not implemented.
     *
     * @param value The admission date to set
     * @throws UnsupportedOperationException Always thrown as method is not yet implemented
     */
    public void setAdmissionDate(LocalDate value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}