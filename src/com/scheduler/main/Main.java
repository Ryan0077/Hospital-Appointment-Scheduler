package com.scheduler.main;

import com.scheduler.model.Hospital;
import com.scheduler.ui.HPsched;
import com.scheduler.util.DataManager;
import javax.swing.SwingUtilities;

/**
 * Main entry point for the Hospital Appointment Scheduler.
 * Loads data and launches the Swing GUI.
 */
public class Main {

    public static void main(String[] args) {
        // Load the hospital data from file (or create a new one)
        final Hospital hospital = DataManager.loadData();

        // Run the GUI on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            HPsched frame = new HPsched(hospital);
            frame.setVisible(true);
        });
    }
}