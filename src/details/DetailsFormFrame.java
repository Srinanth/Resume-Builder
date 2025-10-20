package details;

import main.Main;
import ai.GeminiService;
import ai.ResumePreview;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class DetailsFormFrame extends JPanel {
    private JTextArea nameArea, contactArea, educationArea, experienceArea, skillsArea, projectsArea;
    private JButton generateResumeButton;
    private String userId;
    private String generatedHtml;
    private DetailsController detailsController;
    private Main mainFrame;
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private ResumePreview resumePreview;

    public DetailsFormFrame(int userId, Main mainFrame) {
        this.userId = String.valueOf(userId);
        this.mainFrame = mainFrame;
        this.detailsController = new DetailsController();
        initializeUI();
        loadUserData();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(new Color(230, 240, 255));

        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setOpaque(false);

        JPanel formPanel = createFormCard(); 
        cardPanel.add(formPanel, "form");

        resumePreview = new ResumePreview(userId);
        cardPanel.add(resumePreview, "preview");

        add(cardPanel, BorderLayout.CENTER);
        cardLayout.show(cardPanel, "form");

        setupBackButtonListener();
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(230, 240, 255));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("Resume Builder");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        titleLabel.setForeground(new Color(60, 90, 150));

        JButton logoutButton = new JButton("Logout");
        styleButton(logoutButton, new Color(255, 99, 71));
        logoutButton.addActionListener(this::onLogoutButtonClick);

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(logoutButton, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createFormCard() {
        JPanel shadowPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 0, 0, 50));
                g2.fillRoundRect(5, 5, getWidth() - 10, getHeight() - 10, 30, 30);
            }
        };
        shadowPanel.setOpaque(false);

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
        cardPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        JLabel title = new JLabel("Enter Your Details");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setForeground(new Color(60, 90, 150));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        cardPanel.add(title, gbc);
        gbc.gridwidth = 1;

        String[] labels = {"Name:", "Contact:", "Education:", "Experience:", "Skills:", "Projects:"};
        JTextArea[] areas = {
            nameArea = new JTextArea(1, 20),
            contactArea = new JTextArea(1, 20),
            educationArea = new JTextArea(4, 20),
            experienceArea = new JTextArea(4, 20),
            skillsArea = new JTextArea(4, 20),
            projectsArea = new JTextArea(4, 20)
        };

        for (int i = 0; i < labels.length; i++) {
            gbc.gridy = i + 1;
            gbc.gridx = 0;
            JLabel lbl = new JLabel(labels[i]);
            lbl.setFont(new Font("SansSerif", Font.PLAIN, 14));
            cardPanel.add(lbl, gbc);

            gbc.gridx = 1;
            areas[i].setLineWrap(true);
            areas[i].setWrapStyleWord(true);
            JScrollPane scroll = new JScrollPane(areas[i]);
            scroll.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
            cardPanel.add(scroll, gbc);
        }

        gbc.gridy = labels.length + 1;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        buttonPanel.setOpaque(false);

        generateResumeButton = new JButton("Generate Resume");
        styleButton(generateResumeButton, new Color(60, 179, 113));
        generateResumeButton.setPreferredSize(new Dimension(190, 38));
        generateResumeButton.setMargin(new Insets(6, 15, 6, 15));
        generateResumeButton.addActionListener(this::onGenerateResumeButtonClick);
        buttonPanel.add(generateResumeButton);

        JButton clearButton = new JButton("Clear Fields");
        styleButton(clearButton, new Color(100, 149, 237));
        clearButton.addActionListener(e -> clearFormData());
        buttonPanel.add(clearButton);

        cardPanel.add(buttonPanel, gbc);
        shadowPanel.add(cardPanel);
        return shadowPanel;
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

    private void onLogoutButtonClick(ActionEvent e) {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to logout?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            clearFormData();
            mainFrame.showLoginPanel();
        }
    }

    private void clearFormData() {
        JTextArea[] fields = {nameArea, contactArea, educationArea, experienceArea, skillsArea, projectsArea};
        for (JTextArea field : fields) {
            if (field != null) field.setText("");
        }
        generatedHtml = null;
    }

    private void setupBackButtonListener() {
        resumePreview.addPropertyChangeListener("backToForm", evt -> cardLayout.show(cardPanel, "form"));
    }

    public void loadUserData() {
        try {
            ResumeDetails details = detailsController.loadDetails(Integer.parseInt(userId));
            if (details != null) {
                nameArea.setText(details.getName() != null ? details.getName() : "");
                contactArea.setText(details.getContact() != null ? details.getContact() : "");
                educationArea.setText(details.getEducation() != null ? details.getEducation() : "");
                experienceArea.setText(details.getExperience() != null ? details.getExperience() : "");
                skillsArea.setText(details.getSkills() != null ? details.getSkills() : "");
                projectsArea.setText(details.getProjects() != null ? details.getProjects() : "");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading user data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private ResumeDetails collectDetails() {
        ResumeDetails details = new ResumeDetails();
        details.setName(nameArea.getText());
        details.setContact(contactArea.getText());
        details.setEducation(educationArea.getText());
        details.setExperience(experienceArea.getText());
        details.setSkills(skillsArea.getText());
        details.setProjects(projectsArea.getText());
        return details;
    }

    private boolean isInputValid(ResumeDetails details) {
        return !details.getName().trim().isEmpty() && !details.getContact().trim().isEmpty();
    }

    private void onGenerateResumeButtonClick(ActionEvent e) {
        ResumeDetails details = collectDetails();
        if (isInputValid(details)) {
            try {
                int currentUserId = Integer.parseInt(userId);
                boolean saveSuccess = detailsController.saveDetails(currentUserId, details);
                if (saveSuccess) {
                    loadUserData();
                    GeminiService geminiService = new GeminiService();
                    generatedHtml = geminiService.generateResumeHTML(details, "professional");
                    resumePreview.setHTMLContent(generatedHtml);
                    cardLayout.show(cardPanel, "preview");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to save resume data.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error generating resume: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please fill in Name and Contact fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
