package com.scheduler.model;

import java.io.Serializable;

/**
 * Abstract base class for Patient and Doctor.
 * Demonstrates inheritance (Module 2).
 * Implements Serializable for File I/O (Module 3.2).
 */
public abstract class Person implements Serializable {

    // Use 'protected' so subclasses can access it
    protected String name;

    public Person(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Person person = (Person) obj;
        return name.equalsIgnoreCase(person.name);
    }

    @Override
    public int hashCode() {
        return name.toLowerCase().hashCode();
    }
}