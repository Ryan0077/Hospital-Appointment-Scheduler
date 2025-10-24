## **Java Hospital Appointment Scheduler**

A comprehensive, multi-tabbed desktop program for managing hospital appointments, built with Java Swing.
It's a full-featured academic project demonstrating OOP, File I/O for persistence, and multithreading.

## **Application Preview**


https://github.com/user-attachments/assets/808c506d-d822-4661-8708-8eb82decd0db


The application features a clean, functional, multi-tabbed interface:

Main Window: A JTabbedPane separates the main entities: Patients, Doctors, and Appointments.

Data-Driven Tables: Each tab uses a JTable to display all current data (patient lists, doctor rosters, and the appointment schedule).

Simple Forms: Clean input forms at the bottom of each panel allow users to add new data.

Custom Date/Time Picker: A modal JDialog pops up to provide a user-friendly way to select a specific date and time for scheduling.

## **Core Features**


Full CRUD Functionality: Add new patients and doctors. Schedule and cancel appointments.

Data Persistence (File I/O): The application automatically saves all patient, doctor, and appointment data to a hospital.dat file upon closing. This data is fully reloaded on the next launch.

Smart Scheduling Logic: The system prevents double-booking a doctor for the same time slot.

Emergency Prioritization: If an emergency patient is scheduled, the system will automatically "bump" any existing non-emergency appointment for that doctor by 15 minutes.

Concurrent Task Handling (Multithreading): A "Generate Report" button on the appointments tab runs its task on a new thread, ensuring the UI remains responsive and does not freeze.

Robust Error Handling: The application uses both built-in (NumberFormatException for age input) and custom (AppointmentException for scheduling conflicts) exceptions to handle errors gracefully.

## **Design and Implementation**


Use of OOP Concepts: Strong application of OOP principles, including an abstract class Person extended by Patient and Doctor (Inheritance), private fields with public getters (Encapsulation), and all data model classes implementing Serializable.

GUI Design & Event Handling: A user-friendly GUI built with Java Swing (JTabbedPane, JTable, JPanel, etc.). Event handling is implemented via ActionListener for buttons and a WindowAdapter for saving data on exit.

Use of Package Concept: The project follows a modular design with code organized into distinct packages (com.scheduler.model, com.scheduler.ui, com.scheduler.main, etc.) for high cohesion and low coupling.

Functionality & Output: The application is fully functional and demonstrates advanced Java concepts:

File I/O: Uses ObjectOutputStream and ObjectInputStream to serialize the entire Hospital object.

Exception Handling: Throws a custom AppointmentException for scheduling errors, which is caught by the UI to display a user-friendly JOptionPane.

Multithreading: Creates a new Thread for background tasks and uses SwingUtilities.invokeLater to safely update the UI.

## **Project Structure**

<img width="690" height="495" alt="image" src="https://github.com/user-attachments/assets/6ba068fb-ba66-4747-9508-62bad9ea2a60" />


## Quick Setup Guide

This project has no external dependencies and runs on the standard Java Development Kit.

**Prerequisites:**
* JDK 11 or later.
* IntelliJ IDEA (or another Java IDE).
* Git (for cloning).

**Clone the Repository:**
1.  In IntelliJ, go to `File` > `New` > `Project from Version Control...`.
2.  Paste the repository URL: `[PASTE YOUR REPO URL HERE]`

**Configure Project:**
1.  **Set JDK:** After opening, IntelliJ may ask you to configure the SDK. If not, go to `File` > `Project Structure` > `Project` > `SDK` and select your installed JDK.
2.  **No Libraries Needed:** This project uses only core Java libraries, so no external `.jar` files or `lib` folder are required.

**Run:**
1.  `Build` > `Rebuild Project` (or wait for IntelliJ to index).
2.  Navigate to `src/com/scheduler/main/Main.java`.
3.  Click the green play icon  next to the `main` method and select "Run 'Main.main()'".
