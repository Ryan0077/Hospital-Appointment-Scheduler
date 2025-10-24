package com.scheduler.model;

import com.scheduler.exception.AppointmentException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Manages all core data and logic.
 * Implements Serializable for File I/O (Module 3.2).
 */
public class Hospital implements Serializable {

    // 'final' lists are still serializable, as long as the list *type* (ArrayList) is.
    private final List<Patient> patients = new ArrayList<>();
    private final List<Doctor> doctors = new ArrayList<>();
    private final List<Appointment> appointments = new ArrayList<>();

    public void addPatient(Patient patient) { patients.add(patient); }
    public List<Patient> getAllPatients() { return new ArrayList<>(patients); }
    public Optional<Patient> findPatientByName(String name) {
        return patients.stream().filter(p -> p.getName().equalsIgnoreCase(name)).findFirst();
    }

    public void addDoctor(Doctor doctor) { doctors.add(doctor); }
    public List<Doctor> getAllDoctors() { return new ArrayList<>(doctors); }
    public Optional<Doctor> findDoctorByName(String name) {
        return doctors.stream().filter(d -> d.getName().equalsIgnoreCase(name)).findFirst();
    }

    // This method now throws a custom exception
    public String scheduleAppointment(String patientName, String doctorName, LocalDateTime dateTime) throws AppointmentException {

        Optional<Patient> patientOpt = findPatientByName(patientName);
        Optional<Doctor> doctorOpt = findDoctorByName(doctorName);

        if (patientOpt.isEmpty()) {
            throw new AppointmentException("Patient not found: " + patientName);
        }
        if (doctorOpt.isEmpty()) {
            throw new AppointmentException("Doctor not found: " + doctorName);
        }

        Patient patient = patientOpt.get();
        Doctor doctor = doctorOpt.get();

        if (!doctor.isAvailable()) {
            throw new AppointmentException("Sorry, Dr. " + doctor.getName() + " is not available.");
        }

        boolean isDoubleBooked = appointments.stream()
                .anyMatch(a -> a.getDoctor().equals(doctor) && a.getAppointmentDateTime().equals(dateTime));

        if (isDoubleBooked && !patient.isEmergency()) {
            throw new AppointmentException("Error: Dr. " + doctor.getName() + " already has an appointment at this time.");
        }

        if (patient.isEmergency()) {
            handleEmergencyAppointment(patient, doctor, dateTime);
        } else {
            appointments.add(new Appointment(patient, doctor, dateTime));
        }
        return "Appointment scheduled for " + patient.getName() + " with Dr. " + doctor.getName() + ".";
    }

    private void handleEmergencyAppointment(Patient emergencyPatient, Doctor doctor, LocalDateTime dateTime) {
        appointments.stream()
                .filter(a -> a.getDoctor().equals(doctor) && a.getAppointmentDateTime().equals(dateTime) && !a.getPatient().isEmergency())
                .findFirst()
                .ifPresent(oldAppointment -> {
                    // Reschedule the non-emergency patient's appointment by 15 minutes
                    LocalDateTime newDateTime = oldAppointment.getAppointmentDateTime().plusMinutes(15);
                    oldAppointment.setAppointmentDateTime(newDateTime);
                });
        // Schedule the new emergency appointment
        appointments.add(new Appointment(emergencyPatient, doctor, dateTime));
    }

    public String cancelAppointment(String patientName, String doctorName) {
        Optional<Appointment> appointmentToCancel = appointments.stream()
                .filter(a -> a.getPatient().getName().equalsIgnoreCase(patientName) && a.getDoctor().getName().equalsIgnoreCase(doctorName))
                .findFirst();
        if (appointmentToCancel.isPresent()) {
            appointments.remove(appointmentToCancel.get());
            return "Successfully canceled the appointment.";
        }
        return "Could not find the specified appointment to cancel.";
    }

    public List<Appointment> getAllAppointments() {
        appointments.sort(Comparator.comparing(Appointment::getAppointmentDateTime));
        return new ArrayList<>(appointments);
    }
}