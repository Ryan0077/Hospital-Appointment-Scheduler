package com.scheduler.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents an appointment.
 * Implements Serializable for File I/O (Module 3.2).
 */
public class Appointment implements Serializable {
    private final Patient patient;
    private final Doctor doctor;
    private LocalDateTime appointmentDateTime;

    public Appointment(Patient patient, Doctor doctor, LocalDateTime appointmentDateTime) {
        this.patient = patient;
        this.doctor = doctor;
        this.appointmentDateTime = appointmentDateTime;
    }

    public Patient getPatient() { return patient; }
    public Doctor getDoctor() { return doctor; }
    public LocalDateTime getAppointmentDateTime() { return appointmentDateTime; }
    public void setAppointmentDateTime(LocalDateTime appointmentDateTime) { this.appointmentDateTime = appointmentDateTime; }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return "Appointment [Patient: " + patient.getName() + ", Doctor: " + doctor.getName() +
                ", Date/Time: " + appointmentDateTime.format(formatter) + "]";
    }
}