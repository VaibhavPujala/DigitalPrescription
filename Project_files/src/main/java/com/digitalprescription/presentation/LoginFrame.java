package com.digitalprescription.presentation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import org.bson.Document;

import com.digitalprescription.data.DatabaseConnection;
import com.mongodb.client.MongoCollection;
import static com.mongodb.client.model.Filters.eq;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginFrame() {
        initUI();
    }

    private void initUI() {
        setTitle("Digital Prescription - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420, 550);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        
        JPanel loginCard = createGlassCard();
        createLoginForm(loginCard);
        
        mainPanel.add(loginCard, BorderLayout.CENTER);
        add(mainPanel);
        setVisible(true);
    }

    private JPanel createGlassCard() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(new Color(255, 255, 255, 230));
        card.setPreferredSize(new Dimension(360, 400));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(59, 130, 246, 50), 1),
            BorderFactory.createEmptyBorder(50, 40, 50, 40)
        ));
        return card;
    }

    private void createLoginForm(JPanel card) {
        JLabel logo = new JLabel("🏥 Digital Prescription", SwingConstants.CENTER);
        logo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        logo.setForeground(new Color(59, 130, 246));
        card.add(logo);

        JLabel title = new JLabel("Welcome Back", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        title.setForeground(new Color(107, 114, 128));
        card.add(title);
        card.add(Box.createVerticalStrut(40));

        usernameField = createStyledTextField("👤 Username or Email");
        passwordField = createStyledPasswordField("🔒 Password");
        
        card.add(usernameField);
        card.add(Box.createVerticalStrut(20));
        card.add(passwordField);

        JButton loginBtn = createStyledButton("Login", new Color(59, 130, 246), Color.WHITE);
        loginBtn.addActionListener(e -> handleLogin());
        
        card.add(Box.createVerticalStrut(30));
        card.add(loginBtn);

        JButton signupLink = new JButton("Don't have an account? Sign up");
        signupLink.setBorderPainted(false);
        signupLink.setContentAreaFilled(false);
        signupLink.setForeground(new Color(59, 130, 246));
        signupLink.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        signupLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        signupLink.addActionListener(e -> JOptionPane.showMessageDialog(this, "Signup feature coming soon!"));
        card.add(signupLink);
    }

    private JTextField createStyledTextField(String placeholder) {
        JTextField field = new JTextField(20);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
            BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
        field.setBackground(Color.WHITE);
        field.setCaretColor(new Color(59, 130, 246));
        
        field.setForeground(Color.GRAY);
        field.setText(placeholder);
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent evt) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }
            
            @Override
            public void focusLost(FocusEvent evt) {
                if (field.getText().isEmpty()) {
                    field.setForeground(Color.GRAY);
                    field.setText(placeholder);
                }
            }
        });
        return field;
    }

    private JPasswordField createStyledPasswordField(String placeholder) {
        JPasswordField field = new JPasswordField(20);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
            BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
        field.setBackground(Color.WHITE);
        field.setCaretColor(new Color(59, 130, 246));
        
        field.setEchoChar((char) 0);
        field.setText(placeholder);
        
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent evt) {
                if (new String(field.getPassword()).equals(placeholder)) {
                    field.setText("");
                    field.setEchoChar('*');
                }
            }
            
            @Override
            public void focusLost(FocusEvent evt) {
                if (new String(field.getPassword()).isEmpty()) {
                    field.setEchoChar((char) 0);
                    field.setText(placeholder);
                }
            }
        });
        return field;
    }

    private JButton createStyledButton(String text, Color bgColor, Color fgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(280, 50));
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(new Color(
                    Math.max(0, bgColor.getRed() - 15),
                    Math.max(0, bgColor.getGreen() - 15),
                    Math.max(0, bgColor.getBlue() - 15)
                ));
            }
            
            @Override
            public void mouseExited(MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
        return button;
    }

    @SuppressWarnings("OverlyBroadCatchBlock")
    private void handleLogin() {
        String username = getFieldText(usernameField, "👤 Username or Email");
        String password = getFieldText(passwordField, "🔒 Password");
        
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "❌ Please enter username and password!");
            return;
        }
        
        try {
            MongoCollection<Document> users = DatabaseConnection.getUsers();
            Document userDoc = users.find(eq("username", username)).first();
            
            if (userDoc != null) {
                String storedPassword = userDoc.getString("password");
                String role = userDoc.getString("role");
                String name = userDoc.getString("name");
                if (name == null) name = username;
                
                if (storedPassword != null && storedPassword.equals(password)) {
                    JOptionPane.showMessageDialog(this, "✅ " + role.toUpperCase() + " login successful!");
                    dispose();
                    
                    switch (role) {
                        case "patient":
                            PatientUI patientUI = new PatientUI(name);
                            patientUI.setVisible(true);
                            break;
                        case "doctor":
                            DoctorUI doctorUI = new DoctorUI(name);
                            doctorUI.setVisible(true);
                            break;
                        case "admin":
                            AdminUI adminUI = new AdminUI();
                            adminUI.setVisible(true);
                            break;
                        default:
                            JOptionPane.showMessageDialog(this, "❌ Unknown role: " + role);
                    }
                    return;
                }
            }
            
            JOptionPane.showMessageDialog(this, "❌ Invalid credentials!", 
                "Login Failed", JOptionPane.ERROR_MESSAGE);
                
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "❌ Database error: " + e.getMessage(),
                "Connection Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String getFieldText(JTextField field, String placeholder) {
        String text = field.getText().trim();
        return text.equals(placeholder) ? "" : text;
    }

    private String getFieldText(JPasswordField field, String placeholder) {
        String text = new String(field.getPassword()).trim();
        return text.equals(placeholder) ? "" : text;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame());
    }
}
