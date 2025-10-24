package com.scheduler.model;

/**
 * Represents a patient. Extends Person (Module 2).
 * Implements Serializable (part of Person) (Module 3.2).
 */
public class Patient extends Person {
    private final int age;
    private final String gender;
    private final String healthIssue;
    private final boolean isEmergency;

    public Patient(String name, int age, String gender, String healthIssue, boolean isEmergency) {
        super(name);
        this.age = age;
        this.gender = gender;
        this.healthIssue = healthIssue;
        this.isEmergency = isEmergency;
    }

    public int getAge() { return age; }
    public String getGender() { return gender; }
    public String getHealthIssue() { return healthIssue; }
    public boolean isEmergency() { return isEmergency; }

    @Override
    public String toString() {
        return "Patient [Name: " + name + ", Age: " + age + ", Emergency: " + (isEmergency ? "Yes" : "No") + "]";
    }
}