package com.scheduler.util;

import com.scheduler.model.Hospital;
import java.io.*;

/**
 * Manages saving and loading of hospital data.
 * Demonstrates File I/O (Module 3.2).
 */
public class DataManager {

    private static final String FILENAME = "hospital.dat";

    /**
     * Saves the entire Hospital object to a file.
     */
    public static void saveData(Hospital hospital) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILENAME))) {
            oos.writeObject(hospital);
        } catch (IOException e) {
            System.err.println("Error saving data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Loads the Hospital object from a file.
     * If no file exists, returns a new Hospital object.
     */
    public static Hospital loadData() {
        File dataFile = new File(FILENAME);
        if (dataFile.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILENAME))) {
                return (Hospital) ois.readObject();
            } catch (FileNotFoundException e) {
                // Should not happen due to dataFile.exists() check, but good practice
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error loading data, creating new file: " + e.getMessage());
            }
        }
        // If file not found or is corrupt, return a new hospital
        return new Hospital();
    }
}