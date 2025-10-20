package auth;

import main.Main;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class RegisterFrame extends JPanel {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private AuthService authService;
    private Main mainFrame;

    public RegisterFrame(Main mainFrame) {
        this.mainFrame = mainFrame;
        this.authService = new AuthService();
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new GridBagLayout());
        setBackground(new Color(230, 240, 255));

        // Card panel
        JPanel cardPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 240));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            }
        };
        cardPanel.setOpaque(false);
        cardPanel.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel title = new JLabel("Register");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setForeground(new Color(60, 90, 150));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        cardPanel.add(title, gbc);
        gbc.gridwidth = 1;

        // Username
        gbc.gridy = 1;
        gbc.gridx = 0;
        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        cardPanel.add(userLabel, gbc);

        gbc.gridx = 1;
        usernameField = new JTextField(18);
        cardPanel.add(usernameField, gbc);

        // Password
        gbc.gridy = 2;
        gbc.gridx = 0;
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        cardPanel.add(passLabel, gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField(18);
        cardPanel.add(passwordField, gbc);

        // Spacer to push buttons down
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.weighty = 1;
        cardPanel.add(Box.createVerticalStrut(20), gbc);

        // Buttons Panel
        gbc.gridy = 4;
        gbc.weighty = 0;
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 10, 10)); // 2 rows, better spacing
        buttonPanel.setOpaque(false);

        JButton registerButton = new JButton("Register");
        styleButton(registerButton, new Color(60, 179, 113));
        registerButton.addActionListener(this::onRegisterButtonClick);
        buttonPanel.add(registerButton);

        JButton backButton = new JButton("Back to Login");
        styleButton(backButton, new Color(255, 99, 71));
        backButton.addActionListener(e -> mainFrame.showLoginPanel());
        buttonPanel.add(backButton);

        cardPanel.add(buttonPanel, gbc);

        // Shadow layer
        JPanel shadowPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 0, 0, 50));
                g2.fillRoundRect(5, 5, getWidth() - 10, getHeight() - 10, 30, 30);
            }
        };
        shadowPanel.setLayout(new GridBagLayout());
        shadowPanel.setOpaque(false);
        shadowPanel.add(cardPanel);

        add(shadowPanel);
    }

    private void styleButton(JButton button, Color bgColor) {
        button.setFocusPainted(false);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(160, 35));
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private void onRegisterButtonClick(ActionEvent e) {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        boolean success = authService.registerUser(username, password, "user");
        if (success) {
            JOptionPane.showMessageDialog(this, "Registration successful! Please login.");
            mainFrame.showLoginPanel();
        } else {
            JOptionPane.showMessageDialog(this, "Registration failed!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
