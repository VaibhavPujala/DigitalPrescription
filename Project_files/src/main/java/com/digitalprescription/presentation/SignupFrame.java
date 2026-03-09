package com.digitalprescription.presentation;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.digitalprescription.business.service.UserService;

public class SignupFrame extends JFrame {
    private final UserService userService;
    private JTextField usernameField, emailField, phoneField;
    private JPasswordField passwordField, confirmPasswordField;

    public SignupFrame() {
        this.userService = new UserService();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("🔥 Sign Up - Digital Prescription");
        setSize(450, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 10, 12, 10);

        // Title
        JLabel title = new JLabel("Create New Account", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setForeground(new Color(52, 152, 219));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        mainPanel.add(title, gbc);

        // Username
        gbc.gridwidth = 1; gbc.gridy = 1;
        gbc.gridx = 0;
        mainPanel.add(new JLabel("👤 Username:"), gbc);
        usernameField = new JTextField(18);
        gbc.gridx = 1;
        mainPanel.add(usernameField, gbc);

        // Email
        gbc.gridx = 0; gbc.gridy = 2;
        mainPanel.add(new JLabel("📧 Email:"), gbc);
        emailField = new JTextField(18);
        gbc.gridx = 1;
        mainPanel.add(emailField, gbc);

        // Phone
        gbc.gridx = 0; gbc.gridy = 3;
        mainPanel.add(new JLabel("📱 Phone:"), gbc);
        phoneField = new JTextField(18);
        gbc.gridx = 1;
        mainPanel.add(phoneField, gbc);

        // Password
        gbc.gridx = 0; gbc.gridy = 4;
        mainPanel.add(new JLabel("🔒 Password:"), gbc);
        passwordField = new JPasswordField(18);
        gbc.gridx = 1;
        mainPanel.add(passwordField, gbc);

        // Confirm Password
        gbc.gridx = 0; gbc.gridy = 5;
        mainPanel.add(new JLabel("🔐 Confirm Password:"), gbc);
        confirmPasswordField = new JPasswordField(18);
        gbc.gridx = 1;
        mainPanel.add(confirmPasswordField, gbc);

        // SIGN UP Button (LAMBDA ✅)
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        JButton signupButton = new JButton("✅ CREATE ACCOUNT");
        signupButton.setBackground(new Color(46, 204, 113));
        signupButton.setForeground(Color.WHITE);
        signupButton.setFont(new Font("Arial", Font.BOLD, 16));
        signupButton.addActionListener(e -> signupAction());  // ✅ LAMBDA FIXED
        mainPanel.add(signupButton, gbc);

        // BACK Button (LAMBDA ✅)
        gbc.gridy = 7;
        JButton backButton = new JButton("← BACK TO LOGIN");
        backButton.setBackground(new Color(149, 165, 166));
        backButton.setForeground(Color.WHITE);
        backButton.setFont(new Font("Arial", Font.BOLD, 12));
        backButton.addActionListener(e -> dispose());  // ✅ LAMBDA
        mainPanel.add(backButton, gbc);

        add(mainPanel);
    }

    // 🔥 COMPLETE signupAction with validation
    private void signupAction() {
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String confirmPassword = new String(confirmPasswordField.getPassword()).trim();

        // FULL VALIDATION
        if (username.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "❌ Fill ALL fields!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "❌ Passwords don't match!", "Error", JOptionPane.ERROR_MESSAGE);
            confirmPasswordField.setText("");
            return;
        }

        if (password.length() < 6) {
            JOptionPane.showMessageDialog(this, "❌ Password must be 6+ characters", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // FIXED: 3 parameters (matches your UserService)
        boolean saved = userService.saveUser(username, password, "patient");
        
        if (saved) {
            JOptionPane.showMessageDialog(this, 
                "✅ '" + username + "' SAVED TO MONGODB!\n📧 " + email, 
                "Success", JOptionPane.INFORMATION_MESSAGE);
            clearFields();
        } else {
            JOptionPane.showMessageDialog(this, 
                "❌ Failed to save to MongoDB", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        usernameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        passwordField.setText("");
        confirmPasswordField.setText("");
        usernameField.requestFocus();
    }

    public static void main(String[] args) {
        new SignupFrame().setVisible(true);
    }
}
