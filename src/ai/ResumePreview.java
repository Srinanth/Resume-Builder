package ai;

import pdf.GeminiApiClient;
import pdf.HtmlToPdfConverter;
import pdf.ResumeFeedbackAndRating;
import javax.swing.*;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ResumePreview extends JPanel {
    private JEditorPane previewPane;
    private JScrollPane scrollPane;
    private String userId;
    private JButton backButton; 

    public ResumePreview(String userId) {
        this.userId = userId;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(new Color(230, 230, 250));

        previewPane = new JEditorPane();
        previewPane.setContentType("text/html");
        previewPane.setEditable(false);

        HTMLEditorKit kit = new HTMLEditorKit();
        StyleSheet styleSheet = kit.getStyleSheet();
        styleSheet.addRule("body { font-family: Arial, sans-serif; margin: 20px; }");
        styleSheet.addRule("h1 { color: #2c3e50; }");
        styleSheet.addRule("h2 { color: #34495e; border-bottom: 1px solid #bdc3c7; padding-bottom: 5px; }");
        previewPane.setEditorKit(kit);

        scrollPane = new JScrollPane(previewPane);
        add(scrollPane, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel();
        backButton = new JButton("Back to Form");
        backButton.addActionListener(this::onBackButtonClick);
        buttonPanel.add(backButton);
        
        JButton savePdfButton = new JButton("Save as PDF");
        savePdfButton.addActionListener(this::onSaveAsPdfButtonClick);
        buttonPanel.add(savePdfButton);

        JButton reviewRatingButton = new JButton("Review Rating");
        reviewRatingButton.addActionListener(this::onReviewRatingButtonClick);
        buttonPanel.add(reviewRatingButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void setHTMLContent(String htmlContent) {
        previewPane.setText(htmlContent);
        previewPane.setCaretPosition(0);
    }

    public void setBackButtonListener(ActionListener listener) {
        backButton.addActionListener(listener);
    }

    private void onBackButtonClick(ActionEvent e) {
        firePropertyChange("backToForm", false, true);
    }

    private void onSaveAsPdfButtonClick(ActionEvent e) {
        HtmlToPdfConverter converter = new HtmlToPdfConverter();
        try {
            String pdfPath = converter.convertToPdf(previewPane.getText(), "resume_" + userId + ".pdf");
            JOptionPane.showMessageDialog(this, "PDF saved at: " + pdfPath);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error saving PDF: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void onReviewRatingButtonClick(ActionEvent e) {
        String resumeHtml = previewPane.getText();
        GeminiApiClient geminiClient = new GeminiApiClient();
        try {
            ResumeFeedbackAndRating feedback = geminiClient.analyzeResume(resumeHtml);
            StringBuilder message = new StringBuilder();
            message.append("<html>Resume Analysis:<br><br>");
            message.append("Rating: ").append(String.format("%.1f", feedback.getRating())).append("/10<br>");
            if (feedback.getSuggestions() != null && !feedback.getSuggestions().isEmpty()) {
                message.append("Suggestions:<ul>");
                for (String suggestion : feedback.getSuggestions()) {
                    message.append("<li>").append(suggestion).append("</li>");
                }
                message.append("</ul>");
            }
            if (feedback.getWeakAreas() != null && !feedback.getWeakAreas().isEmpty()) {
                message.append("Weak Areas:<ul>");
                for (String weakArea : feedback.getWeakAreas()) {
                    message.append("<li>").append(weakArea).append("</li>");
                }
                message.append("</ul>");
            }
            if (feedback.getCareerAdvice() != null && !feedback.getCareerAdvice().isEmpty()) {
                message.append("Career Advice:<ul>");
                for (String advice : feedback.getCareerAdvice()) {
                    message.append("<li>").append(advice).append("</li>");
                }
                message.append("</ul>");
            }
            message.append("</html>");

            JOptionPane.showMessageDialog(this, message.toString(), "Resume Feedback", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error analyzing resume: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}