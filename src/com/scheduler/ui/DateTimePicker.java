package com.scheduler.ui;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.Optional;

/**
 * A custom JDialog for picking a date and time.
 * This class is part of the 'ui' package.
 */
public class DateTimePicker {
    private final JDialog dialog;
    private final JComboBox<Integer> yearCombo = new JComboBox<>();
    private final JComboBox<String> monthCombo = new JComboBox<>();
    private final JComboBox<Integer> dayCombo = new JComboBox<>();
    private final JComboBox<Integer> hourCombo = new JComboBox<>();
    private final JComboBox<Integer> minuteCombo = new JComboBox<>();
    private Optional<LocalDateTime> selectedDateTime = Optional.empty();

    public DateTimePicker(JFrame parent) {
        dialog = new JDialog(parent, "Select Date and Time", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 150);

        // Populate combos
        int currentYear = Year.now().getValue();
        for (int i = currentYear; i < currentYear + 10; i++) yearCombo.addItem(i);
        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        for (String month : months) monthCombo.addItem(month);
        for (int i = 0; i < 24; i++) hourCombo.addItem(i);
        minuteCombo.addItem(0);
        minuteCombo.addItem(15);
        minuteCombo.addItem(30);
        minuteCombo.addItem(45);

        updateDayComboBox();

        // Add listeners to update days when month/year changes
        yearCombo.addActionListener(e -> updateDayComboBox());
        monthCombo.addActionListener(e -> updateDayComboBox());

        // Layout
        JPanel pickerPanel = new JPanel(new FlowLayout());
        pickerPanel.add(new JLabel("Year:"));
        pickerPanel.add(yearCombo);
        pickerPanel.add(new JLabel("Month:"));
        pickerPanel.add(monthCombo);
        pickerPanel.add(new JLabel("Day:"));
        pickerPanel.add(dayCombo);
        pickerPanel.add(new JLabel("Time:"));
        pickerPanel.add(hourCombo);
        pickerPanel.add(new JLabel(":"));
        pickerPanel.add(minuteCombo);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        dialog.add(pickerPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        okButton.addActionListener(e -> {
            try {
                int year = (int) yearCombo.getSelectedItem();
                int month = monthCombo.getSelectedIndex() + 1;
                int day = (int) dayCombo.getSelectedItem();
                int hour = (int) hourCombo.getSelectedItem();
                int minute = (int) minuteCombo.getSelectedItem();
                selectedDateTime = Optional.of(LocalDateTime.of(year, month, day, hour, minute));
                dialog.dispose();
            } catch (Exception ex) {
                // Handle case where day might be null
            }
        });
        cancelButton.addActionListener(e -> dialog.dispose());
        dialog.setLocationRelativeTo(parent);
    }

    private void updateDayComboBox() {
        int year = (int) yearCombo.getSelectedItem();
        int month = monthCombo.getSelectedIndex() + 1;
        LocalDate date = LocalDate.of(year, month, 1);
        int daysInMonth = date.lengthOfMonth();

        dayCombo.removeAllItems();
        for (int i = 1; i <= daysInMonth; i++) {
            dayCombo.addItem(i);
        }
    }

    public Optional<LocalDateTime> selectDateTime() {
        dialog.setVisible(true);
        return selectedDateTime;
    }
}