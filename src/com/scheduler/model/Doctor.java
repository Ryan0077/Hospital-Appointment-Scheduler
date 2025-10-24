package com.scheduler.model;

/**
 * Represents a doctor. Extends Person (Module 2).
 * Implements Serializable (part of Person) (Module 3.2).
 */
public class Doctor extends Person {
    private final String specialization;
    private boolean isAvailable;

    public Doctor(String name, String specialization) {
        super(name);
        this.specialization = specialization;
        this.isAvailable = true;
    }

    public String getSpecialization() { return specialization; }
    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean isAvailable) { this.isAvailable = isAvailable; }

    @Override
    public String toString() {
        return "Doctor [Name: " + name + ", Specialization: " + specialization + "]";
    }
}