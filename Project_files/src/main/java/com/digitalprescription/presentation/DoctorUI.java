package com.digitalprescription.presentation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import com.digitalprescription.business.model.Prescription;
import com.digitalprescription.business.service.PrescriptionService;

public class DoctorUI extends JFrame {
    private PrescriptionService service = new PrescriptionService();
    private String doctorName;
    private JTextField patientIdField, medicationField, dosageField, diagnosisField, instructionsField;
    private JTable table;
    private DefaultTableModel model;

    public DoctorUI(String doctorName) {
        this.doctorName = doctorName;
        initUI();
    }

    private void initUI() {
        setTitle("👨‍⚕️ Dr. " + doctorName + " - Digital Prescription");
        setSize(1000, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header
        JLabel header = new JLabel("Create New Prescription", SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 20));
        header.setForeground(new Color(59, 130, 246));
        add(header, BorderLayout.NORTH);

        // Patient ID
        JPanel patientPanel = new JPanel();
        patientPanel.add(new JLabel("Patient ID: "));
        patientIdField = new JTextField("1", 8);
        patientPanel.add(patientIdField);
        add(patientPanel, BorderLayout.NORTH);

        // Complete Prescription Form
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("💊 Prescription Details"));

        formPanel.add(new JLabel("Medication: *"));
        medicationField = new JTextField();
        formPanel.add(medicationField);

        formPanel.add(new JLabel("Dosage: *"));
        dosageField = new JTextField();
        formPanel.add(dosageField);

        formPanel.add(new JLabel("Diagnosis:"));
        diagnosisField = new JTextField();
        formPanel.add(diagnosisField);

        formPanel.add(new JLabel("Special Instructions:"));
        instructionsField = new JTextField();
        formPanel.add(instructionsField);

        JButton createBtn = new JButton("💉 CREATE & SAVE TO MONGODB");
        createBtn.setBackground(new Color(34, 197, 94));
        createBtn.setForeground(Color.WHITE);
        createBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        createBtn.addActionListener(new CreateAction());
        formPanel.add(createBtn);

        add(formPanel, BorderLayout.WEST);

        // History Table
        String[] columns = {"ID", "Medication", "Dosage", "Diagnosis", "Instructions", "Date"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        table.setPreferredScrollableViewportSize(new Dimension(600, 400));
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel();
        JButton refreshBtn = new JButton("🔄 Refresh Patient History");
        refreshBtn.addActionListener(new RefreshAction());
        buttonPanel.add(refreshBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private class CreateAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (medicationField.getText().trim().isEmpty() || dosageField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(DoctorUI.this, "❌ Medication and Dosage are required!");
                return;
            }

            Prescription p = new Prescription();
            p.setPatientId(Integer.parseInt(patientIdField.getText()));
            p.setDoctorId(1); // Current logged-in doctor
            p.setMedication(medicationField.getText());
            p.setDosage(dosageField.getText());
            p.setDiagnosis(diagnosisField.getText());
            p.setInstructions(instructionsField.getText());

            if (service.createPrescription(p)) {
                JOptionPane.showMessageDialog(DoctorUI.this, 
                    "✅ Prescription SAVED to MongoDB!\nPatient ID: " + p.getPatientId());
                clearForm();
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(DoctorUI.this, "❌ Failed to save prescription!");
            }
        }
    }

    private class RefreshAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            refreshTable();
        }
    }

    private void refreshTable() {
        model.setRowCount(0);
        try {
            int patientId = Integer.parseInt(patientIdField.getText());
            List<Prescription> prescriptions = service.getPrescriptionsForPatient(patientId);
            for (Prescription p : prescriptions) {
                model.addRow(new Object[]{
                    "N/A", p.getMedication(), p.getDosage(),
                    p.getDiagnosis(), p.getInstructions(), p.getDate()
                });
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "❌ Enter valid Patient ID!");
        }
    }

    private void clearForm() {
        medicationField.setText("");
        dosageField.setText("");
        diagnosisField.setText("");
        instructionsField.setText("");
    }
}
