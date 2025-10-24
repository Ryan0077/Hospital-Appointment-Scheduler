package com.scheduler.ui;

import com.scheduler.exception.AppointmentException;
import com.scheduler.model.Appointment;
import com.scheduler.model.Doctor;
import com.scheduler.model.Hospital;
import com.scheduler.model.Patient;
import com.scheduler.util.DataManager;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * Main JFrame for the Hospital Appointment Scheduler application.
 * Manages all UI tabs and event handling.
 */
public class HPsched extends JFrame {

    // Core data management
    private final Hospital hospital;

    // UI Components
    private DefaultTableModel patientTableModel;
    private DefaultTableModel doctorTableModel;
    private DefaultTableModel appointmentTableModel;

    public HPsched(Hospital hospital) {
        this.hospital = hospital;

        setTitle("Hospital Appointment Scheduler");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Will be handled by WindowListener
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Patients", createPatientPanel());
        tabbedPane.addTab("Doctors", createDoctorPanel());
        tabbedPane.addTab("Appointments", createAppointmentPanel());

        add(tabbedPane);

        // Add a WindowListener to save data on exit
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                DataManager.saveData(hospital);
                System.out.println("Data saved. Exiting.");
                System.exit(0);
            }
        });

        // Initial table refresh on load
        refreshPatientTable();
        refreshDoctorTable();
        refreshAppointmentTable();
    }

    private JPanel createPatientPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] patientColumns = {"Name", "Age", "Gender", "Health Issue", "Emergency"};
        patientTableModel = new DefaultTableModel(patientColumns, 0);
        JTable patientTable = new JTable(patientTableModel);
        panel.add(new JScrollPane(patientTable), BorderLayout.CENTER);

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        JTextField nameField = new JTextField();
        JTextField ageField = new JTextField();
        JTextField genderField = new JTextField();
        JTextField issueField = new JTextField();
        JCheckBox emergencyBox = new JCheckBox("Is Emergency?");
        JButton addButton = new JButton("Add Patient");

        formPanel.add(new JLabel("Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Age:"));
        formPanel.add(ageField);
        formPanel.add(new JLabel("Gender:"));
        formPanel.add(genderField);
        formPanel.add(new JLabel("Health Issue:"));
        formPanel.add(issueField);
        formPanel.add(emergencyBox);
        formPanel.add(addButton);

        panel.add(formPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> {
            try {
                String name = nameField.getText();
                int age = Integer.parseInt(ageField.getText()); // Throws NumberFormatException
                String gender = genderField.getText();
                String issue = issueField.getText();
                boolean isEmergency = emergencyBox.isSelected();
                if (name.isEmpty() || gender.isEmpty() || issue.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "All fields must be filled out.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                hospital.addPatient(new Patient(name, age, gender, issue, isEmergency));
                refreshPatientTable();
                nameField.setText("");
                ageField.setText("");
                genderField.setText("");
                issueField.setText("");
                emergencyBox.setSelected(false);
            } catch (NumberFormatException ex) {
                // Catches the exception from Integer.parseInt()
                JOptionPane.showMessageDialog(this, "Please enter a valid age.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        return panel;
    }

    private JPanel createDoctorPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] doctorColumns = {"Name", "Specialization", "Available"};
        doctorTableModel = new DefaultTableModel(doctorColumns, 0);
        JTable doctorTable = new JTable(doctorTableModel);
        panel.add(new JScrollPane(doctorTable), BorderLayout.CENTER);

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        JTextField nameField = new JTextField();
        JTextField specField = new JTextField();
        JButton addButton = new JButton("Add Doctor");

        formPanel.add(new JLabel("Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Specialization:"));
        formPanel.add(specField);
        formPanel.add(new JLabel());
        formPanel.add(addButton);

        panel.add(formPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> {
            String name = nameField.getText();
            String spec = specField.getText();
            if (name.isEmpty() || spec.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields must be filled out.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            hospital.addDoctor(new Doctor(name, spec));
            refreshDoctorTable();
            nameField.setText("");
            specField.setText("");
        });
        return panel;
    }

    private JPanel createAppointmentPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] apptColumns = {"Patient", "Doctor", "Date/Time"};
        appointmentTableModel = new DefaultTableModel(apptColumns, 0);
        JTable appointmentTable = new JTable(appointmentTableModel);
        panel.add(new JScrollPane(appointmentTable), BorderLayout.CENTER);

        JPanel formsPanel = new JPanel(new GridLayout(1, 2, 20, 0));

        // Schedule Form
        JPanel schedulePanel = new JPanel(new GridLayout(5, 2, 5, 5));
        schedulePanel.setBorder(BorderFactory.createTitledBorder("Schedule/Reschedule Appointment"));
        JTextField patientNameField = new JTextField();
        JTextField doctorNameField = new JTextField();
        JPanel dateTimePanel = new JPanel(new BorderLayout(5, 0));
        JTextField dateTimeField = new JTextField("Please select a date and time");
        dateTimeField.setEditable(false);
        JButton selectDateButton = new JButton("Select...");
        dateTimePanel.add(dateTimeField, BorderLayout.CENTER);
        dateTimePanel.add(selectDateButton, BorderLayout.EAST);
        JButton scheduleButton = new JButton("Schedule");

        schedulePanel.add(new JLabel("Patient Name:"));
        schedulePanel.add(patientNameField);
        schedulePanel.add(new JLabel("Doctor Name:"));
        schedulePanel.add(doctorNameField);
        schedulePanel.add(new JLabel("Date/Time:"));
        schedulePanel.add(dateTimePanel);
        schedulePanel.add(new JLabel());
        schedulePanel.add(scheduleButton);

        // --- NEW: Multithreading Demo Button ---
        JButton reportButton = new JButton("Generate Report");
        schedulePanel.add(new JLabel("Run Task:"));
        schedulePanel.add(reportButton);
        formsPanel.add(schedulePanel);

        // Cancel Form
        JPanel cancelPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        cancelPanel.setBorder(BorderFactory.createTitledBorder("Cancel Appointment"));
        JTextField cancelPatientField = new JTextField();
        JTextField cancelDoctorField = new JTextField();
        JButton cancelButton = new JButton("Cancel Appointment");
        cancelPanel.add(new JLabel("Patient Name:"));
        cancelPanel.add(cancelPatientField);
        cancelPanel.add(new JLabel("Doctor Name:"));
        cancelPanel.add(cancelDoctorField);
        cancelPanel.add(new JLabel());
        cancelPanel.add(new JLabel());
        cancelPanel.add(new JLabel());
        cancelPanel.add(new JLabel());
        cancelPanel.add(new JLabel());
        cancelPanel.add(cancelButton);
        formsPanel.add(cancelPanel);

        panel.add(formsPanel, BorderLayout.SOUTH);

        // --- Action Listeners ---
        final AtomicReference<LocalDateTime> selectedDateTime = new AtomicReference<>();
        selectDateButton.addActionListener(e -> {
            DateTimePicker picker = new DateTimePicker(this);
            Optional<LocalDateTime> result = picker.selectDateTime();
            result.ifPresent(ldt -> {
                selectedDateTime.set(ldt);
                dateTimeField.setText(ldt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            });
        });

        scheduleButton.addActionListener(e -> {
            try {
                String patientName = patientNameField.getText();
                String doctorName = doctorNameField.getText();
                LocalDateTime dateTime = selectedDateTime.get();

                if (dateTime == null) {
                    JOptionPane.showMessageDialog(this, "Please select a valid date and time.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // This method now throws AppointmentException
                String result = hospital.scheduleAppointment(patientName, doctorName, dateTime);

                JOptionPane.showMessageDialog(this, result);
                refreshAppointmentTable();
                patientNameField.setText("");
                doctorNameField.setText("");
                dateTimeField.setText("Please select a date and time");
                selectedDateTime.set(null);

            } catch (AppointmentException ex) {
                // Catch the custom exception
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Scheduling Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> {
            String result = hospital.cancelAppointment(cancelPatientField.getText(), cancelDoctorField.getText());
            JOptionPane.showMessageDialog(this, result);
            refreshAppointmentTable();
            cancelPatientField.setText("");
            cancelDoctorField.setText("");
        });

        // --- Multithreading (Module 4.1) Action Listener ---
        reportButton.addActionListener(e -> {
            reportButton.setEnabled(false);
            reportButton.setText("Generating...");

            // Create a new thread to run the "long" task
            new Thread(() -> {
                try {
                    // Simulate a long task (e.g., 3 seconds)
                    Thread.sleep(3000);

                    int patientCount = hospital.getAllPatients().size();
                    int apptCount = hospital.getAllAppointments().size();
                    String report = "Report Generated:\n- Total Patients: " + patientCount + "\n- Total Appointments: " + apptCount;

                    // When done, update the GUI *back on the EDT*
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(this, report, "System Report", JOptionPane.INFORMATION_MESSAGE);
                        reportButton.setEnabled(true);
                        reportButton.setText("Generate Report");
                    });
                } catch (InterruptedException ex) {
                    // Handle thread interruption
                    SwingUtilities.invokeLater(() -> {
                        reportButton.setEnabled(true);
                        reportButton.setText("Generate Report");
                    });
                }
            }).start(); // Start the new thread
        });

        return panel;
    }

    private void refreshPatientTable() {
        patientTableModel.setRowCount(0);
        for (Patient p : hospital.getAllPatients()) {
            patientTableModel.addRow(new Object[]{p.getName(), p.getAge(), p.getGender(), p.getHealthIssue(), p.isEmergency() ? "Yes" : "No"});
        }
    }

    private void refreshDoctorTable() {
        doctorTableModel.setRowCount(0);
        for (Doctor d : hospital.getAllDoctors()) {
            doctorTableModel.addRow(new Object[]{d.getName(), d.getSpecialization(), d.isAvailable() ? "Yes" : "No"});
        }
    }

    private void refreshAppointmentTable() {
        appointmentTableModel.setRowCount(0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        for (Appointment a : hospital.getAllAppointments()) {
            appointmentTableModel.addRow(new Object[]{a.getPatient().getName(), a.getDoctor().getName(), a.getAppointmentDateTime().format(formatter)});
        }
    }
}