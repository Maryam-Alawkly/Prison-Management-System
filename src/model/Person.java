package model;

/**
 * Person abstract class serves as a base class for all person entities in the
 * prison management system. This includes prisoners, employees, and visitors.
 * The class provides common properties and methods that are shared by all
 * person types.
 */
public abstract class Person {

    /**
     * Unique identifier for the person. This ID should be unique across all
     * person entities in the system.
     */
    private String id;

    /**
     * Full name of the person. Includes first name and last name.
     */
    private String name;

    /**
     * Contact phone number for the person. Used for emergency contacts and
     * communication purposes.
     */
    private String phone;

    /**
     * Constructs a new Person with the specified identification and contact
     * information.
     *
     * @param id Unique identifier for the person
     * @param name Full name of the person
     * @param phone Contact phone number
     */
    public Person(String id, String name, String phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;
    }

    /**
     * Gets the unique identifier of the person.
     *
     * @return The person's ID
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the full name of the person.
     *
     * @return The person's name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the contact phone number of the person.
     *
     * @return The phone number
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Sets the unique identifier of the person. Note: Changing the ID may
     * affect system references to this person.
     *
     * @param id The new ID to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Sets the full name of the person.
     *
     * @param name The new name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the contact phone number of the person.
     *
     * @param phone The new phone number to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Returns a string representation of the person. The format is "Person:
     * [name] (ID: [id])"
     *
     * @return String representation of the person
     */
    @Override
    public String toString() {
        return "Person: " + name + " (ID: " + id + ")";
    }
}
