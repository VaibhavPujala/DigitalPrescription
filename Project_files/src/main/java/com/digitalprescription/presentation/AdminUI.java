package com.digitalprescription.presentation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;  // ✅ FIXED: Import added
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class AdminUI extends JFrame {
    private JTable usersTable;
    private DefaultTableModel tableModel;
    
    public AdminUI() {
        initUI();
    }
    
    private void initUI() {
        setTitle("Digital Prescription - Admin Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 750);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(248, 250, 252));
        
        createHeader();
        createActionButtons();
        createUsersTable();
        loadUsers();
        createLogoutButton();
    }
    
    private void createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(59, 130, 246));
        header.setPreferredSize(new Dimension(1100, 80));
        header.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        
        JLabel title = new JLabel("Admin Dashboard", SwingConstants.LEFT);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        
        JLabel adminLabel = new JLabel("👨‍💼 Admin Panel", SwingConstants.RIGHT);
        adminLabel.setForeground(Color.WHITE);
        adminLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        
        header.add(title, BorderLayout.WEST);
        header.add(adminLabel, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);
    }
    
    private void createActionButtons() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(new Color(248, 250, 252));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        
        JButton addUserBtn = new JButton("➕ Add User");
        addUserBtn.setBackground(new Color(34, 197, 94));
        addUserBtn.setForeground(Color.WHITE);
        addUserBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        addUserBtn.setFocusPainted(false);
        addUserBtn.setBorderPainted(false);
        addUserBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addUserBtn.addActionListener(e -> addUser());  // ✅ Button connected
        
        JButton deleteUserBtn = new JButton("🗑️ Delete User");
        deleteUserBtn.setBackground(new Color(239, 68, 68));
        deleteUserBtn.setForeground(Color.WHITE);
        deleteUserBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        deleteUserBtn.setFocusPainted(false);
        deleteUserBtn.setBorderPainted(false);
        deleteUserBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        deleteUserBtn.addActionListener(e -> deleteUser());  // ✅ Button connected
        
        buttonPanel.add(addUserBtn);
        buttonPanel.add(deleteUserBtn);
        add(buttonPanel, BorderLayout.NORTH);
    }
    
    private void createUsersTable() {
        String[] columns = {"ID", "Username", "Role", "Created"};
        tableModel = new DefaultTableModel(columns, 0);
        usersTable = new JTable(tableModel);
        
        usersTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        usersTable.setRowHeight(45);
        usersTable.setGridColor(new Color(229, 231, 235));
        usersTable.setSelectionBackground(new Color(59, 130, 246));
        usersTable.setShowHorizontalLines(true);
        
        usersTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        usersTable.getTableHeader().setBackground(new Color(59, 130, 246));
        usersTable.getTableHeader().setForeground(Color.WHITE);
        usersTable.getTableHeader().setReorderingAllowed(false);
        
        JScrollPane scrollPane = new JScrollPane(usersTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 40, 40, 40));
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    // ✅ FIXED: No external dependencies - Pure demo functionality
    private void addUser() {
        JTextField usernameField = new JTextField(15);
        JPasswordField passwordField = new JPasswordField(15);  // ✅ Now works
        
        Object[] fields = {
            "New User Details:", 
            new JLabel("Username:"), usernameField,
            new JLabel("Password:"), passwordField
        };
        
        int result = JOptionPane.showConfirmDialog(this, fields, "Add User", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();  // ✅ Now works
            
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "❌ Username and password required!");
                return;
            }
            
            // ✅ Demo storage (prints to console)
            System.out.println("✅ Added user: " + username + " (password: " + password + ")");
            JOptionPane.showMessageDialog(this, "✅ User '" + username + "' added successfully!");
            
            // Add to demo table
            tableModel.addRow(new Object[]{
                tableModel.getRowCount() + 1, username, "PATIENT", "2026-03-05"
            });
        }
    }
    
    // ✅ FIXED: Pure demo delete
    private void deleteUser() {
        String username = JOptionPane.showInputDialog(this, "Enter username to delete:");
        if (username != null && !username.trim().isEmpty()) {
            String targetUsername = username.trim();
            
            // Remove from demo table
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                if (targetUsername.equalsIgnoreCase((String) tableModel.getValueAt(i, 1))) {
                    tableModel.removeRow(i);
                    JOptionPane.showMessageDialog(this, "✅ User '" + targetUsername + "' deleted!");
                    return;
                }
            }
            JOptionPane.showMessageDialog(this, "❌ User '" + targetUsername + "' not found!");
        }
    }
    
    // ✅ FIXED: Demo users only
    private void loadUsers() {
        tableModel.setRowCount(0);
        tableModel.addRow(new Object[]{1, "admin", "ADMIN", "2026-01-01"});
        tableModel.addRow(new Object[]{2, "doctor1", "DOCTOR", "2026-01-02"});
        tableModel.addRow(new Object[]{3, "patient1", "PATIENT", "2026-01-03"});
    }
    
    private void createLogoutButton() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(248, 250, 252));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 40, 40));
        
        JButton logoutBtn = new JButton("🚪 Logout");
        logoutBtn.setBackground(new Color(239, 68, 68));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBorderPainted(false);
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });
        
        buttonPanel.add(logoutBtn);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminUI().setVisible(true));
    }
}
