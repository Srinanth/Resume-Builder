package main;

import auth.LoginFrame;
import auth.RegisterFrame;
import details.DetailsFormFrame;
import javax.swing.*;
import java.awt.*;

public class Main {
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private DetailsFormFrame detailsPanel;

    public Main() {
        initializeUI();
    }

    private void initializeUI() {
        JFrame frame = new JFrame("Resume Builder");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(400, 300));

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        LoginFrame loginPanel = new LoginFrame(this); 
        cardPanel.add(loginPanel, "login");

        RegisterFrame registerPanel = new RegisterFrame(this); 
        cardPanel.add(registerPanel, "register");

        detailsPanel = null;
        cardPanel.add(new JPanel(), "details"); 

        frame.add(cardPanel, BorderLayout.CENTER);

        cardLayout.show(cardPanel, "login");

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void showDetailsPanel(int userId) {
        if (detailsPanel != null) {
            cardPanel.remove(detailsPanel);
        }
        
        detailsPanel = new DetailsFormFrame(userId, this);
        cardPanel.add(detailsPanel, "details");
        
        cardLayout.show(cardPanel, "details");
        
        cardPanel.revalidate();
        cardPanel.repaint();
    }

    public void showLoginPanel() {
        cardLayout.show(cardPanel, "login");
    }

    public void showRegisterPanel() {
        cardLayout.show(cardPanel, "register");
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            new Main();
        });
    }
}