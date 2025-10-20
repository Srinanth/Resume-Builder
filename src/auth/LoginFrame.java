package auth;

import main.Main;
import details.DetailsFormFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginFrame extends JPanel {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private AuthService authService;
    private Main mainFrame;

    public LoginFrame(Main mainFrame) {
        this.mainFrame = mainFrame;
        this.authService = new AuthService();
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new GridBagLayout());
        setBackground(new Color(230, 240, 255)); 
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
        cardPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Login");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setForeground(new Color(60, 90, 150));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        cardPanel.add(title, gbc);

        gbc.gridwidth = 1;

        String[] labels = {"Username:", "Password:"};
        Component[] components = {
            usernameField = new JTextField(18),
            passwordField = new JPasswordField(18)
        };

        for (int i = 0; i < labels.length; i++) {
            gbc.gridy = i + 1;
            gbc.gridx = 0;
            JLabel lbl = new JLabel(labels[i]);
            lbl.setFont(new Font("SansSerif", Font.PLAIN, 14));
            cardPanel.add(lbl, gbc);

            gbc.gridx = 1;
            cardPanel.add(components[i], gbc);
        }

        gbc.gridx = 0;
        gbc.gridy = labels.length + 1;
        gbc.gridwidth = 2;

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setOpaque(false);

        JButton loginButton = new JButton("Login");
        styleButton(loginButton, new Color(60, 179, 113));
        loginButton.addActionListener(this::onLoginButtonClick);

        JButton registerButton = new JButton("Register");
        styleButton(registerButton, new Color(255, 99, 71));
        registerButton.addActionListener(e -> mainFrame.showRegisterPanel());

        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        cardPanel.add(buttonPanel, gbc);

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
        button.setPreferredSize(new Dimension(100, 35));
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private void onLoginButtonClick(ActionEvent e) {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        User user = authService.loginUser(username, password);
        if (user != null) {
            int userId = user.getUserId();
            JOptionPane.showMessageDialog(this, "Login successful!");
            mainFrame.showDetailsPanel(userId);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
