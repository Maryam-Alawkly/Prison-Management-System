package model;

public abstract class Person {
    private String id;
    private String name;
    private String phone;
    
    // Constructor
    public Person(String id, String name, String phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;
    }
    
    // Getters
    public String getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public String getPhone() {
        return phone;
    }
    
    // Setters
    public void setId(String id) {
        this.id = id;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    @Override
    public String toString() {
        return "Person: " + name + " (ID: " + id + ")";
    }
}